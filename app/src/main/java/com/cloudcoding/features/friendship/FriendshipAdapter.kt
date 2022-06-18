package com.cloudcoding.features.friendship

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.cloudcoding.features.follow.follower.FollowersFragment
import com.cloudcoding.features.follow.following.FollowingsFragment
import com.cloudcoding.features.friendship.friends.FriendsFragment
import com.cloudcoding.features.friendship.requests.FriendRequestsFragment

class FriendshipAdapter(fa: Fragment) : FragmentStateAdapter(fa) {
    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> FriendsFragment()
            else -> FriendRequestsFragment()
        }
    }
}