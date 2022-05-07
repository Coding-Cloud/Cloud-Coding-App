package com.cloudcoding.api

import com.cloudcoding.api.request.LoginRequest
import com.cloudcoding.api.request.SignupRequest
import com.cloudcoding.api.response.MessagesResponse
import com.cloudcoding.api.response.TokenResponse
import com.cloudcoding.models.Conversation
import com.cloudcoding.models.User
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object CloudCodingNetworkManager {
    private val retrofit: CloudCodingAPI = Retrofit.Builder()
        .baseUrl("http://10.0.2.2:3000//")
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .build()
        .create(CloudCodingAPI::class.java)

    suspend fun login(loginRequest: LoginRequest): TokenResponse {
        return retrofit.login(loginRequest).await()
    }

    suspend fun signup(signupRequest: SignupRequest): Void {
        return retrofit.signup(signupRequest).await()
    }

    suspend fun getMe(token: String): User {
        return retrofit.getMe(token).await()
    }

    suspend fun getUserConversations(token: String): List<Conversation> {
        return retrofit.getUserConversations(token).await()
    }

    suspend fun getMessages(token: String, conversationId: String): MessagesResponse {
        return retrofit.getMessages(token, conversationId).await()
    }

}