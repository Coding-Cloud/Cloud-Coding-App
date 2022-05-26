package com.cloudcoding.features.conversations

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.request.RequestOptions
import com.cloudcoding.R
import com.cloudcoding.models.Conversation


class ConversationAdapter(val conversations: List<Conversation>) :
    RecyclerView.Adapter<ConversationItem>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ConversationItem {
        return ConversationItem(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.conversation_item, parent, false)
        )
    }

    override fun onBindViewHolder(cell: ConversationItem, position: Int) {

        cell.itemView.setOnClickListener {
            cell.itemView
                .findNavController()
                .navigate(
                    R.id.action_nav_item_conversations_to_conversationFragment,
                    bundleOf("conversationId" to conversations[position].id)
                )
        }
        cell.name.text = conversations[position].friendshipId
        Glide.with(cell.profilePicture.context)
            .load("https://interactive-examples.mdn.mozilla.net/media/cc0-images/grapefruit-slice-332-332.jpg")
            .centerCrop()
            .apply(RequestOptions.bitmapTransform(CircleCrop()))
            .placeholder(R.drawable.ic_user)
            .into(cell.profilePicture)
    }

    override fun getItemCount(): Int {
        return conversations.size
    }
}