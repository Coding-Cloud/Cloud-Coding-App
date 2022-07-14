package com.cloudcoding.features.comments

import android.content.Context
import android.graphics.drawable.Drawable
import android.text.Html
import android.view.ContextMenu
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.request.RequestOptions
import com.cloudcoding.MainActivity
import com.cloudcoding.R
import com.cloudcoding.api.CloudCodingNetworkManager
import com.cloudcoding.models.Comment
import kotlinx.coroutines.*
import org.json.JSONObject

class CommentAdapter(val comments: MutableList<Comment>) :
    RecyclerView.Adapter<CommentItem>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentItem {
        return CommentItem(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.comment_item, parent, false)
        )
    }

    private var jobs: MutableList<Job> = mutableListOf()

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)
        jobs.forEach { job ->
            job.cancel()
        }
    }

    override fun onBindViewHolder(cell: CommentItem, position: Int) {
        val preference = MainActivity.getContext().getSharedPreferences(
            cell.itemView.context.getString(R.string.me),
            Context.MODE_PRIVATE
        )!!
        val userId = preference.getString(cell.itemView.context.getString(R.string.me), "")!!
        val content = JSONObject(comments[position].content).getString("html")
        jobs.add(GlobalScope.launch(Dispatchers.Default) {
            val imageGetter = MyImageGetter(cell.itemView.context, cell.content,100,100)
            cell.content.text = Html.fromHtml(content, Html.FROM_HTML_MODE_COMPACT, imageGetter, null)

            val user = CloudCodingNetworkManager.getUserById(comments[position].ownerId)
            withContext(Dispatchers.Main) {
                cell.name.text = user.username
            }
        })
        if (comments[position].ownerId == userId) {
            cell.itemView.setOnCreateContextMenuListener { menu: ContextMenu, _: View, _: ContextMenu.ContextMenuInfo? ->
                menu.add("delete").setOnMenuItemClickListener {
                    jobs.add(GlobalScope.launch(Dispatchers.Default) {
                        CloudCodingNetworkManager.deleteComment(comments[position].id)
                        withContext(Dispatchers.Main) {
                            comments.removeAt(position)
                            notifyItemRemoved(position)
                        }
                    })
                    true
                }
            }
        }
        Glide.with(cell.profilePicture.context)
            .load("https://i.pravatar.cc/100")
            .centerCrop()
            .apply(RequestOptions.bitmapTransform(CircleCrop()))
            .placeholder(R.drawable.ic_user)
            .into(cell.profilePicture)
    }

    override fun getItemCount(): Int {
        return comments.size
    }
}

class MyImageGetter(val context : Context, val textView : TextView, val height : Int, val width : Int) : Html.ImageGetter {
    override fun getDrawable(url: String?): Drawable {

        val future = Glide.with(context).load(url).submit()
        val drawable = future.get()

        drawable.setBounds(0, 0, drawable.intrinsicWidth, drawable.intrinsicHeight)

        textView.text = textView.text

        return drawable
    }
}