package com.kadicompanion.data.local.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.kadicompanion.data.local.db.entity.LearnerProfileEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface LearnerProfileDao {
    @Query("SELECT * FROM learner_profile WHERE id = 0")
    fun observe(): Flow<LearnerProfileEntity?>

    @Query("SELECT * FROM learner_profile WHERE id = 0")
    suspend fun get(): LearnerProfileEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(entity: LearnerProfileEntity)
}
