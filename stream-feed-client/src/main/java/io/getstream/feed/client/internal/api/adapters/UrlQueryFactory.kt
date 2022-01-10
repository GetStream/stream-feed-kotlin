package io.getstream.feed.client.internal.api.adapters

import io.getstream.feed.client.internal.api.models.CommaSeparatedQueryParams
import retrofit2.Converter
import retrofit2.Retrofit
import java.lang.reflect.Type

/**
 * A [Converter.Factory] which provides [Converter] for [CommaSeparatedQueryParams].
 */
internal object UrlQueryFactory : Converter.Factory() {

    override fun stringConverter(
        type: Type,
        annotations: Array<out Annotation>,
        retrofit: Retrofit,
    ): Converter<*, String>? = when (getRawType(type)) {
        CommaSeparatedQueryParams::class.java -> CommaSeparatedQueryParamsConverter
        else -> super.stringConverter(type, annotations, retrofit)
    }
}

/**
 * A [Converter] which converts [CommaSeparatedQueryParams] to [String] to be used on the URL.
 */
internal object CommaSeparatedQueryParamsConverter : Converter<CommaSeparatedQueryParams<*>, String> {
    override fun convert(value: CommaSeparatedQueryParams<*>): String? =
        value.params
            .map { it.toString() }
            .filterNot { it.isBlank() }
            .takeUnless { it.isEmpty() }
            ?.joinToString(separator = ",")
}
