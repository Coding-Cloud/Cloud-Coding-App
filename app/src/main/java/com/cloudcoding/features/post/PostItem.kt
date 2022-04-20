package com.cloudcoding.features.post

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.post_item.view.*

class PostItem(v: View) : RecyclerView.ViewHolder(v) {
    val name: TextView = v.name
    val username: TextView = v.username
    val profilePicture: ImageView = v.profile_picture
    val likeCount: TextView = v.like_count
    val like: ImageView = v.like
}