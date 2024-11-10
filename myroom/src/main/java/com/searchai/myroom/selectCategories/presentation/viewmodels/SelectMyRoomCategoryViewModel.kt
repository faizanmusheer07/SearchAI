package com.searchai.myroom.selectCategories.presentation.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.searchai.api.utils.Resource
import com.searchai.myroom.selectCategories.domain.models.*
import com.searchai.myroom.selectCategories.domain.repository.MyRoomRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import javax.inject.Inject

class SelectMyRoomCategoryViewModel @Inject constructor(
    private val repository: MyRoomRepository
) :ViewModel() {
    companion object {
        private const val TAG = "SelectMyRoomCategoryViewModel"
    }

    private val _categories = MutableStateFlow<Resource<List<Category>>>(Resource.Idle)
    val categories: StateFlow<Resource<List<Category>>> = _categories

    private val _subCategories = MutableStateFlow<Resource<List<ListSub>>>(Resource.Idle)
    val subCategories: StateFlow<Resource<List<ListSub>>> = _subCategories

    private val _selectedSubCategories = MutableStateFlow<Set<SubCategory>>(emptySet())
    val selectedSubCategories: StateFlow<Set<SubCategory>> = _selectedSubCategories

    init {
        fetchCategories()
    }

    private fun fetchCategories() {
        viewModelScope.launch {
            repository.getCategories().collect { result ->
                _categories.value = result
            }
        }
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

    fun addToSelection(subcategory: SubCategory) {
        _selectedSubCategories.value += subcategory
    }

    fun removeFromSelection(subcategory: SubCategory) {
        _selectedSubCategories.value -= subcategory
    }
}