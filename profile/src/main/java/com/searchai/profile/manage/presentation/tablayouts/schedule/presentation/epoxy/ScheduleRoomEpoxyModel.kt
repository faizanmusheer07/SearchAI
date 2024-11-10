package com.searchai.profile.manage.presentation.tablayouts.schedule.presentation.epoxy

import com.searchai.common.kotlinExtentions.helper.ViewBindingKotlinModel
import com.searchai.profile.R
import com.searchai.profile.commonModel.Room
import com.searchai.profile.databinding.ListItemRoomBinding

data class ScheduleRoomEpoxyModel(
    val room: Room,
    private val onClickCallback:(Room) -> Unit
):ViewBindingKotlinModel<ListItemRoomBinding>(R.layout.list_item_room) {

    override fun ListItemRoomBinding.bind() {
        roomName.text = room.title
        userName.text = room.admin

        btnGoLive.setOnClickListener {
            onClickCallback(room)
        }
    }


}