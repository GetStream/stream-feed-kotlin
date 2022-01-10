package io.getstream.feed.client.internal

import arrow.core.Either
import arrow.core.computations.either
import arrow.core.left
import arrow.core.right
import io.getstream.feed.client.EmptyParamError
import io.getstream.feed.client.FeedActivity
import io.getstream.feed.client.FilterReactionsParams
import io.getstream.feed.client.FindActivitiesParams
import io.getstream.feed.client.FindUserParams
import io.getstream.feed.client.FollowParams
import io.getstream.feed.client.FollowedParams
import io.getstream.feed.client.FollowersParams
import io.getstream.feed.client.GetActivitiesParams
import io.getstream.feed.client.IncompatibleParamsError
import io.getstream.feed.client.InvalidParamError
import io.getstream.feed.client.NegativeParamError
import io.getstream.feed.client.ParamError
import io.getstream.feed.client.Reaction
import io.getstream.feed.client.RemoveActivityByForeignId
import io.getstream.feed.client.RemoveActivityById
import io.getstream.feed.client.RemoveActivityParams
import io.getstream.feed.client.UnfollowParams
import io.getstream.feed.client.UpdateActivityByForeignIdParams
import io.getstream.feed.client.UpdateActivityByIdParams
import io.getstream.feed.client.UpdateActivityParams
import io.getstream.feed.client.UpdateReactionParams
import io.getstream.feed.client.UpdateUserParams
import io.getstream.feed.client.UserParams

/**
 * Extension Function that checks a [GetActivitiesParams] was created with valid data.
 *
 * @receiver A GetActivitiesParams.
 * @return An [Either] with the same receiver value or the [ParamError].
 */
internal fun GetActivitiesParams.validate(): Either<ParamError, GetActivitiesParams> = when {
    limit < 0 -> NegativeParamError("limit can't be negative").left()
    offset?.let { it < 0 } == true -> NegativeParamError("offset can't be negative").left()
    listOfNotNull(idGreaterThan, idGreaterThanOrEqual).size > 1 ->
        IncompatibleParamsError("Passing both idGreaterThan and idGreaterThanOrEqual is not supported").left()
    listOfNotNull(idSmallerThan, idSmallerThanOrEqual).size > 1 ->
        IncompatibleParamsError("Passing both idSmallerThan and idSmallerThanOrEqual is not supported").left()
    listOfNotNull(idGreaterThan, idGreaterThanOrEqual, idSmallerThan, idSmallerThanOrEqual).size > 1 ->
        IncompatibleParamsError("Passing both idGreaterThan[OrEqual] and idSmallerThan[OrEqual] is not supported").left()
    recentReactionsLimit?.let { it < 0 } == true -> NegativeParamError("recentReactionsLimit can't be negative").left()
    else -> this.right()
}

/**
 * Extension Function that checks a [List] of [FeedActivity] was created with valid data.
 *
 * @receiver A list of FeedActivity.
 * @return An [Either] with the same receiver value or the [ParamError].
 */
internal fun List<FeedActivity>.validate(): Either<ParamError, List<FeedActivity>> = when {
    size <= 0 -> EmptyParamError("The list of activities can't be empty").left()
    else -> this.right()
}

/**
 * Extension Function that checks a [Reaction] was created with valid data.
 *
 * @receiver A Reaction.
 * @return An [Either] with the same receiver value or the [ParamError].
 */
internal fun Reaction.validate(): Either<ParamError, Reaction> = when {
    kind.isBlank() -> EmptyParamError("kind can't be empty").left()
    activityId.isBlank() -> EmptyParamError("activityId can't be empty").left()
    else -> this.right()
}

/**
 * Extension Function that checks a [FollowParams] was created with valid data.
 *
 * @receiver A FollowParams.
 * @return An [Either] with the same receiver value or the [ParamError].
 */
internal fun FollowParams.validate(): Either<ParamError, FollowParams> = when {
    !isInitialized -> EmptyParamError("targetFeedID property need to be initialized").left()
    activityCopyLimit?.let { it < 0 } == true -> NegativeParamError("activityCopyLimit can't be negative").left()
    activityCopyLimit?.let { it > 1000 } == true -> InvalidParamError("activityCopyLimit can't be bigger than 1000").left()
    else -> this.right()
}

/**
 * Extension Function that checks a [UnfollowParams] was created with valid data.
 *
 * @receiver A UnfollowParams.
 * @return An [Either] with the same receiver value or the [ParamError].
 */
internal fun UnfollowParams.validate(): Either<ParamError, UnfollowParams> = when {
    !isInitialized -> EmptyParamError("targetFeedID property need to be initialized").left()
    else -> this.right()
}

/**
 * Extension Function that checks a [FollowedParams] was created with valid data.
 *
 * @receiver A FollowedParams.
 * @return An [Either] with the same receiver value or the [ParamError].
 */
internal fun FollowedParams.validate(): Either<ParamError, FollowedParams> = when {
    limit?.let { it < 0 } == true -> NegativeParamError("limit can't be negative").left()
    offset?.let { it < 0 } == true -> NegativeParamError("offset can't be negative").left()
    else -> this.right()
}

/**
 * Extension Function that checks a [FollowersParams] was created with valid data.
 *
 * @receiver A FollowersParams.
 * @return An [Either] with the same receiver value or the [ParamError].
 */
internal fun FollowersParams.validate(): Either<ParamError, FollowersParams> = when {
    limit?.let { it < 0 } == true -> NegativeParamError("limit can't be negative").left()
    offset?.let { it < 0 } == true -> NegativeParamError("offset can't be negative").left()
    else -> this.right()
}

/**
 * Extension Function that checks a [RemoveActivityParams] was created with valid data.
 *
 * @receiver A RemoveActivityParams.
 * @return An [Either] with the same receiver value or the [ParamError].
 */
internal fun RemoveActivityParams.validate(): Either<ParamError, RemoveActivityParams> = when {
    (this as? RemoveActivityById)?.activityId?.isBlank() == true -> EmptyParamError("activityId can't be empty").left()
    (this as? RemoveActivityByForeignId)?.foreignId?.isBlank() == true -> EmptyParamError("foreignId can't be empty").left()
    else -> this.right()
}

/**
 * Extension Function that checks a [FindActivitiesParams] was created with valid data.
 *
 * @receiver A FindActivitiesParams.
 * @return An [Either] with the same receiver value or the [ParamError].
 */
internal fun FindActivitiesParams.validate(): Either<ParamError, FindActivitiesParams> = when {
    activitiesIds.size > 100 -> InvalidParamError("activitiesIds can't be bigger than 100").left()
    foreignIds.size > 100 -> InvalidParamError("foreignIds can't be bigger than 100").left()
    timestamps.size > 100 -> InvalidParamError("timestamps can't be bigger than 100").left()
    foreignIds.size != timestamps.size -> IncompatibleParamsError("foreignIds length must match timestamps length").left()
    recentReactionsLimit?.let { it < 0 } == true -> NegativeParamError("recentReactionsLimit can't be negative").left()
    else -> this.right()
}

/**
 * Extension Function that checks a [List] of [UpdateActivityParams] was created with valid data.
 *
 * @receiver A list of UpdateActivityParams.
 * @return An [Either] with the same receiver value or the [ParamError].
 */
@JvmName("validateUpdateActivityParams")
internal fun List<UpdateActivityParams>.validate(): Either<ParamError, List<UpdateActivityParams>> = when {
    size <= 0 -> EmptyParamError("The list of updateActivities can't be empty").left()
    else -> either.eager { this@validate.map { it.validate().bind() } }
}

/**
 * Extension Function that checks a [UpdateActivityParams] was created with valid data.
 *
 * @receiver A UpdateActivityParams.
 * @return An [Either] with the same receiver value or the [ParamError].
 */
internal fun UpdateActivityParams.validate(): Either<ParamError, UpdateActivityParams> = when (this) {
    is UpdateActivityByForeignIdParams -> this.validate()
    is UpdateActivityByIdParams -> this.validate()
}

/**
 * Extension Function that checks a [UpdateActivityByIdParams] was created with valid data.
 *
 * @receiver A UpdateActivityByIdParams.
 * @return An [Either] with the same receiver value or the [ParamError].
 */
internal fun UpdateActivityByIdParams.validate(): Either<ParamError, UpdateActivityByIdParams> = when {
    !isInitialized -> EmptyParamError("activityId property need to be initialized").left()
    else -> this.right()
}

/**
 * Extension Function that checks a [UpdateActivityByForeignIdParams] was created with valid data.
 *
 * @receiver A UpdateActivityByForeignIdParams.
 * @return An [Either] with the same receiver value or the [ParamError].
 */
internal fun UpdateActivityByForeignIdParams.validate(): Either<ParamError, UpdateActivityByForeignIdParams> = when {
    !isInitialized -> EmptyParamError("foreignId and time properties need to be initialized").left()
    else -> this.right()
}

/**
 * Extension Function that checks a [FilterReactionsParams] was created with valid data.
 *
 * @receiver A FilterReactionsParams.
 * @return An [Either] with the same receiver value or the [ParamError].
 */
internal fun FilterReactionsParams.validate(): Either<ParamError, FilterReactionsParams> = when {
    !isInitialized -> EmptyParamError("lookup property need to be initialized").left()
    limit < 0 -> NegativeParamError("limit can't be negative").left()
    listOfNotNull(idGreaterThan, idGreaterThanOrEqual).size > 1 ->
        IncompatibleParamsError("Passing both idGreaterThan and idGreaterThanOrEqual is not supported").left()
    listOfNotNull(idSmallerThan, idSmallerThanOrEqual).size > 1 ->
        IncompatibleParamsError("Passing both idSmallerThan and idSmallerThanOrEqual is not supported").left()
    listOfNotNull(idGreaterThan, idGreaterThanOrEqual, idSmallerThan, idSmallerThanOrEqual).size > 1 ->
        IncompatibleParamsError("Passing both idGreaterThan[OrEqual] and idSmallerThan[OrEqual] is not supported").left()
    else -> this.right()
}

/**
 * Extension Function that checks a [UpdateReactionParams] was created with valid data.
 *
 * @receiver A UpdateReactionParams.
 * @return An [Either] with the same receiver value or the [ParamError].
 */
internal fun UpdateReactionParams.validate(): Either<ParamError, UpdateReactionParams> = when {
    !isInitialized -> EmptyParamError("reactionId property need to be initialized").left()
    else -> this.right()
}

/**
 * Extension Function that checks a [UserParams] was created with valid data.
 *
 * @receiver A UserParams.
 * @return An [Either] with the same receiver value or the [ParamError].
 */
internal fun UserParams.validate(): Either<ParamError, UserParams> = when {
    !isInitialized -> EmptyParamError("userId property need to be initialized").left()
    else -> this.right()
}

/**
 * Extension Function that checks a [UpdateUserParams] was created with valid data.
 *
 * @receiver A UpdateUserParams.
 * @return An [Either] with the same receiver value or the [ParamError].
 */
internal fun UpdateUserParams.validate(): Either<ParamError, UpdateUserParams> = when {
    !isInitialized -> EmptyParamError("userId property need to be initialized").left()
    else -> this.right()
}

/**
 * Extension Function that checks a [FindUserParams] was created with valid data.
 *
 * @receiver A FindUserParams.
 * @return An [Either] with the same receiver value or the [ParamError].
 */
internal fun FindUserParams.validate(): Either<ParamError, FindUserParams> = when {
    !isInitialized -> EmptyParamError("userId property need to be initialized").left()
    else -> this.right()
}
