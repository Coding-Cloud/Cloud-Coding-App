package com.cloudcoding.features.conversations

import android.content.Context
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
import com.cloudcoding.models.Conversation
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class ConversationAdapter(val conversations: List<Conversation>) :
    RecyclerView.Adapter<ConversationItem>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ConversationItem {
        return ConversationItem(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.conversation_item, parent, false)
        )
    }

    override fun onBindViewHolder(cell: ConversationItem, position: Int) {
        val preference = cell.itemView.context.getSharedPreferences(
            cell.itemView.context.getString(R.string.me),
            Context.MODE_PRIVATE
        )!!
        val userId = preference.getString(cell.itemView.context.getString(R.string.me), "")!!
        GlobalScope.launch(Dispatchers.Default) {
            val groupId = conversations[position].groupId
            val friendshipId = conversations[position].friendshipId
            val name = when {
                groupId != null -> {
                    CloudCodingNetworkManager.getGroupById(groupId).name
                }
                friendshipId != null -> {
                    val friendship = CloudCodingNetworkManager.getFriendshipById(friendshipId)
                    val user =
                        if (friendship.user1Id == userId) {
                            CloudCodingNetworkManager.getUserById(
                                friendship.user2Id
                            )
                        } else {
                            CloudCodingNetworkManager.getUserById(
                                friendship.user1Id
                            )
                        }
                    user.username
                }
                else -> {
                    ""
                }
            }
            withContext(Dispatchers.Main) {
                cell.name.text = name
            }
        }
        cell.itemView.setOnClickListener {
            cell.itemView
                .findNavController()
                .navigate(
                    R.id.action_nav_item_conversations_to_conversationFragment,
                    bundleOf("conversationId" to conversations[position].id)
                )
        }
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