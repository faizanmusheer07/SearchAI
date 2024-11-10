package com.searchai.common.kotlinExtentions.interestChip.epoxy

import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.searchai.common.kotlinExtentions.base.epoxy.KotlinEpoxyHolder
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.google.android.material.chip.ChipGroup
import com.searchai.common.R
import com.searchai.common.kotlinExtentions.interestChip.adapter.InterestsAdapter


@EpoxyModelClass
abstract class InterestsCategoryModel : EpoxyModelWithHolder<ItemVH>() {

    @EpoxyAttribute
    lateinit var subCategoryHeading: String

    @EpoxyAttribute
    lateinit var chipGroupOne: ChipGroup

    @EpoxyAttribute
    lateinit var chipGroupTwo: ChipGroup

    @EpoxyAttribute
    lateinit var chipGroupThree: ChipGroup

    private lateinit var adapter: InterestsAdapter
    private lateinit var recyclerView: RecyclerView

    override fun getDefaultLayout() = R.layout.view_holder_interest_categories

    override fun bind(view: ItemVH) {
        super.bind(view)
        setUpRecyclerView(view.recyclerView1)
        val list: List<ChipGroup> = listOf(chipGroupOne, chipGroupTwo, chipGroupThree)
        adapter.differ.submitList(list)
        view.subHeading.text = subCategoryHeading
    }

    private fun setUpRecyclerView(recyclerView1: RecyclerView) {
        adapter = InterestsAdapter()
        recyclerView = recyclerView1
        recyclerView.adapter = adapter
        recyclerView.layoutManager = StaggeredGridLayoutManager(3, LinearLayoutManager.HORIZONTAL)
    }
}

class ItemVH : KotlinEpoxyHolder() {
    val subHeading by bind<TextView>(R.id.category_heading)
    val recyclerView1 by bind<RecyclerView>(R.id.recyclerview)
}