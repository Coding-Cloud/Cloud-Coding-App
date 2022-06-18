package com.cloudcoding.features.friendship.requests

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.request.RequestOptions
import com.cloudcoding.R
import com.cloudcoding.api.CloudCodingNetworkManager
import com.cloudcoding.features.friendship.friends.FriendsAdapter
import com.cloudcoding.models.FriendRequest
import com.cloudcoding.models.Friendship
import kotlinx.android.synthetic.main.friends_fragment.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SentFriendRequestAdapter(private val friendRequests: MutableList<FriendRequest>) :
    RecyclerView.Adapter<SentFriendRequestItem>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SentFriendRequestItem {
        return SentFriendRequestItem(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.friend_request_sent_item, parent, false)
        )
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
            GlobalScope.launch(Dispatchers.Default) {
                CloudCodingNetworkManager.cancelFriendRequest(userId)
                withContext(Dispatchers.Main) {
                    val index = friendRequests.indexOfFirst { it.requestedUserId == userId }
                    friendRequests.removeAt(index)
                    notifyItemRemoved(index)
                }
            }
        }
        GlobalScope.launch(Dispatchers.Default) {
            val user = CloudCodingNetworkManager.getUserById(userId)
            withContext(Dispatchers.Main) {
                cell.username.text = cell.itemView.context.getString(R.string.username, user.username)
                cell.name.text = cell.itemView.context.getString(R.string.name, user.firstname, user.lastname)
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