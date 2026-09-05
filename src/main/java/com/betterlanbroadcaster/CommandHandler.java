package com.betterlanbroadcaster;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.Stream;

public class CommandHandler implements SimpleCommand {

    private static final List<String> SUBCOMMANDS = List.of(
            "start", "stop", "status", "setmotd", "setdelay",
            "setport", "setinterface", "debug", "reload", "help", "version"
    );

    private final BetterLANBroadcaster plugin;

    public CommandHandler(BetterLANBroadcaster plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean hasPermission(Invocation invocation) {
        return invocation.source().hasPermission(BetterLANBroadcaster.PERMISSION_ADMIN);
    }

    @Override
    public void execute(Invocation invocation) {
        CommandSource source = invocation.source();
        String[] args = invocation.arguments();

        if (!hasPermission(invocation)) {
            send(source, "error.no-permission");
            return;
        }

        if (args.length == 0) {
            handleHelp(source);
            return;
        }

        String subcommand = args[0].toLowerCase(Locale.ROOT);

        switch (subcommand) {
            case "start" -> handleStart(source);
            case "stop" -> handleStop(source);
            case "status" -> handleStatus(source);
            case "setmotd" -> handleSetMotd(source, args);
            case "setdelay" -> handleSetDelay(source, args);
            case "setport" -> handleSetPort(source, args);
            case "setinterface" -> handleSetInterface(source, args);
            case "debug" -> handleDebug(source, args);
            case "reload" -> handleReload(source);
            case "help" -> handleHelp(source);
            case "version" -> handleVersion(source);
            default -> sendSyntax(source, "/blb help");
        }
    }

    private void sendSyntax(CommandSource source, String usage) {
        send(source, "error.invalid-syntax", usage);
    }

    private void handleStart(CommandSource source) {
        if (plugin.getBroadcaster() != null && plugin.getBroadcaster().isRunning()) {
            send(source, "broadcast.already-running");
            return;
        }

        boolean started = plugin.startBroadcaster();

        if (started) {
            send(source, "broadcast.started");
        } else {
            send(source, "error.start-failed");
        }
    }

    private void handleStop(CommandSource source) {
        if (plugin.getBroadcaster() == null || !plugin.getBroadcaster().isRunning()) {
            send(source, "broadcast.already-stopped");
            return;
        }

        boolean stopped = plugin.stopBroadcaster();

        if (stopped) {
            send(source, "broadcast.stopped");
        } else {
            send(source, "error.stop-failed");
        }
    }

    private void handleStatus(CommandSource source) {
        MulticastBroadcaster broadcaster = plugin.getBroadcaster();

        String status = (broadcaster != null && broadcaster.isRunning())
                ? plugin.getLanguage().get("broadcast.status.running")
                : plugin.getLanguage().get("broadcast.status.stopped");

        sendRaw(source, plugin.getLanguage().get("prefix") + status);
        send(source, "broadcast.status.motd", plugin.getConfig().getMotd());
        send(source, "broadcast.status.delay", plugin.getConfig().getBroadcastDelayMs());

        int port = plugin.getConfig().getBroadcastPort();
        if (port == 0 && broadcaster != null && broadcaster.getPort() > 0) {
            port = broadcaster.getPort();
        }

        send(source, "broadcast.status.port", port);
        send(source, "broadcast.status.interface", plugin.getConfig().getNetworkInterface());

        send(
                source,
                "debug.label",
                plugin.getConfig().isDebug()
                        ? plugin.getLanguage().get("debug.status-on")
                        : plugin.getLanguage().get("debug.status-off")
        );
    }

    private void handleSetMotd(CommandSource source, String[] args) {
        if (args.length < 2) {
            sendSyntax(source, "/blb setmotd <texto>");
            return;
        }

        String motd = String.join(" ", Arrays.copyOfRange(args, 1, args.length));

        if (motd.isBlank()) {
            send(source, "error.invalid-motd");
            return;
        }

        if (!plugin.getConfig().set("motd", motd) || !plugin.getConfig().save()) {
            send(source, "error.config-save");
            return;
        }

        if (isBroadcastRunning()) {
            plugin.reconfigureBroadcaster();
        }

        send(source, "broadcast.motd-set", motd);
    }

    private void handleSetDelay(CommandSource source, String[] args) {
        if (args.length != 2) {
            sendSyntax(source, "/blb setdelay <ms>");
            return;
        }

        long delay;
        try {
            delay = Long.parseLong(args[1]);
        } catch (NumberFormatException e) {
            send(source, "error.invalid-delay");
            return;
        }

        if (delay < 50 || delay > 86_400_000L) {
            send(source, "error.invalid-delay");
            return;
        }

        if (!plugin.getConfig().set("broadcast-delay-ms", delay) || !plugin.getConfig().save()) {
            send(source, "error.config-save");
            return;
        }

        if (isBroadcastRunning()) {
            plugin.reconfigureBroadcaster();
        }

        send(source, "broadcast.delay-set", delay);
    }

    private void handleSetPort(CommandSource source, String[] args) {
        if (args.length != 2) {
            sendSyntax(source, "/blb setport <porta|auto>");
            return;
        }

        String value = args[1].trim();
        int port;

        if (value.equalsIgnoreCase("auto")) {
            port = 0;
        } else {
            try {
                port = Integer.parseInt(value);
            } catch (NumberFormatException e) {
                send(source, "error.invalid-port");
                return;
            }

            if (port < 1 || port > 65535) {
                send(source, "error.invalid-port");
                return;
            }
        }

        if (!plugin.getConfig().set("broadcast-port", port) || !plugin.getConfig().save()) {
            send(source, "error.config-save");
            return;
        }

        if (isBroadcastRunning()) {
            plugin.reconfigureBroadcaster();
        }

        if (port == 0) {
            int detected = plugin.getServer().getBoundAddress().getPort();
            send(source, "broadcast.port-set-auto", detected);
        } else {
            send(source, "broadcast.port-set", port);
        }
    }

    private void handleSetInterface(CommandSource source, String[] args) {
        if (args.length != 2) {
            sendSyntax(source, "/blb setinterface <nome|ip|auto>");
            return;
        }

        String networkInterface = args[1].trim();

        if (networkInterface.isBlank()) {
            send(source, "error.invalid-interface");
            return;
        }

        if (!plugin.getConfig().set("network-interface", networkInterface) || !plugin.getConfig().save()) {
            send(source, "error.config-save");
            return;
        }

        if (isBroadcastRunning()) {
            boolean success = plugin.reconfigureBroadcaster();
            if (!success) {
                send(source, "error.interface-start");
                return;
            }
        }

        send(source, "broadcast.interface-set", networkInterface);
    }

    private void handleDebug(CommandSource source, String[] args) {
        if (args.length == 1) {
            boolean enabled = plugin.getConfig().isDebug();
            send(
                    source,
                    "debug.label",
                    enabled
                            ? plugin.getLanguage().get("debug.status-on")
                            : plugin.getLanguage().get("debug.status-off")
            );
            return;
        }

        if (args.length != 2) {
            sendSyntax(source, "/blb debug <on|off>");
            return;
        }

        String value = args[1].toLowerCase(Locale.ROOT);
        boolean enabled;

        if (value.equals("on") || value.equals("true") || value.equals("enable") || value.equals("enabled")) {
            enabled = true;
        } else if (value.equals("off") || value.equals("false") || value.equals("disable") || value.equals("disabled")) {
            enabled = false;
        } else {
            send(source, "error.invalid-debug");
            return;
        }

        if (!plugin.getConfig().set("debug", enabled) || !plugin.getConfig().save()) {
            send(source, "error.config-save");
            return;
        }

        if (plugin.getBroadcaster() != null) {
            plugin.getBroadcaster().setDebug(enabled);
        }

        send(source, enabled ? "debug.on" : "debug.off");
    }

    private void handleReload(CommandSource source) {
        if (!plugin.getConfig().reload()) {
            send(source, "error.config-reload");
            return;
        }

        plugin.getLanguage().load(plugin.getConfig().getLanguage());

        boolean success = plugin.reconfigureBroadcaster();

        if (!success) {
            send(source, "error.reload-broadcast");
            return;
        }

        send(source, "config.reloaded");
    }

    private void handleHelp(CommandSource source) {
        send(source, "help.title");
        send(source, "help.start");
        send(source, "help.stop");
        send(source, "help.status");
        send(source, "help.setmotd");
        send(source, "help.setdelay");
        send(source, "help.setport");
        send(source, "help.setinterface");
        send(source, "help.debug");
        send(source, "help.reload");
        send(source, "help.version");
        send(source, "help.footer");
    }

    private void handleVersion(CommandSource source) {
        send(source, "version.line1", plugin.getVersion());
        send(source, "version.line2", "myxxr, GabiFurBR");
        send(source, "version.help-hint", "/blb help");
    }

    @Override
    public List<String> suggest(Invocation invocation) {
        if (!hasPermission(invocation)) {
            return List.of();
        }

        String[] args = invocation.arguments();

        if (args.length == 0) {
            return SUBCOMMANDS;
        }

        if (args.length == 1) {
            String input = args[0].toLowerCase(Locale.ROOT);
            return SUBCOMMANDS.stream()
                    .filter(command -> command.startsWith(input))
                    .toList();
        }

        if (args.length == 2) {
            String subcommand = args[0].toLowerCase(Locale.ROOT);
            String input = args[1].toLowerCase(Locale.ROOT);

            return switch (subcommand) {
                case "setport", "setinterface" -> Stream.of("auto")
                        .filter(option -> option.startsWith(input))
                        .toList();
                case "debug" -> Stream.of("on", "off")
                        .filter(option -> option.startsWith(input))
                        .toList();
                default -> List.of();
            };
        }

        return List.of();
    }

    private boolean isBroadcastRunning() {
        return plugin.getBroadcaster() != null && plugin.getBroadcaster().isRunning();
    }

    private void send(CommandSource source, String path, Object... arguments) {
        String message = plugin.getLanguage().message(path, arguments);
        sendRaw(source, message);
    }

    private void sendRaw(CommandSource source, String message) {
        if (message == null || message.isBlank()) {
            return;
        }

        source.sendMessage(LegacyComponentSerializer.legacyAmpersand().deserialize(message));
    }
}