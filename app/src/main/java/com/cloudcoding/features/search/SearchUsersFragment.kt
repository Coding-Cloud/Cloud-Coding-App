package com.cloudcoding.features.search

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.cloudcoding.R
import com.cloudcoding.api.CloudCodingNetworkManager
import com.cloudcoding.api.response.UsersResponse
import com.cloudcoding.utils.PaginationScrollListener
import kotlinx.android.synthetic.main.followers_fragment.*
import kotlinx.android.synthetic.main.users_fragment.*
import kotlinx.coroutines.*

class SearchUsersFragment : Fragment() {
    private val model: SearchModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater,
        parent: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.users_fragment, parent, false)
    }

    private var jobs: MutableList<Job> = mutableListOf()

    override fun onDestroy() {
        super.onDestroy()
        jobs.forEach { job ->
            job.cancel()
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        var loading = true
        val users = UsersResponse(mutableListOf(), 0)
        user_list.run {
            layoutManager = LinearLayoutManager(this@SearchUsersFragment.context)
            adapter = UsersAdapter(users.users, 0)//TODO
            addItemDecoration(
                DividerItemDecoration(
                    context,
                    DividerItemDecoration.VERTICAL
                )
            )
        }
        model.getSelected().observe(viewLifecycleOwner) { search ->
            users.users.clear()
            users.totalResults = 0
            user_list.adapter?.notifyDataSetChanged()
            user_list.addOnScrollListener(object :
                PaginationScrollListener(user_list.layoutManager as LinearLayoutManager) {
                override fun isLastPage(): Boolean {
                    return users.totalResults == users.users.size
                }

                override fun isLoading(): Boolean {
                    return loading
                }

                override fun loadMoreItems() {
                    loading = true
                    jobs.add(GlobalScope.launch(Dispatchers.Default) {
                        val size = users.users.size
                        val usersResponse = CloudCodingNetworkManager.getUsers(
                            search,
                            25,
                            0
                        )
                        users.users.addAll(users.users)
                        users.totalResults = usersResponse.totalResults
                        withContext(Dispatchers.Main) {
                            loading = false
                            user_list.adapter?.notifyItemRangeInserted(
                                size,
                                usersResponse.users.size
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
                val usersResponse =
                    CloudCodingNetworkManager.getUsers(search, 25, 0)
                users.users.addAll(usersResponse.users)
                users.totalResults = usersResponse.totalResults
                withContext(Dispatchers.Main) {
                    user_list.adapter?.notifyItemRangeInserted(0, users.users.size)
                }
            })
        }
    }
}