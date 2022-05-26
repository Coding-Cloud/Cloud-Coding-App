package com.cloudcoding.models

import java.util.*

data class Message(
    val id: String,
    val userId: String,
    val assetId: String,
    val conversationId: String,
    val content: String,
    val createdAt: Date,
)
