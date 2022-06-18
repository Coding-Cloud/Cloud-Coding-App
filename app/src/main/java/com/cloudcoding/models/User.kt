package com.cloudcoding.models

import java.util.*

data class User(
    val id: String,
    val username: String,
    val firstname: String,
    val lastname: String,
    val birthdate: Date,
    val password: String,
    val email: String,
)
