package com.betterlanbroadcaster;

import java.io.IOException;
import java.nio.channels.DatagramChannel;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.StandardProtocolFamily;
import java.net.StandardSocketOptions;
import java.nio.ByteBuffer;
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

    public MulticastBroadcaster(
            BetterLANBroadcaster plugin,
            String motd,
            int port,
            long delayMs,
            String networkInterface
    ) {
        this.plugin = plugin;
        this.configuredMotd = motd;
        this.advertisedPort = port;
        this.delayMs = delayMs;
        this.configuredInterface = networkInterface == null
                ? "auto"
                : networkInterface;

        this.scheduler = Executors.newSingleThreadScheduledExecutor(task -> {
            Thread thread = new Thread(task, "BetterLANBroadcaster");
            thread.setDaemon(true);
            return thread;
        });
    }

    public synchronized void start() throws IOException {
        if (shutdown) {
            throw new IOException("Multicast broadcaster já foi encerrado.");
        }

        if (running) {
            return;
        }

        closeSockets();

        InetAddress multicastAddress =
                InetAddress.getByName(MULTICAST_ADDRESS);

        if (!multicastAddress.isMulticastAddress()) {
            throw new IOException(
                    "Endereço multicast inválido: "
                            + MULTICAST_ADDRESS
            );
        }

        List<NetworkInterface> interfaces = resolveInterfaces();

        if (interfaces.isEmpty()) {
            throw new IOException(
                    "Nenhuma interface de rede compatível com multicast foi encontrada."
            );
        }

        int successful = 0;

        for (NetworkInterface networkInterface : interfaces) {
            try {
                InterfaceSocket interfaceSocket =
                        createSocket(
                                networkInterface,
                                multicastAddress
                        );

                sockets.add(interfaceSocket);
                successful++;

                logDebug(
                        "Interface multicast ativa: "
                                + networkInterface.getDisplayName()
                                + " ["
                                + networkInterface.getName()
                                + "] IPv4="
                                + interfaceSocket.ipv4.getHostAddress()
                );

            } catch (Exception exception) {
                logDebug(
                        "Não foi possível usar a interface "
                                + networkInterface.getDisplayName()
                                + " ["
                                + networkInterface.getName()
                                + "]: "
                                + exception.getMessage()
                );
            }
        }

        if (successful == 0) {
            closeSockets();

            throw new IOException(
                    "Nenhuma das interfaces encontradas pôde ser configurada para multicast."
            );
        }

        running = true;

        long interval = Math.max(50L, delayMs);

        broadcastTask = scheduler.scheduleAtFixedRate(
                this::broadcastSafely,
                0L,
                interval,
                TimeUnit.MILLISECONDS
        );

        plugin.getLogger().info(
                "Multicast broadcaster iniciado: "
                        + MULTICAST_ADDRESS
                        + ":"
                        + MULTICAST_PORT
                        + " usando "
                        + successful
                        + " interface(s)."
        );
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
        if (shutdown) {
            return;
        }

        shutdown = true;

        stop();

        scheduler.shutdownNow();
    }

    public boolean isRunning() {
        return running;
    }

    public int getPort() {
        return advertisedPort;
    }

    public void setDebug(boolean debug) {
        this.debug = debug;
    }

    private List<NetworkInterface> resolveInterfaces()
            throws IOException {

        String configured = configuredInterface == null
                ? "auto"
                : configuredInterface.trim();

        if (configured.isEmpty()
                || configured.equalsIgnoreCase("auto")) {

            return findAllSuitableInterfaces();
        }

        NetworkInterface selected =
                findConfiguredInterface(configured);

        if (selected == null) {
            throw new IOException(
                    "Interface de rede não encontrada: "
                            + configured
            );
        }

        if (!isSuitableInterface(selected)) {
            throw new IOException(
                    "A interface selecionada não é adequada para multicast: "
                            + configured
            );
        }

        List<NetworkInterface> result = new ArrayList<>();
        result.add(selected);

        return result;
    }

    private List<NetworkInterface> findAllSuitableInterfaces()
            throws IOException {

        List<NetworkInterface> result =
                new ArrayList<>();

        Enumeration<NetworkInterface> enumeration =
                NetworkInterface.getNetworkInterfaces();

        if (enumeration == null) {
            return result;
        }

        while (enumeration.hasMoreElements()) {

            NetworkInterface networkInterface =
                    enumeration.nextElement();

            String name =
                    networkInterface.getName();

            String displayName =
                    networkInterface.getDisplayName();

            try {
                logDebug(
                        "Interface encontrada: "
                                + displayName
                                + " ["
                                + name
                                + "]"
                );

                if (!networkInterface.isUp()) {
                    logDebug(
                            "Ignorada: interface desligada."
                    );
                    continue;
                }

                if (networkInterface.isLoopback()) {
                    logDebug(
                            "Ignorada: loopback."
                    );
                    continue;
                }

                if (!networkInterface.supportsMulticast()) {
                    logDebug(
                            "Ignorada: multicast não suportado."
                    );
                    continue;
                }

                InetAddress ipv4 =
                        findIPv4Address(networkInterface);

                if (ipv4 == null) {
                    logDebug(
                            "Ignorada: nenhum IPv4 encontrado."
                    );
                    continue;
                }

                logDebug(
                        "Candidata para multicast: "
                                + displayName
                                + " ["
                                + name
                                + "] IPv4="
                                + ipv4.getHostAddress()
                );

                result.add(networkInterface);

            } catch (Exception exception) {

                logDebug(
                        "Ignorada "
                                + displayName
                                + ": "
                                + exception.getMessage()
                );
            }
        }

        return result;
    }

    private boolean isSuitableInterface(
            NetworkInterface networkInterface
    ) {
        try {
            if (!networkInterface.isUp()) {
                return false;
            }

            if (networkInterface.isLoopback()) {
                return false;
            }

            if (!networkInterface.supportsMulticast()) {
                return false;
            }

            return findIPv4Address(networkInterface) != null;

        } catch (Exception exception) {
            return false;
        }
    }

    private NetworkInterface findConfiguredInterface(
            String configured
    ) throws IOException {

        String normalized =
                configured.trim();

        Enumeration<NetworkInterface> enumeration =
                NetworkInterface.getNetworkInterfaces();

        if (enumeration == null) {
            return null;
        }

        while (enumeration.hasMoreElements()) {

            NetworkInterface networkInterface =
                    enumeration.nextElement();

            String name =
                    networkInterface.getName();

            String displayName =
                    networkInterface.getDisplayName();

            if (name != null
                    && name.trim().equalsIgnoreCase(normalized)) {

                return networkInterface;
            }

            if (displayName != null
                    && displayName.trim().equalsIgnoreCase(normalized)) {

                return networkInterface;
            }

            InetAddress ipv4 =
                    findIPv4Address(networkInterface);

            if (ipv4 != null
                    && ipv4.getHostAddress()
                    .equalsIgnoreCase(normalized)) {

                return networkInterface;
            }
        }

        return null;
    }

    private InetAddress findIPv4Address(
            NetworkInterface networkInterface
    ) {

        Enumeration<InetAddress> addresses =
                networkInterface.getInetAddresses();

        while (addresses.hasMoreElements()) {

            InetAddress address =
                    addresses.nextElement();

            if (address instanceof Inet4Address
                    && !address.isLoopbackAddress()) {

                return address;
            }
        }

        return null;
    }

    private InterfaceSocket createSocket(
            NetworkInterface networkInterface,
            InetAddress multicastAddress
    ) throws IOException {

        InetAddress ipv4 =
                findIPv4Address(networkInterface);

        if (ipv4 == null) {
            throw new IOException(
                    "A interface não possui um endereço IPv4 válido."
            );
        }

        DatagramChannel channel = null;

        try {
            channel =
                    DatagramChannel.open(
                            StandardProtocolFamily.INET
                    );

            channel.setOption(
                    StandardSocketOptions.SO_REUSEADDR,
                    true
            );

            channel.setOption(
                    StandardSocketOptions.IP_MULTICAST_TTL,
                    MULTICAST_TTL
            );

            channel.setOption(
                    StandardSocketOptions.IP_MULTICAST_IF,
                    networkInterface
            );

            channel.bind(
                    new InetSocketAddress(
                            ipv4,
                            0
                    )
            );

            return new InterfaceSocket(
                    networkInterface,
                    ipv4,
                    channel,
                    multicastAddress
            );

        } catch (Exception exception) {

            if (channel != null) {
                try {
                    channel.close();
                } catch (IOException ignored) {
                }
            }

            if (exception instanceof IOException ioException) {
                throw ioException;
            }

            throw new IOException(
                    exception.getMessage(),
                    exception
            );
        }
    }

    private void broadcastSafely() {

        if (!running || sockets.isEmpty()) {
            return;
        }

        String payload =
                buildPayload();

        byte[] data =
                payload.getBytes(
                        StandardCharsets.UTF_8
                );

        if (data.length > MAX_PACKET_SIZE) {

            logDebug(
                    "Broadcast ignorado porque o pacote excede "
                            + MAX_PACKET_SIZE
                            + " bytes."
            );

            return;
        }

        for (InterfaceSocket interfaceSocket :
                new ArrayList<>(sockets)) {

            try {
                sendPacket(
                        interfaceSocket,
                        data,
                        payload
                );

            } catch (IOException exception) {

                logDebug(
                        "Falha ao enviar broadcast pela interface "
                                + interfaceSocket.networkInterface
                                .getDisplayName()
                                + " ["
                                + interfaceSocket.networkInterface
                                .getName()
                                + "]: "
                                + exception.getMessage()
                );
            }
        }
    }

    private void sendPacket(
            InterfaceSocket interfaceSocket,
            byte[] data,
            String payload
    ) throws IOException {

        ByteBuffer buffer =
                ByteBuffer.wrap(data);

        InetSocketAddress destination =
                new InetSocketAddress(
                        interfaceSocket.multicastAddress,
                        MULTICAST_PORT
                );

        interfaceSocket.channel.send(
                buffer,
                destination
        );

        logDebug(
                "Broadcast enviado por "
                        + interfaceSocket.networkInterface
                        .getDisplayName()
                        + " ("
                        + interfaceSocket.ipv4.getHostAddress()
                        + "): "
                        + payload
        );
    }

    private String buildPayload() {

        String motd =
                resolveMotd();

        return "[MOTD]"
                + motd
                + "[/MOTD][AD]"
                + advertisedPort
                + "[/AD]";
    }

    private String resolveMotd() {

        String motd =
                configuredMotd == null
                        ? ""
                        : configuredMotd;

        int online =
                plugin.getServer()
                        .getPlayerCount();

        int max =
                plugin.getServer()
                        .getConfiguration()
                        .getShowMaxPlayers();

        return motd
                .replace(
                        "{online}",
                        String.valueOf(online)
                )
                .replace(
                        "{max}",
                        String.valueOf(max)
                );
    }

    private synchronized void closeSockets() {

        for (InterfaceSocket interfaceSocket :
                sockets) {

            closeSocket(interfaceSocket);
        }

        sockets.clear();
    }

    private void closeSocket(
            InterfaceSocket interfaceSocket
    ) {

        try {
            interfaceSocket.channel.close();
        } catch (IOException ignored) {
        }
    }

    private void logDebug(String message) {

        if (!debug) {
            return;
        }

        plugin.getLogger().info(
                "[DEBUG] " + message
        );
    }

    private static final class InterfaceSocket {

        private final NetworkInterface networkInterface;
        private final InetAddress ipv4;
        private final DatagramChannel channel;
        private final InetAddress multicastAddress;

        private InterfaceSocket(
                NetworkInterface networkInterface,
                InetAddress ipv4,
                DatagramChannel channel,
                InetAddress multicastAddress
        ) {
            this.networkInterface = networkInterface;
            this.ipv4 = ipv4;
            this.channel = channel;
            this.multicastAddress = multicastAddress;
        }
    }
}