package io.getstream.feed.client.internal

import io.getstream.feed.client.GetActivitiesParams
import java.lang.IllegalArgumentException
import kotlin.jvm.Throws

@Throws(IllegalArgumentException::class)
internal fun GetActivitiesParams.validate(): GetActivitiesParams = this.also {
    require(limit > 0) { "limit can't be negative" }
    require(offset?.let { it >= 0 } ?: true) { "offset can't be negative" }
    require(
        (listOfNotNull(idGreaterThan, idGreaterThanOrEqual)).size <= 1
    ) { "Passing both idGreaterThan and idGreaterThanOrEqual is not supported" }
    require(
        (listOfNotNull(idSmallerThan, idSmallerThanOrEqual)).size <= 1
    ) { "Passing both idSmallerThan and idSmallerThanOrEqual is not supported" }
    require(
        (listOfNotNull(idGreaterThan, idGreaterThanOrEqual, idSmallerThan, idSmallerThanOrEqual)).size <= 1
    ) { "Passing both idGreaterThan[OrEqual] and idSmallerThan[OrEqual] is not supported" }
    require(recentReactionsLimit?.let { it > 0 } ?: true) { "recentReactionsLimit can't be negative" }
}
