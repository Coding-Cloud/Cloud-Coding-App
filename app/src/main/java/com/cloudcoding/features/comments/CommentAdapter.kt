package com.cloudcoding.features.comments

import android.content.Context
import android.text.Html
import android.view.ContextMenu
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.cloudcoding.MainActivity
import com.cloudcoding.R
import com.cloudcoding.api.CloudCodingNetworkManager
import com.cloudcoding.models.Comment
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject

class CommentAdapter(val comments: MutableList<Comment>) :
    RecyclerView.Adapter<CommentItem>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentItem {
        return CommentItem(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.comment_item, parent, false)
        )
    }

    override fun onBindViewHolder(cell: CommentItem, position: Int) {
        val preference = MainActivity.getContext().getSharedPreferences(
            cell.itemView.context.getString(R.string.me),
            Context.MODE_PRIVATE
        )!!
        val userId = preference.getString(cell.itemView.context.getString(R.string.me), "")!!
        GlobalScope.launch(Dispatchers.Default) {
            val user = CloudCodingNetworkManager.getUserById(comments[position].ownerId)
            withContext(Dispatchers.Main) {
                cell.name.text = user.username
            }
        }
        val content = JSONObject(comments[position].content).getString("html")
        cell.content.text = Html.fromHtml(content, Html.FROM_HTML_MODE_COMPACT)
        if (comments[position].ownerId == userId) {
            cell.itemView.setOnCreateContextMenuListener { menu: ContextMenu, _: View, _: ContextMenu.ContextMenuInfo? ->
                menu.add("delete").setOnMenuItemClickListener {
                    GlobalScope.launch(Dispatchers.Default) {
                        CloudCodingNetworkManager.deleteComment(comments[position].id)
                        withContext(Dispatchers.Main) {
                            comments.removeAt(position)
                            notifyItemRemoved(position)
                        }
                    }
                    true
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return comments.size
    }
}