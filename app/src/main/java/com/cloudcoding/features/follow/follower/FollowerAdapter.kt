package com.cloudcoding.features.follow.follower

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.cloudcoding.R
import com.cloudcoding.api.CloudCodingNetworkManager
import com.cloudcoding.features.follow.FollowItem
import com.cloudcoding.models.Follower
import kotlinx.coroutines.*

class FollowerAdapter(val followers: MutableList<Follower>) : RecyclerView.Adapter<FollowItem>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FollowItem {
        return FollowItem(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.follower_item, parent, false)
        )
    }

    private var jobs: MutableList<Job> = mutableListOf()

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)
        jobs.forEach { job ->
            job.cancel()
        }
    }

    override fun onBindViewHolder(cell: FollowItem, position: Int) {
        val userId = followers[position].followerId
        cell.itemView.setOnClickListener {
            cell.itemView
                .findNavController()
                .navigate(
                    R.id.action_followFragment_to_nav_item_profile,
                    bundleOf("userId" to userId)
                )

        }
        var isFollowed = false
        cell.follow.setOnClickListener {
            isFollowed = !isFollowed
            if (isFollowed) {
                cell.follow.text = "Unfollow"
            } else {
                cell.follow.text = "Follow"
            }
            jobs.add(GlobalScope.launch(Dispatchers.Default) {
                if (isFollowed) {
                    CloudCodingNetworkManager.follow(userId)
                } else {
                    CloudCodingNetworkManager.unfollow(userId)
                }
            })
        }
        jobs.add(GlobalScope.launch(Dispatchers.Default) {
            val user = CloudCodingNetworkManager.getUserById(userId)
            isFollowed = CloudCodingNetworkManager.isFollowing(userId)
            withContext(Dispatchers.Main) {
                cell.name.text =
                    cell.itemView.context.getString(R.string.user_name, user.firstname, user.lastname)
                cell.username.text =
                    cell.itemView.context.getString(R.string.username, user.username)
                if (isFollowed) {
                    cell.follow.text = "Unfollow"
                } else {
                    cell.follow.text = "Follow"
                }
            }
        })
    }

    override fun getItemCount(): Int {
        return followers.size
    }
}