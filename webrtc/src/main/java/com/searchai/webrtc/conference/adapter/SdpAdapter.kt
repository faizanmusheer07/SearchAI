package com.searchai.webrtc.conference.adapter

import android.util.Log
import org.webrtc.SdpObserver
import org.webrtc.SessionDescription

open class SdpAdapter(tag: String) : SdpObserver {
    private val tag: String = "chao $tag"
    private fun log(s: String) {
        Log.d(tag, s)
    }

    override fun onCreateSuccess(sessionDescription: SessionDescription) {
        log("onCreateSuccess $sessionDescription")
    }

    override fun onSetSuccess() {
        log("onSetSuccess ")
    }

    override fun onCreateFailure(s: String) {
        log("onCreateFailure $s")
    }

    override fun onSetFailure(s: String) {
        log("onSetFailure $s")
    }

}
