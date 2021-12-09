package io.getstream.feed.client

import io.getstream.feed.client.internal.api.models.ActivitiesRequest
import io.getstream.feed.client.internal.api.models.DataDto
import io.getstream.feed.client.internal.api.models.DownstreamActivityDto
import io.getstream.feed.client.internal.api.models.UpdateActivitiesRequest
import io.getstream.feed.client.internal.api.models.UpdateActivityByForeignIdRequest
import io.getstream.feed.client.internal.api.models.UpdateActivityByIdRequest
import io.getstream.feed.client.internal.api.models.UpdateActivityRequest
import io.getstream.feed.client.internal.api.models.UpstreamActivityDto
import kotlin.random.Random

internal object Mother {
    private val random = Random(System.currentTimeMillis())
    private val charPool: CharArray = (('a'..'z') + ('A'..'Z') + ('0'..'9')).toCharArray()
    fun positiveRandomInt(maxInt: Int = Int.MAX_VALUE): Int =
        random.nextInt(maxInt) + 1
    fun negativeRandomInt(minInt: Int = Int.MIN_VALUE): Int =
        random.nextInt(minInt, 0)
    fun randomString(size: Int = 20): String = buildString(capacity = size) {
        repeat(size) {
            append(charPool.random())
        }
    }

    tailrec fun <T> randomDifferentThan(value: T, randomGen: () -> T): T =
        randomGen().takeUnless { it == value } ?: randomDifferentThan(value, randomGen)

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
            randomListOf(size) {
                randomGen()
            }
        }
    fun <T> randomListOf(size: Int = positiveRandomInt(20), randomGen: () -> T): List<T> = List(size = size) { randomGen() }

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
        actor: DataDto = randomDataDto(),
        objectProperty: DataDto = randomDataDto(),
        verb: String = randomString(),
        target: DataDto? = randomOrNull { randomDataDto() },
        time: String? = randomStringOrNull(),
        to: List<String>? = randomListOfOrNull { randomString() },
        foreignId: String? = randomStringOrNull(),
        extraData: MutableMap<String, Any> = mutableMapOf()
    ): DownstreamActivityDto =
        DownstreamActivityDto(id, actor, objectProperty, verb, target, time, to, foreignId, extraData)

    fun randomFeedID(
        slug: String = randomString(),
        userId: String = randomString(),
    ): FeedID =
        FeedID(slug, userId)

    fun randomActor(
        id: String = randomString(),
        data: Map<String, Any> = emptyMap()
    ): Actor =
        Actor(id, data)

    fun randomObject(
        id: String = randomString(),
        data: Map<String, Any> = emptyMap()
    ): Object =
        Object(id, data)

    fun randomTarget(
        id: String = randomString(),
        data: Map<String, Any> = emptyMap()
    ): Target =
        Target(id, data)

    fun randomActivity(
        id: String = randomString(),
        actor: String = randomString(),
        `object`: String = randomString(),
        verb: String = randomString(),
        target: String? = randomStringOrNull(),
        to: List<FeedID> = emptyList(),
        time: String = randomString(),
        foreignId: String? = randomOrNull { randomString() },
        extraData: Map<String, Any> = emptyMap(),
    ): Activity =
        Activity(id, actor, `object`, verb, target, to, time, foreignId, extraData)

    fun randomEnrichActivity(
        id: String = randomString(),
        actor: Actor = randomActor(),
        `object`: Object = randomObject(),
        verb: String = randomString(),
        to: List<FeedID> = emptyList(),
        target: Target? = randomOrNull { randomTarget() },
        time: String = randomString(),
        foreignId: String? = randomOrNull { randomString() },
        extraData: Map<String, Any> = emptyMap(),
    ): EnrichActivity =
        EnrichActivity(id, actor, `object`, verb, to, target, time, foreignId, extraData)

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

    fun randomReaction(
        id: String = randomString(),
        kind: String = randomString(),
        activityId: String = randomString(),
        targetFeeds: List<FeedID> = randomListOf { randomFeedID() },
        data: Map<String, Any> = emptyMap(),
        targetFeedsExtraData: Map<String, Any> = emptyMap(),
    ): Reaction =
        Reaction(id, kind, activityId, targetFeeds, data, targetFeedsExtraData)

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

    fun randomUserLookup(
        id: String = randomString()
    ): FilterReactionsParams.UserLookup =
        FilterReactionsParams.UserLookup(id)

    fun randomActivityLookup(
        id: String = randomString(),
        enrich: Boolean = randomBoolean(),
    ): FilterReactionsParams.ActivityLookup =
        FilterReactionsParams.ActivityLookup(id, enrich)

    fun randomReactionLookup(
        id: String = randomString()
    ): FilterReactionsParams.ReactionLookup =
        FilterReactionsParams.ReactionLookup(id)

    fun randomLookup(): FilterReactionsParams.Lookup = oneOf(
        ::randomUserLookup,
        ::randomActivityLookup,
        ::randomReactionLookup,
    )

    fun createGetActivitiesParams(builder: GetActivitiesParams.() -> Unit = {}): GetActivitiesParams =
        GetActivitiesParams().apply(builder)

    fun createFilterReactionsParams(
        lookup: FilterReactionsParams.Lookup = randomLookup(),
        builder: FilterReactionsParams.() -> Unit = {}
    ): FilterReactionsParams = FilterReactionsParams().apply {
        this.lookup = lookup
        builder()
    }
}
