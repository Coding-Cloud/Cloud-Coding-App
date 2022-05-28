package com.cloudcoding.features.projects

import android.content.Context
import android.view.ContextMenu
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cloudcoding.MainActivity
import com.cloudcoding.R
import com.cloudcoding.api.CloudCodingNetworkManager
import com.cloudcoding.api.SocketManager
import com.cloudcoding.models.Project
import com.cloudcoding.models.ProjectLanguage
import kotlinx.android.synthetic.main.projects_fragment.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ProjectAdapter(val projects: MutableList<Project>, private val action: Int) :
    RecyclerView.Adapter<ProjectItem>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProjectItem {
        return ProjectItem(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.project_item, parent, false)
        )
    }

    override fun onBindViewHolder(cell: ProjectItem, position: Int) {
        cell.itemView.setOnClickListener {
            cell.itemView
                .findNavController()
                .navigate(action)
        }

        val preference = MainActivity.getContext().getSharedPreferences(
            cell.itemView.context.getString(R.string.me),
            Context.MODE_PRIVATE
        )!!
        val userId = preference.getString(cell.itemView.context.getString(R.string.me), "")!!
        cell.name.text = projects[position].name
        when (projects[position].language) {
            ProjectLanguage.ANGULAR -> cell.languageThumbnail.setImageResource(R.drawable.ic_angular)
            ProjectLanguage.QUARKUS -> cell.languageThumbnail.setImageResource(R.drawable.ic_java)
            else -> cell.languageThumbnail.setImageResource(R.drawable.ic_react)
        }
        if(projects[position].creatorId == userId) {
            cell.itemView.setOnCreateContextMenuListener { menu: ContextMenu, _: View, _: ContextMenu.ContextMenuInfo? ->
                menu.add("delete").setOnMenuItemClickListener {
                    GlobalScope.launch(Dispatchers.Default) {
                        CloudCodingNetworkManager.deleteProject(projects[position].id)
                        withContext(Dispatchers.Main) {
                            projects.removeAt(position)
                            notifyItemRemoved(position)
                        }
                    }
                    true
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return projects.size
    }
}