package com.cloudcoding.api.request

data class MessageRequest(val conversationId: String, val content: String, val assetId: String?)
