package io.getstream.feed.client

import arrow.core.Either
import arrow.core.computations.either
import io.getstream.feed.client.internal.api.ActivityApi
import io.getstream.feed.client.internal.api.models.CommaSeparatedQueryParams
import io.getstream.feed.client.internal.obtainEntity
import io.getstream.feed.client.internal.toDomain
import io.getstream.feed.client.internal.validate

class ActivityClient internal constructor(
    private val activityApi: ActivityApi
) {

    suspend fun findActivities(params: FindActivitiesParams.() -> Unit = {}): Either<StreamError, List<FeedActivity>> =
        either {
            val findActivitiesParams = FindActivitiesParams().apply(params).validate().bind()
            when (findActivitiesParams.enrich) {
                true -> activityApi.activities(
                    activitiesIds = CommaSeparatedQueryParams(findActivitiesParams.activitiesIds),
                    foreignIds = CommaSeparatedQueryParams(findActivitiesParams.foreignIds),
                    timestamps = CommaSeparatedQueryParams(findActivitiesParams.timestamps),
                    withOwnReactions = findActivitiesParams.withOwnReactions,
                    withReactionCounts = findActivitiesParams.withReactionCounts,
                    withRecentReactions = findActivitiesParams.withRecentReactions,
                    recentReactionsLimit = findActivitiesParams.recentReactionsLimit,
                )
                false -> activityApi.activities(
                    activitiesIds = CommaSeparatedQueryParams(findActivitiesParams.activitiesIds),
                    foreignIds = CommaSeparatedQueryParams(findActivitiesParams.foreignIds),
                    timestamps = CommaSeparatedQueryParams(findActivitiesParams.timestamps),
                    withOwnReactions = findActivitiesParams.withOwnReactions,
                    withReactionCounts = findActivitiesParams.withReactionCounts,
                    withRecentReactions = findActivitiesParams.withRecentReactions,
                    recentReactionsLimit = findActivitiesParams.recentReactionsLimit,
                )
            }
                .obtainEntity()
                .map { it.toDomain(findActivitiesParams.enrich) }
                .bind()
        }
}