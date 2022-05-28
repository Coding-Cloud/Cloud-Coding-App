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
import com.cloudcoding.models.Message
import kotlinx.android.synthetic.main.conversation_fragment.*

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
        var convMessages: MutableList<Message> = mutableListOf()
        val conversationId = requireArguments().getString("conversationId")!!
        val preference = MainActivity.getContext().getSharedPreferences(
            getString(R.string.me),
            Context.MODE_PRIVATE
        )!!
        val userId = preference.getString(getString(R.string.me), "")!!
        SocketManager.onMessages { messages ->
            convMessages = messages
            activity?.runOnUiThread {
                message_list.run {
                    layoutManager = LinearLayoutManager(this@ConversationFragment.context)
                    adapter = MessageAdapter(messages, userId)
                    scrollToPosition(convMessages.size - 1)
                }
            }
        }
        SocketManager.onMessageCreated { message ->
            activity?.runOnUiThread {
                convMessages.add(message)
                message_list.adapter?.notifyItemInserted(convMessages.size - 1)
                message_list.scrollToPosition(convMessages.size - 1)
            }
        }
        SocketManager.onMessageDeleted { messageId ->
            activity?.runOnUiThread {
                val index = convMessages.indexOfFirst { it.id == messageId }
                convMessages.removeAt(index)
                message_list.adapter?.notifyItemRangeRemoved(index, convMessages.size)
                message_list.scrollToPosition(convMessages.size - 1)
            }
        }
        SocketManager.getMessages(GetMessagesRequest(conversationId, null, null))

        send.setOnClickListener {
            SocketManager.createMessage(
                MessageRequest(
                    conversationId,
                    message_text.text.toString(),
                    null
                )
            )
            message_text.text.clear()
        }
    }
}