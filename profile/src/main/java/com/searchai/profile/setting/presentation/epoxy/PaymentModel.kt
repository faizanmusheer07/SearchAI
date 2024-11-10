package com.searchai.profile.setting.presentation.epoxy

import com.searchai.common.kotlinExtentions.helper.ViewBindingKotlinModel
import com.searchai.profile.R
import com.searchai.profile.databinding.SettingEarningsItemsBinding
import com.searchai.profile.setting.domain.models.EarningModel

class PaymentModel(
    private val earningModel: EarningModel
):ViewBindingKotlinModel<SettingEarningsItemsBinding>(
    R.layout.setting_earnings_items) {

    override fun SettingEarningsItemsBinding.bind() {
        hubCount.text = earningModel.hub.toString()
        tvProfileViews.text = earningModel.viewProfileCount.toString()
        tvEarnings.text = earningModel.revenue.toString()
    }


}