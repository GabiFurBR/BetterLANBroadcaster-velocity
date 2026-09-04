package com.betterlanbroadcaster;

import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.proxy.ProxyShutdownEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import org.slf4j.Logger;

import java.nio.file.Path;

@Plugin(
        id = "betterlanbroadcaster",
        name = "BetterLANBroadcaster",
        version = "1.1.0",
        description = "BetterLANBroadcaster ported to Velocity",
        authors = {"myxxr"}
)
public class BetterLANBroadcaster {

    private final ProxyServer server;
    private final Logger logger;

    private final Config config;
    private final Language language;

    private MulticastBroadcaster broadcaster;

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
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        

        server.getCommandManager().register(
            server.getCommandManager().metaBuilder("blb").build(),
            new CommandHandler(this)
        );
        // Carrega a configuração
        config.load();

        // Carrega o idioma
        language.load(config.getLanguage());

        logger.info("BetterLANBroadcaster-Velocity foi iniciado!");

        // =====================================================
        // MULTICAST BROADCAST
        // =====================================================

        if (config.isBroadcastEnabled()) {

            int port = config.getBroadcastPort();

            // 0 = usar a porta onde o Velocity está ouvindo
            if (port <= 0) {
                port = server.getBoundAddress().getPort();
            }

            broadcaster = new MulticastBroadcaster(
                    this,
                    config.getMotd(),
                    port,
                    (int) config.getBroadcastDelayMs()
            );

            // Ativa debug
            broadcaster.setDebug(config.isDebug());

            logger.info("Broadcasting servidor na porta " + port);

            broadcaster.start();

        } else {

            logger.info("LAN Broadcast está desativado.");
        }
    }

    @Subscribe
    public void onProxyShutdown(ProxyShutdownEvent event) {

        if (broadcaster != null) {
            broadcaster.shutdown();
        }

        logger.info("BetterLANBroadcaster foi desligado.");
    }

    // =========================================================
    // GETTERS
    // =========================================================

    public Logger getLogger() {
        return logger;
    }

    public ProxyServer getServer() {
        return server;
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

    public int getMaxPlayers() {
        return 100;
    }

    // =========================================================
    // MINI MESSAGE
    // =========================================================

    public String formatMiniMessage(String text) {

        try {

            String legacy =
                    net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer
                            .legacySection()
                            .serialize(
                                    net.kyori.adventure.text.minimessage.MiniMessage
                                            .miniMessage()
                                            .deserialize(text)
                            );

            return translateColorCodes(legacy);

        } catch (Exception e) {

            return translateColorCodes(text);
        }
    }

    private String translateColorCodes(String text) {

        return text.replaceAll(
                "&([0-9a-fA-Fk-oK-OrR])",
                "\u00A7$1"
        );
    }
}