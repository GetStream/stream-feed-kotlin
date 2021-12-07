package io.getstream.feed.client.internal.api

import io.getstream.feed.client.internal.api.models.ReactionDto
import io.getstream.feed.client.internal.api.models.ReactionRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

internal interface ReactionApi {

    @POST("/api/v1.0/reaction/")
    suspend fun addReaction(
        @Body reactionRequest: ReactionRequest,
    ): Response<ReactionDto>
}
