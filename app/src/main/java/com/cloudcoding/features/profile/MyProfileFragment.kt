package com.cloudcoding.features.profile

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
import kotlinx.android.synthetic.main.profile_fragment.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MyProfileFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        parent: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.profile_fragment, parent, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        activity?.onBackPressedDispatcher?.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    findNavController().popBackStack()
                }
            })
        name.text=""
        GlobalScope.launch(Dispatchers.Default) {
            withContext(Dispatchers.Main) {
                viewpager.adapter = MyProfileAdapter(this@MyProfileFragment)
                TabLayoutMediator(tabLayout, viewpager) { tab: TabLayout.Tab, i: Int ->
                    when (i) {
                        0 -> tab.text = "Comments"
                        1 -> tab.text = "Projects"
                        else -> tab.text = "Groups"
                    }
                }.attach()
            }
        }
    }
}