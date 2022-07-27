package com.cloudcoding.features.follow

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.cloudcoding.features.follow.follower.FollowersFragment
import com.cloudcoding.features.follow.following.FollowingsFragment

class FollowAdapter(fa: Fragment) : FragmentStateAdapter(fa) {
    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> FollowersFragment()
            else -> FollowingsFragment()
        }
    }
}