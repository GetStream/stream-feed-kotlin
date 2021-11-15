package io.getstream.feed.client

import arrow.core.Either
import io.getstream.feed.client.internal.api.FeedApi
import io.getstream.feed.client.internal.api.models.ActivitiesResponse
import io.getstream.feed.client.internal.obtainEntity
import io.getstream.feed.client.internal.toDomain
import io.getstream.feed.client.internal.validate

class FlatFeed internal constructor(
    private val feedApi: FeedApi,
    private val feedID: FeedID,
) {
    suspend fun getActivities(params: GetActivitiesParams.() -> Unit = {}): Either<Unit, List<FeedActivity>> {
        val getActivitiesParams = GetActivitiesParams().apply(params).validate()
        return feedApi.activities(
            slug = feedID.slug,
            id = feedID.userID,
            limit = getActivitiesParams.limit,
            offset = getActivitiesParams.offset,
            idGreaterThan = getActivitiesParams.idGreaterThan,
            idSmallerThan = getActivitiesParams.idSmallerThan,
            idGreaterThanOrEqual = getActivitiesParams.idGreaterThanOrEqual,
            idSmallerThanOrEqual = getActivitiesParams.idSmallerThanOrEqual,
            withRecentReactions = getActivitiesParams.withRecentReactions,
            withOwnReactions = getActivitiesParams.withOwnReactions,
            withReactionCounts = getActivitiesParams.withReactionCounts,
            recentReactionsLimit = getActivitiesParams.recentReactionsLimit,
        ).obtainEntity().map(ActivitiesResponse::toDomain)
    }
}
