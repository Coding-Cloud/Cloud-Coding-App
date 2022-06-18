package com.cloudcoding.models

import java.util.*

data class FriendRequest(
    val requesterUserId: String,
    val requestedUserId: String,
    val createdAt: Date,
)