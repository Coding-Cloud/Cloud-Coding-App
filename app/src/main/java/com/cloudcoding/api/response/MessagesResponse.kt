package com.cloudcoding.api.response

import com.cloudcoding.models.Message

data class MessagesResponse(val messages: MutableList<Message>, var totalResults: Int)