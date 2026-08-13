package com.kadicompanion.data.local.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.kadicompanion.data.local.db.entity.DebugChallengeAttemptEntity
import com.kadicompanion.data.local.db.entity.QuizAttemptEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface QuizAttemptDao {
    @Query("SELECT * FROM quiz_attempts ORDER BY attemptedAt DESC")
    fun observeAll(): Flow<List<QuizAttemptEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: QuizAttemptEntity)
}

@Dao
interface DebugChallengeAttemptDao {
    @Query("SELECT * FROM debug_challenge_attempts")
    fun observeAll(): Flow<List<DebugChallengeAttemptEntity>>

    @Query("SELECT * FROM debug_challenge_attempts WHERE challengeId = :challengeId")
    suspend fun get(challengeId: String): DebugChallengeAttemptEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(entity: DebugChallengeAttemptEntity)
}
