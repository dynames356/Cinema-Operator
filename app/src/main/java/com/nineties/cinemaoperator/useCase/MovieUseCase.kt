package com.nineties.cinemaoperator.useCase

import com.nineties.cinemaoperator.dataAccess.apiAccess.ErrorResponse
import com.nineties.cinemaoperator.dataAccess.apiAccess.SuccessEmptyResponse
import com.nineties.cinemaoperator.dataAccess.apiAccess.SuccessResponse
import com.nineties.cinemaoperator.dataAccess.MovieRepository
import com.nineties.cinemaoperator.model.enum.ImageSize
import com.nineties.cinemaoperator.model.enum.SortingParam
import com.nineties.cinemaoperator.model.enum.SortingValue
import com.nineties.cinemaoperator.model.MovieDetailModel
import com.nineties.cinemaoperator.model.MovieOverviewModel
import java.util.concurrent.CancellationException
import kotlin.text.StringBuilder

class MovieUseCase(
    private val repository: MovieRepository = MovieRepository(),
    private val posterSize: ImageSize = ImageSize.w342,
    private val backgroundSize: ImageSize = ImageSize.w780
) {
    suspend fun listMovies(sortingList: Pair<SortingParam, SortingValue>, page: Int): Pair<List<MovieOverviewModel>, String> {
        val sortingAlgos = StringBuilder("")
        sortingAlgos.append(sortingList.first).append(".").append(sortingList.second)

        var movies = emptyList<MovieOverviewModel>()
        var message = ""

        try {
            val dataResponse =
                repository.listMovies(sortingAlgo = sortingAlgos.toString(), page = page)
            when (dataResponse) {
                is SuccessResponse -> {
                    movies = dataResponse.data.movieList
                }
                is ErrorResponse -> {
                    message = dataResponse.errorMessage
                }
                is SuccessEmptyResponse -> {
                    movies = emptyList()
                }
            }
        } catch (ce: CancellationException) {
            throw ce
        } catch (ex: Exception) {
            ex.printStackTrace()
            message = ex.localizedMessage?: "Unknown Error"
        }

        for (movie in movies) {
            movie.moviePoster = repository.getImageURL(size = posterSize.name, value = movie.moviePoster)
        }

        return Pair(movies, message)
    }

    suspend fun provideMovieDetail(movieID: Int): Pair<MovieDetailModel, String> {
        var movie = MovieDetailModel()
        var message = ""

        try {
        val dataResponse = repository.getMovieDetail(movieID = movieID)
        when (dataResponse) {
            is SuccessResponse -> {
                movie = dataResponse.data
            }
            is ErrorResponse -> {
                message = dataResponse.errorMessage
            }
            is SuccessEmptyResponse -> {
                movie = MovieDetailModel()
            }
        }
        } catch (ce: CancellationException) {
            throw ce
        } catch (ex: Exception) {
            ex.printStackTrace()
            message = ex.localizedMessage?: "Unknown Error"
        }

        movie.movieBackground = repository.getImageURL(size = backgroundSize.name, value = movie.movieBackground)

        return Pair(movie, message)
    }

    fun retrieveBookingURL(): String {
        return repository.getBookingURL()
    }
}