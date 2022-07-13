package com.cloudcoding.api.response

import com.cloudcoding.models.Project

data class ProjectsResponse(val projects: MutableList<Project>, var totalResults: Int)