package com.example.proyectofinaldelivery.ui.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
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
import com.example.proyectofinaldelivery.ui.adapters.OrderDetailsAdapter

class OrderDetailsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_details)

        val orderId = intent.getIntExtra("ORDER_ID", -1)
        val token = intent.getStringExtra("TOKEN")

        if (orderId != -1 && !token.isNullOrEmpty()) {
            fetchOrderDetails(orderId, token)
        }

        // Configurar el botón de aceptar pedido
        val acceptButton = findViewById<Button>(R.id.btnAceptarPedido)
        acceptButton.setOnClickListener {
            if (orderId != -1 && !token.isNullOrEmpty()) {
                acceptOrder(orderId, token)
            }
        }
    }

    private fun fetchOrderDetails(orderId: Int, token: String) {
        OrderRepository.getOrderDetails(orderId, token) { orderResponse, error ->
            if (orderResponse != null) {
                displayOrderDetails(orderResponse)
            } else {
                Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun displayOrderDetails(orderResponse: OrderResponse) {
        // Mostrar el ID del pedido
        val orderIdTextView = findViewById<TextView>(R.id.textView2)
        orderIdTextView.text = "Pedido: #${orderResponse.id}"

        // Mostrar el total
        val totalValue = findViewById<TextView>(R.id.lblTotalValor)
        totalValue.text = "Total: $${orderResponse.total}"

        // Mostrar la dirección
        val addressTextView = findViewById<TextView>(R.id.lblAddress)
        addressTextView.text = "Dirección: ${orderResponse.address}"

        // Configurar el RecyclerView para los detalles del pedido
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        val adapter = OrderDetailsAdapter(orderResponse.order_details)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
    }

    private fun acceptOrder(orderId: Int, token: String) {
        OrderRepository.acceptOrder(orderId, token) { orderResponse, error ->
            if (orderResponse != null) {
                // Actualizar la interfaz o mostrar un mensaje indicando que el pedido fue aceptado
                Toast.makeText(this, "Pedido aceptado", Toast.LENGTH_SHORT).show()

                // Redirigir a MapsActivity después de aceptar el pedido
                val intent = Intent(this, MapsActivity::class.java)
                intent.putExtra("ORDER_ID", orderId)
                intent.putExtra("TOKEN", token)
                startActivity(intent)
                finish() // Finalizar la actividad actual (opcional)
            } else {
                Toast.makeText(this, "Error al aceptar el pedido: $error", Toast.LENGTH_SHORT).show()
            }
        }
    }
}



