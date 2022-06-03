package com.cloudcoding.features.profile

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.cloudcoding.api.request.GetCommentsRequest
import com.cloudcoding.api.response.CommentsResponse
import com.cloudcoding.features.comments.CommentsFragment
import com.cloudcoding.features.groups.GroupsFragment
import com.cloudcoding.models.Comment

class ProfileAdapter(private var getComments: suspend (GetCommentsRequest) -> CommentsResponse, fa: Fragment) : FragmentStateAdapter(fa) {
    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> CommentsFragment(getComments)
            else -> GroupsFragment()
        }
    }
}