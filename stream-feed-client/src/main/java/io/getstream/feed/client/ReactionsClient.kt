package io.getstream.feed.client

import io.getstream.feed.client.internal.api.ReactionApi

class ReactionsClient internal constructor(
    private val reactionApi: ReactionApi,
    private val userId: String,
)
