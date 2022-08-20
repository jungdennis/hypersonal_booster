package com.example.hypersonalbooster


import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

import com.example.hypersonalbooster.PermissionUtils.PermissionDeniedDialog.Companion.newInstance
import com.example.hypersonalbooster.PermissionUtils.isPermissionGranted

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

import com.example.hypersonalbooster.databinding.LayoutMapMainBinding
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.Marker
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class KioskActivity : AppCompatActivity(), GoogleMap.OnMyLocationButtonClickListener,
    GoogleMap.OnMyLocationClickListener, OnMapReadyCallback,
    ActivityCompat.OnRequestPermissionsResultCallback, GoogleMap.OnMarkerClickListener {

    private var permissionDenied = false
    private lateinit var Map: GoogleMap
    lateinit var binding : LayoutMapMainBinding

    lateinit var database_data : String

    val database = FirebaseDatabase.getInstance("https://hypersonal-booster-default-rtdb.asia-southeast1.firebasedatabase.app")
    val ref = database.getReference("kiosk")

    //private val oneHeung = LatLng(37.558941,126.998959)
    //private var markeroneHeung: Marker? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val shared = getSharedPreferences("data_kiosk", 0)
        database_data = shared.getString("kiosk", "failed").toString()

        binding = LayoutMapMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val mapFragment: SupportMapFragment = supportFragmentManager.findFragmentById(R.id.mapview) as SupportMapFragment
        mapFragment.getMapAsync(this)

        binding.findMyLocation.setOnClickListener{
            // 이러니깐 알림만 뜸;; 수정필요
            onMyLocationButtonClick()
        }

        binding.back.setOnClickListener {
            val main_intent = Intent(this, MainActivity::class.java)
            main_intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            startActivity(main_intent)
        }
        binding.supply.setOnClickListener {
            val supply_intent = Intent(this, BoosterActivity::class.java)
            supply_intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            startActivity(supply_intent)
        }
        binding.qr.setOnClickListener {
            val qr_popup = MainFragment_QR()
            qr_popup.show(supportFragmentManager, qr_popup.tag)
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        Map = googleMap
        googleMap.setOnMyLocationButtonClickListener(this)
        googleMap.setOnMyLocationClickListener(this)
        enableMyLocation()

        val marker1 = LatLng(37.558941,126.998959)
        Map.addMarker(MarkerOptions().position(marker1).title("코끼리 FIT"))
        Map.moveCamera(CameraUpdateFactory.newLatLng(marker1))

        val marker2 = LatLng(37.561228,126.995587)
        Map.addMarker(MarkerOptions().position(marker2).title("충무로 FIT"))
        Map.moveCamera(CameraUpdateFactory.newLatLng(marker2))

        /*
        markeroneHeung = Map.addMarker(
            MarkerOptions()
                .position(oneHeung)
                .title("oneHeung")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_location))
        )
        markeroneHeung?.tag = 0
        */


        googleMap.setOnMarkerClickListener(this)
    }

    /**
     * Enables the My Location layer if the fine location permission has been granted.
     */
    @SuppressLint("MissingPermission")
    private fun enableMyLocation() {

        // 1. Check if permissions are granted, if so, enable the my location layer
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            Map.isMyLocationEnabled = true
            return
        }
        // 2. If if a permission rationale dialog should be shown
        if (ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) || ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        ) {
            PermissionUtils.RationaleDialog.newInstance(
                LOCATION_PERMISSION_REQUEST_CODE, true
            ).show(supportFragmentManager, "dialog")
            return
        }
        // 3. Otherwise, request permission
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ),
            LOCATION_PERMISSION_REQUEST_CODE
        )

    }

    override fun onMyLocationButtonClick(): Boolean {
        Toast.makeText(this, "MyLocation button clicked", Toast.LENGTH_SHORT)
            .show()
        // Return false so that we don't consume the event and the default behavior still occurs
        // (the camera animates to the user's current position).
        return false
    }

    override fun onMyLocationClick(location: Location) {
        Toast.makeText(this, "here:\n$location", Toast.LENGTH_LONG)
            .show()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (requestCode != LOCATION_PERMISSION_REQUEST_CODE) {
            super.onRequestPermissionsResult(
                requestCode,
                permissions,
                grantResults
            )
            return
        }

        if (isPermissionGranted(
                permissions,
                grantResults,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) || isPermissionGranted(
                permissions,
                grantResults,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        ) {
            // Enable the my location layer if the permission has been granted.
            enableMyLocation()
        } else {
            // Permission was denied. Display an error message
            // Display the missing permission error dialog when the fragments resume.
            permissionDenied = true
        }
    }

    override fun onResumeFragments() {
        super.onResumeFragments()
        if (permissionDenied) {
            // Permission was not granted, display error dialog.
            showMissingPermissionError()
            permissionDenied = false
        }
    }

    /**
     * Displays a dialog with error message explaining that the location permission is missing.
     */
    private fun showMissingPermissionError() {
        newInstance(true).show(supportFragmentManager, "dialog")
    }

    companion object {
        /**
         * Request code for location permission request.
         *
         * @see .onRequestPermissionsResult
         */
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
    }


    /** Called when the user clicks a marker.  */
    override fun onMarkerClick(marker: Marker): Boolean {

        val detail_popup = KioskFragment_Detail()
        detail_popup.show(supportFragmentManager, detail_popup.tag)

        // Return false to indicate that we have not consumed the event and that we wish
        // for the default behavior to occur (which is for the camera to move such that the
        // marker is centered and for the marker's info window to open, if it has one).
        return false
    }

}



