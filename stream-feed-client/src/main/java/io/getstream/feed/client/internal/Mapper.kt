package io.getstream.feed.client.internal

import arrow.core.Either
import arrow.core.left
import arrow.core.rightIfNotNull
import io.getstream.feed.client.Activity
import io.getstream.feed.client.Actor
import io.getstream.feed.client.AggregatedActivitiesGroup
import io.getstream.feed.client.EnrichActivity
import io.getstream.feed.client.FeedActivity
import io.getstream.feed.client.FeedID
import io.getstream.feed.client.FollowRelation
import io.getstream.feed.client.NetworkError
import io.getstream.feed.client.NotificationActivitiesGroup
import io.getstream.feed.client.Object
import io.getstream.feed.client.StreamAPIError
import io.getstream.feed.client.StreamError
import io.getstream.feed.client.Target
import io.getstream.feed.client.internal.api.adapters.FeedMoshiConverterFactory
import io.getstream.feed.client.internal.api.models.ActivitiesResponse
import io.getstream.feed.client.internal.api.models.AggregatedActivitiesGroupDto
import io.getstream.feed.client.internal.api.models.AggregatedActivitiesGroupResponse
import io.getstream.feed.client.internal.api.models.CreateActivitiesResponse
import io.getstream.feed.client.internal.api.models.DataDto
import io.getstream.feed.client.internal.api.models.DownstreamActivityDto
import io.getstream.feed.client.internal.api.models.ErrorResponse
import io.getstream.feed.client.internal.api.models.FollowRelationDto
import io.getstream.feed.client.internal.api.models.FollowRelationResponse
import io.getstream.feed.client.internal.api.models.NotificationActivitiesGroupDto
import io.getstream.feed.client.internal.api.models.NotificationsActivitiesGroupResponse
import io.getstream.feed.client.internal.api.models.UpstreamActivityDto
import retrofit2.Response

private fun DataDto.toActorDomain(): Actor = Actor(id = id, data = data)
private fun DataDto.toObjectDomain(): Object = Object(id = id, data = data)
private fun DataDto.toTargetDomain(): Target = Target(id = id, data = data)

internal fun DownstreamActivityDto.toDomain(enrich: Boolean): FeedActivity = when (enrich) {
    true -> EnrichActivity(
        id = id,
        actor = actor.toActorDomain(),
        `object` = objectProperty.toObjectDomain(),
        verb = verb,
        target = target?.toTargetDomain(),
        to = to?.map { it.toFeedID() } ?: listOf(),
        time = time ?: "",
        foreignId = foreignId,
        extraData = extraData
    )
    false -> Activity(
        id = id,
        actor = actor.id,
        `object` = objectProperty.id,
        verb = verb,
        target = target?.id,
        to = to?.map { it.toFeedID() } ?: listOf(),
        time = time ?: "",
        foreignId = foreignId,
        extraData = extraData
    )
}

private fun String.toFeedID(): FeedID = split(":").let { FeedID(it[0], it[1]) }
internal fun FeedID.toStringFeedID(): String = "$slug:$userID"

internal fun ActivitiesResponse.toDomain(enrich: Boolean): List<FeedActivity> =
    activities.map { it.toDomain(enrich) }

internal fun FeedActivity.toDTO(): UpstreamActivityDto = when (this) {
    is Activity -> toDTO()
    is EnrichActivity -> toDTO()
}

internal fun Activity.toDTO(): UpstreamActivityDto =
    UpstreamActivityDto(
        actor = actor,
        objectProperty = `object`,
        verb = verb,
        target = target,
        time = time.takeUnless(String::isBlank),
        to = to.map(FeedID::toStringFeedID).takeUnless(List<String>::isEmpty),
        foreignId = foreignId,
        extraData = extraData.toMutableMap(),
    )

internal fun EnrichActivity.toDTO(): UpstreamActivityDto =
    UpstreamActivityDto(
        actor = actor.id,
        objectProperty = `object`.id,
        verb = verb,
        target = target?.id,
        time = time.takeUnless(String::isBlank),
        to = to.map(FeedID::toStringFeedID).takeUnless(List<String>::isEmpty),
        foreignId = foreignId,
        extraData = extraData.toMutableMap(),
    )

internal fun CreateActivitiesResponse.toDomain(enrich: Boolean): List<FeedActivity> =
    activities.map { it.toDomain(enrich) }

internal fun FollowRelationResponse.toDomain(): List<FollowRelation> =
    followRelations.map(FollowRelationDto::toDomain)

internal fun FollowRelationDto.toDomain(): FollowRelation =
    FollowRelation(
        sourceFeedID = sourceFeedID.toFeedID(),
        targetFeedID = targetFeedID.toFeedID(),
    )

internal fun AggregatedActivitiesGroupResponse.toDomain(enrich: Boolean): List<AggregatedActivitiesGroup> =
    activitiesGroups.map { it.toDomain(enrich) }

internal fun AggregatedActivitiesGroupDto.toDomain(enrich: Boolean): AggregatedActivitiesGroup =
    AggregatedActivitiesGroup(
        id = id,
        activities = activities.map { it.toDomain(enrich) },
        verb = verb,
        group = group,
        actorCount = actorCount,
    )

internal fun NotificationsActivitiesGroupResponse.toDomain(enrich: Boolean): List<NotificationActivitiesGroup> =
    activitiesGroups.map { it.toDomain(enrich) }

internal fun NotificationActivitiesGroupDto.toDomain(enrich: Boolean): NotificationActivitiesGroup =
    NotificationActivitiesGroup(
        id = id,
        activities = activities.map { it.toDomain(enrich) },
        verb = verb,
        group = group,
        actorCount = actorCount,
        isRead = isRead,
        isSeen = isSeen,
    )

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
