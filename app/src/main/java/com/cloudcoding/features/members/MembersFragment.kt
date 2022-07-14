package com.cloudcoding.features.members

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.cloudcoding.R
import com.cloudcoding.features.groups.GroupsModel
import com.cloudcoding.models.GroupMembership
import com.cloudcoding.models.Project
import kotlinx.android.synthetic.main.members_fragment.*

class MembersFragment : Fragment() {
    private val membersModel: MembersModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater,
        parent: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.members_fragment, parent, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val members = membersModel.getMembers().value!!
        member_list.run {
            layoutManager = LinearLayoutManager(this@MembersFragment.context)
            adapter = MemberAdapter(members, R.id.action_groupDetailsFragment_to_nav_item_profile)
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        }
    }
}
class MembersModel : ViewModel() {
    private val selectMembers = MutableLiveData<MutableList<GroupMembership>>()

    fun selectMembers(members: MutableList<GroupMembership>) {
        selectMembers.value = members
    }

    fun getMembers(): MutableLiveData<MutableList<GroupMembership>> {
        return selectMembers
    }
}