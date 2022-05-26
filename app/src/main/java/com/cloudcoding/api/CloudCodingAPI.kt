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
    fun getMe(@Header("authorization") token: String): Deferred<User>

    @GET("/conversations")
    fun getUserConversations(
        @Header("authorization") token: String
    ): Deferred<List<Conversation>>

    @GET("/messages/{conversationId}")
    fun getMessages(
        @Header("authorization") token: String, @Path("conversationId") conversationId: String
    ): Deferred<MutableList<Message>>

    @POST("/messages/{conversationId}")
    fun addMessage(
        @Header("authorization") token: String,
        @Path("conversationId") conversationId: String,
        @Body messageRequest: MessageRequest
    ): Deferred<Void>
}