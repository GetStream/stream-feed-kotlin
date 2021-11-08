package io.getstream.feed.client

import io.getstream.feed.client.internal.api.models.ActorDataDto
import io.getstream.feed.client.internal.api.models.ActorDto
import io.getstream.feed.client.internal.api.models.CustomExtraDTOs
import io.getstream.feed.client.internal.api.models.DownstreamActivityDto
import io.getstream.feed.client.internal.api.models.DownstreamEnrichActivityDto
import io.getstream.feed.client.internal.api.models.UpstreamActivityDto
import io.getstream.feed.client.internal.api.models.UpstreamEnrichActivityDto
import kotlin.random.Random

internal object Mother {
    private val random = Random(System.currentTimeMillis())
    private val charPool: CharArray = (('a'..'z') + ('A'..'Z') + ('0'..'9')).toCharArray()

    fun randomString(size: Int = 20): String = buildString(capacity = size) {
        repeat(size) {
            append(charPool.random())
        }
    }

    fun <T> randomOrNull(randomGen: () -> T): T? = random.nextBoolean().let {
        when (it) {
            true -> randomGen()
            false -> null
        }
    }

    fun randomStringOrNull(): String? = randomOrNull(::randomString)

    fun <T> randomListOfOrNull(size: Int = 20, randomGen: () -> T): List<T>? =
        randomOrNull {
            randomListOf {
                randomGen()
            }
        }
    fun <T> randomListOf(size: Int = 20, randomGen: () -> T): List<T> = List(size = size) { randomGen() }

    fun randomActorDto(
        id: String = randomString(),
        data: ActorDataDto = randomActorDataDto(),
        extraData: Map<String, Any> = emptyMap(),
    ): ActorDto =
        ActorDto(id, data).apply { this.extraData.putAll(extraData) }

    fun randomActorDataDto(
        handle: String = randomString(),
        name: String = randomString(),
        profileImage: String = randomString(),
        extraData: Map<String, Any> = emptyMap()
    ): ActorDataDto =
        ActorDataDto(handle, name, profileImage).withExtras(extraData)

    fun randomUpstreamActivityDto(
        actor: String = randomString(),
        objectProperty: String = randomString(),
        verb: String = randomString(),
        target: String? = randomStringOrNull(),
        time: String? = randomStringOrNull(),
        to: List<String>? = randomListOfOrNull { randomString() },
        foreignId: String? = randomStringOrNull(),
        extraData: Map<String, Any> = emptyMap()
    ): UpstreamActivityDto =
        UpstreamActivityDto(actor, objectProperty, verb, target, time, to, foreignId).withExtras(extraData)

    fun randomDownstreamActivityDto(
        id: String = randomString(),
        actor: String = randomString(),
        objectProperty: String = randomString(),
        verb: String = randomString(),
        target: String? = randomStringOrNull(),
        time: String? = randomStringOrNull(),
        to: List<String>? = randomListOfOrNull { randomString() },
        foreignId: String? = randomStringOrNull(),
        extraData: Map<String, Any> = emptyMap()
    ): DownstreamActivityDto =
        DownstreamActivityDto(id, actor, objectProperty, verb, target, time, to, foreignId).withExtras(extraData)

    fun randomUpstreamEnrichActivitySDto(
        actor: ActorDto = randomActorDto(),
        objectProperty: String = randomString(),
        verb: String = randomString(),
        target: String? = randomStringOrNull(),
        time: String? = randomStringOrNull(),
        to: List<String>? = randomListOfOrNull { randomString() },
        foreignId: String? = randomStringOrNull(),
        extraData: Map<String, Any> = emptyMap()
    ): UpstreamEnrichActivityDto =
        UpstreamEnrichActivityDto(actor, objectProperty, verb, target, time, to, foreignId).withExtras(extraData)

    fun randomDownstreamEnrichActivityDto(
        id: String = randomString(),
        actor: ActorDto = randomActorDto(),
        objectProperty: String = randomString(),
        verb: String = randomString(),
        target: String? = randomStringOrNull(),
        time: String? = randomStringOrNull(),
        to: List<String>? = randomListOfOrNull { randomString() },
        foreignId: String? = randomStringOrNull(),
        extraData: Map<String, Any> = emptyMap()
    ): DownstreamEnrichActivityDto =
        DownstreamEnrichActivityDto(id, actor, objectProperty, verb, target, time, to, foreignId).withExtras(extraData)

    private fun <T : CustomExtraDTOs> T.withExtras(extraData: Map<String, Any>) = apply { this.extraData.putAll(extraData) }
}
