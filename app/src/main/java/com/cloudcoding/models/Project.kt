package com.cloudcoding.models

import java.util.*

data class Project(
    val id: String,
    val name: String,
    val uniqueName: String,
    val lastVersion: Number,
    val language: ProjectLanguage,
    val status: ProjectStatus,
    val globalVisibility: ProjectVisibility,
    val creatorId: String,
    val groupId: String,
    val createdAt: Date,
)

enum class ProjectLanguage {
    ANGULAR, REACT, QUARKUS, NESTJS, FLASK
}

enum class ProjectStatus {
    INITIALISING, INACTIVE, RUNNING
}

enum class ProjectVisibility {
    PRIVATE, PUBLIC, GUEST
}
