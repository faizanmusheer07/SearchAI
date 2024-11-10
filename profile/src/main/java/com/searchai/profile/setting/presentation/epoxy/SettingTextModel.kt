package com.searchai.profile.setting.presentation.epoxy

import com.searchai.common.kotlinExtentions.helper.ViewBindingKotlinModel
import com.searchai.profile.R
import com.searchai.profile.databinding.MenuTextItemBinding
import com.searchai.profile.setting.domain.models.TextModel

data class SettingTextModel(
    val textModel: TextModel,
    val onClickCallback: (String) -> Unit
):ViewBindingKotlinModel<MenuTextItemBinding>(R.layout.menu_text_item) {

    override fun MenuTextItemBinding.bind() {
        tvMenuTitle.text = textModel.title

        root.setOnClickListener {
            onClickCallback(textModel.title)
        }
    }


}