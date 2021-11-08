package io.getstream.feed.client

import io.getstream.feed.client.internal.api.models.ActorDto
import io.getstream.feed.client.internal.api.models.DownstreamActivityDto
import io.getstream.feed.client.internal.api.models.DownstreamEnrichActivityDto
import io.getstream.feed.client.internal.api.models.UpstreamActivityDto
import io.getstream.feed.client.internal.api.models.UpstreamEnrichActivityDto

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
    """.trimIndent().replace(",\n \n}", "}")

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
    """.trimIndent().replace(",\n \n}", "}")

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
    """.trimIndent().replace(",\n \n}", "}")

    fun UpstreamEnrichActivityDto.toJsonString(extraDataJsonString: String? = null): String = """
        {
        "actor" : ${actor.toJsonString()},
        "object" : "$objectProperty",
        "verb" : "$verb",
        ${target?.let { "\"target\" : \"$it\"," } ?: ""}
        ${time?.let { "\"time\" : \"$it\"," } ?: ""}
        ${to?.let { "\"to\" : ${it.toJsonArrayString { "\"$it\"" }}," } ?: ""}
        ${foreignId?.let { "\"foreign_id\" : \"$it\","} ?: ""}
        ${extraDataJsonString ?: ""} 
        }
    """.trimIndent().replace(",\n \n}", "}")

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
    """.trimIndent().replace(",\n \n}", "}")

    private fun <T> List<T>.toJsonArrayString(transform: (T) -> String): String = """[${joinToString(",", transform = transform)}]"""
}
