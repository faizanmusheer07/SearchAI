package com.searchai.myroom.selectCategories.domain.repository

import com.searchai.api.utils.Resource
import com.searchai.myroom.selectCategories.domain.models.Category
import com.searchai.myroom.selectCategories.domain.models.ListSub
import kotlinx.coroutines.flow.Flow

interface MyRoomRepository {
//    suspend fun getCategoryBranches(): Flow<Resource<List<Branch>>>
    suspend fun getCategories(): Flow<Resource<List<Category>>>
    suspend fun getSubCategories(categoryId: String): Flow<Resource<List<ListSub>>>
}