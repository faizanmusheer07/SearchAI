package com.searchai.common.kotlinExtentions.interestChip.adapter


import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.ChipGroup
import com.searchai.common.R

class InterestsAdapter : RecyclerView.Adapter<CategoryViewHolder>() {

    //differCallback object
    private val differCallBack = object : DiffUtil.ItemCallback<ChipGroup>() {
        override fun areItemsTheSame(oldItem: ChipGroup, newItem: ChipGroup): Boolean {
            return oldItem.id == newItem.id
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(oldItem: ChipGroup, newItem: ChipGroup): Boolean {
            return oldItem == newItem
        }

    }

    val differ = AsyncListDiffer(this, differCallBack)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        return CategoryViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.chip_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val category = differ.currentList[position]
        // Clear existing views
        holder.chipGroup.removeAllViews()
        // Add new views
        (category.parent as? ViewGroup)?.removeView(category)
        holder.chipGroup.addView(category)
    }

    override fun getItemCount() = differ.currentList.size

}

class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val chipGroup = itemView.findViewById<ChipGroup>(R.id.chip)!!
}
