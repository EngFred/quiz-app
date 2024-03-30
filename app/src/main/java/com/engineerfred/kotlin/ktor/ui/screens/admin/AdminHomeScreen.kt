package com.engineerfred.kotlin.ktor.ui.screens.admin

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material.icons.rounded.PeopleAlt
import androidx.compose.material.icons.rounded.PeopleOutline
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.toFontFamily
import androidx.compose.ui.text.style.TextAlign
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
import com.engineerfred.kotlin.ktor.util.restartApp
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun AdminHomeScreen(
    sharedViewModel: SharedViewModel,
    onAddAdminClicked: () -> Unit,
    onSignOut: () -> Unit,
    navController: NavHostController = rememberNavController()
) {

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val coroutineScope = rememberCoroutineScope()

    val admin = sharedViewModel.admin

    val context = LocalContext.current

    var isMenuExpanded by remember {
        mutableStateOf(false)
    }

    var isLoggingOut by remember {
        mutableStateOf(false)
    }

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
            destinationScreen = Routes.AdminsScreen.destination
        ),
        BottomBarItem(
            label = "Students",
            description = "admins",
            icon = Icons.Rounded.PeopleOutline,
            destinationScreen = Routes.StudentsScreen.destination
        ),
    )

    if ( admin != null ) {
        Scaffold(
            topBar = {
                if ( bottomBarItems.any { it.destinationScreen == currentDestination?.route } ) {
                    Row(
                        Modifier
                            .background(MaterialTheme.colorScheme.primary)
                            .fillMaxWidth()
                            .padding(start = 10.dp, top = 10.dp, bottom = 10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Hi, ${admin.lastName}",
                            fontFamily = Font(R.font.lexend_bold).toFontFamily(),
                            fontSize = 18.sp,
                            color = Color.White,
                            modifier = Modifier
                                .weight(1f)
                                .padding(end = 8.dp)
                        )

                        Column(
                            modifier = Modifier
                                .width(125.dp),
                            horizontalAlignment = Alignment.End
                        ) {
                            IconButton(
                                onClick = {
                                    if ( !isLoggingOut ) {
                                        isMenuExpanded = true
                                    }
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Rounded.MoreVert,
                                    contentDescription = stringResource(R.string.add_admin),
                                    tint = Color.White
                                )
                            }
                            DropdownMenu(
                                expanded = isMenuExpanded,
                                onDismissRequest = { isMenuExpanded = false }
                            ) {
                                DropdownMenuItem(
                                    text = { Text(text = "Add Admin") },
                                    onClick = {
                                        isMenuExpanded = false
                                        if ( FirebaseAuth.getInstance().currentUser?.email == "omongolealfred4@gmail.com" || FirebaseAuth.getInstance().currentUser?.email == "engfred88@gmail.com" ) {
                                            onAddAdminClicked.invoke()
                                        } else {
                                            Toast.makeText(context, "You need to be a superuser to add admins!", Toast.LENGTH_LONG).show()
                                        }
                                    }
                                )
                                HorizontalDivider()
                                DropdownMenuItem(
                                    text = { Text(text = "Logout") },
                                    onClick = {
                                        isMenuExpanded = false
                                        isLoggingOut = true
                                        Toast.makeText(context, "Logging out...", Toast.LENGTH_SHORT).show()
                                        FirebaseAuth.getInstance().signOut()
                                        coroutineScope.launch {
                                            delay(3000)
                                            restartApp(context)
                                        }
                                    }
                                )
                            }
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

    } else {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(
                text = "Opps! Something went wrong! Try restarting the app.",
                fontSize = 22.sp,
                fontWeight = FontWeight.ExtraBold,
                textAlign = TextAlign.Center
            )
        }
    }


}