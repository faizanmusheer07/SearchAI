package com.searchai.profile.followandfollower.presentation.epoxy

import com.searchai.common.kotlinExtentions.helper.ViewBindingKotlinModel
import com.searchai.profile.R
import com.searchai.profile.databinding.FollowerFollowingItemBinding
import com.searchai.profile.followandfollower.domain.model.FollowItemModels
import com.squareup.picasso.Picasso

data class FollowerEpoxyModel(
    private val followItemModels: FollowItemModels,
    private val onClickCallBack : (FollowItemModels) -> Unit
) : ViewBindingKotlinModel<FollowerFollowingItemBinding>(R.layout.follower_following_item) {

    override fun FollowerFollowingItemBinding.bind() {
        Picasso.get().load(followItemModels.profile_picture).error(com.searchai.common.R.drawable.ic_user_defolt_avator).into(userImage)
        userNameSearch.text = followItemModels.name
        userChannelSearch.text = followItemModels.channel_name

        root.setOnClickListener {
            onClickCallBack(followItemModels)
        }
    }
}

