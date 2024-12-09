package com.example.proyectofinaldelivery.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.proyectofinaldelivery.R
import com.example.proyectofinaldelivery.models.OrderResponse

class OrderAcceptedAdapter(
    private val orders: List<OrderResponse>,
    private val token: String,
    private val onOrderClick: (Int, String) -> Unit
) : RecyclerView.Adapter<OrderAcceptedAdapter.OrderAcceptedViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderAcceptedViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_order_accepted, parent, false)
        return OrderAcceptedViewHolder(view)
    }

    override fun onBindViewHolder(holder: OrderAcceptedViewHolder, position: Int) {
        val order = orders[position]
        holder.bind(order)

        // Configurar clic en el ítem
        holder.itemView.setOnClickListener {
            onOrderClick(order.id, token)
        }
    }

    override fun getItemCount(): Int = orders.size

    class OrderAcceptedViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val orderIdTextView: TextView = itemView.findViewById(R.id.txtOrderId)
        private val totalTextView: TextView = itemView.findViewById(R.id.txtTotal)

        fun bind(order: OrderResponse) {
            orderIdTextView.text = "Pedido: #${order.id}"
            totalTextView.text = "Total: $${order.total}"
        }
    }
}


