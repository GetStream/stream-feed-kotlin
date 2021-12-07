package io.getstream.feed.client.internal.api.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

internal sealed class ActivitySealedDto : CustomExtraDTOs() {
    @Json(name = "verb") abstract val verb: String
    @Json(name = "time") abstract val time: String?
    @Json(name = "to") abstract val to: List<String>?
    @Json(name = "foreign_id") abstract val foreignId: String?
}

@JsonClass(generateAdapter = true)
internal data class UpstreamActivityDto(
    @Json(name = "actor") val actor: String,
    @Json(name = "object") val objectProperty: String,
    @Json(name = "verb") override val verb: String,
    @Json(name = "target") val target: String? = null,
    @Json(name = "time") override val time: String? = null,
    @Json(name = "to") override val to: List<String>? = null,
    @Json(name = "foreign_id") override val foreignId: String? = null,
    override val extraData: MutableMap<String, Any> = mutableMapOf(),
) : ActivitySealedDto()

@JsonClass(generateAdapter = true)
internal data class DownstreamActivityDto(
    @Json(name = "id") val id: String,
    @Json(name = "actor") val actor: DataDto,
    @Json(name = "object") val objectProperty: DataDto,
    @Json(name = "verb") override val verb: String,
    @Json(name = "target") val target: DataDto? = null,
    @Json(name = "time") override val time: String? = null,
    @Json(name = "to") override val to: List<String>? = null,
    @Json(name = "foreign_id") override val foreignId: String? = null,
    override val extraData: MutableMap<String, Any> = mutableMapOf(),
) : ActivitySealedDto()

@JsonClass(generateAdapter = true)
internal data class FollowRelationDto(
    @Json(name = "feed_id") val sourceFeedID: String,
    @Json(name = "target_id") val targetFeedID: String,
)

@JsonClass(generateAdapter = true)
internal data class AggregatedActivitiesGroupDto(
    @Json(name = "id") val id: String,
    @Json(name = "activities") val activities: List<DownstreamActivityDto>,
    @Json(name = "group") val group: String,
    @Json(name = "actor_count") val actorCount: Int,
    @Json(name = "verb") val verb: String,
)

@JsonClass(generateAdapter = true)
internal data class NotificationActivitiesGroupDto(
    @Json(name = "id") val id: String,
    @Json(name = "activities") val activities: List<DownstreamActivityDto>,
    @Json(name = "group") val group: String,
    @Json(name = "actor_count") val actorCount: Int,
    @Json(name = "is_read") val isRead: Boolean,
    @Json(name = "is_seen") val isSeen: Boolean,
    @Json(name = "verb") val verb: String,
)

@JsonClass(generateAdapter = true)
internal data class CollectionDto(
    @Json(name = "id") val id: String,
    @Json(name = "collection") val name: String,
    @Json(name = "foreign_id") val foreignId: String,
    @Json(name = "data") val data: Map<String, Any>
)

@JsonClass(generateAdapter = true)
internal data class ReactionDto(
    @Json(name = "id") val id: String,
    @Json(name = "kind") val kind: String,
    @Json(name = "activity_id") val activityId: String,
    @Json(name = "user_id") val userId: String,
    @Json(name = "user") val user: Map<String, Any>?,
    @Json(name = "data") val data: Map<String, Any>,
    @Json(name = "parent") val parent: String?,
    @Json(name = "target_feeds") val targeFeeds: List<String>?,
    @Json(name = "latest_children") val latestChildren: Map<String, Any>?,
    @Json(name = "children_counts") val childrenCount: Map<String, Any>?,
    @Json(name = "target_feeds_extra_data") val targetFeedsExtraData: Map<String, Any>?
)

internal abstract class CustomExtraDTOs {
    abstract val extraData: MutableMap<String, Any>
}

internal data class DataDto(
    val id: String,
    val data: Map<String, Any>,
)
