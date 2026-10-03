package com.betterlanbroadcaster;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.StandardProtocolFamily;
import java.net.StandardSocketOptions;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

public final class MulticastBroadcaster {

    public static final String MULTICAST_ADDRESS = "224.0.2.60";
    public static final int MULTICAST_PORT = 4445;
    public static final int MULTICAST_TTL = 1;

    /* Transport safeguard; this is not a Minecraft protocol-mandated maximum. */
    private static final int MAX_PACKET_SIZE = 1400;
    private static final long MIN_DELAY_MS = 50L;
    private static final long MAX_DELAY_MS = 86_400_000L;
    private static final long DEBUG_LOG_INTERVAL_NANOS = TimeUnit.SECONDS.toNanos(5);
    private static final long FAILURE_LOG_INTERVAL_NANOS = TimeUnit.SECONDS.toNanos(10);
    private static final long INTERFACE_REFRESH_INTERVAL_NANOS = TimeUnit.SECONDS.toNanos(5);

    private final BetterLANBroadcaster plugin;
    private final ScheduledExecutorService scheduler;
    private final InetAddress multicastAddress;
    private final InetSocketAddress multicastDestination;

    private List<InterfaceSocket> sockets = List.of();
    private ScheduledFuture<?> broadcastTask;

    private String configuredMotd = "A Minecraft Server";
    private int advertisedPort;
    private long delayMs = 1500L;
    private String configuredInterface = "auto";
    private List<String> excludedInterfaces = List.of();

    private volatile boolean running;
    private volatile boolean shutdown;
    private volatile boolean debug;

    private volatile long cycles;
    private volatile long broadcastsSent;
    private volatile long broadcastFailures;
    private volatile long lastBroadcastEpochMillis;

    private int cachedOnline = Integer.MIN_VALUE;
    private int cachedMax = Integer.MIN_VALUE;
    private int cachedPort = Integer.MIN_VALUE;
    private String cachedMotd;
    private byte[] cachedPayload;

    private long nextDebugLogNanos;
    private long nextFailureLogNanos;
    private long nextInterfaceRefreshNanos;

    public MulticastBroadcaster(BetterLANBroadcaster plugin) {
        this.plugin = plugin;
        try {
            this.multicastAddress = InetAddress.getByName(MULTICAST_ADDRESS);
        } catch (IOException exception) {
            throw new IllegalStateException(
                    "Unable to resolve the fixed Minecraft LAN multicast address.", exception
            );
        }
        this.multicastDestination = new InetSocketAddress(multicastAddress, MULTICAST_PORT);
        ThreadFactory threadFactory = task -> {
            Thread thread = new Thread(task, "BetterLANBroadcaster");
            thread.setDaemon(true);
            return thread;
        };
        ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(1, threadFactory);
        executor.setRemoveOnCancelPolicy(true);
        this.scheduler = executor;
    }

    public synchronized void start(
            String motd,
            int port,
            long delayMs,
            String networkInterface,
            List<String> networkInterfaceExcludes,
            boolean debug
    ) throws IOException {
        ensureNotShutdown();
        if (running) {
            return;
        }
        validateAdvertisedPort(port);

        List<InterfaceSocket> created = createSockets(networkInterface, networkInterfaceExcludes);
        if (created.isEmpty()) {
            throw new IOException("No usable IPv4 multicast interfaces were found.");
        }

        applyConfiguration(motd, port, delayMs, networkInterface, networkInterfaceExcludes, debug);
        sockets = List.copyOf(created);
        running = true;
        scheduleBroadcastTask();
    }

    public synchronized void reconfigure(
            String motd,
            int port,
            long delayMs,
            String networkInterface,
            List<String> networkInterfaceExcludes,
            boolean debug
    ) throws IOException {
        ensureNotShutdown();

        if (!running) {
            validateAdvertisedPort(port);
            applyConfiguration(motd, port, delayMs, networkInterface, networkInterfaceExcludes, debug);
            return;
        }

        validateAdvertisedPort(port);

        String normalizedInterface = normalizeInterface(networkInterface);
        List<String> normalizedExcludes = normalizeExcludes(networkInterfaceExcludes);
        boolean networkConfigurationChanged = !configuredInterface.equalsIgnoreCase(normalizedInterface)
                || !sameIgnoreCase(excludedInterfaces, normalizedExcludes);
        long normalizedDelay = normalizeDelay(delayMs);
        boolean delayChanged = this.delayMs != normalizedDelay;

        List<InterfaceSocket> replacement = null;
        if (networkConfigurationChanged) {
            replacement = createSockets(normalizedInterface, normalizedExcludes);
            if (replacement.isEmpty()) {
                throw new IOException("The new network configuration has no usable multicast interfaces.");
            }
        }

        List<InterfaceSocket> previousSockets = sockets;
        applyConfiguration(motd, port, normalizedDelay, normalizedInterface, normalizedExcludes, debug);

        if (replacement != null) {
            sockets = List.copyOf(replacement);
            closeSockets(previousSockets);
        }

        if (delayChanged) {
            scheduleBroadcastTask();
        }
    }

    public synchronized void stop() {
        if (!running) {
            return;
        }

        running = false;
        cancelBroadcastTask();

        List<InterfaceSocket> previousSockets = sockets;
        sockets = List.of();
        closeSockets(previousSockets);
    }

    public synchronized void shutdown() {
        if (shutdown) {
            return;
        }

        shutdown = true;
        running = false;
        cancelBroadcastTask();

        List<InterfaceSocket> previousSockets = sockets;
        sockets = List.of();
        closeSockets(previousSockets);
        scheduler.shutdownNow();
    }

    public boolean isRunning() {
        return running;
    }

    public synchronized int getPort() {
        return advertisedPort;
    }

    public synchronized long getDelayMs() {
        return delayMs;
    }

    public synchronized String getConfiguredInterface() {
        return configuredInterface;
    }

    public synchronized int getActiveInterfaceCount() {
        return sockets.size();
    }

    public synchronized List<InterfaceInfo> getActiveInterfaces() {
        List<InterfaceInfo> result = new ArrayList<>(sockets.size());
        for (InterfaceSocket socket : sockets) {
            result.add(new InterfaceInfo(
                    socket.networkInterface.getName(),
                    socket.networkInterface.getDisplayName(),
                    socket.ipv4.getHostAddress(),
                    socket.networkInterface.isVirtual(),
                    socket.pointToPoint
            ));
        }
        return List.copyOf(result);
    }

    public long getCycles() {
        return cycles;
    }

    public long getBroadcastsSent() {
        return broadcastsSent;
    }

    public long getBroadcastFailures() {
        return broadcastFailures;
    }

    public long getLastBroadcastEpochMillis() {
        return lastBroadcastEpochMillis;
    }

    public synchronized void setDebug(boolean debug) {
        this.debug = debug;
    }

    private void ensureNotShutdown() throws IOException {
        if (shutdown) {
            throw new IOException("Multicast broadcaster is already shut down.");
        }
    }

    private void validateAdvertisedPort(int port) throws IOException {
        if (port < 1 || port > 65535) {
            throw new IOException("The advertised port must be between 1 and 65535.");
        }
    }

    private void applyConfiguration(
            String motd,
            int port,
            long delayMs,
            String networkInterface,
            List<String> networkInterfaceExcludes,
            boolean debug
    ) {
        this.configuredMotd = motd == null ? "" : motd;
        this.advertisedPort = port;
        this.delayMs = normalizeDelay(delayMs);
        this.configuredInterface = normalizeInterface(networkInterface);
        this.excludedInterfaces = normalizeExcludes(networkInterfaceExcludes);
        this.debug = debug;
        invalidatePayloadCache();
    }

    private long normalizeDelay(long value) {
        return Math.max(MIN_DELAY_MS, Math.min(MAX_DELAY_MS, value));
    }

    private void scheduleBroadcastTask() {
        cancelBroadcastTask();
        broadcastTask = scheduler.scheduleAtFixedRate(
                this::broadcastSafely,
                0L,
                delayMs,
                TimeUnit.MILLISECONDS
        );
    }

    private void cancelBroadcastTask() {
        if (broadcastTask != null) {
            broadcastTask.cancel(false);
            broadcastTask = null;
        }
    }

    private List<InterfaceSocket> createSockets(String networkInterface, List<String> excludes) throws IOException {
        List<NetworkInterface> interfaces = resolveInterfaces(networkInterface, excludes);
        if (interfaces.isEmpty()) {
            throw new IOException("No multicast-compatible IPv4 interfaces were found.");
        }

        List<InterfaceSocket> created = new ArrayList<>(interfaces.size());
        for (NetworkInterface networkInterfaceEntry : interfaces) {
            try {
                InterfaceSocket socket = createSocket(networkInterfaceEntry);
                created.add(socket);
                logDebug(
                        "Active multicast interface: {} [{}] IPv4={} virtual={} pointToPoint={}",
                        networkInterfaceEntry.getDisplayName(),
                        networkInterfaceEntry.getName(),
                        socket.ipv4.getHostAddress(),
                        networkInterfaceEntry.isVirtual(),
                        socket.pointToPoint
                );
            } catch (IOException exception) {
                logInterfaceFailure(networkInterfaceEntry, exception);
            } catch (RuntimeException exception) {
                logInterfaceFailure(networkInterfaceEntry, exception);
            }
        }

        if (created.isEmpty()) {
            return List.of();
        }
        return created;
    }

    private List<NetworkInterface> resolveInterfaces(String configured, List<String> excludes) throws IOException {
        String normalized = normalizeInterface(configured);
        List<String> normalizedExcludes = normalizeExcludes(excludes);

        if ("auto".equalsIgnoreCase(normalized)) {
            return findAllSuitableInterfaces(normalizedExcludes);
        }

        NetworkInterface selected = findConfiguredInterface(normalized);
        if (selected == null) {
            throw new IOException("Network interface not found: " + normalized);
        }
        if (isExcluded(selected, normalizedExcludes)) {
            throw new IOException("Configured network interface is excluded: " + normalized);
        }
        if (!isSuitableInterface(selected)) {
            throw new IOException("Selected network interface is not suitable for IPv4 multicast: " + normalized);
        }

        return List.of(selected);
    }

    private List<NetworkInterface> findAllSuitableInterfaces(List<String> excludes) throws IOException {
        Enumeration<NetworkInterface> enumeration = NetworkInterface.getNetworkInterfaces();
        if (enumeration == null) {
            return List.of();
        }

        List<NetworkInterface> result = new ArrayList<>();
        Set<String> seenNames = new HashSet<>();

        while (enumeration.hasMoreElements()) {
            NetworkInterface networkInterface = enumeration.nextElement();
            String name = networkInterface.getName();
            if (name == null || !seenNames.add(name.toLowerCase(Locale.ROOT))) {
                continue;
            }

            if (!isSuitableInterface(networkInterface)) {
                continue;
            }
            if (isExcluded(networkInterface, excludes)) {
                logDebug("Skipping excluded interface: {} [{}]", networkInterface.getDisplayName(), name);
                continue;
            }
            result.add(networkInterface);
        }

        return result;
    }

    private boolean isSuitableInterface(NetworkInterface networkInterface) {
        try {
            return networkInterface.isUp()
                    && !networkInterface.isLoopback()
                    && networkInterface.supportsMulticast()
                    && findIPv4Address(networkInterface) != null;
        } catch (IOException exception) {
            return false;
        }
    }

    private boolean isExcluded(NetworkInterface networkInterface, List<String> excludes) {
        if (excludes.isEmpty()) {
            return false;
        }

        String name = networkInterface.getName();
        String displayName = networkInterface.getDisplayName();
        InetAddress ipv4 = findIPv4Address(networkInterface);
        String address = ipv4 == null ? "" : ipv4.getHostAddress();

        for (String excluded : excludes) {
            if (equalsIgnoreCase(excluded, name)
                    || equalsIgnoreCase(excluded, displayName)
                    || equalsIgnoreCase(excluded, address)) {
                return true;
            }
        }
        return false;
    }

    private NetworkInterface findConfiguredInterface(String configured) throws IOException {
        Enumeration<NetworkInterface> enumeration = NetworkInterface.getNetworkInterfaces();
        if (enumeration == null) {
            return null;
        }

        while (enumeration.hasMoreElements()) {
            NetworkInterface networkInterface = enumeration.nextElement();
            String name = networkInterface.getName();
            String displayName = networkInterface.getDisplayName();

            if (equalsIgnoreCase(configured, name) || equalsIgnoreCase(configured, displayName)) {
                return networkInterface;
            }

            InetAddress ipv4 = findIPv4Address(networkInterface);
            if (ipv4 != null && equalsIgnoreCase(configured, ipv4.getHostAddress())) {
                return networkInterface;
            }
        }
        return null;
    }

    private InetAddress findIPv4Address(NetworkInterface networkInterface) {
        Enumeration<InetAddress> addresses = networkInterface.getInetAddresses();
        while (addresses.hasMoreElements()) {
            InetAddress address = addresses.nextElement();
            if (address instanceof Inet4Address && !address.isLoopbackAddress()) {
                return address;
            }
        }
        return null;
    }

    private InterfaceSocket createSocket(NetworkInterface networkInterface) throws IOException {
        InetAddress ipv4 = findIPv4Address(networkInterface);
        if (ipv4 == null) {
            throw new IOException("The network interface does not have an IPv4 address.");
        }

        DatagramChannel channel = null;
        try {
            channel = DatagramChannel.open(StandardProtocolFamily.INET);
            channel.setOption(StandardSocketOptions.IP_MULTICAST_TTL, MULTICAST_TTL);
            channel.setOption(StandardSocketOptions.IP_MULTICAST_IF, networkInterface);

            // Sender sockets do not join the multicast group and do not need to bind to it.
            // Binding to the wildcard address keeps a DHCP/VPN address change less fragile;
            // IP_MULTICAST_IF still controls which interface carries the datagram.
            channel.bind(new InetSocketAddress(0));

            boolean pointToPoint = networkInterface.isPointToPoint();
            return new InterfaceSocket(networkInterface, ipv4, channel, pointToPoint);
        } catch (IOException | RuntimeException exception) {
            if (channel != null) {
                try {
                    channel.close();
                } catch (IOException ignored) {
                }
            }
            if (exception instanceof IOException ioException) {
                throw ioException;
            }
            throw new IOException("Could not configure multicast socket for "
                    + networkInterface.getName() + ".", exception);
        }
    }

    private void broadcastSafely() {
        synchronized (this) {
            if (!running || sockets.isEmpty()) {
                return;
            }

            cycles++;
            boolean failedThisCycle = false;

            try {
                int online = plugin.getServer().getPlayerCount();
                int max = plugin.getMaxPlayers();
                byte[] data = getPayload(online, max);
                ByteBuffer buffer = ByteBuffer.wrap(data);
                boolean sentAtLeastOnce = false;

                for (InterfaceSocket socket : sockets) {
                    try {
                        buffer.rewind();
                        int written = socket.channel.send(buffer, multicastDestination);
                        if (written != data.length) {
                            throw new IOException("Only " + written + " of " + data.length + " bytes were sent.");
                        }
                        broadcastsSent++;
                        sentAtLeastOnce = true;
                    } catch (IOException | RuntimeException exception) {
                        broadcastFailures++;
                        failedThisCycle = true;
                        logInterfaceFailure(socket.networkInterface, exception);
                    }
                }

                if (sentAtLeastOnce) {
                    lastBroadcastEpochMillis = System.currentTimeMillis();
                }

                if (failedThisCycle) {
                    maybeRefreshInterfacesAfterFailure();
                }
                maybeLogDebugSummary(data.length, online, max);
            } catch (RuntimeException exception) {
                broadcastFailures++;
                failedThisCycle = true;
                logRateLimitedFailure("Unexpected LAN broadcast failure.", exception);
                if (failedThisCycle) {
                    maybeRefreshInterfacesAfterFailure();
                }
            }
        }
    }

    private byte[] getPayload(int online, int max) {
        if (cachedPayload != null
                && cachedOnline == online
                && cachedMax == max
                && cachedPort == advertisedPort
                && Objects.equals(cachedMotd, configuredMotd)) {
            return cachedPayload;
        }

        String motd = configuredMotd == null ? "" : configuredMotd;
        motd = motd.replace("{online}", Integer.toString(online))
                .replace("{max}", Integer.toString(max));
        motd = plugin.formatMiniMessage(motd);
        motd = sanitizeMotd(motd);

        String suffix = "[/MOTD][AD]" + advertisedPort + "[/AD]";
        int availableMotdBytes = MAX_PACKET_SIZE
                - "[MOTD]".getBytes(StandardCharsets.UTF_8).length
                - suffix.getBytes(StandardCharsets.UTF_8).length;
        motd = truncateUtf8(motd, Math.max(0, availableMotdBytes));

        String payload = "[MOTD]" + motd + suffix;
        cachedPayload = payload.getBytes(StandardCharsets.UTF_8);
        cachedOnline = online;
        cachedMax = max;
        cachedPort = advertisedPort;
        cachedMotd = configuredMotd;
        return cachedPayload;
    }

    private String sanitizeMotd(String text) {
        if (text == null || text.isEmpty()) {
            return "";
        }

        StringBuilder result = new StringBuilder(text.length());
        for (int index = 0; index < text.length();) {
            char current = text.charAt(index);

            if (current == '[') {
                if (regionMatchesIgnoreCase(text, index, "[MOTD]")
                        || regionMatchesIgnoreCase(text, index, "[/MOTD]")
                        || regionMatchesIgnoreCase(text, index, "[AD]")
                        || regionMatchesIgnoreCase(text, index, "[/AD]")) {
                    index = skipProtocolToken(text, index);
                    continue;
                }
            }

            if (current < 0x20 || current == 0x7F) {
                index++;
                continue;
            }

            result.append(current);
            index++;
        }
        return result.toString();
    }

    private int skipProtocolToken(String text, int index) {
        int end = text.indexOf(']', index);
        return end >= 0 ? end + 1 : index + 1;
    }

    private boolean regionMatchesIgnoreCase(String text, int offset, String token) {
        return offset + token.length() <= text.length()
                && text.regionMatches(true, offset, token, 0, token.length());
    }

    private String truncateUtf8(String value, int maxBytes) {
        if (value == null || value.isEmpty() || maxBytes <= 0) {
            return maxBytes <= 0 ? "" : value;
        }

        if (utf8Size(value) <= maxBytes) {
            return value;
        }

        StringBuilder result = new StringBuilder(value.length());
        int bytes = 0;
        for (int index = 0; index < value.length();) {
            int codePoint = value.codePointAt(index);
            int charCount = Character.charCount(codePoint);
            int codePointBytes = utf8Size(codePoint);
            if (bytes + codePointBytes > maxBytes) {
                break;
            }
            result.appendCodePoint(codePoint);
            bytes += codePointBytes;
            index += charCount;
        }
        return result.toString();
    }

    private int utf8Size(String value) {
        return value.getBytes(StandardCharsets.UTF_8).length;
    }

    private int utf8Size(int codePoint) {
        if (codePoint <= 0x7F) {
            return 1;
        }
        if (codePoint <= 0x7FF) {
            return 2;
        }
        if (codePoint <= 0xFFFF) {
            return 3;
        }
        return 4;
    }

    private void maybeRefreshInterfacesAfterFailure() {
        if (!running || System.nanoTime() < nextInterfaceRefreshNanos) {
            return;
        }

        nextInterfaceRefreshNanos = System.nanoTime() + INTERFACE_REFRESH_INTERVAL_NANOS;

        try {
            List<InterfaceSocket> replacement = createSockets(configuredInterface, excludedInterfaces);
            if (!replacement.isEmpty()) {
                List<InterfaceSocket> previous = sockets;
                sockets = List.copyOf(replacement);
                closeSockets(previous);
            }
        } catch (IOException exception) {
            logRateLimitedFailure("Multicast interface refresh failed.", exception);
        }
    }

    private void maybeLogDebugSummary(int payloadBytes, int online, int max) {
        if (!debug) {
            return;
        }

        long now = System.nanoTime();
        if (now < nextDebugLogNanos) {
            return;
        }
        nextDebugLogNanos = now + DEBUG_LOG_INTERVAL_NANOS;

        plugin.getLogger().info(
                "[DEBUG] LAN multicast: {} interface(s), {} bytes, {} online/{} max, {} packet(s) sent, {} failure(s), group={}:{}, TTL={}",
                sockets.size(),
                payloadBytes,
                online,
                max,
                broadcastsSent,
                broadcastFailures,
                MULTICAST_ADDRESS,
                MULTICAST_PORT,
                MULTICAST_TTL
        );
    }

    private void logInterfaceFailure(NetworkInterface networkInterface, Throwable exception) {
        long now = System.nanoTime();
        if (now < nextFailureLogNanos) {
            return;
        }

        nextFailureLogNanos = now + FAILURE_LOG_INTERVAL_NANOS;
        plugin.getLogger().warn(
                "LAN multicast send/setup failed on interface {} [{}]: {}",
                networkInterface.getDisplayName(),
                networkInterface.getName(),
                exception.getMessage()
        );
    }

    private void logRateLimitedFailure(String message, Throwable exception) {
        long now = System.nanoTime();
        if (now < nextFailureLogNanos) {
            return;
        }
        nextFailureLogNanos = now + FAILURE_LOG_INTERVAL_NANOS;
        plugin.getLogger().warn(message, exception);
    }

    private void logDebug(String message, Object... arguments) {
        if (debug) {
            plugin.getLogger().debug(message, arguments);
        }
    }

    private void invalidatePayloadCache() {
        cachedOnline = Integer.MIN_VALUE;
        cachedMax = Integer.MIN_VALUE;
        cachedPort = Integer.MIN_VALUE;
        cachedMotd = null;
        cachedPayload = null;
    }

    private void closeSockets(List<InterfaceSocket> targets) {
        for (InterfaceSocket socket : targets) {
            try {
                socket.channel.close();
            } catch (IOException ignored) {
            }
        }
    }

    private String normalizeInterface(String value) {
        if (value == null || value.isBlank()) {
            return "auto";
        }
        return value.trim();
    }

    private List<String> normalizeExcludes(List<String> values) {
        if (values == null || values.isEmpty()) {
            return List.of();
        }

        List<String> result = new ArrayList<>();
        for (String value : values) {
            if (value == null || value.isBlank()) {
                continue;
            }

            String normalized = value.trim();
            boolean duplicate = false;
            for (String existing : result) {
                if (existing.equalsIgnoreCase(normalized)) {
                    duplicate = true;
                    break;
                }
            }
            if (!duplicate) {
                result.add(normalized);
            }
        }
        return List.copyOf(result);
    }

    private boolean sameIgnoreCase(List<String> left, List<String> right) {
        if (left.size() != right.size()) {
            return false;
        }
        for (int i = 0; i < left.size(); i++) {
            if (!left.get(i).equalsIgnoreCase(right.get(i))) {
                return false;
            }
        }
        return true;
    }

    private boolean equalsIgnoreCase(String left, String right) {
        return left != null && right != null && left.equalsIgnoreCase(right);
    }

    public record InterfaceInfo(
            String name,
            String displayName,
            String ipv4,
            boolean virtual,
            boolean pointToPoint
    ) {
    }

    private record InterfaceSocket(
            NetworkInterface networkInterface,
            InetAddress ipv4,
            DatagramChannel channel,
            boolean pointToPoint
    ) {
    }
}
