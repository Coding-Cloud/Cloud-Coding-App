package com.cloudcoding.api

import android.content.Context
import android.util.Log
import com.cloudcoding.MainActivity
import com.cloudcoding.R
import com.cloudcoding.api.request.*
import com.cloudcoding.api.response.CommentsResponse
import com.cloudcoding.api.response.FollowersResponse
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
            Log.i("Request", chain.request().toString())
            val response = chain.proceed(requestBuilder.build())
            if (response.code() == 403) {
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

    suspend fun createProject(createProjectRequest: CreateProjectRequest): Response<String> {
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

    suspend fun getCurrentUserComments(getCommentsRequest: GetCommentsRequest): CommentsResponse {
        return retrofit.getCurrentUserComments(
            getCommentsRequest.search,
            getCommentsRequest.limit,
            getCommentsRequest.offset
        ).await()
    }

    suspend fun getUserComments(getCommentsRequest: GetCommentsRequest): CommentsResponse {
        return retrofit.getUserComments(
            getCommentsRequest.userId!!,
            getCommentsRequest.search,
            getCommentsRequest.limit,
            getCommentsRequest.offset
        ).await()
    }

    suspend fun getJoinedGroups(): MutableList<Group> {
        return retrofit.getJoinedGroups().await()
    }

    suspend fun getOwnedGroups(): MutableList<Group> {
        return retrofit.getOwnedGroups().await()
    }

    suspend fun getGroupProjects(groupId: String): MutableList<Project> {
        return retrofit.getGroupProjects(groupId).await()
    }

    suspend fun getGroupMembers(groupId: String): MutableList<GroupMembership> {
        return retrofit.getGroupMembers(groupId).await()
    }

    suspend fun getUserGroups(userId: String): MutableList<GroupMembership> {
        return retrofit.getUserGroups(userId).await()
    }


    suspend fun getUserProjects(userId: String): MutableList<Project> {
        return retrofit.getUserProjects(userId).await()
    }

    suspend fun getFollowings(getFollowersRequest: GetFollowersRequest): FollowersResponse {
        return retrofit.getFollowings(
            getFollowersRequest.userId,
            getFollowersRequest.limit,
            getFollowersRequest.offset
        ).await()
    }

    suspend fun getFollowers(getFollowersRequest: GetFollowersRequest): FollowersResponse {
        return retrofit.getFollowers(
            getFollowersRequest.userId,
            getFollowersRequest.limit,
            getFollowersRequest.offset
        ).await()
    }

    suspend fun isFollowing(userId: String): Boolean {
        return retrofit.isFollowing(userId).await().body()!!
    }

    suspend fun follow(userId: String): Response<Void> {
        return retrofit.follow(userId).await()
    }

    suspend fun unfollow(userId: String): Response<Void> {
        return retrofit.unfollow(userId).await()
    }

    suspend fun getUserFriends(): MutableList<Friendship> {
        return retrofit.getUserFriends().await()
    }

    suspend fun removeFriend(friendshipId: String): Response<Void> {
        return retrofit.removeFriend(friendshipId).await()
    }

    suspend fun getSentFriendRequests(): MutableList<FriendRequest> {
        return retrofit.getSentFriendRequests().await()
    }

    suspend fun getReceivedFriendRequests(): MutableList<FriendRequest> {
        return retrofit.getReceivedFriendRequests().await()
    }

    suspend fun sendFriendRequest(userId: String): Response<Void> {
        return retrofit.sendFriendRequest(userId).await()
    }

    suspend fun cancelFriendRequest(userId: String): Response<Void> {
        return retrofit.cancelFriendRequest(userId).await()
    }

    suspend fun acceptFriendRequest(userId: String): Response<String> {
        return retrofit.acceptFriendRequest(userId).await()
    }

    suspend fun rejectFriendRequest(userId: String): Response<Void> {
        return retrofit.rejectFriendRequest(userId).await()
    }
}