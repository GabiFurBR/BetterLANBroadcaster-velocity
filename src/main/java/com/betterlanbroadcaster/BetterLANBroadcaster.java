package com.betterlanbroadcaster;

import com.google.inject.Inject;
import com.velocitypowered.api.command.CommandManager;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.proxy.ProxyShutdownEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import org.slf4j.Logger;

import java.nio.file.Path;

@Plugin(
        id = BetterLANBroadcaster.PLUGIN_ID,
        name = "BetterLANBroadcaster",
        version = "1.0.3",
        description = "Velocity port of BetterLANBroadcaster for LAN server discovery",
        authors = {"myxxr", "GabiFurBR"}
)
public class BetterLANBroadcaster {

    public static final String PLUGIN_ID = "betterlanbroadcaster";
    public static final String PERMISSION_ADMIN = "betterlanbroadcaster.admin";
    public static final String VERSION = "1.0.3";

    private final ProxyServer server;
    private final Logger logger;
    private final Config config;
    private final Language language;

    private MulticastBroadcaster broadcaster;
    private boolean initialized;

    @Inject
    public BetterLANBroadcaster(
            ProxyServer server,
            Logger logger,
            @DataDirectory Path dataDirectory
    ) {
        this.server = server;
        this.logger = logger;
        this.config = new Config(this, dataDirectory);
        this.language = new Language(this);
        this.initialized = false;
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        logger.info(language.cleanColors(language.get("plugin.initializing", VERSION)));

        if (!config.load()) {
            logger.error(language.cleanColors(language.get("config.load_failed_default")));
        }

        language.load(config.getLanguage());

        registerCommands();

        initialized = true;

        logger.info(language.cleanColors(language.get("plugin.started")));

        if (config.isBroadcastEnabled()) {
            if (!startBroadcaster()) {
                logger.error(language.cleanColors(language.get("broadcast.start_failed")));
            }
        } else {
            logger.info(language.cleanColors(language.get("broadcast.disabled")));
        }
    }

    @Subscribe
    public void onProxyShutdown(ProxyShutdownEvent event) {
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

        logger.debug(language.cleanColors(language.get("commands.registered")));
    }

    public synchronized boolean startBroadcaster() {
        if (!initialized) {
            logger.warn(language.cleanColors(language.get("broadcast.not_initialized")));
            return false;
        }

        if (broadcaster != null && broadcaster.isRunning()) {
            return false;
        }

        if (broadcaster != null) {
            broadcaster.shutdown();
            broadcaster = null;
        }

        int configuredPort = config.getBroadcastPort();
        int advertisedPort = configuredPort;

        if (advertisedPort == 0) {
            advertisedPort = server
                    .getBoundAddress()
                    .getPort();
        }

        long configuredDelay = config.getBroadcastDelayMs();
        int delayMs = (int) configuredDelay;

        try {
            broadcaster = new MulticastBroadcaster(
                    this,
                    config.getMotd(),
                    advertisedPort,
                    delayMs,
                    config.getNetworkInterface()
            );

            broadcaster.setDebug(config.isDebug());

            broadcaster.start();

            logger.info(language.cleanColors(language.get("broadcast.started_port", advertisedPort)));

            return true;

        } catch (Exception e) {
            logger.error(language.cleanColors(language.get("broadcast.start_failed")), e);

            if (broadcaster != null) {
                broadcaster.shutdown();
                broadcaster = null;
            }

            return false;
        }
    }

    public synchronized boolean stopBroadcaster() {
        if (broadcaster == null) {
            return false;
        }

        if (!broadcaster.isRunning()) {
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
        } catch (Exception e) {
            logger.warn(language.cleanColors(language.get("broadcast.shutdown_error")), e);
        } finally {
            broadcaster = null;
        }
    }

    public synchronized boolean reloadBroadcaster() {
        shutdownBroadcaster();

        if (!config.isBroadcastEnabled()) {
            logger.info(language.cleanColors(language.get("broadcast.disabled_reload")));
            return true;
        }

        return startBroadcaster();
    }

    public synchronized boolean reconfigureBroadcaster() {
        boolean wasRunning = broadcaster != null && broadcaster.isRunning();

        if (!wasRunning) {
            return true;
        }

        shutdownBroadcaster();

        return startBroadcaster();
    }

    public int getMaxPlayers() {
        try {
            int maxPlayers = server
                    .getConfiguration()
                    .getShowMaxPlayers();

            if (maxPlayers > 0) {
                return maxPlayers;
            }
        } catch (Exception e) {
            logger.debug(language.cleanColors(language.get("players.max_fetch_error")), e);
        }

        return 100;
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

    public MulticastBroadcaster getBroadcaster() {
        return broadcaster;
    }

    public boolean isInitialized() {
        return initialized;
    }

    public String getVersion() {
        return VERSION;
    }

    public String formatMiniMessage(String text) {
        if (text == null || text.isEmpty()) {
            return "";
        }

        try {
            String legacy =
                    net.kyori.adventure.text.serializer.legacy
                            .LegacyComponentSerializer
                            .legacySection()
                            .serialize(
                                    net.kyori.adventure.text.minimessage
                                            .MiniMessage
                                            .miniMessage()
                                            .deserialize(text)
                            );

            return translateColorCodes(legacy);

        } catch (Exception e) {
            logger.debug(language.get("minimessage.parse_error", text), e);

            return translateColorCodes(text);
        }
    }

    private String translateColorCodes(String text) {
        if (text == null || text.isEmpty()) {
            return "";
        }

        return text.replaceAll(
                "&([0-9a-fA-Fk-oK-OrR])",
                "\u00A7$1"
        );
    }
}