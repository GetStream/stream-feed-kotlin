package io.getstream.feed.client.internal

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import io.getstream.feed.client.GetActivitiesParams
import io.getstream.feed.client.IncompatibleParamsError
import io.getstream.feed.client.NegativeParamError
import io.getstream.feed.client.ParamError

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
