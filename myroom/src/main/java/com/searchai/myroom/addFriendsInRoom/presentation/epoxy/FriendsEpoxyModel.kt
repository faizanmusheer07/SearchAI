package com.searchai.myroom.addFriendsInRoom.presentation.epoxy

import android.content.Context
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.constraintlayout.widget.ConstraintLayout
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyHolder
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.searchai.myroom.R
import com.searchai.myroom.addFriendsInRoom.domain.models.Message
import com.searchai.myroom.addFriendsInRoom.domain.models.PreviousProfile
import com.searchai.myroom.socket.Socket
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView


@EpoxyModelClass()
abstract class FriendsEpoxyModel(
    private val userId: String,
    private val context: Context
) : EpoxyModelWithHolder<FriendsEpoxyModel.FriendsModelHolder>() {

    private lateinit var mSocket: io.socket.client.Socket

    @EpoxyAttribute
    lateinit var data: PreviousProfile

    val id = userId
    override fun getDefaultLayout(): Int {
        return R.layout.view_holder_friends
    }

    override fun bind(holder: FriendsModelHolder) {
        super.bind(holder)
        setSocket(id)
        mSocket.on("connected") {

        }

        // Picasso Code
        Picasso.get()
            .load(data.picture)
            .into(holder.picture)

        holder.name.text = data.name
        holder.channelName.text = data.channel
        holder.followers.text = data.follower.toString()
        holder.following.text = data.following.toString()

        holder.send.setOnClickListener {
            if (holder.send.text == "Send") {
                sendMessage()
                holder.send.text = "Undo"
                Toast.makeText(context, "Room link sent successfully", Toast.LENGTH_SHORT).show()
            } else {
                holder.send.text = "Send"
            }
        }
    }

    /** uncomment the mSocket.emit() function when you set the room_code in CreateRoomFragment*/
    private fun sendMessage() {
        if (mSocket.connected()) {
            Log.d("socket", "already connected ")
//            mSocket.emit("message",
//                Message(id, data.id,
//                    "https://www.myworld.com/liveRoom?code=${CreateRoomFragment.current_room_code}")
//                    .getMessage())

        }
    }

    inner class FriendsModelHolder : EpoxyHolder() {
        lateinit var picture: CircleImageView
        lateinit var name: TextView
        lateinit var channelName: TextView
        lateinit var followers: TextView
        lateinit var following: TextView
        lateinit var constraintLayout: ConstraintLayout
        lateinit var send: AppCompatButton

        override fun bindView(itemView: View) {
            send = itemView.findViewById(R.id.button4)
            constraintLayout = itemView.findViewById(R.id.cl_item)
            picture = itemView.findViewById(R.id.userImage)
            name = itemView.findViewById(R.id.user_name)
            channelName = itemView.findViewById(R.id.user_channel_search)
            followers = itemView.findViewById(R.id.profile_follower_count)
            following = itemView.findViewById(R.id.profile_following_count)
        }
    }

    private fun setSocket(id: String) {
        Socket.setSocket(id)
        Socket.establishConnection()
        mSocket = Socket.getSocket()
    }
}