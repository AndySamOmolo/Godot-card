package com.kadicompanion.di

import com.kadicompanion.data.repository.AssessmentRepositoryImpl
import com.kadicompanion.data.repository.DocsRepositoryImpl
import com.kadicompanion.data.repository.GlossaryRepositoryImpl
import com.kadicompanion.data.repository.LearnerProfileRepositoryImpl
import com.kadicompanion.data.repository.ProgressRepositoryImpl
import com.kadicompanion.data.repository.TutorialRepositoryImpl
import com.kadicompanion.domain.repository.AssessmentRepository
import com.kadicompanion.domain.repository.DocsRepository
import com.kadicompanion.domain.repository.GlossaryRepository
import com.kadicompanion.domain.repository.LearnerProfileRepository
import com.kadicompanion.domain.repository.ProgressRepository
import com.kadicompanion.domain.repository.TutorialRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds @Singleton abstract fun bindTutorial(impl: TutorialRepositoryImpl): TutorialRepository
    @Binds @Singleton abstract fun bindDocs(impl: DocsRepositoryImpl): DocsRepository
    @Binds @Singleton abstract fun bindProgress(impl: ProgressRepositoryImpl): ProgressRepository
    @Binds @Singleton abstract fun bindAssessment(impl: AssessmentRepositoryImpl): AssessmentRepository
    @Binds @Singleton abstract fun bindGlossary(impl: GlossaryRepositoryImpl): GlossaryRepository
    @Binds @Singleton abstract fun bindLearnerProfile(impl: LearnerProfileRepositoryImpl): LearnerProfileRepository
}
