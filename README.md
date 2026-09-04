# BetterLANBroadcaster-Velocity

A Velocity proxy plugin that broadcasts Minecraft servers over LAN using UDP multicast, allowing Minecraft clients to discover the proxy without manually entering its address.

This project is a Velocity port of the original BetterLANBroadcaster project.

## Features

* LAN server discovery using Minecraft's UDP multicast protocol
* Compatible with Minecraft's vanilla LAN discovery format
* Multicast address: `224.0.2.60:4445`
* Configurable broadcast MOTD
* `{online}` and `{max}` MOTD placeholders
* Configurable broadcast interval
* Automatic or custom broadcast port
* Start/stop broadcasting at runtime
* Broadcast status command
* Debug mode for packet logging
* Multi-language message support
* Configuration reload
* Velocity-native command system
* Java 21 support

## Requirements

* Velocity 3.4.0 or newer
* Java 21 or newer

## Installation

1. Download the latest `BetterLANBroadcaster-Velocity` `.jar` from the Releases page.
2. Place the `.jar` file in the Velocity `plugins/` directory.
3. Start or restart your Velocity proxy.
4. Configure the plugin in:

`plugins/betterlanbroadcaster/config.yml`

5. Enable broadcasting if desired:

```yaml
broadcast-enabled: true
```

Minecraft clients on the same LAN should then be able to discover the proxy through the Multiplayer menu.

## Commands

| Command                        | Description                     |
| ------------------------------ | ------------------------------- |
| `/blb`                         | Show plugin version information |
| `/blb start`                   | Start LAN broadcasting          |
| `/blb stop`                    | Stop LAN broadcasting           |
| `/blb status`                  | Show current broadcast status   |
| `/blb setmotd <MOTD>`          | Change the broadcast MOTD       |
| `/blb setdelay <milliseconds>` | Change the broadcast interval   |
| `/blb setport <port\|auto>`    | Set the broadcast port          |
| `/blb debug <on\|off>`         | Enable or disable debug logging |
| `/blb reload`                  | Reload the configuration        |
| `/blb help`                    | Show available commands         |
| `/blb version`                 | Show plugin version             |

All administrative commands require the following permission:

`betterlanbroadcaster.admin`

## Configuration

The configuration file is located at:

`plugins/betterlanbroadcaster/config.yml`

Example configuration:

```yaml
language: en

debug: false

broadcast-enabled: false
broadcast-delay-ms: 1500
broadcast-port: 0

motd: "A Minecraft Server"
```

### Configuration Options

| Option               | Description                                                | Default              |
| -------------------- | ---------------------------------------------------------- | -------------------- |
| `language`           | Message language                                           | `en`                 |
| `debug`              | Enable debug logging                                       | `false`              |
| `broadcast-enabled`  | Start broadcasting automatically                           | `false`              |
| `broadcast-delay-ms` | Broadcast interval in milliseconds                         | `1500`               |
| `broadcast-port`     | Port advertised to LAN clients; `0` uses the Velocity port | `0`                  |
| `motd`               | MOTD shown in LAN discovery                                | `A Minecraft Server` |

## Languages

Language files are bundled inside the plugin JAR.

Currently available:

* `en` — English
* `br` — Portuguese (Brazil)
* `zh` — Chinese

Set the desired language in `config.yml`:

```yaml
language: br
```

Language files are bundled with the plugin and are not copied to the plugin data directory.

## MOTD Formatting

The MOTD supports Minecraft color codes using `&` codes.

Example:

```yaml
motd: "&6&l✦ &eBetterLAN &7- &aSurvival Server &6&l✦"
```

Supported formatting codes include:

| Code        | Effect        |
| ----------- | ------------- |
| `&0` - `&9` | Colors        |
| `&a` - `&f` | Colors        |
| `&l`        | Bold          |
| `&o`        | Italic        |
| `&n`        | Underline     |
| `&m`        | Strikethrough |
| `&k`        | Obfuscated    |
| `&r`        | Reset         |

The following placeholders are also supported:

* `{online}` — Current number of players connected to the proxy
* `{max}` — Maximum player count

## LAN Discovery Protocol

BetterLANBroadcaster-Velocity uses the same UDP multicast discovery format used by Minecraft's LAN server browser.

Protocol: UDP Multicast

Address: `224.0.2.60`

Port: `4445`

Encoding: UTF-8

Broadcast packets use the following format:

`[MOTD]Server MOTD[/MOTD][AD]Server Port[/AD]`

Example:

`[MOTD]A Minecraft Server[/MOTD][AD]25565[/AD]`

## Building

Clone the repository:

`git clone https://github.com/GabiFurBR/BetterLANBroadcaster-velocity.git`

Then enter the project directory:

`cd BetterLANBroadcaster-velocity`

### Windows

`.\mvnw.cmd clean package`

### Linux / macOS

`./mvnw clean package`

The resulting plugin JAR will be generated in the `target/` directory.

## Project Structure

`src/main/java/com/betterlanbroadcaster/`

* `BetterLANBroadcaster.java`
* `CommandHandler.java`
* `Config.java`
* `Language.java`
* `MulticastBroadcaster.java`

`src/main/resources/`

* `config.yml`
* `lang/messages_br.yml`
* `lang/messages_en.yml`
* `lang/messages_zh.yml`

## Credits

This project is a Velocity port of the original BetterLANBroadcaster.

Original project: `myxxr/BetterLANBroadcaster`

Original author: Immyxxr

Velocity port maintained by GabiFurBR.

## License

This project is licensed under the GNU General Public License v3.0 (GPL v3).

See `LICENSE` for details.
