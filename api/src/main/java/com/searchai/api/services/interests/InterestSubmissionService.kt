package com.searchai.api.services.interests

import com.searchai.api.constants.interests.InterestsConstants
import com.searchai.api.headers.ApiHeaders
import com.searchai.api.models.interests.postrequest.OnBoardingSubmission
import com.searchai.api.models.interests.response.OnBoardingSubmissionResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface InterestSubmissionService {
    @Headers(ApiHeaders.NO_AUTH_HEADER)
    @POST(InterestsConstants.ONBOARD_SUBMISSION)
    suspend fun submitOnBoardingSelection(
        @Body submission: OnBoardingSubmission
    ): Response<OnBoardingSubmissionResponse>
}