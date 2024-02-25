package com.engineerfred.kotlin.ktor.common

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.PeopleAlt
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.toFontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.engineerfred.kotlin.ktor.R
import com.engineerfred.kotlin.ktor.ui.model.BottomBarItem
import com.engineerfred.kotlin.ktor.ui.navigation.Routes
import com.engineerfred.kotlin.ktor.ui.theme.KtorTheme

@Composable
fun BottomBar(
    items: List<BottomBarItem>,
    navController: NavHostController,
) {

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    BottomAppBar(
        containerColor = MaterialTheme.colorScheme.primary,
        contentColor = Color.White
    ) {

        items.forEach {
            NavigationBarItem(
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = if ( !isSystemInDarkTheme() ) Color.Black else Color.White,
                    selectedTextColor = if ( !isSystemInDarkTheme() ) Color.Black else Color.White,
                    unselectedIconColor = if ( !isSystemInDarkTheme() ) Color.White else Color.Black,
                    unselectedTextColor = if ( !isSystemInDarkTheme() ) Color.White else Color.Black,
                    indicatorColor = MaterialTheme.colorScheme.primary
                ),
                selected = currentDestination?.route == it.destinationScreen,
                onClick = {
                    navController.navigate( it.destinationScreen ) {
                        popUpTo(navController.graph.findStartDestination().id){ saveState =  true }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = { Icon(imageVector = it.icon, contentDescription = it.description) },
                label = {  Text(text = it.label, fontFamily = Font(R.font.lexend_medium).toFontFamily())  }
            )
        }
    }

}


@Preview( showBackground = true )
@Composable
fun BottomBarPreview() {
    KtorTheme {
        BottomBar(items = listOf(
            BottomBarItem(
                label = "Home",
                description = "home",
                icon = Icons.Rounded.Home,
                destinationScreen = Routes.StudentDashBoardScreen.destination
            ),
            BottomBarItem(
                label = "Others",
                description = "others",
                icon = Icons.Rounded.PeopleAlt,
                destinationScreen = Routes.StudentDashBoardScreen.destination
            )
        ), navController = rememberNavController())
    }
}