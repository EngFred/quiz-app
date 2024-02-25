package com.engineerfred.kotlin.ktor.util

import androidx.datastore.preferences.core.stringPreferencesKey
import com.google.firebase.auth.FirebaseAuth

object Constants {

    const val ADMINS_COLLECTION = "admins"
    const val STUDENTS_COLLECTION = "students"
    const val QUESTIONS_COLLECTION = "questions"

    const val STUDENTS_PROFILE_IMAGES_FOLDER = "students_profile_image"

    val LOGGED_IN_USER_EMAIL = FirebaseAuth.getInstance().currentUser?.email
    val LOGGED_IN_USER = FirebaseAuth.getInstance().currentUser
    val LOGGED_IN_USER_ID = FirebaseAuth.getInstance().currentUser?.uid
    val userIsCurrentlyLoggedIn = FirebaseAuth.getInstance().currentUser != null

    val USER_KEY = stringPreferencesKey("USER_KEY")

    const val WRONG_EMAIL_OR_PASSWORD_EXCEPTION = "The provided email and password don't match any admin."

}