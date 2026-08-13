package com.kadicompanion.data.local.db.entity

import androidx.room.Entity
import androidx.room.Fts4
import androidx.room.PrimaryKey

@Entity(tableName = "doc_cache")
data class GodotDocEntity(
    @PrimaryKey val className: String,
    val json: String
)

@Fts4
@Entity(tableName = "doc_cache_fts")
data class GodotDocFtsEntity(
    val className: String,
    val searchableText: String
)
