package com.engineerfred.kotlin.ktor.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.engineerfred.kotlin.ktor.ui.model.User
import com.engineerfred.kotlin.ktor.ui.navigation.Graph
import com.engineerfred.kotlin.ktor.ui.navigation.MainNavigationGraph
import com.engineerfred.kotlin.ktor.ui.navigation.Routes
import com.engineerfred.kotlin.ktor.ui.theme.KtorTheme
import com.engineerfred.kotlin.ktor.ui.viewModel.SharedViewModel
import com.engineerfred.kotlin.ktor.ui.viewModel.WelcomeScreenViewModel
import com.engineerfred.kotlin.ktor.util.Constants.LOGGED_IN_USER
import com.engineerfred.kotlin.ktor.util.Constants.userIsCurrentlyLoggedIn
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            KtorTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    val welcomeScreenViewModel = hiltViewModel<WelcomeScreenViewModel>()
                    val sharedViewModel: SharedViewModel = viewModel()
                    val state = welcomeScreenViewModel.uiState.collectAsState().value

                    val startDestination = when (state.appUser) {
                        null -> Routes.WelcomeScreen.destination
                        User.Student.name.lowercase() -> {
                            if ( userIsCurrentlyLoggedIn ) {
                                when (state.studentNotInDatabase) {
                                    true -> {
                                        Routes.StudentProfileSetUpScreen.destination
                                    }
                                    false -> {
                                        sharedViewModel.addStudent( state.student!! )
                                        Graph.STUDENTS_HOME
                                    }
                                    else -> Routes.WelcomeScreen.destination
                                }
                            } else {
                                Routes.StudentRegisterScreen.destination
                            }
                        }
                        User.Admin.name.lowercase() -> {
                            if ( userIsCurrentlyLoggedIn && LOGGED_IN_USER!!.isEmailVerified ) {
                                when (state.adminNotInDatabase) {
                                    true -> {
                                        Routes.AdminLoginScreen.destination
                                    }
                                    false -> {
                                        sharedViewModel.addAdmin( state.admin!! )
                                        Graph.ADMIN_HOME
                                    }
                                    else -> {
                                        Routes.WelcomeScreen.destination
                                    }
                                }
                            } else {
                                Routes.AdminLoginScreen.destination
                            }
                        }
                        else -> Routes.WelcomeScreen.destination
                    }

                    MainNavigationGraph(
                        navController = navController,
                        startDestination = startDestination,
                        sharedViewModel = sharedViewModel,
                        welcomeScreenViewModel = welcomeScreenViewModel
                    )
                }
            }
        }
    }
}

//@Composable
//fun UploadImage() {
//    var imageUri by remember {
//        mutableStateOf<Uri?>(null)
//    }
//
//    var uploadedImage by remember {
//        mutableStateOf<Uri?>(null)
//    }
//
//    var isUploading by remember {
//        mutableStateOf<Boolean>(false)
//    }
//
//    val context = LocalContext.current
//    val storage = FirebaseStorage.getInstance()
//    val storageRef = storage.reference
//    val scope = rememberCoroutineScope()
//
//    val galleryLauncher = rememberLauncherForActivityResult(
//        contract = ActivityResultContracts.GetContent(),
//        onResult = { uri ->
//            uri?.let {
//                imageUri = it
//                isUploading = true
//                Log.i("MainActivity", "filePath: $it")
//                val reducedImage = context.compressImage( it.toString() )
//                val studentsProfileImagesFolder = storageRef.child("${Constants.STUDENTS_PROFILE_IMAGES_FOLDER}/${System.currentTimeMillis()}")
//                scope.launch( Dispatchers.IO ) {
//                    try {
//                        val uploadTask = studentsProfileImagesFolder.putBytes(reducedImage).await()
//                        val uploadedImg = uploadTask.storage.downloadUrl.await()
//                        uploadedImage = uploadedImg
//                        isUploading = false
//                    } catch ( ex: Exception ) {
//                        isUploading = false
//                        Log.d("MainActivity", "Failed to upload image! $ex")
//                    }
//                }
//            }
//        }
//    )
//
//    Column(
//        modifier = Modifier.padding(20.dp),
//        verticalArrangement = Arrangement.Center
//    ) {
//        Button(onClick = {
//            galleryLauncher.launch("image/*")
//        }, enabled = !isUploading) {
//            Text(text = "Select Image")
//        }
//        imageUri?.let {
//            Column {
//                Text(text = "Image from external storage")
//                Image(
//                    painter = rememberAsyncImagePainter(model = imageUri),
//                    contentDescription = null,
//                    modifier = Modifier
//                        .clip(RoundedCornerShape(10.dp))
//                        .fillMaxWidth()
//                        .height(350.dp),
//                    contentScale = ContentScale.Fit
//                )
//            }
//        }
//
//        uploadedImage?.let {
//            Column {
//                Text(text = "The compressed uploaded image")
//                Image(
//                    painter = rememberAsyncImagePainter(model = imageUri),
//                    contentDescription = null,
//                    modifier = Modifier
//                        .clip(RoundedCornerShape(10.dp))
//                        .fillMaxWidth()
//                        .height(350.dp),
//                    contentScale = ContentScale.Fit
//                )
//            }
//        }
//    }
//}
