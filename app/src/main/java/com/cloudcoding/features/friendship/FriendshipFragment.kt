package com.cloudcoding.features.friendship

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.cloudcoding.R
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.friendship_fragment.*

class FriendshipFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        parent: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.friendship_fragment, parent, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewpager.adapter = FriendshipAdapter(this@FriendshipFragment)
        TabLayoutMediator(tabLayout, viewpager) { tab: TabLayout.Tab, i: Int ->
            when (i) {
                0 -> tab.text = "Friends"
                else -> tab.text = "Friend Requests"
            }
        }.attach()
    }
}