package com.cloudcoding.models

import java.util.*

data class Comment(
    val id: String,
    val ownerId: String,
    val projectId: String,
    val content: String,
    val createdAt: Date,
)
