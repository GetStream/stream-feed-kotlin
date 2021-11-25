package io.getstream.feed.client

import io.getstream.feed.client.internal.api.models.ActivitiesRequest
import io.getstream.feed.client.internal.api.models.ActorDto
import io.getstream.feed.client.internal.api.models.DataDto
import io.getstream.feed.client.internal.api.models.DownstreamActivityDto
import io.getstream.feed.client.internal.api.models.DownstreamEnrichActivityDto
import io.getstream.feed.client.internal.api.models.UpdateActivitiesRequest
import io.getstream.feed.client.internal.api.models.UpdateActivityByForeignIdRequest
import io.getstream.feed.client.internal.api.models.UpdateActivityByIdRequest
import io.getstream.feed.client.internal.api.models.UpdateActivityRequest
import io.getstream.feed.client.internal.api.models.UpstreamActivityDto

internal object JSONMother {

    fun ActorDto.toJsonString(extraDataJsonString: String? = null): String = """
            {
            "id" : "$id",
            "data" : 
                {
                "handle": "${data.handle}",
                "name": "${data.name}",
                "profileImage": "${data.profileImage}"
                },
            ${extraDataJsonString ?: ""} 
            }
    """.trimIndent().sanitizeJson()

    fun UpstreamActivityDto.toJsonString(extraDataJsonString: String? = null): String = """
        {
        "actor" : "$actor",
        "object" : "$objectProperty",
        "verb" : "$verb",
        ${target?.let { "\"target\" : \"$it\"," } ?: ""}
        ${time?.let { "\"time\" : \"$it\"," } ?: ""}
        ${to?.let { "\"to\" : ${it.toJsonArrayString { "\"$it\"" }}," } ?: ""}
        ${foreignId?.let { "\"foreign_id\" : \"$it\","} ?: ""}
        ${extraDataJsonString ?: ""} 
        }
    """.trimIndent().sanitizeJson()

    fun DownstreamActivityDto.toJsonString(extraDataJsonString: String? = null): String = """
        {
        "id": "$id",
        "actor" : "$actor",
        "object" : "$objectProperty",
        "verb" : "$verb",
        ${target?.let { "\"target\" : \"$it\"," } ?: ""}
        ${time?.let { "\"time\" : \"$it\"," } ?: ""}
        ${to?.let { "\"to\" : ${it.toJsonArrayString { "\"$it\"" }}," } ?: ""}
        ${foreignId?.let { "\"foreign_id\" : \"$it\","} ?: ""}
        ${extraDataJsonString ?: ""} 
        }
    """.trimIndent().sanitizeJson()

    fun DownstreamEnrichActivityDto.toJsonString(extraDataJsonString: String? = null): String = """
        {
        "id": "$id",
        "actor" : ${actor.toJsonString("")},
        "object" : "$objectProperty",
        "verb" : "$verb",
        ${target?.let { "\"target\" : \"$it\"," } ?: ""}
        ${time?.let { "\"time\" : \"$it\"," } ?: ""}
        ${to?.let { "\"to\" : ${it.toJsonArrayString { "\"$it\"" }}," } ?: ""}
        ${foreignId?.let { "\"foreign_id\" : \"$it\","} ?: ""}
        ${extraDataJsonString ?: ""} 
        }
    """.trimIndent().sanitizeJson()

    fun UpdateActivityByIdRequest.toJsonString(setJsonString: String? = null): String = """
        {
        "id": "$id",
        "set": ${setJsonString ?: "{}"},
        "unset": ${unset.toJsonArrayString { "\"$it\"" }}
        }
    """.trimIndent()

    fun UpdateActivityByForeignIdRequest.toJsonString(setJsonString: String? = null): String = """
        {
        "foreign_id": "$foreignId",
        "time": "$time",
        "set": ${setJsonString ?: "{}"},
        "unset": ${unset.toJsonArrayString { "\"$it\"" }}
        }
    """.trimIndent()

    fun UpdateActivityRequest.toJsonString(): String = when (this) {
        is UpdateActivityByForeignIdRequest -> this.toJsonString()
        is UpdateActivityByIdRequest -> this.toJsonString()
    }

    fun UpdateActivitiesRequest.toJsonString(): String = """
        {
        "changes": ${udpates.toJsonArrayString { it.toJsonString() }}
        }
    """.trimIndent()

    fun ActivitiesRequest.toJsonString(): String = """
        {
        "activities": ${activities.toJsonArrayString { it.toJsonString() }}
        }
    """.trimIndent()

    fun DataDto.toIdStringJsonString(): String = "\"$id\""

    fun DataDto.toJsonString(dataJsonString: String? = null): String = """
        {
            "id" : "$id",
            ${dataJsonString?.let { "\"data\":{$it}" } ?: ""} 
            }
    """.trimIndent().sanitizeJson()

    private fun <T> List<T>.toJsonArrayString(transform: (T) -> String): String = """[${joinToString(",", transform = transform)}]"""

    private fun String.sanitizeJson(): String =
        replace("(?m)^\\s+\n".toRegex(), "")
            .replace(",\\s*\n\\s*}".toRegex(), "}")
}
