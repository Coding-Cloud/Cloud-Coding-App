package com.cloudcoding.api.request

import java.util.*

data class SignupRequest(
    val username: String,
    val firstname: String,
    val lastname: String,
    val birthdate: Date,
    val password: String,
    val email: String,
)
