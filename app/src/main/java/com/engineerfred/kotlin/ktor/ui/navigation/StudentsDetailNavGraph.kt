package com.engineerfred.kotlin.ktor.ui.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.engineerfred.kotlin.ktor.ui.screens.student.questions.QuizQuestionsScreen
import com.engineerfred.kotlin.ktor.ui.screens.student.results.ResultsScreen
import com.engineerfred.kotlin.ktor.ui.viewModel.SharedViewModel
import com.engineerfred.kotlin.ktor.util.Constants

fun NavGraphBuilder.studentsDetailNavGraph(
    navController: NavController ,
    sharedViewModel: SharedViewModel
) {
    navigation(
        route = Graph.STUDENTS_DETAIL,
        startDestination = Routes.QuizQuestionsScreen.destination
    ) {

        composable(
            route = "${Routes.QuizQuestionsScreen.destination}/{${Constants.SUBJECT_KEY}}/{${Constants.LEVEL_KEY}}",
            arguments = listOf(
                navArgument(Constants.SUBJECT_KEY) { NavType.StringType },
                navArgument(Constants.LEVEL_KEY) { NavType.StringType }
            )
        ) {
            val subject = it.arguments?.getString(Constants.SUBJECT_KEY)!!
            val studentLevel = it.arguments?.getString(Constants.LEVEL_KEY)!!

            QuizQuestionsScreen(
                studentLevel = studentLevel,
                subject = subject,
                onBackClicked = {
                    navController.navigateUp()
                }, sharedViewModel = sharedViewModel, onShowPerformance = {
                    navController.navigate(Routes.QuizResultsScreen.destination){
                        launchSingleTop = true
                    }
                }
            )

        }

        composable(
            route = Routes.QuizResultsScreen.destination
        ) {
            ResultsScreen(sharedViewModel = sharedViewModel)
        }

    }
}