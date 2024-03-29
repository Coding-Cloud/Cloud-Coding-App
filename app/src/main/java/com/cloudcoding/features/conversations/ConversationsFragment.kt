package com.cloudcoding.features.conversations

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.cloudcoding.R
import com.cloudcoding.api.SocketManager
import com.cloudcoding.api.request.GetConversationsRequest
import kotlinx.android.synthetic.main.conversations_fragment.*

class ConversationsFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        parent: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.conversations_fragment, parent, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        SocketManager.onConversations { conversations ->
            activity?.runOnUiThread {
                conversation_list.run {
                    layoutManager = LinearLayoutManager(this@ConversationsFragment.context)
                    adapter = ConversationAdapter(conversations)
                    addItemDecoration(
                        DividerItemDecoration(
                            context,
                            DividerItemDecoration.VERTICAL
                        )
                    )
                }
            }
        }
        SocketManager.getConversations(GetConversationsRequest(null, null, null))
    }

    override fun onDestroy() {
        super.onDestroy()
        SocketManager.clearEventListeners()
    }
}