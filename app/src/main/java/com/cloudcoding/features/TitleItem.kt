package com.cloudcoding.features

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.title_item.view.*

class TitleItem(v: View) : RecyclerView.ViewHolder(v) {
    val title: TextView = v.title
}