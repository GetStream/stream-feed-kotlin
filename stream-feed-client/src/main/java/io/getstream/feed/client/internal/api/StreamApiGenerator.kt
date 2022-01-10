package io.getstream.feed.client.internal.api

import com.moczul.ok2curl.CurlInterceptor
import io.getstream.feed.client.internal.api.adapters.FeedMoshiConverterFactory
import io.getstream.feed.client.internal.api.interceptors.ApiKeyInterceptor
import io.getstream.feed.client.internal.api.interceptors.SDKVersionInterceptor
import io.getstream.feed.client.internal.api.interceptors.TokenAuthInterceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit

/**
 * A class that provides all needed endpoints.
 *
 * @property apiKey Stream Feed API Key to be used on every requests.
 * @property userToken Token that authorizes requests.
 * @property flatFeedApi The Flat Feed endpoints.
 * @property notificationFeedApi The Notification Feed endpoints.
 * @property aggregatedFeedApi The Aggregated Feed endpoints.
 * @property activityApi The Activity endpoints.
 * @property collectionsApi The collections endpoints.
 * @property reactionApi The reactions endpoints.
 * @property userApi The user endpoints.
 */
internal class StreamApiGenerator(
    private val apiKey: String,
    private val userToken: String,
) {
    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("https://api.stream-io-api.com")
            .client(
                OkHttpClient.Builder()
                    .addInterceptor(ApiKeyInterceptor(apiKey))
                    .addInterceptor(SDKVersionInterceptor(getVersion()))
                    .addInterceptor(TokenAuthInterceptor { userToken })
                    .addInterceptor(HttpLoggingInterceptor(::println).setLevel(HttpLoggingInterceptor.Level.BODY))
                    .addInterceptor(CurlInterceptor(::println))
                    .build()
            )
            .addConverterFactory(FeedMoshiConverterFactory)
            .build()
    }

    /**
     * Provides version of the SDK. It is obtained from MetaData injected by Gradle Build Script.
     */
    private fun getVersion(): String = "stream-feed-kotlin-${this::class.java.`package`.implementationVersion}"

    val flatFeedApi: FlatFeedApi by lazy { retrofit.create(FlatFeedApi::class.java) }
    val notificationFeedApi: NotificationFeedApi by lazy { retrofit.create(NotificationFeedApi::class.java) }
    val aggregatedFeedApi: AggregatedFeedApi by lazy { retrofit.create(AggregatedFeedApi::class.java) }
    val activityApi: ActivityApi by lazy { retrofit.create(ActivityApi::class.java) }
    val collectionsApi: CollectionsApi by lazy { retrofit.create(CollectionsApi::class.java) }
    val reactionApi: ReactionApi by lazy { retrofit.create(ReactionApi::class.java) }
    val userApi: UserApi by lazy { retrofit.create(UserApi::class.java) }
}
