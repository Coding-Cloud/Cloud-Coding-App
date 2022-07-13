package com.cloudcoding.features.search

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class SearchAdapter(fa: Fragment) : FragmentStateAdapter(fa) {
    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> SearchUsersFragment()
            else -> SearchProjectsFragment()
        }
    }
}