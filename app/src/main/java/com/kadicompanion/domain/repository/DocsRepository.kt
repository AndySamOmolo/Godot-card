package com.kadicompanion.domain.repository

import com.kadicompanion.domain.model.GodotDocEntry
import kotlinx.coroutines.flow.Flow

interface DocsRepository {
    fun observeAll(): Flow<List<GodotDocEntry>>
    suspend fun get(className: String): GodotDocEntry?
    suspend fun search(query: String): List<String>
    suspend fun reindexIfNeeded()
}
