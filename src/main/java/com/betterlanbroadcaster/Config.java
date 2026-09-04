package com.betterlanbroadcaster;

import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.yaml.NodeStyle;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;
import org.spongepowered.configurate.serialize.SerializationException;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Locale;

/**
 * Handles BetterLANBroadcaster configuration.
 *
 * <p>The configuration is stored in {@code config.yml}.</p>
 *
 * <p>All values are validated before being exposed to the
 * rest of the plugin.</p>
 */
public class Config {

    private static final String CONFIG_RESOURCE = "/config.yml";

    private static final String DEFAULT_LANGUAGE = "en";
    private static final boolean DEFAULT_DEBUG = false;
    private static final boolean DEFAULT_BROADCAST_ENABLED = false;
    private static final long DEFAULT_BROADCAST_DELAY_MS = 1500L;
    private static final int DEFAULT_BROADCAST_PORT = 0;
    private static final String DEFAULT_NETWORK_INTERFACE = "auto";
    private static final String DEFAULT_MOTD = "A Minecraft Server";

    private static final long MIN_DELAY_MS = 50L;
    private static final long MAX_DELAY_MS = 86_400_000L;

    private final BetterLANBroadcaster plugin;
    private final Path dataDirectory;
    private final Path configFile;

    private CommentedConfigurationNode root;

    public Config(BetterLANBroadcaster plugin, Path dataDirectory) {
        this.plugin = plugin;
        this.dataDirectory = dataDirectory;
        this.configFile = dataDirectory.resolve("config.yml");
    }

    /**
     * Loads the configuration.
     *
     * @return true if the configuration was loaded successfully
     */
    public boolean load() {
        try {
            createDataDirectory();
            createDefaultConfigIfNecessary();

            root = createLoader().load();

            if (root == null || root.virtual()) {
                plugin.getLogger().warn(
                        "A configuração carregada está vazia. Usando valores padrão."
                );

                root = createLoader().createNode();

                applyDefaults();

                if (!save()) {
                    return false;
                }

                return true;
            }

            boolean changed = validateAndApplyDefaults();

            if (changed) {
                save();
            }

            plugin.getLogger().debug(
                    "Configuração carregada de {}.",
                    configFile
            );

            return true;

        } catch (IOException e) {
            plugin.getLogger().error(
                    "Não foi possível carregar a configuração em {}.",
                    configFile,
                    e
            );

            return false;
        }
    }

    /**
     * Reloads the configuration from disk.
     *
     * @return true if the configuration was reloaded successfully
     */
    public boolean reload() {
        return load();
    }

    /**
     * Saves the current configuration.
     *
     * @return true if the configuration was saved successfully
     */
    public boolean save() {
        if (root == null) {
            plugin.getLogger().warn(
                    "Não foi possível salvar a configuração porque ela ainda não foi carregada."
            );

            return false;
        }

        try {
            createDataDirectory();

            createLoader().save(root);

            plugin.getLogger().debug(
                    "Configuração salva em {}.",
                    configFile
            );

            return true;

        } catch (IOException e) {
            plugin.getLogger().error(
                    "Não foi possível salvar a configuração em {}.",
                    configFile,
                    e
            );

            return false;
        }
    }

    /**
     * Creates the plugin data directory if necessary.
     */
    private void createDataDirectory() throws IOException {
        Files.createDirectories(dataDirectory);
    }

    /**
     * Creates config.yml from the resource bundled inside the plugin.
     */
    private void createDefaultConfigIfNecessary() throws IOException {
        if (Files.exists(configFile)) {
            return;
        }

        try (InputStream input =
                     Config.class.getResourceAsStream(CONFIG_RESOURCE)) {

            if (input == null) {
                throw new IOException(
                        "O recurso padrão config.yml não foi encontrado dentro do plugin."
                );
            }

            Files.copy(input, configFile);
        }

        plugin.getLogger().info(
                "Configuração padrão criada em {}.",
                configFile
        );
    }

    /**
     * Creates the Configurate YAML loader.
     *
     * <p>BLOCK style keeps the configuration readable and prevents
     * Configurate from serializing the entire file as a flow-style object.</p>
     */
    private YamlConfigurationLoader createLoader() {
        return YamlConfigurationLoader.builder()
                .path(configFile)
                .nodeStyle(NodeStyle.BLOCK)
                .indent(2)
                .build();
    }

    /**
     * Applies all default values to an empty configuration.
     */
    private void applyDefaults() throws SerializationException {
        root.node("language").set(DEFAULT_LANGUAGE);
        root.node("debug").set(DEFAULT_DEBUG);
        root.node("broadcast-enabled").set(DEFAULT_BROADCAST_ENABLED);
        root.node("broadcast-delay-ms").set(DEFAULT_BROADCAST_DELAY_MS);
        root.node("broadcast-port").set(DEFAULT_BROADCAST_PORT);
        root.node("network-interface").set(DEFAULT_NETWORK_INTERFACE);
        root.node("motd").set(DEFAULT_MOTD);
    }

    /**
     * Validates all configuration values and fills missing values.
     *
     * @return true if the configuration was modified
     */
    private boolean validateAndApplyDefaults()
            throws SerializationException {

        boolean changed = false;

        /*
         * ========================================================
         * Language
         * ========================================================
         */

        String language;

        try {
            language = root.node("language").get(String.class);
        } catch (SerializationException e) {
            language = null;
        }

        if (language == null || language.isBlank()) {

            root.node("language").set(DEFAULT_LANGUAGE);
            changed = true;

        } else {

            language = language.trim().toLowerCase(Locale.ROOT);

            if (!language.equals("en")
                    && !language.equals("br")
                    && !language.equals("zh")) {

                plugin.getLogger().warn(
                        "Idioma '{}' não é suportado. Usando '{}'.",
                        language,
                        DEFAULT_LANGUAGE
                );

                root.node("language").set(DEFAULT_LANGUAGE);
                changed = true;

            } else {

                String currentLanguage;

                try {
                    currentLanguage =
                            root.node("language").get(String.class);
                } catch (SerializationException e) {
                    currentLanguage = "";
                }

                if (!language.equals(currentLanguage)) {
                    root.node("language").set(language);
                    changed = true;
                }
            }
        }

        /*
         * ========================================================
         * Debug
         * ========================================================
         */

        if (root.node("debug").virtual()) {
            root.node("debug").set(DEFAULT_DEBUG);
            changed = true;
        }

        /*
         * ========================================================
         * Broadcast enabled
         * ========================================================
         */

        if (root.node("broadcast-enabled").virtual()) {
            root.node("broadcast-enabled")
                    .set(DEFAULT_BROADCAST_ENABLED);

            changed = true;
        }

        /*
         * ========================================================
         * Broadcast delay
         * ========================================================
         */

        Long delay;

        try {
            delay = root.node("broadcast-delay-ms")
                    .get(Long.class);
        } catch (SerializationException e) {
            delay = null;
        }

        if (delay == null) {

            plugin.getLogger().warn(
                    "broadcast-delay-ms inválido ou ausente. Usando {} ms.",
                    DEFAULT_BROADCAST_DELAY_MS
            );

            root.node("broadcast-delay-ms")
                    .set(DEFAULT_BROADCAST_DELAY_MS);

            changed = true;

        } else if (delay < MIN_DELAY_MS) {

            plugin.getLogger().warn(
                    "broadcast-delay-ms={} está abaixo do mínimo de {} ms. Ajustando para {} ms.",
                    delay,
                    MIN_DELAY_MS,
                    MIN_DELAY_MS
            );

            root.node("broadcast-delay-ms")
                    .set(MIN_DELAY_MS);

            changed = true;

        } else if (delay > MAX_DELAY_MS) {

            plugin.getLogger().warn(
                    "broadcast-delay-ms={} excede o máximo de {} ms. Ajustando para {} ms.",
                    delay,
                    MAX_DELAY_MS,
                    MAX_DELAY_MS
            );

            root.node("broadcast-delay-ms")
                    .set(MAX_DELAY_MS);

            changed = true;
        }

        /*
         * ========================================================
         * Broadcast port
         * ========================================================
         *
         * 0 = automatic port detection
         */

        Integer port;

        try {
            port = root.node("broadcast-port")
                    .get(Integer.class);
        } catch (SerializationException e) {
            port = null;
        }

        if (port == null) {

            plugin.getLogger().warn(
                    "broadcast-port inválida ou ausente. Usando 0 (porta automática)."
            );

            root.node("broadcast-port")
                    .set(DEFAULT_BROADCAST_PORT);

            changed = true;

        } else if (port < 0 || port > 65535) {

            plugin.getLogger().warn(
                    "broadcast-port={} está fora do intervalo válido. Usando 0.",
                    port
            );

            root.node("broadcast-port")
                    .set(DEFAULT_BROADCAST_PORT);

            changed = true;
        }

        /*
         * ========================================================
         * Network interface
         * ========================================================
         *
         * Supported values:
         *
         * auto
         * interface name
         * IPv4 address
         */

        String networkInterface;

        try {
            networkInterface =
                    root.node("network-interface")
                            .get(String.class);
        } catch (SerializationException e) {
            networkInterface = null;
        }

        if (networkInterface == null
                || networkInterface.isBlank()) {

            plugin.getLogger().warn(
                    "network-interface está ausente. Usando 'auto'."
            );

            root.node("network-interface")
                    .set(DEFAULT_NETWORK_INTERFACE);

            changed = true;

        } else {

            networkInterface = networkInterface.trim();

            String currentInterface;

            try {
                currentInterface =
                        root.node("network-interface")
                                .get(String.class);
            } catch (SerializationException e) {
                currentInterface = "";
            }

            if (!networkInterface.equals(currentInterface)) {

                root.node("network-interface")
                        .set(networkInterface);

                changed = true;
            }
        }

        /*
         * ========================================================
         * MOTD
         * ========================================================
         */

        String motd;

        try {
            motd = root.node("motd").get(String.class);
        } catch (SerializationException e) {
            motd = null;
        }

        if (motd == null) {

            plugin.getLogger().warn(
                    "motd está ausente. Usando o MOTD padrão."
            );

            root.node("motd").set(DEFAULT_MOTD);

            changed = true;
        }

        return changed;
    }

    /*
     * ============================================================
     * Getters
     * ============================================================
     */

    public String getLanguage() {

        if (root == null) {
            return DEFAULT_LANGUAGE;
        }

        try {

            String language =
                    root.node("language")
                            .get(String.class);

            if (language == null || language.isBlank()) {
                return DEFAULT_LANGUAGE;
            }

            return language.trim().toLowerCase(Locale.ROOT);

        } catch (SerializationException e) {

            return DEFAULT_LANGUAGE;
        }
    }

    public boolean isDebug() {

        if (root == null) {
            return DEFAULT_DEBUG;
        }

        return root.node("debug")
                .getBoolean(DEFAULT_DEBUG);
    }

    public boolean isBroadcastEnabled() {

        if (root == null) {
            return DEFAULT_BROADCAST_ENABLED;
        }

        return root.node("broadcast-enabled")
                .getBoolean(DEFAULT_BROADCAST_ENABLED);
    }

    public long getBroadcastDelayMs() {

        if (root == null) {
            return DEFAULT_BROADCAST_DELAY_MS;
        }

        try {

            Long delay =
                    root.node("broadcast-delay-ms")
                            .get(Long.class);

            if (delay == null) {
                return DEFAULT_BROADCAST_DELAY_MS;
            }

            return Math.max(
                    MIN_DELAY_MS,
                    Math.min(MAX_DELAY_MS, delay)
            );

        } catch (SerializationException e) {

            plugin.getLogger().warn(
                    "Não foi possível ler broadcast-delay-ms. Usando {} ms.",
                    DEFAULT_BROADCAST_DELAY_MS
            );

            return DEFAULT_BROADCAST_DELAY_MS;
        }
    }

    public int getBroadcastPort() {

        if (root == null) {
            return DEFAULT_BROADCAST_PORT;
        }

        try {

            Integer port =
                    root.node("broadcast-port")
                            .get(Integer.class);

            if (port == null) {
                return DEFAULT_BROADCAST_PORT;
            }

            if (port < 0 || port > 65535) {
                return DEFAULT_BROADCAST_PORT;
            }

            return port;

        } catch (SerializationException e) {

            plugin.getLogger().warn(
                    "Não foi possível ler broadcast-port. Usando 0."
            );

            return DEFAULT_BROADCAST_PORT;
        }
    }

    public String getNetworkInterface() {

        if (root == null) {
            return DEFAULT_NETWORK_INTERFACE;
        }

        try {

            String networkInterface =
                    root.node("network-interface")
                            .get(String.class);

            if (networkInterface == null
                    || networkInterface.isBlank()) {

                return DEFAULT_NETWORK_INTERFACE;
            }

            return networkInterface.trim();

        } catch (SerializationException e) {

            return DEFAULT_NETWORK_INTERFACE;
        }
    }

    public String getMotd() {

        if (root == null) {
            return DEFAULT_MOTD;
        }

        try {

            String motd =
                    root.node("motd")
                            .get(String.class);

            if (motd == null) {
                return DEFAULT_MOTD;
            }

            return motd;

        } catch (SerializationException e) {

            return DEFAULT_MOTD;
        }
    }

    /*
     * ============================================================
     * Generic Setter
     * ============================================================
     */

    /**
     * Sets a configuration value and saves the configuration.
     *
     * <p>This method exists for compatibility with CommandHandler.</p>
     *
     * @param path configuration path
     * @param value new value
     * @return true if the value was saved successfully
     */
    public boolean set(String path, Object value) {

        if (root == null) {

            plugin.getLogger().warn(
                    "Não foi possível alterar '{}' porque a configuração ainda não foi carregada.",
                    path
            );

            return false;
        }

        if (path == null || path.isBlank()) {
            return false;
        }

        try {

            String[] parts = path.split("\\.");

            root.node((Object[]) parts).set(value);

            return save();

        } catch (SerializationException e) {

            plugin.getLogger().error(
                    "Não foi possível alterar a configuração '{}'.",
                    path,
                    e
            );

            return false;
        }
    }

    /*
     * ============================================================
     * Setters
     * ============================================================
     */

    public boolean setLanguage(String language) {

        if (root == null) {
            return false;
        }

        if (language == null || language.isBlank()) {
            return false;
        }

        language = language.trim().toLowerCase(Locale.ROOT);

        if (!language.equals("en")
                && !language.equals("br")
                && !language.equals("zh")) {
            return false;
        }

        try {

            root.node("language").set(language);

            return save();

        } catch (SerializationException e) {

            plugin.getLogger().error(
                    "Não foi possível alterar o idioma.",
                    e
            );

            return false;
        }
    }

    public boolean setDebug(boolean debug) {

        if (root == null) {
            return false;
        }

        try {

            root.node("debug").set(debug);

            return save();

        } catch (SerializationException e) {

            plugin.getLogger().error(
                    "Não foi possível alterar debug.",
                    e
            );

            return false;
        }
    }

    public boolean setBroadcastEnabled(boolean enabled) {

        if (root == null) {
            return false;
        }

        try {

            root.node("broadcast-enabled")
                    .set(enabled);

            return save();

        } catch (SerializationException e) {

            plugin.getLogger().error(
                    "Não foi possível alterar broadcast-enabled.",
                    e
            );

            return false;
        }
    }

    public boolean setBroadcastDelayMs(long delayMs) {

        if (root == null) {
            return false;
        }

        if (delayMs < MIN_DELAY_MS
                || delayMs > MAX_DELAY_MS) {

            return false;
        }

        try {

            root.node("broadcast-delay-ms")
                    .set(delayMs);

            return save();

        } catch (SerializationException e) {

            plugin.getLogger().error(
                    "Não foi possível alterar broadcast-delay-ms.",
                    e
            );

            return false;
        }
    }

    public boolean setBroadcastPort(int port) {

        if (root == null) {
            return false;
        }

        if (port < 0 || port > 65535) {
            return false;
        }

        try {

            root.node("broadcast-port")
                    .set(port);

            return save();

        } catch (SerializationException e) {

            plugin.getLogger().error(
                    "Não foi possível alterar broadcast-port.",
                    e
            );

            return false;
        }
    }

    public boolean setNetworkInterface(String networkInterface) {

        if (root == null) {
            return false;
        }

        if (networkInterface == null
                || networkInterface.isBlank()) {

            return false;
        }

        networkInterface = networkInterface.trim();

        try {

            root.node("network-interface")
                    .set(networkInterface);

            return save();

        } catch (SerializationException e) {

            plugin.getLogger().error(
                    "Não foi possível alterar network-interface.",
                    e
            );

            return false;
        }
    }

    public boolean setMotd(String motd) {

        if (root == null) {
            return false;
        }

        if (motd == null) {
            return false;
        }

        try {

            root.node("motd").set(motd);

            return save();

        } catch (SerializationException e) {

            plugin.getLogger().error(
                    "Não foi possível alterar o MOTD.",
                    e
            );

            return false;
        }
    }

    /*
     * ============================================================
     * Utility
     * ============================================================
     */

    public Path getConfigFile() {
        return configFile;
    }

    public Path getDataDirectory() {
        return dataDirectory;
    }

    public CommentedConfigurationNode getRoot() {
        return root;
    }
}