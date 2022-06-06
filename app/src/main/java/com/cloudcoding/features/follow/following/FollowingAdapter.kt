package com.cloudcoding.features.follow.following

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.cloudcoding.R
import com.cloudcoding.api.CloudCodingNetworkManager
import com.cloudcoding.features.follow.FollowItem
import com.cloudcoding.models.Follower
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FollowingAdapter(val followings: MutableList<Follower>) :
    RecyclerView.Adapter<FollowItem>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FollowItem {
        return FollowItem(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.follower_item, parent, false)
        )
    }

    override fun onBindViewHolder(cell: FollowItem, position: Int) {
        val userId = followings[position].followedId
        cell.itemView.setOnClickListener {
            cell.itemView
                .findNavController()
                .navigate(
                    R.id.action_followFragment_to_profileFragment2,
                    bundleOf("userId" to userId)
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
        return followings.size
    }
}