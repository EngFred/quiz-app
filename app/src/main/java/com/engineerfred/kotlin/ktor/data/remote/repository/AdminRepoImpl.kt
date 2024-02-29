package com.engineerfred.kotlin.ktor.data.remote.repository

import android.util.Log
import com.engineerfred.kotlin.ktor.domain.model.Admin
import com.engineerfred.kotlin.ktor.domain.repository.AdminRepository
import com.engineerfred.kotlin.ktor.util.Constants.ADMINS_COLLECTION
import com.engineerfred.kotlin.ktor.util.Constants.LOGGED_IN_USER_EMAIL
import com.engineerfred.kotlin.ktor.util.Constants.WRONG_EMAIL_OR_PASSWORD_EXCEPTION
import com.engineerfred.kotlin.ktor.util.Response
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.util.concurrent.CancellationException
import javax.inject.Inject

class AdminRepoImpl @Inject constructor (
    private val db: FirebaseFirestore,
    private val auth: FirebaseAuth
) : AdminRepository {

    companion object {
        val TAG = AdminRepoImpl::class.simpleName
    }

    override suspend fun registerAdmin(admin: Admin):  Response<FirebaseUser> {
        return withContext( NonCancellable ) {
            try {
                val authResult = auth.createUserWithEmailAndPassword( admin.email, admin.password ).await()
                val user = authResult.user!!
                user.sendEmailVerification().await()
                val authenticatedAdmin = admin.copy( id = user.uid )
                db.collection( ADMINS_COLLECTION ).document(authenticatedAdmin.id).set( authenticatedAdmin ).await()
                Log.d(TAG, "Admin with email ${admin.email} has been registered successfully!")
                Response.Success(user)
            }catch (ex: Exception ) {
                Log.d(TAG, "Failed to register admin! $ex")
                Response.Error("$ex")
            }
        }
    }

    override suspend fun loginAdmin(email: String, password: String): Response<Admin?> {
        return try {
            val authResult = auth.signInWithEmailAndPassword( email, password ).await()
            val user = authResult?.user
            if (user?.isEmailVerified == true) {
                Log.d(TAG, "Admin login has been logged successfully!")
                val admin = checkIfAdminExistsInDatabase( user.uid )
                return when ( admin ) {
                    is Response.Error -> {
                        Response.Error(admin.errorMessage)
                    }
                    is Response.Success -> {
                        if ( admin.data != null ) Response.Success(admin.data) else Response.Success(null )
                    }
                    Response.Undefined -> Response.Undefined
                }
            } else {
                Response.Error("Email for this account was not verified!")
            }
        }catch (ex: Exception ) {
            if ( ex is CancellationException ) throw ex
            if ( ex is FirebaseAuthInvalidCredentialsException ) return Response.Error(WRONG_EMAIL_OR_PASSWORD_EXCEPTION)
            if ( ex is FirebaseAuthUserCollisionException ) return Response.Error("The provide email ia already in use by another account!")
            if ( ex is FirebaseException ) return Response.Error("Unable to connect to server.\n Check that you have an active internet connection then try again.")
            Log.d(TAG, "Failed to login admin! $ex")
            Response.Error("$ex")
        }
    }

    override suspend fun deleteAdmin(admin: Admin): Response<Any> {
        return try {
            if ( admin.addedBy == LOGGED_IN_USER_EMAIL ) {
                val result = db.collection( ADMINS_COLLECTION ).document( admin.id ).update( mapOf("isDeleted" to true) ).await() //the admin will be deleted when they try to login into the app since there is no direct way of deleting an admin who is not authenticated
                Log.d(TAG, "Admin has been marked as deleted!")
                return Response.Success(result)
            } else {
                Log.d(TAG, "Can't delete admin since it's not you who added them!")
                return Response.Error("Can't delete admin since it's not you who added them!")
            }
        }catch (ex: Exception ) {
            if ( ex is CancellationException ) throw ex
            Log.d(TAG, "Failed to mark admin as deleted! $ex")
            Response.Error("$ex")
        }
    }

    override fun getAllAdmins(): Flow<Response<List<Admin>>> {
        return callbackFlow {
            val snapshot = db.collection( ADMINS_COLLECTION ).addSnapshotListener { value, error ->
                if ( error != null ) {
                    Log.v(TAG, "Snapshot error: $error")
                    return@addSnapshotListener
                }
                if( value != null ) {
                    Log.v(TAG, "Received an admin snapshot")
                    trySend(Response.Success(value.toObjects( Admin::class.java)))
                }
            }
            awaitClose { snapshot.remove() }
        }.catch {
            Log.d(TAG, "Failed to get admins! $it")

            emit( Response.Success(emptyList()) )
        }.flowOn( Dispatchers.IO )
    }

    override suspend fun updateAdmin(admin: Admin): Response<Any> {
        //only admins can update themselves
        return try {
            val result = db.collection( ADMINS_COLLECTION ).document( admin.id ).update(
                mapOf(
                    "lastName" to admin.lastName,
                    "firstName" to admin.firstName
                )
            ).await()
            Response.Success(result)
        }catch ( ex: Exception ) {
            if ( ex is CancellationException ) throw ex
            Log.d(TAG, "Error while updating admin! $ex")
            Response.Error("$ex")
        }
    }

    override suspend fun checkIfAdminExistsInDatabase( id: String ): Response<Admin?> {
        //this function is called after auth.currentUser.isEmailVerified = true
        return try {
            val ref = db.collection( ADMINS_COLLECTION ).document( id )
            val snapshot = ref.get().await()
            if ( snapshot.exists() ) {
                Log.d(TAG, "Admin exists in the database!")
                val admin = snapshot.toObject( Admin::class.java )
                if ( admin != null ) {
                    if ( admin.isDeleted ) {
                        Log.d(TAG, "But was deleted!")
                        db.collection( ADMINS_COLLECTION ).document( id ).delete().await()
                        auth.currentUser?.delete()?.await()
                        Response.Success(null )
                    }
                    Response.Success( admin )
                }else {
                    Response.Success( null )
                }
            } else {
                auth.currentUser?.delete()?.await()
                Response.Success( null )
            }
        } catch ( ex: Exception ) {
            if ( ex is CancellationException ) throw ex
            Log.d(TAG, "Error while checking if the admin exists in the database! $ex")
            Response.Error("$ex")
        }
    }

    override suspend fun logoutAdmin(): Response<Any> {
        return try {
            val result = auth.signOut()
            Response.Success(result)
        }catch ( ex: Exception ) {
            if ( ex is CancellationException ) throw ex
            Log.d(TAG, "Error while logging out admin! $ex")
            Response.Error("$ex")
        }
    }

}