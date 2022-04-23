package com.cloudcoding.features.conversations

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.findFragment
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.request.RequestOptions
import com.cloudcoding.R

class ConversationAdapter : RecyclerView.Adapter<ConversationItem>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ConversationItem {
        return ConversationItem(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.conversation_item, parent, false)
        )
    }

    override fun onBindViewHolder(cell: ConversationItem, position: Int) {
        cell.itemView.setOnClickListener{
            cell.itemView
                .findNavController()
                .navigate(R.id.action_nav_item_conversations_to_conversationFragment)
        }
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