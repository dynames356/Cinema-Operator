package com.nineties.cinemaoperator.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.ScrollableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.rememberImagePainter
import coil.size.Scale
import coil.transform.CircleCropTransformation
import coil.transform.RoundedCornersTransformation
import com.nineties.cinemaoperator.R
import com.nineties.cinemaoperator.model.MovieDetailModel
import com.nineties.cinemaoperator.ui.theme.CinemaOperatorTheme
import com.nineties.cinemaoperator.viewModel.MovieDetailViewModel
import com.nineties.cinemaoperator.viewModel.factory.MovieDetailViewModelFactory
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@Composable()
fun MovieDetailScreen(movieID: Int, navController: NavHostController?) {
    val viewModel: MovieDetailViewModel = viewModel(key = null, factory = MovieDetailViewModelFactory(movieID = movieID))

    val movie = viewModel.movieDetailState.value
    val genderValue = viewModel.generateGenreString(movie.movieGenres)
    val languageValue = viewModel.generateLanguageString(movie.movieLanguages)

    Scaffold(topBar = {
        AppBar(
            title = "Movie Details",
            icon = Icons.Default.ArrowBack
        ) {
            navController?.navigateUp()
        }
    }) {
        Surface(
            modifier = Modifier.fillMaxSize()
        ) {
            if (viewModel.message.value.isEmpty()) {
                DetailScreen(
                    movie = movie,
                    genderValue = genderValue,
                    languageValue = languageValue,
                    onClick = {
                        val inputURL = URLEncoder.encode(
                            viewModel.retrieveBookingURL(),
                            StandardCharsets.UTF_8.toString()
                        )
                        navController?.navigate("booking_screen/${inputURL}")
                    }
                )
            } else {
                ErrorDetailScreen(
                    message = "Unable to Query movie detail. Reason => ${viewModel.message.value}\nPress Button Below to retry"
                ) {
                    viewModel.provideMovieDetail(movieID = movieID)
                }
            }
        }
    }
}

@Composable
fun DetailScreen(
    movie: MovieDetailModel, genderValue: String, languageValue: String, onClick:() -> Unit) {
    Image(
        painter = rememberImagePainter(
            data = movie.movieBackground,
            builder = {
                transformations(RoundedCornersTransformation(8.0f))
                crossfade(true)
                placeholder(R.drawable.loading_icon)
                error(R.drawable.default_movie_image)
                fallback(R.drawable.default_movie_image)
            }
        ),
        contentDescription = null,
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .fillMaxSize(),
        alpha = 0.2f
    )

    Column(
        modifier = Modifier
            .padding(8.dp)
            .scrollable(
                state = rememberScrollState(0),
                orientation = Orientation.Vertical,
                enabled = true
            ),
        horizontalAlignment = Alignment.Start
    ) {

        Card(
            shape = RoundedCornerShape(8.dp),
            elevation = 2.dp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp, bottom = 4.dp, start = 16.dp, end = 16.dp),
        ) {
            Column(
                modifier = Modifier
                    .padding(8.dp),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = stringResource(id = R.string.synopsis),
                    style = MaterialTheme.typography.h6
                )
                Text(
                    text = if (movie.movieSynopsis.isEmpty()) stringResource(id = R.string.not_available) else movie.movieSynopsis,
                    style = MaterialTheme.typography.body1
                )
            }
        }

        Card(
            shape = RoundedCornerShape(8.dp),
            elevation = 2.dp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 4.dp, bottom = 4.dp, start = 16.dp, end = 16.dp),
        ) {
            Column(
                modifier = Modifier
                    .padding(8.dp),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = stringResource(id = R.string.genres),
                    style = MaterialTheme.typography.h6
                )
                Text(
                    text = if (genderValue.isEmpty()) stringResource(id = R.string.not_available) else genderValue,
                    style = MaterialTheme.typography.body1
                )
            }
        }

        Card(
            shape = RoundedCornerShape(8.dp),
            elevation = 2.dp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 4.dp, bottom = 4.dp, start = 16.dp, end = 16.dp),
        ) {
            Column(
                modifier = Modifier
                    .padding(8.dp),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = stringResource(id = R.string.language),
                    style = MaterialTheme.typography.h6
                )
                Text(
                    text = if (languageValue.isEmpty()) stringResource(id = R.string.not_available) else languageValue,
                    style = MaterialTheme.typography.body1
                )
            }
        }

        Card(
            shape = RoundedCornerShape(8.dp),
            elevation = 2.dp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 4.dp, bottom = 4.dp, start = 16.dp, end = 16.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(8.dp),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = stringResource(id = R.string.duration),
                    style = MaterialTheme.typography.h6
                )
                Text(
                    text = if (movie.movieDuration == 0) stringResource(id = R.string.not_available) else movie.movieDuration.toString() + " minutes",
                    style = MaterialTheme.typography.body1
                )
            }
        }

        Button(
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 4.dp, bottom = 8.dp, start = 16.dp, end = 16.dp),
            onClick = {
                onClick.invoke()
            }
        ) {
            Text(
                text = "Book the Movie",
                style = MaterialTheme.typography.h6
            )
        }
    }
}