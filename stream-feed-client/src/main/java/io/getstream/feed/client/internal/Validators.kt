package io.getstream.feed.client.internal

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import io.getstream.feed.client.EmptyParamError
import io.getstream.feed.client.FeedActivity
import io.getstream.feed.client.FollowParams
import io.getstream.feed.client.GetActivitiesParams
import io.getstream.feed.client.IncompatibleParamsError
import io.getstream.feed.client.InvalidParamError
import io.getstream.feed.client.NegativeParamError
import io.getstream.feed.client.ParamError
import io.getstream.feed.client.UnfollowParams

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

internal fun List<FeedActivity>.validate(): Either<ParamError, List<FeedActivity>> = when {
    size <= 0 -> EmptyParamError("The list of activities can't be empty").left()
    else -> this.right()
}

internal fun FollowParams.validate(): Either<ParamError, FollowParams> = when {
    !isInitialized -> EmptyParamError("targetFeedID property need to be initialized").left()
    activityCopyLimit?.let { it < 0 } == true -> NegativeParamError("activityCopyLimit can't be negative").left()
    activityCopyLimit?.let { it > 1000 } == true -> InvalidParamError("activityCopyLimit can't be bigger than 1000").left()
    else -> this.right()
}

internal fun UnfollowParams.validate(): Either<ParamError, UnfollowParams> = when {
    !isInitialized -> EmptyParamError("targetFeedID property need to be initialized").left()
    else -> this.right()
}
