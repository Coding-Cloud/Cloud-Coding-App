package com.cloudcoding.features.groups.details

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
import kotlinx.android.synthetic.main.group_details_fragment.*
import kotlinx.coroutines.*

class GroupDetailsFragment : Fragment() {
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
            val members = CloudCodingNetworkManager.getGroupMembers(groupId)
            withContext(Dispatchers.Main) {
                viewpager.adapter =
                    GroupDetailsAdapter(projects, members, this@GroupDetailsFragment)
                TabLayoutMediator(tabLayout, viewpager) { tab: TabLayout.Tab, i: Int ->
                    when (i) {
                        0 -> tab.text = "Projects"
                        else -> tab.text = "Members"
                    }
                }.attach()
            }
        })
    }
}