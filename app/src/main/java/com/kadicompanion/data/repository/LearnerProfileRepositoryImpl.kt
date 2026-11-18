package com.kadicompanion.data.repository

import com.kadicompanion.data.local.datastore.PreferencesRepository
import com.kadicompanion.data.local.db.dao.LearnerProfileDao
import com.kadicompanion.data.local.db.entity.LearnerProfileEntity
import com.kadicompanion.domain.model.LearnerTrack
import com.kadicompanion.domain.repository.LearnerProfileRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LearnerProfileRepositoryImpl @Inject constructor(
    private val dao: LearnerProfileDao,
    private val prefs: PreferencesRepository
) : LearnerProfileRepository {

    override fun observeTrack(): Flow<LearnerTrack?> = combine(
        prefs.observeLearnerTrack(),
        dao.observe()
    ) { prefTrack, entity ->
        prefTrack ?: entity?.track?.let { name -> runCatching { LearnerTrack.valueOf(name) }.getOrNull() }
    }

    override suspend fun getTrack(): LearnerTrack? = dao.get()?.track?.let { name ->
        runCatching { LearnerTrack.valueOf(name) }.getOrNull()
    }

    override suspend fun setTrack(track: LearnerTrack) {
        prefs.setLearnerTrack(track)
        val existing = dao.get()
        dao.upsert(
            LearnerProfileEntity(
                id = 0,
                track = track.name,
                onboardedAt = existing?.onboardedAt ?: System.currentTimeMillis()
            )
        )
    }

    override suspend fun isOnboarded(): Boolean = dao.get() != null

    override suspend fun markOnboarded() {
        val existing = dao.get()
        val resolvedTrack = existing?.track?.let { runCatching { LearnerTrack.valueOf(it) }.getOrNull() }
            ?: LearnerTrack.FOUNDATIONS
        prefs.setLearnerTrack(resolvedTrack)
        dao.upsert(
            LearnerProfileEntity(
                id = 0,
                track = resolvedTrack.name,
                onboardedAt = existing?.onboardedAt ?: System.currentTimeMillis()
            )
        )
    }

    override suspend fun clearProfile() {
        prefs.clearLearnerTrack()
        dao.deleteAll()
    }
}
