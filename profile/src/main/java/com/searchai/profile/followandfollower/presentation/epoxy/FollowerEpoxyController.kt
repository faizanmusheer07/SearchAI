package com.searchai.profile.followandfollower.presentation.epoxy

import com.airbnb.epoxy.EpoxyController
import com.google.android.gms.common.util.CollectionUtils
import com.searchai.profile.followandfollower.domain.model.FollowItemModels

class FollowerEpoxyController(
    private val onClickCallBack: (FollowItemModels) -> Unit
) :EpoxyController() {


    private var isLoading:Boolean = false
        set(value){
            field = value
            if(field){
                requestModelBuild()
            }
        }

    var followFollowing = CollectionUtils.listOf<FollowItemModels>()
        set(value){
            field = value
            isLoading = false
            requestModelBuild()
        }

    override fun buildModels() {
        followFollowing.forEach{ data->
            FollowerEpoxyModel(
                data,onClickCallBack)
                .id(data.id)
                .addTo(this)
        }
    }

}