package com.cloudcoding.features.conversations.conversation

import android.view.ContextMenu
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.cloudcoding.R
import com.cloudcoding.api.CloudCodingNetworkManager
import com.cloudcoding.api.SocketManager
import com.cloudcoding.models.Message
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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
        holder.itemView.setOnCreateContextMenuListener { menu: ContextMenu, _: View, _: ContextMenu.ContextMenuInfo? ->
            menu.add("delete").setOnMenuItemClickListener {
                SocketManager.deleteMessage(messages[position].id)
                true
            }
        }
        holder.content.text = messages[position].content
        val date = SimpleDateFormat(
            holder.itemView.context.getString(R.string.date_format),
            Locale.FRANCE
        ).format(messages[position].createdAt)
        holder.date.text = date
        GlobalScope.launch(Dispatchers.Default) {
            val user = CloudCodingNetworkManager.getUserById(messages[position].userId)
            withContext(Dispatchers.Main) {
                holder.name.text =
                    holder.itemView.context.getString(R.string.name, user.firstname, user.lastname)
            }
        }
    }

    private fun bindMessageReceived(holder: MessageReceivedItem, position: Int) {
        holder.content.text = messages[position].content
        val date = SimpleDateFormat(
            holder.itemView.context.getString(R.string.date_format),
            Locale.FRANCE
        ).format(messages[position].createdAt)
        holder.date.text = date
        GlobalScope.launch(Dispatchers.Default) {
            val user = CloudCodingNetworkManager.getUserById(messages[position].userId)
            withContext(Dispatchers.Main) {
                holder.name.text =
                    holder.itemView.context.getString(R.string.name, user.firstname, user.lastname)
            }
        }
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