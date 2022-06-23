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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ReceivedFriendRequestAdapter(private val friendRequests: MutableList<FriendRequest>) :
    RecyclerView.Adapter<ReceivedFriendRequestItem>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReceivedFriendRequestItem {
        return ReceivedFriendRequestItem(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.friend_request_received_item, parent, false)
        )
    }

    override fun onBindViewHolder(cell: ReceivedFriendRequestItem, position: Int) {
        val userId = friendRequests[position].requesterUserId
        cell.itemView.setOnClickListener {
            cell.itemView
                .findNavController()
                .navigate(
                    R.id.action_nav_item_friends_to_nav_item_profile,
                    bundleOf("userId" to userId)
                )
        }
        cell.accept.setOnClickListener {
            GlobalScope.launch(Dispatchers.Default) {
                CloudCodingNetworkManager.acceptFriendRequest(userId)
                withContext(Dispatchers.Main) {
                    val index = friendRequests.indexOfFirst { it.requesterUserId == userId }
                    friendRequests.removeAt(index)
                    notifyItemRemoved(index)
                }
            }
        }
        cell.reject.setOnClickListener {
            GlobalScope.launch(Dispatchers.Default) {
                CloudCodingNetworkManager.rejectFriendRequest(userId)
                withContext(Dispatchers.Main) {
                    val index = friendRequests.indexOfFirst { it.requesterUserId == userId }
                    friendRequests.removeAt(index)
                    notifyItemRemoved(index)
                }
            }
        }
        GlobalScope.launch(Dispatchers.Default) {
            val user = CloudCodingNetworkManager.getUserById(userId)
            withContext(Dispatchers.Main) {
                cell.username.text =
                    cell.itemView.context.getString(R.string.username, user.username)
                cell.name.text =
                    cell.itemView.context.getString(R.string.name, user.firstname, user.lastname)
                Glide.with(cell.profilePicture.context)
                    .load("https://interactive-examples.mdn.mozilla.net/media/cc0-images/grapefruit-slice-332-332.jpg")
                    .centerCrop()
                    .apply(RequestOptions.bitmapTransform(CircleCrop()))
                    .placeholder(R.drawable.ic_user)
                    .into(cell.profilePicture)
            }
        }
    }

    override fun getItemCount(): Int {
        return friendRequests.size
    }
}