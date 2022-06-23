package com.cloudcoding.api.request

data class GetMessagesRequest(val conversationId: String, val limit: Number?, val offset: Number?)
