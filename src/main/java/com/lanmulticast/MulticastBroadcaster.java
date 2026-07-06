package com.lanmulticast;

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
 * <p>
 * Format: [MOTD]serverMOTD[/MOTD][AD]serverPort[/AD]
 * <p>
 * Uses a dedicated ScheduledExecutorService instead of BukkitRunnable,
 * making it compatible with Spigot, Paper, and Folia servers alike.
 */
public class MulticastBroadcaster {

    private static final String MULTICAST_ADDRESS = "224.0.2.60";
    private static final int MULTICAST_PORT = 4445;

    private final BetterLANBroadcaster plugin;
    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor(r -> {
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

    public MulticastBroadcaster(BetterLANBroadcaster plugin, String motd, int port, int delayMs) {
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
     * Stops the broadcast task. The underlying executor remains alive
     * in case the broadcast is resumed later.
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
     * Called from onDisable().
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
     * Cancels the current future and schedules a new one (only if running).
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

    /**
     * Schedules the broadcast on the dedicated executor.
     * Works on Spigot, Paper <em>and</em> Folia without any platform-specific API.
     */
    private void scheduleTask() {
        scheduledFuture = scheduler.scheduleAtFixedRate(
                this::sendBroadcast, 0, delayMs, TimeUnit.MILLISECONDS);
    }

    /**
     * Builds and sends the multicast packet.
     */
    private void sendBroadcast() {
        String resolvedMotd = resolveMotd();
        String payload = "[MOTD]" + resolvedMotd + "[/MOTD][AD]" + port + "[/AD]";
        byte[] message = payload.getBytes(StandardCharsets.UTF_8);

        if (debug) {
            plugin.getLogger().info("[DEBUG] Sending broadcast: " + payload
                    + " -> " + MULTICAST_ADDRESS + ":" + MULTICAST_PORT);
        }

        try (DatagramSocket socket = new DatagramSocket()) {
            InetAddress group = InetAddress.getByName(MULTICAST_ADDRESS);
            DatagramPacket packet = new DatagramPacket(message, message.length, group, MULTICAST_PORT);
            socket.send(packet);

            if (debug) {
                plugin.getLogger().info("[DEBUG] Broadcast sent successfully (" + message.length + " bytes)");
            }
        } catch (UnknownHostException e) {
            plugin.getLogger().warning("Invalid multicast address: " + MULTICAST_ADDRESS);
        } catch (IOException e) {
            plugin.getLogger().warning("Failed to send multicast broadcast: " + e.getMessage());
        }
    }

    /**
     * Resolves the MOTD template:
     * <ol>
     *   <li>Replaces {@code {online}} / {@code {max}} with real-time player counts</li>
     *   <li>Parses PlaceholderAPI placeholders (if the plugin is installed)</li>
     *   <li>Parses MiniMessage tags and converts to legacy {@code §} colour codes (if Adventure is available)</li>
     *   <li>Falls back to standard {@code &} colour code translation for any remaining codes</li>
     * </ol>
     */
    private String resolveMotd() {
        String resolved = motd;

        // 1. Built-in player count placeholders
        int online = plugin.getServer().getOnlinePlayers().size();
        int max = plugin.getServer().getMaxPlayers();
        resolved = resolved.replace("{online}", String.valueOf(online));
        resolved = resolved.replace("{max}", String.valueOf(max));

        // 2. PlaceholderAPI placeholders
        resolved = plugin.parsePlaceholders(resolved);

        // 3. MiniMessage → legacy colour codes, then & → § translation
        resolved = plugin.formatMiniMessage(resolved);

        return resolved;
    }

    /**
     * Sanitizes the MOTD to remove characters that could break the protocol format.
     */
    private String sanitizeMotd(String raw) {
        if (raw == null) {
            return "";
        }
        return raw.replace("[/MOTD]", "")
                  .replace("[/AD]", "")
                  .replace("[MOTD]", "")
                  .replace("[AD]", "");
    }
}
