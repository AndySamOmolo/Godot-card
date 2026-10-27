# File: res://scenes/card/card_scene.gd
extends Control

signal card_clicked(card_node: Control)

@onready var card_bg: Panel = $CardBg
@onready var card_art: TextureRect = $CardArt
@onready var top_rank_label: Label = $TopRankLabel
@onready var center_suit_label: Label = $CenterSuitLabel
@onready var click_button: Button = $ClickButton

var data: CardData
var is_selected: bool = false

func _ready() -> void:
	click_button.pressed.connect(_on_click_button_pressed)
	if data != null:
		_refresh_visuals()
	else:
		setup(CardData.make(CardTypes.Suits.HEARTS, 7))

func setup(card_data: CardData) -> void:
	data = card_data
	if is_node_ready():
		_refresh_visuals()

func _refresh_visuals() -> void:
	if data == null:
		return
	var downloaded_tex: Texture2D = data.resolve_downloaded_texture()
	if downloaded_tex != null:
		card_art.texture = downloaded_tex
		card_art.visible = true
		top_rank_label.visible = false
		center_suit_label.visible = false
	else:
		card_art.visible = false
		top_rank_label.visible = true
		center_suit_label.visible = true
		top_rank_label.text = data.display_name
		center_suit_label.text = data.suit_symbol()
		var suit_color: Color = Color("#FF5A67") if data.is_red_suit() else Color("#ECF2F8")
		top_rank_label.modulate = suit_color
		center_suit_label.modulate = suit_color

func set_selected(selected: bool) -> void:
	is_selected = selected
	var lift: float = -14.0 if is_selected else 0.0
	card_bg.position.y = lift
	card_art.position.y = lift
	top_rank_label.position.y = -8.0 if is_selected else 6.0
	center_suit_label.position.y = lift

func _on_click_button_pressed() -> void:
	card_clicked.emit(self)
