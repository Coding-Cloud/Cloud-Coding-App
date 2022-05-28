package com.cloudcoding.features.projects.details

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
import com.cloudcoding.features.comments.CommentAdapter
import com.cloudcoding.features.projects.ProjectAdapter
import com.cloudcoding.models.ProjectLanguage
import kotlinx.android.synthetic.main.project_details_fragment.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ProjectDetailsFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        parent: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.project_details_fragment, parent, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        activity?.onBackPressedDispatcher?.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    findNavController().popBackStack()
                }
            })
        val projectId = requireArguments().getString("projectId")!!

        GlobalScope.launch(Dispatchers.Default) {
            val project = CloudCodingNetworkManager.getProjectById(projectId)
            val comments = CloudCodingNetworkManager.getProjectComments(projectId).comments
            withContext(Dispatchers.Main) {
                status.text = project.status.toString()
                title.text = project.name
                when (project.language) {
                    ProjectLanguage.ANGULAR -> language_thumbnail.setImageResource(R.drawable.ic_angular)
                    ProjectLanguage.QUARKUS -> language_thumbnail.setImageResource(R.drawable.ic_java)
                    else -> language_thumbnail.setImageResource(R.drawable.ic_react)
                }
                comment_list.run{
                    layoutManager = LinearLayoutManager(this@ProjectDetailsFragment.context)
                    adapter = CommentAdapter(comments)
                    addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
                }
            }
        }
    }
}