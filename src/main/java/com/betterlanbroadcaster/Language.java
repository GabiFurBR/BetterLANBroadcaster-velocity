package com.betterlanbroadcaster;

import org.slf4j.Logger;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

public final class Language {

    private static final String DEFAULT_LANGUAGE = "en";
    private static final Set<String> SUPPORTED_LANGUAGES = Set.of(
            "en", "br", "zh", "es", "de", "fr", "ja", "ru", "it", "ko", "nl", "pl", "tr", "uk", "vi", "id", "cs", "sv"
    );

    private final BetterLANBroadcaster plugin;
    private final Logger logger;

    private volatile String language = DEFAULT_LANGUAGE;
    private volatile Map<String, String> messages = Map.of();
    private volatile Map<String, String> fallbackMessages = Map.of();

    public Language(BetterLANBroadcaster plugin) {
        this.plugin = plugin;
        this.logger = plugin.getLogger();
    }

    public synchronized void load(String lang) {
        String requestedLanguage = normalizeLanguage(lang);

        Map<String, String> english = loadLanguageFile(DEFAULT_LANGUAGE);
        if (english == null) {
            english = Map.of();
        }

        Map<String, String> loaded = english;
        if (!DEFAULT_LANGUAGE.equals(requestedLanguage)) {
            Map<String, String> requested = loadLanguageFile(requestedLanguage);
            if (requested != null) {
                loaded = requested;
            } else {
                logger.warn("Language '{}' was not found. Falling back to English.", requestedLanguage);
                requestedLanguage = DEFAULT_LANGUAGE;
            }
        }

        fallbackMessages = english;
        messages = loaded;
        language = requestedLanguage;
        logger.info("Language loaded: {}", requestedLanguage);
    }

    private Map<String, String> loadLanguageFile(String lang) {
        String path = "/lang/messages_" + lang + ".yml";

        try (InputStream inputStream = Language.class.getResourceAsStream(path)) {
            if (inputStream == null) {
                logger.warn("Language file not found: {}", path);
                return null;
            }

            String yaml = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
            ConfigurationNode root = YamlConfigurationLoader.builder()
                    .buildAndLoadString(yaml);

            Map<String, String> result = new HashMap<>();
            flatten(root, "", result);
            return Map.copyOf(result);
        } catch (IOException | RuntimeException exception) {
            logger.error("Failed to load language file '{}'.", path, exception);
            return null;
        }
    }

    private void flatten(ConfigurationNode node, String prefix, Map<String, String> target) {
        if (!node.childrenMap().isEmpty()) {
            for (Map.Entry<Object, ? extends ConfigurationNode> entry : node.childrenMap().entrySet()) {
                String child = String.valueOf(entry.getKey());
                String path = prefix.isEmpty() ? child : prefix + "." + child;
                flatten(entry.getValue(), path, target);
            }
            return;
        }

        if (!prefix.isEmpty()) {
            String value = node.getString();
            if (value != null) {
                target.put(prefix, value);
            }
        }
    }

    public String get(String path) {
        if (path == null || path.isBlank()) {
            return "";
        }

        String value = messages.get(path);
        if (value == null) {
            value = fallbackMessages.get(path);
        }
        return value != null ? value : path;
    }

    public String get(String path, Object... args) {
        return format(path, args);
    }

    public String format(String path, Object... args) {
        String message = get(path);
        if (args == null || args.length == 0) {
            return message;
        }

        for (int index = 0; index < args.length; index++) {
            message = message.replace("{" + index + "}", String.valueOf(args[index]));
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
        if (text == null || text.isEmpty()) {
            return "";
        }

        StringBuilder result = new StringBuilder(text.length());
        for (int index = 0; index < text.length(); index++) {
            char current = text.charAt(index);
            if ((current == '&' || current == '\u00A7')
                    && index + 1 < text.length()
                    && isLegacyColorCode(text.charAt(index + 1))) {
                index++;
                continue;
            }
            result.append(current);
        }
        return result.toString();
    }

    private String normalizeLanguage(String lang) {
        if (lang == null || lang.isBlank()) {
            return DEFAULT_LANGUAGE;
        }

        String normalized = lang.trim().toLowerCase(Locale.ROOT);
        return SUPPORTED_LANGUAGES.contains(normalized) ? normalized : DEFAULT_LANGUAGE;
    }

    private boolean isLegacyColorCode(char code) {
        return (code >= '0' && code <= '9')
                || (code >= 'a' && code <= 'f')
                || (code >= 'A' && code <= 'F')
                || code == 'k' || code == 'K'
                || code == 'l' || code == 'L'
                || code == 'm' || code == 'M'
                || code == 'n' || code == 'N'
                || code == 'o' || code == 'O'
                || code == 'r' || code == 'R';
    }

    public String getLanguage() {
        return language;
    }

    public BetterLANBroadcaster getPlugin() {
        return plugin;
    }
}
