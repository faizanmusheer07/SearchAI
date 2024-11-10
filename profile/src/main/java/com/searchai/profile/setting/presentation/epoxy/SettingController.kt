package com.searchai.profile.setting.presentation.epoxy

import com.airbnb.epoxy.EpoxyController
import com.google.android.gms.common.util.CollectionUtils
import com.searchai.profile.setting.domain.models.EarningModel
import com.searchai.profile.setting.domain.models.OtherSettings
import com.searchai.profile.setting.domain.models.SwitchModel
import com.searchai.profile.setting.domain.models.TextModel

class SettingController(
    private val onClickCallback:(String) -> Unit
):EpoxyController() {

    var textSetting = CollectionUtils.listOf<TextModel>()
        set(value){
            field = value
            requestModelBuild()
        }

    var switchSetting = CollectionUtils.listOf<SwitchModel>()
        set(value){
            field = value
            requestModelBuild()
        }

    var paymentCount = CollectionUtils.listOf<EarningModel>()
        set(value){
            field = value
            requestModelBuild()
        }

    var otherSetting = CollectionUtils.listOf<OtherSettings>()
        set(value){
            field = value
            requestModelBuild()
        }

    override fun buildModels() {
        paymentCount.forEach { data->
            PaymentModel(data)
                .id(data.id)
                .addTo(this)
        }

        textSetting.forEach{ data->
            SettingTextModel(data,onClickCallback)
                .id(data.id)
                .addTo(this)
        }

        switchSetting.forEach { data->
            SettingSwitchModel(data,onClickCallback)
                .id(data.id)
                .addTo(this)
        }

        otherSetting.forEach { data->
            OtherSettingModel(data,onClickCallback)
                .id(data.id)
                .addTo(this)
        }
    }

}