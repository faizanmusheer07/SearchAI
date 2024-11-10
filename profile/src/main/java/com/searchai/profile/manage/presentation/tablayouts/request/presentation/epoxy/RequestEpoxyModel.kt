package com.searchai.profile.manage.presentation.tablayouts.request.presentation.epoxy

import com.searchai.common.kotlinExtentions.helper.ViewBindingKotlinModel
import com.searchai.profile.R
import com.searchai.profile.databinding.ListItemRequestBinding
import com.searchai.profile.manage.presentation.tablayouts.request.domain.model.RequestModel
import com.squareup.picasso.Picasso

data class RequestEpoxyModel(
    private val request:RequestModel,
    private val onClickCallback:(RequestModel) -> Unit
):ViewBindingKotlinModel<ListItemRequestBinding>(R.layout.list_item_request) {

    override fun ListItemRequestBinding.bind() {
        userNameSearch.text = request.userName
        userChannelSearch.text = request.channelName
        Picasso.get()
            .load(request.userImage)
            .error(com.searchai.common.R.drawable.ic_user_defolt_avator)
            .fit()
            .centerInside()
            .into(userImage)

        chipCheck.setOnClickListener {
            onClickCallback(request)
        }
    }


}