package io.getstream.feed.client.internal.api

import io.getstream.feed.client.internal.api.models.FilterReactionsResponse
import io.getstream.feed.client.internal.api.models.ReactionDto
import io.getstream.feed.client.internal.api.models.ReactionRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

internal interface ReactionApi {

    @POST("/api/v1.0/reaction/")
    suspend fun addReaction(
        @Body reactionRequest: ReactionRequest,
    ): Response<ReactionDto>

    @GET("/api/v1.0/reaction/{lookupAttr}/{lookupValue}/{kind}")
    suspend fun filterReactions(
        @Path("lookupAttr") lookupAttr: String,
        @Path("lookupValue") lookupValue: String,
        @Path("kind") kind: String?,
        @Query("limit") limit: Int,
        @Query("id_gt") idGreaterThan: String?,
        @Query("id_lt") idSmallerThan: String?,
        @Query("id_gte") idGreaterThanOrEqual: String?,
        @Query("id_lte") idSmallerThanOrEqual: String?,
        @Query("with_activity_data") withActivityData: Boolean?,
    ): Response<FilterReactionsResponse>

    @DELETE("/api/v1.0/reaction/{reactionId}")
    suspend fun deleteReaction(
        @Path("reactionId") reactionId: String,
    ): Response<Unit>
}
