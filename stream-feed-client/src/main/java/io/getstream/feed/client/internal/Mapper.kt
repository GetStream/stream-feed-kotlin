package io.getstream.feed.client.internal

import arrow.core.Either
import arrow.core.left
import arrow.core.rightIfNotNull
import io.getstream.feed.client.Activity
import io.getstream.feed.client.Actor
import io.getstream.feed.client.EnrichActivity
import io.getstream.feed.client.FeedActivity
import io.getstream.feed.client.FeedID
import io.getstream.feed.client.NetworkError
import io.getstream.feed.client.StreamAPIError
import io.getstream.feed.client.StreamError
import io.getstream.feed.client.internal.api.adapters.FeedMoshiConverterFactory
import io.getstream.feed.client.internal.api.models.ActivitiesResponse
import io.getstream.feed.client.internal.api.models.ActorDto
import io.getstream.feed.client.internal.api.models.CreateActivitiesResponse
import io.getstream.feed.client.internal.api.models.DownstreamActivityDto
import io.getstream.feed.client.internal.api.models.DownstreamActivitySealedDto
import io.getstream.feed.client.internal.api.models.DownstreamEnrichActivityDto
import io.getstream.feed.client.internal.api.models.ErrorResponse
import io.getstream.feed.client.internal.api.models.UpstreamActivityDto
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
internal fun FeedID.toStringFeedID(): String = "$slug:$userID"

internal fun ActivitiesResponse.toDomain(): List<FeedActivity> =
    activities.map { it.toDomain() }

internal fun FeedActivity.toDTO(): UpstreamActivityDto = when (this) {
    is Activity -> toDTO()
    is EnrichActivity -> toDTO()
}

internal fun Activity.toDTO(): UpstreamActivityDto =
    UpstreamActivityDto(
        actor = actor,
        objectProperty = `object`,
        verb = verb,
        target = null,
        time = time.takeUnless(String::isBlank),
        to = to.map(FeedID::toStringFeedID).takeUnless(List<String>::isEmpty),
        foreignId = foreignId,
        extraData = extraData.toMutableMap(),
    )

internal fun EnrichActivity.toDTO(): UpstreamActivityDto =
    UpstreamActivityDto(
        actor = actor.id,
        objectProperty = `object`,
        verb = verb,
        target = null,
        time = time.takeUnless(String::isBlank),
        to = to.map(FeedID::toStringFeedID).takeUnless(List<String>::isEmpty),
        foreignId = foreignId,
        extraData = extraData.toMutableMap(),
    )

internal fun CreateActivitiesResponse.toDomain(): List<FeedActivity> =
    activities.map(DownstreamActivitySealedDto::toDomain)

internal fun <T> Response<T>.obtainEntity(): Either<StreamError, T> = when (isSuccessful) {
    true -> body().rightIfNotNull { toError() }
    false -> toError().left()
}

private fun Response<*>.toError(): StreamError = this.errorBody()
    ?.string()
    ?.let {
        try {
            FeedMoshiConverterFactory.moshi.adapter(ErrorResponse::class.java).fromJson(it)?.toDomain()
        } catch (_: Exception) {
            null
        } ?: NetworkError(it)
    } ?: NetworkError(this.toString())

private fun ErrorResponse.toDomain(): StreamAPIError = StreamAPIError(
    detail = detail,
    statusCode = statusCode,
    errorCode = errorCode,
    error = error,
    moreInfo = moreInfo,
)
