package com.example.proyectofinaldelivery.repositories

import com.example.proyectofinaldelivery.api.ApiService
import com.example.proyectofinaldelivery.models.OrderItem
import com.example.proyectofinaldelivery.models.OrderRequest
import com.example.proyectofinaldelivery.models.OrderResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object OrderRepository {
    private val apiService: ApiService = RetrofitRepository.getRetrofitInstance().create(ApiService::class.java)

    // Método para obtener los pedidos libres
    fun getFreeOrders(token: String, callback: (List<OrderResponse>?, String?) -> Unit) {
        apiService.getFreeOrders("Bearer $token").enqueue(object : Callback<List<OrderResponse>> {
            override fun onResponse(call: Call<List<OrderResponse>>, response: Response<List<OrderResponse>>) {
                if (response.isSuccessful) {
                    callback(response.body(), null)
                } else {
                    callback(null, "Error al obtener los pedidos libres: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<List<OrderResponse>>, t: Throwable) {
                callback(null, t.message)
            }
        })
    }

    // Método para obtener los detalles de un pedido
    fun getOrderDetails(orderId: Int, token: String, callback: (OrderResponse?, String?) -> Unit) {
        apiService.getOrderDetails(orderId, "Bearer $token").enqueue(object : Callback<OrderResponse> {
            override fun onResponse(call: Call<OrderResponse>, response: Response<OrderResponse>) {
                if (response.isSuccessful) {
                    callback(response.body(), null)
                } else {
                    callback(null, "Error al obtener los detalles del pedido: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<OrderResponse>, t: Throwable) {
                callback(null, t.message)
            }
        })
    }

    // Método para aceptar un pedido
    fun acceptOrder(orderId: Int, token: String, callback: (OrderResponse?, String?) -> Unit) {
        apiService.acceptOrder(orderId, "Bearer $token").enqueue(object : Callback<OrderResponse> {
            override fun onResponse(call: Call<OrderResponse>, response: Response<OrderResponse>) {
                if (response.isSuccessful) {
                    callback(response.body(), null)
                } else {
                    callback(null, "Error al aceptar el pedido: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<OrderResponse>, t: Throwable) {
                callback(null, t.message)
            }
        })
    }
    // Función para marcar el pedido como en camino
    fun markOrderAsOnTheWay(orderId: Int, token: String, callback: (OrderResponse?, String?) -> Unit) {
        apiService.markOrderAsOnTheWay(orderId, "Bearer $token").enqueue(object : Callback<OrderResponse> {
            override fun onResponse(call: Call<OrderResponse>, response: Response<OrderResponse>) {
                if (response.isSuccessful) {
                    callback(response.body(), null)
                } else {
                    callback(null, "Error al marcar como en camino: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<OrderResponse>, t: Throwable) {
                callback(null, t.message)
            }
        })
    }

    // Función para marcar el pedido como entregado
    fun markOrderAsDelivered(orderId: Int, token: String, callback: (OrderResponse?, String?) -> Unit) {
        apiService.markOrderAsDelivered(orderId, "Bearer $token").enqueue(object : Callback<OrderResponse> {
            override fun onResponse(call: Call<OrderResponse>, response: Response<OrderResponse>) {
                if (response.isSuccessful) {
                    callback(response.body(), null)
                } else {
                    callback(null, "Error al marcar como entregado: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<OrderResponse>, t: Throwable) {
                callback(null, t.message)
            }
        })
    }
    fun getAcceptedOrders(token: String, callback: (List<OrderResponse>?, String?) -> Unit) {
        apiService.getAcceptedOrders("Bearer $token").enqueue(object : Callback<List<OrderResponse>> {
            override fun onResponse(call: Call<List<OrderResponse>>, response: Response<List<OrderResponse>>) {
                if (response.isSuccessful) {
                    callback(response.body(), null)
                } else {
                    callback(null, "Error al obtener los pedidos aceptados: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<List<OrderResponse>>, t: Throwable) {
                callback(null, t.message)
            }
        })
    }

}

