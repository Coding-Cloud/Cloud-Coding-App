package com.cloudcoding.features.members

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.request.RequestOptions
import com.cloudcoding.R
import com.cloudcoding.api.CloudCodingNetworkManager
import com.cloudcoding.models.GroupMembership
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MemberAdapter(val members: MutableList<GroupMembership>) : RecyclerView.Adapter<MemberItem>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MemberItem {
        return MemberItem(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.member_item, parent, false)
        )
    }

    override fun onBindViewHolder(cell: MemberItem, position: Int) {
        GlobalScope.launch(Dispatchers.Default) {
            val user = CloudCodingNetworkManager.getUserById(members[position].userid)
            withContext(Dispatchers.Main) {
                cell.name
            }
        }
        Glide.with(cell.profilePicture.context)
            .load("https://interactive-examples.mdn.mozilla.net/media/cc0-images/grapefruit-slice-332-332.jpg")
            .centerCrop()
            .apply(RequestOptions.bitmapTransform(CircleCrop()))
            .placeholder(R.drawable.ic_user)
            .into(cell.profilePicture)
    }

    override fun getItemCount(): Int {
        return members.size
    }
}