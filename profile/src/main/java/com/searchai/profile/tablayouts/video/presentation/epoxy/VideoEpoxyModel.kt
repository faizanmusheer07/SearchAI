package com.searchai.profile.tablayouts.video.presentation.epoxy

import com.searchai.common.kotlinExtentions.helper.ViewBindingKotlinModel
import com.searchai.profile.R
import com.searchai.profile.databinding.SaveVideoItemBinding
import com.searchai.profile.tablayouts.video.domain.model.VideoModel
import com.squareup.picasso.Picasso

data class VideoEpoxyModel(
    val videoModel: VideoModel
):ViewBindingKotlinModel<SaveVideoItemBinding>(R.layout.save_video_item) {

    override fun SaveVideoItemBinding.bind() {
        hostname.text = videoModel.username
        title.text = videoModel.title
        category.text = videoModel.category
        Picasso.get()
            .load(videoModel.thumbnail)
            .error(com.searchai.common.R.drawable.ic_launcher_background)
            .fit()
            .centerInside()
            .into(gridImageContainer)
    }


}