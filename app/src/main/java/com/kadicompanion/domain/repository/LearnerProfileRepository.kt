package com.kadicompanion.domain.repository

import com.kadicompanion.domain.model.LearnerTrack
import kotlinx.coroutines.flow.Flow

interface LearnerProfileRepository {
    fun observeTrack(): Flow<LearnerTrack?>
    suspend fun getTrack(): LearnerTrack?
    suspend fun setTrack(track: LearnerTrack)
    suspend fun isOnboarded(): Boolean
    suspend fun markOnboarded()
}
