package com.cloudcoding.features.groups

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.conversation_item.view.*
import kotlinx.android.synthetic.main.post_item.view.content
import kotlinx.android.synthetic.main.post_item.view.name
import kotlinx.android.synthetic.main.post_item.view.profile_picture

class GroupItem(v: View) : RecyclerView.ViewHolder(v) {
    val name: TextView = v.name
    val content: TextView = v.content
    val lastDate: TextView = v.last_date
    val profilePicture: ImageView = v.profile_picture
}