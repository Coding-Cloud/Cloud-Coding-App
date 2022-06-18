package com.cloudcoding.features.friendship.requests

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.cloudcoding.R
import com.cloudcoding.models.FriendRequest

class ReceivedFriendRequestAdapter(val friendRequests: MutableList<FriendRequest>) :
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
                    R.id.action_followFragment_to_nav_item_profile,
                    bundleOf("userId" to userId)
                )
        }

    }

    override fun getItemCount(): Int {
        return friendRequests.size
    }
}