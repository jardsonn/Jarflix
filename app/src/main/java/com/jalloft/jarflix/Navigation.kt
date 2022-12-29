package com.jalloft.jarflix

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.jalloft.jarflix.model.movie.MovieResult
import com.jalloft.jarflix.ui.components.HorizontalPanelAction
import com.jalloft.jarflix.ui.screens.details.MovieDetails
import com.jalloft.jarflix.ui.screens.home.Home
import timber.log.Timber

@Composable
fun AppNavigator() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screens.HomeScreen.route
    ) {
        composable(Screens.HomeScreen.route) {
            Home() {
                if (it is HorizontalPanelAction.SeeAll) {

                } else if (it is HorizontalPanelAction.Item<*>) {
                    Timber.i("ir para a proxima")
                    navController.navigate(Screens.MovieDetailsScreen.route.plus("/?movieId=${(it.data as MovieResult).id}"))
                }
            }
        }

        composable(
            Screens.MovieDetailsScreen.route.plus("/?movieId={movieId}"),
            arguments = listOf(
                navArgument("movieId") {
                    type = NavType.IntType
                    defaultValue = -1
                })
        ) {
            it.arguments?.getInt("movieId")?.let { movieId ->
                Timber.i("chengando aqi")
                MovieDetails(movieId =  movieId){
                    navController.popBackStack()
                }
            }

        }


    }
}

sealed class Screens(val route: String) {
    object HomeScreen : Screens("home")
    object MovieDetailsScreen : Screens("details")

}