package com.example.petkeeper.view.main

import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.annotation.SuppressLint
import android.content.Context
import android.content.Context.LOCATION_SERVICE
import android.content.pm.PackageManager
import android.location.Location
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
            startTracking()
        }else{
            requestPermission.launch(ACCESS_FINE_LOCATION)
        }
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

    // 현재 사용자 위치추적
    @SuppressLint("MissingPermission")
    private fun startTracking() {
        binding?.mapView?.currentLocationTrackingMode =
            MapView.CurrentLocationTrackingMode.TrackingModeOnWithoutHeading  //이 부분

        val lm: LocationManager = context.getSystemService(LOCATION_SERVICE) as LocationManager
        val userNowLocation: Location? = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
        //위도 , 경도
        val uLatitude = userNowLocation?.latitude
        val uLongitude = userNowLocation?.longitude
        Log.d("test", uLatitude.toString())
        Log.d("test", uLongitude.toString())
        val uNowPosition = MapPoint.mapPointWithGeoCoord(uLatitude!!, uLongitude!!)

//        // 현 위치에 마커 찍기
//        val marker = MapPOIItem()
//        marker.itemName = "현 위치"
//        marker.mapPoint =uNowPosition
//        marker.markerType = MapPOIItem.MarkerType.CustomImage
//
//        marker.selectedMarkerType = MapPOIItem.MarkerType.RedPin
//        binding.mapView.addPOIItem(marker)
    }

    // 위치추적 중지
    private fun stopTracking() {
        binding?.mapView?.currentLocationTrackingMode = MapView.CurrentLocationTrackingMode.TrackingModeOff
    }

    override fun onDestroy() {
        super.onDestroy()
        stopTracking()
    }
}