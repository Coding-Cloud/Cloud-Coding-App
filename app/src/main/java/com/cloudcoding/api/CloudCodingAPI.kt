package com.cloudcoding.api

import com.cloudcoding.api.request.CreateCommentRequest
import com.cloudcoding.api.request.CreateProjectRequest
import com.cloudcoding.api.request.LoginRequest
import com.cloudcoding.api.request.SignupRequest
import com.cloudcoding.api.response.CommentsResponse
import com.cloudcoding.api.response.FollowersResponse
import com.cloudcoding.api.response.TokenResponse
import com.cloudcoding.models.*
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

    @DELETE("/comments/{commentId}")
    fun deleteComment(@Path("commentId") commentId: String): Deferred<Response<Void>>

    @GET("/projects/{projectId}")
    fun getProjectById(@Path("projectId") projectId: String): Deferred<Project>

    @GET("/comments/project/{projectId}")
    fun getProjectComments(@Path("projectId") projectId: String): Deferred<CommentsResponse>

    @POST("/projects")
    fun createProject(@Body createProjectRequest: CreateProjectRequest): Deferred<Project>

    @POST("/comments")
    fun createComment(@Body createCommentRequest: CreateCommentRequest): Deferred<Response<String>>

    @GET("/comments/{id}")
    fun getCommentById(@Path("id") id: String): Deferred<Comment>

    @GET("/groups/{groupId}")
    fun getGroupById(@Path("groupId") groupId: String): Deferred<Group>

    @GET("/friendships/{friendshipId}")
    fun getFriendshipById(@Path("friendshipId") friendshipId: String): Deferred<Friendship>

    @GET("/comments/me")
    fun getCurrentUserComments(
        @Query("search")
        search: String?,
        @Query("limit")
        limit: Number?,
        @Query("offset")
        offset: Number?
    ): Deferred<CommentsResponse>

    @GET("/groups/member")
    fun getJoinedGroups(): Deferred<MutableList<Group>>

    @GET("/groups/owned")
    fun getOwnedGroups(): Deferred<MutableList<Group>>

    @GET("/projects/group/{groupId}")
    fun getGroupProjects(@Path("groupId") groupId: String): Deferred<MutableList<Project>>

    @GET("/group-memberships/group/{groupId}")
    fun getGroupMembers(@Path("groupId") groupId: String): Deferred<MutableList<GroupMembership>>

    @GET("/group-memberships/user/{userId}")
    fun getUserGroups(@Path("userId") userId: String): Deferred<MutableList<GroupMembership>>

    @GET("/followers/{userId}/followings")
    fun getFollowings(
        @Path("userId") userId: String,
        @Query("limit") limit: Number?,
        @Query("offset") offset: Number?
    ): Deferred<FollowersResponse>

    @GET("/followers/{userId}/followers")
    fun getFollowers(
        @Path("userId") userId: String,
        @Query("limit") limit: Number?,
        @Query("offset") offset: Number?
    ): Deferred<FollowersResponse>

    @GET("/followers/{userId}/is-following")
    fun isFollowing(@Path("userId") userId: String): Deferred<Response<Boolean>>
}