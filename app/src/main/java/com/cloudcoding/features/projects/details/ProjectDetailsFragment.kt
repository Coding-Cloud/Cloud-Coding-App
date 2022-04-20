package com.cloudcoding.features.projects.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.cloudcoding.R

class ProjectDetailsFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        parent: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.project_details_fragment, parent, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

    }
}