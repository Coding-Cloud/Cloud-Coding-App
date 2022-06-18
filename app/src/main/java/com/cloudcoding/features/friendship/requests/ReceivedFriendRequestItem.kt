package com.cloudcoding.features.friendship.requests

import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.friend_request_received_item.view.*

class ReceivedFriendRequestItem (v: View) : RecyclerView.ViewHolder(v) {
    val profilePicture: ImageView = v.profile_picture
    val name: TextView = v.name
    val username: TextView = v.username
    val accept: Button = v.accept_request
    val reject: Button = v.reject_request
}