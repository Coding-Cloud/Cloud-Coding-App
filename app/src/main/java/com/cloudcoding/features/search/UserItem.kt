package com.cloudcoding.features.search

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.member_item.view.*

class UserItem(v: View) : RecyclerView.ViewHolder(v) {
    val profilePicture: ImageView = v.profile_picture
    val name: TextView = v.name
    val username: TextView = v.username
}