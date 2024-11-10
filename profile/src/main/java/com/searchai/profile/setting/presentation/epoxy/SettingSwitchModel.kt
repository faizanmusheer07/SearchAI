package com.searchai.profile.setting.presentation.epoxy

import com.searchai.common.kotlinExtentions.helper.ViewBindingKotlinModel
import com.searchai.profile.R
import com.searchai.profile.databinding.MenuSwitchItemBinding
import com.searchai.profile.setting.domain.models.SwitchModel

class SettingSwitchModel(
    private val switchModel: SwitchModel,
    val onClickCallback: (String) -> Unit
):ViewBindingKotlinModel<MenuSwitchItemBinding>(R.layout.menu_switch_item) {

    override fun MenuSwitchItemBinding.bind() {
        tvSwitchSettingTitle.text = switchModel.title
        tvSwitchHelper.text = switchModel.helperText

        root.setOnClickListener {
            onClickCallback(switchModel.title)
        }
    }

}