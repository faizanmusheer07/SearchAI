package com.searchai.auth.data.repository

import android.content.Context
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialCancellationException
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.searchai.api.extensions.retrofitCallFlow
import com.searchai.api.models.auth.request.GoogleRequestBody
import com.searchai.api.models.auth.response.GoogleResponseBody
import com.searchai.api.services.auth.AuthService
import com.searchai.api.utils.Resource
import com.searchai.auth.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth,
    private val googleClientRequest: GetCredentialRequest,
    private val authService: AuthService
): AuthRepository{
    override suspend fun googleSignIn(context: Context): Flow<Resource<GoogleResponseBody>> = retrofitCallFlow(
        body = {
            val credentialManager = CredentialManager.create(context)
            val request = googleClientRequest
            val result = credentialManager.getCredential(
                context = context,
                request = request
            )
            val credential = result.credential
            val googleIdToken = GoogleIdTokenCredential.createFrom(credential.data).idToken
            val firebaseCredential = GoogleAuthProvider.getCredential(googleIdToken, null)
            auth.signInWithCredential(firebaseCredential).await()
            val authResult = authService.googleSignIn(GoogleRequestBody(googleIdToken))
            authResult
        },
        tag = "GoogleSignIn"
    )

    override fun getCurrentUser(): FirebaseUser? = auth.currentUser

    override suspend fun signOut() = auth.signOut()

}