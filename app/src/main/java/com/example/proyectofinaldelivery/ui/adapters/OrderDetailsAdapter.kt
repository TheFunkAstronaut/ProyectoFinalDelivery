package com.example.proyectofinaldelivery.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.proyectofinaldelivery.R
import com.example.proyectofinaldelivery.models.OrderItem

class OrderDetailsAdapter(private val orderItems: List<OrderItem>) : RecyclerView.Adapter<OrderDetailsAdapter.OrderDetailsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderDetailsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_order_details, parent, false)
        return OrderDetailsViewHolder(view)
    }

    override fun onBindViewHolder(holder: OrderDetailsViewHolder, position: Int) {
        val orderItem = orderItems[position]
        holder.productName.text = orderItem.product.name
        holder.productDescription.text = orderItem.product.description
        holder.productQuantity.text = "Cantidad: ${orderItem.quantity}"
        Glide.with(holder.itemView.context).load(orderItem.product.image).into(holder.productImage)
    }

    override fun getItemCount(): Int {
        return orderItems.size
    }

    class OrderDetailsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val productName: TextView = itemView.findViewById(R.id.txtProductName)
        val productDescription: TextView = itemView.findViewById(R.id.txtProductDescription)
        val productQuantity: TextView = itemView.findViewById(R.id.txtProductQuantity)
        val productImage: ImageView = itemView.findViewById(R.id.imgProduct)
    }
}

