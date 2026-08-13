package com.kadicompanion.data.local.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.kadicompanion.data.local.db.entity.GodotDocEntity
import com.kadicompanion.data.local.db.entity.GodotDocFtsEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface GodotDocDao {
    @Query("SELECT * FROM doc_cache")
    fun observeAll(): Flow<List<GodotDocEntity>>

    @Query("SELECT * FROM doc_cache WHERE className = :className")
    suspend fun get(className: String): GodotDocEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<GodotDocEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFts(items: List<GodotDocFtsEntity>)

    @Query("DELETE FROM doc_cache")
    suspend fun clearAll()

    @Query("DELETE FROM doc_cache_fts")
    suspend fun clearFts()

    @Query("SELECT doc_cache.className FROM doc_cache_fts JOIN doc_cache ON doc_cache_fts.className = doc_cache.className WHERE doc_cache_fts MATCH :query ORDER BY doc_cache.className")
    suspend fun search(query: String): List<String>
}
