package com.nineties.cinemaoperator.viewModel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nineties.cinemaoperator.model.GenreModel
import com.nineties.cinemaoperator.model.LanguageModel
import com.nineties.cinemaoperator.model.MovieDetailModel
import com.nineties.cinemaoperator.useCase.MovieUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.StringBuilder

class MovieDetailViewModel(movieID: Int): ViewModel() {
    private val useCase: MovieUseCase = MovieUseCase()

    val movieDetailState = mutableStateOf(MovieDetailModel())
    val message = mutableStateOf("")

    init {
        provideMovieDetail(movieID = movieID)
    }

    fun provideMovieDetail(movieID: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val output = useCase.provideMovieDetail(movieID = movieID)
            movieDetailState.value = output.first
            message.value = output.second
        }
    }

    fun generateGenreString(genres: List<GenreModel>): String {
        return genres.joinToString(separator = ", ") {
            it.genreName
        }
    }

    fun generateLanguageString(languages: List<LanguageModel>): String {
        return languages.joinToString(separator = ", ") {
            it.languageOriName
        }
    }

    fun retrieveBookingURL(): String {
        return useCase.retrieveBookingURL()
    }
}