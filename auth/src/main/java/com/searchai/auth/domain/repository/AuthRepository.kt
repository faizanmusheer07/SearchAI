package com.searchai.auth.domain.repository

import android.content.Context
import com.google.firebase.auth.FirebaseUser
import com.searchai.api.models.auth.response.GoogleResponseBody
import com.searchai.api.utils.Resource
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    suspend fun googleSignIn(context: Context): Flow<Resource<GoogleResponseBody>>
    fun getCurrentUser(): FirebaseUser?
    suspend fun signOut()
}