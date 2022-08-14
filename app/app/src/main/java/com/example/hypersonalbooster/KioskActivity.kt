package com.example.hypersonalbooster

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

import com.example.hypersonalbooster.databinding.LayoutMapMainBinding

class KioskActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    lateinit var binding : LayoutMapMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = LayoutMapMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val mapFragment: SupportMapFragment = supportFragmentManager.findFragmentById(R.id.mapview) as SupportMapFragment
        mapFragment.getMapAsync(this)

        binding.kiosk.setOnClickListener {
            val detail_popup = KioskFragment_Detail()
            detail_popup.show(supportFragmentManager, detail_popup.tag)
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
        mMap = googleMap
        val marker = LatLng(35.241615, 128.695587)
        mMap.addMarker(MarkerOptions().position(marker).title("마커 제목"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(marker))
    }

    override fun onBackPressed() {
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
        startActivity(intent)
    }
}