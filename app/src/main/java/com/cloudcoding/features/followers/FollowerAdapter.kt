package com.cloudcoding.features.followers

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.cloudcoding.R
import com.cloudcoding.api.CloudCodingNetworkManager
import com.cloudcoding.models.Follower
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FollowerAdapter(val followers: MutableList<Follower>) : RecyclerView.Adapter<FollowerItem>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FollowerItem {
        return FollowerItem(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.follower_item, parent, false)
        )
    }

    override fun onBindViewHolder(cell: FollowerItem, position: Int) {
        val userId = followers[position].followerId
        cell.itemView.setOnClickListener {
            cell.itemView
                .findNavController()
                .navigate(
                    R.id.action_viewFollowerFragment_to_profileFragment2,
                    bundleOf("userId" to followers[position].followerId)
                )

        }
        GlobalScope.launch(Dispatchers.Default) {
            val user = CloudCodingNetworkManager.getUserById(userId)
            val isFollowed = CloudCodingNetworkManager.isFollowing(userId)
            withContext(Dispatchers.Main) {
                cell.name.text =
                    cell.itemView.context.getString(R.string.name, user.firstname, user.lastname)
                cell.username.text =
                    cell.itemView.context.getString(R.string.username, user.username)
                if(isFollowed){
                    cell.follow.text= "Unfollow"
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return followers.size
    }
}