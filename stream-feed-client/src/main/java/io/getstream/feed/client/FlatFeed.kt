package io.getstream.feed.client

import arrow.core.Either
import arrow.core.computations.either
import io.getstream.feed.client.internal.api.FlatFeedApi
import io.getstream.feed.client.internal.obtainEntity
import io.getstream.feed.client.internal.toDomain
import io.getstream.feed.client.internal.validate

/**
 * Client that allows you to work with Flat Feed.
 *
 * @property flatFeedApi The Flat Feed endpoint.
 * @property feedID The Flat Feed Id.
 */
class FlatFeed internal constructor(
    private val flatFeedApi: FlatFeedApi,
    private val feedID: FeedID,
) : Feed(flatFeedApi, feedID) {

    /**
     * Obtain activities that match the given params.
     *
     * @param params A function over [GetActivitiesParams] that defines params to be used.
     * @return An [Either] with the [List] of [FeedActivity] found or an [StreamError].
     */
    suspend fun getActivities(params: GetActivitiesParams.() -> Unit = {}): Either<StreamError, List<FeedActivity>> =
        either {
            val getActivitiesParams = GetActivitiesParams().apply(params).validate().bind()
            when (getActivitiesParams.enrich) {
                true -> flatFeedApi.activities(
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
                )
                false -> flatFeedApi.enrichActivities(
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
                )
            }
                .obtainEntity()
                .map { it.toDomain(getActivitiesParams.enrich) }
                .bind()
        }
}
