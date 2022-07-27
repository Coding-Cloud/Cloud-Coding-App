package com.cloudcoding.api.response

import com.cloudcoding.models.User

data class UsersResponse(val users: MutableList<User>, var totalResults: Int)