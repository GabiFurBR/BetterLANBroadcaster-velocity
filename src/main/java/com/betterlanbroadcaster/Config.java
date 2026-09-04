package com.betterlanbroadcaster;

import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

public class Config {

    private final BetterLANBroadcaster plugin;

    private final Path dataDirectory;
    private final Path configFile;

    private ConfigurationNode root;

    public Config(BetterLANBroadcaster plugin, Path dataDirectory) {
        this.plugin = plugin;
        this.dataDirectory = dataDirectory;
        this.configFile = dataDirectory.resolve("config.yml");
    }

    public void load() {
        try {

            // Cria a pasta do plugin caso ela não exista
            Files.createDirectories(dataDirectory);

            // Cria o config.yml apenas na primeira execução
            if (Files.notExists(configFile)) {
                copyDefaultConfig();
            }

            loadFromFile();

            plugin.getLogger().info("Configuração carregada.");

        } catch (IOException e) {
            plugin.getLogger().error(
                    "Não foi possível carregar o config.yml.",
                    e
            );
        }
    }

    private void loadFromFile() throws IOException {

        YamlConfigurationLoader loader =
                YamlConfigurationLoader.builder()
                        .path(configFile)
                        .build();

        root = loader.load();
    }

    private void copyDefaultConfig() throws IOException {

        try (InputStream input =
                     getClass().getResourceAsStream("/config.yml")) {

            if (input == null) {
                throw new IOException(
                        "config.yml não encontrado dentro do JAR."
                );
            }

            Files.copy(input, configFile);
        }
    }

    public void save() {

        if (root == null) {
            plugin.getLogger().warn(
                    "Não é possível salvar o config.yml porque ele não foi carregado."
            );
            return;
        }

        try {

            YamlConfigurationLoader loader =
                    YamlConfigurationLoader.builder()
                            .path(configFile)
                            .build();

            loader.save(root);

        } catch (IOException e) {

            plugin.getLogger().error(
                    "Não foi possível salvar o config.yml.",
                    e
            );
        }
    }

    public void reload() {

        try {

            loadFromFile();

            plugin.getLogger().info(
                    "Configuração recarregada."
            );

        } catch (IOException e) {

            plugin.getLogger().error(
                    "Não foi possível recarregar o config.yml.",
                    e
            );
        }
    }

    public void set(String path, Object value) {

        if (root == null) {
            plugin.getLogger().warn(
                    "Não é possível alterar a configuração porque ela não foi carregada."
            );
            return;
        }

        try {

            root.node((Object[]) path.split("\\.")).set(value);

        } catch (org.spongepowered.configurate.serialize.SerializationException e) {

            plugin.getLogger().error(
                    "Não foi possível alterar a configuração: " + path,
                    e
            );
        }
    }
    public String getLanguage() {
        return root.node("language").getString("en");
    }

    public boolean isDebug() {
        return root.node("debug").getBoolean(false);
    }

    public boolean isBroadcastEnabled() {
        return root.node("broadcast-enabled").getBoolean(false);
    }

    public long getBroadcastDelayMs() {
        return root.node("broadcast-delay-ms").getLong(1500);
    }

    public int getBroadcastPort() {
        return root.node("broadcast-port").getInt(0);
    }

    public String getMotd() {
        return root.node("motd").getString("A Minecraft Server");
    }
    public ConfigurationNode getRoot() {
        return root;
    }

    public Path getDataDirectory() {
        return dataDirectory;
    }

    public Path getConfigFile() {
        return configFile;
    }
}