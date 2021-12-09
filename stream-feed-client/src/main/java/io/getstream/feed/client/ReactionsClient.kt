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

class ReactionsClient internal constructor(
    private val reactionApi: ReactionApi,
    private val userId: String,
) {

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

    suspend fun delete(reactionId: String): Either<StreamError, Unit> = either {
        reactionApi.deleteReaction(reactionId).obtainEntity().bind()
    }

    suspend fun delete(reaction: Reaction): Either<StreamError, Unit> = delete(reaction.id)
}
