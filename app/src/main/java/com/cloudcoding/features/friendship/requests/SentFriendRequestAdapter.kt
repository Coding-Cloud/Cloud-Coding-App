package com.cloudcoding.features.friendship.requests

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
import com.cloudcoding.models.FriendRequest
import kotlinx.coroutines.*

class SentFriendRequestAdapter(private val friendRequests: MutableList<FriendRequest>) :
    RecyclerView.Adapter<SentFriendRequestItem>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SentFriendRequestItem {
        return SentFriendRequestItem(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.friend_request_sent_item, parent, false)
        )
    }

    private var jobs: MutableList<Job> = mutableListOf()

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)
        jobs.forEach { job ->
            job.cancel()
        }
    }

    override fun onBindViewHolder(cell: SentFriendRequestItem, position: Int) {
        val userId = friendRequests[position].requestedUserId
        cell.itemView.setOnClickListener {
            cell.itemView
                .findNavController()
                .navigate(
                    R.id.action_nav_item_friends_to_nav_item_profile,
                    bundleOf("userId" to userId)
                )
        }
        cell.cancel.setOnClickListener {
            jobs.add(GlobalScope.launch(Dispatchers.Default) {
                CloudCodingNetworkManager.cancelFriendRequest(userId)
                withContext(Dispatchers.Main) {
                    val index = friendRequests.indexOfFirst { it.requestedUserId == userId }
                    friendRequests.removeAt(index)
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
                    .load("https://i.pravatar.cc/100")
                    .centerCrop()
                    .apply(RequestOptions.bitmapTransform(CircleCrop()))
                    .placeholder(R.drawable.ic_user)
                    .into(cell.profilePicture)
            }
        })
    }

    override fun getItemCount(): Int {
        return friendRequests.size
    }
}