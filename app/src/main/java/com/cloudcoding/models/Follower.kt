package com.cloudcoding.models

import java.util.*

data class Follower(
    val followerId: String,
    val followedId: String,
    val createdAt: Date,
)