package com.searchai.api.extensions

import com.squareup.moshi.Moshi
import okhttp3.ResponseBody
import retrofit2.Response
import javax.inject.Inject


private val moshi: Moshi = Moshi.Builder().build()

private val ResponseBody.message: String
    get() = runCatching {
        moshi.adapter(ErrorBody::class.java).fromJson(string())!!.message
    }.getOrDefault(defaultValue = "Error!")

data class ErrorBody(
    val message: String,
)

fun <T> Response<T>.getOrThrow(): T {
    if (!isSuccessful){
        val errorMessage = errorBody()?.message ?: "UnKnown Error Occurred"
        throw Exception(errorMessage)
    }

    return body() ?: throw Exception("Response body is null")
}