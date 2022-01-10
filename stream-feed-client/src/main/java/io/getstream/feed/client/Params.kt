package io.getstream.feed.client

/**
 * A class that defines parameters to follow a Feed.
 *
 * @property targetFeedID The Feed Id to be followed.
 * @property activityCopyLimit The number of activities should be copied from the target feed.
 */
class FollowParams {
    lateinit var targetFeedID: FeedID
    var activityCopyLimit: Int? = 100

    internal val isInitialized
        get() = this::targetFeedID.isInitialized
}

/**
 * A class that defines parameters to unfollow a Feed.
 *
 * @property targetFeedID The Feed Id to be unfollowed.
 * @property keepHistory Whether the activities from the unfollowed feed should be removed.
 */
class UnfollowParams {
    lateinit var targetFeedID: FeedID
    var keepHistory: Boolean? = null

    internal val isInitialized
        get() = this::targetFeedID.isInitialized
}

/**
 * A class that defines parameters to obtain Feed's followed.
 *
 * @property limit Amount of result per request.
 * @property offset Number of rows to skip before returning results.
 */
class FollowedParams {
    var limit: Int? = null
    var offset: Int? = null
}

/**
 * A class that defines parameters to obtain Feed's followers.
 *
 * @property limit Amount of result per request.
 * @property offset Number of rows to skip before returning results.
 */
class FollowersParams {
    var limit: Int? = null
    var offset: Int? = null
}

/**
 * A class that defines which activity need to be removed.
 */
sealed class RemoveActivityParams

/**
 * A class that defines which activity need to be removed by its id.
 *
 * @property activityId The id of the activity to be removed.
 */
data class RemoveActivityById(val activityId: String) : RemoveActivityParams()

/**
 * A class that defines which activity need to be removed by its foreignId.
 *
 * @property foreignId The foreignId of the activity to be removed.
 */
data class RemoveActivityByForeignId(val foreignId: String) : RemoveActivityParams()

/**
 * A class that defines parameters to obtain Activities.
 *
 * @property limit The number of Activities to retrieve.
 * @property offset The offset.
 * @property idGreaterThan Filter the feed on ids greater than or equal to the given value.
 * @property idGreaterThanOrEqual Filter the feed on ids greater than to the given value.
 * @property idSmallerThan Filter the feed on ids smaller than to the given value.
 * @property idSmallerThanOrEqual Filter the feed on ids smaller than or equal to the given value.
 * @property withOwnReactions Include reactions added by current user to all activities.
 * @property withReactionCounts Include reaction counts to activities.
 * @property enrich When using collections, you can request Stream to enrich activities to include them.
 */
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

    /**
     * Include recent reactions to activites.
     *
     * @param limit The number of recent reactions to retrieve.
     */
    fun withRecentReactions(limit: Int? = null) = apply {
        withRecentReactions = true
        recentReactionsLimit = limit
    }
}

/**
 * A class that defines parameters to find Activities.
 *
 * @property activitiesIds The ids of the Activities to retrieve.
 * @property foreignIds The foreignIds of the Activities to retrieve.
 * @property timestamps The timestamps of the Activities to retrieve.
 * @property withOwnReactions Include reactions added by current user to all activities.
 * @property withReactionCounts Include reaction counts to activities.
 * @property enrich When using collections, you can request Stream to enrich activities to include them.
 */
class FindActivitiesParams {
    var activitiesIds: List<String> = emptyList()
    var foreignIds: List<String> = emptyList()
    var timestamps: List<String> = emptyList()
    var withOwnReactions: Boolean = false
    var withReactionCounts: Boolean = false
    internal var withRecentReactions: Boolean = false
    internal var recentReactionsLimit: Int? = null
    var enrich: Boolean = false

    /**
     * Include recent reactions to activites.
     *
     * @param limit The number of recent reactions to retrieve.
     */
    fun withRecentReactions(limit: Int? = null) = apply {
        withRecentReactions = true
        recentReactionsLimit = limit
    }
}

/**
 * A class that defines parameters to find User.
 *
 * @property userId The id of the User to retrieve.
 * @property withFollowCount Include Followed/Followers counters.
 */
class FindUserParams {
    lateinit var userId: String
    var withFollowCount: Boolean = false

    internal val isInitialized: Boolean
        get() = this::userId.isInitialized
}

/**
 * A class that defines parameters to get or create User.
 *
 * @property userId The id of the User.
 * @property data The data related to the User.
 * @property getOrCreate When true, the user is created if it didn't exist.
 */
class UserParams {
    lateinit var userId: String
    var data: Map<String, Any> = emptyMap()
    var getOrCreate: Boolean = false

    internal val isInitialized: Boolean
        get() = this::userId.isInitialized
}

/**
 * A class that defines parameters to update a User.
 *
 * @property userId The id of the User.
 * @property data The data related to the User.
 */
class UpdateUserParams {
    lateinit var userId: String
    var data: Map<String, Any> = emptyMap()

    internal val isInitialized: Boolean
        get() = this::userId.isInitialized
}

/**
 * A class that defines parameters to update a Reaction.
 *
 * @property reactionId The id of the Reaction.
 * @property data The data related to the Reaction.
 * @property targetFeeds The list of feeds that should receive a copy of the reaction.
 */
class UpdateReactionParams {
    lateinit var reactionId: String
    var data: Map<String, Any> = emptyMap()
    var targetFeeds: List<FeedID> = emptyList()

    internal val isInitialized: Boolean
        get() = this::reactionId.isInitialized
}

/**
 * A class that defines parameters to update an Activity by id.
 *
 * @property activityId The id of the Activity.
 * @property set The data to be added to the activity.
 * @property unset The list of keys to be removed from the activity data.
 */
class UpdateActivityByIdParams : UpdateActivityParams() {
    lateinit var activityId: String
    override var set: Map<String, Any> = emptyMap()
    override var unset: List<String> = emptyList()
    internal val isInitialized
        get() = this::activityId.isInitialized
}

/**
 * A class that defines parameters to update an Activity by foreignId.
 *
 * @property foreignId The foreignId of the Activity.
 * @property time The time of the Activity.
 * @property set The data to be added to the activity.
 * @property unset The list of keys to be removed from the activity data.
 */
class UpdateActivityByForeignIdParams : UpdateActivityParams() {
    lateinit var foreignId: String
    lateinit var time: String
    override var set: Map<String, Any> = emptyMap()
    override var unset: List<String> = emptyList()
    internal val isInitialized
        get() = this::foreignId.isInitialized && this::time.isInitialized
}

/**
 * A class that defines parameters to update an Activity.
 *
 * @property set The data to be added to the activity.
 * @property unset The list of keys to be removed from the activity data.
 */
sealed class UpdateActivityParams {
    abstract var set: Map<String, Any>
    abstract var unset: List<String>
}

/**
 * A class that defines parameters to filter reactions.
 *
 * @property lookup The attribute to lookup.
 * @property kind The type of reaction.
 * @property limit The number of Activities to retrieve.
 * @property idGreaterThan Retrieve reactions created after the one with ID equal to the given value.
 * @property idGreaterThanOrEqual Retrieve reactions created after the one with ID equal to the given value (inclusive).
 * @property idSmallerThan Retrieve reactions created before the one with ID equal to the given value.
 * @property idSmallerThanOrEqual Retrieve reactions created before the one with ID equal to the given value (inclusive).
 */
class FilterReactionsParams {
    lateinit var lookup: Lookup
    var kind: String = ""
    var limit: Int = 10
    var idGreaterThan: String? = null
    var idGreaterThanOrEqual: String? = null
    var idSmallerThan: String? = null
    var idSmallerThanOrEqual: String? = null

    internal val isInitialized: Boolean
        get() = this::lookup.isInitialized

    /**
     * A class that defines the attribute to lookup.
     *
     * @property key The key of the attribute.
     * @property id The id value of the attribute.
     */
    sealed class Lookup(internal val key: String) {
        internal abstract val id: String
    }

    /**
     * A class that defines the attribute to lookup.
     *
     * @property id The id of the activity.
     * @property enrich When using collections, you can request Stream to enrich activities to include them.
     */
    data class ActivityLookup(override val id: String, val enrich: Boolean = false) : Lookup("activity_id")

    /**
     * A class that defines the attribute to lookup.
     *
     * @property id The id of the reaction.
     */
    data class ReactionLookup(override val id: String) : Lookup("reaction_id")

    /**
     * A class that defines the attribute to lookup.
     *
     * @property id The id of the user.
     */
    data class UserLookup(override val id: String) : Lookup("user_id")
}
