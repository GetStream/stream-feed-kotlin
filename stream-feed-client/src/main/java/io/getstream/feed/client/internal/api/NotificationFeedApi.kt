package io.getstream.feed.client.internal.api

import io.getstream.feed.client.internal.api.models.NotificationsActivitiesGroupResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * API to be used with Retrofit to provide Notification Feed Endpoints.
 */
internal interface NotificationFeedApi : FeedApi {

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
    ): Response<NotificationsActivitiesGroupResponse>

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
    ): Response<NotificationsActivitiesGroupResponse>
}
