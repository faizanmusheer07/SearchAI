package com.searchai.messaging.peertopeerChat.data.repository

import android.util.Log
import com.searchai.messaging.peertopeerChat.domain.repository.PeerToPeerChatRepository
import com.searchai.messaging.peertopeerChat.data.adapter.WebSocketListenerAdapter
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocket
import org.json.JSONObject
import javax.inject.Inject

class PeerToPeerChatRepositoryImpl @Inject constructor(
    private val okHttpClient: OkHttpClient,
) : PeerToPeerChatRepository {

    companion object{
        private const val TAG = "PeerToPeerChatRepositoryImpl"
        private const val NORMAL_CLOSURE_STATUS_CODE = 1000
    }

    private lateinit var websocket: WebSocket

    override val receivedMessages = mutableListOf<Pair<String, String>>()

    override fun connectWebSocket(userId: String){
        val request = Request.Builder().url("wss://others.joinmyworld.live/ws/message?user_id=$userId").build()
        websocket = okHttpClient.newWebSocket(request, object : WebSocketListenerAdapter(TAG){
            override fun onMessage(webSocket: WebSocket, text: String) {
                super.onMessage(webSocket, text)
                receiveMessage(text)
            }
        })
    }

    override fun sendMessage(toUser: String, message: String){
        val sendMessage = JSONObject().apply {
            put("for", toUser)
            put("message", message)
        }
        Log.d("WebSocketClient", "Sending message: $sendMessage")
        websocket.send(sendMessage.toString())
    }

    override fun receiveMessage(text: String){
        val jsonObject = JSONObject(text)
        val message = jsonObject.getString("message")
        val senderId = jsonObject.getString("from")
        Log.d(TAG,"Message received: $message from $senderId")
        receivedMessages.add(Pair(senderId, message))
    }

    override fun disconnect(){
        websocket.close(NORMAL_CLOSURE_STATUS_CODE, "User disconnected")
    }
}