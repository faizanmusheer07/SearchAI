package com.searchai.api.services.explore.model.body

import com.google.gson.annotations.SerializedName

data class RegisterAuthUserBody(
    @SerializedName(value = "device_id") val uuid: String,
)
