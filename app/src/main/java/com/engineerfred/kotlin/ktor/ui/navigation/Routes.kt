package com.engineerfred.kotlin.ktor.ui.navigation

sealed class Routes( val destination: String ) {
    data object WelcomeScreen : Routes("welcome_screen")
    data object AdminLoginScreen : Routes("admin_login_screen")
    data object AdminRegisterScreen : Routes("admin_register_screen")
    data object QuestionsScreen : Routes("questions_screen")
    data object CreateUpdateScreen : Routes("question_create_update_screen")
    data object StudentRegisterScreen : Routes("student_register_screen")
    data object StudentsScreen : Routes("student_screen")
    data object QuizQuestionsScreen : Routes("quiz_questions_screen")
    data object QuizResultsScreen : Routes("quiz_results_screen")
    data object AdminsScreen : Routes("admins_screen")
    data object StudentDashBoardScreen : Routes("student_dash_board_screen")
    data object AdminDashBoardScreen : Routes("admin_dash_board_screen")
    data object StudentProfileSetUpScreen : Routes("student_profile_setup_screen")
}

object Graph {
    const val ROOT = "root_graph"
    const val STUDENTS_HOME = "students_home_graph"
    const val STUDENTS_DETAIL = "students_detail_graph"
    const val ADMIN_HOME = "admin_home_graph"
    const val ADMIN_DETAIL = "admin_detail_graph"
}