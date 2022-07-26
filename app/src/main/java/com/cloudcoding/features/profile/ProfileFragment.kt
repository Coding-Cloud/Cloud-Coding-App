package com.cloudcoding.features.profile

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.request.RequestOptions
import com.cloudcoding.MainActivity
import com.cloudcoding.R
import com.cloudcoding.api.CloudCodingNetworkManager
import com.cloudcoding.api.request.GetFollowersRequest
import com.cloudcoding.features.comments.ProfileCommentsModel
import com.cloudcoding.features.groups.GroupsModel
import com.cloudcoding.features.projects.ProjectsModel
import com.cloudcoding.setTextBold
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.profile_fragment.*
import kotlinx.coroutines.*

class ProfileFragment : Fragment() {
    private val groupsModel: GroupsModel by activityViewModels()
    private val projectsModel: ProjectsModel by activityViewModels()
    private val commentsModel: ProfileCommentsModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater,
        parent: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.profile_fragment, parent, false)
    }

    private var jobs: MutableList<Job> = mutableListOf()

    override fun onDestroy() {
        super.onDestroy()
        jobs.forEach { job ->
            job.cancel()
        }
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
        var isFollowed = false
        val currentUserId = preference.getString(getString(R.string.me), "")!!
        val userId = arguments?.getString("userId") ?: currentUserId
        if (userId == currentUserId) {
            follow_button.visibility = View.GONE
            friend_button.visibility = View.GONE
        } else {
            friendDisplay(userId)
            follow_button.setOnClickListener {
                isFollowed = !isFollowed
                if (isFollowed) {
                    follow_button.text = getString(R.string.unfollow)
                    follow_button.setBackgroundColor(requireContext().getColor(R.color.colorDanger))
                    follow_button.setTextColor(requireContext().getColor(R.color.colorOnDanger))
                } else {
                    follow_button.text = getString(R.string.follow)
                    follow_button.setBackgroundColor(requireContext().getColor(R.color.colorAccent))
                    follow_button.setTextColor(requireContext().getColor(R.color.colorOnAccent))
                }
                jobs.add(GlobalScope.launch(Dispatchers.Default) {
                    if (isFollowed) {
                        CloudCodingNetworkManager.follow(userId)
                    } else {
                        CloudCodingNetworkManager.unfollow(userId)
                    }
                })
            }
        }
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
        jobs.add(GlobalScope.launch(Dispatchers.Default) {
            val user = CloudCodingNetworkManager.getUserById(userId)
            val followerRequest = GetFollowersRequest(user.id, null, null)
            val followerList = CloudCodingNetworkManager.getFollowers(followerRequest)
            val followingList = CloudCodingNetworkManager.getFollowings(followerRequest)
            val projects = CloudCodingNetworkManager.getUserProjects(userId)
            val groups = mutableListOf<Any>()
            val groupMemberships = CloudCodingNetworkManager.getUserGroups(userId)

            groups.addAll(groupMemberships.map { CloudCodingNetworkManager.getGroupById(it.groupId) })
            isFollowed = CloudCodingNetworkManager.isFollowing(userId)
            withContext(Dispatchers.Main) {
                name.text = getString(R.string.user_name, user.firstname, user.lastname)
                username.text = getString(R.string.username, user.username)
                followers.setTextBold(
                    getString(
                        R.string.followers_count,
                        followerList.totalResults
                    )
                )
                followings.setTextBold(
                    getString(
                        R.string.followers_count,
                        followingList.totalResults
                    )
                )

                if (userId == currentUserId) {
                    viewpager.adapter = MyProfileAdapter(this@ProfileFragment)
                } else {
                    commentsModel.selectUserId(userId)
                    projectsModel.selectProjects(projects)
                    projectsModel.selectAction(R.id.action_nav_item_profile_to_projectDetailsFragment)
                    groupsModel.selectGroups(groups)
                    groupsModel.selectAction(R.id.action_nav_item_profile_to_groupDetailsFragment)
                    viewpager.adapter =
                        ProfileAdapter(this@ProfileFragment)
                    if (isFollowed) {
                        follow_button.text = getString(R.string.unfollow)
                    } else {
                        follow_button.text = getString(R.string.follow)
                    }
                }
                TabLayoutMediator(tabLayout, viewpager) { tab: TabLayout.Tab, i: Int ->
                    when (i) {
                        0 -> tab.text = "Comments"
                        1 -> tab.text = "Projects"
                        else -> tab.text = "Groups"
                    }
                }.attach()
            }
        })
        Glide.with(profile_picture)
            .load("https://i.pravatar.cc/100")
            .centerCrop()
            .apply(RequestOptions.bitmapTransform(CircleCrop()))
            .placeholder(R.drawable.ic_user)
            .into(profile_picture)
    }

    private fun friendDisplay(userId: String) {
        jobs.add(GlobalScope.launch(Dispatchers.Default) {
            val friendRequest = try {
                CloudCodingNetworkManager.getUserFriendRequest(userId)
            } catch (e: Exception) {
                null
            }
            var friendship = try {
                CloudCodingNetworkManager.getUserFriendship(userId)
            } catch (e: Exception) {
                null
            }
            withContext(Dispatchers.Main) {
                friend_button.setOnClickListener {
                    jobs.add(GlobalScope.launch(Dispatchers.Default) {
                        if (friendship != null) {
                            CloudCodingNetworkManager.removeFriend(friendship!!.id)
                        } else {
                            CloudCodingNetworkManager.sendFriendRequest(userId)
                        }
                        withContext(Dispatchers.Main) {
                            if (friendship != null) {
                                friend_button.setImageDrawable(
                                    AppCompatResources.getDrawable(
                                        requireContext(),
                                        R.drawable.ic_user_plus
                                    )
                                )
                                friend_button.setColorFilter(
                                    ContextCompat.getColor(
                                        requireContext(),
                                        R.color.colorAccent
                                    ), android.graphics.PorterDuff.Mode.SRC_IN
                                )
                                friendship = null
                            } else {
                                friend_button.visibility = View.GONE
                            }
                        }
                    })
                }
                if (friendship != null) {
                    friend_button.setImageDrawable(
                        AppCompatResources.getDrawable(
                            requireContext(),
                            R.drawable.ic_user_minus
                        )
                    )
                    friend_button.setColorFilter(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.colorDanger
                        ), android.graphics.PorterDuff.Mode.SRC_IN
                    )

                } else {
                    if (friendRequest != null) {
                        friend_button.visibility = View.GONE
                    } else {
                        friend_button.setImageDrawable(
                            AppCompatResources.getDrawable(
                                requireContext(),
                                R.drawable.ic_user_plus
                            )
                        )
                        friend_button.setColorFilter(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.colorAccent
                            ), android.graphics.PorterDuff.Mode.SRC_IN
                        )
                    }
                }
            }
        })
    }
}
