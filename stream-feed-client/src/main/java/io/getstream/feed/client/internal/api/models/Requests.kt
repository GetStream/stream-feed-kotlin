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
    @Json(name = "changes") val udpates: List<UpdateActivityRequest>
)

@JsonClass(generateAdapter = true)
internal data class ActivitiesRequest(
    @Json(name = "activities") val activities: List<UpstreamActivityDto>
)
