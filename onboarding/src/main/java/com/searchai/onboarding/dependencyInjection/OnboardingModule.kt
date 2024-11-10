package com.searchai.onboarding.dependencyInjection

import android.app.Application
import com.searchai.api.services.interests.InterestSubmissionService
import com.searchai.api.services.interests.InterestsService
import com.searchai.onboarding.interests.data.repository.InterestsRepositoryImpl
import com.searchai.onboarding.interests.domain.repository.InterestsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object OnboardingModule {
    @Provides
    @Singleton
    fun provideOnboardingRepository(
        interestsService: InterestsService,
        interestSubmissionService: InterestSubmissionService,
        application: Application  // Inject the Application context
    ): InterestsRepository {
        return InterestsRepositoryImpl(interestsService,interestSubmissionService, application)
    }
}