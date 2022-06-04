package com.cloudcoding.features.groups.details

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.cloudcoding.features.members.MembersFragment
import com.cloudcoding.features.projects.ProjectsFragment
import com.cloudcoding.models.GroupMembership
import com.cloudcoding.models.Project

class GroupDetailsAdapter(val projects: MutableList<Project>,val members: MutableList<GroupMembership>, fa: Fragment) : FragmentStateAdapter(fa) {
    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> ProjectsFragment(projects)
            else -> MembersFragment(members)
        }
    }
}