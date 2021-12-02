package io.getstream.feed.client

class FollowParams {
    lateinit var targetFeedID: FeedID
    var activityCopyLimit: Int? = 100

    internal val isInitialized
        get() = this::targetFeedID.isInitialized
}
class UnfollowParams {
    lateinit var targetFeedID: FeedID
    var keepHistory: Boolean? = null

    internal val isInitialized
        get() = this::targetFeedID.isInitialized
}

class FollowedParams {
    var limit: Int? = null
    var offset: Int? = null
}

class FollowersParams {
    var limit: Int? = null
    var offset: Int? = null
}

sealed class RemoveActivityParams
data class RemoveActivityById(val activityId: String) : RemoveActivityParams()
data class RemoveActivityByForeignId(val foreignId: String) : RemoveActivityParams()

class GetActivitiesParams {
    var limit: Int = 25
    var offset: Int? = null
    var idGreaterThan: String? = null
    var idGreaterThanOrEqual: String? = null
    var idSmallerThan: String? = null
    var idSmallerThanOrEqual: String? = null
    var withOwnReactions: Boolean = false
    var withReactionCounts: Boolean = false
    internal var withRecentReactions: Boolean = false
    internal var recentReactionsLimit: Int? = null
    var enrich: Boolean = false

    fun withRecentReactions(limit: Int? = null) = apply {
        withRecentReactions = true
        recentReactionsLimit = limit
    }
}

class FindActivitiesParams {
    var activitiesIds: List<String> = emptyList()
    var foreignIds: List<String> = emptyList()
    var timestamps: List<String> = emptyList()
    var withOwnReactions: Boolean = false
    var withReactionCounts: Boolean = false
    internal var withRecentReactions: Boolean = false
    internal var recentReactionsLimit: Int? = null
    var enrich: Boolean = false

    fun withRecentReactions(limit: Int? = null) = apply {
        withRecentReactions = true
        recentReactionsLimit = limit
    }
}

class UpdateActivityByIdParams : UpdateActivityParams() {
    lateinit var activityId: String
    override var set: Map<String, Any> = emptyMap()
    override var unset: List<String> = emptyList()
    internal val isInitialized
        get() = this::activityId.isInitialized
}

class UpdateActivityByForeignIdParams : UpdateActivityParams() {
    lateinit var foreignId: String
    lateinit var time: String
    override var set: Map<String, Any> = emptyMap()
    override var unset: List<String> = emptyList()
    internal val isInitialized
        get() = this::foreignId.isInitialized && this::time.isInitialized
}

sealed class UpdateActivityParams {
    abstract var set: Map<String, Any>
    abstract var unset: List<String>
}
