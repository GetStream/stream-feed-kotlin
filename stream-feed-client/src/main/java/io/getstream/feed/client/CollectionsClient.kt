package io.getstream.feed.client

import arrow.core.Either
import arrow.core.computations.either
import io.getstream.feed.client.internal.api.CollectionsApi
import io.getstream.feed.client.internal.api.models.CollectionDto
import io.getstream.feed.client.internal.obtainEntity
import io.getstream.feed.client.internal.toDTO
import io.getstream.feed.client.internal.toDomain

class CollectionsClient internal constructor(
    private val collectionsApi: CollectionsApi,
    private val userID: String,
) {

    suspend fun add(collectionData: CollectionData): Either<StreamError, CollectionData> = either {
        collectionsApi.addCollection(
            collectionData.name,
            collectionData.toDTO(userID)
        )
            .obtainEntity()
            .map(CollectionDto::toDomain)
            .bind()
    }
}
