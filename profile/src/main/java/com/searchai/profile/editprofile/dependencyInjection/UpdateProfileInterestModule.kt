package com.searchai.profile.editprofile.dependencyInjection

import android.app.Application
import com.searchai.api.services.interests.InterestSubmissionService
import com.searchai.api.services.interests.InterestsService
import com.searchai.profile.editprofile.data.remote.InterestDatastore
import com.searchai.profile.editprofile.domain.service.ChipRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UpdateProfileInterestModule {
    @Provides
    @Singleton
    fun provideOnboardingRepository(
        interestsService: InterestsService,
        interestSubmissionService: InterestSubmissionService,
        application: Application  // Inject the Application context
    ): ChipRepository {
        return InterestDatastore(interestsService,interestSubmissionService, application)
    }
}