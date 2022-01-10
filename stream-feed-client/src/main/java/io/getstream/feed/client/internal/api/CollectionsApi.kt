package io.getstream.feed.client.internal.api

import io.getstream.feed.client.internal.api.models.CollectionDto
import io.getstream.feed.client.internal.api.models.CollectionRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

/**
 * API to be used with Retrofit to provide Collections Endpoints.
 */
internal interface CollectionsApi {

    @POST("/api/v1.0/collections/{collectionName}")
    suspend fun addCollection(
        @Path("collectionName") collectionName: String,
        @Body collectionRequest: CollectionRequest,
    ): Response<CollectionDto>

    @GET("/api/v1.0/collections/{collectionName}/{collectionId}")
    suspend fun getCollection(
        @Path("collectionName") collectionName: String,
        @Path("collectionId") collectionId: String,
    ): Response<CollectionDto>

    @DELETE("/api/v1.0/collections/{collectionName}/{collectionId}")
    suspend fun deleteCollection(
        @Path("collectionName") collectionName: String,
        @Path("collectionId") collectionId: String,
    ): Response<Unit>

    @PUT("/api/v1.0/collections/{collectionName}/{collectionId}")
    suspend fun updateCollection(
        @Path("collectionName") collectionName: String,
        @Path("collectionId") collectionId: String,
        @Body collectionRequest: CollectionRequest,
    ): Response<CollectionDto>
}
