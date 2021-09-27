package com.nineties.cinemaoperator.viewModel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nineties.cinemaoperator.R
import com.nineties.cinemaoperator.model.MovieOverviewModel
import com.nineties.cinemaoperator.model.enum.SortingParam
import com.nineties.cinemaoperator.model.enum.SortingValue
import com.nineties.cinemaoperator.useCase.MovieUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MovieOverviewViewModel(
    private val useCase: MovieUseCase = MovieUseCase(),
): ViewModel() {
    var currentPage: Int = 1
    var sortingList: Pair<SortingParam, SortingValue> = Pair(SortingParam.release_date, SortingValue.desc)

    var selectedParam = mutableStateOf(R.string.release_date)
    var selectedValue = mutableStateOf(R.string.descending)
    val refreshing = mutableStateOf(false)

    val movieOverviewState = mutableStateOf(emptyList<MovieOverviewModel>())
    val message = mutableStateOf("")

    init {
        listMovies()
    }

    fun setParamResource(param: SortingParam) {
        sortingList = Pair(param, sortingList.second)
        selectedParam.value = when(param) {
            SortingParam.release_date -> R.string.release_date
            SortingParam.original_title -> R.string.title
            SortingParam.vote_average -> R.string.rating
        }
    }

    fun setValueResource(value: SortingValue) {
        sortingList = Pair(sortingList.first, value)
        selectedValue.value = when (value) {
            SortingValue.asc -> R.string.ascending
            SortingValue.desc -> R.string.descending
        }
    }

    fun refreshMovies() {
        if (refreshing.value) {
            sortingList = Pair(SortingParam.release_date, SortingValue.desc)
            setParamResource(SortingParam.release_date)
            setValueResource(SortingValue.desc)
            listMovies()
        }
    }

    fun listMovies() {
        currentPage = 1
        viewModelScope.launch(Dispatchers.IO) {
            val output = useCase.listMovies(sortingList = sortingList, page = currentPage)
            movieOverviewState.value = output.first
            message.value = output.second
        }
    }

    fun updateMovies(page: Int) {
        currentPage = page
        viewModelScope.launch(Dispatchers.IO) {
            val output = useCase.listMovies(sortingList = sortingList, page = currentPage)
            movieOverviewState.value = movieOverviewState.value.plus(output.first)
            message.value = output.second
        }
    }
}