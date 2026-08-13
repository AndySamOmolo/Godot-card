package com.kadicompanion.di

import com.kadicompanion.data.content.ContentLoader
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import javax.inject.Singleton
import android.content.Context

@Module
@InstallIn(SingletonComponent::class)
object ContentModule {
    @OptIn(ExperimentalSerializationApi::class)
    @Provides
    @Singleton
    fun provideJson(): Json = Json {
        ignoreUnknownKeys = true
        explicitNulls = false
        prettyPrint = false
    }

    @Provides
    @Singleton
    fun provideContentLoader(@ApplicationContext context: Context, json: Json): ContentLoader =
        ContentLoader(context, json)
}
