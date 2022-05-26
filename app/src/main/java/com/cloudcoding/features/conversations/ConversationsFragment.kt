package com.cloudcoding.features.conversations

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.cloudcoding.R
import com.cloudcoding.api.CloudCodingNetworkManager
import kotlinx.android.synthetic.main.conversations_fragment.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ConversationsFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        parent: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.conversations_fragment, parent, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        GlobalScope.launch(Dispatchers.Default) {

            val preference = context?.getSharedPreferences(
                getString(R.string.token),
                Context.MODE_PRIVATE
            )!!
            val token = preference.getString(getString(R.string.token), "")!!
            val conversations = CloudCodingNetworkManager.getUserConversations(token)

            withContext(Dispatchers.Main) {
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
    }
}