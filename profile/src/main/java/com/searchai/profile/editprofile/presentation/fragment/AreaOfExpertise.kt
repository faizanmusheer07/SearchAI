package com.searchai.profile.editprofile.presentation.fragment

import android.app.Dialog
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import androidx.core.view.isGone
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipDrawable
import com.google.android.material.chip.ChipGroup
import com.searchai.api.utils.Resource
import com.searchai.common.kotlinExtentions.BaseBottomSheet
import com.searchai.common.kotlinExtentions.constants.BottomSheetConst
import com.searchai.common.kotlinExtentions.interestChip.models.Category
import com.searchai.common.kotlinExtentions.interestChip.models.ListCategory
import com.searchai.common.kotlinExtentions.interestChip.models.SubCategory
import com.searchai.profile.R
import com.searchai.profile.databinding.FragmentAreaOfExpertiseChipSelectionBinding
import com.searchai.profile.editprofile.presentation.viewmodel.ChipViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class AreaOfExpertise:BaseBottomSheet<FragmentAreaOfExpertiseChipSelectionBinding>(
    R.layout.fragment_area_of_expertise_chip_selection,
    FragmentAreaOfExpertiseChipSelectionBinding::bind
) {

    companion object {
        const val TAG = "EditProfile"
    }

    var count = 0
    private var categoryList:List<Category>? = null

    private val interestsViewModel by viewModels<ChipViewModel>()


    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)

        dialog.setOnShowListener {
            val bottomSheetDialog = it as BottomSheetDialog
            setUpFullScreen(bottomSheetDialog, BottomSheetConst.settingBottomSheetHeight)
        }
        (dialog as BottomSheetDialog).behavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                if (newState == BottomSheetBehavior.STATE_SETTLING) {
                    count++
                    if (count % 2 == 0) {
                        dialog.dismiss()
                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) = Unit

        })
        dialog.behavior.isDraggable = false

        return dialog
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        addCategoryObserver()

        // Set a listener for category selection
        binding.categoryChipGroup.setOnCheckedChangeListener { group, checkedId ->
            val chip = binding.root.findViewById<Chip>(checkedId)
            val selectedCategory = chip?.text?.toString()

            // Find the selected category in the viewModel and fetch its subcategories
            selectedCategory?.let {
                // Find the category in the ViewModel and get subcategories for it
                categoryList?.firstOrNull { category ->
                    category.name == selectedCategory
                }?.let { category ->
                    // Update subcategory chips based on the selected category
                    processSubcategories(category.id)
                }
            }
        }

    }

    private fun addCategoryObserver() {
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                interestsViewModel.categories.collectLatest { categories ->
                    when (categories) {
                        is Resource.Error -> {
                            Log.d(TAG, "Error fetching categories: ${categories.message}")
                        }

                        is Resource.Success -> {
                            Log.d(TAG, "Categories loaded: ${categories.data}")
                             categoryList = categories.data
                            if (categoryList != null) {
                                // Process each category
                                processCategories(categoryList)
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
    }

    private fun processSubcategories(categoryId: String) {
        lifecycleScope.launch {
            interestsViewModel.subCategories.collectLatest { resource ->
                when (resource) {
                    is Resource.Success -> {
                        val listSubCategories = resource.data?.firstOrNull { it.id == categoryId }?.subcategories
                        listSubCategories?.let { subcategories ->
                            // Clear existing subcategory chips
                            binding.subcategoryChipGroup.removeAllViews()

                            // Create new chips for the subcategories
                            subcategories.forEach { subCategory ->
                                val chip = buildSubcategoryChip(subCategory)
                                binding.subcategoryChipGroup.addView(chip)
                            }
                        }
                    }
                    is Resource.Error -> {
                        Log.e(TAG, "Error fetching subcategories: ${resource.message}")
                    }
                    else -> {
                        Log.d(TAG, "Loading subcategories...")
                    }
                }
            }
        }
    }

    // Function to dynamically populate category chips
    private fun processCategories(categoryList: List<Category>?) {
        if (categoryList != null) {
            for (category in categoryList) {
                val chip = Chip(requireContext())
                val chipDrawable = ChipDrawable.createFromAttributes(
                    requireContext(),
                    null,
                    0,
                    com.searchai.common.R.style.MyworldChipStyle
                )
                chip.setChipDrawable(chipDrawable)
                chip.setTextAppearance(com.searchai.common.R.style.MyworldChipStyle)
                chip.text = category.name
                chip.isCheckable = true
                binding.categoryChipGroup.addView(chip)
            }
        }
    }

    // Function to populate subcategory chips based on selected category
    private fun populateSubcategoryChips(chip:Chip) {
        binding.subcategoryChipGroup.removeAllViews()
//        selectedCategory?.let {
//            val subcategoryList = subcategories[it]
//            subcategoryList?.forEach { subcategory ->
//                val chip = Chip(requireContext())
//                chip.text = subcategory
//                chip.isCheckable = true
//
//            }
//
//        }
        binding.subcategoryChipGroup.addView(chip)
    }

    private fun buildSubcategoryChip(subcategory: SubCategory): Chip {
        val chip = Chip(requireContext())
        val chipDrawable = ChipDrawable.createFromAttributes(
            requireContext(),
            null,
            0,
            com.searchai.common.R.style.MyworldChipStyle
        )
        chip.setChipDrawable(chipDrawable)
        chip.setTextAppearance(com.searchai.common.R.style.MyworldChipStyle)
        chip.text = subcategory.name
        chip.isCheckable = true

        // Initial state from viewModel
        chip.isChecked = subcategory in interestsViewModel.selectedSubCategories.value

        chip.setOnClickListener {
            if (chip.isChecked) {
                interestsViewModel.addToSelection(subcategory)
            } else {
                interestsViewModel.removeFromSelection(subcategory)
            }
        }

        return chip
    }




}