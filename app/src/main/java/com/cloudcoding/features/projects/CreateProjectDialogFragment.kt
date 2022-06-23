package com.cloudcoding.features.projects

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.cloudcoding.R
import com.cloudcoding.api.CloudCodingNetworkManager
import com.cloudcoding.api.request.CreateProjectRequest
import com.cloudcoding.models.ProjectLanguage
import com.cloudcoding.models.ProjectVisibility
import kotlinx.android.synthetic.main.create_project_dialog_fragment.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


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
            dismiss()
        }
        create.setOnClickListener {
            GlobalScope.launch(Dispatchers.Default) {
                CloudCodingNetworkManager.createProject(
                    CreateProjectRequest(
                        name_text.text.toString(),
                        ProjectLanguage.valueOf(language_spinner.selectedItem.toString()),
                        ProjectVisibility.valueOf(visibility_spinner.selectedItem.toString())
                    )
                )
                withContext(Dispatchers.Main) {
                    dismiss()
                }
            }
        }
    }
}