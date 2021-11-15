package io.getstream.feed.client.internal

import arrow.core.Either
import arrow.core.left
import arrow.core.rightIfNotNull
import io.getstream.feed.client.Activity
import io.getstream.feed.client.Actor
import io.getstream.feed.client.EnrichActivity
import io.getstream.feed.client.FeedActivity
import io.getstream.feed.client.FeedID
import io.getstream.feed.client.internal.api.models.ActivitiesResponse
import io.getstream.feed.client.internal.api.models.ActorDto
import io.getstream.feed.client.internal.api.models.DownstreamActivityDto
import io.getstream.feed.client.internal.api.models.DownstreamActivitySealedDto
import io.getstream.feed.client.internal.api.models.DownstreamEnrichActivityDto
import retrofit2.Response

internal fun ActorDto.toDomain(): Actor =
    Actor(
        id = id,
        handle = data.handle,
        name = data.name,
        profileImage = data.profileImage,
        extraData = extraData
    )

internal fun DownstreamActivitySealedDto.toDomain(): FeedActivity = when (this) {
    is DownstreamActivityDto -> toDomain()
    is DownstreamEnrichActivityDto -> toDomain()
}

internal fun DownstreamActivityDto.toDomain(): Activity =
    Activity(
        id = id,
        actor = actor,
        `object` = objectProperty,
        verb = verb,
        to = to?.map { it.toFeedID() } ?: listOf(),
        time = time ?: "",
        foreignId = foreignId,
        extraData = extraData
    )

internal fun DownstreamEnrichActivityDto.toDomain(): EnrichActivity =
    EnrichActivity(
        id = id,
        actor = actor.toDomain(),
        `object` = objectProperty,
        verb = verb,
        to = to?.map { it.toFeedID() } ?: listOf(),
        time = time ?: "",
        foreignId = foreignId,
        extraData = extraData
    )

private fun String.toFeedID(): FeedID = split(":").let { FeedID(it[0], it[1]) }

internal fun ActivitiesResponse.toDomain(): List<FeedActivity> =
    activities.map { it.toDomain() }

internal fun <T> Response<T>.obtainEntity(): Either<Unit, T> = when (isSuccessful) {
    true -> body().rightIfNotNull { }
    false -> Unit.left()
}
