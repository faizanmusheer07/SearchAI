package com.searchai.profile.manage.presentation.tablayouts.live.presentation.epoxy

import com.airbnb.epoxy.EpoxyController
import com.google.android.gms.common.util.CollectionUtils
import com.searchai.profile.commonModel.Room
import com.searchai.profile.tablayouts.video.domain.model.VideoModel
import com.searchai.profile.tablayouts.video.presentation.epoxy.VideoEpoxyModel

class LiveRoomController(
    private val onClickCallback: (Room) -> Unit
): EpoxyController() {

    var isLoading:Boolean = false
        set(value){
            field = value
            if(field){
                requestModelBuild()
            }
        }

    var room = CollectionUtils.listOf<Room>()
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

        if (room.isEmpty()){
            //todo show empty state
            return
        }

        room.forEach{ data->
            LiveRoomEpoxyModel(
                data,
                onClickCallback = onClickCallback)
                .id(data.id)
                .addTo(this)
        }
    }

}