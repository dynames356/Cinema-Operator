package com.nineties.cinemaoperator.dataAccess.apiAccess

import com.nineties.cinemaoperator.dataAccess.localAccess.APIConstant
import com.nineties.cinemaoperator.model.MovieDetailModel
import com.nineties.cinemaoperator.model.MovieListModel
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.QueryMap

class MovieData {
    private var dataAccess: MovieAPI

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl(APIConstant.MAIN_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        dataAccess = retrofit.create(MovieAPI::class.java)
    }

    suspend fun queryListing(sortingAlgo: String, page: Int): APIResponse<MovieListModel> {
        val options: MutableMap<String, Any> = mutableMapOf()
        options.put("api_key", APIConstant.API_KEY)
        options.put("sort_by", sortingAlgo)
        options.put("page", page)

        return APIResponse.create(response = dataAccess.queryListing(options = options))
    }

    suspend fun queryDetail(movieID: Int): APIResponse<MovieDetailModel> {
        val options: MutableMap<String, Any> = mutableMapOf()
        options.put("api_key", APIConstant.API_KEY)

        return APIResponse.create(response = dataAccess.queryDetail(movieID = movieID, options = options))
    }

    interface MovieAPI {
        @GET(APIConstant.LISTING)
        @JvmSuppressWildcards
        suspend fun queryListing(@QueryMap(encoded=true) options: Map<String, Any>): Response<MovieListModel>

        @GET(APIConstant.DETAIL)
        @JvmSuppressWildcards
        suspend fun queryDetail(@Path("movieID") movieID: Int, @QueryMap options: Map<String, Any>): Response<MovieDetailModel>
    }
}