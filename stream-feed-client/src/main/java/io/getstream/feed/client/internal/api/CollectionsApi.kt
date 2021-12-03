package io.getstream.feed.client.internal.api

import io.getstream.feed.client.internal.api.models.CollectionDto
import io.getstream.feed.client.internal.api.models.CollectionRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path

internal interface CollectionsApi {

    @POST("/api/v1.0/collections/{collectionName}")
    suspend fun addCollection(
        @Path("collectionName") collectionName: String,
        @Body collectionRequest: CollectionRequest,
    ): Response<CollectionDto>
}
