package com.engineerfred.kotlin.ktor.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.engineerfred.kotlin.ktor.ui.screens.admin.dashbord.AdminDashboardScreen
import com.engineerfred.kotlin.ktor.ui.screens.admin.feedback_review.FeedbackScreen
import com.engineerfred.kotlin.ktor.ui.viewModel.SharedViewModel

@Composable
fun AdminHomeNavigationGraph(
    modifier: Modifier = Modifier,
    sharedViewModel : SharedViewModel,
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        route = Graph.ADMIN_HOME,
        startDestination = Routes.AdminDashBoardScreen.destination
    ) {

        composable(
            route = Routes.AdminDashBoardScreen.destination
        ) {
            AdminDashboardScreen(
                onMathCardClicked = {
                    navController.navigate( "${Routes.QuestionsScreen.destination}/$it" ) {
                        launchSingleTop = true
                    }
                },
                onEnglishCardClicked = {
                    navController.navigate( "${Routes.QuestionsScreen.destination}/$it" ) {
                        launchSingleTop = true
                    }
                }
            )
        }

        composable(
            route = Routes.AdminsFeedbackScreen.destination
        ) {
            FeedbackScreen()
        }

        adminDetailNavGraph( navController )

    }

}