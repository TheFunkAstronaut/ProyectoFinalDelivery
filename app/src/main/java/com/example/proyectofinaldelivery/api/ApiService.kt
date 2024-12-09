package com.example.proyectofinaldelivery.api


import com.example.proyectofinaldelivery.models.LoginRequest
import com.example.proyectofinaldelivery.models.LoginResponse
import com.example.proyectofinaldelivery.models.OrderResponse
import com.example.proyectofinaldelivery.models.ProfileResponse
import com.example.proyectofinaldelivery.models.RegisterRequest
import com.example.proyectofinaldelivery.models.RegisterResponse
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {
    @POST("users/login")
    fun login(@Body credentials: LoginRequest): Call<LoginResponse>

    @POST("users")
    fun register(@Body user: RegisterRequest): Call<RegisterResponse>

    @GET("me")
    fun getProfile(@Header("Authorization") token: String): Call<ProfileResponse>

    @GET("orders/free")
    fun getFreeOrders(@Header("Authorization") token: String): Call<List<OrderResponse>>

    @GET("orders/{id}")
    fun getOrderDetails(@Path("id") orderId: Int, @Header("Authorization") token: String): Call<OrderResponse>

    @POST("orders/{id}/accept")
    fun acceptOrder(@Path("id") orderId: Int, @Header("Authorization") token: String): Call<OrderResponse>

    @POST("orders/{id}/omw")
    fun markOrderAsOnTheWay(@Path("id") orderId: Int, @Header("Authorization") token: String): Call<OrderResponse>

    @POST("orders/{id}/delivered")
    fun markOrderAsDelivered(@Path("id") orderId: Int, @Header("Authorization") token: String): Call<OrderResponse>

    @GET("drivers/orders")
    fun getAcceptedOrders(@Header("Authorization") token: String): Call<List<OrderResponse>>

}
