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

    public boolean load() {
        try {
            createDataDirectory();
            createDefaultConfigIfNecessary();

            root = createLoader().load();

            if (root == null || root.virtual()) {
                plugin.getLogger().warn("A configuração carregada está vazia. Usando valores padrão.");
                root = createLoader().createNode();
                applyDefaults();
                return save();
            }

            boolean changed = validateAndApplyDefaults();
            if (changed) {
                save();
            }

            plugin.getLogger().debug("Configuração carregada de {}.", configFile);
            return true;
        } catch (IOException e) {
            plugin.getLogger().error("Não foi possível carregar a configuração em {}.", configFile, e);
            return false;
        }
    }

    public boolean reload() {
        return load();
    }

    public boolean save() {
        if (root == null) {
            plugin.getLogger().warn("Não foi possível salvar a configuração porque ela ainda não foi carregada.");
            return false;
        }

        try {
            createDataDirectory();
            createLoader().save(root);
            plugin.getLogger().debug("Configuração salva em {}.", configFile);
            return true;
        } catch (IOException e) {
            plugin.getLogger().error("Não foi possível salvar a configuração em {}.", configFile, e);
            return false;
        }
    }

    private void createDataDirectory() throws IOException {
        Files.createDirectories(dataDirectory);
    }

    private void createDefaultConfigIfNecessary() throws IOException {
        if (Files.exists(configFile)) {
            return;
        }

        try (InputStream input = Config.class.getResourceAsStream(CONFIG_RESOURCE)) {
            if (input == null) {
                throw new IOException("O recurso padrão config.yml não foi encontrado dentro do plugin.");
            }
            Files.copy(input, configFile);
        }

        plugin.getLogger().info("Configuração padrão criada em {}.", configFile);
    }

    private YamlConfigurationLoader createLoader() {
        return YamlConfigurationLoader.builder()
                .path(configFile)
                .nodeStyle(NodeStyle.BLOCK)
                .indent(2)
                .build();
    }

    private void applyDefaults() throws SerializationException {
        root.node("language").set(DEFAULT_LANGUAGE);
        root.node("debug").set(DEFAULT_DEBUG);
        root.node("broadcast-enabled").set(DEFAULT_BROADCAST_ENABLED);
        root.node("broadcast-delay-ms").set(DEFAULT_BROADCAST_DELAY_MS);
        root.node("broadcast-port").set(DEFAULT_BROADCAST_PORT);
        root.node("network-interface").set(DEFAULT_NETWORK_INTERFACE);
        root.node("motd").set(DEFAULT_MOTD);
    }

    private boolean validateAndApplyDefaults() throws SerializationException {
        boolean changed = false;
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
            if (!language.equals("en") && !language.equals("br") && !language.equals("zh") && !language.equals("es") && !language.equals("de") && !language.equals("fr") && !language.equals("ja") && !language.equals("ru")) {
                    plugin.getLogger().warn("Language '{}' is not supported. Falling back to '{}'.", language, DEFAULT_LANGUAGE);
                    root.node("language").set(DEFAULT_LANGUAGE);
                    changed = true;
            } else {
                String currentLanguage;
                try {
                    currentLanguage = root.node("language").get(String.class);
                } catch (SerializationException e) {
                    currentLanguage = "";
                }

                if (!language.equals(currentLanguage)) {
                    root.node("language").set(language);
                    changed = true;
                }
            }
        }

        if (root.node("debug").virtual()) {
            root.node("debug").set(DEFAULT_DEBUG);
            changed = true;
        }

        if (root.node("broadcast-enabled").virtual()) {
            root.node("broadcast-enabled").set(DEFAULT_BROADCAST_ENABLED);
            changed = true;
        }

        Long delay;
        try {
            delay = root.node("broadcast-delay-ms").get(Long.class);
        } catch (SerializationException e) {
            delay = null;
        }

        if (delay == null) {
            plugin.getLogger().warn("broadcast-delay-ms inválido ou ausente. Usando {} ms.", DEFAULT_BROADCAST_DELAY_MS);
            root.node("broadcast-delay-ms").set(DEFAULT_BROADCAST_DELAY_MS);
            changed = true;
        } else if (delay < MIN_DELAY_MS) {
            plugin.getLogger().warn("broadcast-delay-ms={} está abaixo do mínimo de {} ms. Ajustando para {} ms.", delay, MIN_DELAY_MS, MIN_DELAY_MS);
            root.node("broadcast-delay-ms").set(MIN_DELAY_MS);
            changed = true;
        } else if (delay > MAX_DELAY_MS) {
            plugin.getLogger().warn("broadcast-delay-ms={} excede o máximo de {} ms. Ajustando para {} ms.", delay, MAX_DELAY_MS, MAX_DELAY_MS);
            root.node("broadcast-delay-ms").set(MAX_DELAY_MS);
            changed = true;
        }

        Integer port;
        try {
            port = root.node("broadcast-port").get(Integer.class);
        } catch (SerializationException e) {
            port = null;
        }

        if (port == null || port < 0 || port > 65535) {
            plugin.getLogger().warn("broadcast-port inválida ou fora do intervalo. Usando 0.");
            root.node("broadcast-port").set(DEFAULT_BROADCAST_PORT);
            changed = true;
        }

        String networkInterface;
        try {
            networkInterface = root.node("network-interface").get(String.class);
        } catch (SerializationException e) {
            networkInterface = null;
        }

        if (networkInterface == null || networkInterface.isBlank()) {
            plugin.getLogger().warn("network-interface está ausente. Usando 'auto'.");
            root.node("network-interface").set(DEFAULT_NETWORK_INTERFACE);
            changed = true;
        } else {
            networkInterface = networkInterface.trim();
            String currentInterface;
            try {
                currentInterface = root.node("network-interface").get(String.class);
            } catch (SerializationException e) {
                currentInterface = "";
            }

            if (!networkInterface.equals(currentInterface)) {
                root.node("network-interface").set(networkInterface);
                changed = true;
            }
        }

        String motd;
        try {
            motd = root.node("motd").get(String.class);
        } catch (SerializationException e) {
            motd = null;
        }

        if (motd == null) {
            plugin.getLogger().warn("motd está ausente. Usando o MOTD padrão.");
            root.node("motd").set(DEFAULT_MOTD);
            changed = true;
        }

        return changed;
    }

    public String getLanguage() {
        if (root == null) return DEFAULT_LANGUAGE;
        try {
            String language = root.node("language").get(String.class);
            return (language == null || language.isBlank()) ? DEFAULT_LANGUAGE : language.trim().toLowerCase(Locale.ROOT);
        } catch (SerializationException e) {
            return DEFAULT_LANGUAGE;
        }
    }

    public boolean isDebug() {
        return root != null && root.node("debug").getBoolean(DEFAULT_DEBUG);
    }

    public boolean isBroadcastEnabled() {
        return root != null && root.node("broadcast-enabled").getBoolean(DEFAULT_BROADCAST_ENABLED);
    }

    public long getBroadcastDelayMs() {
        if (root == null) return DEFAULT_BROADCAST_DELAY_MS;
        try {
            Long delay = root.node("broadcast-delay-ms").get(Long.class);
            if (delay == null) return DEFAULT_BROADCAST_DELAY_MS;
            return Math.max(MIN_DELAY_MS, Math.min(MAX_DELAY_MS, delay));
        } catch (SerializationException e) {
            plugin.getLogger().warn("Não foi possível ler broadcast-delay-ms. Usando {} ms.", DEFAULT_BROADCAST_DELAY_MS);
            return DEFAULT_BROADCAST_DELAY_MS;
        }
    }

    public int getBroadcastPort() {
        if (root == null) return DEFAULT_BROADCAST_PORT;
        try {
            Integer port = root.node("broadcast-port").get(Integer.class);
            if (port == null || port < 0 || port > 65535) return DEFAULT_BROADCAST_PORT;
            return port;
        } catch (SerializationException e) {
            plugin.getLogger().warn("Não foi possível ler broadcast-port. Usando 0.");
            return DEFAULT_BROADCAST_PORT;
        }
    }

    public String getNetworkInterface() {
        if (root == null) return DEFAULT_NETWORK_INTERFACE;
        try {
            String networkInterface = root.node("network-interface").get(String.class);
            return (networkInterface == null || networkInterface.isBlank()) ? DEFAULT_NETWORK_INTERFACE : networkInterface.trim();
        } catch (SerializationException e) {
            return DEFAULT_NETWORK_INTERFACE;
        }
    }

    public String getMotd() {
        if (root == null) return DEFAULT_MOTD;
        try {
            String motd = root.node("motd").get(String.class);
            return motd == null ? DEFAULT_MOTD : motd;
        } catch (SerializationException e) {
            return DEFAULT_MOTD;
        }
    }

    public boolean set(String path, Object value) {
        if (root == null) {
            plugin.getLogger().warn("Não foi possível alterar '{}' porque a configuração ainda não foi carregada.", path);
            return false;
        }
        if (path == null || path.isBlank()) return false;

        try {
            root.node((Object[]) path.split("\\.")).set(value);
            return save();
        } catch (SerializationException e) {
            plugin.getLogger().error("Não foi possível alterar a configuração '{}'.", path, e);
            return false;
        }
    }

    public boolean setLanguage(String language) {
        if (root == null || language == null || language.isBlank()) return false;
        language = language.trim().toLowerCase(Locale.ROOT);
        if (!language.equals("en") && !language.equals("br") && !language.equals("zh")) return false;

        try {
            root.node("language").set(language);
            return save();
        } catch (SerializationException e) {
            plugin.getLogger().error("Não foi possível alterar o idioma.", e);
            return false;
        }
    }

    public boolean setDebug(boolean debug) {
        return updateNode("debug", debug, "debug");
    }

    public boolean setBroadcastEnabled(boolean enabled) {
        return updateNode("broadcast-enabled", enabled, "broadcast-enabled");
    }

    public boolean setBroadcastDelayMs(long delayMs) {
        if (delayMs < MIN_DELAY_MS || delayMs > MAX_DELAY_MS) return false;
        return updateNode("broadcast-delay-ms", delayMs, "broadcast-delay-ms");
    }

    public boolean setBroadcastPort(int port) {
        if (port < 0 || port > 65535) return false;
        return updateNode("broadcast-port", port, "broadcast-port");
    }

    public boolean setNetworkInterface(String networkInterface) {
        if (networkInterface == null || networkInterface.isBlank()) return false;
        return updateNode("network-interface", networkInterface.trim(), "network-interface");
    }

    public boolean setMotd(String motd) {
        if (motd == null) return false;
        return updateNode("motd", motd, "MOTD");
    }

    private boolean updateNode(String path, Object value, String label) {
        if (root == null) return false;
        try {
            root.node(path).set(value);
            return save();
        } catch (SerializationException e) {
            plugin.getLogger().error("Não foi possível alterar {}.", label, e);
            return false;
        }
    }

    public Path getConfigFile() { return configFile; }
    public Path getDataDirectory() { return dataDirectory; }
    public CommentedConfigurationNode getRoot() { return root; }
}