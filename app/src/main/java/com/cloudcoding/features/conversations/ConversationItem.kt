package com.cloudcoding.features.conversations

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.conversation_item.view.*

class ConversationItem(v: View) : RecyclerView.ViewHolder(v) {
    val name: TextView = v.name
    val content: TextView = v.content
    val lastDate: TextView = v.last_date
    val profilePicture: ImageView = v.profile_picture
}