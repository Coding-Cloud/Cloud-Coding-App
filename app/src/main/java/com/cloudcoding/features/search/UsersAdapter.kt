package com.cloudcoding.features.search

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
import com.cloudcoding.models.User
import kotlinx.coroutines.*

class UsersAdapter(val users: MutableList<User>, val action: Int) :
    RecyclerView.Adapter<UserItem>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserItem {
        return UserItem(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.member_item, parent, false)
        )
    }

    private var jobs: MutableList<Job> = mutableListOf()

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)
        jobs.forEach { job ->
            job.cancel()
        }
    }

    override fun onBindViewHolder(cell: UserItem, position: Int) {
        cell.itemView.setOnClickListener {
            cell.itemView
                .findNavController()
                .navigate(action, bundleOf("userId" to users[position].id))

        }
        jobs.add(GlobalScope.launch(Dispatchers.Default) {
            val user = CloudCodingNetworkManager.getUserById(users[position].id)
            withContext(Dispatchers.Main) {
                cell.name.text =
                    cell.itemView.context.getString(R.string.user_name, user.firstname, user.lastname)
                cell.username.text =
                    cell.itemView.context.getString(R.string.username, user.username)
            }
        })
        Glide.with(cell.profilePicture.context)
            .load("https://i.pravatar.cc/100")
            .centerCrop()
            .apply(RequestOptions.bitmapTransform(CircleCrop()))
            .placeholder(R.drawable.ic_user)
            .into(cell.profilePicture)
    }

    override fun getItemCount(): Int {
        return users.size
    }
}