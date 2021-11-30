package io.getstream.feed.client.internal.api

import com.moczul.ok2curl.CurlInterceptor
import io.getstream.feed.client.internal.api.adapters.FeedMoshiConverterFactory
import io.getstream.feed.client.internal.api.interceptors.ApiKeyInterceptor
import io.getstream.feed.client.internal.api.interceptors.SDKVersionInterceptor
import io.getstream.feed.client.internal.api.interceptors.TokenAuthInterceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit

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

    private fun getVersion(): String = "stream-feed-kotlin-${this::class.java.`package`.implementationVersion}"

    val flatFeedApi: FlatFeedApi by lazy { retrofit.create(FlatFeedApi::class.java) }
    val notificationFeedApi: NotificationFeedApi by lazy { retrofit.create(NotificationFeedApi::class.java) }
    val aggregatedFeedApi: AggregatedFeedApi by lazy { retrofit.create(AggregatedFeedApi::class.java) }
    val activityApi: ActivityApi by lazy { retrofit.create(ActivityApi::class.java) }
}
