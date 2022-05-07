package com.cloudcoding.api

import com.cloudcoding.api.request.LoginRequest
import com.cloudcoding.api.request.SignupRequest
import com.cloudcoding.api.response.MessagesResponse
import com.cloudcoding.api.response.TokenResponse
import com.cloudcoding.models.Conversation
import kotlinx.coroutines.Deferred
import retrofit2.http.*

interface CloudCodingAPI {
    @POST("/auth/signin")
    fun login(@Body login: LoginRequest): Deferred<TokenResponse>

    @POST("/auth/signup")
    fun signup(signupRequest: SignupRequest): Deferred<Void>

    @GET("/conversations")
    fun getUserConversations(
        @Header("authorization") token: String): Deferred<List<Conversation>>

    @GET("/messages/{conversationId}")
    fun getMessages(
        @Header("authorization") token: String, @Path("conversationId") conversationId: String): Deferred<MessagesResponse>
}