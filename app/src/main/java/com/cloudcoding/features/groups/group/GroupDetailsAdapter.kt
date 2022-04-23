package com.cloudcoding.features.groups.group

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.cloudcoding.features.home.PostsFragment
import com.cloudcoding.features.members.MembersFragment
import com.cloudcoding.features.projects.ProjectsFragment

class GroupDetailsAdapter(fa: Fragment) : FragmentStateAdapter(fa) {
    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> PostsFragment()
            1 -> ProjectsFragment()
            else -> MembersFragment()
        }
    }
}