package com.searchai.explore.data.repository

import android.util.Log
import com.searchai.api.services.explore.ExploreService
import com.searchai.api.services.explore.model.body.RegisterAuthUserBody
import com.searchai.api.services.explore.model.body.TokenBody
import com.searchai.api.services.explore.model.body.UserRegistrationBody
import com.searchai.common.models.basicModels.Category
import com.searchai.common.preferences.UserIdentifierPreferences
import javax.inject.Inject

class RegistrationRepository @Inject constructor(
    private val exploreService: ExploreService,
    private val userIdentifierPreferences: UserIdentifierPreferences,
) {
    suspend fun registerCategories(categories: Set<String>, sub_categories: Set<String>) {
        Log.d("RegisteringUser",userIdentifierPreferences.uuid)
        exploreService.registerCategory(
            UserRegistrationBody(userIdentifierPreferences.uuid, categories,sub_categories)
        )
    }

    suspend fun registerAuthUser() {
       val result = exploreService.registerCategory(
            RegisterAuthUserBody(userIdentifierPreferences.uuid)
        )
        Log.d("resultyehwalla",result.toString())
    }

    suspend fun registerNotification(token: String) {
        exploreService.registerNotification(TokenBody(token))
    }

    suspend fun getVideoCategories() : Category {
        return exploreService.getVideoCategories()
    }

    suspend fun registerCategoriesAfterLogin(userId:String, categories: Set<String>, sub_categories: Set<String>){
        Log.e("RegisteringLoggedInUser",userId)
        exploreService.registerCategory(
            UserRegistrationBody(userId, categories,sub_categories)
        )
    }
}
