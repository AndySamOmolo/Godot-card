---
class: AudioStreamPlayer2D
inherits: Node2D
verified: 4.3
short: A 2D-positioned audio player whose volume attenuates with distance from the Camera2D.
---

## Properties
- `stream: AudioStream` — The [[AudioStream]] resource to play.
- `volume_db: float` — Base volume in decibels before attenuation.
- `pitch_scale: float` — Pitch multiplier.
- `playing: bool` — True when audio is playing.
- `autoplay: bool` — When true, plays on `_ready`.
- `stream_paused: bool` — Pauses/resumes without resetting.
- `max_distance: float` — Distance beyond which the sound is inaudible.
- `attenuation: float` — Roll-off factor; higher values drop off faster with distance.
- `max_polyphony: int` — Maximum simultaneous overlapping voices.
- `bus: StringName` — The audio bus to route through.
- `area_mask: int` — Bitmask controlling which [[AudioListener2D]] areas affect this player.
- `emission_angle_enabled: bool` — When true, an emission cone is used.
- `emission_angle_degrees: float` — Cone half-angle in degrees.
- `emission_angle_filter_attenuation_db: float` — Volume reduction outside the cone.
- (Position/rotation inherited from [[Node2D]].)

## Methods
- `void play(from_position: float = 0.0)` — Starts playback.
- `void stop()` — Stops and resets.
- `void seek(to_position: float)` — Jumps to a position.
- `bool is_playing()` — True if currently playing.
- `float get_playback_position()` — Returns the current playback time.
- `AudioStreamPlayback get_stream_playback()` — Returns the playback object.

## Signals
- `finished()` — Emitted when the stream ends.

## Description
`AudioStreamPlayer2D` is the spatial 2D counterpart of [[AudioStreamPlayer]]. As a [[Node2D]] it has a position, and its perceived volume attenuates with distance from the active listener (the [[Camera2D]] acts as the listener by default) following `attenuation` and clamped by `max_distance`. This gives sound a sense of place: a card dropped on the left of the table is louder in the left ear-ish sense of "where on screen" (Godot's 2D audio is mono-ish, but distance still applies).

`emission_angle_*` properties let the sound be directional, quieter behind the emitter — useful for things like a directional speaker. For purely global sounds (music, UI) prefer the non-positional player; for anything that originates at a point on the table, `AudioStreamPlayer2D` adds presence.

In the Kadi app each card snap is played through an `AudioStreamPlayer2D` parented to the played card node, so a card dropped at the right of the table sounds closer than one dropped far away — a subtle but satisfying cue. The penalty-draw sound is attached to the drawing player's area and ramps in `volume_db` as the accumulated penalty grows. A distant `AudioStreamPlayer2D` on the draw pile plays a soft "shuffle" rustle during `l_10_01_reshuffle`.
