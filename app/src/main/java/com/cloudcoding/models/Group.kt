package com.cloudcoding.models

import java.util.*

data class Group(
    val id: String,
    val name: String,
    val ownerId: String,
    val isHidden: Boolean,
    val createdAt: Date,
)
