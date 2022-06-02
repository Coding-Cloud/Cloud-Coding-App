package com.cloudcoding.api.request

data class CreateCommentRequest(
    val content: String,
    val projectId: String
)