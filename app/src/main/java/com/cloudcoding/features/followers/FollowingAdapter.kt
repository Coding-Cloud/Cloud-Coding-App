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

class FollowingAdapter(val followings: MutableList<Follower>) :
    RecyclerView.Adapter<FollowerItem>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FollowerItem {
        return FollowerItem(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.follower_item, parent, false)
        )
    }

    override fun onBindViewHolder(cell: FollowerItem, position: Int) {
        cell.itemView.setOnClickListener {
            cell.itemView
                .findNavController()
                .navigate(
                    R.id.action_viewFollowerFragment_to_profileFragment2,
                    bundleOf("userId" to followings[position].followedId)
                )

        }
        GlobalScope.launch(Dispatchers.Default) {
            val user = CloudCodingNetworkManager.getUserById(followings[position].followedId)
            withContext(Dispatchers.Main) {
                cell.name.text =
                    cell.itemView.context.getString(R.string.name, user.firstname, user.lastname)
                cell.username.text =
                    cell.itemView.context.getString(R.string.username, user.username)
            }
        }
    }

    override fun getItemCount(): Int {
        return followings.size
    }
}