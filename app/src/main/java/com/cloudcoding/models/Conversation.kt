package com.cloudcoding.models

import java.util.*

data class Conversation(
    val id: String,
    val friendshipId: String?,
    val groupId: String?,
    val createdAt: Date
)
