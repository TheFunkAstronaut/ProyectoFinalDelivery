package com.example.proyectofinaldelivery.models

data class ProfileResponse(
    val id: Int,
    val name: String,
    val email: String,
    val profile: ProfileDetails
)