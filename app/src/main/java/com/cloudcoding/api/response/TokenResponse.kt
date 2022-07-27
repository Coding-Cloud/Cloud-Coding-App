package com.cloudcoding.api.response

import com.google.gson.annotations.SerializedName

data class TokenResponse(@SerializedName("accessToken") val accessToken: String)
