package com.searchai.myroom.addFriendsInRoom.domain.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import org.json.JSONObject

data class Message(
    @Json(name = "user_id") val senderId: String,
    @Json(name = "receiver_id") val receiverId: String,
    @Json(name = "message") val message: String
) {
    fun getMessage(): JSONObject {
        val moshi = Moshi.Builder().build()
        val jsonAdapter: JsonAdapter<Message> = moshi.adapter(Message::class.java)
        val jsonString = jsonAdapter.toJson(this)  // Convert the Message object to a JSON string
        return JSONObject(jsonString)  // Convert the JSON string to a JSONObject
    }
}

