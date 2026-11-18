---
class: MultiplayerAPI
inherits: RefCounted
verified: 4.3
short: High-level multiplayer manager accessible via Node.multiplayer that routes @rpc calls and peer authority.
---

## Properties
- `multiplayer_peer: MultiplayerPeer` — The underlying transport peer (such as `ENetMultiplayerPeer`).

## Methods
- `bool is_server()` — Returns `true` if this instance is the authoritative host (peer ID `1`).
- `int get_unique_id()` — Returns this peer's network ID.
- `int get_remote_sender_id()` — Inside an `@rpc` function, returns the peer ID that invoked the RPC.

## Signals
- `peer_connected(id: int)` — Emitted when a remote player connects.
- `peer_disconnected(id: int)` — Emitted when a remote player disconnects.

## Description
Accessed via `multiplayer` on any `[[Node]]`, `MultiplayerAPI` powers LAN and online multiplayer. Always check `if not multiplayer.is_server(): return` and `multiplayer.get_remote_sender_id()` when validating client action requests!

### Kadi & Solo Game Dev Example

> [!GODOT]
> **Godot 4.3 Inspector & Reference Tip (`MultiplayerAPI`)**: Press **F1** inside the Godot 4.3 editor or tap any `MultiplayerAPI` pill in Kadi Companion to inspect its properties, methods, and signals offline.

```gdscript
@rpc("any_peer", "call_local", "reliable")
func request_move(suit: int, rank: int) -> void:
	if not multiplayer.is_server():
		return
	var caller_id: int = multiplayer.get_remote_sender_id()
	print("Validated move from peer: ", caller_id)
```
