package com.cloudcoding.api

import com.cloudcoding.api.request.LoginRequest
import com.cloudcoding.api.request.MessageRequest
import com.cloudcoding.api.request.SignupRequest
import com.cloudcoding.api.response.TokenResponse
import com.cloudcoding.models.Conversation
import com.cloudcoding.models.Message
import com.cloudcoding.models.User
import kotlinx.coroutines.Deferred
import retrofit2.http.*

interface CloudCodingAPI {
    @POST("/auth/signin")
    fun login(@Body login: LoginRequest): Deferred<TokenResponse>

    @POST("/auth/signup")
    fun signup(@Body signupRequest: SignupRequest): Deferred<Void>

    @GET("/auth/me")
    fun getMe(): Deferred<User>

    @POST("/messages/{conversationId}")
    fun addMessage(
        @Path("conversationId") conversationId: String,
        @Body messageRequest: MessageRequest
    ): Deferred<Void>
}