package com.searchai.onboarding.interests.presentation.viewmodels

import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.searchai.api.utils.Resource
import com.searchai.onboarding.interests.domain.models.Branch
import com.searchai.onboarding.interests.domain.models.Category
import com.searchai.onboarding.interests.domain.models.ListSub
import com.searchai.onboarding.interests.domain.models.SubCategory
import com.searchai.onboarding.interests.domain.repository.InterestsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class InterestsViewModel @Inject constructor(
    private val repository: InterestsRepository
) : ViewModel() {

    companion object {
        private const val TAG = "InterestsViewModel"
    }

    private val _branches = MutableStateFlow<Resource<List<Branch>>>(Resource.Idle)
    val branches: StateFlow<Resource<List<Branch>>> = _branches

    private val branch = "66c05c114d6b06b6a7055282"

    private val _categories = MutableStateFlow<Resource<List<Category>>>(Resource.Idle)
    val categories: StateFlow<Resource<List<Category>>> = _categories

    private val _subCategories = MutableStateFlow<Resource<List<ListSub>>>(Resource.Idle)
    val subCategories: StateFlow<Resource<List<ListSub>>> = _subCategories

    private val _selectedSubCategories = MutableStateFlow<Set<SubCategory>>(emptySet())
    val selectedSubCategories: StateFlow<Set<SubCategory>> = _selectedSubCategories

    // New state flow to handle onboarding submission
    private val _onboardingSubmissionStatus = MutableStateFlow<Resource<Boolean>>(Resource.Idle)
    val onboardingSubmissionStatus: StateFlow<Resource<Boolean>> = _onboardingSubmissionStatus

    private lateinit var listSub: List<ListSub>

    init {
        fetchCategoryBranches()
        fetchCategories()
        initSublist()
    }

    private fun fetchCategoryBranches() {
        viewModelScope.launch {
            repository.getCategoryBranches().collect { result ->
                _branches.value = result
            }
        }
    }

    private fun fetchCategories() {
        viewModelScope.launch {
            repository.getCategories().collect { result ->
                _categories.value = result
            }
        }
    }

    fun addToSelection(subcategory: SubCategory) {
        _selectedSubCategories.value += subcategory
    }

    fun removeFromSelection(subcategory: SubCategory) {
        _selectedSubCategories.value -= subcategory
    }

    fun fetchSubCategoriesForAllCategories(categoryIds: List<String>) {
        viewModelScope.launch {
            val results = categoryIds.map { categoryId ->
                async {
                    repository.getSubCategories(categoryId)
                        .firstOrNull { it is Resource.Success } as? Resource.Success
                }
            }.awaitAll()

            // Combine the results into ListSub objects
            val listSubCategories = results.mapNotNull { result ->
                when (result) {
                    is Resource.Success -> {
                        ListSub(
                            id = result.data?.firstOrNull()?.id ?: "",
                            categoryName = result.data?.firstOrNull()?.categoryName ?: "",
                            subcategories = result.data?.flatMap { it.subcategories } ?: emptyList()
                        )
                    }

                    else -> {
                        Log.d(TAG, "from fetchSubCategoriesForAllCategories else block")
                        null
                    }
                }
            }
            _subCategories.value = Resource.Success(listSubCategories)
        }
    }


    suspend fun submitOnboardingSelection(): Resource<String> {
        val language = AppCompatDelegate.getApplicationLocales()[0]?.language
            ?: Locale.getDefault().language // fallback to default
            ?: "en"

        val subcategoryIds: List<String> = _selectedSubCategories.value.map { it.id }

        val categoryMap = createCategoryMap(branch, subcategoryIds, listSub)

        try {
            // Launch a coroutine to collect the flow from the repository
            var resultResource: Resource<String> = Resource.Idle

            // Collect the flow emitted by the repository
            repository.submitOnBoardingSelection(language.toString(), categoryMap)
                .collectLatest { resource ->
                    when (resource) {
                        is Resource.Success -> {
                            val message = resource.data ?: ""
                            Log.d(
                                TAG,
                                "On success submitOnboardingSelection ${resource.toString()}"
                            )
                            resultResource = Resource.Success(message)
                        }

                        is Resource.Error -> {
                            Log.e(TAG, "Failed submitOnboardingSelection: ${resource.message}")
                            resultResource = Resource.Error(resource.message)
                        }

                        else -> {
                            Log.e(TAG, "Unknown state submitOnboardingSelection: ${resource}")
                        }
                    }
                }

            return resultResource

        } catch (e: Exception) {
            Log.e(TAG, "Error submitting onboarding selection", e)
            return Resource.Error(e.message)
        }
    }


    private fun createCategoryMap(
        branch: String,
        subcategoryIds: List<String>,
        listSubs: List<ListSub>
    ): Map<String, Map<String, List<String>>> {
        val categoryMap = mutableMapOf<String, MutableMap<String, MutableList<String>>>()
        // Iterate through each subcategory ID
        subcategoryIds.forEach { subcategoryId ->
            // Find the category that the subcategory belongs to
            val categoryId = findCategoryForSubcategory(subcategoryId, listSubs)

            if (categoryId != null) {
                // Get or create the inner map for the fixed branch
                val subcategoryMap = categoryMap.getOrPut(branch) { mutableMapOf() }

                // Get or create the list of subcategory IDs for the category
                val subcategoryList = subcategoryMap.getOrPut(categoryId) { mutableListOf() }

                // Add the subcategory ID to the list
                subcategoryList.add(subcategoryId)
            }
        }

//        Log.d(TAG, categoryMap.toString())
        return categoryMap
    }

    /**this function find category of subcategories*/
    private fun findCategoryForSubcategory(
        subcategoryId: String,
        listSubs: List<ListSub>
    ): String? {
        for (listSub in listSubs) {
            if (listSub.subcategories.any { it.id == subcategoryId }) {
                return listSub.id // Return the category id (listSub id)
            }
        }
        return null
    }

    private fun initSublist() {
        viewModelScope.launch {
            subCategories.collect { resource ->
                when (resource) {
                    is Resource.Success -> {
                        listSub = resource.data ?: emptyList()
                    }

                    else -> {
                        Log.d(TAG, "from initSublist else block")
                    }
                }
            }
        }
    }

    /**
     * Mark current selection as final
     */
    suspend fun finalizeCategories() {
        val selectedCategoryIds = selectedSubCategories.value
            .asIterable()
            .map { it.id }
            .toSet()

        val selectedSubCategoryIds = selectedSubCategories.value
            .asIterable()
            .map { it.id }
            .toSet()

//        videoCategoryRepository.setUserSelectedSubcategoryIds(selectedSubCategoryIds)
//
//        registrationRepository.registerCategories(
//            categories = selectedCategoryIds,
//            sub_categories = selectedSubCategoryIds,
//        )
    }
}
