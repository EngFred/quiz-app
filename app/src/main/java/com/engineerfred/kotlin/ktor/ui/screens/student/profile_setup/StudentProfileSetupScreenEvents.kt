package com.engineerfred.kotlin.ktor.ui.screens.student.profile_setup


import android.content.Context
import android.net.Uri

sealed class StudentProfileSetupScreenEvents {

    data class ProfileImageUrlChanged(val imageUri: Uri ) :  StudentProfileSetupScreenEvents()
    data class NameChanged( val name: String ) :  StudentProfileSetupScreenEvents()
    data class BioChanged( val bio: String ) :  StudentProfileSetupScreenEvents()
    data class RegisterButtonClicked( val context: Context ) : StudentProfileSetupScreenEvents()

}