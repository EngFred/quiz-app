package com.engineerfred.kotlin.ktor.ui.screens.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AddCircle
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.PeopleAlt
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.toFontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.engineerfred.kotlin.ktor.R
import com.engineerfred.kotlin.ktor.common.BottomBar
import com.engineerfred.kotlin.ktor.ui.model.BottomBarItem
import com.engineerfred.kotlin.ktor.ui.navigation.AdminHomeNavigationGraph
import com.engineerfred.kotlin.ktor.ui.navigation.Routes
import com.engineerfred.kotlin.ktor.ui.viewModel.SharedViewModel

@Composable
fun AdminHomeScreen(
    sharedViewModel: SharedViewModel,
    onAddAdminClicked: () -> Unit,
    navController: NavHostController = rememberNavController()
) {

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val admin = sharedViewModel.admin

    val bottomBarItems = listOf(
        BottomBarItem(
            label = "Home",
            description = "home",
            icon = Icons.Rounded.Home,
            destinationScreen = Routes.AdminDashBoardScreen.destination
        ),
        BottomBarItem(
            label = "Admins",
            description = "admins",
            icon = Icons.Rounded.PeopleAlt,
            destinationScreen = Routes.AdminsFeedbackScreen.destination
        )
    )

    admin?.let {

        Scaffold(
            topBar = {
                if ( bottomBarItems.any { it.destinationScreen == currentDestination?.route } ) {
                    Row(
                        Modifier
                            .background(MaterialTheme.colorScheme.primary)
                            .fillMaxWidth()
                            .padding(start = 10.dp, top = 10.dp, bottom = 10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Hi, ${admin.lastName}",
                            fontFamily = Font(R.font.lexend_bold).toFontFamily(),
                            fontSize = 18.sp,
                            color = Color.White
                        )

                        IconButton(onClick = { onAddAdminClicked.invoke() }) {
                            Icon(
                                imageVector = Icons.Rounded.AddCircle,
                                contentDescription = stringResource(R.string.add_admin),
                                tint = Color.White
                            )
                        }
                    }
                }
            },
            bottomBar = {
                if ( bottomBarItems.any { it.destinationScreen == currentDestination?.route } ) {
                    BottomBar(
                        bottomBarItems,
                        navController
                    )
                }
            }
        ) {

            AdminHomeNavigationGraph(
                sharedViewModel = sharedViewModel,
                navController = navController,
                modifier = Modifier.padding(it)
            )
        }

    }

}