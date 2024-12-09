package com.example.proyectofinaldelivery.models

data class OrderItem(
    val id: Int,
    val quantity: Int,
    val price: String,
    val product: Product
)