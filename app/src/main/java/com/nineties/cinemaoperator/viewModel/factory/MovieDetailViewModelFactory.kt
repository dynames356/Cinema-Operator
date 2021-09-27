package com.nineties.cinemaoperator.viewModel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.nineties.cinemaoperator.useCase.MovieUseCase

class MovieDetailViewModelFactory(val movieID: Int) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(Int::class.java, MovieUseCase::class.java).newInstance(movieID, MovieUseCase())
    }
}