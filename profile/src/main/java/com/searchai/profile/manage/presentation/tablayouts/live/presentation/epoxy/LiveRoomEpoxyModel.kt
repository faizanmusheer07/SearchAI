package com.searchai.profile.manage.presentation.tablayouts.live.presentation.epoxy

import com.searchai.common.kotlinExtentions.helper.ViewBindingKotlinModel
import com.searchai.profile.R
import com.searchai.profile.commonModel.Room
import com.searchai.profile.databinding.ProfileVideoItemBinding
import com.squareup.picasso.Picasso

data class LiveRoomEpoxyModel(
    val room:Room,
    private val onClickCallback:(Room) -> Unit
):ViewBindingKotlinModel<ProfileVideoItemBinding>(R.layout.profile_video_item){

    override fun ProfileVideoItemBinding.bind() {

        txtVideoTitle.text = room.title
        txtVideoViewcount.text = room.listeners.count().toString()
        txtCategory.text = room.category

        Picasso.get()
            .load(room.creator.profilePicture)
            .error(com.searchai.common.R.drawable.ic_user_defolt_avator)
            .fit()
            .centerInside()
            .into(ivThumbnail)

        root.setOnClickListener {
            onClickCallback(room)
        }
    }


}