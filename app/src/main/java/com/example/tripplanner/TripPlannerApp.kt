package com.example.tripplanner

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.tripplanner.ui.screens.HomeScreen
import com.example.tripplanner.ui.screens.AddTripScreen
//import com.example.tripplanner.ui.screens.AddItineraryScreen
//import com.example.tripplanner.ui.screens.TripDetailScreen
//import com.example.tripplanner.ui.screens.TripMapScreen

/**
 * enum representing all possible navigation destinations in the app
 */
enum class TripPlannerScreen{
    Home,
    AddTrip,
    TripDetail,
    TripMap,
    AddItinerary
}

//main navigation composable for the app
//sets up navhost and defines all nav routes

@Composable
fun TripPlannerApp(navController: NavHostController = rememberNavController()){
    NavHost( //container for all screens
        navController = navController,
        startDestination = TripPlannerScreen.Home.name
    ) {
        //just establishing the names and logic for screens, this wont work now

        //home screen
        composable(route = TripPlannerScreen.Home.name) {
            HomeScreen(onNavigateToAddTrip = {
                navController.navigate(TripPlannerScreen.AddTrip.name)
            },
            onNavigateToTripDetail = { tripId ->
                navController.navigate("${TripPlannerScreen.TripDetail.name}/$tripId")
            })
        }


        //add trip screen
        composable(route = TripPlannerScreen.AddTrip.name) {
            AddTripScreen(onNavigateBack = {
                navController.popBackStack()
            },
            onTripAdded = {
                navController.popBackStack()
            })
        }
        /*
        //trip details screen
        composable(
            route = "${TripPlannerScreen.TripDetail.name}/tripId",
            arguments = listOf(navArgument("tripId") {type = NavType.IntType})
        ) {
            backStackEntry -> val tripId = backStackEntry.arguments?.getInt("tripId") ?: 0
            TripDetailScreen(tripId = tripId,
                onNavigateBack = {
                    navController.popBackStack()
                },
                onNavigateToMap = {
                    navController.navigate("${TripPlannerScreen.TripMap.name}/$tripId")
                },
                onNavigateToAddItinerary = {
                    navController.navigate("${TripPlannerScreen.AddItinerary.name}/$tripId")
                }
            )
        }

        //trip map screen
        composable(
            route = "${TripPlannerScreen.TripMap.name}/{tripId}",
            arguments = listOf(navArgument("tripId") {type = NavType.IntType})
        ) {
            backStackEntry -> val tripId = backStackEntry.arguments?.getInt("tripId") ?: 0
            TripMapScreen(tripId = tripId,
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }

        //add itinerary screen
        composable(
            route = "${TripPlannerScreen.AddItinerary.name}/{tripId}",
            arguments = listOf(navArgument("tripId") { type = NavType.IntType })
        ) {
            backStackEntry -> val tripId = backStackEntry.arguments?.getInt("tripId") ?: 0
            AddItineraryScreen(tripId = tripId,
                onNavigateBack = {
                    navController.popBackStack()
                },
                onItemAdded = {
                    navController.popBackStack()
                }
            )
        }
         */
    }
}