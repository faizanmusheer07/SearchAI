package com.searchai.onboarding.interests.domain.repository

import com.searchai.api.utils.Resource
import com.searchai.onboarding.interests.domain.models.Branch
import com.searchai.onboarding.interests.domain.models.Category
import com.searchai.onboarding.interests.domain.models.ListSub
import kotlinx.coroutines.flow.Flow

interface InterestsRepository {
    suspend fun getCategoryBranches(): Flow<Resource<List<Branch>>>
    suspend fun getCategories(): Flow<Resource<List<Category>>>
    suspend fun getSubCategories(categoryId: String): Flow<Resource<List<ListSub>>>
    suspend fun submitOnBoardingSelection(
        language: String,
        categories: Map<String, Map<String, List<String>>>
    ): Flow<Resource<String>>
}