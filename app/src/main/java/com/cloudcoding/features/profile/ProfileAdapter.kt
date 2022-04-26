package com.cloudcoding.features.profile

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.cloudcoding.features.groups.GroupsFragment
import com.cloudcoding.features.home.PostsFragment
import com.cloudcoding.features.members.MembersFragment
import com.cloudcoding.features.projects.ProjectsFragment

class ProfileAdapter(fa: Fragment) : FragmentStateAdapter(fa) {
    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> PostsFragment()
            else -> GroupsFragment()
        }
    }
}