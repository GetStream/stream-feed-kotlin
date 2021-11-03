package io.getstream.feed.client.internal.api.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
internal data class ActivitiesResponse(
    @Json(name = "results") val activities: List<DownstreamActivitySealedDto>,
)

@JsonClass(generateAdapter = true)
internal data class CreateActivitiesResponse(
    @Json(name = "activities") val activities: List<DownstreamActivitySealedDto>,
)
