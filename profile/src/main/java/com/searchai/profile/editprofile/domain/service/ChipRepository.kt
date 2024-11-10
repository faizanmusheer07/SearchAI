package com.searchai.profile.editprofile.domain.service

import com.searchai.api.utils.Resource
import com.searchai.common.kotlinExtentions.interestChip.models.Branch
import com.searchai.common.kotlinExtentions.interestChip.models.Category
import com.searchai.common.kotlinExtentions.interestChip.models.ListSub
import kotlinx.coroutines.flow.Flow

interface ChipRepository {
    suspend fun getCategoryBranches(): Flow<Resource<List<Branch>>>
    suspend fun getCategories(): Flow<Resource<List<Category>>>
    suspend fun getSubCategories(categoryId: String): Flow<Resource<List<ListSub>>>
    suspend fun submitOnBoardingSelection(
        language: String,
        categories: Map<String, Map<String, List<String>>>
    ): Flow<Resource<String>>
}