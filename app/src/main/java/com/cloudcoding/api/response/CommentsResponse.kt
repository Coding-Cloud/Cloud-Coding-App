package com.cloudcoding.api.response

import com.cloudcoding.models.Comment

data class CommentsResponse(val comments: MutableList<Comment>, val totalResults: Int)