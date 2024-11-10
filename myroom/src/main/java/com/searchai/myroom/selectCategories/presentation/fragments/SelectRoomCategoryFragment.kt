package com.searchai.myroom.selectCategories.presentation.fragments

import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.searchai.common.kotlinExtentions.base.bottomsheet.BaseBottomSheet
import com.searchai.common.kotlinExtentions.base.bottomsheet.BottomSheetLevelInterface
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipDrawable
import com.google.android.material.chip.ChipGroup
import com.searchai.api.utils.Resource
import com.searchai.common.kotlinExtentions.interestChip.epoxy.InterestsEpoxyController
import com.searchai.common.kotlinExtentions.interestChip.models.ListCategory
import com.searchai.myroom.R
import com.searchai.myroom.databinding.FragmentSelectRoomCategoryBinding
import com.searchai.myroom.selectCategories.domain.models.*
import com.searchai.myroom.selectCategories.presentation.viewmodels.SelectMyRoomCategoryViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
internal class SelectRoomCategoryFragment(
    private val levelInterface: BottomSheetLevelInterface
) : BaseBottomSheet<FragmentSelectRoomCategoryBinding>(
    R.layout.fragment_select_room_category,
    FragmentSelectRoomCategoryBinding::inflate
) {

    companion object {
        private const val TAG = "SelectRoomCategoryFragment"
    }


    private val selectMyRoomCategoryViewModel: SelectMyRoomCategoryViewModel by viewModels()
    private val myRoomCategoryEpoxyController by lazy {
        InterestsEpoxyController(context = requireContext())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        levelInterface.onSheet2Created()

        binding.doneButton.isEnabled = false
        binding.btnProgress.isVisible = true
        binding.doneButton.setOnClickListener { onClickNext() }

        (view.parent as View).setBackgroundColor(Color.TRANSPARENT)

        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                selectMyRoomCategoryViewModel.categories.collectLatest { categories ->
                    when (categories) {
                        is Resource.Error -> {
                            Log.d(TAG, "Error fetching categories: ${categories.message}")
                        }

                        is Resource.Success -> {
                            Log.d(TAG, "Categories loaded: ${categories.data}")
                            val categoryList = categories.data
                            if (categoryList != null) {
                                // Process each category
//                                processCategories(categoryList)
                            } else {
                                Log.d(TAG, "No categories found")
                            }
                        }

                        else -> {
                            Log.d(TAG, "From else block")
                        }
                    }
                }
            }
        }

        lifecycleScope.launch {
            // Next button must be enabled only when 5 or more items are selected
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                selectMyRoomCategoryViewModel.selectedSubCategories
                    .map { it.size >= 3 }
                    .collectLatest {
                        binding.doneButton.isEnabled = it
                    }
            }
        }
    }

    /**
     * Processes the categories by dividing the subcategories into three ChipGroups.
     * This method is responsible for creating the ListCategory objects which
     * contain three chip groups that will be passed into the Epoxy model.
     */
    private fun processCategories(categoryList: List<Category>) {
        lifecycleScope.launch {
            // Fetch subcategories for each category
            selectMyRoomCategoryViewModel.fetchSubCategoriesForAllCategories(categoryList.map { it.id })

            // Collect the results
            selectMyRoomCategoryViewModel.subCategories.collectLatest { result ->
                when (result) {
                    is Resource.Success -> {
                        val listSubCategories = result.data ?: emptyList()

                        // Process results and create ListCategory objects
                        val listCategories = categoryList.map { category ->
                            val listSub =
                                listSubCategories.firstOrNull { it.categoryName == category.name }
                            val subcategories = listSub?.subcategories ?: emptyList()
                            val (groupOne, groupTwo, groupThree) = createChipGroups(subcategories)
                            ListCategory(
                                category = category.name,
                                listFirstChipGroup = groupOne,
                                listSecondChipGroup = groupTwo,
                                listThirdChipGroup = groupThree
                            )
                        }.toMutableList()

                        // Submit the list of categories with their chip groups
                        withContext(Dispatchers.Main) {
                            Log.d(TAG, "Final categories with chips: $listCategories")
                            submitCategoriesChipsToController(listCategories)
                            binding.btnProgress.isGone = true
                        }
                    }

                    is Resource.Error -> {
                        Log.e(TAG, "Error fetching subcategories: ${result.message}")
                    }

                    is Resource.Loading -> {
                        Log.d(TAG, "Fetching subcategories...")
                    }

                    else -> {
                        // Handle other cases if necessary
                    }
                }
            }
        }
    }

    private fun createChipGroups(subcategories: List<SubCategory>): Triple<ChipGroup, ChipGroup, ChipGroup> {
        val groupOne = ChipGroup(requireContext())
        val groupTwo = ChipGroup(requireContext())
        val groupThree = ChipGroup(requireContext())

        val chunkSize = (subcategories.size / 3).let { chunk ->
            if (subcategories.size % 3 != 0) chunk + 1 else chunk
        }

        subcategories.forEachIndexed { index, subcategory ->
            val destination = when {
                index < chunkSize -> groupOne
                index < chunkSize * 2 -> groupTwo
                else -> groupThree
            }
            destination.addView(buildSubcategoryChip(subcategory))
        }

        return Triple(groupOne, groupTwo, groupThree)
    }

    private fun submitCategoriesChipsToController(subCategoryList: List<ListCategory>) {
        binding.epoxySuggestion.adapter = myRoomCategoryEpoxyController.adapter
        myRoomCategoryEpoxyController.setData(subCategoryList)
        myRoomCategoryEpoxyController.setFilterDuplicates(true)
    }

    private fun buildSubcategoryChip(subcategory: SubCategory): Chip {
        val chip = Chip(requireContext())
        val chipDrawable = ChipDrawable.createFromAttributes(
            requireContext(),
            null,
            0,
            R.style.MyworldChipStyle
        )
        chip.setChipDrawable(chipDrawable)
        chip.setTextAppearance(R.style.MyworldChipStyle)
        chip.text = subcategory.name
        chip.isCheckable = true

        // Initial state from viewModel
        chip.isChecked = subcategory in selectMyRoomCategoryViewModel.selectedSubCategories.value

        chip.setOnClickListener {
            if (chip.isChecked) {
                selectMyRoomCategoryViewModel.addToSelection(subcategory)
            } else {
                selectMyRoomCategoryViewModel.removeFromSelection(subcategory)
            }
        }

        return chip
    }

    private fun onClickNext() {
        /**send the data in create room here*/
//        findNavController().navigate(
//            R.id.action_interestsFragment_to_selectLanguageFragment
//        )
        Log.d(TAG,selectMyRoomCategoryViewModel.selectedSubCategories.toString())
        dismiss()
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        levelInterface.onSheet2Dismissed()
    }

    override fun onDestroy() {
        super.onDestroy()
    }

}