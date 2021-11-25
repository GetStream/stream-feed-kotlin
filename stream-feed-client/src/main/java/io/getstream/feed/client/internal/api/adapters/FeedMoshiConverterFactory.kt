package io.getstream.feed.client.internal.api.adapters

import com.squareup.moshi.Moshi
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.lang.reflect.Type

object FeedMoshiConverterFactory : Converter.Factory() {
    private val moshiConverterFactory: MoshiConverterFactory by lazy { MoshiConverterFactory.create(moshi) }
    internal val moshi: Moshi by lazy {
        Moshi.Builder()
            .add(CustomExtraDTOsAdapterFactory)
            .add(DataDtoAdapterFactory)
            .add(ActivityAdapterFactory)
            .add(UpdateActivityBodyAdapterFactory)
            .build()
    }

    override fun responseBodyConverter(
        type: Type,
        annotations: Array<out Annotation>,
        retrofit: Retrofit,
    ): Converter<ResponseBody, *>? =
        UrlQueryFactory.responseBodyConverter(type, annotations, retrofit)
            ?: moshiConverterFactory.responseBodyConverter(type, annotations, retrofit)

    override fun requestBodyConverter(
        type: Type,
        parameterAnnotations: Array<out Annotation>,
        methodAnnotations: Array<out Annotation>,
        retrofit: Retrofit,
    ): Converter<*, RequestBody>? =
        UrlQueryFactory.requestBodyConverter(type, parameterAnnotations, methodAnnotations, retrofit)
            ?: moshiConverterFactory.requestBodyConverter(type, parameterAnnotations, methodAnnotations, retrofit)

    override fun stringConverter(
        type: Type,
        annotations: Array<out Annotation>,
        retrofit: Retrofit,
    ): Converter<*, String>? =
        UrlQueryFactory.stringConverter(type, annotations, retrofit)
            ?: moshiConverterFactory.stringConverter(type, annotations, retrofit)
}
