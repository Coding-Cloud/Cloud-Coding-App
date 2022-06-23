package com.cloudcoding.features.comments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.cloudcoding.R
import com.cloudcoding.api.CloudCodingNetworkManager
import com.cloudcoding.api.request.GetCommentsRequest
import com.cloudcoding.api.response.CommentsResponse
import com.cloudcoding.utils.PaginationScrollListener
import kotlinx.android.synthetic.main.comments_fragment.*
import kotlinx.coroutines.*

class ProfileCommentsFragment(val userId: String) :
    Fragment() {
    private var loading = false
    override fun onCreateView(
        inflater: LayoutInflater,
        parent: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.comments_fragment, parent, false)
    }

    private var jobs: MutableList<Job> = mutableListOf()

    override fun onDestroy() {
        super.onDestroy()
        jobs.forEach { job ->
            job.cancel()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        activity?.onBackPressedDispatcher?.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    findNavController().popBackStack()
                }
            })
        var comments: CommentsResponse
        jobs.add(GlobalScope.launch(Dispatchers.Default) {
            comments = CloudCodingNetworkManager.getUserComments(
                GetCommentsRequest(
                    userId,
                    null,
                    25,
                    0
                )
            )
            withContext(Dispatchers.Main) {
                comment_list.run {
                    layoutManager = LinearLayoutManager(this@ProfileCommentsFragment.context)
                    adapter = CommentAdapter(comments.comments)
                    addItemDecoration(
                        DividerItemDecoration(
                            context,
                            DividerItemDecoration.VERTICAL
                        )
                    )
                }
            }
            comment_list.addOnScrollListener(object :
                PaginationScrollListener(comment_list.layoutManager as LinearLayoutManager) {
                override fun isLastPage(): Boolean {
                    return comments.totalResults == comments.comments.size
                }

                override fun isLoading(): Boolean {
                    return loading
                }

                override fun loadMoreItems() {
                    loading = true
                    jobs.add(GlobalScope.launch(Dispatchers.Default) {
                        val commentsResponse = CloudCodingNetworkManager.getUserComments(
                            GetCommentsRequest(
                                userId,
                                null,
                                25,
                                comments.comments.size
                            )
                        )
                        withContext(Dispatchers.Main) {
                            val oldSize = comments.comments.size
                            comments.comments.addAll(commentsResponse.comments)
                            comments.totalResults = commentsResponse.totalResults
                            comment_list.adapter?.notifyItemRangeInserted(
                                oldSize,
                                comments.comments.size
                            )
                            loading = false
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
        })

    }
}