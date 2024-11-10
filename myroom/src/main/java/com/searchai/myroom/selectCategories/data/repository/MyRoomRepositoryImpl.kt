package com.searchai.myroom.selectCategories.data.repository

import android.util.Log
import com.searchai.api.extensions.retrofitCallFlow
import com.searchai.api.services.myroom.MyRoomService
import com.searchai.api.utils.Resource
import com.searchai.myroom.selectCategories.domain.models.Category
import com.searchai.myroom.selectCategories.domain.models.ListSub
import com.searchai.myroom.selectCategories.domain.models.SubCategory
import com.searchai.myroom.selectCategories.domain.repository.MyRoomRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

class MyRoomRepositoryImpl @Inject constructor(
    private val myRoomService: MyRoomService
): MyRoomRepository {

    override suspend fun getCategories(): Flow<Resource<List<Category>>> = channelFlow {
        retrofitCallFlow(
            body = {
                myRoomService.getCategories()
            },
            tag = "getCategories"
        ).collectLatest { resource ->
            when (resource) {
                is Resource.Success -> {
                    val categories = resource.data?.flatMap { branch ->
                        branch.category.map { category ->
                            Category(
                                id = category.id,
                                name = category.categoryName
                            )
                        }
                    }
                    Log.d("getCategories", "getCategories: $categories")
                    send(Resource.Success(categories))
                }

                is Resource.Error -> {
                    send(Resource.Error(resource.message))
                }

                is Resource.Loading -> {
                    send(Resource.Loading)
                }

                is Resource.Idle -> {
                    send(Resource.Idle)
                }
            }
        }
    }

    override suspend fun getSubCategories(categoryId: String): Flow<Resource<List<ListSub>>> =
        channelFlow {
            retrofitCallFlow(
                body = {
                    myRoomService.getSubCategories(categoryId = categoryId)
                },
                tag = "getSubCategories"
            ).collectLatest { resource ->
                when (resource) {
                    is Resource.Success -> {
//                    val subCategories = resource.data?.category?.get(0)?.subcategories?.map { subCategory ->
//                        SubCategory(
//                            id = subCategory.id,
//                            name = subCategory.subCategoryName,
//                        )
//                    }
                        val sub = resource.data?.category?.map { subCat ->
                            val listSub = subCat.subcategories.map { apiSubCategory ->
                                SubCategory(
                                    id = apiSubCategory.id,
                                    name = apiSubCategory.subCategoryName,
                                )
                            }

                            // Now create ListSub using the mapped subcategories
                            ListSub(
                                id = subCat.id,
                                categoryName = subCat.categoryName,
                                subcategories = listSub
                            )
                        }
                        Log.d("getSubCategories", "getSubCategories: $sub")
                        send(Resource.Success(sub))
                    }

                    is Resource.Error -> {
                        send(Resource.Error(resource.message))
                    }

                    is Resource.Loading -> {
                        send(Resource.Loading)
                    }

                    is Resource.Idle -> {
                        send(Resource.Idle)
                    }
                }
            }
        }
}