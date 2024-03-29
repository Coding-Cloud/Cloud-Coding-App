package com.cloudcoding.features.groups.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.request.RequestOptions
import com.cloudcoding.R
import com.cloudcoding.api.CloudCodingNetworkManager
import com.cloudcoding.features.members.MembersModel
import com.cloudcoding.features.projects.ProjectsModel
import com.cloudcoding.setTextBold
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.group_details_fragment.*
import kotlinx.coroutines.*

class GroupDetailsFragment : Fragment() {
    private val membersModel: MembersModel by activityViewModels()
    private val projectsModel: ProjectsModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater,
        parent: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.group_details_fragment, parent, false)
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
        val groupId = requireArguments().getString("groupId")!!
        jobs.add(GlobalScope.launch(Dispatchers.Default) {
            val projects = CloudCodingNetworkManager.getGroupProjects(groupId)
            val memberList = CloudCodingNetworkManager.getGroupMembers(groupId)
            val group = CloudCodingNetworkManager.getGroupById(groupId)
            withContext(Dispatchers.Main) {
                name.text = group.name
                members.setTextBold(getString(R.string.members_count, memberList.size))

                projectsModel.selectProjects(projects)
                projectsModel.selectAction(R.id.action_groupDetailsFragment_to_projectDetailsFragment)
                membersModel.selectMembers(memberList)
                viewpager.adapter =
                    GroupDetailsAdapter(this@GroupDetailsFragment)
                TabLayoutMediator(tabLayout, viewpager) { tab: TabLayout.Tab, i: Int ->
                    when (i) {
                        0 -> tab.text = "Projects"
                        else -> tab.text = "Members"
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
}