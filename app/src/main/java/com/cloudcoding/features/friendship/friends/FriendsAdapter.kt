package com.cloudcoding.features.friendship.friends

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.request.RequestOptions
import com.cloudcoding.R
import com.cloudcoding.api.CloudCodingNetworkManager
import com.cloudcoding.models.Friendship
import kotlinx.coroutines.*

class FriendsAdapter(private val friendships: MutableList<Friendship>) :
    RecyclerView.Adapter<FriendItem>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FriendItem {
        return FriendItem(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.friend_item, parent, false)
        )
    }

    private var jobs: MutableList<Job> = mutableListOf()

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)
        jobs.forEach { job ->
            job.cancel()
        }
    }

    override fun onBindViewHolder(cell: FriendItem, position: Int) {
        val sharedPref = cell.itemView.context.getSharedPreferences(
            cell.itemView.context.getString(R.string.me),
            Context.MODE_PRIVATE
        )!!
        val currentUserId = sharedPref.getString(cell.itemView.context.getString(R.string.me), "")!!
        val userId =
            if (friendships[position].user1Id == currentUserId) friendships[position].user2Id else friendships[position].user1Id

        cell.itemView.setOnClickListener {
            cell.itemView
                .findNavController()
                .navigate(
                    R.id.action_nav_item_friends_to_nav_item_profile,
                    bundleOf("userId" to userId)
                )
        }
        cell.remove.setOnClickListener {
            jobs.add(GlobalScope.launch(Dispatchers.Default) {
                CloudCodingNetworkManager.removeFriend(friendships[position].id)
                withContext(Dispatchers.Main) {
                    val index =
                        friendships.indexOfFirst { it.user1Id == userId || it.user2Id == userId }
                    friendships.removeAt(index)
                    notifyItemRemoved(index)
                }
            })
        }
        jobs.add(GlobalScope.launch(Dispatchers.Default) {
            val user = CloudCodingNetworkManager.getUserById(userId)
            withContext(Dispatchers.Main) {
                cell.username.text =
                    cell.itemView.context.getString(R.string.username, user.username)
                cell.name.text =
                    cell.itemView.context.getString(R.string.user_name, user.firstname, user.lastname)
                Glide.with(cell.profilePicture.context)
                    .load("https://interactive-examples.mdn.mozilla.net/media/cc0-images/grapefruit-slice-332-332.jpg")
                    .centerCrop()
                    .apply(RequestOptions.bitmapTransform(CircleCrop()))
                    .placeholder(R.drawable.ic_user)
                    .into(cell.profilePicture)
            }
        })
    }

    override fun getItemCount(): Int {
        return friendships.size
    }
}