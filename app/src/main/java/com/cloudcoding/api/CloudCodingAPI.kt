package com.cloudcoding.api

import com.cloudcoding.api.request.LoginRequest
import com.cloudcoding.api.request.MessageRequest
import com.cloudcoding.api.request.SignupRequest
import com.cloudcoding.api.response.TokenResponse
import com.cloudcoding.models.Conversation
import com.cloudcoding.models.Message
import com.cloudcoding.models.Project
import com.cloudcoding.models.User
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.*

interface CloudCodingAPI {
    @POST("/auth/signin")
    fun login(@Body login: LoginRequest): Deferred<TokenResponse>

    @POST("/auth/signup")
    fun signup(@Body signupRequest: SignupRequest): Deferred<Void>

    @GET("/auth/me")
    fun getMe(): Deferred<User>

    @GET("/projects/owned")
    fun getOwnedProjects(): Deferred<MutableList<Project>>

    @DELETE("/projects/{projectId}")
    fun deleteProject(@Path("projectId") projectId: String): Deferred<Response<Void>>

    @GET("/users/{userId}")
    fun getUserById(@Path("userId") userId: String): Deferred<User>
}