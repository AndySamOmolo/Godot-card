# File: res://scripts/turn_manager.gd
# Autoload Singleton: TurnManager
extends Node

signal turn_changed(player_index: int)
signal direction_changed(new_direction: int)

var current_player_index: int = 0
var direction: int = 1
var player_count: int = 4

func reset_turns(num_players: int = 4) -> void:
	player_count = num_players
	current_player_index = 0
	direction = 1
	turn_changed.emit(current_player_index)

func advance_turn(extra_skips: int = 0) -> void:
	var total_steps: int = 1 + extra_skips
	for _i in range(total_steps):
		current_player_index = (current_player_index + direction + player_count) % player_count
	turn_changed.emit(current_player_index)

func reverse_direction() -> void:
	direction *= -1
	direction_changed.emit(direction)
