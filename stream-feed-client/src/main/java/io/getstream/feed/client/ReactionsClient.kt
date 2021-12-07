package io.getstream.feed.client

import arrow.core.Either
import arrow.core.computations.either
import io.getstream.feed.client.internal.api.ReactionApi
import io.getstream.feed.client.internal.api.models.ReactionDto
import io.getstream.feed.client.internal.obtainEntity
import io.getstream.feed.client.internal.toDTO
import io.getstream.feed.client.internal.toDomain
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
}
