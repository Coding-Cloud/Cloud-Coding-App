package com.cloudcoding.features.conversations.conversation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.cloudcoding.R

class MessageAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val SENT = 0
    private val RECEIVED = 1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            SENT -> MessageSentItem(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.message_sent_fragment, parent, false)
            )
            else -> MessageReceivedItem(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.message_received_fragment, parent, false)
            )
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is MessageSentItem) {
            bindMessageSent(holder, position)
        } else if (holder is MessageReceivedItem) {
            bindMessageReceived(holder, position)
        }
    }

    private fun bindMessageSent(holder: MessageSentItem, position: Int) {

    }

    private fun bindMessageReceived(holder: MessageReceivedItem, position: Int) {

    }

    override fun getItemCount(): Int {
        return 10
    }

    override fun getItemViewType(position: Int): Int {
        return if (position % 2 == 0) {
            SENT
        } else {
            RECEIVED
        }
    }
}