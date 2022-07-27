package com.cloudcoding.features.profile

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.cloudcoding.R
import com.cloudcoding.features.comments.ProfileCommentsFragment
import com.cloudcoding.features.groups.GroupsFragment
import com.cloudcoding.features.projects.ProjectsFragment

class ProfileAdapter(fa: Fragment) : FragmentStateAdapter(fa) {
    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> ProfileCommentsFragment()
            1 -> ProjectsFragment()
            else -> GroupsFragment()
        }
    }
}