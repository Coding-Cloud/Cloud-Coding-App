package com.cloudcoding.features.projects

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.project_item.view.*

class ProjectItem(v: View) : RecyclerView.ViewHolder(v) {
    val name: TextView = v.title
    val languageThumbnail: ImageView = v.language_thumbnail
}