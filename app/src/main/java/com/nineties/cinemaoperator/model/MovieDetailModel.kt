package com.nineties.cinemaoperator.model

import com.google.gson.annotations.SerializedName

data class MovieDetailModel(
    @SerializedName("id") val movieID: Int = 0,
    @SerializedName("original_title") val movieTitle: String = "Sample",
    @SerializedName("overview") val movieSynopsis: String = "Sample Synopsis",
    @SerializedName("genres") val movieGenres: List<GenreModel> = emptyList(),
    @SerializedName("spoken_languages") val movieLanguages: List<LanguageModel> = emptyList(),
    @SerializedName("runtime") val movieDuration: Int = 0, // In Minutes
    @SerializedName("backdrop_path") var movieBackground: String = "",
)