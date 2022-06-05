package com.cloudcoding.api.response

import com.cloudcoding.models.Follower

data class FollowersResponse(val comments: MutableList<Follower>, var totalResults: Int)