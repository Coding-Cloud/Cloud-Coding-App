package com.cloudcoding.features.friendship.friends

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.cloudcoding.R
import com.cloudcoding.models.Friendship

class FriendsAdapter(val friendships: MutableList<Friendship>) :
    RecyclerView.Adapter<FriendItem>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FriendItem {
        return FriendItem(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.friend_item, parent, false)
        )
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
                    R.id.action_followFragment_to_nav_item_profile,
                    bundleOf("userId" to userId)
                )
        }
    }

    override fun getItemCount(): Int {
        return friendships.size
    }
}