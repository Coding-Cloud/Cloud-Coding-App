package com.cloudcoding.api.request

data class GetFollowersRequest(
    val userId: String,
    val limit: Number?,
    val offset: Number?
)