package com.cloudcoding.api

import android.content.Context
import com.cloudcoding.MainActivity
import com.cloudcoding.api.request.LoginRequest
import com.cloudcoding.api.request.MessageRequest
import com.cloudcoding.api.request.SignupRequest
import com.cloudcoding.api.response.TokenResponse
import com.cloudcoding.models.User
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object CloudCodingNetworkManager {
    private val retrofit: CloudCodingAPI = retrofit()

    private fun retrofit(): CloudCodingAPI {
        val preference = MainActivity.getContext().getSharedPreferences(
            "token",
            Context.MODE_PRIVATE
        )!!
        val token = preference.getString("token", "")!!
        val httpClient = OkHttpClient.Builder().addInterceptor { chain ->
            val requestBuilder: Request.Builder = chain.request().newBuilder()
            requestBuilder.header("authorization", token)
            chain.proceed(requestBuilder.build())
        }.build()
        return Retrofit.Builder()
            .baseUrl("http://10.0.2.2:3000/")
            .client(httpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .build()
            .create(CloudCodingAPI::class.java)
    }

    suspend fun login(loginRequest: LoginRequest): TokenResponse {
        return retrofit.login(loginRequest).await()
    }

    suspend fun signup(signupRequest: SignupRequest): Void {
        return retrofit.signup(signupRequest).await()
    }

    suspend fun getMe(): User {
        return retrofit.getMe().await()
    }

    suspend fun addMessage(
        conversationId: String,
        messageRequest: MessageRequest
    ): Void {
        return retrofit.addMessage(conversationId, messageRequest).await()
    }

}