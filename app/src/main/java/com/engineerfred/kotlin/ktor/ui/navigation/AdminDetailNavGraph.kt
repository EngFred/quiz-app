package com.engineerfred.kotlin.ktor.ui.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.engineerfred.kotlin.ktor.ui.screens.admin.add_admin.AdminRegisterScreen
import com.engineerfred.kotlin.ktor.ui.screens.admin.create_update.CreateUpdateScreen
import com.engineerfred.kotlin.ktor.ui.screens.admin.home.QuestionsScreen
import com.engineerfred.kotlin.ktor.ui.screens.admin.verify_email.VerifyEmailScreen

fun NavGraphBuilder.adminDetailNavGraph( navController: NavHostController ) {
    navigation(
        startDestination = Routes.QuestionsScreen.destination,
        route = Graph.ADMIN_DETAIL
    ) {

        composable(
            route = "${Routes.QuestionsScreen.destination}/{subject}",
            arguments = listOf(
                navArgument("subject"){ NavType.StringType }
            )
        ) {
            val subject = it.arguments?.getString("subject")!!
            QuestionsScreen(
                subject = subject,
                onQuestionClicked = { questionId ->
                    navController.navigate( "${Routes.CreateUpdateScreen.destination}?questionId=$questionId" ){
                        launchSingleTop = true
                    }
                },
                onAddQuestionClicked = { subj  ->
                    navController.navigate( "${Routes.CreateUpdateScreen.destination}?subject=$subj" ){
                        launchSingleTop = true
                    }
                },
                onBackClicked = {
                    navController.navigateUp()
                }
            )
        }

        composable(
            route = "${Routes.CreateUpdateScreen.destination}?questionId={questionId}&subject={subject}",
            arguments = listOf(
                navArgument("questionId") {
                    type = NavType.StringType
                    nullable = true
                },
                navArgument("subject") {
                    type =  NavType.StringType
                    nullable = true
                }
            )
        ) {
            val questionId = it.arguments?.getString("questionId")
            val subject = it.arguments?.getString("subject")
            CreateUpdateScreen(
                questionId,
                subject,
                onBackClicked = {
                    navController.navigateUp()
                }
            )
        }

        composable(
            route = Routes.AdminRegisterScreen.destination
        ) {
            AdminRegisterScreen(
                onRegistrationSuccessful = {
                    navController.navigate( "${Routes.EmailVerificationScreen.destination}/$it") {
                        popUpTo(Routes.EmailVerificationScreen.destination)
                        launchSingleTop = true
                    }
                }
            )
        }

        composable(
            route = "${Routes.EmailVerificationScreen.destination}/{email}",
            arguments = listOf( navArgument("email") { NavType.StringType } )
        ) {
            val email = it.arguments?.getString("email")!!
            VerifyEmailScreen(
                email = email,
                onDoneClicked = {
                    navController.navigate( Routes.AdminDashBoardScreen.destination ) {
                        popUpTo( Routes.AdminDashBoardScreen.destination  )
                        launchSingleTop = true
                    }
                }
            )
        }

    }
}