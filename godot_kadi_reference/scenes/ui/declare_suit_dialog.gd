# File: res://scenes/ui/declare_suit_dialog.gd
class_name DeclareSuitDialog
extends Control

signal suit_declared(suit: int)

@onready var option_button: OptionButton = $Panel/OptionButton
@onready var confirm_button: Button = $Panel/ConfirmButton

func _ready() -> void:
	option_button.clear()
	option_button.add_item("♥ Hearts", CardTypes.Suits.HEARTS)
	option_button.add_item("♦ Diamonds", CardTypes.Suits.DIAMONDS)
	option_button.add_item("♣ Clubs", CardTypes.Suits.CLUBS)
	option_button.add_item("♠ Spades", CardTypes.Suits.SPADES)
	confirm_button.pressed.connect(_on_confirm)
	hide()

func _on_confirm() -> void:
	var chosen_suit: int = option_button.get_selected_id()
	hide()
	suit_declared.emit(chosen_suit)
