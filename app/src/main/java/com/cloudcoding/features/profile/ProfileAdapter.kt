package com.cloudcoding.features.profile

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.cloudcoding.R
import com.cloudcoding.features.comments.ProfileCommentsFragment
import com.cloudcoding.features.groups.GroupsFragment
import com.cloudcoding.features.projects.ProjectsFragment
import com.cloudcoding.models.Project

class ProfileAdapter(
    val userId: String,
    val projects: MutableList<Project>,
    val groups: MutableList<Any>,
    fa: Fragment
) : FragmentStateAdapter(fa) {
    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> ProfileCommentsFragment(userId)
            1 -> ProjectsFragment(projects, R.id.action_nav_item_profile_to_projectDetailsFragment)
            else -> GroupsFragment(groups, R.id.action_nav_item_profile_to_groupDetailsFragment)
        }
    }
}