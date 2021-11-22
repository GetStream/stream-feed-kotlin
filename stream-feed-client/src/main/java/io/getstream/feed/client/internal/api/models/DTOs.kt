package io.getstream.feed.client.internal.api.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
internal data class ActorDto(
    @Json(name = "id") val id: String,
    @Json(name = "data") val data: ActorDataDto,
    override val extraData: MutableMap<String, Any> = mutableMapOf(),
) : CustomExtraDTOs()

@JsonClass(generateAdapter = true)
internal data class ActorDataDto(
    @Json(name = "handle") val handle: String,
    @Json(name = "name") val name: String,
    @Json(name = "profileImage") val profileImage: String,
)

internal sealed class ActivitySealedDto : CustomExtraDTOs() {
    @Json(name = "object") abstract val objectProperty: String
    @Json(name = "verb") abstract val verb: String
    @Json(name = "target") abstract val target: String?
    @Json(name = "time") abstract val time: String?
    @Json(name = "to") abstract val to: List<String>?
    @Json(name = "foreign_id") abstract val foreignId: String?
}
internal sealed class DownstreamActivitySealedDto : ActivitySealedDto() {
    abstract val id: String
}

@JsonClass(generateAdapter = true)
internal data class UpstreamActivityDto(
    @Json(name = "actor") val actor: String,
    @Json(name = "object") override val objectProperty: String,
    @Json(name = "verb") override val verb: String,
    @Json(name = "target") override val target: String? = null,
    @Json(name = "time") override val time: String? = null,
    @Json(name = "to") override val to: List<String>? = null,
    @Json(name = "foreign_id") override val foreignId: String? = null,
    override val extraData: MutableMap<String, Any> = mutableMapOf(),
) : ActivitySealedDto()

@JsonClass(generateAdapter = true)
internal data class DownstreamActivityDto(
    @Json(name = "id") override val id: String,
    @Json(name = "actor") val actor: String,
    @Json(name = "object") override val objectProperty: String,
    @Json(name = "verb") override val verb: String,
    @Json(name = "target") override val target: String? = null,
    @Json(name = "time") override val time: String? = null,
    @Json(name = "to") override val to: List<String>? = null,
    @Json(name = "foreign_id") override val foreignId: String? = null,
    override val extraData: MutableMap<String, Any> = mutableMapOf(),
) : DownstreamActivitySealedDto()

@JsonClass(generateAdapter = true)
internal data class DownstreamEnrichActivityDto(
    @Json(name = "id") override val id: String,
    @Json(name = "actor") val actor: ActorDto,
    @Json(name = "object") override val objectProperty: String,
    @Json(name = "verb") override val verb: String,
    @Json(name = "target") override val target: String? = null,
    @Json(name = "time") override val time: String? = null,
    @Json(name = "to") override val to: List<String>? = null,
    @Json(name = "foreign_id") override val foreignId: String? = null,
    override val extraData: MutableMap<String, Any> = mutableMapOf(),
) : DownstreamActivitySealedDto()

@JsonClass(generateAdapter = true)
internal data class FollowRelationDto(
    @Json(name = "feed_id") val sourceFeedID: String,
    @Json(name = "target_id") val targetFeedID: String,
)

@JsonClass(generateAdapter = true)
internal data class AggregatedActivitiesGroupDto(
    @Json(name = "id") val id: String,
    @Json(name = "activities") val activities: List<DownstreamActivitySealedDto>,
    @Json(name = "group") val group: String,
    @Json(name = "actor_count") val actorCount: Int,
    @Json(name = "verb") val verb: String,
)

@JsonClass(generateAdapter = true)
internal data class NotificationActivitiesGroupDto(
    @Json(name = "id") val id: String,
    @Json(name = "activities") val activities: List<DownstreamActivitySealedDto>,
    @Json(name = "group") val group: String,
    @Json(name = "actor_count") val actorCount: Int,
    @Json(name = "is_read") val isRead: Boolean,
    @Json(name = "is_seen") val isSeen: Boolean,
    @Json(name = "verb") val verb: String,
)

internal abstract class CustomExtraDTOs {
    abstract val extraData: MutableMap<String, Any>
}
