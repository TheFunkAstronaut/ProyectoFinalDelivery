package com.example.proyectofinaldelivery.models

data class RegisterResponse(
    val id: Int,
    val name: String,
    val email: String,
    val profile: Profile
)