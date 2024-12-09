package com.example.proyectofinaldelivery.ui.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.proyectofinaldelivery.R
import com.example.proyectofinaldelivery.models.OrderResponse
import com.example.proyectofinaldelivery.repositories.OrderRepository
import com.example.proyectofinaldelivery.ui.adapters.OrderAcceptedAdapter

class OrderAcceptedActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: OrderAcceptedAdapter
    private val orders = mutableListOf<OrderResponse>() // Lista de pedidos cargados

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_accepted)

        val token = intent.getStringExtra("TOKEN") ?: ""
        if (token.isEmpty()) {
            Toast.makeText(this, "Token inválido o no recibido", Toast.LENGTH_SHORT).show()
            return
        }

        recyclerView = findViewById(R.id.recyclerViewOrders)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Configurar adaptador con la acción para clics
        adapter = OrderAcceptedAdapter(orders, token) { orderId, token ->
            openMap(orderId, token)
        }
        recyclerView.adapter = adapter

        // Cargar pedidos (esto sería una llamada a tu repositorio o servicio)
        loadOrders(token)
    }

    private fun loadOrders(token: String) {
        OrderRepository.getAcceptedOrders(token) { orderList, error ->
            if (orderList != null) {
                orders.clear()
                orders.addAll(orderList)
                adapter.notifyDataSetChanged()
            } else {
                Toast.makeText(this, "Error al cargar los pedidos: $error", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun openMap(orderId: Int, token: String) {
        val intent = Intent(this, MapsActivity::class.java)
        intent.putExtra("ORDER_ID", orderId)
        intent.putExtra("TOKEN", token)
        startActivity(intent)
    }
}

