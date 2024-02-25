package com.engineerfred.kotlin.ktor.domain.repository

import com.engineerfred.kotlin.ktor.domain.model.Admin
import com.engineerfred.kotlin.ktor.util.Response
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow

interface AdminRepository {

    suspend fun registerAdmin( admin: Admin ) : Response<FirebaseUser>
    suspend fun deleteAdmin( admin: Admin ) : Response<Any>
    fun getAllAdmins() : Flow<Response<List<Admin>>>
    suspend fun updateAdmin( admin: Admin ) : Response<Any>

    suspend fun loginAdmin( email: String, password: String ) : Response<Admin?>
    suspend fun logoutAdmin() : Response<Any>

    suspend fun checkIfAdminExistsInDatabase( id: String ) : Response<Admin?>

}