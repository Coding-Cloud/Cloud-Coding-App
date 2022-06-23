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
import com.cloudcoding.api.CloudCodingNetworkManager
import kotlinx.android.synthetic.main.my_projects_fragment.*
import kotlinx.android.synthetic.main.projects_fragment.project_list
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MyProjectsFragment : Fragment() {
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


        GlobalScope.launch(Dispatchers.Default) {
            val projects = CloudCodingNetworkManager.getOwnedProjects()
            withContext(Dispatchers.Main) {
                project_list.run {
                    layoutManager = LinearLayoutManager(this@MyProjectsFragment.context)
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
                add.setOnClickListener {
                    val dialog = CreateProjectDialogFragment { project ->
                        projects.add(project)
                        project_list.adapter!!.notifyItemInserted(projects.size - 1)
                    }
                    dialog.show(
                        childFragmentManager, CreateProjectDialogFragment.TAG
                    )
                }
            }
        }
    }
}