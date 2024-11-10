package com.searchai.profile.tablayouts.room.presentation.epoxy

import com.airbnb.epoxy.EpoxyController
import com.google.android.gms.common.util.CollectionUtils
import com.searchai.profile.tablayouts.video.domain.model.VideoModel
import com.searchai.profile.tablayouts.video.presentation.epoxy.VideoEpoxyModel

class Controller:EpoxyController() {

    var isLoading:Boolean = false
        set(value){
            field = value
            if(field){
                requestModelBuild()
            }
        }

    var roomData = CollectionUtils.listOf<VideoModel>()
        set(value){
            field = value
            isLoading = false
            requestModelBuild()
        }

    override fun buildModels() {
        if (isLoading){
            //todo loading state
            return
        }

        if (roomData.isEmpty()){
            //todo show empty state
            return
        }

        roomData.forEach{ data->
            Model(data)
                .id(data.userid)
                .addTo(this)
        }
    }

}