package com.kadicompanion.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class GodotDocEntry(
    val className: String,
    val inherits: String?,
    val shortDescription: String,
    val properties: List<DocMember>,
    val methods: List<DocMember>,
    val signals: List<DocMember>,
    val bodyMarkdown: String,
    val lastVerifiedAgainstGodotVersion: String
)

@Serializable
data class DocMember(
    val name: String,
    val type: String?,
    val description: String
)
