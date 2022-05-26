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
import com.cloudcoding.R
import com.cloudcoding.api.CloudCodingNetworkManager
import com.cloudcoding.api.request.MessageRequest
import kotlinx.android.synthetic.main.conversation_fragment.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ConversationFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        parent: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.conversation_fragment, parent, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val conversationId = requireArguments().getString("conversationId")!!
        loadMessageList(conversationId)
        send.setOnClickListener {
            val messageRequest = MessageRequest(message_text.text.toString(), null)
            message_text.text.clear()
            GlobalScope.launch(Dispatchers.Default) {

                val preferenceToken = context?.getSharedPreferences(
                    getString(R.string.token),
                    Context.MODE_PRIVATE
                )!!
                val token = preferenceToken.getString(getString(R.string.token), "")!!
                CloudCodingNetworkManager.addMessage(token, conversationId, messageRequest)
                withContext(Dispatchers.Main) {
                    loadMessageList(conversationId)
                }
            }
        }
        activity?.onBackPressedDispatcher?.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    findNavController().popBackStack()
                }
            })
    }

    private fun loadMessageList(conversationId: String) {
        GlobalScope.launch(Dispatchers.Default) {

            val preferenceToken = context?.getSharedPreferences(
                getString(R.string.token),
                Context.MODE_PRIVATE
            )!!
            val token = preferenceToken.getString(getString(R.string.token), "")!!
            val messages = CloudCodingNetworkManager.getMessages(token, conversationId)
            val me = CloudCodingNetworkManager.getMe(token)

            withContext(Dispatchers.Main) {
                message_list.run {
                    layoutManager = LinearLayoutManager(this@ConversationFragment.context)
                    adapter = MessageAdapter(messages, me.id)
                    scrollToPosition(9)
                }
            }
        }
    }
}