package com.engineerfred.kotlin.ktor.domain.repository

import android.app.Activity
import android.content.Context
import com.engineerfred.kotlin.ktor.domain.model.Student
import com.engineerfred.kotlin.ktor.util.Response
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.PhoneAuthCredential
import kotlinx.coroutines.flow.Flow

interface StudentsRepository {

    suspend fun verifyStudentPhoneNumber( phoneNumber: String, activity: Activity ) : Response<String>
    suspend fun signInStudentWithAuthCredentials( credential: PhoneAuthCredential) : Response<AuthResult>
    suspend fun deleteStudent( studentId: String ) : Response<Any>
    fun getAllStudents() : Flow<Response<List<Student>>>
    suspend fun updateStudent( student: Student, context: Context ) : Response<Any>
    suspend fun upgradeStudentLevel( studentId: String, newLevel: String, subject: String ) : Response<Any>
    suspend fun logoutStudent() : Response<Any>
    suspend fun addStudent(student: Student, context: Context) : Response<Any>
    fun checkIfStudentExistsInDatabase( studentId: String ) : Flow<Response<Student?>> //at the same time getting the student

}