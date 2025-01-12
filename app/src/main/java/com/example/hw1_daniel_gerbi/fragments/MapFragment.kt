package com.example.hw1_daniel_gerbi.fragments

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng

class MapFragment : SupportMapFragment() {

    fun zoom(lat: Double, lon: Double) {
        val location = LatLng(lat, lon)
        getMapAsync { googleMap ->
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(location, 15f))
        }
    }
}
