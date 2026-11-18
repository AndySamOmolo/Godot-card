# File: res://scripts/save_manager.gd
# Autoload Singleton: SaveManager
extends Node

signal profile_updated(profile: Dictionary)

const SAVE_PATH: String = "user://kadi_profile.json"
const TMP_SAVE_PATH: String = "user://kadi_profile.json.tmp"
const ALLOWED_INT_KEYS: Array[String] = ["wins", "matches", "streak", "high_score_streak", "coins"]

var profile: Dictionary = {
	"wins": 0,
	"matches": 0,
	"streak": 0,
	"high_score_streak": 0,
	"coins": 100
}

func _ready() -> void:
	load_profile()

func save_profile() -> void:
	var file := FileAccess.open(TMP_SAVE_PATH, FileAccess.WRITE)
	if file != null:
		file.store_string(JSON.stringify(profile, "\t"))
		file.close()
		DirAccess.rename_absolute(TMP_SAVE_PATH, SAVE_PATH)
		profile_updated.emit(profile)

func load_profile() -> void:
	if not FileAccess.file_exists(SAVE_PATH):
		return
	var raw_text: String = FileAccess.get_file_as_string(SAVE_PATH)
	var parsed: Variant = JSON.parse_string(raw_text)
	if parsed is Dictionary:
		for key in ALLOWED_INT_KEYS:
			if parsed.has(key):
				var val_raw: Variant = parsed[key]
				if val_raw is int or val_raw is float:
					profile[key] = maxi(0, int(val_raw))
		profile_updated.emit(profile)

func reset_profile() -> void:
	profile = {
		"wins": 0,
		"matches": 0,
		"streak": 0,
		"high_score_streak": 0,
		"coins": 100
	}
	if FileAccess.file_exists(SAVE_PATH):
		DirAccess.remove_absolute(SAVE_PATH)
	profile_updated.emit(profile)

func record_match_result(human_won: bool) -> void:
	profile["matches"] = int(profile.get("matches", 0)) + 1
	if human_won:
		profile["wins"] = int(profile.get("wins", 0)) + 1
		profile["streak"] = int(profile.get("streak", 0)) + 1
		profile["coins"] = int(profile.get("coins", 100)) + 50
		if int(profile["streak"]) > int(profile.get("high_score_streak", 0)):
			profile["high_score_streak"] = profile["streak"]
	else:
		profile["streak"] = 0
	save_profile()

func get_summary_text() -> String:
	return "Wins: %d/%d | Streak: %d | Coins: %d" % [
		int(profile.get("wins", 0)),
		int(profile.get("matches", 0)),
		int(profile.get("streak", 0)),
		int(profile.get("coins", 100))
	]
