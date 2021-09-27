package com.nineties.cinemaoperator.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.nineties.cinemaoperator.screen.BookingScreen
import com.nineties.cinemaoperator.screen.MovieDetailScreen
import com.nineties.cinemaoperator.screen.MovieListingScreen
import com.nineties.cinemaoperator.ui.theme.CinemaOperatorTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CinemaOperatorTheme {
                AppApplication()
            }
        }
    }
}

@Composable
fun AppApplication() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "movie_list", builder = {
        composable(route = "movie_list") {
            MovieListingScreen(navController = navController)
        }
        composable(
            route = "movie_detail/{movieID}",
            arguments = listOf(navArgument("movieID") {
                type = NavType.IntType
            })
        ) {
            MovieDetailScreen(movieID = it.arguments!!.getInt("movieID"), navController = navController)
        }
        composable(
            route = "booking_screen/{inputURL}",
            arguments = listOf(navArgument("inputURL") {
                type = NavType.StringType
            })
        ) {
            BookingScreen(inputURL = it.arguments!!.getString("inputURL", ""), navController = navController)
        }
    })
}