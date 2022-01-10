package io.getstream.feed.client.internal

import arrow.core.Either
import arrow.core.left
import arrow.core.rightIfNotNull
import io.getstream.feed.client.Activity
import io.getstream.feed.client.Actor
import io.getstream.feed.client.AggregatedActivitiesGroup
import io.getstream.feed.client.CollectionData
import io.getstream.feed.client.EnrichActivity
import io.getstream.feed.client.FeedActivity
import io.getstream.feed.client.FeedID
import io.getstream.feed.client.FollowRelation
import io.getstream.feed.client.NetworkError
import io.getstream.feed.client.NotificationActivitiesGroup
import io.getstream.feed.client.Object
import io.getstream.feed.client.Reaction
import io.getstream.feed.client.StreamAPIError
import io.getstream.feed.client.StreamError
import io.getstream.feed.client.Target
import io.getstream.feed.client.UpdateActivityByForeignIdParams
import io.getstream.feed.client.UpdateActivityByIdParams
import io.getstream.feed.client.UpdateActivityParams
import io.getstream.feed.client.UpdateReactionParams
import io.getstream.feed.client.User
import io.getstream.feed.client.internal.api.adapters.FeedMoshiConverterFactory
import io.getstream.feed.client.internal.api.models.ActivitiesResponse
import io.getstream.feed.client.internal.api.models.AggregatedActivitiesGroupDto
import io.getstream.feed.client.internal.api.models.AggregatedActivitiesGroupResponse
import io.getstream.feed.client.internal.api.models.CollectionDto
import io.getstream.feed.client.internal.api.models.CollectionRequest
import io.getstream.feed.client.internal.api.models.CreateActivitiesResponse
import io.getstream.feed.client.internal.api.models.DataDto
import io.getstream.feed.client.internal.api.models.DownstreamActivityDto
import io.getstream.feed.client.internal.api.models.ErrorResponse
import io.getstream.feed.client.internal.api.models.FilterReactionsResponse
import io.getstream.feed.client.internal.api.models.FollowRelationDto
import io.getstream.feed.client.internal.api.models.FollowRelationResponse
import io.getstream.feed.client.internal.api.models.NotificationActivitiesGroupDto
import io.getstream.feed.client.internal.api.models.NotificationsActivitiesGroupResponse
import io.getstream.feed.client.internal.api.models.ReactionDto
import io.getstream.feed.client.internal.api.models.ReactionRequest
import io.getstream.feed.client.internal.api.models.UpdateActivitiesRequest
import io.getstream.feed.client.internal.api.models.UpdateActivitiesResponse
import io.getstream.feed.client.internal.api.models.UpdateActivityByForeignIdRequest
import io.getstream.feed.client.internal.api.models.UpdateActivityByIdRequest
import io.getstream.feed.client.internal.api.models.UpdateActivityRequest
import io.getstream.feed.client.internal.api.models.UpdateReactionRequest
import io.getstream.feed.client.internal.api.models.UpstreamActivityDto
import io.getstream.feed.client.internal.api.models.UserDto
import retrofit2.Response

/**
 * Extension function to transform [DataDto] to [Actor].
 *
 * @receiver A DataDto.
 * @return An Actor.
 */
private fun DataDto.toActorDomain(): Actor = Actor(id = id, data = data)

/**
 * Extension function to transform [DataDto] to [Object].
 *
 * @receiver A DataDto.
 * @return An Object.
 */
private fun DataDto.toObjectDomain(): Object = Object(id = id, data = data)

/**
 * Extension function to transform [DataDto] to [Target].
 *
 * @receiver A DataDto.
 * @return A Target.
 */
private fun DataDto.toTargetDomain(): Target = Target(id = id, data = data)

/**
 * Extension function to transform [DownstreamActivityDto] to [FeedActivity].
 *
 * @param enrich if true an [EnrichActivity] is created, else an [Activity].
 * @receiver A DownstreamActivityDto.
 * @return A FeedActivity.
 */
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

/**
 * Extension function to transform [String] to [FeedID].
 *
 * @receiver A String that follow the structure `slug:userId`.
 * @return A FeedID.
 */
private fun String.toFeedID(): FeedID = split(":").let { FeedID(it[0], it[1]) }

/**
 * Extension function to transform [FeedID] to [String].
 *
 * @receiver A FeedID.
 * @return A String with the format `slug:userId`.
 */
internal fun FeedID.toStringFeedID(): String = "$slug:$userID"

/**
 * Extension function to transform [FilterReactionsResponse] to [List] of [Reaction].
 *
 * @receiver A FilterReactionsResponse.
 * @return A list of Reactions.
 */
internal fun FilterReactionsResponse.toDomin(): List<Reaction> =
    reactions.map(ReactionDto::toDomain)

/**
 * Extension function to transform [UpdateActivitiesResponse] to [List] of [FeedActivity].
 *
 * By default FeedActivities are enriched.
 *
 * @receiver A UpdateActivitiesResponse.
 * @return A list of FeedActivities.
 */
internal fun UpdateActivitiesResponse.toDomain(): List<FeedActivity> =
    activities.map { it.toDomain(true) }

/**
 * Extension function to transform [ActivitiesResponse] to [List] of [FeedActivity].
 *
 * @param enrich if true an [EnrichActivity] is created, else an [Activity].
 * @receiver A ActivitiesResponse.
 * @return A list of FeedActivities.
 */
internal fun ActivitiesResponse.toDomain(enrich: Boolean): List<FeedActivity> =
    activities.map { it.toDomain(enrich) }

/**
 * Extension function to transform [UpdateReactionParams] to [UpdateReactionRequest].
 *
 * @receiver A UpdateReactionParams.
 * @return A UpdateReactionRequest.
 */
internal fun UpdateReactionParams.toDTO(): UpdateReactionRequest =
    UpdateReactionRequest(
        data = data.takeUnless(Map<String, Any>::isEmpty),
        targeFeeds = targetFeeds.map(FeedID::toStringFeedID).takeUnless(List<String>::isEmpty)
    )

/**
 * Extension function to transform [FeedActivity] to [UpstreamActivityDto].
 *
 * @receiver A FeedActivity.
 * @return A UpstreamActivityDto.
 */
internal fun FeedActivity.toDTO(): UpstreamActivityDto = when (this) {
    is Activity -> toDTO()
    is EnrichActivity -> toDTO()
}

/**
 * Extension function to transform [Activity] to [UpstreamActivityDto].
 *
 * @receiver A Activity.
 * @return A UpstreamActivityDto.
 */
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

/**
 * Extension function to transform [EnrichActivity] to [UpstreamActivityDto].
 *
 * @receiver A EnrichActivity.
 * @return A UpstreamActivityDto.
 */
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

/**
 * Extension function to transform a [List] of [UpdateActivityParams] to [UpdateActivitiesRequest].
 *
 * @receiver A list of UpdateActivityParams.
 * @return A UpdateActivitiesRequest.
 */
internal fun List<UpdateActivityParams>.toDTO(): UpdateActivitiesRequest =
    UpdateActivitiesRequest(
        updates = this.map(UpdateActivityParams::toDTO)
    )

/**
 * Extension function to transform [UpdateActivityParams] to [UpdateActivityRequest].
 *
 * Depending on the type of [UpdateActivityParams] the returned value will be of type [UpdateActivityByIdRequest] or
 * [UpdateActivityByForeignIdRequest].
 *
 * @receiver A UpdateActivityParams.
 * @return A UpdateActivityRequest.
 */
internal fun UpdateActivityParams.toDTO(): UpdateActivityRequest = when (this) {
    is UpdateActivityByForeignIdParams -> this.toDTO()
    is UpdateActivityByIdParams -> this.toDTO()
}

/**
 * Extension function to transform a [UpdateActivityByIdParams] to [UpdateActivitiesRequest].
 *
 * @receiver A UpdateActivityByIdParams.
 * @return A UpdateActivitiesRequest.
 */
internal fun UpdateActivityByIdParams.toDTO(): UpdateActivityByIdRequest =
    UpdateActivityByIdRequest(
        id = activityId,
        set = set,
        unset = unset
    )

/**
 * Extension function to transform a [UpdateActivityByForeignIdParams] to [UpdateActivityByForeignIdRequest].
 *
 * @receiver A UpdateActivityByForeignIdParams.
 * @return A UpdateActivityByForeignIdRequest.
 */
internal fun UpdateActivityByForeignIdParams.toDTO(): UpdateActivityByForeignIdRequest =
    UpdateActivityByForeignIdRequest(
        foreignId = foreignId,
        time = time,
        set = set,
        unset = unset
    )

/**
 * Extension function to transform a [CreateActivitiesResponse] to [List] of [FeedActivity].
 *
 * @param enrich if true an [EnrichActivity] is created, else an [Activity].
 * @receiver A CreateActivitiesResponse.
 * @return A list of FeedActivities.
 */
internal fun CreateActivitiesResponse.toDomain(enrich: Boolean): List<FeedActivity> =
    activities.map { it.toDomain(enrich) }

/**
 * Extension function to transform a [FollowRelationResponse] to [List] of [FollowRelation].
 *
 * @receiver A FollowRelationResponse.
 * @return A list of FollowRelation.
 */
internal fun FollowRelationResponse.toDomain(): List<FollowRelation> =
    followRelations.map(FollowRelationDto::toDomain)

/**
 * Extension function to transform a [FollowRelationDto] to [FollowRelation].
 *
 * @receiver A FollowRelationDto.
 * @return A FollowRelation.
 */
internal fun FollowRelationDto.toDomain(): FollowRelation =
    FollowRelation(
        sourceFeedID = sourceFeedID.toFeedID(),
        targetFeedID = targetFeedID.toFeedID(),
    )

/**
 * Extension function to transform a [UserDto] to [User].
 *
 * @receiver A UserDto.
 * @return A User.
 */
internal fun UserDto.toDomain(): User =
    User(
        id = id,
        data = data,
        followersCount = followersCount ?: 0,
        followingCount = followingCount ?: 0
    )

/**
 * Extension function to transform a [AggregatedActivitiesGroupResponse] to [List] of [AggregatedActivitiesGroup].
 *
 * @param enrich if true activities within every AggregatedActivitiesGroup are enriched.
 * @receiver A AggregatedActivitiesGroupResponse.
 * @return A list of AggregatedActivitiesGroup.
 */
internal fun AggregatedActivitiesGroupResponse.toDomain(enrich: Boolean): List<AggregatedActivitiesGroup> =
    activitiesGroups.map { it.toDomain(enrich) }

/**
 * Extension function to transform a [AggregatedActivitiesGroupDto] to [AggregatedActivitiesGroup].
 *
 * @param enrich if true activities within AggregatedActivitiesGroup are enriched.
 * @receiver A AggregatedActivitiesGroupDto.
 * @return A AggregatedActivitiesGroup.
 */
internal fun AggregatedActivitiesGroupDto.toDomain(enrich: Boolean): AggregatedActivitiesGroup =
    AggregatedActivitiesGroup(
        id = id,
        activities = activities.map { it.toDomain(enrich) },
        verb = verb,
        group = group,
        actorCount = actorCount,
    )

/**
 * Extension function to transform a [NotificationsActivitiesGroupResponse] to [List] of [NotificationActivitiesGroup].
 *
 * @param enrich if true activities within every NotificationActivitiesGroup are enriched.
 * @receiver A NotificationsActivitiesGroupResponse.
 * @return A list of NotificationActivitiesGroup.
 */
internal fun NotificationsActivitiesGroupResponse.toDomain(enrich: Boolean): List<NotificationActivitiesGroup> =
    activitiesGroups.map { it.toDomain(enrich) }

/**
 * Extension function to transform a [NotificationActivitiesGroupDto] to [NotificationActivitiesGroup].
 *
 * @param enrich if true activities within NotificationActivitiesGroup are enriched.
 * @receiver A NotificationActivitiesGroupDto.
 * @return A NotificationActivitiesGroup.
 */
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

/**
 * Extension function to transform a [ReactionDto] to [Reaction].
 *
 * @receiver A ReactionDto.
 * @return A Reaction.
 */
internal fun ReactionDto.toDomain(): Reaction =
    Reaction(
        id = id,
        kind = kind,
        activityId = activityId,
        targetFeeds = targeFeeds?.map(String::toFeedID) ?: emptyList(),
        data = data,
        targetFeedsExtraData = targetFeedsExtraData ?: emptyMap(),
    )

/**
 * Extension function to transform a [Reaction] to [ReactionRequest].
 *
 * @param userId of the user that performs the reaction.
 * @receiver A Reaction.
 * @return A ReactionRequest.
 */
internal fun Reaction.toDTO(userId: String): ReactionRequest =
    ReactionRequest(
        kind = kind,
        activityId = activityId,
        userId = userId,
        extraData = data.takeUnless(Map<String, Any>::isEmpty),
        targeFeeds = targetFeeds.map(FeedID::toStringFeedID).takeUnless(List<String>::isEmpty),
        targetFeedsExtraData = targetFeedsExtraData.takeUnless(Map<String, Any>::isEmpty),
    )

/**
 * Extension function to transform a [CollectionDto] to [CollectionData].
 *
 * @receiver A CollectionDto.
 * @return A CollectionData.
 */
internal fun CollectionDto.toDomain(): CollectionData =
    CollectionData(
        id = id,
        name = name,
        foreignId = foreignId,
        data = data
    )

/**
 * Extension function to transform a [CollectionData] to [CollectionRequest].
 *
 * @param userId of the owner of the collection.
 * @receiver A CollectionData.
 * @return A CollectionRequest.
 */
internal fun CollectionData.toDTO(userId: String): CollectionRequest =
    CollectionRequest(
        id = id,
        userId = userId,
        data = data,
    )

/**
 * Extension function to parse [Response] into the expected DTO of type [T].
 *
 * @param T the type of the expected DTO.
 * @receiver A Response.
 * @return An [Either] with the expected DTO of type [T] or an error [StreamError].
 */
internal fun <T> Response<T>.obtainEntity(): Either<StreamError, T> = when (isSuccessful) {
    true -> body().rightIfNotNull { toError() }
    false -> toError().left()
}

/**
 * Extension function to parse [Response] into an [StreamError].
 * It will try to obtain an Stream Feed error from the json returned form the backend or a [NetworkError] in other case.
 *
 * @receiver A Response.
 * @return A StreamError.
 */
private fun Response<*>.toError(): StreamError = this.errorBody()
    ?.string()
    ?.let {
        try {
            FeedMoshiConverterFactory.moshi.adapter(ErrorResponse::class.java).fromJson(it)?.toDomain()
        } catch (_: Exception) {
            null
        } ?: NetworkError(it)
    } ?: NetworkError(this.toString())

/**
 * Extension function to transform a [ErrorResponse] to [StreamAPIError].
 *
 * @receiver A ErrorResponse.
 * @return A StreamAPIError.
 */
private fun ErrorResponse.toDomain(): StreamAPIError = StreamAPIError(
    detail = detail,
    statusCode = statusCode,
    errorCode = errorCode,
    error = error,
    moreInfo = moreInfo,
)
