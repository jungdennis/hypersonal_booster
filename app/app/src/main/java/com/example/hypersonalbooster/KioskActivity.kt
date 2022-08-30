package com.example.hypersonalbooster


import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.Location

import android.os.Bundle
import android.util.Log
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

import com.example.hypersonalbooster.PermissionUtils.PermissionDeniedDialog.Companion.newInstance
import com.example.hypersonalbooster.PermissionUtils.isPermissionGranted

import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.*

import com.example.hypersonalbooster.databinding.LayoutMapMainBinding

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
    var globalKioskArr = Array(3) { arrayOfNulls<String>(3) }

    lateinit var database_data : String
    val database = FirebaseDatabase.getInstance("https://hypersonal-booster-default-rtdb.asia-southeast1.firebasedatabase.app")
    val ref = database.getReference("kiosk")
    val ref_main = database.getReference("1RwUEzmqz5l9hilFIeJI5gEQu3AUwRAepCc4YzzJGnZY")

    var kioskData_list = ""
    var kioskName_list = ""
    var kioskName = ""
    var cMarkerPos = ""
    var mm : Int = 0
    var kiosks_string = ""

    var BoosterID_list = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val shared = getSharedPreferences("data_kiosk", 0)
        database_data = shared.getString("kiosk", "failed").toString()


        binding = LayoutMapMainBinding.inflate(layoutInflater)
        setContentView(binding.root)



        val mapFragment: SupportMapFragment = supportFragmentManager.findFragmentById(R.id.mapview) as SupportMapFragment
        mapFragment.getMapAsync(this)


        val shared_kioskData = getSharedPreferences("data_kioskData", 0)
        val editor_kioskData = shared_kioskData.edit()

        val shared_kioskNameData = getSharedPreferences("data_kioskNameData", 0)
        val editor_kioskNameData = shared_kioskNameData.edit()

        val shared_BoosterID = getSharedPreferences("BoosterID_list", 0)
        val editor_BoosterID = shared_BoosterID.edit()


            // kiosk 별 보충제 정보 받아오기
            ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
            var temp = ArrayList<String>()
            for (snapshot in dataSnapshot.getChildren()) {
            val name = snapshot.child("name").getValue().toString()
            val b1 = snapshot.child("보충제").child("1").getValue().toString()
            val b2 = snapshot.child("보충제").child("2").getValue().toString()
            val b3 = snapshot.child("보충제").child("3").getValue().toString()
            val b4 = snapshot.child("보충제").child("4").getValue().toString()
            val b5 = snapshot.child("보충제").child("5").getValue().toString()
            val b6 = snapshot.child("보충제").child("6").getValue().toString()
            val kioskDB = "$name,$b1,$b2,$b3,$b4,$b5,$b6"
                val kioskNameDB = "$name"
            if(kioskDB.isNotEmpty()) {
            if(kioskDB !in temp){
            if(kioskData_list.isEmpty()){
                kioskData_list = kioskData_list + kioskDB
                kioskName_list += kioskNameDB
            } else{
                kioskData_list = kioskData_list + "/" + kioskDB
                kioskName_list = kioskName_list + "/" + kioskNameDB
            }
            }
                Log.d("kioskName_list", "$kioskName_list")
            temp.add(kioskDB)
            }
            }
            editor_kioskData.putString("kioskData", kioskData_list)
            editor_kioskData.apply()
                editor_kioskNameData.putString("kioskNameData", kioskName_list)
                editor_kioskNameData.apply()
            }

            override fun onCancelled(databaseError: DatabaseError) {}})

        // 부스터 ID 정보 받기
        ref_main.child("booster").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                var temp = ArrayList<String>()
                Log.d("CLOUD_KIOSK", "$dataSnapshot")
                for (snapshot in dataSnapshot.getChildren()) {
                    val name = snapshot.child("ID").getValue().toString()

                    if(name.isNotEmpty()) {
                        if(name !in temp){
                            if(BoosterID_list.isEmpty()){
                                BoosterID_list = BoosterID_list + name
                            }
                            else{
                                BoosterID_list = BoosterID_list + "/" + name
                            }
                            Log.d("CLOUD_KIOSK", "String : $BoosterID_list")
                        }
                        temp.add(name)
                    }
                }
                editor_BoosterID.putString("BoosterID_list", BoosterID_list)
                editor_BoosterID.apply()

                Log.d("KioskLoading", "Kiosk Loading Success")
            }

            override fun onCancelled(databaseError: DatabaseError) {}})


        binding.searchButton.setOnClickListener {
            val intent = Intent(this, KioskSearch::class.java)
            startActivity(intent)
        }

        binding.back.setOnClickListener {
            finish()
        }

        binding.supply.setOnClickListener {
            val supply_intent = Intent(this, BoosterActivity::class.java)
            supply_intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            startActivity(supply_intent)
            finish()
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
        googleMap.uiSettings.isMyLocationButtonEnabled = true
        enableMyLocation()
        val shared_selName = getSharedPreferences("data_selectedName", 0).getString("selKiosk", "failed")
        var shared_selNameasd = shared_selName
/*
        if(shared_selNameasd != ""){
            var lat:Double = 0.0
            var long:Double = 0.0
            for(i in 0..mm){
                if(shared_selNameasd == globalKioskArr[i][0]){
                    lat = globalKioskArr[i][1]!!.toDouble()
                    long = globalKioskArr[i][2]!!.toDouble()
                }
            }
            CameraUpdateFactory.newLatLngZoom(LatLng(lat, long),17f)
            shared_selNameasd = ""
        }else{
            CameraUpdateFactory.zoomBy(10f)
        }
*/

        val shared_kiosk = getSharedPreferences("data_kiosk", 0)
        var cnt = 0
        var kioskNum:Int = 0
        val kiosk = shared_kiosk.getString("kiosk", "NoKiosk")
        Log.d("Kiosk_String", "$kiosk")
        val stKiosk: String = kiosk.toString()
        var mKioskArr = Array(3) { arrayOfNulls<String>(3) }

        if (stKiosk != null) {
            for (i in 0 until stKiosk.length - 2) {
                if (kiosk!![i].toString() == "/") {
                    cnt++
                }
                kioskNum = cnt + 1

            }
            var kioskArr = Array(kioskNum) { arrayOfNulls<String>(3) }
            var n = 0
            var m = 0
            for (i in stKiosk.indices) {
                if (kiosk!![i].toString() == ",") {
                    kioskArr[m][n] = kioskName
                    n++
                    kioskName = ""
                } else if (kiosk!![i].toString() == "/") {
                    kioskArr[m][n] = kioskName
                    n = 0
                    m++
                    kioskName = ""
                } else {
                    kioskName += kiosk!![i].toString()
                }
            }
            kioskArr[m][n] = kioskName
            
            kioskName = ""
            mKioskArr = kioskArr
            globalKioskArr = mKioskArr
            mm = m

        }

        for(i in 0..mm){
            Map.addMarker(MarkerOptions().position(LatLng(mKioskArr[i][1]!!.toDouble(),
                mKioskArr[i][2]!!.toDouble())).title(mKioskArr[i][0]))
            Map.moveCamera(CameraUpdateFactory.newLatLng(LatLng(mKioskArr[i][1]!!.toDouble(),
                mKioskArr[i][2]!!.toDouble())))

        }

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

        // Return false so that we don't consume the event and the default behavior still occurs
        // (the camera animates to the user's current position).
        return false
    }

    override fun onMyLocationClick(location: Location) {

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

        var x : Int = 0
        var kioskPos = ""
        var selKioskName = ""
        var mKioskPosArr = arrayOfNulls<String>(2)

        val shared_markerPos = getSharedPreferences("data_markerPos", 0)
        val editor_markerPos = shared_markerPos.edit()

        cMarkerPos = marker.position.toString()

        for (i in cMarkerPos.indices){
            if (cMarkerPos!![i].toString() == "("){
                kioskPos = ""
            }
            else if(cMarkerPos!![i].toString() == ","){
                mKioskPosArr[x] = kioskPos
                kioskPos = ""
                x++
            }
            else if(cMarkerPos!![i].toString() == ")"){
                mKioskPosArr[x] = kioskPos
                kioskPos = ""
                x = 0
            }
            else{
                kioskPos += cMarkerPos!![i].toString()
            }
        }
        for(i in 0..mm){
            if(globalKioskArr[i][1] == mKioskPosArr[0]){
                if(globalKioskArr[i][2] == mKioskPosArr[1]){
                    selKioskName = globalKioskArr[i][0].toString()
                }
            }
        }

        editor_markerPos.putString("markerPos", selKioskName)
        editor_markerPos.apply()

        val detail_popup = KioskFragment_Detail()
        detail_popup.show(supportFragmentManager, detail_popup.tag)



        // Return false to indicate that we have not consumed the event and that we wish
        // for the default behavior to occur (which is for the camera to move such that the
        // marker is centered and for the marker's info window to open, if it has one).
        return false
    }

}








