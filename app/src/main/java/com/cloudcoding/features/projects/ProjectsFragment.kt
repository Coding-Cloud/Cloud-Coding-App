package com.cloudcoding.features.projects

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
import com.cloudcoding.models.Project
import kotlinx.android.synthetic.main.projects_fragment.*

class ProjectsFragment : Fragment() {
    private val projectsModel: ProjectsModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater,
        parent: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.projects_fragment, parent, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        activity?.onBackPressedDispatcher?.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    findNavController().popBackStack()
                }
            })
        val projects = projectsModel.getProjects().value!!
        val action = projectsModel.getAction().value!!
        project_list.run {
            layoutManager = LinearLayoutManager(this@ProjectsFragment.context)
            adapter = ProjectAdapter(
                projects,
                action
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

class ProjectsModel : ViewModel() {
    private val selectProjects = MutableLiveData<MutableList<Project>>()
    private val selectAction = MutableLiveData<Int>()
    fun selectProjects(projects: MutableList<Project>) {
        selectProjects.value = projects
    }

    fun getProjects(): MutableLiveData<MutableList<Project>> {
        return selectProjects
    }

    fun selectAction(action: Int) {
        selectAction.value = action
    }

    fun getAction(): MutableLiveData<Int> {
        return selectAction
    }
}