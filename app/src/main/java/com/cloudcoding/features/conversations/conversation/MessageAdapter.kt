package com.cloudcoding.features.conversations.conversation

import android.text.Html
import android.view.ContextMenu
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.cloudcoding.R
import com.cloudcoding.api.CloudCodingNetworkManager
import com.cloudcoding.api.SocketManager
import com.cloudcoding.models.Message
import com.cloudcoding.models.User
import kotlinx.coroutines.*
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*

class MessageAdapter(
    private val messages: MutableList<Message>,
    private val currentUserId: String
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val users = mutableMapOf<String, User>()
    private val SENT = 0
    private val RECEIVED = 1

    private var jobs: MutableList<Job> = mutableListOf()

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)
        jobs.forEach { job ->
            job.cancel()
        }
    }

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
        val content = JSONObject(messages[position].content).getString("html")
        holder.content.text = Html.fromHtml(content, Html.FROM_HTML_MODE_COMPACT)
        val date = SimpleDateFormat(
            holder.itemView.context.getString(R.string.date_format),
            Locale.FRANCE
        ).format(messages[position].createdAt)
        holder.date.text = date
        jobs.add(GlobalScope.launch(Dispatchers.Default) {
            val user = if (users.containsKey(messages[position].userId)) {
                users[messages[position].userId]
            } else {
                val user = CloudCodingNetworkManager.getUserById(messages[position].userId)
                users[user.id] = user
                user
            }!!
            withContext(Dispatchers.Main) {
                holder.name.text =
                    holder.itemView.context.getString(R.string.user_name, user.firstname, user.lastname)
            }
        })
    }

    private fun bindMessageReceived(holder: MessageReceivedItem, position: Int) {
        val content = JSONObject(messages[position].content).getString("html")
        holder.content.text = Html.fromHtml(content, Html.FROM_HTML_MODE_COMPACT)
        val date = SimpleDateFormat(
            holder.itemView.context.getString(R.string.date_format),
            Locale.FRANCE
        ).format(messages[position].createdAt)
        holder.date.text = date
        jobs.add(GlobalScope.launch(Dispatchers.Default) {
            val user = if (users.containsKey(messages[position].userId)) {
                users[messages[position].userId]
            } else {
                val user = CloudCodingNetworkManager.getUserById(messages[position].userId)
                users[user.id] = user
                user
            }!!
            withContext(Dispatchers.Main) {
                holder.name.text =
                    holder.itemView.context.getString(R.string.user_name, user.firstname, user.lastname)
            }
        })
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