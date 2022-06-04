package com.cloudcoding.features.projects

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
import com.cloudcoding.models.Project
import kotlinx.android.synthetic.main.my_projects_fragment.*
import kotlinx.android.synthetic.main.projects_fragment.project_list

class ProjectsFragment(val projects: MutableList<Project>) : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        parent: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.my_projects_fragment, parent, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        activity?.onBackPressedDispatcher?.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    findNavController().popBackStack()
                }
            })
        add.setOnClickListener {
            CreateProjectDialogFragment().show(
                childFragmentManager, CreateProjectDialogFragment.TAG
            )
        }
        project_list.run {
            layoutManager = LinearLayoutManager(this@ProjectsFragment.context)
            adapter = ProjectAdapter(
                projects,
                R.id.action_myProjectsFragment_to_projectDetailsFragment
            )
            addItemDecoration(
                DividerItemDecoration(
                    context,
                    DividerItemDecoration.VERTICAL
                )
            )
        }
    }
}