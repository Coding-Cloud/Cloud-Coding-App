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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MyGroupsFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        parent: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.groups_fragment, parent, false)
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
            val ownedGroups = CloudCodingNetworkManager.getOwnedGroups()
            val joinedGroups = CloudCodingNetworkManager.getJoinedGroups()
            val groups = mutableListOf<Any>()
            if (ownedGroups.size > 0) {
                groups.add(getString(R.string.owned_groups))
                groups.addAll(ownedGroups)
            }
            if (joinedGroups.size > 0) {
                groups.add(getString(R.string.joined_groups))
                groups.addAll(joinedGroups)
            }
            withContext(Dispatchers.Main) {
                group_list.run {
                    layoutManager = LinearLayoutManager(this@MyGroupsFragment.context)
                    adapter =
                        GroupAdapter(groups, R.id.action_nav_item_groups_to_groupDetailsFragment)
                    addItemDecoration(
                        DividerItemDecoration(
                            context,
                            DividerItemDecoration.VERTICAL
                        )
                    )
                }
            }
        }
    }
}