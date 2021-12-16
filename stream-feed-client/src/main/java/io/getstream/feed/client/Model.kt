package io.getstream.feed.client

data class FeedID(
    val slug: String,
    val userID: String,
)

data class User(
    val id: String,
    val data: Map<String, Any> = emptyMap(),
    val followersCount: Int,
    val followingCount: Int,
)

class Actor(
    val id: String,
    val data: Map<String, Any> = emptyMap(),
)

class Object(
    val id: String,
    val data: Map<String, Any> = emptyMap(),
)

class Target(
    val id: String,
    val data: Map<String, Any> = emptyMap(),
)

data class CollectionData(
    val id: String,
    val name: String,
    val foreignId: String? = null,
    val data: Map<String, Any> = emptyMap(),
)

sealed class FeedActivity {
    abstract val id: String
    abstract val verb: String
    abstract val to: List<FeedID>
    abstract val time: String
    abstract val foreignId: String?
    abstract val extraData: Map<String, Any>
}

data class Activity(
    override val id: String = "",
    val actor: String,
    val `object`: String,
    override val verb: String,
    val target: String? = null,
    override val to: List<FeedID> = emptyList(),
    override val time: String = "",
    override val foreignId: String? = null,
    override val extraData: Map<String, Any> = emptyMap()
) : FeedActivity()

data class EnrichActivity(
    override val id: String = "",
    val actor: Actor,
    val `object`: Object,
    override val verb: String,
    override val to: List<FeedID> = emptyList(),
    val target: Target? = null,
    override val time: String = "",
    override val foreignId: String? = null,
    override val extraData: Map<String, Any> = emptyMap()
) : FeedActivity()

data class FollowRelation(
    val sourceFeedID: FeedID,
    val targetFeedID: FeedID,
)

data class AggregatedActivitiesGroup(
    val id: String,
    val activities: List<FeedActivity>,
    val verb: String,
    val group: String,
    val actorCount: Int,
)

data class Reaction(
    val id: String = "",
    val kind: String,
    val activityId: String,
    val targetFeeds: List<FeedID> = emptyList(),
    val data: Map<String, Any> = emptyMap(),
    val targetFeedsExtraData: Map<String, Any> = emptyMap(),
)

data class NotificationActivitiesGroup(
    val id: String,
    val activities: List<FeedActivity>,
    val verb: String,
    val group: String,
    val actorCount: Int,
    val isRead: Boolean,
    val isSeen: Boolean,
)
