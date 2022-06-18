package com.cloudcoding.features.friendship.friends

import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.friend_item.view.*

class FriendItem(v: View) : RecyclerView.ViewHolder(v) {
    val profilePicture: ImageView = v.profile_picture
    val name: TextView = v.name
    val username: TextView = v.username
    val remove: Button = v.remove_friend
}