package com.searchai.profile.setting.presentation.epoxy

import com.searchai.common.kotlinExtentions.helper.ViewBindingKotlinModel
import com.searchai.profile.R
import com.searchai.profile.databinding.SettingLogoutAndPaymentItemBinding
import com.searchai.profile.setting.domain.models.OtherSettings

class OtherSettingModel(
    private val otherSettings: OtherSettings,
    private val onClickCallback: (String) ->Unit
):ViewBindingKotlinModel<SettingLogoutAndPaymentItemBinding>(
    R.layout.setting_logout_and_payment_item) {

    override fun SettingLogoutAndPaymentItemBinding.bind() {
        tvOtherSettingTitle.text = otherSettings.title
        ivSettingImage.setImageResource(otherSettings.image)
        root.setOnClickListener {
            onClickCallback(otherSettings.title)
        }
    }
}