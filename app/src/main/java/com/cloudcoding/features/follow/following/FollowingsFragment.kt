package com.cloudcoding.features.follow.following

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.cloudcoding.R
import com.cloudcoding.api.CloudCodingNetworkManager
import com.cloudcoding.api.request.GetFollowersRequest
import com.cloudcoding.api.response.FollowersResponse
import com.cloudcoding.utils.PaginationScrollListener
import kotlinx.android.synthetic.main.followers_fragment.*
import kotlinx.coroutines.*

class FollowingsFragment(val userId: String) : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        parent: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.followers_fragment, parent, false)
    }

    private var jobs: MutableList<Job> = mutableListOf()

    override fun onDestroy() {
        super.onDestroy()
        jobs.forEach { job ->
            job.cancel()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        var loading = true
        val followings = FollowersResponse(mutableListOf(), 0)
        follower_list.run {
            layoutManager = LinearLayoutManager(this@FollowingsFragment.context)
            adapter = FollowingAdapter(followings.followers)
            addItemDecoration(
                DividerItemDecoration(
                    context,
                    DividerItemDecoration.VERTICAL
                )
            )
        }
        follower_list.addOnScrollListener(object :
            PaginationScrollListener(follower_list.layoutManager as LinearLayoutManager) {
            override fun isLastPage(): Boolean {
                return followings.totalResults == followings.followers.size
            }

            override fun isLoading(): Boolean {
                return loading
            }

            override fun loadMoreItems() {
                loading = true
                jobs.add(GlobalScope.launch(Dispatchers.Default) {
                    val size = followings.followers.size
                    val followersResponse = CloudCodingNetworkManager.getFollowers(
                        GetFollowersRequest(
                            userId,
                            25,
                            0
                        )
                    )
                    followings.followers.addAll(followersResponse.followers)
                    followings.totalResults = followersResponse.totalResults
                    withContext(Dispatchers.Main) {
                        loading = false
                        follower_list.adapter?.notifyItemRangeInserted(
                            size,
                            followersResponse.followers.size
                        )
                    }
                })
            }

            override fun isReversed(): Boolean {
                return false
            }

            override fun getTotalPageCount(): Int {
                return 0
            }
        })
        jobs.add(GlobalScope.launch(Dispatchers.Default) {
            val followingsResponse =
                CloudCodingNetworkManager.getFollowings(GetFollowersRequest(userId, 25, 0))
            followings.followers.addAll(followingsResponse.followers)
            followings.totalResults = followingsResponse.totalResults
            withContext(Dispatchers.Main) {
                follower_list.adapter?.notifyItemRangeInserted(0, followings.followers.size)
            }
        })
    }
}