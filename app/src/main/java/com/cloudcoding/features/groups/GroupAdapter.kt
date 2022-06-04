package com.cloudcoding.features.groups

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.request.RequestOptions
import com.cloudcoding.R
import com.cloudcoding.features.TitleItem
import com.cloudcoding.models.Group

class GroupAdapter(val groups: MutableList<Any>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val TITLE = 0
    private val GROUP = 1
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            GROUP -> GroupItem(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.group_item, parent, false)
            )
            else -> TitleItem(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.title_item, parent, false)
            )
        }
    }

    override fun onBindViewHolder(cell: RecyclerView.ViewHolder, position: Int) {
        if (cell is GroupItem) {
            bindGroup(cell, position)
        } else if (cell is TitleItem) {
            bindTitle(cell, position)
        }
    }

    private fun bindGroup(holder: GroupItem, position: Int) {
        holder.itemView.setOnClickListener {
            holder.itemView
            .findNavController()
            .navigate(R.id.action_nav_item_profile_to_groupDetailsFragment)
    }
        val group = groups[position] as Group
        holder.title.text = group.name
        Glide.with(holder.languageThumbnail.context)
            .load("https://interactive-examples.mdn.mozilla.net/media/cc0-images/grapefruit-slice-332-332.jpg")
            .centerCrop()
            .apply(RequestOptions.bitmapTransform(CircleCrop()))
            .placeholder(R.drawable.ic_user)
            .into(holder.languageThumbnail)
    }

    private fun bindTitle(holder: TitleItem, position: Int) {
        holder.title.text = groups[position] as String
    }

    override fun getItemCount(): Int {
        return groups.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (groups[position] is Group) {
            GROUP
        } else {
            TITLE
        }
    }
}