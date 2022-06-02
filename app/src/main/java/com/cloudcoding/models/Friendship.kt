package com.cloudcoding.models

import java.util.*

data class Friendship(
    val id: String,
    val user1Id: String,
    val user2Id: String,
    val createdAt: Date,
)
