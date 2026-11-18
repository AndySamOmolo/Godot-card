---
class: ENetMultiplayerPeer
inherits: RefCounted
verified: 4.3
short: UDP/ENet transport peer for hosting and joining low-latency LAN and online multiplayer matches.
---

## Properties
- (Managed via singleton methods.)

## Methods
- `Error create_server(port: int, max_clients: int = 32)` — Starts listening as the host (`peer_id = 1`) on `port`.
- `Error create_client(address: String, port: int)` — Connects to a host at `address:port`.
- `void close()` — Closes the ENet connection.

## Signals
- (Signals are emitted via `multiplayer.peer_connected` / `peer_disconnected`.)

## Description
`ENetMultiplayerPeer` provides reliable and unreliable UDP channels over LAN or internet. Assign it to `multiplayer.multiplayer_peer` to activate `@rpc` calls across devices.

### Kadi & Solo Game Dev Example

> [!GODOT]
> **Godot 4.3 Inspector & Reference Tip (`ENetMultiplayerPeer`)**: Press **F1** inside the Godot 4.3 editor or tap any `ENetMultiplayerPeer` pill in Kadi Companion to inspect its properties, methods, and signals offline.

```gdscript
func start_lan_host(port: int = 8910) -> void:
	var peer := ENetMultiplayerPeer.new()
	if peer.create_server(port, 4) == OK:
		multiplayer.multiplayer_peer = peer
```
