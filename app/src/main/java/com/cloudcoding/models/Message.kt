package com.cloudcoding.models

import java.util.*

data class Message(
    val id: String,
    val userId: String,
    var assetId: String?,
    val conversationId: String,
    var content: String,
    val createdAt: Date,
)
