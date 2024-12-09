package com.example.proyectofinaldelivery.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.proyectofinaldelivery.R
import com.example.proyectofinaldelivery.models.OrderResponse

class OrdersAdapter(
    private val orders: List<OrderResponse>,
    private val onClick: (OrderResponse) -> Unit // Lambda que se ejecutará cuando se haga clic en un pedido
) : RecyclerView.Adapter<OrdersAdapter.OrderViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_orders, parent, false)
        return OrderViewHolder(view)
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        val order = orders[position]
        holder.bind(order)
    }

    override fun getItemCount(): Int = orders.size

    inner class OrderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val orderIdTextView: TextView = itemView.findViewById(R.id.txtOrderId)
        private val totalTextView: TextView = itemView.findViewById(R.id.txtTotal)
        private val statusTextView: TextView = itemView.findViewById(R.id.txtStatus)

        fun bind(order: OrderResponse) {
            orderIdTextView.text = "Pedido ID: #${order.id}"
            totalTextView.text = "Total: $${order.total}"
            statusTextView.text = "Estado: ${mapStatusToText(order.status)}"

            // Asignar el clic a la vista del item
            itemView.setOnClickListener {
                onClick(order) // Ejecuta el lambda con el pedido
            }
        }
    }

    private fun mapStatusToText(status: String): String {
        return when (status) {
            "1" -> "Disponible"
            "2" -> "NO DISPONIBLE"
            "3" -> "XD"
            else -> "Desconocido"
        }
    }
}
