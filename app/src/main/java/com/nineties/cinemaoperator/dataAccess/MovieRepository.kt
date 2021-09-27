package com.nineties.cinemaoperator.dataAccess

import com.nineties.cinemaoperator.dataAccess.apiAccess.APIResponse
import com.nineties.cinemaoperator.dataAccess.apiAccess.MovieData
import com.nineties.cinemaoperator.dataAccess.localAccess.APIConstant
import com.nineties.cinemaoperator.model.MovieDetailModel
import com.nineties.cinemaoperator.model.MovieListModel

class MovieRepository(private val dataAccess: MovieData = MovieData()) {
    suspend fun listMovies(sortingAlgo: String, page: Int): APIResponse<MovieListModel> {
        return dataAccess.queryListing(sortingAlgo = sortingAlgo, page = page)
    }

    suspend fun getMovieDetail(movieID: Int): APIResponse<MovieDetailModel> {
        return dataAccess.queryDetail(movieID = movieID)
    }

    fun getImageURL(size: String, value: String?): String {
        if (value.isNullOrEmpty())
            return ""

        val imageURL: String = APIConstant.IMAGE_URL + APIConstant.IMAGE
        return imageURL.replace("{imageSize}", size).replace("{imageURL}", value)
    }

    fun getBookingURL(): String {
        return APIConstant.BOOKING_URL
    }
}