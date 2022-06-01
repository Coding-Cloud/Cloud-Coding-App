package com.cloudcoding.api.request

import com.cloudcoding.models.ProjectLanguage
import com.cloudcoding.models.ProjectVisibility

data class CreateProjectRequest(
    val name: String,
    val language: ProjectLanguage,
    val globalVisibility: ProjectVisibility
)