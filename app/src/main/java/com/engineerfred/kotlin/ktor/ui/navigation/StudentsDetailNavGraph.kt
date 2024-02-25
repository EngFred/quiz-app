package com.engineerfred.kotlin.ktor.ui.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.engineerfred.kotlin.ktor.ui.screens.student.questions.QuizQuestionsScreen

fun NavGraphBuilder.studentsDetailNavGraph( navController: NavController ) {
    navigation(
        route = Graph.STUDENTS_DETAIL,
        startDestination = Routes.QuizQuestionsScreen.destination
    ) {

        composable(
            route = "${Routes.QuizQuestionsScreen.destination}/{subject}/{level}",
            arguments = listOf(
                navArgument("subject") { NavType.StringType },
                navArgument("level") { NavType.StringType }
            )
        ) {
            val subject = it.arguments?.getString("subject")!!
            val studentLevel = it.arguments?.getString("level")!!

            QuizQuestionsScreen(
                studentLevel = studentLevel,
                subject = subject
            )

        }

    }
}