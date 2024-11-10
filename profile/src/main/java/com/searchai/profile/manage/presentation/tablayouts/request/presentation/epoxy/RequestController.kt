package com.searchai.profile.manage.presentation.tablayouts.request.presentation.epoxy

import com.airbnb.epoxy.EpoxyController
import com.google.android.gms.common.util.CollectionUtils
import com.searchai.profile.manage.presentation.tablayouts.request.domain.model.RequestModel

class RequestController(
    private val onClickCallback:(RequestModel)->Unit
):EpoxyController() {
    var isLoading:Boolean = false
        set(value){
            field = value
            if(field){
                requestModelBuild()
            }
        }

    var request = CollectionUtils.listOf<RequestModel>()
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

        if (request.isEmpty()){
            //todo show empty state
            return
        }

        request.forEach{ data->
            RequestEpoxyModel(
                data,
                onClickCallback = onClickCallback)
                .id(data.id)
                .addTo(this)
        }
    }

}