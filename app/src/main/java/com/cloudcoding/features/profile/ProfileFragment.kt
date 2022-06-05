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
import kotlinx.android.synthetic.main.profile_fragment.*
import kotlinx.android.synthetic.main.profile_fragment.followers
import kotlinx.android.synthetic.main.profile_fragment.followers_count
import kotlinx.android.synthetic.main.profile_fragment.followings
import kotlinx.android.synthetic.main.profile_fragment.followings_count
import kotlinx.android.synthetic.main.profile_fragment.name
import kotlinx.android.synthetic.main.profile_fragment.tabLayout
import kotlinx.android.synthetic.main.profile_fragment.username
import kotlinx.android.synthetic.main.profile_fragment.viewpager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ProfileFragment : Fragment() {
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
        val userId = requireArguments().getString("userId")!!
        GlobalScope.launch(Dispatchers.Default) {
            val user = CloudCodingNetworkManager.getUserById(userId)
            val followers = CloudCodingNetworkManager.getFollowers(userId)
            val followings = CloudCodingNetworkManager.getFollowings(userId)
            val groups = mutableListOf<Any>()
//            val groupMemberships = CloudCodingNetworkManager.getUserGroups(userId)
//            groups.addAll(groupMemberships.map { CloudCodingNetworkManager.getGroupById(it.groupId) })
            withContext(Dispatchers.Main) {
                name.text = getString(R.string.name, user.firstname, user.lastname)
                username.text = getString(R.string.username, user.username)
                followers_count.text = followers.totalResults.toString()
                followings_count.text = followings.totalResults.toString()
                viewpager.adapter = MyProfileAdapter(groups, this@ProfileFragment)
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