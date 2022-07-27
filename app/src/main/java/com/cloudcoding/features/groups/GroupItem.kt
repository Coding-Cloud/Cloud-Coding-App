package com.cloudcoding.features.groups

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.group_item.view.*

class GroupItem(v: View) : RecyclerView.ViewHolder(v) {
    val title: TextView = v.title
    val languageThumbnail: ImageView = v.language_thumbnail
}