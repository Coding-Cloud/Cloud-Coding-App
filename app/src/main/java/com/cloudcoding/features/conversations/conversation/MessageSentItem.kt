package com.cloudcoding.features.conversations.conversation

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.message_sent_fragment.view.*

class MessageSentItem(v: View) : RecyclerView.ViewHolder(v) {
    val name: TextView = v.name
    val content: TextView = v.content
    val date: TextView = v.date
}