package com.cloudcoding.features.groups

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.cloudcoding.R
import kotlinx.android.synthetic.main.groups_fragment.*

class GroupsFragment : Fragment() {
    private val groupsModel: GroupsModel by activityViewModels()
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
        val groups = groupsModel.getGroups().value!!
        val action = groupsModel.getAction().value!!
        group_list.run {
            layoutManager = LinearLayoutManager(this@GroupsFragment.context)
            adapter = GroupAdapter(groups, action)
            addItemDecoration(
                DividerItemDecoration(
                    context,
                    DividerItemDecoration.VERTICAL
                )
            )
        }
    }
}

class GroupsModel : ViewModel() {
    private val selectGroups = MutableLiveData<MutableList<Any>>()
    private val selectAction = MutableLiveData<Int>()

    fun selectGroups(groups: MutableList<Any>) {
        selectGroups.value = groups
    }
    fun getGroups(): MutableLiveData<MutableList<Any>> {
        return selectGroups
    }

    fun selectAction(action: Int) {
        selectAction.value = action
    }

    fun getAction(): MutableLiveData<Int> {
        return selectAction
    }
}