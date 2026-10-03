package com.betterlanbroadcaster;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public final class CommandHandler implements SimpleCommand {

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
            case "start" -> handleStart(source, args);
            case "stop" -> handleStop(source, args);
            case "status" -> handleStatus(source, args);
            case "setmotd" -> handleSetMotd(source, args);
            case "setdelay" -> handleSetDelay(source, args);
            case "setport" -> handleSetPort(source, args);
            case "setinterface" -> handleSetInterface(source, args);
            case "debug" -> handleDebug(source, args);
            case "reload" -> handleReload(source, args);
            case "help" -> handleHelp(source);
            case "version" -> handleVersion(source);
            default -> sendSyntax(source, "/blb help");
        }
    }

    private void handleStart(CommandSource source, String[] args) {
        if (args.length != 1) {
            sendSyntax(source, "/blb start");
            return;
        }

        synchronized (plugin) {
            if (isBroadcastRunning()) {
                send(source, "broadcast.already-running");
                return;
            }

            if (plugin.startBroadcaster()) {
                send(source, "broadcast.started");
            } else {
                send(source, "error.start-failed");
            }
        }
    }

    private void handleStop(CommandSource source, String[] args) {
        if (args.length != 1) {
            sendSyntax(source, "/blb stop");
            return;
        }

        synchronized (plugin) {
            if (!isBroadcastRunning()) {
                send(source, "broadcast.already-stopped");
                return;
            }

            if (plugin.stopBroadcaster()) {
                send(source, "broadcast.stopped");
            } else {
                send(source, "error.stop-failed");
            }
        }
    }

    private void handleStatus(CommandSource source, String[] args) {
        if (args.length != 1) {
            sendSyntax(source, "/blb status");
            return;
        }

        MulticastBroadcaster broadcaster = plugin.getBroadcaster();
        String status = broadcaster != null && broadcaster.isRunning()
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
        send(source, "broadcast.status.multicast", MulticastBroadcaster.MULTICAST_ADDRESS, MulticastBroadcaster.MULTICAST_PORT, MulticastBroadcaster.MULTICAST_TTL);
        send(source, "broadcast.status.interfaces", broadcaster == null ? 0 : broadcaster.getActiveInterfaceCount());

        if (broadcaster != null) {
            for (MulticastBroadcaster.InterfaceInfo info : broadcaster.getActiveInterfaces()) {
                send(source, "broadcast.status.interface-detail", info.displayName(), info.name(), info.ipv4());
            }
            send(source, "broadcast.status.stats", broadcaster.getBroadcastsSent(), broadcaster.getBroadcastFailures());
            send(source, "broadcast.status.last-broadcast", formatLastBroadcast(broadcaster.getLastBroadcastEpochMillis()));
        }

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

        String motd = String.join(" ", Arrays.copyOfRange(args, 1, args.length)).trim();
        if (motd.isBlank()) {
            send(source, "error.invalid-motd");
            return;
        }

        synchronized (plugin) {
            String previous = plugin.getConfig().getMotd();
            if (!plugin.getConfig().setMotd(motd)) {
                send(source, "error.config-save");
                return;
            }

            if (isBroadcastRunning() && !plugin.reconfigureBroadcaster()) {
                plugin.getConfig().setMotd(previous);
                plugin.reconfigureBroadcaster();
                send(source, "error.reload-broadcast");
                return;
            }
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
        } catch (NumberFormatException exception) {
            send(source, "error.invalid-delay");
            return;
        }

        if (delay < 50L || delay > 86_400_000L) {
            send(source, "error.invalid-delay");
            return;
        }

        synchronized (plugin) {
            long previous = plugin.getConfig().getBroadcastDelayMs();
            if (!plugin.getConfig().setBroadcastDelayMs(delay)) {
                send(source, "error.config-save");
                return;
            }

            if (isBroadcastRunning() && !plugin.reconfigureBroadcaster()) {
                plugin.getConfig().setBroadcastDelayMs(previous);
                plugin.reconfigureBroadcaster();
                send(source, "error.reload-broadcast");
                return;
            }
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
            } catch (NumberFormatException exception) {
                send(source, "error.invalid-port");
                return;
            }
            if (port < 1 || port > 65535) {
                send(source, "error.invalid-port");
                return;
            }
        }

        synchronized (plugin) {
            int previous = plugin.getConfig().getBroadcastPort();
            if (!plugin.getConfig().setBroadcastPort(port)) {
                send(source, "error.config-save");
                return;
            }

            if (isBroadcastRunning() && !plugin.reconfigureBroadcaster()) {
                plugin.getConfig().setBroadcastPort(previous);
                plugin.reconfigureBroadcaster();
                send(source, "error.reload-broadcast");
                return;
            }
        }

        if (port == 0) {
            send(source, "broadcast.port-set-auto", plugin.getServer().getBoundAddress().getPort());
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

        synchronized (plugin) {
            String previous = plugin.getConfig().getNetworkInterface();
            if (!plugin.getConfig().setNetworkInterface(networkInterface)) {
                send(source, "error.config-save");
                return;
            }

            if (isBroadcastRunning() && !plugin.reconfigureBroadcaster()) {
                plugin.getConfig().setNetworkInterface(previous);
                plugin.reconfigureBroadcaster();
                send(source, "error.interface-start");
                return;
            }
        }

        send(source, "broadcast.interface-set", networkInterface);
    }

    private void handleDebug(CommandSource source, String[] args) {
        if (args.length == 1) {
            send(
                    source,
                    "debug.label",
                    plugin.getConfig().isDebug()
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

        synchronized (plugin) {
            if (!plugin.getConfig().setDebug(enabled)) {
                send(source, "error.config-save");
                return;
            }
            if (plugin.getBroadcaster() != null) {
                plugin.getBroadcaster().setDebug(enabled);
            }
        }

        send(source, enabled ? "debug.on" : "debug.off");
    }

    private void handleReload(CommandSource source, String[] args) {
        if (args.length != 1) {
            sendSyntax(source, "/blb reload");
            return;
        }

        synchronized (plugin) {
            if (!plugin.reloadBroadcaster()) {
                send(source, "error.config-reload");
                return;
            }
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
                case "setport", "setinterface" -> filterOptions(List.of("auto"), input);
                case "debug" -> filterOptions(List.of("on", "off"), input);
                default -> List.of();
            };
        }

        return List.of();
    }

    private List<String> filterOptions(List<String> options, String input) {
        List<String> result = new ArrayList<>();
        for (String option : options) {
            if (option.startsWith(input)) {
                result.add(option);
            }
        }
        return result;
    }

    private boolean isBroadcastRunning() {
        MulticastBroadcaster broadcaster = plugin.getBroadcaster();
        return broadcaster != null && broadcaster.isRunning();
    }

    private String formatLastBroadcast(long epochMillis) {
        if (epochMillis <= 0) {
            return plugin.getLanguage().get("broadcast.status.never");
        }
        return java.time.Instant.ofEpochMilli(epochMillis).toString();
    }

    private void sendSyntax(CommandSource source, String usage) {
        send(source, "error.invalid-syntax", usage);
    }

    private void send(CommandSource source, String path, Object... arguments) {
        sendRaw(source, plugin.getLanguage().message(path, arguments));
    }

    private void sendRaw(CommandSource source, String message) {
        if (message == null || message.isBlank()) {
            return;
        }
        source.sendMessage(LegacyComponentSerializer.legacyAmpersand().deserialize(message));
    }
}
