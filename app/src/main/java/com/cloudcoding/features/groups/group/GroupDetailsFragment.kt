package com.cloudcoding.features.groups.group

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.cloudcoding.R
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.group_details_fragment.*

class GroupDetailsFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        parent: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.group_details_fragment, parent, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        activity?.onBackPressedDispatcher?.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    findNavController().popBackStack()
                }
            })
        viewpager.adapter = GroupDetailsAdapter(this)
        TabLayoutMediator(tabLayout, viewpager) { tab: TabLayout.Tab, i: Int ->
            println(i)
            when (i) {
                0 -> tab.text = "Posts"
                1 -> tab.text = "Projects"
                else -> tab.text = "Members"
            }
        }.attach()
    }
}