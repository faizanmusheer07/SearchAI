package com.searchai.onboarding.interests.presentation.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipDrawable
import com.google.android.material.chip.ChipGroup
import com.google.android.material.transition.MaterialSharedAxis
import com.searchai.api.utils.Resource
import com.searchai.onboarding.R
import com.searchai.onboarding.databinding.FragmentInterestsBinding
import com.searchai.onboarding.interests.domain.models.Category
import com.searchai.onboarding.interests.domain.models.SubCategory
import com.searchai.common.kotlinExtentions.interestChip.epoxy.InterestsEpoxyController
import com.searchai.common.kotlinExtentions.interestChip.models.ListCategory
import com.searchai.onboarding.interests.presentation.viewmodels.InterestsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class InterestsFragment : Fragment() {
    companion object {
        private const val TAG = "InterestsFragment"
    }

    private var _binding: FragmentInterestsBinding? = null
    private val binding get() = _binding!!

    private val interestsViewModel: InterestsViewModel by viewModels()

    private val interestsEpoxyController by lazy {
        InterestsEpoxyController(context = requireContext())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enterTransition = MaterialSharedAxis(MaterialSharedAxis.Z, true)
        returnTransition = MaterialSharedAxis(MaterialSharedAxis.Z, false)
        exitTransition = MaterialSharedAxis(MaterialSharedAxis.Z, true)
        reenterTransition = MaterialSharedAxis(MaterialSharedAxis.Z, false)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentInterestsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnNext.isEnabled = false
        binding.progressBar.isVisible = true
        binding.btnNext.setOnClickListener { onClickNext() }

        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                interestsViewModel.categories.collectLatest { categories ->
                    when (categories) {
                        is Resource.Error -> {
                            Log.d(TAG, "Error fetching categories: ${categories.message}")
                        }

                        is Resource.Success -> {
                            Log.d(TAG, "Categories loaded: ${categories.data}")
                            val categoryList = categories.data
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

        lifecycleScope.launch {
            // Next button must be enabled only when 5 or more items are selected
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                interestsViewModel.selectedSubCategories
                    .map { it.size >= 5 }
                    .collectLatest {
                        binding.btnNext.isEnabled = it
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
            interestsViewModel.fetchSubCategoriesForAllCategories(categoryList.map { it.id })

            // Collect the results
            interestsViewModel.subCategories.collectLatest { result ->
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
                            binding.progressBar.isGone = true
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
        binding.epoxyInterest.adapter = interestsEpoxyController.adapter
        interestsEpoxyController.setData(subCategoryList)
        interestsEpoxyController.setFilterDuplicates(true)
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

    private fun onClickNext() {
        lifecycleScope.launch {
            interestsViewModel.submitOnboardingSelection()
            Log.d(TAG, "Interest Fragment ${interestsViewModel.onboardingSubmissionStatus}")
        }
        findNavController().navigate(
            R.id.action_interestsFragment_to_selectLanguageFragment
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
