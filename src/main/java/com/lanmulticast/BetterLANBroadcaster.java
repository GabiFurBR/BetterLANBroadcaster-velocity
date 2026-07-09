package com.lanmulticast;

import org.bstats.bukkit.Metrics;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

public class BetterLANBroadcaster extends JavaPlugin {

    /** True if the Adventure MiniMessage library is available at runtime. */
    private static final boolean HAS_MINI_MESSAGE;
    /** True if the PlaceholderAPI plugin is loaded at runtime. */
    private static final boolean HAS_PLACEHOLDER_API;

    static {
        boolean mm = false;
        try {
            Class.forName("net.kyori.adventure.text.minimessage.MiniMessage");
            mm = true;
        } catch (ClassNotFoundException ignored) {
        }
        HAS_MINI_MESSAGE = mm;

        boolean papi = false;
        try {
            Class.forName("me.clip.placeholderapi.PlaceholderAPI");
            papi = true;
        } catch (ClassNotFoundException ignored) {
        }
        HAS_PLACEHOLDER_API = papi;
    }

    private MulticastBroadcaster broadcaster;
    private LanguageManager languageManager;
    private CommandHandler commandHandler;

    @Override
    public void onEnable() {
        // Save default config and language files
        saveDefaultConfig();
        saveResourceIfNotExists("lang/messages_en.yml");
        saveResourceIfNotExists("lang/messages_zh.yml");

        // Load language manager
        languageManager = new LanguageManager(this);

        // Load config values
        String motd = getConfig().getString("motd", "A Minecraft Server");
        int delayMs = getConfig().getInt("broadcast-delay-ms", 1500);
        int port = resolvePort();
        boolean debug = getConfig().getBoolean("debug", false);

        // Initialize broadcaster (not started yet until /blb start)
        broadcaster = new MulticastBroadcaster(this, motd, port, delayMs);
        broadcaster.setDebug(debug);

        // Auto-start broadcasting if enabled in config
        if (getConfig().getBoolean("broadcast-enabled", false)) {
            broadcaster.start();
        }

        // Register command handler
        commandHandler = new CommandHandler(this);
        getCommand("betterlanbroadcaster").setExecutor(commandHandler);
        getCommand("betterlanbroadcaster").setTabCompleter(commandHandler);

        // Initialize bStats metrics
        // Plugin ID: 32441 (https://bstats.org/plugin/bukkit/BetterLANBroadcaster/32441)
        Metrics metrics = new Metrics(this, 32441);

        // Log detected features
        StringBuilder features = new StringBuilder("BetterLANBroadcaster enabled! Port: ").append(port);
        if (HAS_MINI_MESSAGE) features.append(" | MiniMessage");
        if (HAS_PLACEHOLDER_API) features.append(" | PlaceholderAPI");
        getLogger().info(features.toString());
    }

    @Override
    public void onDisable() {
        if (broadcaster != null) {
            broadcaster.shutdown();
        }
        getLogger().info("BetterLANBroadcaster has been disabled.");
    }

    /**
     * Reloads the config and language files, then re-applies settings to the broadcaster.
     */
    public void reloadPluginConfig() {
        reloadConfig();

        // Reload language
        languageManager.loadLanguage();

        // Apply config values
        String motd = getConfig().getString("motd", "A Minecraft Server");
        int delayMs = getConfig().getInt("broadcast-delay-ms", 1500);
        int port = resolvePort();
        boolean debug = getConfig().getBoolean("debug", false);

        broadcaster.setMotd(motd);
        broadcaster.setDelayMs(delayMs);
        broadcaster.setPort(port);
        broadcaster.setDebug(debug);

        // Handle state transition based on config
        boolean shouldBeRunning = getConfig().getBoolean("broadcast-enabled", false);
        if (shouldBeRunning && !broadcaster.isRunning()) {
            broadcaster.start();
        } else if (!shouldBeRunning && broadcaster.isRunning()) {
            broadcaster.stop();
        }
    }

    /**
     * Resolves the broadcast port:
     * - If config "broadcast-port" > 0, use that value
     * - Otherwise, auto-detect from the server
     */
    private int resolvePort() {
        int configPort = getConfig().getInt("broadcast-port", 0);
        if (configPort > 0 && configPort <= 65535) {
            return configPort;
        }
        return getServer().getPort();
    }

    public MulticastBroadcaster getBroadcaster() {
        return broadcaster;
    }

    public LanguageManager getLanguageManager() {
        return languageManager;
    }

    // -----------------------------------------------------------------------
    // Formatting helpers
    // -----------------------------------------------------------------------

    /**
     * Parses PlaceholderAPI placeholders (if available).
     * Uses the first online player as context for player-dependent placeholders;
     * falls back to the raw text if no players are online.
     */
    public String parsePlaceholders(String text) {
        if (!HAS_PLACEHOLDER_API) {
            return text;
        }
        try {
            // Use first online player as context, if available
            if (!getServer().getOnlinePlayers().isEmpty()) {
                return me.clip.placeholderapi.PlaceholderAPI.setPlaceholders(
                        getServer().getOnlinePlayers().iterator().next(), text);
            }
            // PlaceholderAPI.setPlaceholders(OfflinePlayer, String) accepts null
            return me.clip.placeholderapi.PlaceholderAPI.setPlaceholders(null, text);
        } catch (Exception e) {
            return text;
        }
    }

    /**
     * Attempts to parse the text as MiniMessage and convert it to legacy
     * {@code §} colour codes. Afterwards translates any remaining legacy
     * {@code &} codes as well.
     * <p>
     * If MiniMessage is not available or parsing fails, the method falls back
     * to simple {@code &}→{@code §} translation.
     */
    public String formatMiniMessage(String text) {
        if (HAS_MINI_MESSAGE) {
            try {
                net.kyori.adventure.text.Component component =
                        net.kyori.adventure.text.minimessage.MiniMessage.miniMessage().deserialize(text);
                String legacy = net.kyori.adventure.text.serializer.legacy
                        .LegacyComponentSerializer.legacySection().serialize(component);
                // Still translate any remaining & codes (e.g. from MiniMessage-ignored text)
                return ChatColor.translateAlternateColorCodes('&', legacy);
            } catch (Exception ignored) {
                // MiniMessage failed — fall through to legacy translation
            }
        }
        return ChatColor.translateAlternateColorCodes('&', text);
    }

    /**
     * Saves a resource from the jar to the plugin data folder only if it doesn't exist.
     */
    private void saveResourceIfNotExists(String resource) {
        if (getResource(resource) != null && !new java.io.File(getDataFolder(), resource).exists()) {
            saveResource(resource, false);
        }
    }
}
