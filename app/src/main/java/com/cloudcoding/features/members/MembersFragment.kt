package com.cloudcoding.features.members

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.cloudcoding.R
import com.cloudcoding.models.GroupMembership
import kotlinx.android.synthetic.main.members_fragment.*

class MembersFragment(val members: MutableList<GroupMembership>) : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        parent: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.members_fragment, parent, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        member_list.run {
            layoutManager = LinearLayoutManager(this@MembersFragment.context)
            adapter = MemberAdapter(members, R.id.action_groupDetailsFragment_to_profileFragment)
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        }
    }
}