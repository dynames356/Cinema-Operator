package com.nineties.cinemaoperator.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.rememberImagePainter
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.nineties.cinemaoperator.R
import com.nineties.cinemaoperator.model.MovieOverviewModel
import com.nineties.cinemaoperator.model.enum.SortingParam
import com.nineties.cinemaoperator.model.enum.SortingValue
import com.nineties.cinemaoperator.ui.theme.CinemaOperatorTheme
import com.nineties.cinemaoperator.viewModel.MovieOverviewViewModel

@Composable
fun MovieListingScreen(navController: NavHostController?) {
    val viewModel: MovieOverviewViewModel = viewModel()
    val movies = viewModel.movieOverviewState.value

    Scaffold(topBar = {
        AppBar(
            title = "Movie List",
            icon = Icons.Default.Home
        ) { }
    }) {
        Surface(
            modifier = Modifier.fillMaxSize()
        ) {
            if (viewModel.message.value.isEmpty()) {
                Column() {
                    ListingBar(
                        selectedParam = viewModel.selectedParam.value,
                        selectedValue = viewModel.selectedValue.value,
                        sortParamAction = {
                            viewModel.setParamResource(it)
                            viewModel.listMovies()
                        },
                        sortValueAction = {
                            viewModel.setValueResource(it)
                            viewModel.listMovies()
                        }
                    )
                    
                    SwipeRefresh(
                        state = rememberSwipeRefreshState(isRefreshing = viewModel.refreshing.value),
                        onRefresh = {
                            viewModel.refreshing.value = true
                            viewModel.refreshMovies()
                            viewModel.refreshing.value = false
                        }
                    ) {
                        LazyColumn() {
                            itemsIndexed(movies) { index, movie ->
                                if (movies.lastIndex == index) {
                                    LaunchedEffect(key1 = true, block = {
                                        viewModel.updateMovies(viewModel.currentPage + 1)
                                    })
                                }

                                MovieCell(movie = movie) {
                                    navController?.navigate("movie_detail/${movie.movieID}")
                                }
                            }
                        }
                    }
                }
            } else {
                ErrorDetailScreen(
                    message = "Unable to Query Movies. Reason => ${viewModel.message.value}\nPress Button Below to retry"
                ) {
                    viewModel.listMovies()
                }
            }
        }
    }
}

@Composable
fun ListingBar(selectedParam: Int, selectedValue: Int, sortParamAction: (SortingParam) -> Unit, sortValueAction: (SortingValue) -> Unit) {
    var paramExpanded by remember {
        mutableStateOf(false)
    }
    var valueExpanded by remember {
        mutableStateOf(false)
    }

    val paramItems = enumValues<SortingParam>()
    val valueItems = enumValues<SortingValue>()

    Card(
        shape = CutCornerShape(8.dp),
        elevation = 2.dp,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp, bottom = 0.dp, start = 16.dp, end = 16.dp)
    ) {
        Column() {
            Text(
                text = stringResource(id = R.string.order_by),
                style = MaterialTheme.typography.body1,
                modifier = Modifier.padding(4.dp)
            )
            
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                Column() {
                    Button(onClick = { paramExpanded = !paramExpanded }) {
                        Text(
                            text = stringResource(id = selectedParam),
                            style = MaterialTheme.typography.body2
                        )
                    }
                    DropdownMenu(
                        expanded = paramExpanded,
                        onDismissRequest = { paramExpanded = false },
                        modifier = Modifier
                            .fillMaxWidth()

                    ) {
                        paramItems.forEach {
                            DropdownMenuItem(onClick = {
                                paramExpanded = false
                                sortParamAction(it)
                            }) {
                                Text(
                                    text = stringResource(id = getParamResource(it)),
                                    style = MaterialTheme.typography.body2
                                )
                            }
                        }
                    }
                }

                Column() {
                    Button(onClick = { valueExpanded = !valueExpanded }) {
                        Text(
                            text = stringResource(id = selectedValue),
                            style = MaterialTheme.typography.body2
                        )
                    }
                    DropdownMenu(
                        expanded = valueExpanded,
                        onDismissRequest = { paramExpanded = false }
                    ) {
                        valueItems.forEach {
                            DropdownMenuItem(onClick = {
                                valueExpanded = false
                                sortValueAction(it)
                            }) {
                                Text(
                                    text = stringResource(id = getValueResource(it)),
                                    style = MaterialTheme.typography.body2
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

fun getParamResource(param: SortingParam): Int {
    return when(param) {
        SortingParam.release_date -> R.string.release_date
        SortingParam.original_title -> R.string.title
        SortingParam.vote_average -> R.string.rating
    }
}

fun getValueResource(value: SortingValue): Int {
    return when (value) {
        SortingValue.asc -> R.string.ascending
        SortingValue.desc -> R.string.descending
    }
}

@Composable
fun MovieCell(movie: MovieOverviewModel, clickAction: () -> Unit) {
    Card(
        shape = RoundedCornerShape(8.dp),
        elevation = 2.dp,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp, bottom = 0.dp, start = 16.dp, end = 16.dp)
            .clickable(onClick = { clickAction.invoke() })
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Image(
                painter = rememberImagePainter(
                    data = movie.moviePoster,
                    builder = {
                        crossfade(true)
                        placeholder(R.drawable.loading_icon)
                        error(R.drawable.default_movie_image)
                        fallback(R.drawable.default_movie_image)
                    }
                ),
                contentDescription = null,
                modifier = Modifier
                    .size(100.dp)
                    .padding(4.dp)
            )

            Column(
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier
                        .padding(start = 8.dp, end = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(id = R.string.movie_title),
                        style = MaterialTheme.typography.body1,
                        modifier = Modifier.padding(end = 8.dp)
                    )
                    Text(
                        text = movie.movieTitle,
                        style = MaterialTheme.typography.h6
                    )
                }

                Row(
                    modifier = Modifier
                        .padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(id = R.string.popularity),
                        style = MaterialTheme.typography.body2,
                        modifier = Modifier.padding(end = 8.dp)
                    )
                    Text(
                        text = movie.moviePopularity,
                        style = MaterialTheme.typography.body1
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ListingBarPreview() {
    CinemaOperatorTheme {
        ListingBar(selectedParam = R.string.ascending, selectedValue = R.string.descending, sortParamAction = { }, sortValueAction = { })
    }
}

@Preview(showBackground = true)
@Composable
fun MovieCellPreview() {
    CinemaOperatorTheme {
        MovieCell(movie = MovieOverviewModel()) { }
    }
}