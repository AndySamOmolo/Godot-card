---
class: AudioStreamPlayer
inherits: Node
verified: 4.3
short: A node that plays an AudioStream at a non-positional, screen-wide volume.
---

## Properties
- `stream: AudioStream` — The [[AudioStream]] resource to play.
- `volume_db: float` — Volume in decibels (0 dB = unchanged, -80 dB ≈ silent).
- `pitch_scale: float` — Pitch multiplier (1.0 = original).
- `playing: bool` — True when audio is currently playing.
- `autoplay: bool` — When true, plays on `_ready`.
- `stream_paused: bool` — Pauses/resumes playback without resetting position.
- `max_polyphony: int` — Maximum simultaneous voices (for overlapping plays).
- `bus: StringName` — The audio bus to route through, e.g. `Master` or `SFX`.

## Methods
- `void play(from_position: float = 0.0)` — Starts playback, optionally at `from_position` seconds.
- `void stop()` — Stops playback and resets position.
- `void seek(to_position: float)` — Jumps to `to_position` seconds.
- `bool is_playing()` — True if currently playing.
- `float get_playback_position()` — Returns the current playback time in seconds.
- `void set_stream_paused(pause: bool)` — Pauses or resumes without resetting.
- `AudioStreamPlayback get_stream_playback()` — Returns the playback object (for fine control).

## Signals
- `finished()` — Emitted when the stream reaches its end.

## Description
`AudioStreamPlayer` plays audio that has no spatial position — music, UI clicks, global announcements. Give it an `AudioStream` (often an imported `.wav`/`.ogg`/`.mp3`), call `play()`, and it routes through the bus named by `bus` (default `Master`). `volume_db` and `pitch_scale` adjust loudness and pitch; `max_polyphony` lets one player play the same clip overlapping for rapid-fire sound effects.

For audio that should seem to come from a place in the world, use [[AudioStreamPlayer2D]] or [[AudioStreamPlayer3D]] instead; this non-positional player is for anything that fills the screen uniformly. The `finished` signal is the standard way to know when a one-shot clip ends, useful for chaining sounds or advancing a sequence.

In the Kadi app an `AudioStreamPlayer` handles background music and global UI sounds: the soft "click" of a button, the "deal" whoosh, and the distinctive "Niko Kadi!" call sample. Each card-play routes its sound through the `SFX` bus so a master volume slider can mute effects without touching music. The `finished` signal on the win fanfare chains into the victory music track, so the celebration never has an awkward silent gap.

### Kadi & Downloaded Assets Example

> [!GODOT]
> **Godot 4.3 Inspector & Downloaded Assets Tip (`AudioStreamPlayer`)**: When configuring `AudioStreamPlayer` in the Godot 4.3 Inspector, hover any property name to read its built-in engine tooltip, or press **F1** to jump to `AudioStreamPlayer` offline. If your `AudioStreamPlayer` works with downloaded card PNGs (`res://assets/cards/`) or audio (`res://assets/audio/`), always verify paths with `ResourceLoader.exists(path)` so procedural fallbacks remain active while testing.

```gdscript
@onready var sfx_player: AudioStreamPlayer = $SfxPlayer

func play_downloaded_sfx(file_name: String) -> void:
    var path: String = "res://assets/audio/" + file_name
    if ResourceLoader.exists(path):
        sfx_player.stream = load(path)
        sfx_player.pitch_scale = randf_range(0.96, 1.04)
        sfx_player.play()
```

