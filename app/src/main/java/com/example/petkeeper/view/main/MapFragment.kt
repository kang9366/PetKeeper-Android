package com.example.petkeeper.view.main

import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.annotation.SuppressLint
import android.content.Context
import android.content.Context.LOCATION_SERVICE
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.example.petkeeper.R
import com.example.petkeeper.databinding.FragmentMapBinding
import com.example.petkeeper.util.binding.BindingFragment
import net.daum.mf.map.api.MapPoint
import net.daum.mf.map.api.MapView

class MapFragment : BindingFragment<FragmentMapBinding>(R.layout.fragment_map, true) {
    private lateinit var context: Context

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 123
    }

    override fun onAttach(p0: Context) {
        super.onAttach(p0)
        context = p0 as MainActivity
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if(isLocationPermissionGranted()){
//            startTracking()
        }else{
            requestPermission.launch(ACCESS_FINE_LOCATION)
        }

        binding?.button?.setOnClickListener {
            startTracking()
//            stopTracking()
        }

//        getMapData()
    }

    private  val requestPermission = registerForActivityResult(
        ActivityResultContracts.RequestPermission()) {
        when(it) {
            true -> { Toast.makeText(context,"권한 허가",Toast.LENGTH_SHORT).show()}
            false -> {
                Toast.makeText(context,"권한 거부",Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun isLocationPermissionGranted(): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    val locationListener: LocationListener = object: LocationListener{
        override fun onLocationChanged(p0: Location) {
            val uLatitude = p0.latitude
            val uLongitude = p0.longitude
            Log.d("location data", uLatitude.toString())
            Log.d("location data", uLongitude.toString())
        }

        override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
            super.onStatusChanged(provider, status, extras)
        }

        override fun onProviderEnabled(provider: String) {
            super.onProviderEnabled(provider)
        }

        override fun onProviderDisabled(provider: String) {
            super.onProviderDisabled(provider)
        }
    }

    // 현재 사용자 위치추적
    @SuppressLint("MissingPermission")
    private fun startTracking() {
//        binding?.mapView?.currentLocationTrackingMode =
//            MapView.CurrentLocationTrackingMode.TrackingModeOnWithoutHeading
        val lm: LocationManager = context.getSystemService(LOCATION_SERVICE) as LocationManager
        lm.requestLocationUpdates(
            LocationManager.NETWORK_PROVIDER,
            2 * 60 * 1000,
            10f,
            locationListener
        )
    }

    // 위치추적 중지
//    private fun stopTracking() {
//        binding?.mapView?.currentLocationTrackingMode = MapView.CurrentLocationTrackingMode.TrackingModeOff
//    }

    override fun onDestroy() {
        super.onDestroy()
//        stopTracking()
    }
}