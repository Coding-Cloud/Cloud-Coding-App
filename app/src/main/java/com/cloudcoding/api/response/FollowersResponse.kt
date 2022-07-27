package com.cloudcoding.api.response

import com.cloudcoding.models.Follower

data class FollowersResponse(val followers: MutableList<Follower>, var totalResults: Int)