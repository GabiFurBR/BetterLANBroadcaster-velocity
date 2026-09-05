# BetterLANBroadcaster-Velocity

A lightweight Velocity proxy plugin that allows Minecraft clients to discover a Velocity proxy through Minecraft's native LAN server discovery system.

BetterLANBroadcaster-Velocity broadcasts the proxy over UDP multicast, allowing it to appear automatically in the Multiplayer menu without requiring players to manually enter the server address.

This project is a Velocity port and substantial modification of the original BetterLANBroadcaster project by myxxr.

## Features

* Minecraft LAN server discovery using UDP multicast
* Compatible with Minecraft's native LAN discovery format
* Velocity-native implementation
* Configurable broadcast MOTD
* `{online}` and `{max}` MOTD placeholders
* Configurable broadcast interval
* Automatic or custom advertised server port
* Automatic network interface detection
* Manual network interface selection
* Multiple network interface support
* Start/stop broadcasting at runtime
* Runtime configuration reload
* Runtime broadcaster reconfiguration
* Broadcast status command
* Debug mode with multicast packet logging
* Multi-language message support
* English, Brazilian Portuguese, and Chinese translations
* English fallback for missing translations
* Java 21 support

## Requirements

* Velocity 3.4.0 or newer
* Java 21 or newer
* A Minecraft client that supports Minecraft's native LAN server discovery

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

| Command                                | Description                                        |
| -------------------------------------- | -------------------------------------------------- |
| `/blb`                                 | Show plugin version information                    |
| `/blb start`                           | Start LAN broadcasting                             |
| `/blb stop`                            | Stop LAN broadcasting                              |
| `/blb status`                          | Show current broadcast status                      |
| `/blb setmotd <MOTD>`                  | Change the broadcast MOTD                          |
| `/blb setdelay <milliseconds>`         | Change the broadcast interval                      |
| `/blb setport <port\|auto>`            | Set the advertised LAN port                        |
| `/blb setinterface <auto\|name\|IPv4>` | Select the network interface used for broadcasting |
| `/blb debug <on\|off>`                 | Enable or disable debug logging                    |
| `/blb reload`                          | Reload the configuration                           |
| `/blb help`                            | Show available commands                            |
| `/blb version`                         | Show plugin version                                |

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
```

### Configuration Options

| Option               | Description                                                                        | Default              |
| -------------------- | ---------------------------------------------------------------------------------- | -------------------- |
| `language`           | Language used for plugin messages                                                  | `en`                 |
| `debug`              | Enable debug logging                                                               | `false`              |
| `broadcast-enabled`  | Start broadcasting automatically when Velocity starts                              | `false`              |
| `broadcast-delay-ms` | Interval between broadcast packets in milliseconds                                 | `100`                |
| `broadcast-port`     | Port advertised to LAN clients; `0` automatically uses the Velocity listening port | `0`                  |
| `network-interface`  | Network interface used for multicast broadcasting                                  | `auto`               |
| `motd`               | MOTD displayed during LAN discovery                                                | `A Minecraft Server` |

### Broadcast Port

`broadcast-port` controls the port advertised to Minecraft clients through LAN discovery.

It **does not change the port used by Velocity**.

For example:

```yaml
broadcast-port: 0
```

automatically advertises the port on which Velocity is listening.

A custom advertised port can also be specified:

```yaml
broadcast-port: 25567
```

This only changes the port included in the LAN discovery packet.

### Network Interface

BetterLANBroadcaster-Velocity supports automatic and manual network interface selection.

```yaml
network-interface: auto
```

Available options:

* `auto` — automatically detect suitable IPv4 multicast interfaces
* Interface name — select a specific network interface
* IPv4 address — select the interface associated with that address

Examples:

```yaml
network-interface: auto
```

```yaml
network-interface: ethernet_32774
```

```yaml
network-interface: 192.168.0.56
```

Using `auto` is recommended for most installations.

Manual selection can be useful when the machine has multiple network adapters, such as Ethernet, Wi-Fi, VPN, or virtual network interfaces.

## Languages

Language files are bundled inside the plugin JAR.

Currently available:

* `en` — English
* `br` — Brazilian Portuguese
* `zh` — Chinese

Set the desired language in `config.yml`:

```yaml
language: br
```

Language files are bundled with the plugin and are not copied to the plugin data directory.

If a translation or message key is missing, the plugin falls back to English.

## MOTD Formatting

The broadcast MOTD supports Minecraft legacy color and formatting codes using `&`.

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

### Placeholders

The following placeholders can be used in the broadcast MOTD:

* `{online}` — Current number of players connected to the Velocity proxy
* `{max}` — Maximum player count advertised by the proxy

Example:

```yaml
motd: "&aSurvival &7| &f{online}&7/&f{max} players"
```

## LAN Discovery Protocol

BetterLANBroadcaster-Velocity uses Minecraft's native UDP multicast LAN discovery format.

| Property          | Value         |
| ----------------- | ------------- |
| Protocol          | UDP Multicast |
| Multicast address | `224.0.2.60`  |
| Multicast port    | `4445`        |
| Encoding          | UTF-8         |

Broadcast packets use the following format:

```text
[MOTD]Server MOTD[/MOTD][AD]Server Port[/AD]
```

Example:

```text
[MOTD]A Minecraft Server[/MOTD][AD]25565[/AD]
```

The multicast address and protocol format are part of Minecraft's LAN discovery mechanism and normally should not be changed.

## Troubleshooting

### The server does not appear in Minecraft

Check the following:

1. Make sure BetterLANBroadcaster is running:

   `/blb status`

2. Make sure broadcasting is enabled:

   `/blb start`

3. Enable debug logging:

   `/blb debug on`

4. Check that the selected network interface supports IPv4 multicast.

5. If the machine has multiple network adapters, try explicitly selecting the correct interface:

   ```yaml
   network-interface: 192.168.0.56
   ```

6. Check your operating system firewall and make sure UDP multicast traffic is not being blocked.

7. VPN and virtual network adapters can affect multicast routing. If necessary, use `network-interface` to select the intended physical network interface.

### The server appears but players cannot connect

Check the advertised port:

`/blb status`

If using:

```yaml
broadcast-port: 0
```

the plugin advertises the port currently used by Velocity.

If a custom port is configured, make sure that port actually corresponds to a reachable Minecraft server/proxy endpoint.

### Debug logging

Debug mode can be enabled with:

`/blb debug on`

The plugin will log multicast broadcast activity to the console.

Disable it when troubleshooting is complete:

`/blb debug off`

## Building

Clone the repository:

```bash
git clone https://github.com/GabiFurBR/BetterLANBroadcaster-velocity.git
```

Enter the project directory:

```bash
cd BetterLANBroadcaster-velocity
```

### Windows

```bat
.\mvnw.cmd clean package
```

### Linux / macOS

```bash
./mvnw clean package
```

The resulting plugin JAR will be generated in the `target/` directory.

## Project Structure

```text
src/main/java/com/betterlanbroadcaster/
├── BetterLANBroadcaster.java
├── CommandHandler.java
├── Config.java
├── Language.java
└── MulticastBroadcaster.java

src/main/resources/
├── config.yml
└── lang/
    ├── messages_br.yml
    ├── messages_en.yml
    └── messages_zh.yml
```

## Credits

BetterLANBroadcaster-Velocity is based on and substantially modified from the original BetterLANBroadcaster project by myxxr.

**Original project:**

`https://github.com/myxxr/BetterLANBroadcaster`

**Original author:**

myxxr

**Velocity port and maintenance:**

GabiFurBR

The original project and this project are licensed under the GNU General Public License v3.0.

## Project Origin

The original BetterLANBroadcaster was designed for the Bukkit/Spigot ecosystem.

This repository ports the project to Velocity and introduces substantial modifications to the platform integration, networking implementation, configuration system, command system, language system, and runtime lifecycle.

The goal of this project is to provide the same core LAN discovery functionality in a Velocity-native implementation while adding additional configuration and networking capabilities.

## License

This project is licensed under the GNU General Public License v3.0 (GPL v3).

See the `LICENSE` file for the complete license text.

## Links

* GitHub: https://github.com/GabiFurBR/BetterLANBroadcaster-velocity
* Original project: https://github.com/myxxr/BetterLANBroadcaster
