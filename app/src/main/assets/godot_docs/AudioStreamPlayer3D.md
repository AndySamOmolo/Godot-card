---
class: AudioStreamPlayer3D
inherits: Node3D
verified: 4.3
short: A 3D-positioned audio player with distance attenuation, panning, and doppler.
---

## Properties
- `stream: AudioStream` — The [[AudioStream]] resource to play.
- `volume_db: float` — Base volume in decibels before attenuation.
- `pitch_scale: float` — Pitch multiplier.
- `unit_size: float` — Reference distance at which the sound is at full volume.
- `max_db: float` — Maximum volume cap after attenuation.
- `max_distance: float` — Distance beyond which the sound is inaudible.
- `attenuation_model: AttenuationModel` — Inverse, inverse-square, logarithmic, or disabled.
- `emission_angle_enabled: bool` — When true, a directional cone is used.
- `emission_angle_degrees: float` — Cone half-angle.
- `emission_angle_filter_attenuation_db: float` — Attenuation outside the cone.
- `doppler_tracking: DopplerTracking` — Whether doppler shift is applied to moving emitters/listeners.
- `stream_paused: bool` — Pauses/resumes.
- `autoplay: bool` — When true, plays on `_ready`.
- `max_polyphony: int` — Maximum simultaneous voices.
- `bus: StringName` — Audio bus.
- `area_mask: int` — Bitmask of [[AudioListener3D]] areas affecting this player.
- (Transform inherited from [[Node3D]].)

## Methods
- `void play(from_position: float = 0.0)` — Starts playback.
- `void stop()` — Stops and resets.
- `void seek(to_position: float)` — Jumps to a position.
- `bool is_playing()` — True if currently playing.
- `float get_playback_position()` — Returns the current playback time.
- `AudioStreamPlayback get_stream_playback()` — Returns the playback object.
- `void set_panning_strength(panning: float)` — Overrides the computed pan amount.

## Signals
- `finished()` — Emitted when the stream ends.

## Description
`AudioStreamPlayer3D` is the fully-spatial audio node. As a [[Node3D]] it has a 3D position, and the audio is panned and attenuated based on the relative position of the active [[Camera3D]] (the listener). `attenuation_model` chooses the roll-off curve, `unit_size` is the distance at which the sound is at full volume, and `doppler_tracking` applies pitch shifts to moving emitters — the classic car-passing effect.

`emission_angle_*` makes a sound directional so it's quieter behind the emitter, useful for things like a directional speaker or a card-flip that should be heard only when the camera faces it. For non-positional sounds (music, UI), use [[AudioStreamPlayer]]; for 2D spatial, use [[AudioStreamPlayer2D]].

The Kadi app is 2D, so `AudioStreamPlayer3D` is documented here for learners who extend the project to a 3D table view. In that variant, each card node carries a small `AudioStreamPlayer3D` for its flip and place sounds; the [[Camera3D]]'s motion produces natural panning as it orbits, and `doppler_tracking` would pitch up the dealing swoosh as a card flies toward the camera. The same `bus` and `volume_db` patterns carry over from the 2D player unchanged.
