package io.getstream.feed.client

import arrow.core.Either
import arrow.core.computations.either
import io.getstream.feed.client.internal.api.FeedApi
import io.getstream.feed.client.internal.api.models.ActivitiesRequest
import io.getstream.feed.client.internal.api.models.ActivitiesResponse
import io.getstream.feed.client.internal.api.models.CreateActivitiesResponse
import io.getstream.feed.client.internal.api.models.FollowRequest
import io.getstream.feed.client.internal.obtainEntity
import io.getstream.feed.client.internal.toDTO
import io.getstream.feed.client.internal.toDomain
import io.getstream.feed.client.internal.toStringFeedID
import io.getstream.feed.client.internal.validate

class FlatFeed internal constructor(
    private val feedApi: FeedApi,
    private val feedID: FeedID,
) {

    suspend fun getActivities(params: GetActivitiesParams.() -> Unit = {}): Either<StreamError, List<FeedActivity>> =
        either {
            val getActivitiesParams = GetActivitiesParams().apply(params).validate().bind()
            when (getActivitiesParams.enrich) {
                true -> feedApi.activities(
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
                false -> feedApi.enrichActivities(
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
                .map(ActivitiesResponse::toDomain)
                .bind()
        }

    suspend fun addActivities(activities: List<FeedActivity>): Either<StreamError, List<FeedActivity>> = either {
        val activitiesRequest = ActivitiesRequest(
            activities = activities
                .validate()
                .bind()
                .map(FeedActivity::toDTO)
        )
        feedApi.sendActivities(
            slug = feedID.slug,
            id = feedID.userID,
            activities = activitiesRequest
        ).obtainEntity()
            .map(CreateActivitiesResponse::toDomain)
            .bind()
    }

    suspend fun addActivity(activity: FeedActivity): Either<StreamError, FeedActivity> =
        addActivities(listOf(activity)).map(List<FeedActivity>::first)

    suspend fun follow(params: FollowParams.() -> Unit = {}): Either<StreamError, Unit> =
        either {
            val followRequest: FollowRequest = FollowParams()
                .apply(params)
                .validate()
                .bind()
                .let { FollowRequest(it.targetFeedID.toStringFeedID(), it.activityCopyLimit) }
            feedApi.follow(
                slug = feedID.slug,
                id = feedID.userID,
                followRequest = followRequest,
            ).obtainEntity()
                .bind()
        }

    suspend fun unfollow(params: UnfollowParams.() -> Unit = {}): Either<StreamError, Unit> =
        either {
            val unfollowParams: UnfollowParams = UnfollowParams()
                .apply(params)
                .validate()
                .bind()
            feedApi.unfollow(
                slug = feedID.slug,
                id = feedID.userID,
                unfollowParams.targetFeedID.toStringFeedID(),
                unfollowParams.keepHistory
            ).obtainEntity()
                .bind()
        }
}
