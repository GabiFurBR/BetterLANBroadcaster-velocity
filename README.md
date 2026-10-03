# BetterLANBroadcaster-Velocity

BetterLANBroadcaster-Velocity is a lightweight Velocity proxy plugin that enables Minecraft's native LAN server discovery through UDP multicast.

It allows Minecraft servers running behind a Velocity proxy to be advertised through Minecraft's built-in LAN discovery mechanism, making them visible in the Multiplayer menu of compatible Minecraft clients on the same local network without requiring players to manually enter the server address.

The plugin focuses on a simple and reliable design, with configurable networking, runtime controls, multilingual support, and minimal overhead.

## Features

* 📡 Native Minecraft LAN server discovery using UDP multicast
* 🚀 Designed specifically for Velocity
* ⚙️ Configurable broadcast interval and advertised server port
* 💬 Customizable MOTD with `{online}` and `{max}` placeholders
* 🌐 Automatic or manual network interface selection
* 🔀 Support for multiple network interfaces
* 🚫 Optional network interface exclusions
* 🔄 Start, stop, reload, and runtime reconfiguration
* 📊 Runtime status and broadcast statistics
* 🐛 Configurable debug logging with reduced log spam
* 🌍 Multilingual messages with 18 supported languages
* 🔙 Automatic English fallback for missing or unavailable translations
* 🛡️ Input validation and safer runtime reconfiguration
* ♻️ Controlled socket and executor lifecycle
* ☕ Java 21 support

## Supported Languages

The plugin currently includes:

* 🇺🇸 English (`en`)
* 🇧🇷 Brazilian Portuguese (`br`)
* 🇨🇳 Chinese (`zh`)
* 🇪🇸 Spanish (`es`)
* 🇩🇪 German (`de`)
* 🇫🇷 French (`fr`)
* 🇯🇵 Japanese (`ja`)
* 🇷🇺 Russian (`ru`)
* 🇮🇹 Italian (`it`)
* 🇰🇷 Korean (`ko`)
* 🇳🇱 Dutch (`nl`)
* 🇵🇱 Polish (`pl`)
* 🇹🇷 Turkish (`tr`)
* 🇺🇦 Ukrainian (`uk`)
* 🇻🇳 Vietnamese (`vi`)
* 🇮🇩 Indonesian (`id`)
* 🇨🇿 Czech (`cs`)
* 🇸🇪 Swedish (`sv`)

English is used as the fallback language when a selected translation is unavailable or incomplete.

## Requirements

* Velocity 3.4.0 or newer
* Java 21 or newer
* A Minecraft client that supports native LAN server discovery

## How It Works

BetterLANBroadcaster-Velocity periodically sends UDP multicast packets using Minecraft's LAN server discovery protocol.

Velocity continues to handle player connections and server routing, while BetterLANBroadcaster-Velocity advertises the proxy as a LAN server.

The plugin uses:

* Multicast address: `224.0.2.60`
* UDP port: `4445`
* IPv4 multicast
* UTF-8 payload encoding
* Multicast TTL: `1`

The advertised payload follows Minecraft's LAN discovery format:

```text
[MOTD]MOTD[/MOTD][AD]PORT[/AD]
```

For example:

```text
[MOTD]My Minecraft Server[/MOTD][AD]25565[/AD]
```

The advertised port can be configured independently from the proxy's actual listening configuration when necessary.

## MOTD Placeholders

The MOTD supports:

| Placeholder | Description                                               |
| ----------- | --------------------------------------------------------- |
| `{online}`  | Current number of players connected to the Velocity proxy |
| `{max}`     | Maximum player capacity reported by the proxy             |

Example:

```yaml
motd: "<green>My Network</green> <gray>[{online}/{max}]</gray>"
```

The MOTD supports Unicode and Adventure MiniMessage formatting. Before transmission, the generated payload is sanitized and constrained to a safe UDP packet size.

## Network Interfaces

The plugin supports both automatic and manual interface selection.

### Automatic

```yaml
network-interface: auto
```

In automatic mode, the plugin discovers suitable IPv4 network interfaces and can advertise through multiple interfaces when appropriate.

This is useful for systems with more than one active network path, such as:

* Ethernet and Wi-Fi
* Physical LAN adapters
* VPN interfaces
* Virtual network adapters

### Manual

An interface can be selected by name or IPv4 address.

This can be useful when a machine has several adapters and LAN discovery should be restricted to a specific network.

### Excluding Interfaces

Unwanted interfaces can optionally be excluded from automatic selection.

This can help prevent unnecessary multicast traffic through virtual adapters, VPNs, development environments, or other network interfaces that are not intended to participate in LAN discovery.

Network configuration, operating system firewall rules, VPN software, routing, multicast support, and virtual adapters may affect discovery behavior.

## Configuration

The plugin provides configuration options for:

* Language
* Broadcast enabled/disabled
* Broadcast interval
* Advertised port
* Network interface
* Network interface exclusions
* MOTD
* Debug logging

Example:

```yaml
language: br

broadcast-enabled: true

broadcast-delay-ms: 1500

broadcast-port: auto

network-interface: auto

network-interface-exclude: []

motd: "<green>Meu Servidor</green> <gray>[{online}/{max}]</gray>"

debug: false
```

The configuration can be reloaded at runtime without restarting the proxy.

## Commands

All administrative commands use the `/blb` command.

```text
/blb start
/blb stop
/blb status
/blb setmotd <motd>
/blb setdelay <milliseconds>
/blb setport <port|auto>
/blb setinterface <auto|name|IPv4>
/blb debug [on|off]
/blb reload
/blb help
/blb version
```

The plugin also provides command suggestions/autocomplete where applicable.

Administrative commands require the appropriate plugin permission.

## Runtime Behavior

BetterLANBroadcaster-Velocity is designed to remain lightweight while running continuously.

The broadcaster uses:

* A dedicated scheduled executor
* A single scheduled broadcast task
* One UDP socket per selected network interface
* Cached multicast destination information
* Controlled interface discovery
* Safe socket shutdown and recreation
* Rate-limited diagnostic logging

Network interfaces are not enumerated on every broadcast. They are evaluated when the broadcaster starts, is reconfigured, or when recovery is required after a network failure.

## Compatibility

The plugin is designed for:

* Velocity
* Java 21+
* Windows
* Linux
* IPv4-capable network environments

Multicast behavior may vary between operating systems, network drivers, firewalls, routers, VPN software, and virtual adapters.

In particular, VPN and virtual-network software such as Radmin VPN, ZeroTier, Tailscale, Hamachi, Hyper-V, VMware, VirtualBox, Docker, and WSL can affect which interfaces support or route multicast traffic.

For environments with multiple network interfaces, manual interface selection or interface exclusions may provide more predictable behavior.

## Performance

BetterLANBroadcaster-Velocity is intended to have minimal impact on the Velocity proxy.

The implementation avoids unnecessary polling and repeated network-interface enumeration, minimizes object creation during the broadcast loop, reuses runtime resources where practical, and keeps network operations isolated from the proxy's main plugin lifecycle.

The plugin does not create a thread per network interface and does not perform continuous network scanning.

## bStats

BetterLANBroadcaster-Velocity includes bStats for anonymous plugin usage statistics.

The metrics are intended to provide general information about plugin usage and configuration trends while avoiding the collection of unnecessary personal information.

bStats is used for telemetry only and is not required for the plugin's LAN discovery functionality.

## Based on BetterLANBroadcaster

BetterLANBroadcaster-Velocity is a Velocity port and substantial modification of the original BetterLANBroadcaster project by **myxxr**.

The original project was designed for the Bukkit/Spigot ecosystem. This project adapts the functionality to Velocity and includes changes to:

* Velocity platform integration
* Plugin lifecycle management
* UDP multicast networking
* Network interface handling
* Configuration management
* Runtime reconfiguration
* Command handling
* Language and translation handling
* Logging
* Resource and socket management

Original project:

https://github.com/myxxr/BetterLANBroadcaster

Original credits are preserved in the project repository.

## Issues & Contributions

Bug reports, suggestions, and contributions are welcome.

When reporting an issue, please include, when possible:

* Velocity version
* Java version
* Minecraft client version
* Operating system
* Network configuration
* Plugin configuration
* Relevant console logs
* Whether the server is using VPN or virtual network adapters

This information can be especially useful when diagnosing multicast discovery problems.

GitHub:

https://github.com/GabiFurBR/BetterLANBroadcaster-velocity

## License

See the repository's license file for the applicable license and redistribution terms.
