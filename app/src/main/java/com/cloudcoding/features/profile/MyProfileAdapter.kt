package com.cloudcoding.features.profile

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.cloudcoding.R
import com.cloudcoding.features.comments.MyCommentsFragment
import com.cloudcoding.features.groups.GroupsFragment
import com.cloudcoding.features.projects.MyProfileProjectsFragment

class MyProfileAdapter(val groups: MutableList<Any>, fa: Fragment) : FragmentStateAdapter(fa) {
    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> MyCommentsFragment()
            1 -> MyProfileProjectsFragment()
            else -> GroupsFragment(groups, R.id.action_nav_item_my_profile_to_groupDetailsFragment)
        }
    }
}