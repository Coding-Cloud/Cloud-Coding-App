package com.cloudcoding.features.profile

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.cloudcoding.MainActivity
import com.cloudcoding.R
import com.cloudcoding.api.CloudCodingNetworkManager
import com.cloudcoding.api.request.GetFollowersRequest
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.profile_fragment.*
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
        val preference = MainActivity.getContext().getSharedPreferences(
            getString(R.string.me),
            Context.MODE_PRIVATE
        )!!
        val currentUserId = preference.getString(getString(R.string.me), "")!!
        val userId = arguments?.getString("userId") ?: currentUserId
        followers.setOnClickListener {
            findNavController()
                .navigate(
                    R.id.action_nav_item_profile_to_followFragment,
                    bundleOf("userId" to userId)
                )
        }
        followings.setOnClickListener {
            findNavController()
                .navigate(
                    R.id.action_nav_item_profile_to_followFragment,
                    bundleOf("userId" to userId)
                )
        }
        GlobalScope.launch(Dispatchers.Default) {
            val user = CloudCodingNetworkManager.getUserById(userId)
            val followerRequest = GetFollowersRequest(user.id, null, null)
            val followers = CloudCodingNetworkManager.getFollowers(followerRequest)
            val followings = CloudCodingNetworkManager.getFollowings(followerRequest)
            val projects = CloudCodingNetworkManager.getUserProjects(userId)
            val groups = mutableListOf<Any>()
            val groupMemberships = CloudCodingNetworkManager.getUserGroups(userId)
            if (userId == currentUserId) {
                val ownedGroups = CloudCodingNetworkManager.getOwnedGroups()
                val joinedGroups = CloudCodingNetworkManager.getJoinedGroups()
                groups.addAll(ownedGroups)
                groups.addAll(joinedGroups)
            } else {
                groups.addAll(groupMemberships.map { CloudCodingNetworkManager.getGroupById(it.groupId) })
            }
            withContext(Dispatchers.Main) {
                name.text = getString(R.string.name, user.firstname, user.lastname)
                username.text = getString(R.string.username, user.username)
                followers_count.text = followers.totalResults.toString()
                followings_count.text = followings.totalResults.toString()
                if (userId == currentUserId) {
                    viewpager.adapter = MyProfileAdapter(groups, this@ProfileFragment)
                } else {
                    viewpager.adapter =
                        ProfileAdapter(userId, projects, groups, this@ProfileFragment)
                }
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