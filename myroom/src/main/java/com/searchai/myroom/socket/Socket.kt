package com.searchai.myroom.socket

import android.util.Log
import io.socket.client.IO
import io.socket.client.Socket

object Socket{

    private const val TAG = "SocketHandler"

    lateinit var mSocket: Socket

    @Synchronized
    fun setSocket(userID:String) {

        val param : IO.Options = IO.Options()
//        param.timeout= 600000
//            param.secure= true
        Log.d(TAG,"userid = $userID")
        param.query= "user_id=$userID"


        // TODO: Replace with https://messaging-api.joinmyworld.live
        mSocket = IO.socket("https://messaging.joinmyworld.in/chat",param)
    }

    @Synchronized
    fun getSocket(): Socket {
        return mSocket
    }

    @Synchronized
    fun establishConnection() {
        mSocket.connect()
    }

    @Synchronized
    fun closeConnection() {
        mSocket.disconnect()
    }

}