package com.kadicompanion.data.local.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.kadicompanion.domain.model.LearnerTrack
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "kadi_prefs")

enum class ThemePref { DARK, LIGHT, SYSTEM }

@Singleton
class PreferencesRepository @Inject constructor(
    @dagger.hilt.android.qualifiers.ApplicationContext private val context: Context
) {
    fun observeTheme(): Flow<ThemePref> = context.dataStore.data.map {
        ThemePref.valueOf(it[KEY_THEME] ?: ThemePref.DARK.name)
    }
    suspend fun setTheme(theme: ThemePref) {
        context.dataStore.edit { it[KEY_THEME] = theme.name }
    }
    // Default to the hand-tuned Kadi brand palette; Material You stays available in Settings.
    fun observeDynamicColor(): Flow<Boolean> = context.dataStore.data.map {
        it[KEY_DYNAMIC_COLOR] ?: false
    }
    suspend fun setDynamicColor(enabled: Boolean) {
        context.dataStore.edit { it[KEY_DYNAMIC_COLOR] = enabled }
    }
    fun observeReduceMotion(): Flow<Boolean> = context.dataStore.data.map {
        it[KEY_REDUCE_MOTION] ?: false
    }
    suspend fun setReduceMotion(enabled: Boolean) {
        context.dataStore.edit { it[KEY_REDUCE_MOTION] = enabled }
    }
    fun observeDyslexiaFont(): Flow<Boolean> = context.dataStore.data.map {
        it[KEY_DYSLEXIA_FONT] ?: false
    }
    suspend fun setDyslexiaFont(enabled: Boolean) {
        context.dataStore.edit { it[KEY_DYSLEXIA_FONT] = enabled }
    }
    fun observeSkipGating(): Flow<Boolean> = context.dataStore.data.map {
        it[KEY_SKIP_GATING] ?: false
    }
    suspend fun setSkipGating(enabled: Boolean) {
        context.dataStore.edit { it[KEY_SKIP_GATING] = enabled }
    }
    fun observeContentVersion(): Flow<String?> = context.dataStore.data.map {
        it[KEY_CONTENT_VERSION]
    }
    suspend fun setContentVersion(version: String) {
        context.dataStore.edit { it[KEY_CONTENT_VERSION] = version }
    }
    fun observeLearnerTrack(): Flow<LearnerTrack?> = context.dataStore.data.map {
        it[KEY_TRACK]?.let { name -> runCatching { LearnerTrack.valueOf(name) }.getOrNull() }
    }
    suspend fun setLearnerTrack(track: LearnerTrack) {
        context.dataStore.edit { it[KEY_TRACK] = track.name }
    }
    companion object {
        private val KEY_THEME = stringPreferencesKey("theme")
        private val KEY_DYNAMIC_COLOR = booleanPreferencesKey("dynamic_color")
        private val KEY_REDUCE_MOTION = booleanPreferencesKey("reduce_motion")
        private val KEY_DYSLEXIA_FONT = booleanPreferencesKey("dyslexia_font")
        private val KEY_SKIP_GATING = booleanPreferencesKey("skip_gating")
        private val KEY_CONTENT_VERSION = stringPreferencesKey("content_version")
        private val KEY_TRACK = stringPreferencesKey("track")
    }
}
