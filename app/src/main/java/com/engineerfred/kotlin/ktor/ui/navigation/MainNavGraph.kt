package com.engineerfred.kotlin.ktor.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.engineerfred.kotlin.ktor.ui.screens.admin.AdminHomeScreen
import com.engineerfred.kotlin.ktor.ui.screens.admin.add_admin.AdminRegisterScreen
import com.engineerfred.kotlin.ktor.ui.screens.admin.login.AdminLoginScreen
import com.engineerfred.kotlin.ktor.ui.screens.student.StudentsHomeScreen
import com.engineerfred.kotlin.ktor.ui.screens.student.profile_setup.StudentProfileSetupScreen
import com.engineerfred.kotlin.ktor.ui.screens.student.register.StudentRegisterScreen
import com.engineerfred.kotlin.ktor.ui.screens.welcome.WelcomeScreen
import com.engineerfred.kotlin.ktor.ui.viewModel.SharedViewModel
import com.engineerfred.kotlin.ktor.ui.viewModel.WelcomeScreenViewModel

@Composable
fun MainNavigationGraph(
    navController: NavHostController,
    welcomeScreenViewModel: WelcomeScreenViewModel,
    sharedViewModel: SharedViewModel,
    startDestination: String
) {


    NavHost(
        navController = navController,
        route = Graph.ROOT,
        startDestination = startDestination
    ) {
        composable(
            route = Routes.WelcomeScreen.destination
        ) {
            WelcomeScreen(
                welcomeScreenViewModel = welcomeScreenViewModel,
                navigateToStudentRegister = {
                    navController.navigate( Routes.StudentRegisterScreen.destination ) {
                        popUpTo(Routes.StudentRegisterScreen.destination)
                        launchSingleTop = true
                    }
                },
                navigateToAdminLogin = {
                    navController.navigate( Routes.AdminLoginScreen.destination ) {
                        popUpTo(Routes.AdminLoginScreen.destination)
                        launchSingleTop = true
                    }
                }
            )
        }
        composable(
            route = Routes.StudentRegisterScreen.destination
        ) {
            StudentRegisterScreen(
                onNewStudentAuthenticationSuccess = {
                    navController.navigate( Routes.StudentProfileSetUpScreen.destination ) {
                        popUpTo( Routes.StudentProfileSetUpScreen.destination  )
                        launchSingleTop = true
                    }
                },
                onOldStudentAuthenticationSuccess = {
                    navController.navigate( Graph.STUDENTS_HOME ) {
                        popUpTo( Graph.STUDENTS_HOME )
                        launchSingleTop = true
                    }
                },
                onNotStudentClicked = {
                    navController.navigate( Routes.AdminLoginScreen.destination ) {
                        popUpTo( Routes.AdminLoginScreen.destination )
                        launchSingleTop = true
                    }
                }, sharedViewModel = sharedViewModel
            )
        }

        composable(
            route = Routes.AdminLoginScreen.destination
        ) {
            AdminLoginScreen(
                onLoginSuccessful = {
                    navController.navigate( Graph.ADMIN_HOME ) {
                        popUpTo(Graph.ADMIN_HOME)
                        launchSingleTop = true
                    }
                },
                onNotAdminClick = {
                    navController.navigate( Routes.StudentRegisterScreen.destination ) {
                        popUpTo( Routes.StudentRegisterScreen.destination )
                        launchSingleTop = true
                    }
                },
                sharedViewModel = sharedViewModel
            )
        }

        composable(
            route = Routes.StudentProfileSetUpScreen.destination
        ) {
            StudentProfileSetupScreen(
                onRegistrationSuccess = {
                    navController.navigate( Graph.STUDENTS_HOME ) {
                        popUpTo( Graph.STUDENTS_HOME )
                        launchSingleTop = true
                    }
                },
                sharedViewModel = sharedViewModel
            )
        }

        composable(
            route = Graph.STUDENTS_HOME
        ) {
            StudentsHomeScreen(sharedViewModel = sharedViewModel)
        }

        composable(
            route = Graph.ADMIN_HOME
        ) {
            AdminHomeScreen(
                sharedViewModel = sharedViewModel,
                onAddAdminClicked = {
                navController.navigate( Routes.AdminRegisterScreen.destination ) {
                    launchSingleTop = true
                } }, onSignOut = {
                    navController.navigate( Routes.AdminLoginScreen.destination ) {
                        popUpTo( Routes.AdminLoginScreen.destination )
                        launchSingleTop = true
                    }
                }
            )
        }

        composable(
            route = Routes.AdminRegisterScreen.destination
        ) {
            AdminRegisterScreen(
                onRegistrationSuccessful = {
                    navController.navigateUp()
                }
            )
        }

    }

}