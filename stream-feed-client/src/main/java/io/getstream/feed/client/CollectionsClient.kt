package io.getstream.feed.client

import arrow.core.Either
import arrow.core.computations.either
import io.getstream.feed.client.internal.api.CollectionsApi
import io.getstream.feed.client.internal.api.models.CollectionDto
import io.getstream.feed.client.internal.obtainEntity
import io.getstream.feed.client.internal.toDTO
import io.getstream.feed.client.internal.toDomain

/**
 * Client that allows you to work with collections.
 *
 * @property collectionsApi The collections endpoint.
 * @property userID The user id of the user that works with these collections.
 */
class CollectionsClient internal constructor(
    private val collectionsApi: CollectionsApi,
    private val userID: String,
) {

    /**
     * Add a new Collection with the given data.
     *
     * @param collectionData The collection to be added.
     * @return An [Either] with the [CollectionData] or an [StreamError].
     */
    suspend fun add(collectionData: CollectionData): Either<StreamError, CollectionData> = either {
        collectionsApi.addCollection(
            collectionData.name,
            collectionData.toDTO(userID)
        )
            .obtainEntity()
            .map(CollectionDto::toDomain)
            .bind()
    }

    /**
     * Get a collection by the collectionName and collectionId.
     *
     * @param collectionName The collection name of the expected Collection.
     * @param collectionId The collection id of the expected Collection.
     * @return An [Either] with the [CollectionData] or an [StreamError].
     */
    suspend fun get(collectionName: String, collectionId: String): Either<StreamError, CollectionData> = either {
        collectionsApi.getCollection(collectionName, collectionId)
            .obtainEntity()
            .map(CollectionDto::toDomain)
            .bind()
    }

    /**
     * Update an already existing collection with the new data.
     *
     * @param collectionData The collection data to be updated.
     * @return An [Either] with the updated [CollectionData] or an [StreamError].
     */
    suspend fun update(collectionData: CollectionData): Either<StreamError, CollectionData> = either {
        collectionsApi.updateCollection(
            collectionData.name,
            collectionData.id,
            collectionData.toDTO(userID)
        )
            .obtainEntity()
            .map(CollectionDto::toDomain)
            .bind()
    }

    /**
     * Delete a collection by the collectionName and collectionId.
     *
     * @param collectionName The collection name of the Collection to be deleted.
     * @param collectionId The collection id of the Collection to be deleted.
     * @return An [Either] with the [Unit] if the collection was deleted or an [StreamError].
     */
    suspend fun delete(collectionName: String, collectionId: String): Either<StreamError, Unit> = either {
        collectionsApi.deleteCollection(collectionName, collectionId)
            .obtainEntity()
            .bind()
    }
}
