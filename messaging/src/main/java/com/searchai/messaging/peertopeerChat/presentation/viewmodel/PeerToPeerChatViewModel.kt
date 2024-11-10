package com.searchai.messaging.peertopeerChat.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.searchai.messaging.peertopeerChat.domain.repository.PeerToPeerChatRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PeerToPeerChatViewModel @Inject constructor(
    private val repository: PeerToPeerChatRepository
) : ViewModel() {

    private val _receivedMessages = MutableLiveData<List<Pair<String, String>>>()
    val receivedMessages: LiveData<List<Pair<String, String>>> = _receivedMessages

    fun connectToWebSocket(userId: String) {
        repository.connectWebSocket(userId)
    }

    fun sendMessage(toUser: String, message: String) {
        repository.sendMessage(toUser, message)
    }

    fun disconnect() {
        repository.disconnect()
    }

    fun fetchReceivedMessages() {
        _receivedMessages.value = repository.receivedMessages
    }
}
