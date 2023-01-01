package com.jalloft.jarflix

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.jalloft.jarflix.model.genre.toJson
import com.jalloft.jarflix.model.movie.MovieRowType
import com.jalloft.jarflix.ui.screens.details.MovieDetails
import com.jalloft.jarflix.ui.screens.home.Home
import com.jalloft.jarflix.ui.screens.list.MovieListScreen

@Composable
fun AppNavigator() {
    val navController = rememberNavController()
    val currentMovieName = remember {
        mutableStateOf("")
    }
    NavHost(
        navController = navController,
        startDestination = Screens.HomeScreen.route
    ) {
        composable(Screens.HomeScreen.route) {
            Home(
                onSeeMore = {
                    navController.navigate(Screens.MovieListScreen.route.plus("/?itemType=${it.name}"))
                },
                onGenre = {
                    navController.navigate(
                        Screens.MovieListScreen.route.plus("/?genre=${it.toJson()}")
                            .plus("&itemType=${MovieRowType.GENRE.name}")
                    )
                }
            ) {
                currentMovieName.value = it.title ?: ""
                navController.navigate(
                    Screens.MovieDetailsScreen.route.plus("/?movieId=${it.id}")
                )
            }
        }

        composable(
            Screens.MovieDetailsScreen.route.plus("/?movieId={movieId}"),
            arguments = listOf(
                navArgument("movieId") {
                    type = NavType.IntType
                    defaultValue = -1
                },
            )
        ) {
            it.arguments?.getInt("movieId")?.let { movieId ->
                MovieDetails(
                    movieId = movieId,
                    onSeeMore = { movieDetaisl ->
                        navController.navigate(
                            Screens.MovieListScreen.route.plus("/?movieId=$movieId")
                                .plus("&movieName=${movieDetaisl.title}")
                        )
                    },
                    onBackPressed = { navController.popBackStack() })
            }

        }

        composable(
            Screens.MovieListScreen.route.plus("/?itemType={itemType}").plus("&movieId={movieId}")
                .plus("&movieName={movieName}").plus("&genre={genre}"),
            arguments = listOf(
                navArgument("itemType") {
                    type = NavType.StringType
                    defaultValue = MovieRowType.SIMILAR.name
                },
                navArgument("movieId") {
                    type = NavType.IntType
                    defaultValue = -1
                },
                navArgument("movieName") {
                    type = NavType.StringType
                    defaultValue = ""
                },
                navArgument("genre") {
                    type = NavType.StringType
                    defaultValue = ""
                },
            )
        ) {
            it.arguments?.let { extra ->
                MovieListScreen(
                    onBackPressed = { navController.popBackStack() }
                ) { movieReult ->
                    navController.navigate(Screens.MovieDetailsScreen.route.plus("/?movieId=${movieReult.id}"))
                }
            }
        }


    }
}

sealed class Screens(val route: String) {
    object HomeScreen : Screens("home")
    object MovieDetailsScreen : Screens("details")
    object MovieListScreen : Screens("list")

}