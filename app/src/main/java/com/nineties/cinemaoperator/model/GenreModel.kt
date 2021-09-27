package com.nineties.cinemaoperator.model

import com.google.gson.annotations.SerializedName

data class GenreModel(
    @SerializedName("id") val genreID: Int,
    @SerializedName("name") val genreName: String,
)