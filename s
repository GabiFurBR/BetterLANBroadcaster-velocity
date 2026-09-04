warning: in the working copy of 'src/main/java/com/betterlanbroadcaster/BetterLANBroadcaster.java', LF will be replaced by CRLF the next time Git touches it
warning: in the working copy of 'src/main/java/com/betterlanbroadcaster/MulticastBroadcaster.java', LF will be replaced by CRLF the next time Git touches it
warning: in the working copy of 'src/main/resources/lang/messages_en.yml', LF will be replaced by CRLF the next time Git touches it
warning: in the working copy of 'src/main/resources/lang/messages_zh.yml', LF will be replaced by CRLF the next time Git touches it
[1mdiff --git a/.github/workflows/build.yml b/.github/workflows/build.yml[m
[1mindex 8fa409c..8b08af4 100644[m
[1m--- a/.github/workflows/build.yml[m
[1m+++ b/.github/workflows/build.yml[m
[36m@@ -1,61 +1,61 @@[m
[31m-name: Build[m
[31m-[m
[31m-on:[m
[31m-  push:[m
[31m-    branches: [ main ][m
[31m-    tags: [ 'v*' ][m
[31m-  pull_request:[m
[31m-    branches: [ main ][m
[31m-  workflow_dispatch:[m
[31m-[m
[31m-permissions:[m
[31m-  contents: read[m
[31m-[m
[31m-jobs:[m
[31m-  build:[m
[31m-    name: Build with Maven[m
[31m-    runs-on: ubuntu-latest[m
[31m-[m
[31m-    steps:[m
[31m-      - name: Checkout repository[m
[31m-        uses: actions/checkout@v4[m
[31m-[m
[31m-      - name: Set up JDK 21[m
[31m-        uses: actions/setup-java@v4[m
[31m-        with:[m
[31m-          java-version: '21'[m
[31m-          distribution: 'temurin'[m
[31m-          cache: maven[m
[31m-[m
[31m-      - name: Build with Maven[m
[31m-        run: mvn clean package -DskipTests --no-transfer-progress[m
[31m-[m
[31m-      - name: Upload build artifact[m
[31m-        uses: actions/upload-artifact@v5[m
[31m-        with:[m
[31m-          name: BetterLANBroadcaster-Velocity[m
[31m-          path: target/*.jar[m
[31m-          if-no-files-found: error[m
[31m-[m
[31m-  release:[m
[31m-    name: Create GitHub Release[m
[31m-    needs: build[m
[31m-    if: startsWith(github.ref, 'refs/tags/v')[m
[31m-[m
[31m-    runs-on: ubuntu-latest[m
[31m-[m
[31m-    permissions:[m
[31m-      contents: write[m
[31m-[m
[31m-    steps:[m
[31m-      - name: Download build artifact[m
[31m-        uses: actions/download-artifact@v5[m
[31m-        with:[m
[31m-          name: BetterLANBroadcaster-Velocity[m
[31m-          path: release[m
[31m-[m
[31m-      - name: Create Release[m
[31m-        uses: softprops/action-gh-release@v2[m
[31m-        with:[m
[31m-          files: release/*.jar[m
[31m-          generate_release_notes: true[m
\ No newline at end of file[m
[32m+[m[32m  name: Build[m
[32m+[m
[32m+[m[32m  on:[m
[32m+[m[32m    push:[m
[32m+[m[32m      branches: [ main ][m
[32m+[m[32m      tags: [ 'v*' ][m
[32m+[m[32m    pull_request:[m
[32m+[m[32m      branches: [ main ][m
[32m+[m[32m    workflow_dispatch:[m
[32m+[m
[32m+[m[32m  permissions:[m
[32m+[m[32m    contents: read[m
[32m+[m
[32m+[m[32m  jobs:[m
[32m+[m[32m    build:[m
[32m+[m[32m      name: Build with Maven[m
[32m+[m[32m      runs-on: ubuntu-latest[m
[32m+[m
[32m+[m[32m      steps:[m
[32m+[m[32m        - name: Checkout repository[m
[32m+[m[32m          uses: actions/checkout@v5[m
[32m+[m
[32m+[m[32m        - name: Set up JDK 21[m
[32m+[m[32m          uses: actions/setup-java@v5[m
[32m+[m[32m          with:[m
[32m+[m[32m            java-version: '21'[m
[32m+[m[32m            distribution: 'temurin'[m
[32m+[m[32m            cache: maven[m
[32m+[m
[32m+[m[32m        - name: Build with Maven[m
[32m+[m[32m          run: mvn clean package -DskipTests --no-transfer-progress[m
[32m+[m
[32m+[m[32m        - name: Upload build artifact[m
[32m+[m[32m          uses: actions/upload-artifact@v5[m
[32m+[m[32m          with:[m
[32m+[m[32m            name: BetterLANBroadcaster-Velocity[m
[32m+[m[32m            path: target/*.jar[m
[32m+[m[32m            if-no-files-found: error[m
[32m+[m
[32m+[m[32m    release:[m
[32m+[m[32m      name: Create GitHub Release[m
[32m+[m[32m      needs: build[m
[32m+[m[32m      if: startsWith(github.ref, 'refs/tags/v')[m
[32m+[m
[32m+[m[32m      runs-on: ubuntu-latest[m
[32m+[m
[32m+[m[32m      permissions:[m
[32m+[m[32m        contents: write[m
[32m+[m
[32m+[m[32m      steps:[m
[32m+[m[32m        - name: Download build artifact[m
[32m+[m[32m          uses: actions/download-artifact@v5[m
[32m+[m[32m          with:[m
[32m+[m[32m            name: BetterLANBroadcaster-Velocity[m
[32m+[m[32m            path: release[m
[32m+[m
[32m+[m[32m        - name: Create Release[m
[32m+[m[32m          uses: softprops/action-gh-release@v2[m
[32m+[m[32m          with:[m
[32m+[m[32m            files: release/*.jar[m
[32m+[m[32m            generate_release_notes: true[m
\ No newline at end of file[m
[1mdiff --git a/src/main/java/com/betterlanbroadcaster/BetterLANBroadcaster.java b/src/main/java/com/betterlanbroadcaster/BetterLANBroadcaster.java[m
[1mindex 432f22b..61a10ac 100644[m
[1m--- a/src/main/java/com/betterlanbroadcaster/BetterLANBroadcaster.java[m
[1m+++ b/src/main/java/com/betterlanbroadcaster/BetterLANBroadcaster.java[m
[36m@@ -1,6 +1,7 @@[m
 package com.betterlanbroadcaster;[m
 [m
 import com.google.inject.Inject;[m
[32m+[m[32mimport com.velocitypowered.api.command.CommandManager;[m
 import com.velocitypowered.api.event.Subscribe;[m
 import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;[m
 import com.velocitypowered.api.event.proxy.ProxyShutdownEvent;[m
[36m@@ -12,21 +13,25 @@[m [mimport org.slf4j.Logger;[m
 import java.nio.file.Path;[m
 [m
 @Plugin([m
[31m-    id = "betterlanbroadcaster",[m
[31m-    name = "BetterLANBroadcaster",[m
[31m-    version = "1.0.2",[m
[31m-    description = "Velocity port of BetterLANBroadcaster for LAN server discovery",[m
[31m-    authors = {"myxxr", "GabiFurBR"}[m
[32m+[m[32m        id = BetterLANBroadcaster.PLUGIN_ID,[m
[32m+[m[32m        name = "BetterLANBroadcaster",[m
[32m+[m[32m        version = "1.0.2",[m
[32m+[m[32m        description = "Velocity port of BetterLANBroadcaster for LAN server discovery",[m
[32m+[m[32m        authors = {"myxxr", "GabiFurBR"}[m
 )[m
 public class BetterLANBroadcaster {[m
 [m
[32m+[m[32m    public static final String PLUGIN_ID = "betterlanbroadcaster";[m
[32m+[m[32m    public static final String PERMISSION_ADMIN = "betterlanbroadcaster.admin";[m
[32m+[m[32m    public static final String VERSION = "1.0.2";[m
[32m+[m
     private final ProxyServer server;[m
     private final Logger logger;[m
[31m-[m
     private final Config config;[m
     private final Language language;[m
 [m
     private MulticastBroadcaster broadcaster;[m
[32m+[m[32m    private boolean initialized;[m
 [m
     @Inject[m
     public BetterLANBroadcaster([m
[36m@@ -36,71 +41,325 @@[m [mpublic class BetterLANBroadcaster {[m
     ) {[m
         this.server = server;[m
         this.logger = logger;[m
[31m-[m
         this.config = new Config(this, dataDirectory);[m
         this.language = new Language(this);[m
[32m+[m[32m        this.initialized = false;[m
     }[m
 [m
[32m+[m[32m    // =========================================================[m
[32m+[m[32m    // INITIALIZATION[m
[32m+[m[32m    // =========================================================[m
[32m+[m
     @Subscribe[m
     public void onProxyInitialization(ProxyInitializeEvent event) {[m
[31m-        [m
[31m-[m
[31m-        server.getCommandManager().register([m
[31m-            server.getCommandManager().metaBuilder("blb").build(),[m
[31m-            new CommandHandler(this)[m
[32m+[m[32m        logger.info([m
[32m+[m[32m                "Inicializando BetterLANBroadcaster-Velocity {}...",[m
[32m+[m[32m                VERSION[m
         );[m
 [m
[31m-        config.load();[m
[32m+[m[32m        if (!config.load()) {[m
[32m+[m[32m            logger.error([m
[32m+[m[32m                    "Não foi possível carregar a configuração. "[m
[32m+[m[32m                            + "O plugin será inicializado com os valores padrão."[m
[32m+[m[32m            );[m
[32m+[m[32m        }[m
 [m
         language.load(config.getLanguage());[m
 [m
[32m+[m[32m        registerCommands();[m
[32m+[m
[32m+[m[32m        initialized = true;[m
[32m+[m
         logger.info("BetterLANBroadcaster-Velocity foi iniciado!");[m
 [m
[32m+[m[32m        /*[m
[32m+[m[32m         * broadcast-enabled controla somente o início automático.[m
[32m+[m[32m         *[m
[32m+[m[32m         * Isso NÃO impede /blb start de iniciar o broadcaster manualmente.[m
[32m+[m[32m         */[m
         if (config.isBroadcastEnabled()) {[m
[32m+[m[32m            if (!startBroadcaster()) {[m
[32m+[m[32m                logger.error([m
[32m+[m[32m                        "Não foi possível iniciar o LAN Broadcast."[m
[32m+[m[32m                );[m
[32m+[m[32m            }[m
[32m+[m[32m        } else {[m
[32m+[m[32m            logger.info("LAN Broadcast está desativado.");[m
[32m+[m[32m        }[m
[32m+[m[32m    }[m
 [m
[31m-            int port = config.getBroadcastPort();[m
[32m+[m[32m    // =========================================================[m
[32m+[m[32m    // SHUTDOWN[m
[32m+[m[32m    // =========================================================[m
 [m
[31m-            if (port <= 0) {[m
[31m-                port = server.getBoundAddress().getPort();[m
[31m-            }[m
[32m+[m[32m    @Subscribe[m
[32m+[m[32m    public void onProxyShutdown(ProxyShutdownEvent event) {[m
[32m+[m[32m        initialized = false;[m
[32m+[m
[32m+[m[32m        /*[m
[32m+[m[32m         * shutdown() é utilizado em vez de stop(), pois o shutdown[m
[32m+[m[32m         * encerra também o executor/socket interno do broadcaster.[m
[32m+[m[32m         */[m
[32m+[m[32m        shutdownBroadcaster();[m
[32m+[m
[32m+[m[32m        logger.info([m
[32m+[m[32m                "BetterLANBroadcaster foi desligado."[m
[32m+[m[32m        );[m
[32m+[m[32m    }[m
[32m+[m
[32m+[m[32m    // =========================================================[m
[32m+[m[32m    // COMMAND REGISTRATION[m
[32m+[m[32m    // =========================================================[m
[32m+[m
[32m+[m[32m    private void registerCommands() {[m
[32m+[m[32m        CommandManager commandManager = server.getCommandManager();[m
[32m+[m
[32m+[m[32m        CommandHandler handler = new CommandHandler(this);[m
[32m+[m
[32m+[m[32m        commandManager.register([m
[32m+[m[32m                commandManager.metaBuilder("blb")[m
[32m+[m[32m                        .aliases("betterlanbroadcaster")[m
[32m+[m[32m                        .plugin(this)[m
[32m+[m[32m                        .build(),[m
[32m+[m[32m                handler[m
[32m+[m[32m        );[m
[32m+[m
[32m+[m[32m        logger.debug([m
[32m+[m[32m                "Comandos /blb e /betterlanbroadcaster registrados."[m
[32m+[m[32m        );[m
[32m+[m[32m    }[m
 [m
[32m+[m[32m    // =========================================================[m
[32m+[m[32m    // START[m
[32m+[m[32m    // =========================================================[m
[32m+[m
[32m+[m[32m    /**[m
[32m+[m[32m     * Inicia o broadcaster utilizando a configuração atual.[m
[32m+[m[32m     *[m
[32m+[m[32m     * @return true se o broadcaster foi iniciado com sucesso.[m
[32m+[m[32m     */[m
[32m+[m[32m    public synchronized boolean startBroadcaster() {[m
[32m+[m
[32m+[m[32m        if (!initialized) {[m
[32m+[m[32m            logger.warn([m
[32m+[m[32m                    "Não é possível iniciar o broadcaster antes "[m
[32m+[m[32m                            + "da inicialização do plugin."[m
[32m+[m[32m            );[m
[32m+[m[32m            return false;[m
[32m+[m[32m        }[m
[32m+[m
[32m+[m[32m        /*[m
[32m+[m[32m         * Se já está executando, não criamos uma segunda instância.[m
[32m+[m[32m         */[m
[32m+[m[32m        if (broadcaster != null && broadcaster.isRunning()) {[m
[32m+[m[32m            return false;[m
[32m+[m[32m        }[m
[32m+[m
[32m+[m[32m        /*[m
[32m+[m[32m         * Caso exista um objeto antigo parado, destruímos completamente[m
[32m+[m[32m         * esse objeto antes de criar um novo.[m
[32m+[m[32m         */[m
[32m+[m[32m        if (broadcaster != null) {[m
[32m+[m[32m            broadcaster.shutdown();[m
[32m+[m[32m            broadcaster = null;[m
[32m+[m[32m        }[m
[32m+[m
[32m+[m[32m        int configuredPort = config.getBroadcastPort();[m
[32m+[m
[32m+[m[32m        /*[m
[32m+[m[32m         * Porta 0 significa:[m
[32m+[m[32m         * "usar automaticamente a porta em que o Velocity está ouvindo".[m
[32m+[m[32m         */[m
[32m+[m[32m        int advertisedPort = configuredPort;[m
[32m+[m
[32m+[m[32m        if (advertisedPort == 0) {[m
[32m+[m[32m            advertisedPort = server[m
[32m+[m[32m                    .getBoundAddress()[m
[32m+[m[32m                    .getPort();[m
[32m+[m[32m        }[m
[32m+[m
[32m+[m[32m        long configuredDelay = config.getBroadcastDelayMs();[m
[32m+[m
[32m+[m[32m        /*[m
[32m+[m[32m         * Config garante que o delay esteja dentro do intervalo válido.[m
[32m+[m[32m         * MulticastBroadcaster trabalha com int.[m
[32m+[m[32m         */[m
[32m+[m[32m        int delayMs = (int) configuredDelay;[m
[32m+[m
[32m+[m[32m        try {[m
             broadcaster = new MulticastBroadcaster([m
                     this,[m
                     config.getMotd(),[m
[31m-                    port,[m
[31m-                    (int) config.getBroadcastDelayMs()[m
[32m+[m[32m                    advertisedPort,[m
[32m+[m[32m                    delayMs,[m
[32m+[m[32m                    config.getNetworkInterface()[m
             );[m
 [m
[31m-            broadcaster.setDebug(config.isDebug());[m
[31m-[m
[31m-            logger.info("Broadcasting servidor na porta " + port);[m
[32m+[m[32m            broadcaster.setDebug([m
[32m+[m[32m                    config.isDebug()[m
[32m+[m[32m            );[m
 [m
             broadcaster.start();[m
 [m
[31m-        } else {[m
[32m+[m[32m            logger.info([m
[32m+[m[32m                    "LAN Broadcast iniciado na porta {}.",[m
[32m+[m[32m                    advertisedPort[m
[32m+[m[32m            );[m
 [m
[31m-            logger.info("LAN Broadcast está desativado.");[m
[32m+[m[32m            return true;[m
[32m+[m
[32m+[m[32m        } catch (Exception e) {[m
[32m+[m
[32m+[m[32m            logger.error([m
[32m+[m[32m                    "Não foi possível iniciar o LAN Broadcast.",[m
[32m+[m[32m                    e[m
[32m+[m[32m            );[m
[32m+[m
[32m+[m[32m            if (broadcaster != null) {[m
[32m+[m[32m                broadcaster.shutdown();[m
[32m+[m[32m                broadcaster = null;[m
[32m+[m[32m            }[m
[32m+[m
[32m+[m[32m            return false;[m
         }[m
     }[m
 [m
[31m-    @Subscribe[m
[31m-    public void onProxyShutdown(ProxyShutdownEvent event) {[m
[32m+[m[32m    // =========================================================[m
[32m+[m[32m    // STOP[m
[32m+[m[32m    // =========================================================[m
 [m
[31m-        if (broadcaster != null) {[m
[31m-            broadcaster.shutdown();[m
[32m+[m[32m    /**[m
[32m+[m[32m     * Para o broadcaster mantendo o objeto disponível para[m
[32m+[m[32m     * compatibilidade com o fluxo de start/stop.[m
[32m+[m[32m     *[m
[32m+[m[32m     * @return true se o broadcaster estava rodando e foi parado.[m
[32m+[m[32m     */[m
[32m+[m[32m    public synchronized boolean stopBroadcaster() {[m
[32m+[m
[32m+[m[32m        if (broadcaster == null) {[m
[32m+[m[32m            return false;[m
         }[m
 [m
[31m-        logger.info("BetterLANBroadcaster foi desligado.");[m
[32m+[m[32m        if (!broadcaster.isRunning()) {[m
[32m+[m[32m            return false;[m
[32m+[m[32m        }[m
[32m+[m
[32m+[m[32m        broadcaster.stop();[m
[32m+[m
[32m+[m[32m        logger.info([m
[32m+[m[32m                "LAN Broadcast parado."[m
[32m+[m[32m        );[m
[32m+[m
[32m+[m[32m        return true;[m
     }[m
 [m
[31m-    public Logger getLogger() {[m
[31m-        return logger;[m
[32m+[m[32m    // =========================================================[m
[32m+[m[32m    // SHUTDOWN[m
[32m+[m[32m    // =========================================================[m
[32m+[m
[32m+[m[32m    /**[m
[32m+[m[32m     * Encerra completamente o broadcaster e libera seus recursos.[m
[32m+[m[32m     */[m
[32m+[m[32m    public synchronized void shutdownBroadcaster() {[m
[32m+[m
[32m+[m[32m            if (broadcaster == null) {[m
[32m+[m[32m                return;[m
[32m+[m[32m            }[m
[32m+[m
[32m+[m[32m            try {[m
[32m+[m[32m                broadcaster.shutdown();[m
[32m+[m[32m            } catch (Exception e) {[m
[32m+[m[32m                logger.warn([m
[32m+[m[32m                        "Erro ao encerrar o LAN Broadcast.",[m
[32m+[m[32m                        e[m
[32m+[m[32m                );[m
[32m+[m[32m            } finally {[m
[32m+[m[32m                broadcaster = null;[m
[32m+[m[32m            }[m
[32m+[m[32m        }[m
[32m+[m
[32m+[m[32m        // =========================================================[m
[32m+[m[32m        // RELOAD[m
[32m+[m[32m        // =========================================================[m
[32m+[m
[32m+[m[32m        /**[m
[32m+[m[32m         * Recria completamente o broadcaster utilizando a configuração atual.[m
[32m+[m[32m         *[m
[32m+[m[32m         * O valor broadcast-enabled determina se ele deverá ficar rodando[m
[32m+[m[32m         * após o reload.[m
[32m+[m[32m         *[m
[32m+[m[32m         * @return true se o reload foi concluído com sucesso.[m
[32m+[m[32m         */[m
[32m+[m[32m        public synchronized boolean reloadBroadcaster() {[m
[32m+[m[32m        shutdownBroadcaster();[m
[32m+[m
[32m+[m[32m        if (!config.isBroadcastEnabled()) {[m
[32m+[m[32m            logger.info("LAN Broadcast está desativado após o reload.");[m
[32m+[m[32m            return true;[m
[32m+[m[32m        }[m
[32m+[m
[32m+[m[32m        return startBroadcaster();[m
[32m+[m[32m    }[m
[32m+[m
[32m+[m[32m    public synchronized boolean reconfigureBroadcaster() {[m
[32m+[m[32m        boolean wasRunning = broadcaster != null && broadcaster.isRunning();[m
[32m+[m
[32m+[m[32m        if (!wasRunning) {[m
[32m+[m[32m            return true;[m
[32m+[m[32m        }[m
[32m+[m
[32m+[m[32m        shutdownBroadcaster();[m
[32m+[m
[32m+[m[32m        return startBroadcaster();[m
     }[m
 [m
[32m+[m[32m    // =========================================================[m
[32m+[m[32m    // MAX PLAYERS[m
[32m+[m[32m    // =========================================================[m
[32m+[m
[32m+[m[32m    /**[m
[32m+[m[32m     * Retorna o número máximo de jogadores utilizado pelo Velocity.[m
[32m+[m[32m     *[m
[32m+[m[32m     * @return limite máximo de jogadores.[m
[32m+[m[32m     */[m
[32m+[m[32m    public int getMaxPlayers() {[m
[32m+[m
[32m+[m[32m        try {[m
[32m+[m[32m            int maxPlayers = server[m
[32m+[m[32m                    .getConfiguration()[m
[32m+[m[32m                    .getShowMaxPlayers();[m
[32m+[m
[32m+[m[32m            if (maxPlayers > 0) {[m
[32m+[m[32m                return maxPlayers;[m
[32m+[m[32m            }[m
[32m+[m
[32m+[m[32m        } catch (Exception e) {[m
[32m+[m
[32m+[m[32m            logger.debug([m
[32m+[m[32m                    "Não foi possível obter o limite de jogadores "[m
[32m+[m[32m                            + "da configuração do Velocity.",[m
[32m+[m[32m                    e[m
[32m+[m[32m            );[m
[32m+[m[32m        }[m
[32m+[m
[32m+[m[32m        /*[m
[32m+[m[32m         * Fallback para compatibilidade.[m
[32m+[m[32m         */[m
[32m+[m[32m        return 100;[m
[32m+[m[32m    }[m
[32m+[m
[32m+[m[32m    // =========================================================[m
[32m+[m[32m    // GETTERS[m
[32m+[m[32m    // =========================================================[m
[32m+[m
     public ProxyServer getServer() {[m
         return server;[m
     }[m
 [m
[32m+[m[32m    public Logger getLogger() {[m
[32m+[m[32m        return logger;[m
[32m+[m[32m    }[m
[32m+[m
     public Config getConfig() {[m
         return config;[m
     }[m
[36m@@ -113,19 +372,36 @@[m [mpublic class BetterLANBroadcaster {[m
         return broadcaster;[m
     }[m
 [m
[31m-    public int getMaxPlayers() {[m
[31m-        return 100;[m
[32m+[m[32m    public boolean isInitialized() {[m
[32m+[m[32m        return initialized;[m
[32m+[m[32m    }[m
[32m+[m
[32m+[m[32m    public String getVersion() {[m
[32m+[m[32m        return VERSION;[m
     }[m
 [m
[32m+[m[32m    // =========================================================[m
[32m+[m[32m    // MINIMESSAGE / LEGACY[m
[32m+[m[32m    // =========================================================[m
[32m+[m
[32m+[m[32m    /**[m
[32m+[m[32m     * Converte mensagens que utilizam MiniMessage para legacy.[m
[32m+[m[32m     */[m
     public String formatMiniMessage(String text) {[m
 [m
[32m+[m[32m        if (text == null || text.isEmpty()) {[m
[32m+[m[32m            return "";[m
[32m+[m[32m        }[m
[32m+[m
         try {[m
 [m
             String legacy =[m
[31m-                    net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer[m
[32m+[m[32m                    net.kyori.adventure.text.serializer.legacy[m
[32m+[m[32m                            .LegacyComponentSerializer[m
                             .legacySection()[m
                             .serialize([m
[31m-                                    net.kyori.adventure.text.minimessage.MiniMessage[m
[32m+[m[32m                                    net.kyori.adventure.text.minimessage[m
[32m+[m[32m                                            .MiniMessage[m
                                             .miniMessage()[m
                                             .deserialize(text)[m
                             );[m
[36m@@ -134,12 +410,25 @@[m [mpublic class BetterLANBroadcaster {[m
 [m
         } catch (Exception e) {[m
 [m
[32m+[m[32m            logger.debug([m
[32m+[m[32m                    "Não foi possível interpretar MiniMessage: {}",[m
[32m+[m[32m                    text,[m
[32m+[m[32m                    e[m
[32m+[m[32m            );[m
[32m+[m
             return translateColorCodes(text);[m
         }[m
     }[m
 [m
[32m+[m[32m    /**[m
[32m+[m[32m     * Converte códigos de cor no formato &a, &l, etc.[m
[32m+[m[32m     */[m
     private String translateColorCodes(String text) {[m
 [m
[32m+[m[32m        if (text == null || text.isEmpty()) {[m
[32m+[m[32m            return "";[m
[32m+[m[32m        }[m
[32m+[m
         return text.replaceAll([m
                 "&([0-9a-fA-Fk-oK-OrR])",[m
                 "\u00A7$1"[m
[1mdiff --git a/src/main/java/com/betterlanbroadcaster/CommandHandler.java b/src/main/java/com/betterlanbroadcaster/CommandHandler.java[m
[1mindex e36f9ef..480481a 100644[m
[1m--- a/src/main/java/com/betterlanbroadcaster/CommandHandler.java[m
[1m+++ b/src/main/java/com/betterlanbroadcaster/CommandHandler.java[m
[36m@@ -1,26 +1,30 @@[m
 package com.betterlanbroadcaster;[m
 [m
[32m+[m[32mimport com.velocitypowered.api.command.CommandSource;[m
 import com.velocitypowered.api.command.SimpleCommand;[m
[32m+[m[32mimport net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;[m
 [m
[31m-import java.util.ArrayList;[m
 import java.util.Arrays;[m
 import java.util.List;[m
[32m+[m[32mimport java.util.Locale;[m
 import java.util.stream.Collectors;[m
[31m-@SuppressWarnings("unused")[m
[32m+[m
 public class CommandHandler implements SimpleCommand {[m
 [m
[31m-    private static final List<String> SUBCOMMANDS = Arrays.asList([m
[31m-            "start",[m
[31m-            "stop",[m
[31m-            "status",[m
[31m-            "setmotd",[m
[31m-            "setdelay",[m
[31m-            "setport",[m
[31m-            "debug",[m
[31m-            "reload",[m
[31m-            "help",[m
[31m-            "version"[m
[31m-    );[m
[32m+[m[32m    private static final List<String> SUBCOMMANDS =[m
[32m+[m[32m            Arrays.asList([m
[32m+[m[32m                    "start",[m
[32m+[m[32m                    "stop",[m
[32m+[m[32m                    "status",[m
[32m+[m[32m                    "setmotd",[m
[32m+[m[32m                    "setdelay",[m
[32m+[m[32m                    "setport",[m
[32m+[m[32m                    "setinterface",[m
[32m+[m[32m                    "debug",[m
[32m+[m[32m                    "reload",[m
[32m+[m[32m                    "help",[m
[32m+[m[32m                    "version"[m
[32m+[m[32m            );[m
 [m
     private final BetterLANBroadcaster plugin;[m
 [m
[36m@@ -29,31 +33,35 @@[m [mpublic class CommandHandler implements SimpleCommand {[m
     }[m
 [m
     @Override[m
[31m-    public void execute(Invocation invocation) {[m
[32m+[m[32m    public boolean hasPermission(Invocation invocation) {[m
[32m+[m[32m        return invocation.source().hasPermission([m
[32m+[m[32m                BetterLANBroadcaster.PERMISSION_ADMIN[m
[32m+[m[32m        );[m
[32m+[m[32m    }[m
 [m
[31m-        // Quem executou o comando[m
[31m-        var source = invocation.source();[m
[32m+[m[32m    @Override[m
[32m+[m[32m    public void execute(Invocation invocation) {[m
 [m
[31m-        // Argumentos[m
[32m+[m[32m        CommandSource source = invocation.source();[m
         String[] args = invocation.arguments();[m
 [m
[31m-        // Verifica permissão[m
[31m-        if (!source.hasPermission("betterlanbroadcaster.admin")) {[m
[31m-            source.sendMessage([m
[31m-                    net.kyori.adventure.text.Component.text([m
[31m-                            "Você não tem permissão para executar este comando."[m
[31m-                    )[m
[32m+[m[32m        if (!hasPermission(invocation)) {[m
[32m+[m[32m            send([m
[32m+[m[32m                    source,[m
[32m+[m[32m                    "error.no-permission"[m
             );[m
             return;[m
         }[m
 [m
[31m-        // Sem argumentos = version[m
         if (args.length == 0) {[m
[31m-            handleVersion(source);[m
[32m+[m[32m            handleHelp(source);[m
             return;[m
         }[m
 [m
[31m-        switch (args[0].toLowerCase()) {[m
[32m+[m[32m        String subcommand =[m
[32m+[m[32m                args[0].toLowerCase(Locale.ROOT);[m
[32m+[m
[32m+[m[32m        switch (subcommand) {[m
 [m
             case "start":[m
                 handleStart(source);[m
[36m@@ -79,6 +87,10 @@[m [mpublic class CommandHandler implements SimpleCommand {[m
                 handleSetPort(source, args);[m
                 break;[m
 [m
[32m+[m[32m            case "setinterface":[m
[32m+[m[32m                handleSetInterface(source, args);[m
[32m+[m[32m                break;[m
[32m+[m
             case "debug":[m
                 handleDebug(source, args);[m
                 break;[m
[36m@@ -96,86 +108,864 @@[m [mpublic class CommandHandler implements SimpleCommand {[m
                 break;[m
 [m
             default:[m
[31m-                source.sendMessage([m
[31m-                        net.kyori.adventure.text.Component.text([m
[31m-                                "Sintaxe inválida."[m
[31m-                        )[m
[32m+[m[32m                send([m
[32m+[m[32m                        source,[m
[32m+[m[32m                        "error.invalid-syntax"[m
                 );[m
                 break;[m
         }[m
     }[m
 [m
[31m-    private void handleStart(Object source) {[m
[31m-        // vamos implementar[m
[32m+[m[32m    // =========================================================[m
[32m+[m[32m    // START[m
[32m+[m[32m    // =========================================================[m
[32m+[m
[32m+[m[32m    private void handleStart(CommandSource source) {[m
[32m+[m
[32m+[m[32m        /*[m
[32m+[m[32m         * broadcast-enabled NÃO bloqueia mais o /blb start.[m
[32m+[m[32m         *[m
[32m+[m[32m         * Essa configuração controla somente o início automático[m
[32m+[m[32m         * do plugin durante o ProxyInitializeEvent.[m
[32m+[m[32m         */[m
[32m+[m
[32m+[m[32m        if (plugin.getBroadcaster() != null[m
[32m+[m[32m                && plugin.getBroadcaster().isRunning()) {[m
[32m+[m
[32m+[m[32m            send([m
[32m+[m[32m                    source,[m
[32m+[m[32m                    "broadcast.already-running"[m
[32m+[m[32m            );[m
[32m+[m[32m            return;[m
[32m+[m[32m        }[m
[32m+[m
[32m+[m[32m        boolean started =[m
[32m+[m[32m                plugin.startBroadcaster();[m
[32m+[m
[32m+[m[32m        if (started) {[m
[32m+[m
[32m+[m[32m            send([m
[32m+[m[32m                    source,[m
[32m+[m[32m                    "broadcast.started"[m
[32m+[m[32m            );[m
[32m+[m
[32m+[m[32m        } else {[m
[32m+[m
[32m+[m[32m            send([m
[32m+[m[32m                    source,[m
[32m+[m[32m                    "error.start-failed"[m
[32m+[m[32m            );[m
[32m+[m[32m        }[m
     }[m
 [m
[31m-    private void handleStop(Object source) {[m
[31m-        // vamos implementar[m
[32m+[m[32m    // =========================================================[m
[32m+[m[32m    // STOP[m
[32m+[m[32m    // =========================================================[m
[32m+[m
[32m+[m[32m    private void handleStop(CommandSource source) {[m
[32m+[m
[32m+[m[32m        if (plugin.getBroadcaster() == null[m
[32m+[m[32m                || !plugin.getBroadcaster().isRunning()) {[m
[32m+[m
[32m+[m[32m            send([m
[32m+[m[32m                    source,[m
[32m+[m[32m                    "broadcast.already-stopped"[m
[32m+[m[32m            );[m
[32m+[m[32m            return;[m
[32m+[m[32m        }[m
[32m+[m
[32m+[m[32m        boolean stopped =[m
[32m+[m[32m                plugin.stopBroadcaster();[m
[32m+[m
[32m+[m[32m        if (stopped) {[m
[32m+[m
[32m+[m[32m            send([m
[32m+[m[32m                    source,[m
[32m+[m[32m                    "broadcast.stopped"[m
[32m+[m[32m            );[m
[32m+[m
[32m+[m[32m        } else {[m
[32m+[m
[32m+[m[32m            send([m
[32m+[m[32m                    source,[m
[32m+[m[32m                    "error.stop-failed"[m
[32m+[m[32m            );[m
[32m+[m[32m        }[m
     }[m
 [m
[31m-    private void handleStatus(Object source) {[m
[31m-        // vamos implementar[m
[32m+[m[32m    // =========================================================[m
[32m+[m[32m    // STATUS[m
[32m+[m[32m    // =========================================================[m
[32m+[m
[32m+[m[32m    private void handleStatus(CommandSource source) {[m
[32m+[m[32m        MulticastBroadcaster broadcaster = plugin.getBroadcaster();[m
[32m+[m
[32m+[m[32m        String status;[m
[32m+[m
[32m+[m[32m        if (broadcaster != null && broadcaster.isRunning()) {[m
[32m+[m[32m            status = plugin.getLanguage().get("broadcast.status.running");[m
[32m+[m[32m        } else {[m
[32m+[m[32m            status = plugin.getLanguage().get("broadcast.status.stopped");[m
[32m+[m[32m        }[m
[32m+[m
[32m+[m[32m        sendRaw([m
[32m+[m[32m                source,[m
[32m+[m[32m                plugin.getLanguage().get("prefix") + status[m
[32m+[m[32m        );[m
[32m+[m
[32m+[m[32m        send([m
[32m+[m[32m                source,[m
[32m+[m[32m                "broadcast.status.motd",[m
[32m+[m[32m                plugin.getConfig().getMotd()[m
[32m+[m[32m        );[m
[32m+[m
[32m+[m[32m        send([m
[32m+[m[32m                source,[m
[32m+[m[32m                "broadcast.status.delay",[m
[32m+[m[32m                plugin.getConfig().getBroadcastDelayMs()[m
[32m+[m[32m        );[m
[32m+[m
[32m+[m[32m        int port = plugin.getConfig().getBroadcastPort();[m
[32m+[m
[32m+[m[32m        if (port == 0 && broadcaster != null && broadcaster.getPort() > 0) {[m
[32m+[m[32m            port = broadcaster.getPort();[m
[32m+[m[32m        }[m
[32m+[m
[32m+[m[32m        send([m
[32m+[m[32m                source,[m
[32m+[m[32m                "broadcast.status.port",[m
[32m+[m[32m                port[m
[32m+[m[32m        );[m
[32m+[m
[32m+[m[32m        send([m
[32m+[m[32m                source,[m
[32m+[m[32m                "broadcast.status.interface",[m
[32m+[m[32m                plugin.getConfig().getNetworkInterface()[m
[32m+[m[32m        );[m
[32m+[m
[32m+[m[32m        send([m
[32m+[m[32m                source,[m
[32m+[m[32m                "debug.label",[m
[32m+[m[32m                plugin.getConfig().isDebug()[m
[32m+[m[32m                        ? plugin.getLanguage().get("debug.status-on")[m
[32m+[m[32m                        : plugin.getLanguage().get("debug.status-off")[m
[32m+[m[32m        );[m
     }[m
 [m
[31m-    private void handleSetMotd(Object source, String[] args) {[m
[31m-        // vamos implementar[m
[32m+[m[32m    // =========================================================[m
[32m+[m[32m    // SET MOTD[m
[32m+[m[32m    // =========================================================[m
[32m+[m
[32m+[m[32m    private void handleSetMotd([m
[32m+[m[32m            CommandSource source,[m
[32m+[m[32m            String[] args[m
[32m+[m[32m    ) {[m
[32m+[m
[32m+[m[32m        if (args.length < 2) {[m
[32m+[m
[32m+[m[32m            send([m
[32m+[m[32m                    source,[m
[32m+[m[32m                    "error.invalid-syntax"[m
[32m+[m[32m            );[m
[32m+[m[32m            return;[m
[32m+[m[32m        }[m
[32m+[m
[32m+[m[32m        String motd =[m
[32m+[m[32m                String.join([m
[32m+[m[32m                        " ",[m
[32m+[m[32m                        Arrays.copyOfRange([m
[32m+[m[32m                                args,[m
[32m+[m[32m                                1,[m
[32m+[m[32m                                args.length[m
[32m+[m[32m                        )[m
[32m+[m[32m                );[m
[32m+[m
[32m+[m[32m        if (motd.isBlank()) {[m
[32m+[m
[32m+[m[32m            send([m
[32m+[m[32m                    source,[m
[32m+[m[32m                    "error.invalid-motd"[m
[32m+[m[32m            );[m
[32m+[m[32m            return;[m
[32m+[m[32m        }[m
[32m+[m
[32m+[m[32m        if (!plugin.getConfig().set([m
[32m+[m[32m                "motd",[m
[32m+[m[32m                motd[m
[32m+[m[32m        )) {[m
[32m+[m
[32m+[m[32m            send([m
[32m+[m[32m                    source,[m
[32m+[m[32m                    "error.config-save"[m
[32m+[m[32m            );[m
[32m+[m[32m            return;[m
[32m+[m[32m        }[m
[32m+[m
[32m+[m[32m        if (!plugin.getConfig().save()) {[m
[32m+[m
[32m+[m[32m            send([m
[32m+[m[32m                    source,[m
[32m+[m[32m                    "error.config-save"[m
[32m+[m[32m            );[m
[32m+[m[32m            return;[m
[32m+[m[32m        }[m
[32m+[m
[32m+[m[32m        /*[m
[32m+[m[32m         * Se o broadcaster estiver realmente rodando,[m
[32m+[m[32m         * aplicamos a mudança imediatamente.[m
[32m+[m[32m         */[m
[32m+[m[32m        if (isBroadcastRunning()) {[m
[32m+[m[32m            plugin.reconfigureBroadcaster();[m
[32m+[m[32m        }[m
[32m+[m
[32m+[m[32m        send([m
[32m+[m[32m                source,[m
[32m+[m[32m                "broadcast.motd-set",[m
[32m+[m[32m                motd[m
[32m+[m[32m        );[m
     }[m
 [m
[31m-    private void handleSetDelay(Object source, String[] args) {[m
[31m-        // vamos implementar[m
[32m+[m[32m    // =========================================================[m
[32m+[m[32m    // SET DELAY[m
[32m+[m[32m    // =========================================================[m
[32m+[m
[32m+[m[32m    private void handleSetDelay([m
[32m+[m[32m            CommandSource source,[m
[32m+[m[32m            String[] args[m
[32m+[m[32m    ) {[m
[32m+[m
[32m+[m[32m        if (args.length != 2) {[m
[32m+[m
[32m+[m[32m            send([m
[32m+[m[32m                    source,[m
[32m+[m[32m                    "error.invalid-syntax"[m
[32m+[m[32m            );[m
[32m+[m[32m            return;[m
[32m+[m[32m        }[m
[32m+[m
[32m+[m[32m        long delay;[m
[32m+[m
[32m+[m[32m        try {[m
[32m+[m
[32m+[m[32m            delay =[m
[32m+[m[32m                    Long.parseLong(args[1]);[m
[32m+[m
[32m+[m[32m        } catch (NumberFormatException e) {[m
[32m+[m
[32m+[m[32m            send([m
[32m+[m[32m                    source,[m
[32m+[m[32m                    "error.invalid-delay"[m
[32m+[m[32m            );[m
[32m+[m[32m            return;[m
[32m+[m[32m        }[m
[32m+[m
[32m+[m[32m        /*[m
[32m+[m[32m         * Limites utilizados pelo Config:[m
[32m+[m[32m         * mínimo = 50 ms[m
[32m+[m[32m         * máximo = 24 horas[m
[32m+[m[32m         */[m
[32m+[m[32m        if (delay < 50[m
[32m+[m[32m                || delay > 86_400_000L) {[m
[32m+[m
[32m+[m[32m            send([m
[32m+[m[32m                    source,[m
[32m+[m[32m                    "error.invalid-delay"[m
[32m+[m[32m            );[m
[32m+[m[32m            return;[m
[32m+[m[32m        }[m
[32m+[m
[32m+[m[32m        if (!plugin.getConfig().set([m
[32m+[m[32m                "broadcast-delay-ms",[m
[32m+[m[32m                delay[m
[32m+[m[32m        )) {[m
[32m+[m
[32m+[m[32m            send([m
[32m+[m[32m                    source,[m
[32m+[m[32m                    "error.config-save"[m
[32m+[m[32m            );[m
[32m+[m[32m            return;[m
[32m+[m[32m        }[m
[32m+[m
[32m+[m[32m        if (!plugin.getConfig().save()) {[m
[32m+[m
[32m+[m[32m            send([m
[32m+[m[32m                    source,[m
[32m+[m[32m                    "error.config-save"[m
[32m+[m[32m            );[m
[32m+[m[32m            return;[m
[32m+[m[32m        }[m
[32m+[m
[32m+[m[32m        /*[m
[32m+[m[32m         * Só recria o broadcaster se ele estiver realmente ativo.[m
[32m+[m[32m         *[m
[32m+[m[32m         * Isso permite configurar o delay enquanto o broadcast[m
[32m+[m[32m         * estiver parado sem iniciá-lo acidentalmente.[m
[32m+[m[32m         */[m
[32m+[m[32m        if (isBroadcastRunning()) {[m
[32m+[m[32m            plugin.reconfigureBroadcaster();[m
[32m+[m[32m        }[m
[32m+[m
[32m+[m[32m        send([m
[32m+[m[32m                source,[m
[32m+[m[32m                "broadcast.delay-set",[m
[32m+[m[32m                delay[m
[32m+[m[32m        );[m
     }[m
 [m
[31m-    private void handleSetPort(Object source, String[] args) {[m
[31m-        // vamos implementar[m
[32m+[m[32m    // =========================================================[m
[32m+[m[32m    // SET PORT[m
[32m+[m[32m    // =========================================================[m
[32m+[m
[32m+[m[32m    private void handleSetPort([m
[32m+[m[32m            CommandSource source,[m
[32m+[m[32m            String[] args[m
[32m+[m[32m    ) {[m
[32m+[m
[32m+[m[32m        if (args.length != 2) {[m
[32m+[m
[32m+[m[32m            send([m
[32m+[m[32m                    source,[m
[32m+[m[32m                    "error.invalid-syntax"[m
[32m+[m[32m            );[m
[32m+[m[32m            return;[m
[32m+[m[32m        }[m
[32m+[m
[32m+[m[32m        String value =[m
[32m+[m[32m                args[1].trim();[m
[32m+[m
[32m+[m[32m        int port;[m
[32m+[m
[32m+[m[32m        if (value.equalsIgnoreCase("auto")) {[m
[32m+[m
[32m+[m[32m            port = 0;[m
[32m+[m
[32m+[m[32m        } else {[m
[32m+[m
[32m+[m[32m            try {[m
[32m+[m
[32m+[m[32m                port =[m
[32m+[m[32m                        Integer.parseInt(value);[m
[32m+[m
[32m+[m[32m            } catch (NumberFormatException e) {[m
[32m+[m
[32m+[m[32m                send([m
[32m+[m[32m                        source,[m
[32m+[m[32m                        "error.invalid-port"[m
[32m+[m[32m                );[m
[32m+[m[32m                return;[m
[32m+[m[32m            }[m
[32m+[m
[32m+[m[32m            if (port < 1 || port > 65535) {[m
[32m+[m
[32m+[m[32m                send([m
[32m+[m[32m                        source,[m
[32m+[m[32m                        "error.invalid-port"[m
[32m+[m[32m                );[m
[32m+[m[32m                return;[m
[32m+[m[32m            }[m
[32m+[m[32m        }[m
[32m+[m
[32m+[m[32m        if (!plugin.getConfig().set([m
[32m+[m[32m                "broadcast-port",[m
[32m+[m[32m                port[m
[32m+[m[32m        )) {[m
[32m+[m
[32m+[m[32m            send([m
[32m+[m[32m                    source,[m
[32m+[m[32m                    "error.config-save"[m
[32m+[m[32m            );[m
[32m+[m[32m            return;[m
[32m+[m[32m        }[m
[32m+[m
[32m+[m[32m        if (!plugin.getConfig().save()) {[m
[32m+[m
[32m+[m[32m            send([m
[32m+[m[32m                    source,[m
[32m+[m[32m                    "error.config-save"[m
[32m+[m[32m            );[m
[32m+[m[32m            return;[m
[32m+[m[32m        }[m
[32m+[m
[32m+[m[32m        /*[m
[32m+[m[32m         * Aplica imediatamente somente se estiver rodando.[m
[32m+[m[32m         */[m
[32m+[m[32m        if (isBroadcastRunning()) {[m
[32m+[m[32m            plugin.reconfigureBroadcaster();[m
[32m+[m[32m        }[m
[32m+[m
[32m+[m[32m        if (port == 0) {[m
[32m+[m
[32m+[m[32m            int detected =[m
[32m+[m[32m                    plugin.getServer()[m
[32m+[m[32m                            .getBoundAddress()[m
[32m+[m[32m                            .getPort();[m
[32m+[m
[32m+[m[32m            send([m
[32m+[m[32m                    source,[m
[32m+[m[32m                    "broadcast.port-set-auto",[m
[32m+[m[32m                    detected[m
[32m+[m[32m            );[m
[32m+[m
[32m+[m[32m        } else {[m
[32m+[m
[32m+[m[32m            send([m
[32m+[m[32m                    source,[m
[32m+[m[32m                    "broadcast.port-set",[m
[32m+[m[32m                    port[m
[32m+[m[32m            );[m
[32m+[m[32m        }[m
     }[m
 [m
[31m-    private void handleDebug(Object source, String[] args) {[m
[31m-        // vamos implementar[m
[32m+[m[32m    // =========================================================[m
[32m+[m[32m    // SET INTERFACE[m
[32m+[m[32m    // =========================================================[m
[32m+[m
[32m+[m[32m    private void handleSetInterface([m
[32m+[m[32m            CommandSource source,[m
[32m+[m[32m            String[] args[m
[32m+[m[32m    ) {[m
[32m+[m
[32m+[m[32m        if (args.length != 2) {[m
[32m+[m
[32m+[m[32m            send([m
[32m+[m[32m                    source,[m
[32m+[m[32m                    "error.invalid-syntax"[m
[32m+[m[32m            );[m
[32m+[m[32m            return;[m
[32m+[m[32m        }[m
[32m+[m
[32m+[m[32m        String networkInterface =[m
[32m+[m[32m                args[1].trim();[m
[32m+[m
[32m+[m[32m        if (networkInterface.isBlank()) {[m
[32m+[m
[32m+[m[32m            send([m
[32m+[m[32m                    source,[m
[32m+[m[32m                    "error.invalid-interface"[m
[32m+[m[32m            );[m
[32m+[m[32m            return;[m
[32m+[m[32m        }[m
[32m+[m
[32m+[m[32m        if (!plugin.getConfig().set([m
[32m+[m[32m                "network-interface",[m
[32m+[m[32m                networkInterface[m
[32m+[m[32m        )) {[m
[32m+[m
[32m+[m[32m            send([m
[32m+[m[32m                    source,[m
[32m+[m[32m                    "error.config-save"[m
[32m+[m[32m            );[m
[32m+[m[32m            return;[m
[32m+[m[32m        }[m
[32m+[m
[32m+[m[32m        if (!plugin.getConfig().save()) {[m
[32m+[m
[32m+[m[32m            send([m
[32m+[m[32m                    source,[m
[32m+[m[32m                    "error.config-save"[m
[32m+[m[32m            );[m
[32m+[m[32m            return;[m
[32m+[m[32m        }[m
[32m+[m
[32m+[m[32m        /*[m
[32m+[m[32m         * Aplica imediatamente somente se estiver rodando.[m
[32m+[m[32m         */[m
[32m+[m[32m        if (isBroadcastRunning()) {[m
[32m+[m
[32m+[m[32m            boolean success =[m
[32m+[m[32m                    plugin.reconfigureBroadcaster();[m
[32m+[m
[32m+[m[32m            if (!success) {[m
[32m+[m
[32m+[m[32m                send([m
[32m+[m[32m                        source,[m
[32m+[m[32m                        "error.interface-start"[m
[32m+[m[32m                );[m
[32m+[m[32m                return;[m
[32m+[m[32m            }[m
[32m+[m[32m        }[m
[32m+[m
[32m+[m[32m        send([m
[32m+[m[32m                source,[m
[32m+[m[32m                "broadcast.interface-set",[m
[32m+[m[32m                networkInterface[m
[32m+[m[32m        );[m
     }[m
 [m
[31m-    private void handleReload(Object source) {[m
[31m-        // vamos implementar[m
[32m+[m[32m    // =========================================================[m
[32m+[m[32m    // DEBUG[m
[32m+[m[32m    // =========================================================[m
[32m+[m
[32m+[m[32m    private void handleDebug([m
[32m+[m[32m            CommandSource source,[m
[32m+[m[32m            String[] args[m
[32m+[m[32m    ) {[m
[32m+[m
[32m+[m[32m        if (args.length == 1) {[m
[32m+[m
[32m+[m[32m            boolean enabled =[m
[32m+[m[32m                    plugin.getConfig().isDebug();[m
[32m+[m
[32m+[m[32m            send([m
[32m+[m[32m                    source,[m
[32m+[m[32m                    "debug.label",[m
[32m+[m[32m                    enabled[m
[32m+[m[32m                            ? plugin.getLanguage().get([m
[32m+[m[32m                                    "debug.status-on"[m
[32m+[m[32m                            )[m
[32m+[m[32m                            : plugin.getLanguage().get([m
[32m+[m[32m                                    "debug.status-off"[m
[32m+[m[32m                            )[m
[32m+[m[32m            );[m
[32m+[m
[32m+[m[32m            return;[m
[32m+[m[32m        }[m
[32m+[m
[32m+[m[32m        if (args.length != 2) {[m
[32m+[m
[32m+[m[32m            send([m
[32m+[m[32m                    source,[m
[32m+[m[32m                    "error.invalid-syntax"[m
[32m+[m[32m            );[m
[32m+[m[32m            return;[m
[32m+[m[32m        }[m
[32m+[m
[32m+[m[32m        String value =[m
[32m+[m[32m                args[1].toLowerCase(Locale.ROOT);[m
[32m+[m
[32m+[m[32m        boolean enabled;[m
[32m+[m
[32m+[m[32m        if (value.equals("on")[m
[32m+[m[32m                || value.equals("true")[m
[32m+[m[32m                || value.equals("enable")[m
[32m+[m[32m                || value.equals("enabled")) {[m
[32m+[m
[32m+[m[32m            enabled = true;[m
[32m+[m
[32m+[m[32m        } else if ([m
[32m+[m[32m                value.equals("off")[m
[32m+[m[32m                        || value.equals("false")[m
[32m+[m[32m                        || value.equals("disable")[m
[32m+[m[32m                        || value.equals("disabled")[m
[32m+[m[32m        ) {[m
[32m+[m
[32m+[m[32m            enabled = false;[m
[32m+[m
[32m+[m[32m        } else {[m
[32m+[m
[32m+[m[32m            send([m
[32m+[m[32m                    source,[m
[32m+[m[32m                    "error.invalid-debug"[m
[32m+[m[32m            );[m
[32m+[m[32m            return;[m
[32m+[m[32m        }[m
[32m+[m
[32m+[m[32m        if (!plugin.getConfig().set([m
[32m+[m[32m                "debug",[m
[32m+[m[32m                enabled[m
[32m+[m[32m        )) {[m
[32m+[m
[32m+[m[32m            send([m
[32m+[m[32m                    source,[m
[32m+[m[32m                    "error.config-save"[m
[32m+[m[32m            );[m
[32m+[m[32m            return;[m
[32m+[m[32m        }[m
[32m+[m
[32m+[m[32m        if (!plugin.getConfig().save()) {[m
[32m+[m
[32m+[m[32m            send([m
[32m+[m[32m                    source,[m
[32m+[m[32m                    "error.config-save"[m
[32m+[m[32m            );[m
[32m+[m[32m            return;[m
[32m+[m[32m        }[m
[32m+[m
[32m+[m[32m        /*[m
[32m+[m[32m         * Debug pode ser alterado diretamente sem recriar o broadcaster.[m
[32m+[m[32m         */[m
[32m+[m[32m        if (plugin.getBroadcaster() != null) {[m
[32m+[m
[32m+[m[32m            plugin.getBroadcaster()[m
[32m+[m[32m                    .setDebug(enabled);[m
[32m+[m[32m        }[m
[32m+[m
[32m+[m[32m        send([m
[32m+[m[32m                source,[m
[32m+[m[32m                enabled[m
[32m+[m[32m                        ? "debug.on"[m
[32m+[m[32m                        : "debug.off"[m
[32m+[m[32m        );[m
     }[m
 [m
[31m-    private void handleHelp(Object source) {[m
[31m-        // vamos implementar[m
[32m+[m[32m    // =========================================================[m
[32m+[m[32m    // RELOAD[m
[32m+[m[32m    // =========================================================[m
[32m+[m
[32m+[m[32m    private void handleReload([m
[32m+[m[32m            CommandSource source[m
[32m+[m[32m    ) {[m
[32m+[m
[32m+[m[32m        if (!plugin.getConfig().reload()) {[m
[32m+[m
[32m+[m[32m            send([m
[32m+[m[32m                    source,[m
[32m+[m[32m                    "error.config-reload"[m
[32m+[m[32m            );[m
[32m+[m[32m            return;[m
[32m+[m[32m        }[m
[32m+[m
[32m+[m[32m        plugin.getLanguage().load([m
[32m+[m[32m                plugin.getConfig().getLanguage()[m
[32m+[m[32m        );[m
[32m+[m
[32m+[m[32m        /*[m
[32m+[m[32m         * reloadBroadcaster() agora:[m
[32m+[m[32m         *[m
[32m+[m[32m         * - destrói a instância antiga;[m
[32m+[m[32m         * - inicia novamente se broadcast-enabled=true;[m
[32m+[m[32m         * - mantém parado se broadcast-enabled=false.[m
[32m+[m[32m         */[m
[32m+[m[32m        boolean success =[m
[32m+[m[32m                plugin.reconfigureBroadcaster();[m
[32m+[m
[32m+[m[32m        if (!success) {[m
[32m+[m
[32m+[m[32m            send([m
[32m+[m[32m                    source,[m
[32m+[m[32m                    "error.reload-broadcast"[m
[32m+[m[32m            );[m
[32m+[m[32m            return;[m
[32m+[m[32m        }[m
[32m+[m
[32m+[m[32m        send([m
[32m+[m[32m                source,[m
[32m+[m[32m                "config.reloaded"[m
[32m+[m[32m        );[m
     }[m
 [m
[31m-    private void handleVersion(Object source) {[m
[31m-        // vamos implementar[m
[32m+[m[32m    // =========================================================[m
[32m+[m[32m    // HELP[m
[32m+[m[32m    // =========================================================[m
[32m+[m
[32m+[m[32m    private void handleHelp([m
[32m+[m[32m            CommandSource source[m
[32m+[m[32m    ) {[m
[32m+[m[32m        send([m
[32m+[m[32m                source,[m
[32m+[m[32m                "help.title"[m
[32m+[m[32m        );[m
[32m+[m
[32m+[m[32m        send([m
[32m+[m[32m                source,[m
[32m+[m[32m                "help.start"[m
[32m+[m[32m        );[m
[32m+[m
[32m+[m[32m        send([m
[32m+[m[32m                source,[m
[32m+[m[32m                "help.stop"[m
[32m+[m[32m        );[m
[32m+[m
[32m+[m[32m        send([m
[32m+[m[32m                source,[m
[32m+[m[32m                "help.status"[m
[32m+[m[32m        );[m
[32m+[m
[32m+[m[32m        send([m
[32m+[m[32m                source,[m
[32m+[m[32m                "help.setmotd"[m
[32m+[m[32m        );[m
[32m+[m
[32m+[m[32m        send([m
[32m+[m[32m                source,[m
[32m+[m[32m                "help.setdelay"[m
[32m+[m[32m        );[m
[32m+[m
[32m+[m[32m        send([m
[32m+[m[32m                source,[m
[32m+[m[32m                "help.setport"[m
[32m+[m[32m        );[m
[32m+[m
[32m+[m[32m        send([m
[32m+[m[32m                source,[m
[32m+[m[32m                "help.setinterface"[m
[32m+[m[32m        );[m
[32m+[m
[32m+[m[32m        send([m
[32m+[m[32m                source,[m
[32m+[m[32m                "help.debug"[m
[32m+[m[32m        );[m
[32m+[m
[32m+[m[32m        send([m
[32m+[m[32m                source,[m
[32m+[m[32m                "help.reload"[m
[32m+[m[32m        );[m
[32m+[m
[32m+[m[32m        send([m
[32m+[m[32m                source,[m
[32m+[m[32m                "help.version"[m
[32m+[m[32m        );[m
[32m+[m[32m        send([m
[32m+[m[32m                source,[m
[32m+[m[32m                "help.footer"[m
[32m+[m[32m        );[m
     }[m
 [m
[32m+[m[32m    // =========================================================[m
[32m+[m[32m    // VERSION[m
[32m+[m[32m    // =========================================================[m
[32m+[m
[32m+[m[32m    private void handleVersion([m
[32m+[m[32m        CommandSource source[m
[32m+[m[32m) {[m
[32m+[m[32m    send([m
[32m+[m[32m            source,[m
[32m+[m[32m            "version.line1",[m
[32m+[m[32m            plugin.getVersion()[m
[32m+[m[32m    );[m
[32m+[m
[32m+[m[32m    send([m
[32m+[m[32m            source,[m
[32m+[m[32m            "version.line2",[m
[32m+[m[32m            "myxxr, GabiFurBR"[m
[32m+[m[32m    );[m
[32m+[m
[32m+[m[32m    send([m
[32m+[m[32m            source,[m
[32m+[m[32m            "version.help-hint",[m
[32m+[m[32m            "/blb help"[m
[32m+[m[32m    );[m
[32m+[m[32m}[m
[32m+[m
[32m+[m[32m    // =========================================================[m
[32m+[m[32m    // TAB COMPLETION[m
[32m+[m[32m    // =========================================================[m
[32m+[m
     @Override[m
[31m-    public List<String> suggest(Invocation invocation) {[m
[32m+[m[32m    public List<String> suggest([m
[32m+[m[32m            Invocation invocation[m
[32m+[m[32m    ) {[m
 [m
[31m-        String[] args = invocation.arguments();[m
[32m+[m[32m        if (!hasPermission(invocation)) {[m
[32m+[m[32m            return List.of();[m
[32m+[m[32m        }[m
[32m+[m
[32m+[m[32m        String[] args =[m
[32m+[m[32m                invocation.arguments();[m
 [m
[31m-        if (!invocation.source().hasPermission([m
[31m-                "betterlanbroadcaster.admin")) {[m
[31m-            return new ArrayList<>();[m
[32m+[m[32m        if (args.length == 0) {[m
[32m+[m[32m            return SUBCOMMANDS;[m
         }[m
 [m
         if (args.length == 1) {[m
 [m
[31m-            String partial = args[0].toLowerCase();[m
[32m+[m[32m            String input =[m
[32m+[m[32m                    args[0].toLowerCase(Locale.ROOT);[m
 [m
             return SUBCOMMANDS.stream()[m
[31m-                    .filter(s -> s.startsWith(partial))[m
[32m+[m[32m                    .filter([m
[32m+[m[32m                            command ->[m
[32m+[m[32m                                    command.startsWith(input)[m
[32m+[m[32m                    )[m
                     .collect(Collectors.toList());[m
         }[m
 [m
[31m-        if (args[0].equalsIgnoreCase("setport")[m
[31m-                && args.length == 2) {[m
[32m+[m[32m        if (args.length != 2) {[m
[32m+[m[32m            return List.of();[m
[32m+[m[32m        }[m
 [m
[31m-            return Arrays.asList("auto");[m
[32m+[m[32m        String subcommand =[m
[32m+[m[32m                args[0].toLowerCase(Locale.ROOT);[m
[32m+[m
[32m+[m[32m        String input =[m
[32m+[m[32m                args[1].toLowerCase(Locale.ROOT);[m
[32m+[m
[32m+[m[32m        if (subcommand.equals("setport")) {[m
[32m+[m
[32m+[m[32m            return List.of("auto")[m
[32m+[m[32m                    .stream()[m
[32m+[m[32m                    .filter([m
[32m+[m[32m                            option ->[m
[32m+[m[32m                                    option.startsWith(input)[m
[32m+[m[32m                    )[m
[32m+[m[32m                    .collect(Collectors.toList());[m
[32m+[m[32m        }[m
[32m+[m
[32m+[m[32m        if (subcommand.equals("debug")) {[m
[32m+[m
[32m+[m[32m            return List.of("on", "off")[m
[32m+[m[32m                    .stream()[m
[32m+[m[32m                    .filter([m
[32m+[m[32m                            option ->[m
[32m+[m[32m                                    option.startsWith(input)[m
[32m+[m[32m                    )[m
[32m+[m[32m                    .collect(Collectors.toList());[m
         }[m
 [m
[31m-        if (args[0].equalsIgnoreCase("debug")[m
[31m-                && args.length == 2) {[m
[32m+[m[32m        if (subcommand.equals("setinterface")) {[m
 [m
[31m-            return Arrays.asList("on", "off");[m
[32m+[m[32m            return List.of("auto")[m
[32m+[m[32m                    .stream()[m
[32m+[m[32m                    .filter([m
[32m+[m[32m                            option ->[m
[32m+[m[32m                                    option.startsWith(input)[m
[32m+[m[32m                    )[m
[32m+[m[32m                    .collect(Collectors.toList());[m
         }[m
 [m
[31m-        return new ArrayList<>();[m
[32m+[m[32m        return List.of();[m
[32m+[m[32m    }[m
[32m+[m
[32m+[m[32m    // =========================================================[m
[32m+[m[32m    // HELPERS[m
[32m+[m[32m    // =========================================================[m
[32m+[m
[32m+[m[32m    private boolean isBroadcastRunning() {[m
[32m+[m
[32m+[m[32m        return plugin.getBroadcaster() != null[m
[32m+[m[32m                && plugin.getBroadcaster().isRunning();[m
[32m+[m[32m    }[m
[32m+[m
[32m+[m[32m    // =========================================================[m
[32m+[m[32m    // MESSAGE HELPERS[m
[32m+[m[32m    // =========================================================[m
[32m+[m
[32m+[m[32m    private void send([m
[32m+[m[32m        CommandSource source,[m
[32m+[m[32m        String path,[m
[32m+[m[32m        Object... arguments[m
[32m+[m[32m) {[m
[32m+[m[32m    String message =[m
[32m+[m[32m            plugin.getLanguage().message([m
[32m+[m[32m                    path,[m
[32m+[m[32m                    arguments[m
[32m+[m[32m            );[m
[32m+[m
[32m+[m[32m    sendRaw([m
[32m+[m[32m            source,[m
[32m+[m[32m            message[m
[32m+[m[32m    );[m
[32m+[m[32m}[m
[32m+[m
[32m+[m[32m    private void sendRaw([m
[32m+[m[32m            CommandSource source,[m
[32m+[m[32m            String message[m
[32m+[m[32m    ) {[m
[32m+[m
[32m+[m[32m        if (message == null) {[m
[32m+[m[32m            return;[m
[32m+[m[32m        }[m
[32m+[m
[32m+[m[32m        String converted =[m
[32m+[m[32m                message.replaceAll([m
[32m+[m[32m                        "&([0-9a-fA-Fk-oK-OrR])",[m
[32m+[m[32m                        "\u00A7$1"[m
[32m+[m[32m                );[m
[32m+[m
[32m+[m[32m        source.sendMessage([m
[32m+[m[32m                LegacyComponentSerializer[m
[32m+[m[32m                        .legacySection()[m
[32m+[m[32m                        .deserialize(converted)[m
[32m+[m[32m        );[m
     }[m
[31m-}   [m
\ No newline at end of file[m
[32m+[m[32m}[m
\ No newline at end of file[m
[1mdiff --git a/src/main/java/com/betterlanbroadcaster/Config.java b/src/main/java/com/betterlanbroadcaster/Config.java[m
[1mindex 41923fa..f473a04 100644[m
[1m--- a/src/main/java/com/betterlanbroadcaster/Config.java[m
[1m+++ b/src/main/java/com/betterlanbroadcaster/Config.java[m
[36m@@ -1,21 +1,44 @@[m
 package com.betterlanbroadcaster;[m
 [m
[31m-import org.spongepowered.configurate.ConfigurationNode;[m
[32m+[m[32mimport org.spongepowered.configurate.CommentedConfigurationNode;[m
[32m+[m[32mimport org.spongepowered.configurate.yaml.NodeStyle;[m
 import org.spongepowered.configurate.yaml.YamlConfigurationLoader;[m
[32m+[m[32mimport org.spongepowered.configurate.serialize.SerializationException;[m
 [m
 import java.io.IOException;[m
 import java.io.InputStream;[m
 import java.nio.file.Files;[m
 import java.nio.file.Path;[m
[31m-[m
[32m+[m[32mimport java.util.Locale;[m
[32m+[m
[32m+[m[32m/**[m
[32m+[m[32m * Handles BetterLANBroadcaster configuration.[m
[32m+[m[32m *[m
[32m+[m[32m * <p>The configuration is stored in {@code config.yml}.</p>[m
[32m+[m[32m *[m
[32m+[m[32m * <p>All values are validated before being exposed to the[m
[32m+[m[32m * rest of the plugin.</p>[m
[32m+[m[32m */[m
 public class Config {[m
 [m
[31m-    private final BetterLANBroadcaster plugin;[m
[32m+[m[32m    private static final String CONFIG_RESOURCE = "/config.yml";[m
[32m+[m
[32m+[m[32m    private static final String DEFAULT_LANGUAGE = "en";[m
[32m+[m[32m    private static final boolean DEFAULT_DEBUG = false;[m
[32m+[m[32m    private static final boolean DEFAULT_BROADCAST_ENABLED = false;[m
[32m+[m[32m    private static final long DEFAULT_BROADCAST_DELAY_MS = 1500L;[m
[32m+[m[32m    private static final int DEFAULT_BROADCAST_PORT = 0;[m
[32m+[m[32m    private static final String DEFAULT_NETWORK_INTERFACE = "auto";[m
[32m+[m[32m    private static final String DEFAULT_MOTD = "A Minecraft Server";[m
 [m
[32m+[m[32m    private static final long MIN_DELAY_MS = 50L;[m
[32m+[m[32m    private static final long MAX_DELAY_MS = 86_400_000L;[m
[32m+[m
[32m+[m[32m    private final BetterLANBroadcaster plugin;[m
     private final Path dataDirectory;[m
     private final Path configFile;[m
 [m
[31m-    private ConfigurationNode root;[m
[32m+[m[32m    private CommentedConfigurationNode root;[m
 [m
     public Config(BetterLANBroadcaster plugin, Path dataDirectory) {[m
         this.plugin = plugin;[m
[36m@@ -23,153 +46,869 @@[m [mpublic class Config {[m
         this.configFile = dataDirectory.resolve("config.yml");[m
     }[m
 [m
[31m-    public void load() {[m
[32m+[m[32m    /**[m
[32m+[m[32m     * Loads the configuration.[m
[32m+[m[32m     *[m
[32m+[m[32m     * @return true if the configuration was loaded successfully[m
[32m+[m[32m     */[m
[32m+[m[32m    public boolean load() {[m
         try {[m
[32m+[m[32m            createDataDirectory();[m
[32m+[m[32m            createDefaultConfigIfNecessary();[m
[32m+[m
[32m+[m[32m            root = createLoader().load();[m
 [m
[31m-            // Cria a pasta do plugin caso ela não exista[m
[31m-            Files.createDirectories(dataDirectory);[m
[32m+[m[32m            if (root == null || root.virtual()) {[m
[32m+[m[32m                plugin.getLogger().warn([m
[32m+[m[32m                        "A configuração carregada está vazia. Usando valores padrão."[m
[32m+[m[32m                );[m
[32m+[m
[32m+[m[32m                root = createLoader().createNode();[m
[32m+[m
[32m+[m[32m                applyDefaults();[m
 [m
[31m-            // Cria o config.yml apenas na primeira execução[m
[31m-            if (Files.notExists(configFile)) {[m
[31m-                copyDefaultConfig();[m
[32m+[m[32m                if (!save()) {[m
[32m+[m[32m                    return false;[m
[32m+[m[32m                }[m
[32m+[m
[32m+[m[32m                return true;[m
             }[m
 [m
[31m-            loadFromFile();[m
[32m+[m[32m            boolean changed = validateAndApplyDefaults();[m
 [m
[31m-            plugin.getLogger().info("Configuração carregada.");[m
[32m+[m[32m            if (changed) {[m
[32m+[m[32m                save();[m
[32m+[m[32m            }[m
[32m+[m
[32m+[m[32m            plugin.getLogger().debug([m
[32m+[m[32m                    "Configuração carregada de {}.",[m
[32m+[m[32m                    configFile[m
[32m+[m[32m            );[m
[32m+[m
[32m+[m[32m            return true;[m
 [m
         } catch (IOException e) {[m
             plugin.getLogger().error([m
[31m-                    "Não foi possível carregar o config.yml.",[m
[32m+[m[32m                    "Não foi possível carregar a configuração em {}.",[m
[32m+[m[32m                    configFile,[m
                     e[m
             );[m
[32m+[m
[32m+[m[32m            return false;[m
         }[m
     }[m
 [m
[31m-    private void loadFromFile() throws IOException {[m
[32m+[m[32m    /**[m
[32m+[m[32m     * Reloads the configuration from disk.[m
[32m+[m[32m     *[m
[32m+[m[32m     * @return true if the configuration was reloaded successfully[m
[32m+[m[32m     */[m
[32m+[m[32m    public boolean reload() {[m
[32m+[m[32m        return load();[m
[32m+[m[32m    }[m
[32m+[m
[32m+[m[32m    /**[m
[32m+[m[32m     * Saves the current configuration.[m
[32m+[m[32m     *[m
[32m+[m[32m     * @return true if the configuration was saved successfully[m
[32m+[m[32m     */[m
[32m+[m[32m    public boolean save() {[m
[32m+[m[32m        if (root == null) {[m
[32m+[m[32m            plugin.getLogger().warn([m
[32m+[m[32m                    "Não foi possível salvar a configuração porque ela ainda não foi carregada."[m
[32m+[m[32m            );[m
[32m+[m
[32m+[m[32m            return false;[m
[32m+[m[32m        }[m
[32m+[m
[32m+[m[32m        try {[m
[32m+[m[32m            createDataDirectory();[m
[32m+[m
[32m+[m[32m            createLoader().save(root);[m
[32m+[m
[32m+[m[32m            plugin.getLogger().debug([m
[32m+[m[32m                    "Configuração salva em {}.",[m
[32m+[m[32m                    configFile[m
[32m+[m[32m            );[m
[32m+[m
[32m+[m[32m            return true;[m
 [m
[31m-        YamlConfigurationLoader loader =[m
[31m-                YamlConfigurationLoader.builder()[m
[31m-                        .path(configFile)[m
[31m-                        .build();[m
[32m+[m[32m        } catch (IOException e) {[m
[32m+[m[32m            plugin.getLogger().error([m
[32m+[m[32m                    "Não foi possível salvar a configuração em {}.",[m
[32m+[m[32m                    configFile,[m
[32m+[m[32m                    e[m
[32m+[m[32m            );[m
 [m
[31m-        root = loader.load();[m
[32m+[m[32m            return false;[m
[32m+[m[32m        }[m
     }[m
 [m
[31m-    private void copyDefaultConfig() throws IOException {[m
[32m+[m[32m    /**[m
[32m+[m[32m     * Creates the plugin data directory if necessary.[m
[32m+[m[32m     */[m
[32m+[m[32m    private void createDataDirectory() throws IOException {[m
[32m+[m[32m        Files.createDirectories(dataDirectory);[m
[32m+[m[32m    }[m
[32m+[m
[32m+[m[32m    /**[m
[32m+[m[32m     * Creates config.yml from the resource bundled inside the plugin.[m
[32m+[m[32m     */[m
[32m+[m[32m    private void createDefaultConfigIfNecessary() throws IOException {[m
[32m+[m[32m        if (Files.exists(configFile)) {[m
[32m+[m[32m            return;[m
[32m+[m[32m        }[m
 [m
         try (InputStream input =[m
[31m-                     getClass().getResourceAsStream("/config.yml")) {[m
[32m+[m[32m                     Config.class.getResourceAsStream(CONFIG_RESOURCE)) {[m
 [m
             if (input == null) {[m
                 throw new IOException([m
[31m-                        "config.yml não encontrado dentro do JAR."[m
[32m+[m[32m                        "O recurso padrão config.yml não foi encontrado dentro do plugin."[m
                 );[m
             }[m
 [m
             Files.copy(input, configFile);[m
         }[m
[32m+[m
[32m+[m[32m        plugin.getLogger().info([m
[32m+[m[32m                "Configuração padrão criada em {}.",[m
[32m+[m[32m                configFile[m
[32m+[m[32m        );[m
[32m+[m[32m    }[m
[32m+[m
[32m+[m[32m    /**[m
[32m+[m[32m     * Creates the Configurate YAML loader.[m
[32m+[m[32m     *[m
[32m+[m[32m     * <p>BLOCK style keeps the configuration readable and prevents[m
[32m+[m[32m     * Configurate from serializing the entire file as a flow-style object.</p>[m
[32m+[m[32m     */[m
[32m+[m[32m    private YamlConfigurationLoader createLoader() {[m
[32m+[m[32m        return YamlConfigurationLoader.builder()[m
[32m+[m[32m                .path(configFile)[m
[32m+[m[32m                .nodeStyle(NodeStyle.BLOCK)[m
[32m+[m[32m                .indent(2)[m
[32m+[m[32m                .build();[m
[32m+[m[32m    }[m
[32m+[m
[32m+[m[32m    /**[m
[32m+[m[32m     * Applies all default values to an empty configuration.[m
[32m+[m[32m     */[m
[32m+[m[32m    private void applyDefaults() throws SerializationException {[m
[32m+[m[32m        root.node("language").set(DEFAULT_LANGUAGE);[m
[32m+[m[32m        root.node("debug").set(DEFAULT_DEBUG);[m
[32m+[m[32m        root.node("broadcast-enabled").set(DEFAULT_BROADCAST_ENABLED);[m
[32m+[m[32m        root.node("broadcast-delay-ms").set(DEFAULT_BROADCAST_DELAY_MS);[m
[32m+[m[32m        root.node("broadcast-port").set(DEFAULT_BROADCAST_PORT);[m
[32m+[m[32m        root.node("network-interface").set(DEFAULT_NETWORK_INTERFACE);[m
[32m+[m[32m        root.node("motd").set(DEFAULT_MOTD);[m
[32m+[m[32m    }[m
[32m+[m
[32m+[m[32m    /**[m
[32m+[m[32m     * Validates all configuration values and fills missing values.[m
[32m+[m[32m     *[m
[32m+[m[32m     * @return true if the configuration was modified[m
[32m+[m[32m     */[m
[32m+[m[32m    private boolean validateAndApplyDefaults()[m
[32m+[m[32m            throws SerializationException {[m
[32m+[m
[32m+[m[32m        boolean changed = false;[m
[32m+[m
[32m+[m[32m        /*[m
[32m+[m[32m         * ========================================================[m
[32m+[m[32m         * Language[m
[32m+[m[32m         * ========================================================[m
[32m+[m[32m         */[m
[32m+[m
[32m+[m[32m        String language;[m
[32m+[m
[32m+[m[32m        try {[m
[32m+[m[32m            language = root.node("language").get(String.class);[m
[32m+[m[32m        } catch (SerializationException e) {[m
[32m+[m[32m            language = null;[m
[32m+[m[32m        }[m
[32m+[m
[32m+[m[32m        if (language == null || language.isBlank()) {[m
[32m+[m
[32m+[m[32m            root.node("language").set(DEFAULT_LANGUAGE);[m
[32m+[m[32m            changed = true;[m
[32m+[m
[32m+[m[32m        } else {[m
[32m+[m
[32m+[m[32m            language = language.trim().toLowerCase(Locale.ROOT);[m
[32m+[m
[32m+[m[32m            if (!language.equals("en")[m
[32m+[m[32m                    && !language.equals("br")[m
[32m+[m[32m                    && !language.equals("zh")) {[m
[32m+[m
[32m+[m[32m                plugin.getLogger().warn([m
[32m+[m[32m                        "Idioma '{}' não é suportado. Usando '{}'.",[m
[32m+[m[32m                        language,[m
[32m+[m[32m                        DEFAULT_LANGUAGE[m
[32m+[m[32m                );[m
[32m+[m
[32m+[m[32m                root.node("language").set(DEFAULT_LANGUAGE);[m
[32m+[m[32m                changed = true;[m
[32m+[m
[32m+[m[32m            } else {[m
[32m+[m
[32m+[m[32m                String currentLanguage;[m
[32m+[m
[32m+[m[32m                try {[m
[32m+[m[32m                    currentLanguage =[m
[32m+[m[32m                            root.node("language").get(String.class);[m
[32m+[m[32m                } catch (SerializationException e) {[m
[32m+[m[32m                    currentLanguage = "";[m
[32m+[m[32m                }[m
[32m+[m
[32m+[m[32m                if (!language.equals(currentLanguage)) {[m
[32m+[m[32m                    root.node("language").set(language);[m
[32m+[m[32m                    changed = true;[m
[32m+[m[32m                }[m
[32m+[m[32m            }[m
[32m+[m[32m        }[m
[32m+[m
[32m+[m[32m        /*[m
[32m+[m[32m         * ========================================================[m
[32m+[m[32m         * Debug[m
[32m+[m[32m         * ========================================================[m
[32m+[m[32m         */[m
[32m+[m
[32m+[m[32m        if (root.node("debug").virtual()) {[m
[32m+[m[32m            root.node("debug").set(DEFAULT_DEBUG);[m
[32m+[m[32m            changed = true;[m
[32m+[m[32m        }[m
[32m+[m
[32m+[m[32m        /*[m
[32m+[m[32m         * ========================================================[m
[32m+[m[32m         * Broadcast enabled[m
[32m+[m[32m         * ========================================================[m
[32m+[m[32m         */[m
[32m+[m
[32m+[m[32m        if (root.node("broadcast-enabled").virtual()) {[m
[32m+[m[32m            root.node("broadcast-enabled")[m
[32m+[m[32m                    .set(DEFAULT_BROADCAST_ENABLED);[m
[32m+[m
[32m+[m[32m            changed = true;[m
[32m+[m[32m        }[m
[32m+[m
[32m+[m[32m        /*[m
[32m+[m[32m         * ========================================================[m
[32m+[m[32m         * Broadcast delay[m
[32m+[m[32m         * ========================================================[m
[32m+[m[32m         */[m
[32m+[m
[32m+[m[32m        Long delay;[m
[32m+[m
[32m+[m[32m        try {[m
[32m+[m[32m            delay = root.node("broadcast-delay-ms")[m
[32m+[m[32m                    .get(Long.class);[m
[32m+[m[32m        } catch (SerializationException e) {[m
[32m+[m[32m            delay = null;[m
[32m+[m[32m        }[m
[32m+[m
[32m+[m[32m        if (delay == null) {[m
[32m+[m
[32m+[m[32m            plugin.getLogger().warn([m
[32m+[m[32m                    "broadcast-delay-ms inválido ou ausente. Usando {} ms.",[m
[32m+[m[32m                    DEFAULT_BROADCAST_DELAY_MS[m
[32m+[m[32m            );[m
[32m+[m
[32m+[m[32m            root.node("broadcast-delay-ms")[m
[32m+[m[32m                    .set(DEFAULT_BROADCAST_DELAY_MS);[m
[32m+[m
[32m+[m[32m            changed = true;[m
[32m+[m
[32m+[m[32m        } else if (delay < MIN_DELAY_MS) {[m
[32m+[m
[32m+[m[32m            plugin.getLogger().warn([m
[32m+[m[32m                    "broadcast-delay-ms={} está abaixo do mínimo de {} ms. Ajustando para {} ms.",[m
[32m+[m[32m                    delay,[m
[32m+[m[32m                    MIN_DELAY_MS,[m
[32m+[m[32m                    MIN_DELAY_MS[m
[32m+[m[32m            );[m
[32m+[m
[32m+[m[32m            root.node("broadcast-delay-ms")[m
[32m+[m[32m                    .set(MIN_DELAY_MS);[m
[32m+[m
[32m+[m[32m            changed = true;[m
[32m+[m
[32m+[m[32m        } else if (delay > MAX_DELAY_MS) {[m
[32m+[m
[32m+[m[32m            plugin.getLogger().warn([m
[32m+[m[32m                    "broadcast-delay-ms={} excede o máximo de {} ms. Ajustando para {} ms.",[m
[32m+[m[32m                    delay,[m
[32m+[m[32m                    MAX_DELAY_MS,[m
[32m+[m[32m                    MAX_DELAY_MS[m
[32m+[m[32m            );[m
[32m+[m
[32m+[m[32m            root.node("broadcast-delay-ms")[m
[32m+[m[32m                    .set(MAX_DELAY_MS);[m
[32m+[m
[32m+[m[32m            changed = true;[m
[32m+[m[32m        }[m
[32m+[m
[32m+[m[32m        /*[m
[32m+[m[32m         * ========================================================[m
[32m+[m[32m         * Broadcast port[m
[32m+[m[32m         * ========================================================[m
[32m+[m[32m         *[m
[32m+[m[32m         * 0 = automatic port detection[m
[32m+[m[32m         */[m
[32m+[m
[32m+[m[32m        Integer port;[m
[32m+[m
[32m+[m[32m        try {[m
[32m+[m[32m            port = root.node("broadcast-port")[m
[32m+[m[32m                    .get(Integer.class);[m
[32m+[m[32m        } catch (SerializationException e) {[m
[32m+[m[32m            port = null;[m
[32m+[m[32m        }[m
[32m+[m
[32m+[m[32m        if (port == null) {[m
[32m+[m
[32m+[m[32m            plugin.getLogger().warn([m
[32m+[m[32m                    "broadcast-port inválida ou ausente. Usando 0 (porta automática)."[m
[32m+[m[32m            );[m
[32m+[m
[32m+[m[32m            root.node("broadcast-port")[m
[32m+[m[32m                    .set(DEFAULT_BROADCAST_PORT);[m
[32m+[m
[32m+[m[32m            changed = true;[m
[32m+[m
[32m+[m[32m        } else if (port < 0 || port > 65535) {[m
[32m+[m
[32m+[m[32m            plugin.getLogger().warn([m
[32m+[m[32m                    "broadcast-port={} está fora do intervalo válido. Usando 0.",[m
[32m+[m[32m                    port[m
[32m+[m[32m            );[m
[32m+[m
[32m+[m[32m            root.node("broadcast-port")[m
[32m+[m[32m                    .set(DEFAULT_BROADCAST_PORT);[m
[32m+[m
[32m+[m[32m            changed = true;[m
[32m+[m[32m        }[m
[32m+[m
[32m+[m[32m        /*[m
[32m+[m[32m         * ========================================================[m
[32m+[m[32m         * Network interface[m
[32m+[m[32m         * ========================================================[m
[32m+[m[32m         *[m
[32m+[m[32m         * Supported values:[m
[32m+[m[32m         *[m
[32m+[m[32m         * auto[m
[32m+[m[32m         * interface name[m
[32m+[m[32m         * IPv4 address[m
[32m+[m[32m         */[m
[32m+[m
[32m+[m[32m        String networkInterface;[m
[32m+[m
[32m+[m[32m        try {[m
[32m+[m[32m            networkInterface =[m
[32m+[m[32m                    root.node("network-interface")[m
[32m+[m[32m                            .get(String.class);[m
[32m+[m[32m        } catch (SerializationException e) {[m
[32m+[m[32m            networkInterface = null;[m
[32m+[m[32m        }[m
[32m+[m
[32m+[m[32m        if (networkInterface == null[m
[32m+[m[32m                || networkInterface.isBlank()) {[m
[32m+[m
[32m+[m[32m            plugin.getLogger().warn([m
[32m+[m[32m                    "network-interface está ausente. Usando 'auto'."[m
[32m+[m[32m            );[m
[32m+[m
[32m+[m[32m            root.node("network-interface")[m
[32m+[m[32m                    .set(DEFAULT_NETWORK_INTERFACE);[m
[32m+[m
[32m+[m[32m            changed = true;[m
[32m+[m
[32m+[m[32m        } else {[m
[32m+[m
[32m+[m[32m            networkInterface = networkInterface.trim();[m
[32m+[m
[32m+[m[32m            String currentInterface;[m
[32m+[m
[32m+[m[32m            try {[m
[32m+[m[32m                currentInterface =[m
[32m+[m[32m                        root.node("network-interface")[m
[32m+[m[32m                                .get(String.class);[m
[32m+[m[32m            } catch (SerializationException e) {[m
[32m+[m[32m                currentInterface = "";[m
[32m+[m[32m            }[m
[32m+[m
[32m+[m[32m            if (!networkInterface.equals(currentInterface)) {[m
[32m+[m
[32m+[m[32m                root.node("network-interface")[m
[32m+[m[32m                        .set(networkInterface);[m
[32m+[m
[32m+[m[32m                changed = true;[m
[32m+[m[32m            }[m
[32m+[m[32m        }[m
[32m+[m
[32m+[m[32m        /*[m
[32m+[m[32m         * ========================================================[m
[32m+[m[32m         * MOTD[m
[32m+[m[32m         * ========================================================[m
[32m+[m[32m         */[m
[32m+[m
[32m+[m[32m        String motd;[m
[32m+[m
[32m+[m[32m        try {[m
[32m+[m[32m            motd = root.node("motd").get(String.class);[m
[32m+[m[32m        } catch (SerializationException e) {[m
[32m+[m[32m            motd = null;[m
[32m+[m[32m        }[m
[32m+[m
[32m+[m[32m        if (motd == null) {[m
[32m+[m
[32m+[m[32m            plugin.getLogger().warn([m
[32m+[m[32m                    "motd está ausente. Usando o MOTD padrão."[m
[32m+[m[32m            );[m
[32m+[m
[32m+[m[32m            root.node("motd").set(DEFAULT_MOTD);[m
[32m+[m
[32m+[m[32m            changed = true;[m
[32m+[m[32m        }[m
[32m+[m
[32m+[m[32m        return changed;[m
[32m+[m[32m    }[m
[32m+[m
[32m+[m[32m    /*[m
[32m+[m[32m     * ============================================================[m
[32m+[m[32m     * Getters[m
[32m+[m[32m     * ============================================================[m
[32m+[m[32m     */[m
[32m+[m
[32m+[m[32m    public String getLanguage() {[m
[32m+[m
[32m+[m[32m        if (root == null) {[m
[32m+[m[32m            return DEFAULT_LANGUAGE;[m
[32m+[m[32m        }[m
[32m+[m
[32m+[m[32m        try {[m
[32m+[m
[32m+[m[32m            String language =[m
[32m+[m[32m                    root.node("language")[m
[32m+[m[32m                            .get(String.class);[m
[32m+[m
[32m+[m[32m            if (language == null || language.isBlank()) {[m
[32m+[m[32m                return DEFAULT_LANGUAGE;[m
[32m+[m[32m            }[m
[32m+[m
[32m+[m[32m            return language.trim().toLowerCase(Locale.ROOT);[m
[32m+[m
[32m+[m[32m        } catch (SerializationException e) {[m
[32m+[m
[32m+[m[32m            return DEFAULT_LANGUAGE;[m
[32m+[m[32m        }[m
[32m+[m[32m    }[m
[32m+[m
[32m+[m[32m    public boolean isDebug() {[m
[32m+[m
[32m+[m[32m        if (root == null) {[m
[32m+[m[32m            return DEFAULT_DEBUG;[m
[32m+[m[32m        }[m
[32m+[m
[32m+[m[32m        return root.node("debug")[m
[32m+[m[32m                .getBoolean(DEFAULT_DEBUG);[m
     }[m
 [m
[31m-    public void save() {[m
[32m+[m[32m    public boolean isBroadcastEnabled() {[m
 [m
         if (root == null) {[m
[32m+[m[32m            return DEFAULT_BROADCAST_ENABLED;[m
[32m+[m[32m        }[m
[32m+[m
[32m+[m[32m        return root.node("broadcast-enabled")[m
[32m+[m[32m                .getBoolean(DEFAULT_BROADCAST_ENABLED);[m
[32m+[m[32m    }[m
[32m+[m
[32m+[m[32m    public long getBroadcastDelayMs() {[m
[32m+[m
[32m+[m[32m        if (root == null) {[m
[32m+[m[32m            return DEFAULT_BROADCAST_DELAY_MS;[m
[32m+[m[32m        }[m
[32m+[m
[32m+[m[32m        try {[m
[32m+[m
[32m+[m[32m            Long delay =[m
[32m+[m[32m                    root.node("broadcast-delay-ms")[m
[32m+[m[32m                            .get(Long.class);[m
[32m+[m
[32m+[m[32m            if (delay == null) {[m
[32m+[m[32m                return DEFAULT_BROADCAST_DELAY_MS;[m
[32m+[m[32m            }[m
[32m+[m
[32m+[m[32m            return Math.max([m
[32m+[m[32m                    MIN_DELAY_MS,[m
[32m+[m[32m                    Math.min(MAX_DELAY_MS, delay)[m
[32m+[m[32m            );[m
[32m+[m
[32m+[m[32m        } catch (SerializationException e) {[m
[32m+[m
             plugin.getLogger().warn([m
[31m-                    "Não é possível salvar o config.yml porque ele não foi carregado."[m
[32m+[m[32m                    "Não foi possível ler broadcast-delay-ms. Usando {} ms.",[m
[32m+[m[32m                    DEFAULT_BROADCAST_DELAY_MS[m
             );[m
[31m-            return;[m
[32m+[m
[32m+[m[32m            return DEFAULT_BROADCAST_DELAY_MS;[m
[32m+[m[32m        }[m
[32m+[m[32m    }[m
[32m+[m
[32m+[m[32m    public int getBroadcastPort() {[m
[32m+[m
[32m+[m[32m        if (root == null) {[m
[32m+[m[32m            return DEFAULT_BROADCAST_PORT;[m
         }[m
 [m
         try {[m
 [m
[31m-            YamlConfigurationLoader loader =[m
[31m-                    YamlConfigurationLoader.builder()[m
[31m-                            .path(configFile)[m
[31m-                            .build();[m
[32m+[m[32m            Integer port =[m
[32m+[m[32m                    root.node("broadcast-port")[m
[32m+[m[32m                            .get(Integer.class);[m
 [m
[31m-            loader.save(root);[m
[32m+[m[32m            if (port == null) {[m
[32m+[m[32m                return DEFAULT_BROADCAST_PORT;[m
[32m+[m[32m            }[m
 [m
[31m-        } catch (IOException e) {[m
[32m+[m[32m            if (port < 0 || port > 65535) {[m
[32m+[m[32m                return DEFAULT_BROADCAST_PORT;[m
[32m+[m[32m            }[m
 [m
[31m-            plugin.getLogger().error([m
[31m-                    "Não foi possível salvar o config.yml.",[m
[31m-                    e[m
[32m+[m[32m            return port;[m
[32m+[m
[32m+[m[32m        } catch (SerializationException e) {[m
[32m+[m
[32m+[m[32m            plugin.getLogger().warn([m
[32m+[m[32m                    "Não foi possível ler broadcast-port. Usando 0."[m
             );[m
[32m+[m
[32m+[m[32m            return DEFAULT_BROADCAST_PORT;[m
[32m+[m[32m        }[m
[32m+[m[32m    }[m
[32m+[m
[32m+[m[32m    public String getNetworkInterface() {[m
[32m+[m
[32m+[m[32m        if (root == null) {[m
[32m+[m[32m            return DEFAULT_NETWORK_INTERFACE;[m
[32m+[m[32m        }[m
[32m+[m
[32m+[m[32m        try {[m
[32m+[m
[32m+[m[32m            String networkInterface =[m
[32m+[m[32m                    root.node("network-interface")[m
[32m+[m[32m                            .get(String.class);[m
[32m+[m
[32m+[m[32m            if (networkInterface == null[m
[32m+[m[32m                    || networkInterface.isBlank()) {[m
[32m+[m
[32m+[m[32m                return DEFAULT_NETWORK_INTERFACE;[m
[32m+[m[32m            }[m
[32m+[m
[32m+[m[32m            return networkInterface.trim();[m
[32m+[m
[32m+[m[32m        } catch (SerializationException e) {[m
[32m+[m
[32m+[m[32m            return DEFAULT_NETWORK_INTERFACE;[m
         }[m
     }[m
 [m
[31m-    public void reload() {[m
[32m+[m[32m    public String getMotd() {[m
[32m+[m
[32m+[m[32m        if (root == null) {[m
[32m+[m[32m            return DEFAULT_MOTD;[m
[32m+[m[32m        }[m
 [m
         try {[m
 [m
[31m-            loadFromFile();[m
[32m+[m[32m            String motd =[m
[32m+[m[32m                    root.node("motd")[m
[32m+[m[32m                            .get(String.class);[m
[32m+[m
[32m+[m[32m            if (motd == null) {[m
[32m+[m[32m                return DEFAULT_MOTD;[m
[32m+[m[32m            }[m
[32m+[m
[32m+[m[32m            return motd;[m
[32m+[m
[32m+[m[32m        } catch (SerializationException e) {[m
[32m+[m
[32m+[m[32m            return DEFAULT_MOTD;[m
[32m+[m[32m        }[m
[32m+[m[32m    }[m
[32m+[m
[32m+[m[32m    /*[m
[32m+[m[32m     * ============================================================[m
[32m+[m[32m     * Generic Setter[m
[32m+[m[32m     * ============================================================[m
[32m+[m[32m     */[m
[32m+[m
[32m+[m[32m    /**[m
[32m+[m[32m     * Sets a configuration value and saves the configuration.[m
[32m+[m[32m     *[m
[32m+[m[32m     * <p>This method exists for compatibility with CommandHandler.</p>[m
[32m+[m[32m     *[m
[32m+[m[32m     * @param path configuration path[m
[32m+[m[32m     * @param value new value[m
[32m+[m[32m     * @return true if the value was saved successfully[m
[32m+[m[32m     */[m
[32m+[m[32m    public boolean set(String path, Object value) {[m
[32m+[m
[32m+[m[32m        if (root == null) {[m
 [m
[31m-            plugin.getLogger().info([m
[31m-                    "Configuração recarregada."[m
[32m+[m[32m            plugin.getLogger().warn([m
[32m+[m[32m                    "Não foi possível alterar '{}' porque a configuração ainda não foi carregada.",[m
[32m+[m[32m                    path[m
             );[m
 [m
[31m-        } catch (IOException e) {[m
[32m+[m[32m            return false;[m
[32m+[m[32m        }[m
[32m+[m
[32m+[m[32m        if (path == null || path.isBlank()) {[m
[32m+[m[32m            return false;[m
[32m+[m[32m        }[m
[32m+[m
[32m+[m[32m        try {[m
[32m+[m
[32m+[m[32m            String[] parts = path.split("\\.");[m
[32m+[m
[32m+[m[32m            root.node((Object[]) parts).set(value);[m
[32m+[m
[32m+[m[32m            return save();[m
[32m+[m
[32m+[m[32m        } catch (SerializationException e) {[m
 [m
             plugin.getLogger().error([m
[31m-                    "Não foi possível recarregar o config.yml.",[m
[32m+[m[32m                    "Não foi possível alterar a configuração '{}'.",[m
[32m+[m[32m                    path,[m
                     e[m
             );[m
[32m+[m
[32m+[m[32m            return false;[m
         }[m
     }[m
 [m
[31m-    public void set(String path, Object value) {[m
[32m+[m[32m    /*[m
[32m+[m[32m     * ============================================================[m
[32m+[m[32m     * Setters[m
[32m+[m[32m     * ============================================================[m
[32m+[m[32m     */[m
[32m+[m
[32m+[m[32m    public boolean setLanguage(String language) {[m
 [m
         if (root == null) {[m
[31m-            plugin.getLogger().warn([m
[31m-                    "Não é possível alterar a configuração porque ela não foi carregada."[m
[31m-            );[m
[31m-            return;[m
[32m+[m[32m            return false;[m
[32m+[m[32m        }[m
[32m+[m
[32m+[m[32m        if (language == null || language.isBlank()) {[m
[32m+[m[32m            return false;[m
[32m+[m[32m        }[m
[32m+[m
[32m+[m[32m        language = language.trim().toLowerCase(Locale.ROOT);[m
[32m+[m
[32m+[m[32m        if (!language.equals("en")[m
[32m+[m[32m                && !language.equals("br")[m
[32m+[m[32m                && !language.equals("zh")) {[m
[32m+[m[32m            return false;[m
         }[m
 [m
         try {[m
 [m
[31m-            root.node((Object[]) path.split("\\.")).set(value);[m
[32m+[m[32m            root.node("language").set(language);[m
[32m+[m
[32m+[m[32m            return save();[m
 [m
[31m-        } catch (org.spongepowered.configurate.serialize.SerializationException e) {[m
[32m+[m[32m        } catch (SerializationException e) {[m
 [m
             plugin.getLogger().error([m
[31m-                    "Não foi possível alterar a configuração: " + path,[m
[32m+[m[32m                    "Não foi possível alterar o idioma.",[m
                     e[m
             );[m
[32m+[m
[32m+[m[32m            return false;[m
         }[m
     }[m
[31m-    public String getLanguage() {[m
[31m-        return root.node("language").getString("en");[m
[32m+[m
[32m+[m[32m    public boolean setDebug(boolean debug) {[m
[32m+[m
[32m+[m[32m        if (root == null) {[m
[32m+[m[32m            return false;[m
[32m+[m[32m        }[m
[32m+[m
[32m+[m[32m        try {[m
[32m+[m
[32m+[m[32m            root.node("debug").set(debug);[m
[32m+[m
[32m+[m[32m            return save();[m
[32m+[m
[32m+[m[32m        } catch (SerializationException e) {[m
[32m+[m
[32m+[m[32m            plugin.getLogger().error([m
[32m+[m[32m                    "Não foi possível alterar debug.",[m
[32m+[m[32m                    e[m
[32m+[m[32m            );[m
[32m+[m
[32m+[m[32m            return false;[m
[32m+[m[32m        }[m
     }[m
 [m
[31m-    public boolean isDebug() {[m
[31m-        return root.node("debug").getBoolean(false);[m
[32m+[m[32m    public boolean setBroadcastEnabled(boolean enabled) {[m
[32m+[m
[32m+[m[32m        if (root == null) {[m
[32m+[m[32m            return false;[m
[32m+[m[32m        }[m
[32m+[m
[32m+[m[32m        try {[m
[32m+[m
[32m+[m[32m            root.node("broadcast-enabled")[m
[32m+[m[32m                    .set(enabled);[m
[32m+[m
[32m+[m[32m            return save();[m
[32m+[m
[32m+[m[32m        } catch (SerializationException e) {[m
[32m+[m
[32m+[m[32m            plugin.getLogger().error([m
[32m+[m[32m                    "Não foi possível alterar broadcast-enabled.",[m
[32m+[m[32m                    e[m
[32m+[m[32m            );[m
[32m+[m
[32m+[m[32m            return false;[m
[32m+[m[32m        }[m
     }[m
 [m
[31m-    public boolean isBroadcastEnabled() {[m
[31m-        return root.node("broadcast-enabled").getBoolean(false);[m
[32m+[m[32m    public boolean setBroadcastDelayMs(long delayMs) {[m
[32m+[m
[32m+[m[32m        if (root == null) {[m
[32m+[m[32m            return false;[m
[32m+[m[32m        }[m
[32m+[m
[32m+[m[32m        if (delayMs < MIN_DELAY_MS[m
[32m+[m[32m                || delayMs > MAX_DELAY_MS) {[m
[32m+[m
[32m+[m[32m            return false;[m
[32m+[m[32m        }[m
[32m+[m
[32m+[m[32m        try {[m
[32m+[m
[32m+[m[32m            root.node("broadcast-delay-ms")[m
[32m+[m[32m                    .set(delayMs);[m
[32m+[m
[32m+[m[32m            return save();[m
[32m+[m
[32m+[m[32m        } catch (SerializationException e) {[m
[32m+[m
[32m+[m[32m            plugin.getLogger().error([m
[32m+[m[32m                    "Não foi possível alterar broadcast-delay-ms.",[m
[32m+[m[32m                    e[m
[32m+[m[32m            );[m
[32m+[m
[32m+[m[32m            return false;[m
[32m+[m[32m        }[m
     }[m
 [m
[31m-    public long getBroadcastDelayMs() {[m
[31m-        return root.node("broadcast-delay-ms").getLong(1500);[m
[32m+[m[32m    public boolean setBroadcastPort(int port) {[m
[32m+[m
[32m+[m[32m        if (root == null) {[m
[32m+[m[32m            return false;[m
[32m+[m[32m        }[m
[32m+[m
[32m+[m[32m        if (port < 0 || port > 65535) {[m
[32m+[m[32m            return false;[m
[32m+[m[32m        }[m
[32m+[m
[32m+[m[32m        try {[m
[32m+[m
[32m+[m[32m            root.node("broadcast-port")[m
[32m+[m[32m                    .set(port);[m
[32m+[m
[32m+[m[32m            return save();[m
[32m+[m
[32m+[m[32m        } catch (SerializationException e) {[m
[32m+[m
[32m+[m[32m            plugin.getLogger().error([m
[32m+[m[32m                    "Não foi possível alterar broadcast-port.",[m
[32m+[m[32m                    e[m
[32m+[m[32m            );[m
[32m+[m
[32m+[m[32m            return false;[m
[32m+[m[32m        }[m
     }[m
 [m
[31m-    public int getBroadcastPort() {[m
[31m-        return root.node("broadcast-port").getInt(0);[m
[32m+[m[32m    public boolean setNetworkInterface(String networkInterface) {[m
[32m+[m
[32m+[m[32m        if (root == null) {[m
[32m+[m[32m            return false;[m
[32m+[m[32m        }[m
[32m+[m
[32m+[m[32m        if (networkInterface == null[m
[32m+[m[32m                || networkInterface.isBlank()) {[m
[32m+[m
[32m+[m[32m            return false;[m
[32m+[m[32m        }[m
[32m+[m
[32m+[m[32m        networkInterface = networkInterface.trim();[m
[32m+[m
[32m+[m[32m        try {[m
[32m+[m
[32m+[m[32m            root.node("network-interface")[m
[32m+[m[32m                    .set(networkInterface);[m
[32m+[m
[32m+[m[32m            return save();[m
[32m+[m
[32m+[m[32m        } catch (SerializationException e) {[m
[32m+[m
[32m+[m[32m            plugin.getLogger().error([m
[32m+[m[32m                    "Não foi possível alterar network-interface.",[m
[32m+[m[32m                    e[m
[32m+[m[32m            );[m
[32m+[m
[32m+[m[32m            return false;[m
[32m+[m[32m        }[m
     }[m
 [m
[31m-    public String getMotd() {[m
[31m-        return root.node("motd").getString("A Minecraft Server");[m
[32m+[m[32m    public boolean setMotd(String motd) {[m
[32m+[m
[32m+[m[32m        if (root == null) {[m
[32m+[m[32m            return false;[m
[32m+[m[32m        }[m
[32m+[m
[32m+[m[32m        if (motd == null) {[m
[32m+[m[32m            return false;[m
[32m+[m[32m        }[m
[32m+[m
[32m+[m[32m        try {[m
[32m+[m
[32m+[m[32m            root.node("motd").set(motd);[m
[32m+[m
[32m+[m[32m            return save();[m
[32m+[m
[32m+[m[32m        } catch (SerializationException e) {[m
[32m+[m
[32m+[m[32m            plugin.getLogger().error([m
[32m+[m[32m                    "Não foi possível alterar o MOTD.",[m
[32m+[m[32m                    e[m
[32m+[m[32m            );[m
[32m+[m
[32m+[m[32m            return false;[m
[32m+[m[32m        }[m
     }[m
[31m-    public ConfigurationNode getRoot() {[m
[31m-        return root;[m
[32m+[m
[32m+[m[32m    /*[m
[32m+[m[32m     * ============================================================[m
[32m+[m[32m     * Utility[m
[32m+[m[32m     * ============================================================[m
[32m+[m[32m     */[m
[32m+[m
[32m+[m[32m    public Path getConfigFile() {[m
[32m+[m[32m        return configFile;[m
     }[m
 [m
     public Path getDataDirectory() {[m
         return dataDirectory;[m
     }[m
 [m
[31m-    public Path getConfigFile() {[m
[31m-        return configFile;[m
[32m+[m[32m    public CommentedConfigurationNode getRoot() {[m
[32m+[m[32m        return root;[m
     }[m
 }[m
\ No newline at end of file[m
[1mdiff --git a/src/main/java/com/betterlanbroadcaster/Language.java b/src/main/java/com/betterlanbroadcaster/Language.java[m
[1mindex e172793..f5e09b9 100644[m
[1m--- a/src/main/java/com/betterlanbroadcaster/Language.java[m
[1m+++ b/src/main/java/com/betterlanbroadcaster/Language.java[m
[36m@@ -1,97 +1,230 @@[m
 package com.betterlanbroadcaster;[m
 [m
[31m-import org.spongepowered.configurate.ConfigurationNode;[m
[31m-import org.spongepowered.configurate.yaml.YamlConfigurationLoader;[m
[32m+[m[32mimport org.slf4j.Logger;[m
 [m
 import java.io.BufferedReader;[m
 import java.io.IOException;[m
 import java.io.InputStream;[m
 import java.io.InputStreamReader;[m
 import java.nio.charset.StandardCharsets;[m
[32m+[m[32mimport java.util.Locale;[m
[32m+[m[32mimport java.util.Map;[m
[32m+[m
[32m+[m[32mimport org.yaml.snakeyaml.Yaml;[m
 [m
 public class Language {[m
 [m
[32m+[m[32m    private static final String DEFAULT_LANGUAGE = "en";[m
[32m+[m
     private final BetterLANBroadcaster plugin;[m
[32m+[m[32m    private final Logger logger;[m
[32m+[m
[32m+[m[32m    private String language = DEFAULT_LANGUAGE;[m
 [m
[31m-    private ConfigurationNode root;[m
[31m-    private String currentLanguage;[m
[32m+[m[32m    private Map<String, Object> messages = Map.of();[m
[32m+[m[32m    private Map<String, Object> fallbackMessages = Map.of();[m
 [m
     public Language(BetterLANBroadcaster plugin) {[m
         this.plugin = plugin;[m
[32m+[m[32m        this.logger = plugin.getLogger();[m
[32m+[m
[32m+[m[32m        load(plugin.getConfig().getLanguage());[m
     }[m
 [m
[32m+[m[32m    /**[m
[32m+[m[32m     * Loads the specified language.[m
[32m+[m[32m     */[m
     public void load(String language) {[m
[32m+[m[32m        String requestedLanguage = language;[m
 [m
[31m-        if (language == null || language.isBlank()) {[m
[31m-            language = "en";[m
[32m+[m[32m        if (requestedLanguage == null || requestedLanguage.isBlank()) {[m
[32m+[m[32m            requestedLanguage = DEFAULT_LANGUAGE;[m
         }[m
 [m
[31m-        language = language.toLowerCase();[m
[32m+[m[32m        requestedLanguage = requestedLanguage.toLowerCase(Locale.ROOT);[m
 [m
[31m-        String resourcePath = "/lang/messages_" + language + ".yml";[m
[32m+[m[32m        fallbackMessages = loadLanguageFile(DEFAULT_LANGUAGE);[m
 [m
[31m-        try {[m
[31m-            InputStream input = getClass().getResourceAsStream(resourcePath);[m
[32m+[m[32m        if (fallbackMessages == null) {[m
[32m+[m[32m            fallbackMessages = Map.of();[m
[32m+[m[32m        }[m
 [m
[31m-            if (input == null) {[m
[32m+[m[32m        Map<String, Object> loadedMessages;[m
 [m
[31m-                plugin.getLogger().warn([m
[31m-                        "Idioma '" + language + "' não encontrado. Usando en."[m
[31m-                );[m
[32m+[m[32m        if (DEFAULT_LANGUAGE.equals(requestedLanguage)) {[m
[32m+[m[32m            loadedMessages = fallbackMessages;[m
[32m+[m[32m        } else {[m
[32m+[m[32m            loadedMessages = loadLanguageFile(requestedLanguage);[m
 [m
[31m-                language = "en";[m
[31m-                resourcePath = "/lang/messages_en.yml";[m
[32m+[m[32m            if (loadedMessages == null) {[m
[32m+[m[32m                logger.warn([m
[32m+[m[32m                        "Language '{}' not found. Falling back to English.",[m
[32m+[m[32m                        requestedLanguage[m
[32m+[m[32m                );[m
 [m
[31m-                input = getClass().getResourceAsStream(resourcePath);[m
[32m+[m[32m                requestedLanguage = DEFAULT_LANGUAGE;[m
[32m+[m[32m                loadedMessages = fallbackMessages;[m
             }[m
[32m+[m[32m        }[m
 [m
[31m-            if (input == null) {[m
[31m-                throw new IOException([m
[31m-                        "messages_en.yml não encontrado dentro do JAR."[m
[31m-                );[m
[31m-            }[m
[32m+[m[32m        messages = loadedMessages != null[m
[32m+[m[32m                ? loadedMessages[m
[32m+[m[32m                : Map.of();[m
[32m+[m
[32m+[m[32m        language = requestedLanguage;[m
[32m+[m
[32m+[m[32m        logger.info("Language loaded: {}", language);[m
[32m+[m[32m    }[m
[32m+[m
[32m+[m[32m    private Map<String, Object> loadLanguageFile(String lang) {[m
[32m+[m[32m        String path = "/lang/messages_" + lang + ".yml";[m
 [m
[31m-            try (BufferedReader reader =[m
[31m-            new BufferedReader([m
[31m-                new InputStreamReader(input, StandardCharsets.UTF_8))) {[m
[31m-                YamlConfigurationLoader loader =[m
[31m-                        YamlConfigurationLoader.builder()[m
[31m-                                .source(() -> reader)[m
[31m-                                .build();[m
[32m+[m[32m        try (InputStream inputStream = Language.class.getResourceAsStream(path)) {[m
 [m
[31m-                root = loader.load();[m
[32m+[m[32m            if (inputStream == null) {[m
[32m+[m[32m                logger.warn("Language file not found: {}", path);[m
[32m+[m[32m                return null;[m
             }[m
 [m
[31m-            currentLanguage = language;[m
[32m+[m[32m            try (BufferedReader reader = new BufferedReader([m
[32m+[m[32m                    new InputStreamReader([m
[32m+[m[32m                            inputStream,[m
[32m+[m[32m                            StandardCharsets.UTF_8[m
[32m+[m[32m                    )[m
[32m+[m[32m            )) {[m
 [m
[31m-            plugin.getLogger().info([m
[31m-                    "Idioma carregado: " + currentLanguage[m
[31m-            );[m
[32m+[m[32m                Yaml yaml = new Yaml();[m
[32m+[m[32m                Object loaded = yaml.load(reader);[m
[32m+[m
[32m+[m[32m                if (loaded instanceof Map<?, ?> map) {[m
[32m+[m[32m                    @SuppressWarnings("unchecked")[m
[32m+[m[32m                    Map<String, Object> result =[m
[32m+[m[32m                            (Map<String, Object>) map;[m
[32m+[m
[32m+[m[32m                    return result;[m
[32m+[m[32m                }[m
[32m+[m
[32m+[m[32m                logger.warn("Invalid language file: {}", path);[m
 [m
[31m-        } catch (IOException e) {[m
[32m+[m[32m            }[m
 [m
[31m-            plugin.getLogger().error([m
[31m-                    "Não foi possível carregar o idioma: " + language,[m
[31m-                    e[m
[32m+[m[32m        } catch (IOException | RuntimeException exception) {[m
[32m+[m[32m            logger.error([m
[32m+[m[32m                    "Failed to load language file '{}'.",[m
[32m+[m[32m                    path,[m
[32m+[m[32m                    exception[m
             );[m
         }[m
[32m+[m
[32m+[m[32m        return null;[m
     }[m
 [m
[32m+[m[32m    /**[m
[32m+[m[32m     * Gets a translated message without adding the prefix.[m
[32m+[m[32m     */[m
     public String get(String path) {[m
[32m+[m[32m        Object value = find(messages, path);[m
[32m+[m
[32m+[m[32m        if (value == null) {[m
[32m+[m[32m            value = find(fallbackMessages, path);[m
[32m+[m[32m        }[m
 [m
[31m-        if (root == null) {[m
[32m+[m[32m        if (value == null) {[m
             return path;[m
         }[m
 [m
[31m-        return root.node((Object[]) path.split("\\."))[m
[31m-                .getString(path);[m
[32m+[m[32m        return String.valueOf(value);[m
[32m+[m[32m    }[m
[32m+[m
[32m+[m[32m    /**[m
[32m+[m[32m     * Gets a translated message and replaces numbered placeholders.[m
[32m+[m[32m     *[m
[32m+[m[32m     * Example:[m
[32m+[m[32m     * language.get("broadcast.status.port", 25566)[m
[32m+[m[32m     */[m
[32m+[m[32m    public String get(String path, Object... args) {[m
[32m+[m[32m        return format(path, args);[m
[32m+[m[32m    }[m
[32m+[m
[32m+[m[32m    /**[m
[32m+[m[32m     * Formats a translated message.[m
[32m+[m[32m     *[m
[32m+[m[32m     * Kept for compatibility with the existing CommandHandler.[m
[32m+[m[32m     */[m
[32m+[m[32m    public String format(String path, Object... args) {[m
[32m+[m[32m        String message = get(path);[m
[32m+[m
[32m+[m[32m        if (args != null) {[m
[32m+[m[32m            for (int i = 0; i < args.length; i++) {[m
[32m+[m[32m                message = message.replace([m
[32m+[m[32m                        "{" + i + "}",[m
[32m+[m[32m                        String.valueOf(args[i])[m
[32m+[m[32m                );[m
[32m+[m[32m            }[m
[32m+[m[32m        }[m
[32m+[m
[32m+[m[32m        return message;[m
[32m+[m[32m    }[m
[32m+[m
[32m+[m[32m    /**[m
[32m+[m[32m     * Gets a translated message with the standard prefix.[m
[32m+[m[32m     *[m
[32m+[m[32m     * Example:[m
[32m+[m[32m     *[m
[32m+[m[32m     * [BetterLANBroadcaster] Broadcast started.[m
[32m+[m[32m     */[m
[32m+[m[32m    public String message(String path) {[m
[32m+[m[32m        return get("prefix") + get(path);[m
[32m+[m[32m    }[m
[32m+[m
[32m+[m[32m    /**[m
[32m+[m[32m     * Gets a translated message with the standard prefix[m
[32m+[m[32m     * and replaces numbered placeholders.[m
[32m+[m[32m     */[m
[32m+[m[32m    public String message(String path, Object... args) {[m
[32m+[m[32m        return get("prefix") + format(path, args);[m
[32m+[m[32m    }[m
[32m+[m
[32m+[m[32m    /**[m
[32m+[m[32m     * Gets a translated message without the prefix.[m
[32m+[m[32m     */[m
[32m+[m[32m    public String raw(String path) {[m
[32m+[m[32m        return get(path);[m
[32m+[m[32m    }[m
[32m+[m
[32m+[m[32m    /**[m
[32m+[m[32m     * Gets a translated message without the prefix[m
[32m+[m[32m     * and replaces numbered placeholders.[m
[32m+[m[32m     */[m
[32m+[m[32m    public String raw(String path, Object... args) {[m
[32m+[m[32m        return format(path, args);[m
[32m+[m[32m    }[m
[32m+[m
[32m+[m[32m    private Object find(Map<String, Object> root, String path) {[m
[32m+[m[32m        if (root == null || path == null || path.isBlank()) {[m
[32m+[m[32m            return null;[m
[32m+[m[32m        }[m
[32m+[m
[32m+[m[32m        String[] parts = path.split("\\.");[m
[32m+[m
[32m+[m[32m        Object current = root;[m
[32m+[m
[32m+[m[32m        for (String part : parts) {[m
[32m+[m[32m            if (!(current instanceof Map<?, ?> map)) {[m
[32m+[m[32m                return null;[m
[32m+[m[32m            }[m
[32m+[m
[32m+[m[32m            current = map.get(part);[m
[32m+[m[32m        }[m
[32m+[m
[32m+[m[32m        return current;[m
     }[m
 [m
     public String getLanguage() {[m
[31m-        return currentLanguage;[m
[32m+[m[32m        return language;[m
     }[m
 [m
[31m-    public ConfigurationNode getRoot() {[m
[31m-        return root;[m
[32m+[m[32m    public BetterLANBroadcaster getPlugin() {[m
[32m+[m[32m        return plugin;[m
     }[m
 }[m
\ No newline at end of file[m
[1mdiff --git a/src/main/java/com/betterlanbroadcaster/MulticastBroadcaster.java b/src/main/java/com/betterlanbroadcaster/MulticastBroadcaster.java[m
[1mindex d88fefd..aa09eb9 100644[m
[1m--- a/src/main/java/com/betterlanbroadcaster/MulticastBroadcaster.java[m
[1m+++ b/src/main/java/com/betterlanbroadcaster/MulticastBroadcaster.java[m
[36m@@ -1,11 +1,18 @@[m
 package com.betterlanbroadcaster;[m
 [m
 import java.io.IOException;[m
[31m-import java.net.DatagramPacket;[m
[31m-import java.net.DatagramSocket;[m
[32m+[m[32mimport java.nio.channels.DatagramChannel;[m
[32m+[m[32mimport java.net.Inet4Address;[m
 import java.net.InetAddress;[m
[31m-import java.net.UnknownHostException;[m
[32m+[m[32mimport java.net.InetSocketAddress;[m
[32m+[m[32mimport java.net.NetworkInterface;[m
[32m+[m[32mimport java.net.StandardProtocolFamily;[m
[32m+[m[32mimport java.net.StandardSocketOptions;[m
[32m+[m[32mimport java.nio.ByteBuffer;[m
 import java.nio.charset.StandardCharsets;[m
[32m+[m[32mimport java.util.ArrayList;[m
[32m+[m[32mimport java.util.Enumeration;[m
[32m+[m[32mimport java.util.List;[m
 import java.util.concurrent.Executors;[m
 import java.util.concurrent.ScheduledExecutorService;[m
 import java.util.concurrent.ScheduledFuture;[m
[36m@@ -15,202 +22,668 @@[m [mpublic class MulticastBroadcaster {[m
 [m
     private static final String MULTICAST_ADDRESS = "224.0.2.60";[m
     private static final int MULTICAST_PORT = 4445;[m
[32m+[m[32m    private static final int MULTICAST_TTL = 1;[m
[32m+[m[32m    private static final int MAX_PACKET_SIZE = 1400;[m
 [m
     private final BetterLANBroadcaster plugin;[m
[32m+[m[32m    private final String configuredMotd;[m
[32m+[m[32m    private final int advertisedPort;[m
[32m+[m[32m    private final long delayMs;[m
[32m+[m[32m    private final String configuredInterface;[m
 [m
[31m-    private final ScheduledExecutorService scheduler =[m
[31m-            Executors.newSingleThreadScheduledExecutor(r -> {[m
[31m-                Thread t = new Thread(r, "BetterLANBroadcaster");[m
[31m-                t.setDaemon(true);[m
[31m-                return t;[m
[31m-            });[m
[32m+[m[32m    private final ScheduledExecutorService scheduler;[m
[32m+[m[32m    private final List<InterfaceSocket> sockets = new ArrayList<>();[m
 [m
[31m-    private String motd;[m
[31m-    private int port;[m
[31m-    private int delayMs;[m
[31m-    private boolean running;[m
[31m-    private boolean debug;[m
[31m-    private ScheduledFuture<?> scheduledFuture;[m
[32m+[m[32m    private ScheduledFuture<?> broadcastTask;[m
[32m+[m
[32m+[m[32m    private volatile boolean running;[m
[32m+[m[32m    private volatile boolean shutdown;[m
[32m+[m[32m    private volatile boolean debug;[m
 [m
     public MulticastBroadcaster([m
             BetterLANBroadcaster plugin,[m
             String motd,[m
             int port,[m
[31m-            int delayMs[m
[32m+[m[32m            long delayMs,[m
[32m+[m[32m            String networkInterface[m
     ) {[m
         this.plugin = plugin;[m
[31m-        this.motd = sanitizeMotd(motd);[m
[31m-        this.port = port;[m
[31m-        this.delayMs = Math.max(50, delayMs);[m
[31m-        this.running = false;[m
[31m-        this.debug = false;[m
[32m+[m[32m        this.configuredMotd = motd;[m
[32m+[m[32m        this.advertisedPort = port;[m
[32m+[m[32m        this.delayMs = delayMs;[m
[32m+[m[32m        this.configuredInterface = networkInterface == null[m
[32m+[m[32m                ? "auto"[m
[32m+[m[32m                : networkInterface;[m
[32m+[m
[32m+[m[32m        this.scheduler = Executors.newSingleThreadScheduledExecutor(task -> {[m
[32m+[m[32m            Thread thread = new Thread(task, "BetterLANBroadcaster");[m
[32m+[m[32m            thread.setDaemon(true);[m
[32m+[m[32m            return thread;[m
[32m+[m[32m        });[m
     }[m
 [m
[32m+[m[32m    public synchronized void start() throws IOException {[m
[32m+[m[32m        if (shutdown) {[m
[32m+[m[32m            throw new IOException("Multicast broadcaster já foi encerrado.");[m
[32m+[m[32m        }[m
 [m
[31m-    public void start() {[m
         if (running) {[m
             return;[m
         }[m
 [m
[32m+[m[32m        closeSockets();[m
[32m+[m
[32m+[m[32m        InetAddress multicastAddress =[m
[32m+[m[32m                InetAddress.getByName(MULTICAST_ADDRESS);[m
[32m+[m
[32m+[m[32m        if (!multicastAddress.isMulticastAddress()) {[m
[32m+[m[32m            throw new IOException([m
[32m+[m[32m                    "Endereço multicast inválido: "[m
[32m+[m[32m                            + MULTICAST_ADDRESS[m
[32m+[m[32m            );[m
[32m+[m[32m        }[m
[32m+[m
[32m+[m[32m        List<NetworkInterface> interfaces = resolveInterfaces();[m
[32m+[m
[32m+[m[32m        if (interfaces.isEmpty()) {[m
[32m+[m[32m            throw new IOException([m
[32m+[m[32m                    "Nenhuma interface de rede compatível com multicast foi encontrada."[m
[32m+[m[32m            );[m
[32m+[m[32m        }[m
[32m+[m
[32m+[m[32m        int successful = 0;[m
[32m+[m
[32m+[m[32m        for (NetworkInterface networkInterface : interfaces) {[m
[32m+[m[32m            try {[m
[32m+[m[32m                InterfaceSocket interfaceSocket =[m
[32m+[m[32m                        createSocket([m
[32m+[m[32m                                networkInterface,[m
[32m+[m[32m                                multicastAddress[m
[32m+[m[32m                        );[m
[32m+[m
[32m+[m[32m                sockets.add(interfaceSocket);[m
[32m+[m[32m                successful++;[m
[32m+[m
[32m+[m[32m                logDebug([m
[32m+[m[32m                        "Interface multicast ativa: "[m
[32m+[m[32m                                + networkInterface.getDisplayName()[m
[32m+[m[32m                                + " ["[m
[32m+[m[32m                                + networkInterface.getName()[m
[32m+[m[32m                                + "] IPv4="[m
[32m+[m[32m                                + interfaceSocket.ipv4.getHostAddress()[m
[32m+[m[32m                );[m
[32m+[m
[32m+[m[32m            } catch (Exception exception) {[m
[32m+[m[32m                logDebug([m
[32m+[m[32m                        "Não foi possível usar a interface "[m
[32m+[m[32m                                + networkInterface.getDisplayName()[m
[32m+[m[32m                                + " ["[m
[32m+[m[32m                                + networkInterface.getName()[m
[32m+[m[32m                                + "]: "[m
[32m+[m[32m                                + exception.getMessage()[m
[32m+[m[32m                );[m
[32m+[m[32m            }[m
[32m+[m[32m        }[m
[32m+[m
[32m+[m[32m        if (successful == 0) {[m
[32m+[m[32m            closeSockets();[m
[32m+[m
[32m+[m[32m            throw new IOException([m
[32m+[m[32m                    "Nenhuma das interfaces encontradas pôde ser configurada para multicast."[m
[32m+[m[32m            );[m
[32m+[m[32m        }[m
[32m+[m
         running = true;[m
[31m-        scheduleTask();[m
[32m+[m
[32m+[m[32m        long interval = Math.max(50L, delayMs);[m
[32m+[m
[32m+[m[32m        broadcastTask = scheduler.scheduleAtFixedRate([m
[32m+[m[32m                this::broadcastSafely,[m
[32m+[m[32m                0L,[m
[32m+[m[32m                interval,[m
[32m+[m[32m                TimeUnit.MILLISECONDS[m
[32m+[m[32m        );[m
[32m+[m
[32m+[m[32m        plugin.getLogger().info([m
[32m+[m[32m                "Multicast broadcaster iniciado: "[m
[32m+[m[32m                        + MULTICAST_ADDRESS[m
[32m+[m[32m                        + ":"[m
[32m+[m[32m                        + MULTICAST_PORT[m
[32m+[m[32m                        + " usando "[m
[32m+[m[32m                        + successful[m
[32m+[m[32m                        + " interface(s)."[m
[32m+[m[32m        );[m
     }[m
 [m
[31m-    public void stop() {[m
[32m+[m[32m    public synchronized void stop() {[m
[32m+[m[32m        if (!running) {[m
[32m+[m[32m            closeSockets();[m
[32m+[m[32m            return;[m
[32m+[m[32m        }[m
[32m+[m
         running = false;[m
 [m
[31m-        if (scheduledFuture != null) {[m
[31m-            scheduledFuture.cancel(false);[m
[31m-            scheduledFuture = null;[m
[32m+[m[32m        if (broadcastTask != null) {[m
[32m+[m[32m            broadcastTask.cancel(false);[m
[32m+[m[32m            broadcastTask = null;[m
         }[m
[32m+[m
[32m+[m[32m        closeSockets();[m
     }[m
 [m
[31m-    public void shutdown() {[m
[32m+[m[32m    public synchronized void shutdown() {[m
[32m+[m[32m        if (shutdown) {[m
[32m+[m[32m            return;[m
[32m+[m[32m        }[m
[32m+[m
[32m+[m[32m        shutdown = true;[m
[32m+[m
         stop();[m
[32m+[m
         scheduler.shutdownNow();[m
     }[m
[32m+[m
     public boolean isRunning() {[m
         return running;[m
     }[m
 [m
[31m-    public void setMotd(String newMotd) {[m
[31m-        this.motd = sanitizeMotd(newMotd);[m
[31m-[m
[31m-        if (running) {[m
[31m-            reschedule();[m
[31m-        }[m
[32m+[m[32m    public int getPort() {[m
[32m+[m[32m        return advertisedPort;[m
     }[m
 [m
[31m-    public String getMotd() {[m
[31m-        return motd;[m
[32m+[m[32m    public void setDebug(boolean debug) {[m
[32m+[m[32m        this.debug = debug;[m
     }[m
 [m
[31m-    public void setDelayMs(int newDelayMs) {[m
[31m-        this.delayMs = Math.max(50, newDelayMs);[m
[32m+[m[32m    private List<NetworkInterface> resolveInterfaces()[m
[32m+[m[32m            throws IOException {[m
 [m
[31m-        if (running) {[m
[31m-            reschedule();[m
[32m+[m[32m        String configured = configuredInterface == null[m
[32m+[m[32m                ? "auto"[m
[32m+[m[32m                : configuredInterface.trim();[m
[32m+[m
[32m+[m[32m        if (configured.isEmpty()[m
[32m+[m[32m                || configured.equalsIgnoreCase("auto")) {[m
[32m+[m
[32m+[m[32m            return findAllSuitableInterfaces();[m
         }[m
[31m-    }[m
 [m
[31m-    public int getDelayMs() {[m
[31m-        return delayMs;[m
[31m-    }[m
[31m-    public void setPort(int newPort) {[m
[31m-        if (newPort > 0 && newPort <= 65535) {[m
[31m-            this.port = newPort;[m
[32m+[m[32m        NetworkInterface selected =[m
[32m+[m[32m                findConfiguredInterface(configured);[m
[32m+[m
[32m+[m[32m        if (selected == null) {[m
[32m+[m[32m            throw new IOException([m
[32m+[m[32m                    "Interface de rede não encontrada: "[m
[32m+[m[32m                            + configured[m
[32m+[m[32m            );[m
         }[m
[31m-    }[m
 [m
[31m-    public int getPort() {[m
[31m-        return port;[m
[32m+[m[32m        if (!isSuitableInterface(selected)) {[m
[32m+[m[32m            throw new IOException([m
[32m+[m[32m                    "A interface selecionada não é adequada para multicast: "[m
[32m+[m[32m                            + configured[m
[32m+[m[32m            );[m
[32m+[m[32m        }[m
[32m+[m
[32m+[m[32m        List<NetworkInterface> result = new ArrayList<>();[m
[32m+[m[32m        result.add(selected);[m
[32m+[m
[32m+[m[32m        return result;[m
     }[m
 [m
[31m-    public void setDebug(boolean debug) {[m
[31m-        this.debug = debug;[m
[32m+[m[32m    private List<NetworkInterface> findAllSuitableInterfaces()[m
[32m+[m[32m            throws IOException {[m
[32m+[m
[32m+[m[32m        List<NetworkInterface> result =[m
[32m+[m[32m                new ArrayList<>();[m
[32m+[m
[32m+[m[32m        Enumeration<NetworkInterface> enumeration =[m
[32m+[m[32m                NetworkInterface.getNetworkInterfaces();[m
[32m+[m
[32m+[m[32m        if (enumeration == null) {[m
[32m+[m[32m            return result;[m
[32m+[m[32m        }[m
[32m+[m
[32m+[m[32m        while (enumeration.hasMoreElements()) {[m
[32m+[m
[32m+[m[32m            NetworkInterface networkInterface =[m
[32m+[m[32m                    enumeration.nextElement();[m
[32m+[m
[32m+[m[32m            String name =[m
[32m+[m[32m                    networkInterface.getName();[m
[32m+[m
[32m+[m[32m            String displayName =[m
[32m+[m[32m                    networkInterface.getDisplayName();[m
[32m+[m
[32m+[m[32m            try {[m
[32m+[m[32m                logDebug([m
[32m+[m[32m                        "Interface encontrada: "[m
[32m+[m[32m                                + displayName[m
[32m+[m[32m                                + " ["[m
[32m+[m[32m                                + name[m
[32m+[m[32m                                + "]"[m
[32m+[m[32m                );[m
[32m+[m
[32m+[m[32m                if (!networkInterface.isUp()) {[m
[32m+[m[32m                    logDebug([m
[32m+[m[32m                            "Ignorada: interface desligada."[m
[32m+[m[32m                    );[m
[32m+[m[32m                    continue;[m
[32m+[m[32m                }[m
[32m+[m
[32m+[m[32m                if (networkInterface.isLoopback()) {[m
[32m+[m[32m                    logDebug([m
[32m+[m[32m                            "Ignorada: loopback."[m
[32m+[m[32m                    );[m
[32m+[m[32m                    continue;[m
[32m+[m[32m                }[m
[32m+[m
[32m+[m[32m                if (!networkInterface.supportsMulticast()) {[m
[32m+[m[32m                    logDebug([m
[32m+[m[32m                            "Ignorada: multicast não suportado."[m
[32m+[m[32m                    );[m
[32m+[m[32m                    continue;[m
[32m+[m[32m                }[m
[32m+[m
[32m+[m[32m                InetAddress ipv4 =[m
[32m+[m[32m                        findIPv4Address(networkInterface);[m
[32m+[m
[32m+[m[32m                if (ipv4 == null) {[m
[32m+[m[32m                    logDebug([m
[32m+[m[32m                            "Ignorada: nenhum IPv4 encontrado."[m
[32m+[m[32m                    );[m
[32m+[m[32m                    continue;[m
[32m+[m[32m                }[m
[32m+[m
[32m+[m[32m                logDebug([m
[32m+[m[32m                        "Candidata para multicast: "[m
[32m+[m[32m                                + displayName[m
[32m+[m[32m                                + " ["[m
[32m+[m[32m                                + name[m
[32m+[m[32m                                + "] IPv4="[m
[32m+[m[32m                                + ipv4.getHostAddress()[m
[32m+[m[32m                );[m
[32m+[m
[32m+[m[32m                result.add(networkInterface);[m
[32m+[m
[32m+[m[32m            } catch (Exception exception) {[m
[32m+[m
[32m+[m[32m                logDebug([m
[32m+[m[32m                        "Ignorada "[m
[32m+[m[32m                                + displayName[m
[32m+[m[32m                                + ": "[m
[32m+[m[32m                                + exception.getMessage()[m
[32m+[m[32m                );[m
[32m+[m[32m            }[m
[32m+[m[32m        }[m
[32m+[m
[32m+[m[32m        return result;[m
     }[m
 [m
[31m-    public boolean isDebug() {[m
[31m-        return debug;[m
[32m+[m[32m    private boolean isSuitableInterface([m
[32m+[m[32m            NetworkInterface networkInterface[m
[32m+[m[32m    ) {[m
[32m+[m[32m        try {[m
[32m+[m[32m            if (!networkInterface.isUp()) {[m
[32m+[m[32m                return false;[m
[32m+[m[32m            }[m
[32m+[m
[32m+[m[32m            if (networkInterface.isLoopback()) {[m
[32m+[m[32m                return false;[m
[32m+[m[32m            }[m
[32m+[m
[32m+[m[32m            if (!networkInterface.supportsMulticast()) {[m
[32m+[m[32m                return false;[m
[32m+[m[32m            }[m
[32m+[m
[32m+[m[32m            return findIPv4Address(networkInterface) != null;[m
[32m+[m
[32m+[m[32m        } catch (Exception exception) {[m
[32m+[m[32m            return false;[m
[32m+[m[32m        }[m
     }[m
[31m-    private void reschedule() {[m
[31m-        if (scheduledFuture != null) {[m
[31m-            scheduledFuture.cancel(false);[m
[31m-            scheduledFuture = null;[m
[32m+[m
[32m+[m[32m    private NetworkInterface findConfiguredInterface([m
[32m+[m[32m            String configured[m
[32m+[m[32m    ) throws IOException {[m
[32m+[m
[32m+[m[32m        String normalized =[m
[32m+[m[32m                configured.trim();[m
[32m+[m
[32m+[m[32m        Enumeration<NetworkInterface> enumeration =[m
[32m+[m[32m                NetworkInterface.getNetworkInterfaces();[m
[32m+[m
[32m+[m[32m        if (enumeration == null) {[m
[32m+[m[32m            return null;[m
         }[m
 [m
[31m-        if (running) {[m
[31m-            scheduleTask();[m
[32m+[m[32m        while (enumeration.hasMoreElements()) {[m
[32m+[m
[32m+[m[32m            NetworkInterface networkInterface =[m
[32m+[m[32m                    enumeration.nextElement();[m
[32m+[m
[32m+[m[32m            String name =[m
[32m+[m[32m                    networkInterface.getName();[m
[32m+[m
[32m+[m[32m            String displayName =[m
[32m+[m[32m                    networkInterface.getDisplayName();[m
[32m+[m
[32m+[m[32m            if (name != null[m
[32m+[m[32m                    && name.trim().equalsIgnoreCase(normalized)) {[m
[32m+[m
[32m+[m[32m                return networkInterface;[m
[32m+[m[32m            }[m
[32m+[m
[32m+[m[32m            if (displayName != null[m
[32m+[m[32m                    && displayName.trim().equalsIgnoreCase(normalized)) {[m
[32m+[m
[32m+[m[32m                return networkInterface;[m
[32m+[m[32m            }[m
[32m+[m
[32m+[m[32m            InetAddress ipv4 =[m
[32m+[m[32m                    findIPv4Address(networkInterface);[m
[32m+[m
[32m+[m[32m            if (ipv4 != null[m
[32m+[m[32m                    && ipv4.getHostAddress()[m
[32m+[m[32m                    .equalsIgnoreCase(normalized)) {[m
[32m+[m
[32m+[m[32m                return networkInterface;[m
[32m+[m[32m            }[m
         }[m
[32m+[m
[32m+[m[32m        return null;[m
     }[m
 [m
[31m-    private void scheduleTask() {[m
[31m-        scheduledFuture = scheduler.scheduleAtFixedRate([m
[31m-                this::sendBroadcast,[m
[31m-                0,[m
[31m-                delayMs,[m
[31m-                TimeUnit.MILLISECONDS[m
[31m-        );[m
[32m+[m[32m    private InetAddress findIPv4Address([m
[32m+[m[32m            NetworkInterface networkInterface[m
[32m+[m[32m    ) {[m
[32m+[m
[32m+[m[32m        Enumeration<InetAddress> addresses =[m
[32m+[m[32m                networkInterface.getInetAddresses();[m
[32m+[m
[32m+[m[32m        while (addresses.hasMoreElements()) {[m
[32m+[m
[32m+[m[32m            InetAddress address =[m
[32m+[m[32m                    addresses.nextElement();[m
[32m+[m
[32m+[m[32m            if (address instanceof Inet4Address[m
[32m+[m[32m                    && !address.isLoopbackAddress()) {[m
[32m+[m
[32m+[m[32m                return address;[m
[32m+[m[32m            }[m
[32m+[m[32m        }[m
[32m+[m
[32m+[m[32m        return null;[m
     }[m
[31m-    private void sendBroadcast() {[m
[31m-        String resolvedMotd = resolveMotd();[m
 [m
[31m-        String payload =[m
[31m-                "[MOTD]" + resolvedMotd + "[/MOTD][AD]" + port + "[/AD]";[m
[32m+[m[32m    private InterfaceSocket createSocket([m
[32m+[m[32m            NetworkInterface networkInterface,[m
[32m+[m[32m            InetAddress multicastAddress[m
[32m+[m[32m    ) throws IOException {[m
 [m
[31m-        byte[] message = payload.getBytes(StandardCharsets.UTF_8);[m
[32m+[m[32m        InetAddress ipv4 =[m
[32m+[m[32m                findIPv4Address(networkInterface);[m
 [m
[31m-        if (debug) {[m
[31m-            plugin.getLogger().info([m
[31m-                    "[DEBUG] Sending broadcast: "[m
[31m-                            + payload[m
[31m-                            + " -> "[m
[31m-                            + MULTICAST_ADDRESS[m
[31m-                            + ":"[m
[31m-                            + MULTICAST_PORT[m
[32m+[m[32m        if (ipv4 == null) {[m
[32m+[m[32m            throw new IOException([m
[32m+[m[32m                    "A interface não possui um endereço IPv4 válido."[m
             );[m
         }[m
 [m
[31m-        try (DatagramSocket socket = new DatagramSocket()) {[m
[31m-            InetAddress group = InetAddress.getByName(MULTICAST_ADDRESS);[m
[32m+[m[32m        DatagramChannel channel = null;[m
[32m+[m
[32m+[m[32m        try {[m
[32m+[m[32m            /*[m
[32m+[m[32m             * Usa explicitamente IPv4.[m
[32m+[m[32m             *[m
[32m+[m[32m             * Isso evita que o sistema tente criar um socket IPv6[m
[32m+[m[32m             * quando o endereço multicast utilizado pelo protocolo[m
[32m+[m[32m             * é IPv4.[m
[32m+[m[32m             */[m
[32m+[m[32m            channel =[m
[32m+[m[32m                    DatagramChannel.open([m
[32m+[m[32m                            StandardProtocolFamily.INET[m
[32m+[m[32m                    );[m
[32m+[m
[32m+[m[32m            channel.setOption([m
[32m+[m[32m                    StandardSocketOptions.SO_REUSEADDR,[m
[32m+[m[32m                    true[m
[32m+[m[32m            );[m
 [m
[31m-            DatagramPacket packet = new DatagramPacket([m
[31m-                    message,[m
[31m-                    message.length,[m
[31m-                    group,[m
[31m-                    MULTICAST_PORT[m
[32m+[m[32m            channel.setOption([m
[32m+[m[32m                    StandardSocketOptions.IP_MULTICAST_TTL,[m
[32m+[m[32m                    MULTICAST_TTL[m
             );[m
 [m
[31m-            socket.send(packet);[m
[32m+[m[32m            /*[m
[32m+[m[32m             * Esta é a parte principal da correção.[m
[32m+[m[32m             *[m
[32m+[m[32m             * Em vez de:[m
[32m+[m[32m             *[m
[32m+[m[32m             *     MulticastSocket.setInterface(ipv4)[m
[32m+[m[32m             *[m
[32m+[m[32m             * usamos:[m
[32m+[m[32m             *[m
[32m+[m[32m             *     IP_MULTICAST_IF -> NetworkInterface[m
[32m+[m[32m             *[m
[32m+[m[32m             * que é a API NIO moderna para selecionar[m
[32m+[m[32m             * a interface de saída multicast.[m
[32m+[m[32m             */[m
[32m+[m[32m            channel.setOption([m
[32m+[m[32m                    StandardSocketOptions.IP_MULTICAST_IF,[m
[32m+[m[32m                    networkInterface[m
[32m+[m[32m            );[m
 [m
[31m-            if (debug) {[m
[31m-                plugin.getLogger().info([m
[31m-                        "[DEBUG] Broadcast sent successfully ("[m
[31m-                                + message.length[m
[31m-                                + " bytes)"[m
[31m-                );[m
[32m+[m[32m            /*[m
[32m+[m[32m             * Faz o bind no IPv4 da interface.[m
[32m+[m[32m             *[m
[32m+[m[32m             * Porta 0 significa que o sistema escolhe uma[m
[32m+[m[32m             * porta local livre para o socket de envio.[m
[32m+[m[32m             */[m
[32m+[m[32m            channel.bind([m
[32m+[m[32m                    new InetSocketAddress([m
[32m+[m[32m                            ipv4,[m
[32m+[m[32m                            0[m
[32m+[m[32m                    )[m
[32m+[m[32m            );[m
[32m+[m
[32m+[m[32m            return new InterfaceSocket([m
[32m+[m[32m                    networkInterface,[m
[32m+[m[32m                    ipv4,[m
[32m+[m[32m                    channel,[m
[32m+[m[32m                    multicastAddress[m
[32m+[m[32m            );[m
[32m+[m
[32m+[m[32m        } catch (Exception exception) {[m
[32m+[m
[32m+[m[32m            if (channel != null) {[m
[32m+[m[32m                try {[m
[32m+[m[32m                    channel.close();[m
[32m+[m[32m                } catch (IOException ignored) {[m
[32m+[m[32m                }[m
[32m+[m[32m            }[m
[32m+[m
[32m+[m[32m            if (exception instanceof IOException ioException) {[m
[32m+[m[32m                throw ioException;[m
             }[m
 [m
[31m-        } catch (UnknownHostException e) {[m
[31m-            plugin.getLogger().warn([m
[31m-                    "Invalid multicast address: " + MULTICAST_ADDRESS[m
[32m+[m[32m            throw new IOException([m
[32m+[m[32m                    exception.getMessage(),[m
[32m+[m[32m                    exception[m
             );[m
[32m+[m[32m        }[m
[32m+[m[32m    }[m
[32m+[m
[32m+[m[32m    private void broadcastSafely() {[m
 [m
[31m-        } catch (IOException e) {[m
[31m-            plugin.getLogger().warn([m
[31m-                    "Failed to send multicast broadcast: " + e.getMessage()[m
[32m+[m[32m        if (!running || sockets.isEmpty()) {[m
[32m+[m[32m            return;[m
[32m+[m[32m        }[m
[32m+[m
[32m+[m[32m        String payload =[m
[32m+[m[32m                buildPayload();[m
[32m+[m
[32m+[m[32m        byte[] data =[m
[32m+[m[32m                payload.getBytes([m
[32m+[m[32m                        StandardCharsets.UTF_8[m
[32m+[m[32m                );[m
[32m+[m
[32m+[m[32m        if (data.length > MAX_PACKET_SIZE) {[m
[32m+[m
[32m+[m[32m            logDebug([m
[32m+[m[32m                    "Broadcast ignorado porque o pacote excede "[m
[32m+[m[32m                            + MAX_PACKET_SIZE[m
[32m+[m[32m                            + " bytes."[m
             );[m
[32m+[m
[32m+[m[32m            return;[m
[32m+[m[32m        }[m
[32m+[m
[32m+[m[32m        for (InterfaceSocket interfaceSocket :[m
[32m+[m[32m                new ArrayList<>(sockets)) {[m
[32m+[m
[32m+[m[32m            try {[m
[32m+[m[32m                sendPacket([m
[32m+[m[32m                        interfaceSocket,[m
[32m+[m[32m                        data,[m
[32m+[m[32m                        payload[m
[32m+[m[32m                );[m
[32m+[m
[32m+[m[32m            } catch (IOException exception) {[m
[32m+[m
[32m+[m[32m                logDebug([m
[32m+[m[32m                        "Falha ao enviar broadcast pela interface "[m
[32m+[m[32m                                + interfaceSocket.networkInterface[m
[32m+[m[32m                                .getDisplayName()[m
[32m+[m[32m                                + " ["[m
[32m+[m[32m                                + interfaceSocket.networkInterface[m
[32m+[m[32m                                .getName()[m
[32m+[m[32m                                + "]: "[m
[32m+[m[32m                                + exception.getMessage()[m
[32m+[m[32m                );[m
[32m+[m[32m            }[m
         }[m
     }[m
[31m-    private String resolveMotd() {[m
[31m-        String resolved = motd;[m
 [m
[31m-        int online = plugin.getServer().getPlayerCount();[m
[31m-        int max = plugin.getMaxPlayers();[m
[32m+[m[32m    private void sendPacket([m
[32m+[m[32m            InterfaceSocket interfaceSocket,[m
[32m+[m[32m            byte[] data,[m
[32m+[m[32m            String payload[m
[32m+[m[32m    ) throws IOException {[m
[32m+[m
[32m+[m[32m        ByteBuffer buffer =[m
[32m+[m[32m                ByteBuffer.wrap(data);[m
 [m
[31m-        resolved = resolved.replace([m
[31m-                "{online}",[m
[31m-                String.valueOf(online)[m
[32m+[m[32m        InetSocketAddress destination =[m
[32m+[m[32m                new InetSocketAddress([m
[32m+[m[32m                        interfaceSocket.multicastAddress,[m
[32m+[m[32m                        MULTICAST_PORT[m
[32m+[m[32m                );[m
[32m+[m
[32m+[m[32m        interfaceSocket.channel.send([m
[32m+[m[32m                buffer,[m
[32m+[m[32m                destination[m
         );[m
 [m
[31m-        resolved = resolved.replace([m
[31m-                "{max}",[m
[31m-                String.valueOf(max)[m
[32m+[m[32m        logDebug([m
[32m+[m[32m                "Broadcast enviado por "[m
[32m+[m[32m                        + interfaceSocket.networkInterface[m
[32m+[m[32m                        .getDisplayName()[m
[32m+[m[32m                        + " ("[m
[32m+[m[32m                        + interfaceSocket.ipv4.getHostAddress()[m
[32m+[m[32m                        + "): "[m
[32m+[m[32m                        + payload[m
         );[m
[32m+[m[32m    }[m
[32m+[m
[32m+[m[32m    private String buildPayload() {[m
[32m+[m
[32m+[m[32m        String motd =[m
[32m+[m[32m                resolveMotd();[m
 [m
[31m-        resolved = plugin.formatMiniMessage(resolved);[m
[32m+[m[32m        return "[MOTD]"[m
[32m+[m[32m                + motd[m
[32m+[m[32m                + "[/MOTD][AD]"[m
[32m+[m[32m                + advertisedPort[m
[32m+[m[32m                + "[/AD]";[m
[32m+[m[32m    }[m
[32m+[m
[32m+[m[32m    private String resolveMotd() {[m
[32m+[m
[32m+[m[32m        String motd =[m
[32m+[m[32m                configuredMotd == null[m
[32m+[m[32m                        ? ""[m
[32m+[m[32m                        : configuredMotd;[m
[32m+[m
[32m+[m[32m        int online =[m
[32m+[m[32m                plugin.getServer()[m
[32m+[m[32m                        .getPlayerCount();[m
[32m+[m
[32m+[m[32m        int max =[m
[32m+[m[32m                plugin.getServer()[m
[32m+[m[32m                        .getConfiguration()[m
[32m+[m[32m                        .getShowMaxPlayers();[m
[32m+[m
[32m+[m[32m        return motd[m
[32m+[m[32m                .replace([m
[32m+[m[32m                        "{online}",[m
[32m+[m[32m                        String.valueOf(online)[m
[32m+[m[32m                )[m
[32m+[m[32m                .replace([m
[32m+[m[32m                        "{max}",[m
[32m+[m[32m                        String.valueOf(max)[m
[32m+[m[32m                );[m
[32m+[m[32m    }[m
[32m+[m
[32m+[m[32m    private synchronized void closeSockets() {[m
[32m+[m
[32m+[m[32m        for (InterfaceSocket interfaceSocket :[m
[32m+[m[32m                sockets) {[m
[32m+[m
[32m+[m[32m            closeSocket(interfaceSocket);[m
[32m+[m[32m        }[m
[32m+[m
[32m+[m[32m        sockets.clear();[m
[32m+[m[32m    }[m
[32m+[m
[32m+[m[32m    private void closeSocket([m
[32m+[m[32m            InterfaceSocket interfaceSocket[m
[32m+[m[32m    ) {[m
 [m
[31m-        return resolved;[m
[32m+[m[32m        try {[m
[32m+[m[32m            interfaceSocket.channel.close();[m
[32m+[m[32m        } catch (IOException ignored) {[m
[32m+[m[32m        }[m
     }[m
[31m-    private String sanitizeMotd(String raw) {[m
[31m-        if (raw == null) {[m
[31m-            return "";[m
[32m+[m
[32m+[m[32m    private void logDebug(String message) {[m
[32m+[m
[32m+[m[32m        if (!debug) {[m
[32m+[m[32m            return;[m
         }[m
 [m
[31m-        return raw[m
[31m-                .replace("[/MOTD]", "")[m
[31m-                .replace("[/AD]", "")[m
[31m-                .replace("[MOTD]", "")[m
[31m-                .replace("[AD]", "");[m
[32m+[m[32m        plugin.getLogger().info([m
[32m+[m[32m                "[DEBUG] " + message[m
[32m+[m[32m        );[m
[32m+[m[32m    }[m
[32m+[m
[32m+[m[32m    private static final class InterfaceSocket {[m
[32m+[m
[32m+[m[32m        private final NetworkInterface networkInterface;[m
[32m+[m[32m        private final InetAddress ipv4;[m
[32m+[m[32m        private final DatagramChannel channel;[m
[32m+[m[32m        private final InetAddress multicastAddress;[m
[32m+[m
[32m+[m[32m        private InterfaceSocket([m
[32m+[m[32m                NetworkInterface networkInterface,[m
[32m+[m[32m                InetAddress ipv4,[m
[32m+[m[32m                DatagramChannel channel,[m
[32m+[m[32m                InetAddress multicastAddress[m
[32m+[m[32m        ) {[m
[32m+[m[32m            this.networkInterface = networkInterface;[m
[32m+[m[32m            this.ipv4 = ipv4;[m
[32m+[m[32m            this.channel = channel;[m
[32m+[m[32m            this.multicastAddress = multicastAddress;[m
[32m+[m[32m        }[m
     }[m
 }[m
\ No newline at end of file[m
[1mdiff --git a/src/main/resources/config.yml b/src/main/resources/config.yml[m
[1mindex d5e79b2..93b7c1b 100644[m
[1m--- a/src/main/resources/config.yml[m
[1m+++ b/src/main/resources/config.yml[m
[36m@@ -3,7 +3,12 @@[m
 # ============================================================[m
 [m
 # Language used by plugin messages.[m
[31m-# Available: en, zh, br[m
[32m+[m[32m#[m
[32m+[m[32m# Available:[m
[32m+[m[32m#   en = English[m
[32m+[m[32m#   br = Brazilian Portuguese[m
[32m+[m[32m#   zh = Chinese[m
[32m+[m[32m#[m
 language: en[m
 [m
 [m
[36m@@ -14,8 +19,9 @@[m [mlanguage: en[m
 # Enable debug logging.[m
 #[m
 # WARNING:[m
[31m-# When enabled, every LAN broadcast packet will be logged[m
[31m-# to the console.[m
[32m+[m[32m# When enabled, detailed LAN broadcast information will be[m
[32m+[m[32m# printed to the console.[m
[32m+[m[32m#[m
 debug: false[m
 [m
 [m
[36m@@ -24,27 +30,61 @@[m [mdebug: false[m
 # ============================================================[m
 [m
 # Automatically start broadcasting when Velocity starts.[m
[32m+[m[32m#[m
[32m+[m[32m# true  = start automatically[m
[32m+[m[32m# false = do not start automatically[m
[32m+[m[32m#[m
 broadcast-enabled: false[m
 [m
[32m+[m
 # Interval between LAN broadcast packets, in milliseconds.[m
 #[m
 # 1500 ms = 1.5 seconds[m
[32m+[m[32m#[m
 # This is the standard Minecraft LAN discovery interval.[m
[32m+[m[32m#[m
[32m+[m[32m# Minimum: 50 ms[m
[32m+[m[32m# Maximum: 86400000 ms (24 hours)[m
[32m+[m[32m#[m
 broadcast-delay-ms: 1500[m
 [m
 [m
[31m-# Port advertised in the LAN server list.[m
[32m+[m[32m# Port advertised in the Minecraft LAN server list.[m
 #[m
 # 0 = automatically use the port where Velocity is listening.[m
 #[m
 # Example:[m
 # broadcast-port: 25566[m
 #[m
[31m-# This is useful when you want the LAN entry to point to[m
[31m-# a specific port instead of the Velocity listener.[m
[32m+[m[32m# This does NOT change the Velocity listening port.[m
[32m+[m[32m# It only changes the port announced through LAN discovery.[m
[32m+[m[32m#[m
 broadcast-port: 0[m
 [m
 [m
[32m+[m[32m# ============================================================[m
[32m+[m[32m# Network[m
[32m+[m[32m# ============================================================[m
[32m+[m
[32m+[m[32m# Network interface used for LAN multicast.[m
[32m+[m[32m#[m
[32m+[m[32m# auto[m
[32m+[m[32m#   Automatically detects all suitable IPv4 multicast[m
[32m+[m[32m#   interfaces and broadcasts through them.[m
[32m+[m[32m#[m
[32m+[m[32m# Interface name[m
[32m+[m[32m#   Example:[m
[32m+[m[32m#   network-interface: ethernet_32774[m
[32m+[m[32m#[m
[32m+[m[32m# IPv4 address[m
[32m+[m[32m#   Example:[m
[32m+[m[32m#   network-interface: 192.168.0.56[m
[32m+[m[32m#[m
[32m+[m[32m# Using "auto" is recommended for most users.[m
[32m+[m[32m#[m
[32m+[m[32mnetwork-interface: auto[m
[32m+[m
[32m+[m
 # ============================================================[m
 # Server List[m
 # ============================================================[m
[36m@@ -54,25 +94,43 @@[m [mbroadcast-port: 0[m
 # Available placeholders:[m
 #[m
 # {online} = number of players currently connected to Velocity[m
[31m-# {max}    = maximum number of players[m
[32m+[m[32m# {max}    = maximum number of players configured in Velocity[m
[32m+[m[32m#[m
[32m+[m[32m# Legacy color codes:[m
 #[m
[31m-# Color codes:[m
 # &a, &b, &c, &e, etc.[m
 #[m
[31m-# MiniMessage is also supported when available:[m
[31m-# <green>, <red>, <bold>, etc.[m
[32m+[m[32m# MiniMessage is also supported:[m
[32m+[m[32m#[m
[32m+[m[32m# <green>[m
[32m+[m[32m# <red>[m
[32m+[m[32m# <bold>[m
[32m+[m[32m# etc.[m
[32m+[m[32m#[m
 motd: "A Minecraft Server"[m
 [m
 [m
 # ============================================================[m
[31m-# Notes[m
[32m+[m[32m# LAN Discovery Protocol[m
 # ============================================================[m
[32m+[m
[32m+[m[32m# BetterLANBroadcaster uses Minecraft's LAN multicast[m
[32m+[m[32m# discovery protocol.[m
[32m+[m[32m#[m
[32m+[m[32m# Multicast address:[m
[32m+[m[32m#   224.0.2.60[m
[32m+[m[32m#[m
[32m+[m[32m# Multicast port:[m
[32m+[m[32m#   4445[m
[32m+[m[32m#[m
[32m+[m[32m# These values are part of the Minecraft LAN discovery[m
[32m+[m[32m# protocol and normally should NOT be changed.[m
 #[m
[31m-# The plugin uses Minecraft's LAN multicast discovery protocol:[m
[32m+[m[32m# Broadcast packet format:[m
 #[m
[31m-# Address: 224.0.2.60[m
[31m-# Port:    4445[m
[32m+[m[32m# [MOTD]Server MOTD[/MOTD][AD]Server Port[/AD][m
 #[m
[31m-# These values normally should NOT be changed.[m
[32m+[m[32m# Encoding:[m
[32m+[m[32m#   UTF-8[m
 #[m
 # ============================================================[m
\ No newline at end of file[m
[1mdiff --git a/src/main/resources/lang/messages_br.yml b/src/main/resources/lang/messages_br.yml[m
[1mindex e69de29..609a1a6 100644[m
[1m--- a/src/main/resources/lang/messages_br.yml[m
[1m+++ b/src/main/resources/lang/messages_br.yml[m
[36m@@ -0,0 +1,98 @@[m
[32m+[m[32m# ============================================================[m
[32m+[m[32m# BetterLANBroadcaster - Português (Brasil)[m
[32m+[m[32m# ============================================================[m
[32m+[m
[32m+[m[32mprefix: "&7[&6BetterLANBroadcaster&7] "[m
[32m+[m
[32m+[m[32m# ============================================================[m
[32m+[m[32m# Broadcast[m
[32m+[m[32m# ============================================================[m
[32m+[m
[32m+[m[32mbroadcast:[m
[32m+[m[32m  started: "&aBroadcast multicast LAN iniciado."[m
[32m+[m[32m  stopped: "&cBroadcast multicast LAN parado."[m
[32m+[m[32m  already-running: "&eO broadcast multicast LAN já está em execução."[m
[32m+[m[32m  already-stopped: "&eO broadcast multicast LAN já está parado."[m
[32m+[m[32m  disabled: "&eO broadcast LAN automático está desativado na configuração."[m
[32m+[m
[32m+[m[32m  status:[m
[32m+[m[32m    running: "&a&l● &aEm execução"[m
[32m+[m[32m    stopped: "&c&l○ &cParado"[m
[32m+[m[32m    motd: "&7MOTD: &f{0}"[m
[32m+[m[32m    delay: "&7Intervalo: &f{0} ms"[m
[32m+[m[32m    port: "&7Porta: &f{0}"[m
[32m+[m[32m    interface: "&7Interface: &f{0}"[m
[32m+[m
[32m+[m[32m  motd-set: "&aMOTD atualizado para: &f{0}"[m
[32m+[m[32m  delay-set: "&aIntervalo do broadcast definido para &f{0} ms&a."[m
[32m+[m[32m  port-set: "&aPorta do broadcast definida para &f{0}&a."[m
[32m+[m[32m  port-set-auto: "&aPorta do broadcast definida como automática: &f{0}&a."[m
[32m+[m[32m  interface-set: "&aInterface de rede definida para: &f{0}"[m
[32m+[m
[32m+[m[32m# ============================================================[m
[32m+[m[32m# Configuração[m
[32m+[m[32m# ============================================================[m
[32m+[m
[32m+[m[32mconfig:[m
[32m+[m[32m  reloaded: "&aConfiguração recarregada com sucesso."[m
[32m+[m
[32m+[m[32m# ============================================================[m
[32m+[m[32m# Debug[m
[32m+[m[32m# ============================================================[m
[32m+[m
[32m+[m[32mdebug:[m
[32m+[m[32m  "on": "&aModo de debug ativado. Cada pacote enviado será registrado no console."[m
[32m+[m[32m  "off": "&cModo de debug desativado."[m
[32m+[m[32m  status-on: "&a&lAtivado"[m
[32m+[m[32m  status-off: "&7Desativado"[m
[32m+[m[32m  label: "&7Debug: &f{0}"[m
[32m+[m
[32m+[m[32m# ============================================================[m
[32m+[m[32m# Ajuda[m
[32m+[m[32m# ============================================================[m
[32m+[m
[32m+[m[32mhelp:[m
[32m+[m[32m  title: "&6===== Ajuda do BetterLANBroadcaster ====="[m
[32m+[m[32m  start: "&e/blb start &7- Iniciar o broadcast LAN"[m
[32m+[m[32m  stop: "&e/blb stop &7- Parar o broadcast LAN"[m
[32m+[m[32m  status: "&e/blb status &7- Ver o status do broadcast"[m
[32m+[m[32m  setmotd: "&e/blb setmotd <MOTD> &7- Alterar a MOTD do broadcast"[m
[32m+[m[32m  setdelay: "&e/blb setdelay <ms> &7- Alterar o intervalo do broadcast em milissegundos"[m
[32m+[m[32m  setport: "&e/blb setport <porta|auto> &7- Definir a porta anunciada"[m
[32m+[m[32m  setinterface: "&e/blb setinterface <auto|nome|IPv4> &7- Definir a interface de rede"[m
[32m+[m[32m  debug: "&e/blb debug <on|off> &7- Ativar ou desativar o modo de debug"[m
[32m+[m[32m  reload: "&e/blb reload &7- Recarregar a configuração"[m
[32m+[m[32m  help: "&e/blb help &7- Mostrar esta ajuda"[m
[32m+[m[32m  version: "&e/blb version &7- Mostrar a versão do plugin"[m
[32m+[m[32m  footer: "&6==================================="[m
[32m+[m
[32m+[m[32m# ============================================================[m
[32m+[m[32m# Versão[m
[32m+[m[32m# ============================================================[m
[32m+[m
[32m+[m[32mversion:[m
[32m+[m[32m  line1: "&6BetterLANBroadcaster &ev{0}"[m
[32m+[m[32m  line2: "&7Autores: &f{0}"[m
[32m+[m[32m  help-hint: "&7Digite &f{0} &7para ver a ajuda"[m
[32m+[m
[32m+[m[32m# ============================================================[m
[32m+[m[32m# Erros[m
[32m+[m[32m# ============================================================[m
[32m+[m
[32m+[m[32merror:[m
[32m+[m[32m  no-permission: "&cVocê não tem permissão para usar este comando."[m
[32m+[m[32m  invalid-delay: "&cIntervalo inválido. Informe um número positivo em milissegundos."[m
[32m+[m[32m  invalid-port: "&cPorta inválida. Informe um número entre 1 e 65535 ou use 'auto'."[m
[32m+[m[32m  invalid-interface: "&cInterface de rede inválida. Use 'auto', o nome da interface ou um endereço IPv4."[m
[32m+[m[32m  invalid-syntax: "&cSintaxe inválida. Uso: &f{0}"[m
[32m+[m[32m  start-failed: "&cNão foi possível iniciar o broadcast LAN."[m
[32m+[m[32m  reload-failed: "&cNão foi possível recarregar o BetterLANBroadcaster."[m
[32m+[m[32m  configuration-error: "&cOcorreu um erro na configuração. Verifique o console para mais detalhes."[m
[32m+[m[32m  stop-failed: "&cNão foi possível parar o broadcast LAN."[m
[32m+[m[32m  invalid-motd: "&cMOTD inválida. Informe uma MOTD que não esteja vazia."[m
[32m+[m[32m  config-save: "&cNão foi possível salvar a configuração."[m
[32m+[m[32m  interface-start: "&cNão foi possível reiniciar o broadcast LAN com a interface de rede selecionada."[m
[32m+[m[32m  invalid-debug: "&cOpção de debug inválida. Use &fon&c ou &foff&c."[m
[32m+[m[32m  config-reload: "&cNão foi possível recarregar a configuração."[m
[32m+[m[32m  reload-broadcast: "&cNão foi possível reiniciar o broadcast LAN após recarregar a configuração."[m
[32m+[m
[1mdiff --git a/src/main/resources/lang/messages_en.yml b/src/main/resources/lang/messages_en.yml[m
[1mindex 1712341..fab3c7d 100644[m
[1m--- a/src/main/resources/lang/messages_en.yml[m
[1m+++ b/src/main/resources/lang/messages_en.yml[m
[36m@@ -1,52 +1,97 @@[m
[31m-# English messages for BetterLANBroadcaster[m
[31m-prefix: "&7[&6BetterLAN&eBroadcaster&7] "[m
[32m+[m[32m# ============================================================[m
[32m+[m[32m# BetterLANBroadcaster - English[m
[32m+[m[32m# ============================================================[m
[32m+[m
[32m+[m[32mprefix: "&7[&6BetterLANBroadcaster&7] "[m
[32m+[m
[32m+[m[32m# ============================================================[m
[32m+[m[32m# Broadcast[m
[32m+[m[32m# ============================================================[m
[32m+[m
 broadcast:[m
[31m-  started: "&aLAN multicast broadcasting &a&lstarted&a."[m
[31m-  stopped: "&cLAN multicast broadcasting &c&lstopped&c."[m
[32m+[m[32m  started: "&aLAN multicast broadcasting started."[m
[32m+[m[32m  stopped: "&cLAN multicast broadcasting stopped."[m
   already-running: "&eLAN multicast broadcasting is already running."[m
   already-stopped: "&eLAN multicast broadcasting is already stopped."[m
[32m+[m[32m  disabled: "&eAutomatic LAN broadcasting is disabled in the configuration."[m
[32m+[m
   status:[m
     running: "&a&l● &aRunning"[m
     stopped: "&c&l○ &cStopped"[m
     motd: "&7MOTD: &f{0}"[m
     delay: "&7Delay: &f{0} ms"[m
     port: "&7Port: &f{0}"[m
[32m+[m[32m    interface: "&7Interface: &f{0}"[m
[32m+[m
   motd-set: "&aMOTD updated to: &f{0}"[m
   delay-set: "&aBroadcast delay set to &f{0} ms&a."[m
   port-set: "&aBroadcast port set to &f{0}&a."[m
   port-set-auto: "&aBroadcast port set to auto-detect: &f{0}&a."[m
[32m+[m[32m  interface-set: "&aNetwork interface set to: &f{0}"[m
[32m+[m
[32m+[m[32m# ============================================================[m
[32m+[m[32m# Configuration[m
[32m+[m[32m# ============================================================[m
 [m
 config:[m
   reloaded: "&aConfiguration reloaded successfully."[m
 [m
[32m+[m[32m# ============================================================[m
[32m+[m[32m# Debug[m
[32m+[m[32m# ============================================================[m
[32m+[m
 debug:[m
[31m-  on: "&aDebug mode enabled. Each broadcast packet will be logged to console."[m
[31m-  off: "&cDebug mode disabled."[m
[32m+[m[32m  "on": "&aDebug mode enabled. Each broadcast packet will be logged to console."[m
[32m+[m[32m  "off": "&cDebug mode disabled."[m
   status-on: "&a&lEnabled"[m
   status-off: "&7Disabled"[m
   label: "&7Debug: &f{0}"[m
 [m
[32m+[m[32m# ============================================================[m
[32m+[m[32m# Help[m
[32m+[m[32m# ============================================================[m
[32m+[m
 help:[m
   title: "&6===== BetterLANBroadcaster Help ====="[m
   start: "&e/blb start &7- Start LAN broadcasting"[m
   stop: "&e/blb stop &7- Stop LAN broadcasting"[m
   status: "&e/blb status &7- View broadcast status"[m
   setmotd: "&e/blb setmotd <MOTD> &7- Set broadcast MOTD"[m
[31m-  setdelay: "&e/blb setdelay <ms> &7- Set broadcast delay (milliseconds)"[m
[31m-  setport: "&e/blb setport <port|auto> &7- Set broadcast port (use 'auto' for auto-detect)"[m
[31m-  debug: "&e/blb debug <on|off> &7- Enable/disable debug mode"[m
[32m+[m[32m  setdelay: "&e/blb setdelay <ms> &7- Set broadcast delay in milliseconds"[m
[32m+[m[32m  setport: "&e/blb setport <port|auto> &7- Set broadcast port"[m
[32m+[m[32m  setinterface: "&e/blb setinterface <auto|name|IPv4> &7- Set network interface"[m
[32m+[m[32m  debug: "&e/blb debug <on|off> &7- Enable or disable debug mode"[m
   reload: "&e/blb reload &7- Reload configuration"[m
   help: "&e/blb help &7- Show this help"[m
   version: "&e/blb version &7- Show plugin version"[m
   footer: "&6==================================="[m
 [m
[32m+[m[32m# ============================================================[m
[32m+[m[32m# Version[m
[32m+[m[32m# ============================================================[m
[32m+[m
 version:[m
   line1: "&6BetterLANBroadcaster &ev{0}"[m
[31m-  line2: "&7Author: &f{0}"[m
[32m+[m[32m  line2: "&7Authors: &f{0}"[m
   help-hint: "&7Type &f{0} &7for help"[m
 [m
[32m+[m[32m# ============================================================[m
[32m+[m[32m# Errors[m
[32m+[m[32m# ============================================================[m
[32m+[m
 error:[m
   no-permission: "&cYou do not have permission to use this command."[m
[31m-  invalid-delay: "&cInvalid delay. Please enter a positive number (milliseconds)."[m
[32m+[m[32m  invalid-delay: "&cInvalid delay. Please enter a positive number in milliseconds."[m
   invalid-port: "&cInvalid port. Please enter a number between 1 and 65535, or use 'auto'."[m
[32m+[m[32m  invalid-interface: "&cInvalid network interface. Use 'auto', an interface name, or an IPv4 address."[m
   invalid-syntax: "&cInvalid syntax. Usage: &f{0}"[m
[32m+[m[32m  start-failed: "&cFailed to start LAN broadcasting."[m
[32m+[m[32m  reload-failed: "&cFailed to reload BetterLANBroadcaster."[m
[32m+[m[32m  configuration-error: "&cA configuration error occurred. Check the console for details."[m
[32m+[m[32m  stop-failed: "&cFailed to stop LAN broadcasting."[m
[32m+[m[32m  invalid-motd: "&cInvalid MOTD. Please enter a non-empty MOTD."[m
[32m+[m[32m  config-save: "&cFailed to save the configuration."[m
[32m+[m[32m  interface-start: "&cFailed to restart LAN broadcasting with the selected network interface."[m
[32m+[m[32m  invalid-debug: "&cInvalid debug option. Use &fon&c or &foff&c."[m
[32m+[m[32m  config-reload: "&cFailed to reload the configuration."[m
[32m+[m[32m  reload-broadcast: "&cFailed to restart LAN broadcasting after reloading the configuration."[m
\ No newline at end of file[m
[1mdiff --git a/src/main/resources/lang/messages_zh.yml b/src/main/resources/lang/messages_zh.yml[m
[1mindex 64522cb..50de61a 100644[m
[1m--- a/src/main/resources/lang/messages_zh.yml[m
[1m+++ b/src/main/resources/lang/messages_zh.yml[m
[36m@@ -1,30 +1,35 @@[m
[31m-# Chinese messages for BetterLANBroadcaster[m
[31m-prefix: "&7[&6BetterLAN&eBroadcaster&7] "[m
[32m+[m[32mprefix: "&7[&6BetterLANBroadcaster&7] "[m
[32m+[m
 broadcast:[m
[31m-  started: "&a局域网广播已&a&l启动&a。"[m
[31m-  stopped: "&c局域网广播已&c&l停止&c。"[m
[31m-  already-running: "&e局域网广播已在运行中。"[m
[31m-  already-stopped: "&e局域网广播已处于停止状态。"[m
[32m+[m[32m  started: "&a局域网组播广播已启动。"[m
[32m+[m[32m  stopped: "&c局域网组播广播已停止。"[m
[32m+[m[32m  already-running: "&e局域网组播广播已经在运行。"[m
[32m+[m[32m  already-stopped: "&e局域网组播广播已经停止。"[m
[32m+[m[32m  disabled: "&e配置中的自动局域网广播已禁用。"[m
[32m+[m
   status:[m
     running: "&a&l● &a运行中"[m
     stopped: "&c&l○ &c已停止"[m
[31m-    motd: "&7MOTD: &f{0}"[m
[31m-    delay: "&7广播延迟: &f{0} 毫秒"[m
[31m-    port: "&7端口: &f{0}"[m
[31m-  motd-set: "&aMOTD 已更新为: &f{0}"[m
[31m-  delay-set: "&a广播延迟已设置为 &f{0} 毫秒&a。"[m
[32m+[m[32m    motd: "&7MOTD：&f{0}"[m
[32m+[m[32m    delay: "&7间隔：&f{0} 毫秒"[m
[32m+[m[32m    port: "&7端口：&f{0}"[m
[32m+[m[32m    interface: "&7网络接口：&f{0}"[m
[32m+[m
[32m+[m[32m  motd-set: "&aMOTD 已更新为：&f{0}"[m
[32m+[m[32m  delay-set: "&a广播间隔已设置为 &f{0} 毫秒&a。"[m
   port-set: "&a广播端口已设置为 &f{0}&a。"[m
[31m-  port-set-auto: "&a广播端口已设置为自动获取: &f{0}&a。"[m
[32m+[m[32m  port-set-auto: "&a广播端口已设置为自动检测：&f{0}&a。"[m
[32m+[m[32m  interface-set: "&a网络接口已设置为：&f{0}"[m
 [m
 config:[m
[31m-  reloaded: "&a配置文件已重新加载。"[m
[32m+[m[32m  reloaded: "&a配置重新加载成功。"[m
 [m
 debug:[m
[31m-  on: "&a调试模式已启用，每次发送广播包将在控制台输出日志。"[m
[31m-  off: "&c调试模式已禁用。"[m
[32m+[m[32m  "on": "&a调试模式已启用。每个广播数据包都会记录到控制台。"[m
[32m+[m[32m  "off": "&c调试模式已禁用。"[m
   status-on: "&a&l已启用"[m
   status-off: "&7已禁用"[m
[31m-  label: "&7调试模式: &f{0}"[m
[32m+[m[32m  label: "&7调试：&f{0}"[m
 [m
 help:[m
   title: "&6===== BetterLANBroadcaster 帮助 ====="[m
[36m@@ -32,21 +37,33 @@[m [mhelp:[m
   stop: "&e/blb stop &7- 停止局域网广播"[m
   status: "&e/blb status &7- 查看广播状态"[m
   setmotd: "&e/blb setmotd <MOTD> &7- 设置广播 MOTD"[m
[31m-  setdelay: "&e/blb setdelay <毫秒> &7- 设置广播延迟（毫秒）"[m
[31m-  setport: "&e/blb setport <端口|auto> &7- 设置广播端口（使用 auto 自动获取）"[m
[31m-  debug: "&e/blb debug <on|off> &7- 开启/关闭调试模式"[m
[31m-  reload: "&e/blb reload &7- 重载配置文件"[m
[31m-  help: "&e/blb help &7- 显示此帮助"[m
[32m+[m[32m  setdelay: "&e/blb setdelay <毫秒> &7- 设置广播间隔（毫秒）"[m
[32m+[m[32m  setport: "&e/blb setport <端口|auto> &7- 设置广播端口"[m
[32m+[m[32m  setinterface: "&e/blb setinterface <auto|名称|IPv4> &7- 设置网络接口"[m
[32m+[m[32m  debug: "&e/blb debug <on|off> &7- 启用或禁用调试模式"[m
[32m+[m[32m  reload: "&e/blb reload &7- 重新加载配置"[m
[32m+[m[32m  help: "&e/blb help &7- 显示帮助"[m
   version: "&e/blb version &7- 显示插件版本"[m
   footer: "&6==================================="[m
 [m
 version:[m
   line1: "&6BetterLANBroadcaster &ev{0}"[m
[31m-  line2: "&7作者: &f{0}"[m
[31m-  help-hint: "&7输入 &f{0} &7获取帮助"[m
[32m+[m[32m  line2: "&7作者：&f{0}"[m
[32m+[m[32m  help-hint: "&7输入 &f{0} &7查看帮助"[m
 [m
 error:[m
   no-permission: "&c你没有权限使用此命令。"[m
[31m-  invalid-delay: "&c无效的延迟值，请输入一个正数（毫秒）。"[m
[31m-  invalid-port: "&c无效的端口号，请输入 1-65535 之间的数字，或使用 auto。"[m
[31m-  invalid-syntax: "&c无效的语法。用法: &f{0}"[m
[32m+[m[32m  invalid-delay: "&c无效的广播间隔。请输入一个以毫秒为单位的正数。"[m
[32m+[m[32m  invalid-port: "&c无效的端口。请输入 1 到 65535 之间的数字，或使用 'auto'。"[m
[32m+[m[32m  invalid-interface: "&c无效的网络接口。请输入 'auto'、接口名称或 IPv4 地址。"[m
[32m+[m[32m  invalid-syntax: "&c无效的命令格式。用法：&f{0}"[m
[32m+[m[32m  start-failed: "&c无法启动局域网广播。"[m
[32m+[m[32m  reload-failed: "&c无法重新加载 BetterLANBroadcaster。"[m
[32m+[m[32m  configuration-error: "&c配置发生错误。请检查控制台以获取详细信息。"[m
[32m+[m[32m  stop-failed: "&c无法停止局域网广播。"[m
[32m+[m[32m  invalid-motd: "&c无效的 MOTD。请输入非空的 MOTD。"[m
[32m+[m[32m  config-save: "&c无法保存配置。"[m
[32m+[m[32m  interface-start: "&c无法使用所选网络接口重新启动局域网广播。"[m
[32m+[m[32m  invalid-debug: "&c无效的调试选项。请使用 &fon&c 或 &foff&c。"[m
[32m+[m[32m  config-reload: "&c无法重新加载配置。"[m
[32m+[m[32m  reload-broadcast: "&c重新加载配置后无法重新启动局域网广播。"[m
\ No newline at end of file[m
