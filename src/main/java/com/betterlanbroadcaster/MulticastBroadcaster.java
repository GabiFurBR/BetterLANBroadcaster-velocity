package com.betterlanbroadcaster;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * Periodically broadcasts the server's MOTD and port via UDP multicast
 * to 224.0.2.60:4445, following the Minecraft LAN discovery protocol.
 *
 * Format: [MOTD]serverMOTD[/MOTD][AD]serverPort[/AD]
 *
 * Uses a dedicated ScheduledExecutorService for periodic broadcasts.
 */
public class MulticastBroadcaster {

    private static final String MULTICAST_ADDRESS = "224.0.2.60";
    private static final int MULTICAST_PORT = 4445;

    private final BetterLANBroadcaster plugin;

    private final ScheduledExecutorService scheduler =
            Executors.newSingleThreadScheduledExecutor(r -> {
                Thread t = new Thread(r, "BetterLANBroadcaster");
                t.setDaemon(true);
                return t;
            });

    private String motd;
    private int port;
    private int delayMs;
    private boolean running;
    private boolean debug;
    private ScheduledFuture<?> scheduledFuture;

    public MulticastBroadcaster(
            BetterLANBroadcaster plugin,
            String motd,
            int port,
            int delayMs
    ) {
        this.plugin = plugin;
        this.motd = sanitizeMotd(motd);
        this.port = port;
        this.delayMs = Math.max(50, delayMs);
        this.running = false;
        this.debug = false;
    }

    /**
     * Starts the periodic multicast broadcast task.
     */
    public void start() {
        if (running) {
            return;
        }

        running = true;
        scheduleTask();
    }

    /**
     * Stops the broadcast task.
     * The underlying executor remains alive in case the broadcast is resumed later.
     */
    public void stop() {
        running = false;

        if (scheduledFuture != null) {
            scheduledFuture.cancel(false);
            scheduledFuture = null;
        }
    }

    /**
     * Full shutdown — stops the task and terminates the executor.
     */
    public void shutdown() {
        stop();
        scheduler.shutdownNow();
    }

    /**
     * @return true if currently broadcasting
     */
    public boolean isRunning() {
        return running;
    }

    public void setMotd(String newMotd) {
        this.motd = sanitizeMotd(newMotd);

        if (running) {
            reschedule();
        }
    }

    public String getMotd() {
        return motd;
    }

    public void setDelayMs(int newDelayMs) {
        this.delayMs = Math.max(50, newDelayMs);

        if (running) {
            reschedule();
        }
    }

    public int getDelayMs() {
        return delayMs;
    }

    /**
     * Updates the broadcast port.
     */
    public void setPort(int newPort) {
        if (newPort > 0 && newPort <= 65535) {
            this.port = newPort;
        }
    }

    public int getPort() {
        return port;
    }

    public void setDebug(boolean debug) {
        this.debug = debug;
    }

    public boolean isDebug() {
        return debug;
    }

    /**
     * Cancels the current future and schedules a new one if running.
     */
    private void reschedule() {
        if (scheduledFuture != null) {
            scheduledFuture.cancel(false);
            scheduledFuture = null;
        }

        if (running) {
            scheduleTask();
        }
    }

    private void scheduleTask() {
        scheduledFuture = scheduler.scheduleAtFixedRate(
                this::sendBroadcast,
                0,
                delayMs,
                TimeUnit.MILLISECONDS
        );
    }

    /**
     * Builds and sends the multicast packet.
     */
    private void sendBroadcast() {
        String resolvedMotd = resolveMotd();

        String payload =
                "[MOTD]" + resolvedMotd + "[/MOTD][AD]" + port + "[/AD]";

        byte[] message = payload.getBytes(StandardCharsets.UTF_8);

        if (debug) {
            plugin.getLogger().info(
                    "[DEBUG] Sending broadcast: "
                            + payload
                            + " -> "
                            + MULTICAST_ADDRESS
                            + ":"
                            + MULTICAST_PORT
            );
        }

        try (DatagramSocket socket = new DatagramSocket()) {
            InetAddress group = InetAddress.getByName(MULTICAST_ADDRESS);

            DatagramPacket packet = new DatagramPacket(
                    message,
                    message.length,
                    group,
                    MULTICAST_PORT
            );

            socket.send(packet);

            if (debug) {
                plugin.getLogger().info(
                        "[DEBUG] Broadcast sent successfully ("
                                + message.length
                                + " bytes)"
                );
            }

        } catch (UnknownHostException e) {
            plugin.getLogger().warn(
                    "Invalid multicast address: " + MULTICAST_ADDRESS
            );

        } catch (IOException e) {
            plugin.getLogger().warn(
                    "Failed to send multicast broadcast: " + e.getMessage()
            );
        }
    }

    /**
     * Resolves the MOTD template.
     *
     * Supports:
     * - {online} for the current player count
     * - {max} for the maximum player count
     * - MiniMessage formatting through the main plugin
     */
    private String resolveMotd() {
        String resolved = motd;

        int online = plugin.getServer().getPlayerCount();
        int max = plugin.getMaxPlayers();

        resolved = resolved.replace(
                "{online}",
                String.valueOf(online)
        );

        resolved = resolved.replace(
                "{max}",
                String.valueOf(max)
        );

        resolved = plugin.formatMiniMessage(resolved);

        return resolved;
    }

    /**
     * Sanitizes the MOTD to remove characters that could break
     * the Minecraft LAN discovery protocol format.
     */
    private String sanitizeMotd(String raw) {
        if (raw == null) {
            return "";
        }

        return raw
                .replace("[/MOTD]", "")
                .replace("[/AD]", "")
                .replace("[MOTD]", "")
                .replace("[AD]", "");
    }
}