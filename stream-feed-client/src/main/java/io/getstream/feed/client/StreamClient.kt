package io.getstream.feed.client

import io.getstream.feed.client.internal.api.StreamApiGenerator

/**
 * A class that provides different clients to interact with Stream Server.
 *
 * @property streamApiGenerator Used to generate endpoints.
 * @property activityClient The client to work with activities.
 * @property userClient The client to work with users.
 */
public class StreamClient private constructor(private val streamApiGenerator: StreamApiGenerator) {

    val activityClient: ActivityClient by lazy { ActivityClient(streamApiGenerator.activityApi) }
    val userClient: UserClient by lazy { UserClient(streamApiGenerator.userApi) }

    /**
     * Create a [FlatFeed]
     *
     * @param feedID The feed Id of the [FlatFeed]
     * @return [FlatFeed]
     */
    fun flatFeed(feedID: FeedID): FlatFeed = FlatFeed(streamApiGenerator.flatFeedApi, feedID = feedID)

    /**
     * Create a [NotificationFeed]
     *
     * @param feedID The feed Id of the [NotificationFeed]
     * @return [NotificationFeed]
     */
    fun notificationFeed(feedID: FeedID): NotificationFeed = NotificationFeed(streamApiGenerator.notificationFeedApi, feedID = feedID)

    /**
     * Create a [AggregatedFeed]
     *
     * @param feedID The feed Id of the [AggregatedFeed]
     * @return [AggregatedFeed]
     */
    fun aggregatedFeed(feedID: FeedID): AggregatedFeed = AggregatedFeed(streamApiGenerator.aggregatedFeedApi, feedID = feedID)

    /**
     * Create a [CollectionsClient]
     *
     * @param userID The id of the user to which the collections belong.
     * @return [CollectionsClient]
     */
    fun collectionsClient(userID: String): CollectionsClient = CollectionsClient(streamApiGenerator.collectionsApi, userID)

    /**
     * Create a [ReactionsClient]
     *
     * @param userID The id of the user to which the reactions belong.
     * @return [ReactionsClient]
     */
    fun reactionClient(userID: String): ReactionsClient = ReactionsClient(streamApiGenerator.reactionApi, userID)

    companion object {

        /**
         * Create a [StreamClient]
         *
         * @param apiKey The API Key used to communicated with the server.
         * @param userToken The user token to be used to authenticate with he server.
         * @return [StreamClient]
         */
        @JvmStatic
        fun connect(apiKey: String, userToken: String): StreamClient =
            StreamClient(StreamApiGenerator(apiKey, userToken))
    }
}
