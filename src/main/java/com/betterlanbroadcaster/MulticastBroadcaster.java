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
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class MulticastBroadcaster {

    private static final String MULTICAST_ADDRESS = "224.0.2.60";
    private static final int MULTICAST_PORT = 4445;
    private static final int MULTICAST_TTL = 1;
    private static final int MAX_PACKET_SIZE = 1400;

    private final BetterLANBroadcaster plugin;
    private final String configuredMotd;
    private final int advertisedPort;
    private final long delayMs;
    private final String configuredInterface;

    private final ScheduledExecutorService scheduler;
    private final List<InterfaceSocket> sockets = new ArrayList<>();

    private ScheduledFuture<?> broadcastTask;

    private volatile boolean running;
    private volatile boolean shutdown;
    private volatile boolean debug;

    public MulticastBroadcaster(BetterLANBroadcaster plugin, String motd, int port, long delayMs, String networkInterface) {
        this.plugin = plugin;
        this.configuredMotd = motd;
        this.advertisedPort = port;
        this.delayMs = delayMs;
        this.configuredInterface = networkInterface == null ? "auto" : networkInterface;

        this.scheduler = Executors.newSingleThreadScheduledExecutor(task -> {
            Thread thread = new Thread(task, "BetterLANBroadcaster");
            thread.setDaemon(true);
            return thread;
        });
    }

    public synchronized void start() throws IOException {
        if (shutdown) throw new IOException("Multicast broadcaster is already shut down.");
        if (running) return;

        closeSockets();

        InetAddress multicastAddress = InetAddress.getByName(MULTICAST_ADDRESS);
        if (!multicastAddress.isMulticastAddress()) {
            throw new IOException("Invalid multicast address: " + MULTICAST_ADDRESS);
        }

        List<NetworkInterface> interfaces = resolveInterfaces();
        if (interfaces.isEmpty()) {
            throw new IOException("No multicast-compatible network interfaces were found.");
        }

        int successful = 0;
        for (NetworkInterface networkInterface : interfaces) {
            try {
                InterfaceSocket interfaceSocket = createSocket(networkInterface, multicastAddress);
                sockets.add(interfaceSocket);
                successful++;

                logDebug("Active multicast interface: " + networkInterface.getDisplayName() 
                        + " [" + networkInterface.getName() + "] IPv4=" + interfaceSocket.ipv4.getHostAddress());
            } catch (Exception exception) {
                logDebug("Could not use interface " + networkInterface.getDisplayName() 
                        + " [" + networkInterface.getName() + "]: " + exception.getMessage());
            }
        }

        if (successful == 0) {
            closeSockets();
            throw new IOException("None of the discovered network interfaces could be configured for multicast.");
        }

        running = true;
        long interval = Math.max(50L, delayMs);

        broadcastTask = scheduler.scheduleAtFixedRate(
                this::broadcastSafely,
                0L,
                interval,
                TimeUnit.MILLISECONDS
        );

        plugin.getLogger().info("Multicast broadcaster started: " + MULTICAST_ADDRESS + ":" + MULTICAST_PORT + " using " + successful + " interface(s).");
    }

    public synchronized void stop() {
        if (!running) {
            closeSockets();
            return;
        }

        running = false;
        if (broadcastTask != null) {
            broadcastTask.cancel(false);
            broadcastTask = null;
        }

        closeSockets();
    }

    public synchronized void shutdown() {
        if (shutdown) return;
        shutdown = true;
        stop();
        scheduler.shutdownNow();
    }

    public boolean isRunning() { return running; }
    public int getPort() { return advertisedPort; }
    public void setDebug(boolean debug) { this.debug = debug; }

    private List<NetworkInterface> resolveInterfaces() throws IOException {
        String configured = configuredInterface == null ? "auto" : configuredInterface.trim();

        if (configured.isEmpty() || configured.equalsIgnoreCase("auto")) {
            return findAllSuitableInterfaces();
        }

        NetworkInterface selected = findConfiguredInterface(configured);
        if (selected == null) {
            throw new IOException("Network interface not found: " + configured);
        }

        if (!isSuitableInterface(selected)) {
            throw new IOException("Selected network interface is not suitable for multicast: " + configured);
        }

        return List.of(selected);
    }

    private List<NetworkInterface> findAllSuitableInterfaces() throws IOException {
        List<NetworkInterface> result = new ArrayList<>();
        Enumeration<NetworkInterface> enumeration = NetworkInterface.getNetworkInterfaces();

        if (enumeration == null) return result;

        while (enumeration.hasMoreElements()) {
            NetworkInterface networkInterface = enumeration.nextElement();
            String name = networkInterface.getName();
            String displayName = networkInterface.getDisplayName();

            try {
                logDebug("Discovered interface: " + displayName + " [" + name + "]");

                if (!networkInterface.isUp() || networkInterface.isLoopback() || !networkInterface.supportsMulticast()) {
                    continue;
                }

                InetAddress ipv4 = findIPv4Address(networkInterface);
                if (ipv4 == null) {
                    continue;
                }

                logDebug("Multicast candidate: " + displayName + " [" + name + "] IPv4=" + ipv4.getHostAddress());
                result.add(networkInterface);

            } catch (Exception exception) {
                logDebug("Ignored interface " + displayName + ": " + exception.getMessage());
            }
        }

        return result;
    }

    private boolean isSuitableInterface(NetworkInterface networkInterface) {
        try {
            return networkInterface.isUp() 
                    && !networkInterface.isLoopback() 
                    && networkInterface.supportsMulticast() 
                    && findIPv4Address(networkInterface) != null;
        } catch (Exception exception) {
            return false;
        }
    }

    private NetworkInterface findConfiguredInterface(String configured) throws IOException {
        String normalized = configured.trim();
        Enumeration<NetworkInterface> enumeration = NetworkInterface.getNetworkInterfaces();

        if (enumeration == null) return null;

        while (enumeration.hasMoreElements()) {
            NetworkInterface networkInterface = enumeration.nextElement();
            String name = networkInterface.getName();
            String displayName = networkInterface.getDisplayName();

            if (name != null && name.trim().equalsIgnoreCase(normalized)) return networkInterface;
            if (displayName != null && displayName.trim().equalsIgnoreCase(normalized)) return networkInterface;

            InetAddress ipv4 = findIPv4Address(networkInterface);
            if (ipv4 != null && ipv4.getHostAddress().equalsIgnoreCase(normalized)) return networkInterface;
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

    private InterfaceSocket createSocket(NetworkInterface networkInterface, InetAddress multicastAddress) throws IOException {
        InetAddress ipv4 = findIPv4Address(networkInterface);
        if (ipv4 == null) {
            throw new IOException("The network interface does not have a valid IPv4 address.");
        }

        DatagramChannel channel = null;
        try {
            channel = DatagramChannel.open(StandardProtocolFamily.INET);
            channel.setOption(StandardSocketOptions.SO_REUSEADDR, true);
            channel.setOption(StandardSocketOptions.IP_MULTICAST_TTL, MULTICAST_TTL);
            channel.setOption(StandardSocketOptions.IP_MULTICAST_IF, networkInterface);
            channel.bind(new InetSocketAddress(ipv4, 0));

            return new InterfaceSocket(networkInterface, ipv4, channel, multicastAddress);
        } catch (Exception exception) {
            if (channel != null) {
                try { channel.close(); } catch (IOException ignored) {}
            }
            if (exception instanceof IOException ioException) throw ioException;
            throw new IOException(exception.getMessage(), exception);
        }
    }

    private void broadcastSafely() {
        if (!running || sockets.isEmpty()) return;

        String payload = buildPayload();
        byte[] data = payload.getBytes(StandardCharsets.UTF_8);

        if (data.length > MAX_PACKET_SIZE) {
            logDebug("Broadcast skipped because payload exceeds " + MAX_PACKET_SIZE + " bytes.");
            return;
        }

        for (InterfaceSocket interfaceSocket : new ArrayList<>(sockets)) {
            try {
                sendPacket(interfaceSocket, data, payload);
            } catch (IOException exception) {
                logDebug("Failed to broadcast on interface " + interfaceSocket.networkInterface.getDisplayName()
                        + " [" + interfaceSocket.networkInterface.getName() + "]: " + exception.getMessage());
            }
        }
    }

    private void sendPacket(InterfaceSocket interfaceSocket, byte[] data, String payload) throws IOException {
        ByteBuffer buffer = ByteBuffer.wrap(data);
        InetSocketAddress destination = new InetSocketAddress(interfaceSocket.multicastAddress, MULTICAST_PORT);

        interfaceSocket.channel.send(buffer, destination);
        logDebug("Broadcast sent via " + interfaceSocket.networkInterface.getDisplayName() 
                + " (" + interfaceSocket.ipv4.getHostAddress() + "): " + payload);
    }

    private String buildPayload() {
        return "[MOTD]" + resolveMotd() + "[/MOTD][AD]" + advertisedPort + "[/AD]";
    }

    private String resolveMotd() {
        String motd = configuredMotd == null ? "" : configuredMotd;

        int online = plugin.getServer().getPlayerCount();
        int max = plugin.getServer().getConfiguration().getShowMaxPlayers();

        motd = motd.replace("{online}", String.valueOf(online))
                   .replace("{max}", String.valueOf(max));

        return translateLegacyColors(motd);
    }

    private String translateLegacyColors(String text) {
        if (text == null || text.isEmpty()) return text;

        StringBuilder result = new StringBuilder(text.length());
        for (int i = 0; i < text.length(); i++) {
            char current = text.charAt(i);
            if (current == '&' && i + 1 < text.length()) {
                char code = text.charAt(i + 1);
                if (isLegacyColorCode(code)) {
                    result.append('§').append(Character.toLowerCase(code));
                    i++;
                    continue;
                }
            }
            result.append(current);
        }

        return result.toString();
    }

    private boolean isLegacyColorCode(char code) {
        return (code >= '0' && code <= '9')
                || (code >= 'a' && code <= 'f')
                || (code >= 'A' && code <= 'F')
                || code == 'k' || code == 'K'
                || code == 'l' || code == 'L'
                || code == 'm' || code == 'M'
                || code == 'n' || code == 'N'
                || code == 'o' || code == 'O'
                || code == 'r' || code == 'R';
    }

    private synchronized void closeSockets() {
        for (InterfaceSocket interfaceSocket : sockets) {
            try {
                interfaceSocket.channel.close();
            } catch (IOException ignored) {}
        }
        sockets.clear();
    }

    private void logDebug(String message) {
        if (debug) {
            plugin.getLogger().info("[DEBUG] " + message);
        }
    }

    private static final class InterfaceSocket {
        private final NetworkInterface networkInterface;
        private final InetAddress ipv4;
        private final DatagramChannel channel;
        private final InetAddress multicastAddress;

        private InterfaceSocket(NetworkInterface networkInterface, InetAddress ipv4, DatagramChannel channel, InetAddress multicastAddress) {
            this.networkInterface = networkInterface;
            this.ipv4 = ipv4;
            this.channel = channel;
            this.multicastAddress = multicastAddress;
        }
    }
}               