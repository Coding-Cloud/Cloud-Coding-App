package com.cloudcoding.features.projects

import android.content.Context
import android.view.ContextMenu
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.cloudcoding.MainActivity
import com.cloudcoding.R
import com.cloudcoding.api.CloudCodingNetworkManager
import com.cloudcoding.models.Project
import com.cloudcoding.models.ProjectLanguage
import kotlinx.coroutines.*

class ProjectAdapter(val projects: MutableList<Project>, private val action: Int) :
    RecyclerView.Adapter<ProjectItem>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProjectItem {
        return ProjectItem(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.project_item, parent, false)
        )
    }

    private var jobs: MutableList<Job> = mutableListOf()

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)
        jobs.forEach { job ->
            job.cancel()
        }
    }

    override fun onBindViewHolder(cell: ProjectItem, position: Int) {
        cell.itemView.setOnClickListener {
            cell.itemView
                .findNavController()
                .navigate(action, bundleOf("projectId" to projects[position].id))
        }

        val preference = MainActivity.getContext().getSharedPreferences(
            cell.itemView.context.getString(R.string.me),
            Context.MODE_PRIVATE
        )!!
        val userId = preference.getString(cell.itemView.context.getString(R.string.me), "")!!
        cell.name.text = projects[position].name
        when (projects[position].language) {
            ProjectLanguage.ANGULAR -> cell.languageThumbnail.setImageResource(R.drawable.ic_angular)
            ProjectLanguage.QUARKUS -> cell.languageThumbnail.setImageResource(R.drawable.ic_quarkus)
            else -> cell.languageThumbnail.setImageResource(R.drawable.ic_react)
        }
        if (projects[position].creatorId == userId) {
            cell.itemView.setOnCreateContextMenuListener { menu: ContextMenu, _: View, _: ContextMenu.ContextMenuInfo? ->
                menu.add("delete").setOnMenuItemClickListener {
                    jobs.add(GlobalScope.launch(Dispatchers.Default) {
                        CloudCodingNetworkManager.deleteProject(projects[position].id)
                        withContext(Dispatchers.Main) {
                            projects.removeAt(position)
                            notifyItemRemoved(position)
                        }
                    })
                    true
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return projects.size
    }
}