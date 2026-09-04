package com.betterlanbroadcaster;

import com.velocitypowered.api.command.SimpleCommand;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
@SuppressWarnings("unused")
public class CommandHandler implements SimpleCommand {

    private static final List<String> SUBCOMMANDS = Arrays.asList(
            "start",
            "stop",
            "status",
            "setmotd",
            "setdelay",
            "setport",
            "debug",
            "reload",
            "help",
            "version"
    );

    private final BetterLANBroadcaster plugin;

    public CommandHandler(BetterLANBroadcaster plugin) {
        this.plugin = plugin;
    }

    @Override
    public void execute(Invocation invocation) {

        // Quem executou o comando
        var source = invocation.source();

        // Argumentos
        String[] args = invocation.arguments();

        // Verifica permissão
        if (!source.hasPermission("betterlanbroadcaster.admin")) {
            source.sendMessage(
                    net.kyori.adventure.text.Component.text(
                            "Você não tem permissão para executar este comando."
                    )
            );
            return;
        }

        // Sem argumentos = version
        if (args.length == 0) {
            handleVersion(source);
            return;
        }

        switch (args[0].toLowerCase()) {

            case "start":
                handleStart(source);
                break;

            case "stop":
                handleStop(source);
                break;

            case "status":
                handleStatus(source);
                break;

            case "setmotd":
                handleSetMotd(source, args);
                break;

            case "setdelay":
                handleSetDelay(source, args);
                break;

            case "setport":
                handleSetPort(source, args);
                break;

            case "debug":
                handleDebug(source, args);
                break;

            case "reload":
                handleReload(source);
                break;

            case "help":
                handleHelp(source);
                break;

            case "version":
                handleVersion(source);
                break;

            default:
                source.sendMessage(
                        net.kyori.adventure.text.Component.text(
                                "Sintaxe inválida."
                        )
                );
                break;
        }
    }

    private void handleStart(Object source) {
        // vamos implementar
    }

    private void handleStop(Object source) {
        // vamos implementar
    }

    private void handleStatus(Object source) {
        // vamos implementar
    }

    private void handleSetMotd(Object source, String[] args) {
        // vamos implementar
    }

    private void handleSetDelay(Object source, String[] args) {
        // vamos implementar
    }

    private void handleSetPort(Object source, String[] args) {
        // vamos implementar
    }

    private void handleDebug(Object source, String[] args) {
        // vamos implementar
    }

    private void handleReload(Object source) {
        // vamos implementar
    }

    private void handleHelp(Object source) {
        // vamos implementar
    }

    private void handleVersion(Object source) {
        // vamos implementar
    }

    @Override
    public List<String> suggest(Invocation invocation) {

        String[] args = invocation.arguments();

        if (!invocation.source().hasPermission(
                "betterlanbroadcaster.admin")) {
            return new ArrayList<>();
        }

        if (args.length == 1) {

            String partial = args[0].toLowerCase();

            return SUBCOMMANDS.stream()
                    .filter(s -> s.startsWith(partial))
                    .collect(Collectors.toList());
        }

        if (args[0].equalsIgnoreCase("setport")
                && args.length == 2) {

            return Arrays.asList("auto");
        }

        if (args[0].equalsIgnoreCase("debug")
                && args.length == 2) {

            return Arrays.asList("on", "off");
        }

        return new ArrayList<>();
    }
}   