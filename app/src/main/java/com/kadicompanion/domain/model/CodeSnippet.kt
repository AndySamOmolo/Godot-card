package com.kadicompanion.domain.model

data class CodeSnippet(
    val id: String,
    val language: String = "gdscript",
    val code: String,
    val caption: String? = null
)
