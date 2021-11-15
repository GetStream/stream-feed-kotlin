package io.getstream.feed.client

data class FeedID(
    val slug: String,
    val userID: String,
)

class Actor(
    val id: String,
    val handle: String,
    val name: String,
    val profileImage: String,
    val extraData: Map<String, Any>
)

sealed class FeedActivity {
    abstract val id: String
    abstract val `object`: String
    abstract val verb: String
    abstract val to: List<FeedID>
    abstract val time: String
    abstract val foreignId: String?
    abstract val extraData: Map<String, Any>
}

data class Activity(
    override val id: String,
    val actor: String,
    override val `object`: String,
    override val verb: String,
    override val to: List<FeedID>,
    override val time: String,
    override val foreignId: String?,
    override val extraData: Map<String, Any>
) : FeedActivity()

data class EnrichActivity(
    override val id: String,
    val actor: Actor,
    override val `object`: String,
    override val verb: String,
    override val to: List<FeedID>,
    override val time: String,
    override val foreignId: String?,
    override val extraData: Map<String, Any>
) : FeedActivity()
