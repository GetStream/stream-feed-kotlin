package io.getstream.feed.client.internal

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import io.getstream.feed.client.EmptyParamError
import io.getstream.feed.client.FeedActivity
import io.getstream.feed.client.Mother.randomActivity
import io.getstream.feed.client.Mother.randomEnrichActivity
import io.getstream.feed.client.Mother.randomFeedActivity
import io.getstream.feed.client.Mother.randomListOfActivity
import io.getstream.feed.client.Mother.randomListOfEnrichActivity
import io.getstream.feed.client.Mother.randomListOfFeedActivity
import io.getstream.feed.client.StreamError
import io.getstream.feed.client.internal.GetActivitiesParamsValidatorTest.Companion.getActivitiesParamsArgs
import org.amshove.kluent.`should be equal to`
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource

class ListActivitiesValidatorTest {

    /**
     * Use [getActivitiesParamsArgs] as arguments
     */
    @ParameterizedTest
    @MethodSource("getActivitiesParamsArgs")
    fun `Should return either the same list of FeedActivity or an StreamError`(
        listActivities: List<FeedActivity>,
        expectedResult: Either<StreamError, List<FeedActivity>>
    ) {
        listActivities.validate() `should be equal to` expectedResult
    }

    companion object {

        @JvmStatic
        fun getActivitiesParamsArgs() = validGetActivitiesParamsArgs() + invalidGetActivitiesParamsArgs()

        @JvmStatic
        private fun validGetActivitiesParamsArgs() = listOf(
            listOf(randomFeedActivity()).let { Arguments.of(it, it.right()) },
            listOf(randomActivity()).let { Arguments.of(it, it.right()) },
            listOf(randomEnrichActivity()).let { Arguments.of(it, it.right()) },
            randomListOfFeedActivity().let { Arguments.of(it, it.right()) },
            randomListOfActivity().let { Arguments.of(it, it.right()) },
            randomListOfEnrichActivity().let { Arguments.of(it, it.right()) },
        )

        @JvmStatic
        private fun invalidGetActivitiesParamsArgs() = listOf(
            Arguments.of(emptyList<FeedActivity>(), EmptyParamError("The list of activities can't be empty").left()),
        )
    }
}
