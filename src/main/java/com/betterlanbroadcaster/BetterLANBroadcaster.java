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
        version = "1.0.2",
        description = "Velocity port of BetterLANBroadcaster for LAN server discovery",
        authors = {"myxxr", "GabiFurBR"}
)
public class BetterLANBroadcaster {

    public static final String PLUGIN_ID = "betterlanbroadcaster";
    public static final String PERMISSION_ADMIN = "betterlanbroadcaster.admin";
    public static final String VERSION = "1.0.2";

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

    // =========================================================
    // INITIALIZATION
    // =========================================================

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        logger.info(
                "Inicializando BetterLANBroadcaster-Velocity {}...",
                VERSION
        );

        if (!config.load()) {
            logger.error(
                    "Não foi possível carregar a configuração. "
                            + "O plugin será inicializado com os valores padrão."
            );
        }

        language.load(config.getLanguage());

        registerCommands();

        initialized = true;

        logger.info("BetterLANBroadcaster-Velocity foi iniciado!");

        /*
         * broadcast-enabled controla somente o início automático.
         *
         * Isso NÃO impede /blb start de iniciar o broadcaster manualmente.
         */
        if (config.isBroadcastEnabled()) {
            if (!startBroadcaster()) {
                logger.error(
                        "Não foi possível iniciar o LAN Broadcast."
                );
            }
        } else {
            logger.info("LAN Broadcast está desativado.");
        }
    }

    // =========================================================
    // SHUTDOWN
    // =========================================================

    @Subscribe
    public void onProxyShutdown(ProxyShutdownEvent event) {
        initialized = false;

        /*
         * shutdown() é utilizado em vez de stop(), pois o shutdown
         * encerra também o executor/socket interno do broadcaster.
         */
        shutdownBroadcaster();

        logger.info(
                "BetterLANBroadcaster foi desligado."
        );
    }

    // =========================================================
    // COMMAND REGISTRATION
    // =========================================================

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

        logger.debug(
                "Comandos /blb e /betterlanbroadcaster registrados."
        );
    }

    // =========================================================
    // START
    // =========================================================

    /**
     * Inicia o broadcaster utilizando a configuração atual.
     *
     * @return true se o broadcaster foi iniciado com sucesso.
     */
    public synchronized boolean startBroadcaster() {

        if (!initialized) {
            logger.warn(
                    "Não é possível iniciar o broadcaster antes "
                            + "da inicialização do plugin."
            );
            return false;
        }

        /*
         * Se já está executando, não criamos uma segunda instância.
         */
        if (broadcaster != null && broadcaster.isRunning()) {
            return false;
        }

        /*
         * Caso exista um objeto antigo parado, destruímos completamente
         * esse objeto antes de criar um novo.
         */
        if (broadcaster != null) {
            broadcaster.shutdown();
            broadcaster = null;
        }

        int configuredPort = config.getBroadcastPort();

        /*
         * Porta 0 significa:
         * "usar automaticamente a porta em que o Velocity está ouvindo".
         */
        int advertisedPort = configuredPort;

        if (advertisedPort == 0) {
            advertisedPort = server
                    .getBoundAddress()
                    .getPort();
        }

        long configuredDelay = config.getBroadcastDelayMs();

        /*
         * Config garante que o delay esteja dentro do intervalo válido.
         * MulticastBroadcaster trabalha com int.
         */
        int delayMs = (int) configuredDelay;

        try {
            broadcaster = new MulticastBroadcaster(
                    this,
                    config.getMotd(),
                    advertisedPort,
                    delayMs,
                    config.getNetworkInterface()
            );

            broadcaster.setDebug(
                    config.isDebug()
            );

            broadcaster.start();

            logger.info(
                    "LAN Broadcast iniciado na porta {}.",
                    advertisedPort
            );

            return true;

        } catch (Exception e) {

            logger.error(
                    "Não foi possível iniciar o LAN Broadcast.",
                    e
            );

            if (broadcaster != null) {
                broadcaster.shutdown();
                broadcaster = null;
            }

            return false;
        }
    }

    // =========================================================
    // STOP
    // =========================================================

    /**
     * Para o broadcaster mantendo o objeto disponível para
     * compatibilidade com o fluxo de start/stop.
     *
     * @return true se o broadcaster estava rodando e foi parado.
     */
    public synchronized boolean stopBroadcaster() {

        if (broadcaster == null) {
            return false;
        }

        if (!broadcaster.isRunning()) {
            return false;
        }

        broadcaster.stop();

        logger.info(
                "LAN Broadcast parado."
        );

        return true;
    }

    // =========================================================
    // SHUTDOWN
    // =========================================================

    /**
     * Encerra completamente o broadcaster e libera seus recursos.
     */
    public synchronized void shutdownBroadcaster() {

            if (broadcaster == null) {
                return;
            }

            try {
                broadcaster.shutdown();
            } catch (Exception e) {
                logger.warn(
                        "Erro ao encerrar o LAN Broadcast.",
                        e
                );
            } finally {
                broadcaster = null;
            }
        }

        // =========================================================
        // RELOAD
        // =========================================================

        /**
         * Recria completamente o broadcaster utilizando a configuração atual.
         *
         * O valor broadcast-enabled determina se ele deverá ficar rodando
         * após o reload.
         *
         * @return true se o reload foi concluído com sucesso.
         */
        public synchronized boolean reloadBroadcaster() {
        shutdownBroadcaster();

        if (!config.isBroadcastEnabled()) {
            logger.info("LAN Broadcast está desativado após o reload.");
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

    // =========================================================
    // MAX PLAYERS
    // =========================================================

    /**
     * Retorna o número máximo de jogadores utilizado pelo Velocity.
     *
     * @return limite máximo de jogadores.
     */
    public int getMaxPlayers() {

        try {
            int maxPlayers = server
                    .getConfiguration()
                    .getShowMaxPlayers();

            if (maxPlayers > 0) {
                return maxPlayers;
            }

        } catch (Exception e) {

            logger.debug(
                    "Não foi possível obter o limite de jogadores "
                            + "da configuração do Velocity.",
                    e
            );
        }

        /*
         * Fallback para compatibilidade.
         */
        return 100;
    }

    // =========================================================
    // GETTERS
    // =========================================================

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

    // =========================================================
    // MINIMESSAGE / LEGACY
    // =========================================================

    /**
     * Converte mensagens que utilizam MiniMessage para legacy.
     */
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

            logger.debug(
                    "Não foi possível interpretar MiniMessage: {}",
                    text,
                    e
            );

            return translateColorCodes(text);
        }
    }

    /**
     * Converte códigos de cor no formato &a, &l, etc.
     */
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