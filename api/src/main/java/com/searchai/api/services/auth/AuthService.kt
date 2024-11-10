package com.searchai.api.services.auth

import com.searchai.api.constants.auth.AuthConstants
import com.searchai.api.headers.ApiHeaders
import com.searchai.api.models.auth.request.GoogleRequestBody
import com.searchai.api.models.auth.response.GoogleResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface AuthService {
    @Headers(ApiHeaders.NO_AUTH_HEADER)
    @POST(AuthConstants.GOOGLE_ENDPOINT)
    suspend fun googleSignIn(
        @Body requestBody: GoogleRequestBody
    ): Response<GoogleResponseBody>
}