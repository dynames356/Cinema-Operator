package com.nineties.cinemaoperator

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.nineties.cinemaoperator.model.MovieDetailModel
import com.nineties.cinemaoperator.model.MovieOverviewModel
import com.nineties.cinemaoperator.model.enum.SortingParam
import com.nineties.cinemaoperator.model.enum.SortingValue
import com.nineties.cinemaoperator.useCase.MovieUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import kotlin.system.measureTimeMillis

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {

    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.nineties.cinemaoperator", appContext.packageName)
    }

    @Test
    fun getMoviesAPICall() = runBlocking {
        var outputMovies: List<MovieOverviewModel>
        var outputMessage: String

        val totalExecutionTime = measureTimeMillis {
            val useCase = MovieUseCase()
            val output = useCase.listMovies(Pair(SortingParam.release_date, SortingValue.desc), 1)

            outputMovies = output.first
            outputMessage = output.second

            assertTrue(outputMessage.isEmpty() && outputMovies.count() > 0)
        }

        print("Total Execution Time: $totalExecutionTime")
    }

    @Test
    fun getMovieAPICall() = runBlocking {
        var outputMovie: MovieDetailModel
        var outputMessage: String

        val totalExecutionTime = measureTimeMillis {
            val useCase = MovieUseCase()
            val output = useCase.provideMovieDetail(328111)

            outputMovie = output.first
            outputMessage = output.second

            assertTrue(outputMessage.isEmpty() && outputMovie.movieTitle.isNotEmpty())
        }

        print("Total Execution Time: $totalExecutionTime")
    }
}