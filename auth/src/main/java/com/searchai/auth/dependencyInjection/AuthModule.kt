package com.searchai.auth.dependencyInjection

import android.content.Context
import androidx.credentials.GetCredentialRequest
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.searchai.api.constants.auth.AuthConstants
import com.searchai.api.services.auth.AuthService
import com.searchai.auth.data.repository.AuthRepositoryImpl
import com.searchai.auth.domain.repository.AuthRepository
import com.searchai.auth.shared_preferences.AuthPreferencesManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AuthModule {
    @Provides
    @Singleton
    fun providesFirebase(): FirebaseAuth = Firebase.auth

    @Provides
    @Singleton
    fun providesAuthRepository(
        auth: FirebaseAuth, getCredentialRequest: GetCredentialRequest, authService: AuthService
    ): AuthRepository = AuthRepositoryImpl(auth, getCredentialRequest, authService)

    @Provides
    @Singleton
    fun providesGoogleClientRequest(): GetCredentialRequest {
        val googleIdOption = GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(false)
            .setServerClientId(AuthConstants.GOOGLE_WEB_CLIENT_ID)
            .setAutoSelectEnabled(false)
            .build()
        val request = GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()
        return request
    }

    @Provides
    @Singleton
    fun providesAuthPreferencesManager(@ApplicationContext context: Context): AuthPreferencesManager = AuthPreferencesManager(context)
}