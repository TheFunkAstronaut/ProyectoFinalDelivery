package com.example.proyectofinaldelivery.repositories

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.example.proyectofinaldelivery.api.ApiService
import com.example.proyectofinaldelivery.models.LoginRequest
import com.example.proyectofinaldelivery.models.LoginResponse
import com.example.proyectofinaldelivery.models.ProfileResponse
import com.example.proyectofinaldelivery.models.RegisterRequest
import com.example.proyectofinaldelivery.models.RegisterResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object UserRepository {
    private lateinit var sharedPreferences: SharedPreferences

    fun init(context: Context) {
        sharedPreferences = context.getSharedPreferences("UserPreferences", Context.MODE_PRIVATE)
    }

    fun login(credentials: LoginRequest, onSuccess: (LoginResponse) -> Unit, onError: (Throwable) -> Unit) {
        val apiService = RetrofitRepository.getRetrofitInstance().create(ApiService::class.java)
        apiService.login(credentials).enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful) {
                    response.body()?.let { loginResponse ->
                        // Obtener el token del login
                        val token = loginResponse.access_token
                        saveAccessToken(token)

                        // Llamar al endpoint /me para obtener el perfil
                        apiService.getProfile("Bearer $token").enqueue(object : Callback<ProfileResponse> {
                            override fun onResponse(call: Call<ProfileResponse>, response: Response<ProfileResponse>) {
                                if (response.isSuccessful) {
                                    response.body()?.let { profile ->
                                        if (profile.profile.role == 2) {
                                            // El usuario tiene rol de repartidor
                                            onSuccess(loginResponse)
                                        } else {
                                            onError(Throwable("Solo los repartidores pueden iniciar sesión"))
                                        }
                                    }
                                } else {
                                    onError(Throwable("Error al obtener el perfil: ${response.code()}"))
                                }
                            }

                            override fun onFailure(call: Call<ProfileResponse>, t: Throwable) {
                                onError(t)
                            }
                        })
                    }
                } else {
                    onError(Throwable("Error al iniciar sesión: ${response.code()}"))
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                onError(t)
            }
        })
    }



    fun register(user: RegisterRequest, onSuccess: () -> Unit, onError: (Throwable) -> Unit) {
        val apiService = RetrofitRepository.getRetrofitInstance().create(ApiService::class.java)
        apiService.register(user).enqueue(object : Callback<RegisterResponse> {
            override fun onResponse(call: Call<RegisterResponse>, response: Response<RegisterResponse>) {
                if (response.isSuccessful) {
                    onSuccess()
                } else {
                    onError(Throwable("Error al registrar usuario"))
                }
            }

            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                onError(t)
            }
        })
    }

    private fun saveAccessToken(token: String) {
        sharedPreferences.edit().putString("ACCESS_TOKEN", token).apply()
    }

}