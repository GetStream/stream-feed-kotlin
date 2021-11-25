package io.getstream.feed.client

import io.getstream.feed.client.internal.api.models.ActivitiesRequest
import io.getstream.feed.client.internal.api.models.ActorDataDto
import io.getstream.feed.client.internal.api.models.ActorDto
import io.getstream.feed.client.internal.api.models.DataDto
import io.getstream.feed.client.internal.api.models.DownstreamActivityDto
import io.getstream.feed.client.internal.api.models.DownstreamEnrichActivityDto
import io.getstream.feed.client.internal.api.models.UpdateActivitiesRequest
import io.getstream.feed.client.internal.api.models.UpdateActivityByForeignIdRequest
import io.getstream.feed.client.internal.api.models.UpdateActivityByIdRequest
import io.getstream.feed.client.internal.api.models.UpdateActivityRequest
import io.getstream.feed.client.internal.api.models.UpstreamActivityDto
import kotlin.random.Random

internal object Mother {
    private val random = Random(System.currentTimeMillis())
    private val charPool: CharArray = (('a'..'z') + ('A'..'Z') + ('0'..'9')).toCharArray()
    fun positiveRandomInt(maxInt: Int = Int.MAX_VALUE - 2): Int =
        random.nextInt(maxInt + 2) + 1
    fun negativeRandomInt(minInt: Int = Int.MIN_VALUE): Int =
        random.nextInt(minInt, 0)
    fun randomString(size: Int = 20): String = buildString(capacity = size) {
        repeat(size) {
            append(charPool.random())
        }
    }

    fun randomBoolean(): Boolean = random.nextBoolean()

    fun <T> oneOf(vararg randomGen: () -> T): T = randomGen.random().invoke()

    fun <T> randomOrNull(randomGen: () -> T): T? = random.nextBoolean().let {
        when (it) {
            true -> randomGen()
            false -> null
        }
    }

    fun randomStringOrNull(): String? = randomOrNull(::randomString)

    fun <T> randomListOfOrNull(size: Int = positiveRandomInt(20), randomGen: () -> T): List<T>? =
        randomOrNull {
            randomListOf {
                randomGen()
            }
        }
    fun <T> randomListOf(size: Int = positiveRandomInt(20), randomGen: () -> T): List<T> = List(size = size) { randomGen() }

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
    ): ActorDataDto =
        ActorDataDto(handle, name, profileImage)

    fun randomUpstreamActivityDto(
        actor: String = randomString(),
        objectProperty: String = randomString(),
        verb: String = randomString(),
        target: String? = randomStringOrNull(),
        time: String? = randomStringOrNull(),
        to: List<String>? = randomListOfOrNull { randomString() },
        foreignId: String? = randomStringOrNull(),
        extraData: MutableMap<String, Any> = mutableMapOf()
    ): UpstreamActivityDto =
        UpstreamActivityDto(actor, objectProperty, verb, target, time, to, foreignId, extraData)

    fun randomDownstreamActivityDto(
        id: String = randomString(),
        actor: String = randomString(),
        objectProperty: String = randomString(),
        verb: String = randomString(),
        target: String? = randomStringOrNull(),
        time: String? = randomStringOrNull(),
        to: List<String>? = randomListOfOrNull { randomString() },
        foreignId: String? = randomStringOrNull(),
        extraData: MutableMap<String, Any> = mutableMapOf()
    ): DownstreamActivityDto =
        DownstreamActivityDto(id, actor, objectProperty, verb, target, time, to, foreignId, extraData)

    fun randomDownstreamEnrichActivityDto(
        id: String = randomString(),
        actor: ActorDto = randomActorDto(),
        objectProperty: String = randomString(),
        verb: String = randomString(),
        target: String? = randomStringOrNull(),
        time: String? = randomStringOrNull(),
        to: List<String>? = randomListOfOrNull { randomString() },
        foreignId: String? = randomStringOrNull(),
        extraData: MutableMap<String, Any> = mutableMapOf()
    ): DownstreamEnrichActivityDto =
        DownstreamEnrichActivityDto(id, actor, objectProperty, verb, target, time, to, foreignId, extraData)

    fun randomFeedID(
        slug: String = randomString(),
        userId: String = randomString(),
    ): FeedID =
        FeedID(slug, userId)

    fun randomActor(
        id: String = randomString(),
        handle: String = randomString(),
        name: String = randomString(),
        profileImage: String = randomString(),
        extraData: Map<String, Any> = emptyMap()
    ): Actor =
        Actor(id, handle, name, profileImage, extraData)

    fun randomActivity(
        id: String = randomString(),
        actor: String = randomString(),
        `object`: String = randomString(),
        verb: String = randomString(),
        to: List<FeedID> = emptyList(),
        time: String = randomString(),
        foreignId: String? = randomOrNull { randomString() },
        extraData: Map<String, Any> = emptyMap(),
    ): Activity =
        Activity(id, actor, `object`, verb, to, time, foreignId, extraData)

    fun randomEnrichActivity(
        id: String = randomString(),
        actor: Actor = randomActor(),
        `object`: String = randomString(),
        verb: String = randomString(),
        to: List<FeedID> = emptyList(),
        time: String = randomString(),
        foreignId: String? = randomOrNull { randomString() },
        extraData: Map<String, Any> = emptyMap(),
    ): EnrichActivity =
        EnrichActivity(id, actor, `object`, verb, to, time, foreignId, extraData)

    fun randomFeedActivity(): FeedActivity =
        oneOf(::randomActivity, ::randomEnrichActivity)

    fun randomListOfFeedActivity(): List<FeedActivity> = randomListOf { randomFeedActivity() }
    fun randomListOfActivity(): List<Activity> = randomListOf { randomActivity() }
    fun randomListOfEnrichActivity(): List<EnrichActivity> = randomListOf { randomEnrichActivity() }

    fun randomUpdateActivityByIdRequest(
        id: String = randomString(),
        set: Map<String, Any> = emptyMap(),
        unset: List<String> = randomListOf { randomString() },
    ): UpdateActivityByIdRequest =
        UpdateActivityByIdRequest(id, set, unset)

    fun randomUpdateActivityByForeignIdRequest(
        foreignId: String = randomString(),
        time: String = randomString(),
        set: Map<String, Any> = emptyMap(),
        unset: List<String> = randomListOf { randomString() },
    ): UpdateActivityByForeignIdRequest =
        UpdateActivityByForeignIdRequest(foreignId, time, set, unset)

    fun randomUpdateActivityRequest(
        set: Map<String, Any> = emptyMap(),
        unset: List<String> = randomListOf { randomString() },
    ): UpdateActivityRequest = oneOf(
        {
            randomUpdateActivityByIdRequest(
                set = set,
                unset = unset,
            )
        },
        {
            randomUpdateActivityByForeignIdRequest(
                set = set,
                unset = unset,
            )
        }
    )

    fun randomDataDto(
        id: String = randomString(),
        data: Map<String, Any> = emptyMap(),
    ): DataDto = DataDto(id, data)

    fun randomUpdateActivitiesRequest(
        updates: List<UpdateActivityRequest> = randomListOf { randomUpdateActivityRequest() }
    ): UpdateActivitiesRequest =
        UpdateActivitiesRequest(updates)

    fun randomActivitiesRequest(
        activities: List<UpstreamActivityDto> = randomListOf { randomUpstreamActivityDto() }
    ): ActivitiesRequest =
        ActivitiesRequest(activities)

    fun createGetActivitiesParams(builder: GetActivitiesParams.() -> Unit = {}): GetActivitiesParams =
        GetActivitiesParams().apply(builder)
}
