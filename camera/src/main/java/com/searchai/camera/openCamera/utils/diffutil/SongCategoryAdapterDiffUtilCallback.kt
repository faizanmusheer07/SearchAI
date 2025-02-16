package com.searchai.camera.openCamera.utils.diffutil

import android.os.Bundle
import androidx.annotation.Nullable
import androidx.recyclerview.widget.DiffUtil
import com.searchai.common.models.cameraModels.Audio


class SongCategoryAdapterDiffUtilCallback(newList: ArrayList<Audio>?, oldList: ArrayList<Audio>) :
    DiffUtil.Callback() {

    var newList: ArrayList<Audio>? = newList
    var oldList: ArrayList<Audio>? = oldList

    override fun getOldListSize(): Int {
        return if (oldList != null) oldList!!.size else 0
    }

    override fun getNewListSize(): Int {
        return if (newList != null) newList!!.size else 0
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return newList!![newItemPosition].category === oldList!![oldItemPosition].category
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val result: Int = newList!![newItemPosition].compareTo(oldList!![oldItemPosition])
        return result == 0
    }

    @Nullable
    override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? {
        val newModel = newList!![newItemPosition]
        val oldModel = oldList!![oldItemPosition]
        val diff = Bundle()
        if (newModel.category !== oldModel.category) {
            diff.putString("category name", newModel.category)
        }
        return if (diff.size() == 0) {
            null
        } else diff
    }

}
