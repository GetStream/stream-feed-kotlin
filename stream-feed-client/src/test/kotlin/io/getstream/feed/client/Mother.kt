package io.getstream.feed.client

internal object Mother {
    private val charPool: CharArray = (('a'..'z') + ('A'..'Z') + ('0'..'9')).toCharArray()

    fun randomString(size: Int = 20): String = buildString(capacity = size) {
        repeat(size) {
            append(charPool.random())
        }
    }
}
