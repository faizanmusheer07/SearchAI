package com.searchai.common.kotlinExtentions.interestChip.models

import com.google.android.material.chip.ChipGroup

data class ListCategory(
    val category : String,
    val listFirstChipGroup : ChipGroup,
    val listSecondChipGroup: ChipGroup,
    val listThirdChipGroup: ChipGroup
)
