package com.searchai.profile.editprofile.data.remote

import android.annotation.SuppressLint
import android.app.Application
import android.provider.Settings
import android.util.Log
import com.searchai.api.extensions.retrofitCallFlow
import com.searchai.api.models.interests.postrequest.OnBoardingSubmission
import com.searchai.api.services.interests.InterestSubmissionService
import com.searchai.api.services.interests.InterestsService
import com.searchai.api.utils.Resource
import com.searchai.common.kotlinExtentions.interestChip.models.Branch
import com.searchai.common.kotlinExtentions.interestChip.models.Category
import com.searchai.common.kotlinExtentions.interestChip.models.ListSub
import com.searchai.common.kotlinExtentions.interestChip.models.SubCategory
import com.searchai.profile.editprofile.domain.service.ChipRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

class InterestDatastore @Inject constructor(
    private val interestsService: InterestsService,
    private val interestSubmissionService: InterestSubmissionService,
    application: Application,
) : ChipRepository{
    override suspend fun getCategoryBranches(): Flow<Resource<List<Branch>>> = channelFlow {
        retrofitCallFlow(
            body = {
                interestsService.getCategoryBranches()
            },
            tag = "getCategoryBranches"
        ).collectLatest { resource ->
            when (resource) {
                is Resource.Success -> {
                    val branches = resource.data?.map { branch ->
                        Branch(
                            id = branch.id,
                            name = branch.branchId
                        )
                    }
                    Log.d("getCategoryBranches", "getCategoryBranches: $branches")
                    send(Resource.Success(branches))
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

    override suspend fun getCategories(): Flow<Resource<List<Category>>> = channelFlow {
        retrofitCallFlow(
            body = {
                interestsService.getCategories()
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
                    interestsService.getSubCategories(categoryId = categoryId)
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

    @SuppressLint("HardwareIds")
    private val deviceId = Settings.Secure.getString(
        application.contentResolver,
        Settings.Secure.ANDROID_ID
    )

    override suspend fun submitOnBoardingSelection(
        language: String,
        categories: Map<String, Map<String, List<String>>>
    ): Flow<Resource<String>> = channelFlow {

        val submission = OnBoardingSubmission(
            deviceId = deviceId,
            language = language,
            categories = categories
        )
        Log.d("submitOnBoardingSelection", "Submission: $submission")

        // Call the Retrofit service and collect the result
        retrofitCallFlow(
            body = { interestSubmissionService.submitOnBoardingSelection(submission) },
            tag = "submitOnBoardingSelection"
        ).collectLatest { resource ->
            when (resource) {
                is Resource.Success -> {
                    val response = resource.data
                    val resultMessage = response?.message ?: "Unknown success response"
                    Log.d("submitOnBoardingSelection", "Success: $resultMessage")
                    send(Resource.Success(resultMessage))
                }

                is Resource.Error -> {
                    Log.e("submitOnBoardingSelection", "Error: ${resource.message}")
                    send(Resource.Error(resource.message ?: "Unknown error occurred"))
                }

                is Resource.Loading -> {
                    send(Resource.Loading)
                }

                else -> {
                    Log.e("submitOnBoardingSelection", "Unhandled state: $resource")
                }
            }
        }
    }
}
