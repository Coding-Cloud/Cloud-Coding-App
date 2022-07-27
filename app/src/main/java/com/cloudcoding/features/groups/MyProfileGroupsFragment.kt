package com.cloudcoding.features.groups

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.cloudcoding.R
import com.cloudcoding.api.CloudCodingNetworkManager
import kotlinx.android.synthetic.main.groups_fragment.*
import kotlinx.coroutines.*

class MyProfileGroupsFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        parent: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.groups_fragment, parent, false)
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

        jobs.add(GlobalScope.launch(Dispatchers.Default) {
            val groups = mutableListOf<Any>()
            val ownedGroups = CloudCodingNetworkManager.getOwnedGroups()
            val joinedGroups = CloudCodingNetworkManager.getJoinedGroups()
            groups.addAll(ownedGroups)
            groups.addAll(joinedGroups)
            withContext(Dispatchers.Main) {
                group_list.run {
                    layoutManager = LinearLayoutManager(this@MyProfileGroupsFragment.context)
                    adapter =
                        GroupAdapter(groups, R.id.action_nav_item_profile_to_groupDetailsFragment)
                    addItemDecoration(
                        DividerItemDecoration(
                            context,
                            DividerItemDecoration.VERTICAL
                        )
                    )
                }
            }
        })
    }
}