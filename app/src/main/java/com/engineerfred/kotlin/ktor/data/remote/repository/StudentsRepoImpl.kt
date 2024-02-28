package com.engineerfred.kotlin.ktor.data.remote.repository

import android.app.Activity
import android.content.Context
import android.util.Log
import com.engineerfred.kotlin.ktor.domain.model.Student
import com.engineerfred.kotlin.ktor.domain.repository.StudentsRepository
import com.engineerfred.kotlin.ktor.ui.model.Subject
import com.engineerfred.kotlin.ktor.util.Constants.STUDENTS_COLLECTION
import com.engineerfred.kotlin.ktor.util.Constants.STUDENTS_PROFILE_IMAGES_FOLDER
import com.engineerfred.kotlin.ktor.util.Response
import com.engineerfred.kotlin.ktor.util.compressImage
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthMissingActivityForRecaptchaException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.util.concurrent.CancellationException
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class StudentsRepoImpl @Inject constructor(
    private val db: FirebaseFirestore,
    private val auth: FirebaseAuth,
    private val storage: FirebaseStorage
) : StudentsRepository {

    companion object {
        private val TAG = StudentsRepoImpl::class.java.simpleName
    }

    private val storageRef = storage.reference

    override suspend fun verifyStudentPhoneNumber(phoneNumber: String, activity: Activity): Response<String> {
        return try {
            val responseChannel = Channel<Response<String>>() // Channel to communicate the response

            val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

                override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                    Log.d(TAG, "onVerificationCompleted:$credential")
                    //signInStudentWithAuthCredentials(credential)
                }

                override fun onVerificationFailed(e: FirebaseException) {
                    val errorMessage = when (e) {
                        is FirebaseAuthInvalidCredentialsException -> {
                            Log.d(TAG, "Invalid request")
                            "Invalid request! Could be the phone number is badly formatted."
                        }
                        is FirebaseTooManyRequestsException -> {
                            Log.d(TAG, "The SMS quota for the project has been exceeded")
                            "The SMS quota for the project has been exceeded."
                        }
                        is FirebaseAuthMissingActivityForRecaptchaException -> {
                            Log.d(TAG, "reCAPTCHA verification attempted with null Activity")
                            "reCAPTCHA verification attempted with null Activity."
                        }
                        else -> {
                            Log.d(TAG, "$e")
                            "Phone verification failed!"
                        }
                    }
                    responseChannel.trySend(Response.Error(errorMessage))
                }

                override fun onCodeSent(
                    verificationId: String,
                    token: PhoneAuthProvider.ForceResendingToken,
                ) {
                    Log.d(TAG, "onCodeSent:$verificationId")
                    responseChannel.trySend( Response.Success(verificationId) )
                }
            }

            val options = PhoneAuthOptions.newBuilder(auth)
                .setPhoneNumber(phoneNumber)
                .setTimeout(60L, TimeUnit.SECONDS)
                .setActivity(activity) // Activity (for callback binding)
                .setCallbacks(callbacks)
                .build()

            PhoneAuthProvider.verifyPhoneNumber(options)

            // Wait for response from the channel
            responseChannel.receive()
        }catch ( ex: Exception ) {
            if ( ex is CancellationException) throw ex
            Log.d(TAG, "Failed to register student! $ex")
            Response.Error("$ex")
        }
    }

    override suspend fun signInStudentWithAuthCredentials( credential: PhoneAuthCredential ): Response<Any> {
        return try {
            val task = auth.signInWithCredential(credential).await()
            Response.Success(task)
        }catch (ex: Exception) {
            if ( ex is CancellationException) throw ex
            if (ex is FirebaseAuthInvalidCredentialsException) {
                Response.Error("The verification code entered was invalid")
            }
            Log.d(TAG, "Failed to sign in student with auth credentials! $ex")
            Response.Error("$ex")
        }

    }

    override suspend fun deleteStudent(studentId: String): Response<Any> {
        //deletes students profile
        return try {
            withContext(NonCancellable) {
                db.collection( STUDENTS_COLLECTION ).document( studentId ).delete().await()
                val result = auth.currentUser?.delete()!!.await()
                Response.Success(result)
            }
        } catch ( ex: Exception ) {
            if (  ex is CancellationException ) throw  ex
            Log.v(TAG, "Error deleting student's profile! $ex")
            Response.Error("$ex")
        }
    }

    override fun getAllStudents(): Flow<Response<List<Student>>> {
        return callbackFlow {
            val snapshot = db.collection(STUDENTS_COLLECTION).addSnapshotListener { value, error ->
                if ( error != null ) {
                    Log.v(TAG, "Snapshot error: $error")
                    return@addSnapshotListener
                }
                if( value != null ) {
                    Log.v(TAG, "Received a student snapshot")
                    val students = value.toObjects( Student::class.java )
                    trySend(Response.Success(students))
                }
            }
            awaitClose { snapshot.remove() }
        }.catch {
            Log.d(TAG, "Failed to get students! $it")
            emit( Response.Success(emptyList()) )
        }.flowOn( Dispatchers.IO )
    }

    override suspend fun updateStudent(student: Student, context: Context): Response<Any> {
        return try {
            if ( student.profileImage != null ) {
                if ( student.profileImage.toString().contains("firebase") ) {
                    val result = db.collection( STUDENTS_COLLECTION ).document( student.id ).update(
                        mapOf(
                            "about" to student.about,
                            "name" to student.name
                        )
                    ).await()
                    Response.Success(result)
                } else {
                    val studentsProfileImagesFolder = storageRef.child("${STUDENTS_PROFILE_IMAGES_FOLDER}/${System.currentTimeMillis()}")
                    val compressedImage = context.compressImage( student.profileImage  )
                    val uploadTask = studentsProfileImagesFolder.putBytes( compressedImage ).await()
                    val uploadedCompressedImage = uploadTask.storage.downloadUrl.await()
                    val result = db.collection( STUDENTS_COLLECTION ).document( student.id ).update(
                        mapOf(
                            "profileImage" to uploadedCompressedImage,
                            "about" to student.about,
                            "name" to student.name
                        )
                    ).await()
                    Response.Success(result)
                }
            } else {
                val result = db.collection( STUDENTS_COLLECTION ).document( student.id ).update(
                    mapOf(
                        "about" to student.about,
                        "name" to student.name
                    )
                ).await()
                Response.Success(result)
            }
        }catch ( ex: Exception ) {
            if ( ex is CancellationException ) throw  ex
            Log.i(TAG, "Error updating student! $ex")
            Response.Error("$ex")
        }
    }

    override suspend fun logoutStudent(): Response<Any> {
        return try {
            val result = auth.signOut()
            Response.Success(result)
        }catch ( ex: Exception ) {
            if ( ex is CancellationException ) throw  ex
            Log.i(TAG, "Error logging out student! $ex")
            Response.Error("$ex")
        }
    }

    override fun checkIfStudentExistsInDatabase(studentId: String): Flow<Response<Student?>> {
       return callbackFlow {
           val docRef = db.collection( STUDENTS_COLLECTION ).document(studentId)
           val snapShot = docRef.addSnapshotListener { value, error ->
               if ( error != null ) {
                   Log.v(TAG, "$error")
                   return@addSnapshotListener
               }
               if ( value != null ) {
                   val student = value.toObject(Student::class.java)
                   if ( student != null ) {
                       trySend( Response.Success(student) )
                   } else {
                       trySend( Response.Success(null) )
                   }
               }
           }
           awaitClose { snapShot.remove() }
       }.catch{
           if ( it is CancellationException ) throw  it
           Log.i(TAG, "Error checking if the student exists in the database! $it")
           emit( Response.Success(null) )
       }.flowOn( Dispatchers.IO )
    }

    //a student here is already authenticated
    override suspend fun addStudent(student: Student, context: Context): Response<Any> {
        return try {
            if ( student.profileImage != null ) {
                val studentsProfileImagesFolder = storageRef.child("${STUDENTS_PROFILE_IMAGES_FOLDER}/${System.currentTimeMillis()}")
                val compressedImage = context.compressImage( student.profileImage  )
                val uploadTask = studentsProfileImagesFolder.putBytes( compressedImage  ).await()
                val uploadedCompressedImage = uploadTask.storage.downloadUrl.await()
                val newStudent = student.copy( profileImage = uploadedCompressedImage.toString() )
                val result = db.collection( STUDENTS_COLLECTION ).document( newStudent.id ).set( newStudent ).await()
                Response.Success(result)
            } else {
                val result = db.collection( STUDENTS_COLLECTION ).document( student.id ).set( student ).await()
                Response.Success(result)
            }
        } catch ( ex: Exception ) {
            if ( ex is CancellationException ) throw  ex
            Log.i(TAG, "Error adding student in the database! $ex")
            Response.Error("$ex")
        }
    }

    override suspend fun upgradeStudentLevel(studentId: String, newLevel: String, subject: String): Response<Any> {
        return try {

            withContext(NonCancellable) {
                val map = when (subject) {
                    Subject.English.name -> mapOf("englishLevel" to newLevel )
                    Subject.Mathematics.name -> mapOf("mathLevel" to newLevel )
                    else -> mapOf("englishLevel" to newLevel)
                }

                val task = db.collection( STUDENTS_COLLECTION ).document(studentId).update( map ).await()
                Response.Success(task)
            }

        }catch ( ex: Exception ) {
            if ( ex is CancellationException ) throw  ex
            Log.i(TAG, "Error upgrading student level! $ex")
            Response.Error("$ex")
        }
    }
}