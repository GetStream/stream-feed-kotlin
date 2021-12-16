package io.getstream.feed.client

import io.getstream.feed.client.internal.api.StreamApiGenerator

public class StreamClient private constructor(private val streamApiGenerator: StreamApiGenerator) {

    val activityClient: ActivityClient by lazy { ActivityClient(streamApiGenerator.activityApi) }
    val userClient: UserClient by lazy { UserClient(streamApiGenerator.userApi) }
    fun flatFeed(feedID: FeedID): FlatFeed = FlatFeed(streamApiGenerator.flatFeedApi, feedID = feedID)
    fun notificationFeed(feedID: FeedID): NotificationFeed = NotificationFeed(streamApiGenerator.notificationFeedApi, feedID = feedID)
    fun aggregatedFeed(feedID: FeedID): AggregatedFeed = AggregatedFeed(streamApiGenerator.aggregatedFeedApi, feedID = feedID)
    fun collectionsClient(userID: String): CollectionsClient = CollectionsClient(streamApiGenerator.collectionsApi, userID)
    fun reactionClient(userID: String): ReactionsClient = ReactionsClient(streamApiGenerator.reactionApi, userID)

    companion object {

        @JvmStatic
        fun connect(apiKey: String, userToken: String): StreamClient =
            StreamClient(StreamApiGenerator(apiKey, userToken))
    }
}
