package com.cloudcoding.features.conversations.conversation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.cloudcoding.R
import com.cloudcoding.models.Message
import java.text.SimpleDateFormat
import java.util.*

class MessageAdapter(
    private val messages: MutableList<Message>,
    private val currentUserId: String
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
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
        holder.content.text = messages[position].content
        holder.name.text = messages[position].userId
        val date = SimpleDateFormat(
            holder.itemView.context.getString(R.string.date_format),
            Locale.FRANCE
        ).format(messages[position].createdAt)
        holder.date.text = date
    }

    private fun bindMessageReceived(holder: MessageReceivedItem, position: Int) {
        holder.content.text = messages[position].content
        holder.name.text = messages[position].userId
        val date = SimpleDateFormat(
            holder.itemView.context.getString(R.string.date_format),
            Locale.FRANCE
        ).format(messages[position].createdAt)
        holder.date.text = date
    }

    override fun getItemCount(): Int {
        return messages.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (messages[position].userId == currentUserId) {
            SENT
        } else {
            RECEIVED
        }
    }
}