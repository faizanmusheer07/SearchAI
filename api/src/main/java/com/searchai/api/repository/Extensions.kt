package com.searchai.api.repository

import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

internal fun File.createMultipartBody(formName: String, contentType: String): MultipartBody.Part? {
    if (!exists()) return null

    return MultipartBody.Part.createFormData(
        name = formName,
        filename = name,
        body = asRequestBody(contentType.toMediaTypeOrNull()),
    )
}
