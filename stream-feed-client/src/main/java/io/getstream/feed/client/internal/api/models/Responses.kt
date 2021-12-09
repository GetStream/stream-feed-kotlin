package io.getstream.feed.client.internal.api.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
internal data class ActivitiesResponse(
    @Json(name = "results") val activities: List<DownstreamActivityDto>,
)

@JsonClass(generateAdapter = true)
internal data class CreateActivitiesResponse(
    @Json(name = "activities") val activities: List<DownstreamActivityDto>,
)

@JsonClass(generateAdapter = true)
internal data class UpdateActivitiesResponse(
    @Json(name = "activities") val activities: List<DownstreamActivityDto>,
)

@JsonClass(generateAdapter = true)
internal data class ErrorResponse(
    @Json(name = "detail") val detail: String,
    @Json(name = "status_code") val statusCode: Int,
    @Json(name = "code") val errorCode: Int,
    @Json(name = "exception") val error: String,
    @Json(name = "more_info") val moreInfo: String,
)

@JsonClass(generateAdapter = true)
internal data class FollowRelationResponse(
    @Json(name = "results") val followRelations: List<FollowRelationDto>
)

@JsonClass(generateAdapter = true)
internal data class AggregatedActivitiesGroupResponse(
    @Json(name = "results") val activitiesGroups: List<AggregatedActivitiesGroupDto>,
)

@JsonClass(generateAdapter = true)
internal data class NotificationsActivitiesGroupResponse(
    @Json(name = "results") val activitiesGroups: List<NotificationActivitiesGroupDto>,
)

@JsonClass(generateAdapter = true)
internal data class FilterReactionsResponse(
    @Json(name = "results") val reactions: List<ReactionDto>,
)
