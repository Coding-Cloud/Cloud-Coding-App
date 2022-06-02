package com.cloudcoding.api

import android.content.Context
import com.cloudcoding.MainActivity
import com.cloudcoding.R
import com.cloudcoding.api.request.CreateCommentRequest
import com.cloudcoding.api.request.CreateProjectRequest
import com.cloudcoding.api.request.LoginRequest
import com.cloudcoding.api.request.SignupRequest
import com.cloudcoding.api.response.CommentsResponse
import com.cloudcoding.api.response.TokenResponse
import com.cloudcoding.models.*
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory


object CloudCodingNetworkManager {
    private val retrofit: CloudCodingAPI = retrofit()

    private fun retrofit(): CloudCodingAPI {
        val preference = MainActivity.getContext().getSharedPreferences(
            MainActivity.getContext().getString(R.string.token),
            Context.MODE_PRIVATE
        )!!
        val sharedPrefMe = MainActivity.getContext().getSharedPreferences(
            MainActivity.getContext().getString(R.string.me),
            Context.MODE_PRIVATE
        )!!
        val httpClient = OkHttpClient.Builder().addInterceptor { chain ->
            val token = preference.getString("token", "")!!
            val requestBuilder: Request.Builder = chain.request().newBuilder()
            requestBuilder.header("authorization", token)
            val response = chain.proceed(requestBuilder.build())
            if (response.code() == 403)
            {
                with(sharedPrefMe.edit()) {
                    clear()
                    commit()
                }
                with(preference.edit()) {
                    clear()
                    commit()
                }
            }
            response
        }.build()
        return Retrofit.Builder()
            .baseUrl("http://10.0.2.2:3000/")
            .client(httpClient)
            .addConverterFactory(ScalarsConverterFactory.create())
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

    suspend fun getOwnedProjects(): MutableList<Project> {
        return retrofit.getOwnedProjects().await()
    }

    suspend fun deleteProject(projectId: String): Response<Void> {
        return retrofit.deleteProject(projectId).await()
    }

    suspend fun getUserById(userId: String): User {
        return retrofit.getUserById(userId).await()
    }

    suspend fun deleteComment(commentId: String): Response<Void> {
        return retrofit.deleteComment(commentId).await()
    }

    suspend fun getProjectById(projectId: String): Project {
        return retrofit.getProjectById(projectId).await()
    }

    suspend fun getProjectComments(projectId: String): CommentsResponse {
        return retrofit.getProjectComments(projectId).await()
    }

    suspend fun createProject(createProjectRequest: CreateProjectRequest): Project {
        return retrofit.createProject(createProjectRequest).await()
    }
    suspend fun createComment(createCommentRequest: CreateCommentRequest): Response<String> {
        return retrofit.createComment(createCommentRequest).await()
    }
    suspend fun getCommentById(id: String): Comment {
        return retrofit.getCommentById(id).await()
    }

    suspend fun getGroupById(groupId: String): Group {
        return retrofit.getGroupById(groupId).await()
    }

    suspend fun getFriendshipById(friendshipId: String): Friendship {
        return retrofit.getFriendshipById(friendshipId).await()
    }
}