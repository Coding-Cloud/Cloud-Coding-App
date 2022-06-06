package com.cloudcoding.features.follow

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.cloudcoding.R
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.my_profile_fragment.*

class FollowFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        parent: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.follow_fragment, parent, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val userId = requireArguments().getString("userId")!!
        viewpager.adapter = FollowAdapter(userId, this@FollowFragment)
        TabLayoutMediator(tabLayout, viewpager) { tab: TabLayout.Tab, i: Int ->
            when (i) {
                0 -> tab.text = "Followers"
                else -> tab.text = "Followings"
            }
        }.attach()
    }
}