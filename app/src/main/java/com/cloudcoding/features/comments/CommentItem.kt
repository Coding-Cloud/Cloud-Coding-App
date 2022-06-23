package com.cloudcoding.features.comments

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.comment_item.view.*

class CommentItem(v: View) : RecyclerView.ViewHolder(v) {
    val name: TextView = v.name
    val content: TextView = v.content
    val profilePicture: ImageView = v.profile_picture
}