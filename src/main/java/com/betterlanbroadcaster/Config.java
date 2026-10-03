package com.betterlanbroadcaster;

import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.yaml.NodeStyle;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public final class Config {

    private static final String CONFIG_RESOURCE = "/config.yml";

    private static final String DEFAULT_LANGUAGE = "en";
    private static final boolean DEFAULT_DEBUG = false;
    private static final boolean DEFAULT_BROADCAST_ENABLED = false;
    private static final long DEFAULT_BROADCAST_DELAY_MS = 1500L;
    private static final int DEFAULT_BROADCAST_PORT = 0;
    private static final String DEFAULT_NETWORK_INTERFACE = "auto";
    private static final String DEFAULT_MOTD = "A Minecraft Server";
    private static final List<String> DEFAULT_NETWORK_INTERFACE_EXCLUDES = List.of();

    private static final long MIN_DELAY_MS = 50L;
    private static final long MAX_DELAY_MS = 86_400_000L;

    private static final Set<String> SUPPORTED_LANGUAGES = Set.of(
            "en", "br", "zh", "es", "de", "fr", "ja", "ru", "it", "ko", "nl", "pl", "tr", "uk", "vi", "id", "cs", "sv"
    );

    private final BetterLANBroadcaster plugin;
    private final Path dataDirectory;
    private final Path configFile;

    private volatile CommentedConfigurationNode root;
    private volatile Settings settings = Settings.defaults();

    public Config(BetterLANBroadcaster plugin, Path dataDirectory) {
        this.plugin = plugin;
        this.dataDirectory = dataDirectory;
        this.configFile = dataDirectory.resolve("config.yml");
    }

    public synchronized boolean load() {
        try {
            Files.createDirectories(dataDirectory);
            createDefaultConfigIfNecessary();

            CommentedConfigurationNode loaded = createLoader().load();
            if (loaded.virtual()) {
                applyDefaults(loaded);
                if (!saveNode(loaded)) {
                    return false;
                }
            } else if (validateAndApplyDefaults(loaded) && !saveNode(loaded)) {
                return false;
            }

            root = loaded;
            settings = readSettings(loaded);
            plugin.getLogger().debug("Configuration loaded from {}.", configFile);
            return true;
        } catch (IOException | RuntimeException exception) {
            plugin.getLogger().error("Could not load configuration from {}.", configFile, exception);
            return false;
        }
    }

    public synchronized boolean reload() {
        return load();
    }

    public synchronized boolean save() {
        CommentedConfigurationNode current = root;
        if (current == null) {
            plugin.getLogger().warn("Could not save configuration before it was loaded.");
            return false;
        }
        return saveNode(current);
    }

    private boolean saveNode(CommentedConfigurationNode node) {
        try {
            Files.createDirectories(dataDirectory);
            createLoader().save(node);
            plugin.getLogger().debug("Configuration saved to {}.", configFile);
            return true;
        } catch (IOException exception) {
            plugin.getLogger().error("Could not save configuration to {}.", configFile, exception);
            return false;
        }
    }

    private void createDefaultConfigIfNecessary() throws IOException {
        if (Files.exists(configFile)) {
            return;
        }

        try (InputStream input = Config.class.getResourceAsStream(CONFIG_RESOURCE)) {
            if (input == null) {
                throw new IOException("Bundled config.yml was not found.");
            }
            Files.copy(input, configFile);
        }
        plugin.getLogger().info("Created default configuration at {}.", configFile);
    }

    private YamlConfigurationLoader createLoader() {
        return YamlConfigurationLoader.builder()
                .path(configFile)
                .nodeStyle(NodeStyle.BLOCK)
                .indent(2)
                .build();
    }

    private void applyDefaults(CommentedConfigurationNode target) throws SerializationException {
        target.node("language").set(DEFAULT_LANGUAGE);
        target.node("debug").set(DEFAULT_DEBUG);
        target.node("broadcast-enabled").set(DEFAULT_BROADCAST_ENABLED);
        target.node("broadcast-delay-ms").set(DEFAULT_BROADCAST_DELAY_MS);
        target.node("broadcast-port").set(DEFAULT_BROADCAST_PORT);
        target.node("network-interface").set(DEFAULT_NETWORK_INTERFACE);
        target.node("network-interface-exclude").set(DEFAULT_NETWORK_INTERFACE_EXCLUDES);
        target.node("motd").set(DEFAULT_MOTD);
    }

    private boolean validateAndApplyDefaults(CommentedConfigurationNode target) throws SerializationException {
        boolean changed = false;

        String language = target.node("language").getString();
        String normalizedLanguage = normalizeLanguage(language);
        if (!SUPPORTED_LANGUAGES.contains(normalizedLanguage)) {
            normalizedLanguage = DEFAULT_LANGUAGE;
            plugin.getLogger().warn("Unsupported language '{}'. Falling back to '{}'.", language, DEFAULT_LANGUAGE);
        }
        if (!normalizedLanguage.equals(language)) {
            target.node("language").set(normalizedLanguage);
            changed = true;
        }

        Boolean debug = readBoolean(target.node("debug"));
        if (debug == null) {
            target.node("debug").set(DEFAULT_DEBUG);
            changed = true;
        }

        Boolean broadcastEnabled = readBoolean(target.node("broadcast-enabled"));
        if (broadcastEnabled == null) {
            target.node("broadcast-enabled").set(DEFAULT_BROADCAST_ENABLED);
            changed = true;
        }

        Long delay = readLong(target.node("broadcast-delay-ms"));
        if (delay == null) {
            plugin.getLogger().warn("broadcast-delay-ms is invalid or missing. Using {} ms.", DEFAULT_BROADCAST_DELAY_MS);
            target.node("broadcast-delay-ms").set(DEFAULT_BROADCAST_DELAY_MS);
            changed = true;
        } else if (delay < MIN_DELAY_MS) {
            plugin.getLogger().warn("broadcast-delay-ms={} is below the minimum of {} ms. Adjusting to {} ms.", delay, MIN_DELAY_MS, MIN_DELAY_MS);
            target.node("broadcast-delay-ms").set(MIN_DELAY_MS);
            changed = true;
        } else if (delay > MAX_DELAY_MS) {
            plugin.getLogger().warn("broadcast-delay-ms={} exceeds the maximum of {} ms. Adjusting to {} ms.", delay, MAX_DELAY_MS, MAX_DELAY_MS);
            target.node("broadcast-delay-ms").set(MAX_DELAY_MS);
            changed = true;
        }

        Integer port = readInt(target.node("broadcast-port"));
        if (port == null || port < 0 || port > 65535) {
            plugin.getLogger().warn("broadcast-port is invalid or out of range. Using 0 (auto).");
            target.node("broadcast-port").set(DEFAULT_BROADCAST_PORT);
            changed = true;
        }

        String networkInterface = target.node("network-interface").getString();
        String normalizedInterface = networkInterface == null ? "" : networkInterface.trim();
        if (normalizedInterface.isBlank()) {
            normalizedInterface = DEFAULT_NETWORK_INTERFACE;
        }
        if (!normalizedInterface.equals(networkInterface)) {
            target.node("network-interface").set(normalizedInterface);
            changed = true;
        }

        List<String> excludes = readStringList(target.node("network-interface-exclude"));
        List<String> normalizedExcludes = normalizeExcludes(excludes);
        if (!normalizedExcludes.equals(excludes)) {
            target.node("network-interface-exclude").set(normalizedExcludes);
            changed = true;
        }

        String motd = target.node("motd").getString();
        if (motd == null || motd.isBlank()) {
            plugin.getLogger().warn("motd is missing or blank. Using the default MOTD.");
            target.node("motd").set(DEFAULT_MOTD);
            changed = true;
        }

        return changed;
    }

    private Settings readSettings(CommentedConfigurationNode source) {
        String language = normalizeLanguage(source.node("language").getString());
        if (!SUPPORTED_LANGUAGES.contains(language)) {
            language = DEFAULT_LANGUAGE;
        }

        Boolean debug = readBoolean(source.node("debug"));
        Boolean broadcastEnabled = readBoolean(source.node("broadcast-enabled"));
        Long delay = readLong(source.node("broadcast-delay-ms"));
        Integer port = readInt(source.node("broadcast-port"));

        String networkInterface = source.node("network-interface").getString();
        if (networkInterface == null || networkInterface.isBlank()) {
            networkInterface = DEFAULT_NETWORK_INTERFACE;
        } else {
            networkInterface = networkInterface.trim();
        }

        String motd = source.node("motd").getString();
        if (motd == null || motd.isBlank()) {
            motd = DEFAULT_MOTD;
        }

        return new Settings(
                language,
                debug != null ? debug : DEFAULT_DEBUG,
                broadcastEnabled != null ? broadcastEnabled : DEFAULT_BROADCAST_ENABLED,
                delay != null ? clampDelay(delay) : DEFAULT_BROADCAST_DELAY_MS,
                port != null && port >= 0 && port <= 65535 ? port : DEFAULT_BROADCAST_PORT,
                networkInterface,
                motd,
                normalizeExcludes(readStringList(source.node("network-interface-exclude")))
        );
    }

    private Boolean readBoolean(CommentedConfigurationNode node) {
        try {
            return node.get(Boolean.class);
        } catch (SerializationException exception) {
            return null;
        }
    }

    private Long readLong(CommentedConfigurationNode node) {
        try {
            return node.get(Long.class);
        } catch (SerializationException exception) {
            return null;
        }
    }

    private Integer readInt(CommentedConfigurationNode node) {
        try {
            return node.get(Integer.class);
        } catch (SerializationException exception) {
            return null;
        }
    }

    private List<String> readStringList(CommentedConfigurationNode node) {
        try {
            List<String> values = node.getList(String.class, List.of());
            return values == null ? List.of() : values;
        } catch (SerializationException exception) {
            return List.of();
        }
    }

    private long clampDelay(long value) {
        return Math.max(MIN_DELAY_MS, Math.min(MAX_DELAY_MS, value));
    }

    private String normalizeLanguage(String language) {
        if (language == null || language.isBlank()) {
            return DEFAULT_LANGUAGE;
        }
        return language.trim().toLowerCase(Locale.ROOT);
    }

    private List<String> normalizeExcludes(List<String> values) {
        if (values == null || values.isEmpty()) {
            return List.of();
        }

        LinkedHashSet<String> normalized = new LinkedHashSet<>();
        for (String value : values) {
            if (value == null) {
                continue;
            }
            String trimmed = value.trim();
            if (!trimmed.isBlank()) {
                normalized.add(trimmed);
            }
        }
        return List.copyOf(normalized);
    }

    public synchronized boolean set(String path, Object value) {
        if (path == null || path.isBlank() || root == null) {
            return false;
        }

        try {
            CommentedConfigurationNode candidate = root.copy();
            candidate.node((Object[]) path.split("\\.")).set(value);
            if (!saveNode(candidate)) {
                return false;
            }

            root = candidate;
            settings = readSettings(candidate);
            return true;
        } catch (SerializationException exception) {
            plugin.getLogger().error("Could not change configuration path '{}'.", path, exception);
            return false;
        }
    }

    public synchronized boolean setLanguage(String language) {
        if (language == null || language.isBlank()) {
            return false;
        }

        String normalized = language.trim().toLowerCase(Locale.ROOT);
        if (!SUPPORTED_LANGUAGES.contains(normalized)) {
            return false;
        }
        return set("language", normalized);
    }

    public boolean setDebug(boolean debug) {
        return set("debug", debug);
    }

    public boolean setBroadcastEnabled(boolean enabled) {
        return set("broadcast-enabled", enabled);
    }

    public boolean setBroadcastDelayMs(long delayMs) {
        if (delayMs < MIN_DELAY_MS || delayMs > MAX_DELAY_MS) {
            return false;
        }
        return set("broadcast-delay-ms", delayMs);
    }

    public boolean setBroadcastPort(int port) {
        if (port < 0 || port > 65535) {
            return false;
        }
        return set("broadcast-port", port);
    }

    public boolean setNetworkInterface(String networkInterface) {
        if (networkInterface == null || networkInterface.isBlank()) {
            return false;
        }
        return set("network-interface", networkInterface.trim());
    }

    public boolean setNetworkInterfaceExcludes(List<String> excludes) {
        return set("network-interface-exclude", normalizeExcludes(excludes));
    }

    public boolean setMotd(String motd) {
        if (motd == null || motd.isBlank()) {
            return false;
        }
        return set("motd", motd);
    }

    public String getLanguage() {
        return settings.language();
    }

    public boolean isDebug() {
        return settings.debug();
    }

    public boolean isBroadcastEnabled() {
        return settings.broadcastEnabled();
    }

    public long getBroadcastDelayMs() {
        return settings.broadcastDelayMs();
    }

    public int getBroadcastPort() {
        return settings.broadcastPort();
    }

    public String getNetworkInterface() {
        return settings.networkInterface();
    }

    public List<String> getNetworkInterfaceExcludes() {
        return settings.networkInterfaceExcludes();
    }

    public String getMotd() {
        return settings.motd();
    }

    public Settings snapshot() {
        return settings;
    }

    public Path getConfigFile() {
        return configFile;
    }

    public Path getDataDirectory() {
        return dataDirectory;
    }

    public synchronized CommentedConfigurationNode getRoot() {
        return root == null ? null : root.copy();
    }

    public record Settings(
            String language,
            boolean debug,
            boolean broadcastEnabled,
            long broadcastDelayMs,
            int broadcastPort,
            String networkInterface,
            String motd,
            List<String> networkInterfaceExcludes
    ) {
        private static Settings defaults() {
            return new Settings(
                    DEFAULT_LANGUAGE,
                    DEFAULT_DEBUG,
                    DEFAULT_BROADCAST_ENABLED,
                    DEFAULT_BROADCAST_DELAY_MS,
                    DEFAULT_BROADCAST_PORT,
                    DEFAULT_NETWORK_INTERFACE,
                    DEFAULT_MOTD,
                    DEFAULT_NETWORK_INTERFACE_EXCLUDES
            );
        }

        public Settings {
            networkInterfaceExcludes = networkInterfaceExcludes == null
                    ? List.of()
                    : List.copyOf(new ArrayList<>(networkInterfaceExcludes));
        }
    }
}
