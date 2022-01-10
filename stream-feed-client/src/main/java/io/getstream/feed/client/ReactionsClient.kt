package io.getstream.feed.client

import arrow.core.Either
import arrow.core.computations.either
import io.getstream.feed.client.internal.api.ReactionApi
import io.getstream.feed.client.internal.api.models.FilterReactionsResponse
import io.getstream.feed.client.internal.api.models.ReactionDto
import io.getstream.feed.client.internal.obtainEntity
import io.getstream.feed.client.internal.toDTO
import io.getstream.feed.client.internal.toDomain
import io.getstream.feed.client.internal.toDomin
import io.getstream.feed.client.internal.validate

/**
 * Client that allows you to work with Reaction.
 *
 * @property reactionApi The reactions endpoint.
 * @property userId The user id of the user that works with these collections.
 */
class ReactionsClient internal constructor(
    private val reactionApi: ReactionApi,
    private val userId: String,
) {

    /**
     * Add a new Reaction with the given data.
     *
     * @param reaction The reaction to be added.
     * @return An [Either] with the [Reaction] or an [StreamError].
     */
    suspend fun add(reaction: Reaction): Either<StreamError, Reaction> = either {
        reactionApi.addReaction(
            reaction
                .validate()
                .bind()
                .toDTO(userId)
        )
            .obtainEntity()
            .map(ReactionDto::toDomain)
            .bind()
    }

    /**
     * Filter reactions that match the given params.
     *
     * @param params A function over [FilterReactionsParams] that defines params to be used.
     * @return An [Either] with the [List] of [Reaction] found or an [StreamError].
     */
    suspend fun filter(params: FilterReactionsParams.() -> Unit = {}): Either<StreamError, List<Reaction>> = either {
        val filterReactionsParams = FilterReactionsParams()
            .apply(params)
            .validate()
            .bind()
        reactionApi.filterReactions(
            lookupAttr = filterReactionsParams.lookup.key,
            lookupValue = filterReactionsParams.lookup.id,
            kind = filterReactionsParams.kind,
            limit = filterReactionsParams.limit,
            idGreaterThan = filterReactionsParams.idGreaterThan,
            idSmallerThan = filterReactionsParams.idSmallerThan,
            idGreaterThanOrEqual = filterReactionsParams.idGreaterThanOrEqual,
            idSmallerThanOrEqual = filterReactionsParams.idSmallerThanOrEqual,
            withActivityData = (filterReactionsParams.lookup as? FilterReactionsParams.ActivityLookup)?.enrich
        )
            .obtainEntity()
            .map(FilterReactionsResponse::toDomin)
            .bind()
    }

    /**
     * Delete a reaction by id.
     *
     * @param reactionId The id of the reaction to be deleted.
     * @return An [Either] with the [Unit] if the reaction was deleted or an [StreamError].
     */
    suspend fun delete(reactionId: String): Either<StreamError, Unit> = either {
        reactionApi.deleteReaction(reactionId).obtainEntity().bind()
    }

    /**
     * Delete a reaction.
     *
     * @param reaction The reaction to be deleted.
     * @return An [Either] with the [Unit] if the reaction was deleted or an [StreamError].
     */
    suspend fun delete(reaction: Reaction): Either<StreamError, Unit> = delete(reaction.id)

    /**
     * Update an already existing reaction with the given params.
     *
     * @param params A function over [UpdateReactionParams] that defines params to be used.
     * @return An [Either] with the updated [Reaction] or an [StreamError].
     */
    suspend fun update(params: UpdateReactionParams.() -> Unit = {}): Either<StreamError, Reaction> = either {
        val updateReactionParams = UpdateReactionParams()
            .apply(params)
            .validate()
            .bind()
        reactionApi.updateReaction(
            updateReactionParams.reactionId,
            updateReactionParams.toDTO()
        )
            .obtainEntity()
            .map(ReactionDto::toDomain)
            .bind()
    }
}
