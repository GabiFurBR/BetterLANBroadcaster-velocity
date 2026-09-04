package com.betterlanbroadcaster;

import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class Language {

    private final BetterLANBroadcaster plugin;

    private ConfigurationNode root;
    private String currentLanguage;

    public Language(BetterLANBroadcaster plugin) {
        this.plugin = plugin;
    }

    public void load(String language) {

        if (language == null || language.isBlank()) {
            language = "en";
        }

        language = language.toLowerCase();

        String resourcePath = "/lang/messages_" + language + ".yml";

        try {
            InputStream input = getClass().getResourceAsStream(resourcePath);

            // Idioma não encontrado → usa inglês
            if (input == null) {

                plugin.getLogger().warn(
                        "Idioma '" + language + "' não encontrado. Usando en."
                );

                language = "en";
                resourcePath = "/lang/messages_en.yml";

                input = getClass().getResourceAsStream(resourcePath);
            }

            if (input == null) {
                throw new IOException(
                        "messages_en.yml não encontrado dentro do JAR."
                );
            }

            try (BufferedReader reader =
            new BufferedReader(
                new InputStreamReader(input, StandardCharsets.UTF_8))) {
                YamlConfigurationLoader loader =
                        YamlConfigurationLoader.builder()
                                .source(() -> reader)
                                .build();

                root = loader.load();
            }

            currentLanguage = language;

            plugin.getLogger().info(
                    "Idioma carregado: " + currentLanguage
            );

        } catch (IOException e) {

            plugin.getLogger().error(
                    "Não foi possível carregar o idioma: " + language,
                    e
            );
        }
    }

    public String get(String path) {

        if (root == null) {
            return path;
        }

        return root.node((Object[]) path.split("\\."))
                .getString(path);
    }

    public String getLanguage() {
        return currentLanguage;
    }

    public ConfigurationNode getRoot() {
        return root;
    }
}