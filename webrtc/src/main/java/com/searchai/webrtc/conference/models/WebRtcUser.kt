package com.searchai.webrtc.conference.models

import org.webrtc.PeerConnection

data class WebRtcUser(
    val id: String,
    val name: String,
    var rtcPeer: PeerConnection? = null,
    val role: String
)
