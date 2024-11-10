package com.searchai.common.kotlinExtentions.interestChip.epoxy

import android.content.Context
import android.util.Log
import com.airbnb.epoxy.TypedEpoxyController
import com.searchai.common.kotlinExtentions.interestChip.models.ListCategory


class InterestsEpoxyController(
    val context: Context
) : TypedEpoxyController<List<ListCategory>>() {

    companion object {
        private const val TAG = "InterestsEpoxyController"
    }

    // Handle any exceptions that Epoxy swallows by default
    override fun onExceptionSwallowed(exception: RuntimeException) {
        super.onExceptionSwallowed(exception)
        Log.d(TAG, "Exception swallowed: $exception")
    }

    /**
     * Builds the Epoxy models for the UI by looping through each ListCategory object
     * and generating a model for it.
     */
    override fun buildModels(data: List<ListCategory>) {
        data.forEachIndexed { index, listCategory ->
            interestsCategory {
                id("category_$index") // Unique ID for the model
                subCategoryHeading(listCategory.category)
                chipGroupOne(listCategory.listFirstChipGroup)
                chipGroupTwo(listCategory.listSecondChipGroup)
                chipGroupThree(listCategory.listThirdChipGroup)
            }
        }
    }
}
