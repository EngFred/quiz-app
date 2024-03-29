package com.engineerfred.kotlin.ktor.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.engineerfred.kotlin.ktor.ui.screens.student.dashboard.StudentDashBoardScreen
import com.engineerfred.kotlin.ktor.ui.screens.student.students.AllStudentsScreen
import com.engineerfred.kotlin.ktor.ui.viewModel.SharedViewModel

@Composable
fun StudentsHomeNavigationGraph(
    modifier: Modifier = Modifier,
    sharedViewModel : SharedViewModel,
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        route = Graph.STUDENTS_HOME,
        startDestination = Routes.StudentDashBoardScreen.destination
    ) {

        composable( route = Routes.StudentDashBoardScreen.destination ) {
            StudentDashBoardScreen(
                onEnglishCardClicked = { subject, level ->
                    navController.navigate( "${Routes.QuizQuestionsScreen.destination}/$subject/$level" ) {
                        launchSingleTop = true
                    }
                },
                onMathCardClicked = { subject, level ->
                    navController.navigate( "${Routes.QuizQuestionsScreen.destination}/$subject/$level" ) {
                        launchSingleTop = true
                    }
                }, sharedViewModel
            )
        }

        composable(
            route = Routes.StudentsScreen.destination
        ) {
            AllStudentsScreen(modifier)
        }

        //nested nav graph
        studentsDetailNavGraph( navController, sharedViewModel )

    }

}