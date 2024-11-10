package com.searchai.webrtc.conference.client

import android.util.Log
import com.searchai.webrtc.conference.adapter.PeerConnectionAdapter
import com.searchai.webrtc.conference.adapter.SdpAdapter
import com.searchai.webrtc.conference.models.WebRtcUser
import io.socket.client.Socket
import kotlinx.coroutines.suspendCancellableCoroutine
import org.json.JSONObject
import org.webrtc.IceCandidate
import org.webrtc.MediaConstraints
import org.webrtc.PeerConnection
import org.webrtc.PeerConnectionFactory
import org.webrtc.SdpObserver
import org.webrtc.SessionDescription
import javax.inject.Inject
import kotlin.coroutines.resume

class SignallingClient @Inject constructor(
    private val socket: Socket,
    private val peerConnectionFactory: PeerConnectionFactory,
    private val rtcConfig: PeerConnection.RTCConfiguration
) {
    private lateinit var userId: String
    companion object {
        private const val TAG = "SignallingClient"
    }

    val peerConnection =
        peerConnectionFactory.createPeerConnection(rtcConfig, object : PeerConnectionAdapter(TAG) {
            override fun onIceCandidate(iceCandidate: IceCandidate) {
                super.onIceCandidate(iceCandidate)
                addIceCandidate(iceCandidate)
                Log.d(TAG, "Ice candidate generated: ${iceCandidate.sdp}")
                sendIceCandidateToSignallingServer(
                    candidate = iceCandidate,
                    userId = userId,
                    roomId = "room1",
                    user = WebRtcUser(
                        id = userId,
                        role = "host",
                        name = "Gursimar Singh"
                    )
                )
            }

            override fun onIceGatheringChange(iceGatheringState: PeerConnection.IceGatheringState) {
                super.onIceGatheringChange(iceGatheringState)
                Log.d(TAG, "Ice gathering state changed to: $iceGatheringState")
                when (iceGatheringState) {
                    PeerConnection.IceGatheringState.NEW -> Log.d(TAG, "ICE Gathering started")
                    PeerConnection.IceGatheringState.GATHERING -> Log.d(TAG, "ICE Candidate gathering in progress")
                    PeerConnection.IceGatheringState.COMPLETE -> Log.d(TAG, "ICE Candidate gathering complete")
                }
            }

        })

    init {
        connect()
    }

    private fun addIceCandidate(candidate: IceCandidate) {
        peerConnection?.addIceCandidate(candidate)
    }

    private fun connect() {
        socket.connect()
        socket.on(Socket.EVENT_CONNECT) {
            Log.d("Socket", "Connected to Socket.IO")
        }
        socket.on(Socket.EVENT_CONNECT_ERROR) {
            Log.e("Socket", "Connection error: ${it.joinToString()}")
        }
    }

    fun disconnect() {
        socket.off(Socket.EVENT_CONNECT) {
            Log.d("Socket", "Disconnected from Socket.IO")
        }
        socket.disconnect()
    }

    suspend fun init() {
        val userId = getUserIdFromSocket()
        sendOfferToSignallingServer(
            user = WebRtcUser(
                id = userId,
                role = "host",
                name = "Gursimar Singh"
            ),
            roomId = "room1"
        )
        receiveOfferFromSignallingServer()
    }

    private suspend fun getUserIdFromSocket(): String {
        return suspendCancellableCoroutine { continuation ->
            socket.on("connection") { args ->
                if (args.isNotEmpty()) {
                    val data = args[0] as JSONObject
                    Log.d(TAG, data.toString())
                    userId = data.getString("userID")
                    Log.d(TAG, "User ID: $userId")
                    continuation.resume(userId)
                } else {
                    Log.d(TAG, "No data received")
                }
            }
        }
    }


    private fun sendOfferToSignallingServer(user: WebRtcUser, roomId: String) {
        val constraints = MediaConstraints().apply {
            mandatory.add(MediaConstraints.KeyValuePair("audio", "true"))
            optional.add(MediaConstraints.KeyValuePair("maxWidth", "320"))
            optional.add(MediaConstraints.KeyValuePair("maxFrameRate", "15"))
            optional.add(MediaConstraints.KeyValuePair("minFrameRate", "15"))
        }
        user.rtcPeer = peerConnection
        peerConnection?.createOffer(object : SdpAdapter(TAG) {
            override fun onCreateSuccess(sessionDescription: SessionDescription) {
                peerConnection.setLocalDescription(object : SdpAdapter(TAG) {
                    override fun onSetSuccess() {
                        offerMessageToSignallingServer(user, sessionDescription.description, roomId)
                        Log.d(TAG, "Offer sent: ${sessionDescription.description}")
                    }
                }, sessionDescription)
            }
        }, constraints)
    }

    private fun offerMessageToSignallingServer(user: WebRtcUser, offer: String, roomId: String) {
        if (user.role == "stream") {
//             Assuming you have a socket for stream connection
            socket.emit(
                "signalling", mapOf(
                    "action" to "sdpOffer",
                    "offer" to offer,
                    "offerFor" to user.id
                )
            )
        } else {
            when (user.role) {
                "host" -> {
                    val host = JSONObject().apply {
                        put("action", "start")
                        put("roomName", roomId)
                        put("role", "host")
                        put("userName", user.name)
                    }
                    socket.emit("host", host)
                }

                "member" -> {
                    val start = JSONObject().apply {
                        put("action", "start")
                        put("roomName", roomId)
                        put("role", "member")
                    }
                    socket.emit("member", start)
                }
            }
            Log.d(TAG, "Sending offer to ${user.id}")
            // Assuming you have a standard socket connection
            val sendOffer = JSONObject().apply {
                put("action", "sdpOffer")
                put("offerFor", user.id)
                put("roomID", roomId)
                put("offer", offer)
                put("role", user.role)
            }
            socket.emit("signalling", sendOffer)
        }
    }

    private fun receiveOfferFromSignallingServer() {
        socket.on("signalling") { args ->
            Log.d(TAG, "Signalling message received")
            if (args.isNotEmpty()) {
                val message = args[0] as JSONObject
                when (message.getString("action")) {
                    "sdpAnswer" -> {
                        val senderID = message.getString("senderID")
                        val sdpAnswer = message.getString("sdpAnswer")
                        Log.d(TAG, "Answer received: $sdpAnswer")
                        processAnswer(senderID, sdpAnswer)
                    }

                    "candidate" -> {
                        val userID = message.getString("userID")
                        val candidate = message.getString("candidate")
                        Log.d(TAG, "Candidate received: $userID, $candidate")
                    }
                }
            }
        }
    }

    private fun processAnswer(senderID: String, sdpAnswer: String) {
        if (peerConnection?.signalingState() == PeerConnection.SignalingState.HAVE_LOCAL_OFFER) {
            val sessionDescription = SessionDescription(SessionDescription.Type.ANSWER, sdpAnswer)
            peerConnection.setRemoteDescription(object : SdpAdapter(TAG) {
                override fun onSetSuccess() {
                    Log.d(TAG, "Remote description set successfully for $senderID")
                }
            }, sessionDescription)
        } else {
            Log.d(TAG, "PeerConnection is in an invalid state: ${peerConnection?.signalingState()}")
        }
    }

    fun sendIceCandidateToSignallingServer(
        candidate: IceCandidate,
        userId: String,
        roomId: String,
        user: WebRtcUser
    ) {
        if (user.role == "stream") {
            val streamJson = JSONObject().apply {
                put("action", "candidate")
                put("candidate", candidate.sdp)
                put("role", "steam")
                put("roomId", roomId)
            }
            socket.emit("signalling", streamJson)
        } else {
            val candidateJson = JSONObject().apply {
                put("action", "candidate")
                put("for", userId)
                put("roomName", roomId)
                put("role", user.role)
                put("candidate", candidate.sdp)
            }
            Log.d(TAG, candidateJson.toString())
            socket.emit("signalling", candidateJson)
        }
    }
}