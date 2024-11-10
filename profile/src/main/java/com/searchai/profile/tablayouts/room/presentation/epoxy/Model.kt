package com.searchai.profile.tablayouts.room.presentation.epoxy

import com.searchai.common.kotlinExtentions.helper.ViewBindingKotlinModel
import com.searchai.profile.R
import com.searchai.profile.databinding.UserRoomListItemBinding
import com.searchai.profile.tablayouts.video.domain.model.VideoModel
import com.squareup.picasso.Picasso

data class Model(
    val videoModel: VideoModel
):ViewBindingKotlinModel<UserRoomListItemBinding>(R.layout.user_room_list_item) {
    override fun UserRoomListItemBinding.bind() {

        tvHostName.text = videoModel.username
        upcomingRoomTitlep.text = videoModel.title
        upcomingRoomHostTimep.text = videoModel.filename
        upcomingRoomTimep.text = videoModel.category
        Picasso.get()
            .load(videoModel.thumbnail)
            .error(com.searchai.common.R.drawable.ic_launcher_background)
            .fit()
            .centerInside()
            .into(samplepoxyviewp)
    }


}