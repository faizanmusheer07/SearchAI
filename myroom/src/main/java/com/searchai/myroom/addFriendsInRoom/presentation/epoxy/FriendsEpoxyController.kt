package com.searchai.myroom.addFriendsInRoom.presentation.epoxy

import android.content.Context
import com.airbnb.epoxy.TypedEpoxyController
import com.searchai.myroom.addFriendsInRoom.domain.models.PreviousProfile

class FriendsEpoxyController(var id:String,var context: Context) : TypedEpoxyController<List<PreviousProfile>>() {
    override fun buildModels(data: List<PreviousProfile>) {
        data.forEach { myData ->
            FriendsEpoxyModel_(id,context)
                .id(myData.id)
                .data(myData)
                .addTo(this)
        }
    }
}