package com.searchai.messaging.peertopeerChat.data.adapter

import android.util.Log
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import okio.ByteString
import org.json.JSONObject

open class WebSocketListenerAdapter (
    tag: String,
): WebSocketListener() {
    companion object {
        const val NORMAL_CLOSURE_STATUS_CODE = 1000
    }

    private val tag = "chao $tag"
    private fun log(s: String) {
        Log.d(tag, s)
    }

    override fun onOpen(webSocket: WebSocket, response: Response) {
        super.onOpen(webSocket, response)
        log("Connected to WebSocket $webSocket $response")
    }

    override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
        super.onFailure(webSocket, t, response)
        log("WebSocket connection failed $webSocket $t $response")
    }

    override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
        super.onClosing(webSocket, code, reason)
        log("WebSocket closing: Code = $code, Reason = $reason")
        webSocket.close(NORMAL_CLOSURE_STATUS_CODE, null)
    }

    override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
        super.onClosed(webSocket, code, reason)
        log("WebSocket closed: Code = $code, Reason = $reason")
    }

    override fun onMessage(webSocket: WebSocket, text: String) {
        super.onMessage(webSocket, text)
        log("Message received: $text")
    }

    override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
        super.onMessage(webSocket, bytes)
        log("Message received: $bytes")
    }
}