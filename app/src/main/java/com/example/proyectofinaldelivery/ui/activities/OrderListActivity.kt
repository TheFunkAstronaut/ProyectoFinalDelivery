package com.example.proyectofinaldelivery.ui.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
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
import com.example.proyectofinaldelivery.ui.adapters.OrdersAdapter

class OrderListActivity : AppCompatActivity() {
    private lateinit var ordersAdapter: OrdersAdapter
    private val orders = mutableListOf<OrderResponse>()
    private lateinit var botonAceptados: Button // Declarar como lateinit

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_list)

        // Inicializar botonAceptados después de setContentView
        botonAceptados = findViewById(R.id.btnVerOrdenes)
        setupEventClickListeners()
        setupRecyclerView()
        fetchOrders()
    }

    private fun setupEventClickListeners() {
        botonAceptados.setOnClickListener {
            val token = getSharedPreferences("UserPreferences", MODE_PRIVATE).getString("ACCESS_TOKEN", "")
            if (!token.isNullOrEmpty()) {
                val intent = Intent(this, OrderAcceptedActivity::class.java).apply {
                    putExtra("TOKEN", token)
                }
                startActivity(intent)
            } else {
                Toast.makeText(this, "Token no disponible", Toast.LENGTH_SHORT).show()
            }
        }
    }


    private fun setupRecyclerView() {
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerViewOrders)
        recyclerView.layoutManager = LinearLayoutManager(this)
        ordersAdapter = OrdersAdapter(orders) { order ->
            val intent = Intent(this, OrderDetailsActivity::class.java).apply {
                putExtra("ORDER_ID", order.id)
                putExtra(
                    "TOKEN",
                    getSharedPreferences("UserPreferences", MODE_PRIVATE)
                        .getString("ACCESS_TOKEN", "")
                )
            }
            startActivity(intent)
        }
        recyclerView.adapter = ordersAdapter
    }

    private fun fetchOrders() {
        val token =
            getSharedPreferences("UserPreferences", MODE_PRIVATE).getString("ACCESS_TOKEN", null)
        if (token.isNullOrEmpty()) {
            Toast.makeText(this, "Token inválido", Toast.LENGTH_SHORT).show()
            return
        }

        OrderRepository.getFreeOrders(token) { fetchedOrders, error ->
            if (fetchedOrders != null) {
                val filteredOrders = fetchedOrders.filter { it.status == "1" }
                orders.clear()
                orders.addAll(filteredOrders)
                ordersAdapter.notifyDataSetChanged()
            } else {
                Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
            }
        }
    }
}

