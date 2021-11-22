package io.getstream.feed.client

import arrow.core.Either
import arrow.core.computations.either
import io.getstream.feed.client.internal.api.FeedApi
import io.getstream.feed.client.internal.api.models.ActivitiesRequest
import io.getstream.feed.client.internal.api.models.CreateActivitiesResponse
import io.getstream.feed.client.internal.api.models.FollowRelationResponse
import io.getstream.feed.client.internal.api.models.FollowRequest
import io.getstream.feed.client.internal.obtainEntity
import io.getstream.feed.client.internal.toDTO
import io.getstream.feed.client.internal.toDomain
import io.getstream.feed.client.internal.toStringFeedID
import io.getstream.feed.client.internal.validate

abstract class Feed internal constructor(
    private val feedApi: FeedApi,
    private val feedID: FeedID,
) {

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

    suspend fun followed(params: FollowedParams.() -> Unit): Either<StreamError, List<FollowRelation>> =
        either {
            val followedParams = FollowedParams()
                .apply(params)
                .validate()
                .bind()
            feedApi.followed(
                slug = feedID.slug,
                id = feedID.userID,
                limit = followedParams.limit,
                offset = followedParams.offset,
            ).obtainEntity()
                .map(FollowRelationResponse::toDomain)
                .bind()
        }

    suspend fun followers(params: FollowersParams.() -> Unit): Either<StreamError, List<FollowRelation>> =
        either {
            val followersParams = FollowersParams()
                .apply(params)
                .validate()
                .bind()
            feedApi.followers(
                slug = feedID.slug,
                id = feedID.userID,
                limit = followersParams.limit,
                offset = followersParams.offset,
            ).obtainEntity()
                .map(FollowRelationResponse::toDomain)
                .bind()
        }

    suspend fun removeActivity(params: RemoveActivityParams): Either<StreamError, Unit> =
        either {
            val removeActivityParams: RemoveActivityParams = params.validate().bind()
            when (removeActivityParams) {
                is RemoveActivityByForeignId -> feedApi.removeActivityByForeignId(
                    slug = feedID.slug,
                    id = feedID.userID,
                    removeActivityParams.foreignId,
                )
                is RemoveActivityById -> feedApi.removeActivityById(
                    slug = feedID.slug,
                    id = feedID.userID,
                    removeActivityParams.activityId,
                )
            }.obtainEntity()
                .bind()
        }
}
