package com.cloudcoding.features.groups

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.cloudcoding.R
import com.cloudcoding.features.projects.ProjectItem

class GroupAdapter : RecyclerView.Adapter<ProjectItem>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProjectItem {
        return ProjectItem(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.group_item, parent, false)
        )
    }

    override fun onBindViewHolder(cell: ProjectItem, position: Int) {
        cell.itemView.setOnClickListener {
            cell.itemView
                .findNavController()
                .navigate(R.id.action_groupsFragment_to_groupDetailsFragment)
        }
        cell.languageThumbnail.setImageResource(R.drawable.ic_angular)
    }

    override fun getItemCount(): Int {
        return 10
    }
}