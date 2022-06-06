package com.cloudcoding.features.followers

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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FollowingsFragment(val userId: String) : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        parent: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.followers_fragment, parent, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        var loading = true
        var followers = FollowersResponse(mutableListOf(), 0)
        follower_list.run {
            layoutManager = LinearLayoutManager(this@FollowingsFragment.context)
            adapter = FollowingAdapter(followers.followers)
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
                return followers.totalResults == followers.followers.size
            }

            override fun isLoading(): Boolean {
                return loading
            }

            override fun loadMoreItems() {
                loading = true
                GlobalScope.launch(Dispatchers.Default) {
                    val size = followers.followers.size
                    val followersResponse = CloudCodingNetworkManager.getFollowers(
                        GetFollowersRequest(
                            userId,
                            25,
                            0
                        )
                    )
                    followers.followers.addAll(followersResponse.followers)
                    followers.totalResults = followersResponse.totalResults
                    withContext(Dispatchers.Main) {
                        loading = false
                        follower_list.adapter?.notifyItemRangeInserted(
                            size,
                            followersResponse.followers.size
                        )
                    }
                }
            }

            override fun isReversed(): Boolean {
                return false
            }

            override fun getTotalPageCount(): Int {
                return 0
            }
        })
        GlobalScope.launch(Dispatchers.Default) {
            followers = CloudCodingNetworkManager.getFollowings(GetFollowersRequest(userId, 25, 0))
            withContext(Dispatchers.Main) {
                follower_list.adapter?.notifyItemRangeInserted(0, followers.followers.size)
            }
        }
    }
}