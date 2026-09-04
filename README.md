# BetterLANBroadcaster-Velocity

A lightweight Velocity proxy plugin that allows Minecraft clients to discover a Velocity proxy through Minecraft's native LAN server discovery system.

BetterLANBroadcaster-Velocity broadcasts the proxy over UDP multicast, allowing it to appear automatically in the Multiplayer menu without requiring players to manually enter the server address.

This project is a Velocity port and substantial modification of the original BetterLANBroadcaster project by myxxr.

## Features

- Minecraft LAN server discovery using UDP multicast
- Compatible with Minecraft's native LAN discovery format
- Velocity-native implementation
- Configurable broadcast MOTD
- `{online}` and `{max}` MOTD placeholders
- Configurable broadcast interval
- Automatic or custom advertised server port
- Automatic network interface detection
- Manual network interface selection
- Multiple network interface support
- Start/stop broadcasting at runtime
- Runtime configuration reload
- Runtime broadcaster reconfiguration
- Broadcast status command
- Debug mode with multicast packet logging
- Multi-language message support
- English, Brazilian Portuguese, and Chinese translations
- English fallback for missing translations
- Java 21 support

## Requirements

- Velocity 3.4.0 or newer
- Java 21 or newer
- A Minecraft client that supports Minecraft's native LAN server discovery

## Installation

1. Download the latest `BetterLANBroadcaster-Velocity` `.jar` from the Releases page.
2. Place the `.jar` file inside the Velocity `plugins/` directory.
3. Start or restart your Velocity proxy.
4. The plugin will generate its configuration at:

   `plugins/betterlanbroadcaster/config.yml`

5. Configure the plugin as desired.
6. Set `broadcast-enabled: true` if you want LAN broadcasting to start automatically.

Minecraft clients on the same local network should then be able to discover the Velocity proxy through the Multiplayer menu.

> **Note:** LAN discovery uses UDP multicast. Network configuration, firewalls, routers, VPN adapters, and operating-system settings may prevent multicast packets from reaching other devices.

## Commands

| Command | Description |
| --- | --- |
| `/blb` | Show plugin version information |
| `/blb start` | Start LAN broadcasting |
| `/blb stop` | Stop LAN broadcasting |
| `/blb status` | Show current broadcast status |
| `/blb setmotd <MOTD>` | Change the broadcast MOTD |
| `/blb setdelay <milliseconds>` | Change the broadcast interval |
| `/blb setport <port\|auto>` | Set the advertised LAN port |
| `/blb setinterface <auto\|name\|IPv4>` | Select the network interface used for broadcasting |
| `/blb debug <on\|off>` | Enable or disable debug logging |
| `/blb reload` | Reload the configuration |
| `/blb help` | Show available commands |
| `/blb version` | Show plugin version |

The command also supports the alias:

`/betterlanbroadcaster`

Administrative commands require:

`betterlanbroadcaster.admin`

## Configuration

The configuration file is located at:

`plugins/betterlanbroadcaster/config.yml`

Example:

```yaml
language: en

debug: false

broadcast-enabled: false
broadcast-delay-ms: 100
broadcast-port: 0

network-interface: auto

motd: "&aA Minecraft Server"
