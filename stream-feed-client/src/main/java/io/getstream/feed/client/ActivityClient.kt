package io.getstream.feed.client

import arrow.core.Either
import arrow.core.computations.either
import io.getstream.feed.client.internal.api.ActivityApi
import io.getstream.feed.client.internal.api.models.CommaSeparatedQueryParams
import io.getstream.feed.client.internal.api.models.UpdateActivitiesResponse
import io.getstream.feed.client.internal.obtainEntity
import io.getstream.feed.client.internal.toDTO
import io.getstream.feed.client.internal.toDomain
import io.getstream.feed.client.internal.validate

/**
 * Client that allows you to work with activities.
 *
 * @property activityApi The activity endpoint.
 */
class ActivityClient internal constructor(
    private val activityApi: ActivityApi
) {

    /**
     * Find activities that match the given params.
     *
     * @param params A function over [FindActivitiesParams] that defines params to be used.
     * @return An [Either] with the [List] of [FeedActivity] found or an [StreamError].
     */
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

    /**
     * Update an already existing activity with the given params.
     *
     * @param params A function over [UpdateActivityByIdParams] that defines params to be used.
     * @return An [Either] with the updated [FeedActivity] or an [StreamError].
     */
    suspend fun partialUpdateActivityById(params: UpdateActivityByIdParams.() -> Unit = {}): Either<StreamError, FeedActivity> =
        partialUpdateActivity(UpdateActivityByIdParams().apply(params))

    /**
     * Update an already existing activity with the given params.
     *
     * @param params A function over [UpdateActivityByForeignIdParams] that defines params to be used.
     * @return An [Either] with the updated [FeedActivity] or an [StreamError].
     */
    suspend fun partialUpdateActivityByForeignId(params: UpdateActivityByForeignIdParams.() -> Unit = {}): Either<StreamError, FeedActivity> =
        partialUpdateActivity(UpdateActivityByForeignIdParams().apply(params))

    /**
     * Update an already existing activity with the given params.
     *
     * @param params A function over [UpdateActivityParams] that defines params to be used.
     * @return An [Either] with the updated [FeedActivity] or an [StreamError].
     */
    suspend fun partialUpdateActivity(params: UpdateActivityParams): Either<StreamError, FeedActivity> =
        partialUpdateActivities(listOf(params)).map(List<FeedActivity>::first)

    /**
     * Update an already existing activities with the given params.
     *
     * @param params A function over a [List] of [UpdateActivityByIdParams] that defines params to be used.
     * @return An [Either] with the updated [List] of [FeedActivity] or an [StreamError].
     */
    suspend fun partialUpdateActivities(params: List<UpdateActivityParams>): Either<StreamError, List<FeedActivity>> =
        either {
            params.validate()
                .map {
                    activityApi.updateActivities(it.toDTO())
                        .obtainEntity()
                        .map(UpdateActivitiesResponse::toDomain)
                        .bind()
                }
                .bind()
        }
}
