package com.betterlanbroadcaster;

import com.google.inject.Inject;
import com.velocitypowered.api.command.CommandManager;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.proxy.ProxyShutdownEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bstats.velocity.Metrics;
import org.slf4j.Logger;

import java.nio.file.Path;

@Plugin(
        id = BetterLANBroadcaster.PLUGIN_ID,
        name = "BetterLANBroadcaster",
        version = BetterLANBroadcaster.VERSION,
        description = "Velocity port of BetterLANBroadcaster for LAN server discovery",
        authors = {"myxxr", "GabiFurBR"}
)
public final class BetterLANBroadcaster {

    public static final String PLUGIN_ID = "betterlanbroadcaster";
    public static final String PERMISSION_ADMIN = "betterlanbroadcaster.admin";
    public static final String VERSION = "1.1.1";

    private static final int BSTATS_PLUGIN_ID = 34465;

    private final ProxyServer server;
    private final Logger logger;
    private final Metrics.Factory metricsFactory;
    private final Config config;
    private final Language language;

    private MulticastBroadcaster broadcaster;
    private boolean initialized;
    private Metrics metrics;

    @Inject
    public BetterLANBroadcaster(
            ProxyServer server,
            Logger logger,
            @DataDirectory Path dataDirectory,
            Metrics.Factory metricsFactory
    ) {
        this.server = server;
        this.logger = logger;
        this.metricsFactory = metricsFactory;
        this.config = new Config(this, dataDirectory);
        this.language = new Language(this);
    }

    @Subscribe
    public synchronized void onProxyInitialization(ProxyInitializeEvent event) {
        if (initialized) {
            return;
        }

        if (!config.load()) {
            logger.error("BetterLANBroadcaster could not fully load its configuration; defaults will be used in memory where possible.");
        }

        language.load(config.getLanguage());
        logger.info(language.cleanColors(language.get("plugin.initializing", VERSION)));
        registerCommands();
        initializeMetrics();

        broadcaster = new MulticastBroadcaster(this);
        initialized = true;

        logger.info(language.cleanColors(language.get("plugin.started")));

        if (config.isBroadcastEnabled()) {
            if (!startBroadcaster()) {
                logger.error(language.cleanColors(language.get("error.start-failed")));
            }
        } else {
            logger.info(language.cleanColors(language.get("broadcast.disabled")));
        }
    }

    private void initializeMetrics() {
        try {
            metrics = metricsFactory.make(this, BSTATS_PLUGIN_ID);
        } catch (RuntimeException exception) {
            logger.warn("Could not initialize bStats metrics. Broadcasting will continue without metrics.", exception);
        }
    }

    @Subscribe
    public synchronized void onProxyShutdown(ProxyShutdownEvent event) {
        if (!initialized && broadcaster == null) {
            return;
        }

        initialized = false;
        shutdownBroadcaster();
        logger.info(language.cleanColors(language.get("plugin.shutdown")));
    }

    private void registerCommands() {
        CommandManager commandManager = server.getCommandManager();
        CommandHandler handler = new CommandHandler(this);

        commandManager.register(
                commandManager.metaBuilder("blb")
                        .aliases("betterlanbroadcaster")
                        .plugin(this)
                        .build(),
                handler
        );

        logger.debug("BetterLANBroadcaster command registered.");
    }

    public synchronized boolean startBroadcaster() {
        if (!initialized) {
            logger.warn("BetterLANBroadcaster is not initialized yet.");
            return false;
        }

        if (broadcaster == null) {
            broadcaster = new MulticastBroadcaster(this);
        }

        if (broadcaster.isRunning()) {
            return false;
        }

        try {
            broadcaster.start(
                    config.getMotd(),
                    resolveAdvertisedPort(),
                    config.getBroadcastDelayMs(),
                    config.getNetworkInterface(),
                    config.getNetworkInterfaceExcludes(),
                    config.isDebug()
            );

            logger.info(
                    "LAN multicast broadcasting started on advertised port {} using {} interface(s).",
                    broadcaster.getPort(),
                    broadcaster.getActiveInterfaceCount()
            );
            return true;
        } catch (Exception exception) {
            logger.error(language.cleanColors(language.get("error.start-failed")), exception);
            return false;
        }
    }

    public synchronized boolean stopBroadcaster() {
        if (broadcaster == null || !broadcaster.isRunning()) {
            return false;
        }

        broadcaster.stop();
        logger.info(language.cleanColors(language.get("broadcast.stopped")));
        return true;
    }

    public synchronized void shutdownBroadcaster() {
        if (broadcaster == null) {
            return;
        }

        try {
            broadcaster.shutdown();
        } catch (RuntimeException exception) {
            logger.warn("An error occurred while shutting down the multicast broadcaster.", exception);
        } finally {
            broadcaster = null;
        }
    }

    public synchronized boolean reloadBroadcaster() {
        if (!initialized) {
            return false;
        }

        if (!config.reload()) {
            return false;
        }

        language.load(config.getLanguage());

        boolean desiredRunning = config.isBroadcastEnabled();
        boolean currentlyRunning = broadcaster != null && broadcaster.isRunning();

        if (!desiredRunning) {
            if (currentlyRunning) {
                stopBroadcaster();
            }
            return true;
        }

        if (broadcaster == null) {
            broadcaster = new MulticastBroadcaster(this);
        }

        try {
            if (currentlyRunning) {
                broadcaster.reconfigure(
                        config.getMotd(),
                        resolveAdvertisedPort(),
                        config.getBroadcastDelayMs(),
                        config.getNetworkInterface(),
                        config.getNetworkInterfaceExcludes(),
                        config.isDebug()
                );
            } else {
                startBroadcaster();
            }
            return broadcaster.isRunning();
        } catch (Exception exception) {
            logger.error(language.cleanColors(language.get("error.reload-broadcast")), exception);
            return false;
        }
    }

    public synchronized boolean reconfigureBroadcaster() {
        if (!initialized || broadcaster == null || !broadcaster.isRunning()) {
            return true;
        }

        try {
            broadcaster.reconfigure(
                    config.getMotd(),
                    resolveAdvertisedPort(),
                    config.getBroadcastDelayMs(),
                    config.getNetworkInterface(),
                    config.getNetworkInterfaceExcludes(),
                    config.isDebug()
            );
            return true;
        } catch (Exception exception) {
            logger.error(language.cleanColors(language.get("error.reload-broadcast")), exception);
            return false;
        }
    }

    private int resolveAdvertisedPort() {
        int configuredPort = config.getBroadcastPort();
        if (configuredPort > 0) {
            return configuredPort;
        }

        int boundPort = server.getBoundAddress().getPort();
        if (boundPort < 1 || boundPort > 65535) {
            throw new IllegalStateException("Velocity returned an invalid bound port: " + boundPort);
        }

        return boundPort;
    }

    public int getMaxPlayers() {
        try {
            int maxPlayers = server.getConfiguration().getShowMaxPlayers();
            return maxPlayers > 0 ? maxPlayers : 100;
        } catch (RuntimeException exception) {
            logger.debug("Could not read Velocity's maximum player count.", exception);
            return 100;
        }
    }

    public ProxyServer getServer() {
        return server;
    }

    public Logger getLogger() {
        return logger;
    }

    public Config getConfig() {
        return config;
    }

    public Language getLanguage() {
        return language;
    }

    public synchronized MulticastBroadcaster getBroadcaster() {
        return broadcaster;
    }

    public synchronized boolean isInitialized() {
        return initialized;
    }

    public String getVersion() {
        return VERSION;
    }

    /**
     * Converts MiniMessage and legacy '&' formatting to the section-code representation
     * expected by the Minecraft LAN discovery payload.
     */
    public String formatMiniMessage(String text) {
        if (text == null || text.isEmpty()) {
            return "";
        }

        try {
            String legacy = LegacyComponentSerializer.legacySection().serialize(
                    MiniMessage.miniMessage().deserialize(text)
            );
            return translateColorCodes(legacy);
        } catch (RuntimeException exception) {
            logger.debug("Could not parse MOTD MiniMessage input. Falling back to raw/legacy text.", exception);
            return translateColorCodes(text);
        }
    }

    private String translateColorCodes(String text) {
        StringBuilder result = new StringBuilder(text.length());
        for (int index = 0; index < text.length(); index++) {
            char current = text.charAt(index);
            if (current == '&' && index + 1 < text.length() && isLegacyColorCode(text.charAt(index + 1))) {
                result.append('\u00A7').append(Character.toLowerCase(text.charAt(index + 1)));
                index++;
                continue;
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
}
