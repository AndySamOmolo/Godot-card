package com.kadicompanion.domain.repository

import com.kadicompanion.domain.model.GlossaryEntry
import kotlinx.coroutines.flow.Flow

interface GlossaryRepository {
    fun observeAll(): Flow<List<GlossaryEntry>>
    suspend fun get(term: String): GlossaryEntry?
    suspend fun search(query: String): List<GlossaryEntry>
}
