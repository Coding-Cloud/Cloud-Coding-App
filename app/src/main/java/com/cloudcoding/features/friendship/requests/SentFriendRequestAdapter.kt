package com.cloudcoding.features.friendship.requests

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.cloudcoding.R
import com.cloudcoding.api.CloudCodingNetworkManager
import com.cloudcoding.models.FriendRequest
import com.cloudcoding.models.Friendship
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SentFriendRequestAdapter(val friendRequests: MutableList<FriendRequest>) :
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
                    R.id.action_followFragment_to_nav_item_profile,
                    bundleOf("userId" to userId)
                )
        }

    }

    override fun getItemCount(): Int {
        return friendRequests.size
    }
}