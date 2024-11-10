package com.searchai.profile.tablayouts.video.presentation.epoxy

import com.airbnb.epoxy.EpoxyController
import com.google.android.gms.common.util.CollectionUtils
import com.searchai.profile.tablayouts.video.domain.model.VideoModel

class VideoController:EpoxyController() {

    var isLoading:Boolean = false
        set(value){
            field = value
            if(field){
                requestModelBuild()
            }
        }

    var videoData = CollectionUtils.listOf<VideoModel>()
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

        if (videoData.isEmpty()){
            //todo show empty state
            return
        }

        videoData.forEach{ data->
            VideoEpoxyModel(data)
                .id(data.userid)
                .addTo(this)
        }
    }

}