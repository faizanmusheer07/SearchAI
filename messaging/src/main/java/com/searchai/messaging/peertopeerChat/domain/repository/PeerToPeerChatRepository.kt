package com.searchai.messaging.peertopeerChat.domain.repository

interface PeerToPeerChatRepository {
    val receivedMessages: MutableList<Pair<String, String>>
    fun connectWebSocket(userId: String)
    fun sendMessage(toUser: String, message: String)
    fun receiveMessage(text: String)
    fun disconnect()
}