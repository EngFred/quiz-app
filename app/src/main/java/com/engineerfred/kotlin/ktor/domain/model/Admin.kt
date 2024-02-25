package com.engineerfred.kotlin.ktor.domain.model

data class Admin(
    val id: String, //will be generated by firebase after a successful registration
    val lastName: String,
    val firstName: String,
    val email: String,
    val password: String,
    val addedBy: String? = null,
    val isDeleted: Boolean = false
) {
    constructor() : this(
        id = "",
        lastName ="",
        firstName ="",
        email = "",
        password = ""
    )
}