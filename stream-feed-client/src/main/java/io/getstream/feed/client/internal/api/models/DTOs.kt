package io.getstream.feed.client.internal.api.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
internal data class ActorDto(
    @Json(name = "id") val id: String,
    @Json(name = "data") val data: ActorDataDto,
) : CustomExtraDTOs()

@JsonClass(generateAdapter = true)
internal data class ActorDataDto(
    @Json(name = "handle") val handle: String,
    @Json(name = "name") val name: String,
    @Json(name = "profileImage") val profileImage: String,
) : CustomExtraDTOs()

internal sealed class ActivitySealedDto : CustomExtraDTOs() {
    @Json(name = "object") abstract val objectProperty: String
    @Json(name = "verb") abstract val verb: String
    @Json(name = "target") abstract val target: String?
    @Json(name = "time") abstract val time: String?
    @Json(name = "to") abstract val to: List<String>?
    @Json(name = "foreign_id") abstract val foreignId: String?
}
internal sealed class UpstreamActivitySealedDto : ActivitySealedDto()
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
) : UpstreamActivitySealedDto()

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
) : DownstreamActivitySealedDto()

@JsonClass(generateAdapter = true)
internal data class UpstreamEnrichActivityDto(
    @Json(name = "actor") val actor: ActorDto,
    @Json(name = "object") override val objectProperty: String,
    @Json(name = "verb") override val verb: String,
    @Json(name = "target") override val target: String? = null,
    @Json(name = "time") override val time: String? = null,
    @Json(name = "to") override val to: List<String>? = null,
    @Json(name = "foreign_id") override val foreignId: String? = null,
) : UpstreamActivitySealedDto()

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
) : DownstreamActivitySealedDto()

internal abstract class CustomExtraDTOs {
    val extraData: MutableMap<String, Any> = mutableMapOf()
}
