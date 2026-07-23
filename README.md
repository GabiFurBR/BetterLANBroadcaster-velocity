# BetterLANBroadcaster

A Spigot plugin that broadcasts Minecraft servers over LAN using UDP multicast, allowing clients to discover servers without manually entering addresses.

## Features

- Follows the Minecraft LAN discovery protocol, sending server information to `224.0.2.60:4445`
- MOTD supports `{online}` (current online players) and `{max}` (max players) variables, automatically replaced in real-time
- Dynamic start/stop broadcasting via commands
- Online modification of broadcast MOTD and broadcast delay
- View current broadcast status
- Custom broadcast port (manual or auto-detect)
- Debug mode for real-time broadcast packet logs
- Multi-language support (English, Chinese)
- Hot-reload all configurations

## Commands

| Command | Description |
|---------|-------------|
| `/blb` | Show plugin version info |
| `/blb start` | Start broadcasting |
| `/blb stop` | Stop broadcasting |
| `/blb status` | View broadcast status |
| `/blb setmotd <MOTD>` | Set broadcast MOTD |
| `/blb setdelay <milliseconds>` | Set broadcast delay |
| `/blb setport <port\|auto>` | Set broadcast port (use `auto` to auto-detect) |
| `/blb debug <on\|off>` | Enable/disable debug mode |
| `/blb reload` | Reload configuration files |
| `/blb help` | Show all subcommand help |
| `/blb version` | Show plugin version info |

> Tip: `/betterlanbroadcaster` is the full command, `/blb` is its alias; both are equivalent.

All commands require the `betterlanbroadcaster.admin` permission, available to OPs by default.

## Configuration

`plugins/BetterLANBroadcaster/config.yml`

```yaml
# Language setting: en or zh
language: en

# Server MOTD displayed on LAN
# Available variables:
#   {online} - Current online player count
#   {max}    - Maximum player count
motd: "A Minecraft Server"

# Broadcast delay (milliseconds)
# Default: 1500 ms = 1.5 seconds
broadcast-delay-ms: 1500

# Debug mode: when true, each broadcast packet is logged to console
debug: false

# Broadcast port: set to 0 to auto-detect server port, or specify a port number
broadcast-port: 0
```

## Multi-language

Language files are located in the `plugins/BetterLANBroadcaster/lang/` directory:

- `messages_en.yml` - English language file
- `messages_zh.yml` - Chinese language file

Set `language` to `en` or `zh` in `config.yml` to switch languages.

## Color Codes

MOTD supports Minecraft color codes using the `&` symbol followed by a color character to color the server name.

Common color codes:

| Code | Color |
|------|-------|
| `&0` | Black |
| `&1` | Dark Blue |
| `&2` | Dark Green |
| `&3` | Dark Aqua |
| `&4` | Dark Red |
| `&5` | Purple |
| `&6` | Gold |
| `&7` | Gray |
| `&8` | Dark Gray |
| `&9` | Blue |
| `&a` | Green |
| `&b` | Aqua |
| `&c` | Red |
| `&d` | Light Purple |
| `&e` | Yellow |
| `&f` | White |

Format codes:

| Code | Effect |
|------|--------|
| `&l` | Bold |
| `&o` | Italic |
| `&n` | Underline |
| `&m` | Strikethrough |
| `&k` | Random |
| `&r` | Reset |

For example, setting a colored MOTD in the config:

```yaml
motd: "&6&l✦ &eBetterLAN &7- &aSurvival Server &6&l✦"
```

In-game, it will display as gold **✦ BetterLAN - Survival Server ✦**.

## LAN Discovery Protocol

The plugin implements a protocol fully compatible with Minecraft's vanilla LAN broadcast:

- Protocol: UDP Multicast
- Address: `224.0.2.60`
- Port: `4445`
- Message format: `[MOTD]Server MOTD[/MOTD][AD]Server Port[/AD]`
- Encoding: UTF-8

## Build

```bash
git clone https://github.com/myxxr/BetterLANBroadcaster.git
cd BetterLANBroadcaster
mvn clean package
```

The build artifact will be at `target/BetterLANBroadcaster-1.0.0.jar`.

## Dependencies

- Spigot 1.20.4+ API
- Java 17+

## Installation

1. Place `BetterLANBroadcaster-1.0.0.jar` into the `plugins/` directory
2. Start the server
3. Use `/blb start` to start broadcasting
4. Minecraft clients on the LAN can discover the server in the Multiplayer menu

## Author

Immyxxr
- Email: myxxr1999@163.com
- QQ: 2855848368

## License

This project is licensed under the GNU General Public License v3.0 (GPL v3). For details, see the [LICENSE](LICENSE) file.

## bStats

![bStats](https://bstats.org/signatures/bukkit/BetterLANBroadcaster.svg)
