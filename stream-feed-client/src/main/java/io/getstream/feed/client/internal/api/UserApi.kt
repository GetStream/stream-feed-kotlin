package io.getstream.feed.client.internal.api

import io.getstream.feed.client.internal.api.models.UpdateUserRequest
import io.getstream.feed.client.internal.api.models.UserDto
import io.getstream.feed.client.internal.api.models.UserRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * API to be used with Retrofit to provide User Endpoints.
 */
internal interface UserApi {

    @POST("/api/v1.0/user/")
    suspend fun addUser(
        @Query("get_or_create") getOrCreate: Boolean,
        @Body userRequest: UserRequest,
    ): Response<UserDto>

    @PUT("/api/v1.0/user/{id}")
    suspend fun updateUser(
        @Path("id") userId: String,
        @Body updateUserRequest: UpdateUserRequest,
    ): Response<UserDto>

    @GET("/api/v1.0/user/{id}")
    suspend fun getUser(
        @Path("id") userId: String,
        @Query("with_follow_counts") withFollowCounts: Boolean,
    ): Response<UserDto>

    @DELETE("/api/v1.0/user/{id}")
    suspend fun deleteUser(
        @Path("id") userId: String,
    ): Response<Unit>
}
