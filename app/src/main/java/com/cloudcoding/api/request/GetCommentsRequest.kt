package com.cloudcoding.api.request

data class GetCommentsRequest(
    val userId: String?,
    val search: String?,
    val limit: Number?,
    val offset: Number?
)