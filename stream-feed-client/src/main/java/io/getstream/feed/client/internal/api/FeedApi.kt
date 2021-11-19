package io.getstream.feed.client.internal.api

import io.getstream.feed.client.internal.api.models.ActivitiesRequest
import io.getstream.feed.client.internal.api.models.ActivitiesResponse
import io.getstream.feed.client.internal.api.models.CreateActivitiesResponse
import io.getstream.feed.client.internal.api.models.FollowRelationResponse
import io.getstream.feed.client.internal.api.models.FollowRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

internal interface FeedApi {

    @GET("/api/v1.0/enrich/feed/{slug}/{id}")
    suspend fun enrichActivities(
        @Path("slug") slug: String,
        @Path("id") id: String,
        @Query("limit") limit: Int,
        @Query("offset") offset: Int?,
        @Query("id_gt") idGreaterThan: String?,
        @Query("id_lt") idSmallerThan: String?,
        @Query("id_gte") idGreaterThanOrEqual: String?,
        @Query("id_lte") idSmallerThanOrEqual: String?,
        @Query("withRecentReactions") withRecentReactions: Boolean,
        @Query("withOwnReactions") withOwnReactions: Boolean,
        @Query("withReactionCounts") withReactionCounts: Boolean,
        @Query("recentReactionsLimit") recentReactionsLimit: Int?,
    ): Response<ActivitiesResponse>

    @GET("/api/v1.0/feed/{slug}/{id}")
    suspend fun activities(
        @Path("slug") slug: String,
        @Path("id") id: String,
        @Query("limit") limit: Int,
        @Query("offset") offset: Int?,
        @Query("id_gt") idGreaterThan: String?,
        @Query("id_lt") idSmallerThan: String?,
        @Query("id_gte") idGreaterThanOrEqual: String?,
        @Query("id_lte") idSmallerThanOrEqual: String?,
        @Query("withRecentReactions") withRecentReactions: Boolean,
        @Query("withOwnReactions") withOwnReactions: Boolean,
        @Query("withReactionCounts") withReactionCounts: Boolean,
        @Query("recentReactionsLimit") recentReactionsLimit: Int?,
    ): Response<ActivitiesResponse>

    @POST("/api/v1.0/feed/{slug}/{id}")
    suspend fun sendActivities(
        @Path("slug") slug: String,
        @Path("id") id: String,
        @Body activities: ActivitiesRequest,
    ): Response<CreateActivitiesResponse>

    @POST("/api/v1.0/feed/{slug}/{id}/follows")
    suspend fun follow(
        @Path("slug") slug: String,
        @Path("id") id: String,
        @Body followRequest: FollowRequest,
    ): Response<Unit>

    @DELETE("/api/v1.0/feed/{slug}/{id}/following/{targetFeedID}")
    suspend fun unfollow(
        @Path("slug") slug: String,
        @Path("id") id: String,
        @Path("targetFeedID") targetFeedId: String,
        @Query("keep_history") keepHistory: Boolean?,
    ): Response<Unit>

    @GET("/api/v1.0/feed/{slug}/{id}/following")
    suspend fun followed(
        @Path("slug") slug: String,
        @Path("id") id: String,
        @Query("limit") limit: Int?,
        @Query("offset") offset: Int?,
    ): Response<FollowRelationResponse>

    @GET("/api/v1.0/feed/{slug}/{id}/followers")
    suspend fun followers(
        @Path("slug") slug: String,
        @Path("id") id: String,
        @Query("limit") limit: Int?,
        @Query("offset") offset: Int?,
    ): Response<FollowRelationResponse>

    @DELETE("/api/v1.0/feed/{slug}/{id}/{activityId}")
    suspend fun removeActivityById(
        @Path("slug") slug: String,
        @Path("id") id: String,
        @Path("activityId") activityId: String,
    ): Response<Unit>

    @DELETE("/api/v1.0/feed/{slug}/{id}/{foreignId}?foreign_id=1")
    suspend fun removeActivityByForeignId(
        @Path("slug") slug: String,
        @Path("id") id: String,
        @Path("foreignId") foreignId: String,
    ): Response<Unit>
}
