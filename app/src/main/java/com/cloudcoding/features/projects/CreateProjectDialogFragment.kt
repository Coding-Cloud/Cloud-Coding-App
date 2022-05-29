package com.cloudcoding.features.projects

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.cloudcoding.R
import kotlinx.android.synthetic.main.create_project_dialog_fragment.*


class CreateProjectDialogFragment : DialogFragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.create_project_dialog_fragment, container, false)
    }

    companion object {
        const val TAG = "CreateProjectDialog"
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        cancel.setOnClickListener {
            this.dismiss()
        }
        create.setOnClickListener {

        }
    }
}