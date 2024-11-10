package com.searchai.camera.openCamera.presentation.epoxy

import android.view.View
import android.view.ViewParent
import androidx.databinding.ViewDataBinding
import com.airbnb.epoxy.DataBindingEpoxyModel
import com.searchai.camera.R
import com.searchai.camera.databinding.GalleryVideoRecyclerRowBinding

class GalleryVideoRecyclerRow(
    private var videoPath: String,
    private var onVideoClickListener: () -> Unit
) : DataBindingEpoxyModel()  {


    override fun getDefaultLayout() =  R.layout.gallery_video_recycler_row

    override fun setDataBindingVariables(binding: ViewDataBinding?) {
        // Ensure that the binding is of the correct type before setting variables.
        if (binding is GalleryVideoRecyclerRowBinding) {
            binding.setGalleryVideo(videoPath)
            binding.setOnVideoClicked(View.OnClickListener { onVideoClickListener() })
        }
    }
}
