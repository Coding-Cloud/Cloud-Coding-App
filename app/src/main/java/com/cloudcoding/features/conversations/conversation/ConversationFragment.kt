package com.cloudcoding.features.conversations.conversation

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.cloudcoding.MainActivity
import com.cloudcoding.R
import com.cloudcoding.api.SocketManager
import com.cloudcoding.api.request.GetMessagesRequest
import com.cloudcoding.api.request.MessageRequest
import com.cloudcoding.api.response.MessagesResponse
import com.cloudcoding.utils.PaginationScrollListener
import kotlinx.android.synthetic.main.conversation_fragment.*
import org.json.JSONObject

class ConversationFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        parent: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.conversation_fragment, parent, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        activity?.onBackPressedDispatcher?.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    findNavController().popBackStack()
                }
            })
        var loading = true
        val convMessages = MessagesResponse(mutableListOf(), 0)
        val conversationId = requireArguments().getString("conversationId")!!
        val preference = MainActivity.getContext().getSharedPreferences(
            getString(R.string.me),
            Context.MODE_PRIVATE
        )!!
        val userId = preference.getString(getString(R.string.me), "")!!
        message_list.run {
            layoutManager = LinearLayoutManager(this@ConversationFragment.context)
            adapter = MessageAdapter(convMessages.messages, userId)
            scrollToPosition(convMessages.messages.size - 1)
        }

        message_list.addOnScrollListener(object :
            PaginationScrollListener(message_list.layoutManager as LinearLayoutManager) {
            override fun isLastPage(): Boolean {
                return convMessages.totalResults == convMessages.messages.size
            }

            override fun isLoading(): Boolean {
                return loading
            }

            override fun loadMoreItems() {
                SocketManager.getMessages(
                    GetMessagesRequest(
                        conversationId,
                        25,
                        convMessages.messages.size
                    )
                )
            }

            override fun getTotalPageCount(): Int {
                return 0
            }

            override fun isReversed(): Boolean {
                return true
            }
        })
        SocketManager.onMessages { messages ->
            loading = false
            activity?.runOnUiThread {
                convMessages.totalResults = messages.totalResults
                val size = convMessages.messages.size
                convMessages.messages.addAll(0, messages.messages.reversed())
                message_list.adapter?.notifyItemRangeInserted(0, messages.messages.size)
                if (size == 0) {
                    message_list.scrollToPosition(convMessages.messages.size - 1)
                }
            }
        }
        SocketManager.onMessageCreated { message ->
            activity?.runOnUiThread {
                convMessages.messages.add(message)
                convMessages.totalResults += 1
                message_list.adapter?.notifyItemInserted(convMessages.messages.size - 1)
                message_list.scrollToPosition(convMessages.messages.size - 1)
            }
        }
        SocketManager.onMessageDeleted { messageId ->
            activity?.runOnUiThread {
                val index = convMessages.messages.indexOfFirst { it.id == messageId }
                convMessages.messages.removeAt(index)
                convMessages.totalResults -= 1
                message_list.adapter?.notifyItemRangeRemoved(index, convMessages.messages.size)
            }
        }
        SocketManager.onMessageUpdated { message ->
            activity?.runOnUiThread {
                val index = convMessages.messages.indexOfFirst { it.id == message.messageId }
                convMessages.messages[index].assetId = message.assetId
                convMessages.messages[index].content = message.content
                message_list.adapter?.notifyItemChanged(index)
            }
        }

        send.setOnClickListener {
            val message = message_text.text.toString().trim()
            if(message.isBlank()){
                return@setOnClickListener
            }
            val json = JSONObject()
            json.put("html", getString(R.string.comment_html, message))
            SocketManager.createMessage(
                MessageRequest(
                    conversationId,
                    json.toString(),
                    null
                )
            )
            message_text.text.clear()
        }
        SocketManager.getMessages(GetMessagesRequest(conversationId, 25, 0))
    }
}