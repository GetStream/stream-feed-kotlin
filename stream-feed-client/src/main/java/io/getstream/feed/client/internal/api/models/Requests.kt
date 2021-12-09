package io.getstream.feed.client.internal.api.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

internal class CommaSeparatedQueryParams<T>(val params: List<T>)

internal sealed class UpdateActivityRequest {
    abstract val set: Map<String, Any>
    abstract val unset: List<String>
}

@JsonClass(generateAdapter = true)
internal data class UpdateActivityByIdRequest(
    @Json(name = "id") val id: String,
    @Json(name = "set") override val set: Map<String, Any> = mapOf(),
    @Json(name = "unset") override val unset: List<String> = listOf(),
) : UpdateActivityRequest()

@JsonClass(generateAdapter = true)
internal data class UpdateActivityByForeignIdRequest(
    @Json(name = "foreign_id") val foreignId: String,
    @Json(name = "time") val time: String,
    @Json(name = "set") override val set: Map<String, Any> = mapOf(),
    @Json(name = "unset") override val unset: List<String> = listOf(),
) : UpdateActivityRequest()

@JsonClass(generateAdapter = true)
internal data class UpdateActivitiesRequest(
    @Json(name = "changes") val updates: List<UpdateActivityRequest>
)

@JsonClass(generateAdapter = true)
internal data class ActivitiesRequest(
    @Json(name = "activities") val activities: List<UpstreamActivityDto>
)

@JsonClass(generateAdapter = true)
internal data class FollowRequest(
    @Json(name = "target") val targetFeedId: String,
    @Json(name = "activity_copy_limit") val activityCopyLimit: Int?,
)

@JsonClass(generateAdapter = true)
internal data class CollectionRequest(
    @Json(name = "data") val data: Map<String, Any>,
    @Json(name = "user_id") val userId: String,
    @Json(name = "id") val id: String,
)

@JsonClass(generateAdapter = true)
internal data class ReactionRequest(
    @Json(name = "kind") val kind: String,
    @Json(name = "activity_id") val activityId: String,
    @Json(name = "user_id") val userId: String,
    @Json(name = "data") val extraData: Map<String, Any>?,
    @Json(name = "target_feeds") val targeFeeds: List<String>?,
    @Json(name = "target_feeds_extra_data") val targetFeedsExtraData: Map<String, Any>?
)

@JsonClass(generateAdapter = true)
internal data class UpdateReactionRequest(
    @Json(name = "data") val data: Map<String, Any>?,
    @Json(name = "target_feeds") val targeFeeds: List<String>?,
)
