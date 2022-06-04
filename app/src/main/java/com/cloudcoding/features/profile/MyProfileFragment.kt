package com.cloudcoding.features.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.cloudcoding.R
import com.cloudcoding.api.CloudCodingNetworkManager
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.my_profile_fragment.*
import kotlinx.android.synthetic.main.my_profile_fragment.name
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
        return inflater.inflate(R.layout.my_profile_fragment, parent, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        activity?.onBackPressedDispatcher?.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    findNavController().popBackStack()
                }
            })
        GlobalScope.launch(Dispatchers.Default) {
            val me = CloudCodingNetworkManager.getMe()
            withContext(Dispatchers.Main) {
                name.text = getString(R.string.name, me.firstname, me.lastname)
                username.text = me.username
            }
        }
        GlobalScope.launch(Dispatchers.Default) {
            val ownedGroups = CloudCodingNetworkManager.getOwnedGroups()
            val joinedGroups = CloudCodingNetworkManager.getJoinedGroups()
            val groups = mutableListOf<Any>()
            groups.addAll(ownedGroups)
            groups.addAll(joinedGroups)
            withContext(Dispatchers.Main) {
                viewpager.adapter = MyProfileAdapter(groups, this@MyProfileFragment)
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