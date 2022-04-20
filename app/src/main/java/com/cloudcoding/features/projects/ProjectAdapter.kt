package com.cloudcoding.features.projects

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.cloudcoding.R

class ProjectAdapter : RecyclerView.Adapter<ProjectItem>() {
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
                .navigate(R.id.action_projectsFragment_to_projectDetailsFragment)
        }
        cell.languageThumbnail.setImageResource(R.drawable.ic_angular)
    }

    override fun getItemCount(): Int {
        return 10
    }
}