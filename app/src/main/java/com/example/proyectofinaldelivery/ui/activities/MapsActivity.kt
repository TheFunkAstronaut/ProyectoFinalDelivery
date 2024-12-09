package com.example.proyectofinaldelivery.ui.activities

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.core.app.ActivityCompat

import com.example.proyectofinaldelivery.R

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.example.proyectofinaldelivery.databinding.ActivityMapsBinding
import com.example.proyectofinaldelivery.models.OrderResponse
import com.example.proyectofinaldelivery.repositories.OrderRepository
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.Marker


class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var currentLocation: LatLng
    private lateinit var restaurantLocation: LatLng
    private lateinit var customerLocation: LatLng
    private lateinit var deliveryMarker: Marker

    private val REQUEST_CODE_LOCATION_PERMISSION = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)

        val orderId = intent.getIntExtra("ORDER_ID", -1)
        val token = intent.getStringExtra("TOKEN") ?: ""

        // Llamar a la API para obtener detalles del pedido
        if (orderId != -1 && token.isNotEmpty()) {
            fetchOrderDetails(orderId, token)
        }

        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        findViewById<Button>(R.id.btnOmw).setOnClickListener {
            // Llamar a la API para marcar el pedido como "En camino"
            if (orderId != -1 && token.isNotEmpty()) {
                OrderRepository.markOrderAsOnTheWay(orderId, token) { _, error ->
                    if (error != null) {
                        Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this, "Pedido en camino", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        findViewById<Button>(R.id.btnDelivered).setOnClickListener {
            // Llamar a la API para marcar el pedido como "Entregado"
            if (orderId != -1 && token.isNotEmpty()) {
                OrderRepository.markOrderAsDelivered(orderId, token) { _, error ->
                    if (error != null) {
                        Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this, "Pedido entregado", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun fetchOrderDetails(orderId: Int, token: String) {
        OrderRepository.getOrderDetails(orderId, token) { orderResponse, error ->
            if (orderResponse != null) {
                customerLocation = LatLng(orderResponse.latitude.toDouble(), orderResponse.longitude.toDouble())
                restaurantLocation = LatLng(-17.783885296698, -63.18463245704563) // Ubicación fija del restaurante

                if (::mMap.isInitialized) {
                    addMarkers()
                    findViewById<Button>(R.id.btnOmw).visibility = View.VISIBLE  // Hacer visible el botón "Estoy en camino"
                }
            } else {
                Toast.makeText(this, "Error al cargar el pedido: $error", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.uiSettings.isZoomControlsEnabled = true

        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            getCurrentLocation { location ->
                currentLocation = LatLng(location.latitude, location.longitude)
                mMap.isMyLocationEnabled = true
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 15f))

                if (::customerLocation.isInitialized && ::restaurantLocation.isInitialized) {
                    addMarkers()
                }
            }
        } else {
            requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_CODE_LOCATION_PERMISSION)
        }
    }

    private fun addMarkers() {
        // Añadir marcadores sin borrar los anteriores
        mMap.addMarker(MarkerOptions().position(customerLocation).title("Cliente").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)))
        val restaurantMarker = mMap.addMarker(MarkerOptions().position(restaurantLocation).title("Restaurante").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)))
        deliveryMarker = mMap.addMarker(MarkerOptions().position(currentLocation).title("Tú").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)))!!

        val boundsBuilder = LatLngBounds.Builder()
        boundsBuilder.include(customerLocation)
        boundsBuilder.include(restaurantLocation)
        boundsBuilder.include(currentLocation)
        mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(boundsBuilder.build(), 100))
    }

    private fun getCurrentLocation(callback: (Location) -> Unit) {
        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                if (location != null) {
                    callback(location)
                } else {
                    Toast.makeText(this, "No se pudo obtener la ubicación actual", Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_CODE_LOCATION_PERMISSION)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_LOCATION_PERMISSION && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            getCurrentLocation { location ->
                // Procesar la ubicación aquí si el usuario otorgó el permiso
            }
        } else {
            Toast.makeText(this, "Permiso de ubicación denegado", Toast.LENGTH_SHORT).show()
        }
    }
}






