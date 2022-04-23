package com.cloudcoding.features.members

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.request.RequestOptions
import com.cloudcoding.R

class MemberAdapter : RecyclerView.Adapter<MemberItem>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MemberItem {
        return MemberItem(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.member_item, parent, false)
        )
    }

    override fun onBindViewHolder(cell: MemberItem, position: Int) {
        Glide.with(cell.profilePicture.context)
            .load("")
            .centerCrop()
            .apply(RequestOptions.bitmapTransform(CircleCrop()))
            .placeholder(R.drawable.ic_user)
            .into(cell.profilePicture)
    }

    override fun getItemCount(): Int {
        return 10
    }
}