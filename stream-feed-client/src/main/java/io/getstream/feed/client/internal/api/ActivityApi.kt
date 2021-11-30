package io.getstream.feed.client.internal.api

import io.getstream.feed.client.internal.api.models.ActivitiesResponse
import io.getstream.feed.client.internal.api.models.CommaSeparatedQueryParams
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

internal interface ActivityApi {

    @GET("/api/v1.0/enrich/activities")
    suspend fun enrichActivities(
        @Query("ids") activitiesIds: CommaSeparatedQueryParams<String>?,
        @Query("foreign_ids") foreignIds: CommaSeparatedQueryParams<String>?,
        @Query("timestamps") timestamps: CommaSeparatedQueryParams<String>?,
        @Query("withOwnReactions") withOwnReactions: Boolean,
        @Query("withReactionCounts") withReactionCounts: Boolean,
        @Query("withRecentReactions") withRecentReactions: Boolean,
        @Query("recentReactionsLimit") recentReactionsLimit: Int?,
    ): Response<ActivitiesResponse>

    @GET("/api/v1.0/activities")
    suspend fun activities(
        @Query("ids") activitiesIds: CommaSeparatedQueryParams<String>?,
        @Query("foreign_ids") foreignIds: CommaSeparatedQueryParams<String>?,
        @Query("timestamps") timestamps: CommaSeparatedQueryParams<String>?,
        @Query("withOwnReactions") withOwnReactions: Boolean,
        @Query("withReactionCounts") withReactionCounts: Boolean,
        @Query("withRecentReactions") withRecentReactions: Boolean,
        @Query("recentReactionsLimit") recentReactionsLimit: Int?,
    ): Response<ActivitiesResponse>
}
