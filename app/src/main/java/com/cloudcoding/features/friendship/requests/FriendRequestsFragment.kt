package com.cloudcoding.features.friendship.requests

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.cloudcoding.R
import com.cloudcoding.api.CloudCodingNetworkManager
import kotlinx.android.synthetic.main.friend_requests_fragment.*
import kotlinx.coroutines.*

class FriendRequestsFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        parent: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.friend_requests_fragment, parent, false)
    }

    private var jobs: MutableList<Job> = mutableListOf()

    override fun onDestroy() {
        super.onDestroy()
        jobs.forEach { job ->
            job.cancel()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        jobs.add(GlobalScope.launch(Dispatchers.Default) {
            val sentFriendRequests = CloudCodingNetworkManager.getSentFriendRequests()
            val receivedFriendRequests = CloudCodingNetworkManager.getReceivedFriendRequests()

            withContext(Dispatchers.Main) {
                received_friend_requests_list.run {
                    layoutManager = LinearLayoutManager(this@FriendRequestsFragment.context)
                    adapter = ReceivedFriendRequestAdapter(receivedFriendRequests)
                    addItemDecoration(
                        DividerItemDecoration(
                            context,
                            DividerItemDecoration.VERTICAL
                        )
                    )
                }
                sent_friend_requests_list.run {
                    layoutManager = LinearLayoutManager(this@FriendRequestsFragment.context)
                    adapter = SentFriendRequestAdapter(sentFriendRequests)
                    addItemDecoration(
                        DividerItemDecoration(
                            context,
                            DividerItemDecoration.VERTICAL
                        )
                    )
                }
            }
        })
    }
}