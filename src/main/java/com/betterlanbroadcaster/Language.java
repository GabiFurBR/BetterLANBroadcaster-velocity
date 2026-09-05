package com.betterlanbroadcaster;

import org.slf4j.Logger;
import org.yaml.snakeyaml.Yaml;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.Map;

public class Language {

    private static final String DEFAULT_LANGUAGE = "en";

    private final BetterLANBroadcaster plugin;
    private final Logger logger;

    private String language = DEFAULT_LANGUAGE;

    private Map<String, Object> messages = Map.of();
    private Map<String, Object> fallbackMessages = Map.of();

    public Language(BetterLANBroadcaster plugin) {
        this.plugin = plugin;
        this.logger = plugin.getLogger();
        load(plugin.getConfig().getLanguage());
    }

    public void load(String lang) {
        String requestedLanguage = (lang == null || lang.isBlank()) 
                ? DEFAULT_LANGUAGE 
                : lang.toLowerCase(Locale.ROOT);

        fallbackMessages = loadLanguageFile(DEFAULT_LANGUAGE);
        if (fallbackMessages == null) {
            fallbackMessages = Map.of();
        }

        Map<String, Object> loadedMessages;
        if (DEFAULT_LANGUAGE.equals(requestedLanguage)) {
            loadedMessages = fallbackMessages;
        } else {
            loadedMessages = loadLanguageFile(requestedLanguage);
            if (loadedMessages == null) {
                logger.warn("Language '{}' not found. Falling back to English.", requestedLanguage);
                requestedLanguage = DEFAULT_LANGUAGE;
                loadedMessages = fallbackMessages;
            }
        }

        messages = loadedMessages != null ? loadedMessages : Map.of();
        this.language = requestedLanguage;

        logger.info("Language loaded: {}", this.language);
    }

    private Map<String, Object> loadLanguageFile(String lang) {
        String path = "/lang/messages_" + lang + ".yml";

        try (InputStream inputStream = Language.class.getResourceAsStream(path)) {
            if (inputStream == null) {
                logger.warn("Language file not found: {}", path);
                return null;
            }

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
                Object loaded = new Yaml().load(reader);

                if (loaded instanceof Map<?, ?> map) {
                    @SuppressWarnings("unchecked")
                    Map<String, Object> result = (Map<String, Object>) map;
                    return result;
                }
                logger.warn("Invalid language file structure: {}", path);
            }
        } catch (IOException | RuntimeException exception) {
            logger.error("Failed to load language file '{}'.", path, exception);
        }

        return null;
    }

    public String get(String path) {
        Object value = find(messages, path);
        if (value == null) {
            value = find(fallbackMessages, path);
        }
        return value != null ? String.valueOf(value) : path;
    }

    public String get(String path, Object... args) {
        return format(path, args);
    }

    public String format(String path, Object... args) {
        String message = get(path);
        if (args != null) {
            for (int i = 0; i < args.length; i++) {
                message = message.replace("{" + i + "}", String.valueOf(args[i]));
            }
        }
        return message;
    }

    public String message(String path) {
        return get("prefix") + get(path);
    }

    public String message(String path, Object... args) {
        return get("prefix") + format(path, args);
    }

    public String raw(String path) {
        return get(path);
    }

    public String raw(String path, Object... args) {
        return format(path, args);
    }

    public String cleanColors(String text) {
        if (text == null) return "";
        return text.replaceAll("&[0-9a-fA-FK-ORk-or]", "").replaceAll("\u00A7[0-9a-fA-FK-ORk-or]", "");
    }

    private Object find(Map<String, Object> root, String path) {
        if (root == null || path == null || path.isBlank()) {
            return null;
        }

        if (root.containsKey(path)) {
            return root.get(path);
        }

        Object current = root;
        for (String part : path.split("\\.")) {
            if (!(current instanceof Map<?, ?> map)) {
                return null;
            }
            current = map.get(part);
        }

        return current;
    }

    public String getLanguage() { return language; }
    public BetterLANBroadcaster getPlugin() { return plugin; }
}