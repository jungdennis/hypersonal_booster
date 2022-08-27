package com.example.hypersonalbooster


import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.Location

import android.os.Bundle
import android.os.Looper
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

import com.example.hypersonalbooster.PermissionUtils.PermissionDeniedDialog.Companion.newInstance
import com.example.hypersonalbooster.PermissionUtils.isPermissionGranted

import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.*

import com.example.hypersonalbooster.databinding.LayoutMapMainBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices

import com.google.android.gms.maps.model.Marker
import com.google.firebase.database.FirebaseDatabase


class KioskActivity : AppCompatActivity(), GoogleMap.OnMyLocationButtonClickListener,
    GoogleMap.OnMyLocationClickListener, OnMapReadyCallback,
    ActivityCompat.OnRequestPermissionsResultCallback, GoogleMap.OnMarkerClickListener {

    private var permissionDenied = false
    private lateinit var Map: GoogleMap
    lateinit var binding : LayoutMapMainBinding

    private var mFusedLocationProviderClient: FusedLocationProviderClient? = null // 현재 위치를 가져오기 위한 변수
    lateinit var mLastLocation: Location // 위치 값을 가지고 있는 객체
    internal lateinit var mLocationRequest: com.google.android.gms.location.LocationRequest
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    lateinit var database_data : String

    val database = FirebaseDatabase.getInstance("https://hypersonal-booster-default-rtdb.asia-southeast1.firebasedatabase.app")
    val ref = database.getReference("kiosk")
    var kioskData_list = ""
    var kioskName = ""

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


        val shared_kioskData = getSharedPreferences("data_kioskData", 0)
        val editor_kioskData = shared_kioskData.edit()
    /*
        val shared_kiosk = getSharedPreferences("data_kiosk", 0)
        var cnt=0
        var kioskNum = 0
        val kiosk = shared_kiosk.getString("kiosk","NoKiosk")
        if (kiosk != null) {
            for (i in 0 until kiosk.length-1){
                if (kiosk[i].equals("/")){
                    cnt++
                }
                kioskNum = cnt + 1

            }

            val kioskArr = Array(kioskNum) { arrayOfNulls<String>(3) }
            var n = 0
            var m = 0
            for (i in 0 until kiosk.length-1){
                if(kiosk[i].equals(",")){
                    kioskArr[m][n] = kioskName
                    n++
                    kioskName = ""
                }
                else if(kiosk[i].equals("/")){
                    kioskArr[m][n] = kioskName
                    n = 0
                    m++
                    kioskName = ""
                }
                else{
                    kioskName += kiosk[i]
                }
            }
        }
    */
            /**
            ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
            var temp = ArrayList<String>()
            for (snapshot in dataSnapshot.getChildren()) {
            val name = snapshot.child("name").getValue().toString()
            val b1 = snapshot.child("1").getValue().toString()
            val b2 = snapshot.child("2").getValue().toString()
            val b3 = snapshot.child("3").getValue().toString()
            val b4 = snapshot.child("4").getValue().toString()
            val b5 = snapshot.child("5").getValue().toString()
            val b6 = snapshot.child("6").getValue().toString()
            val kioskDB = "$name,$b1,$b2,$b3,$b4,$b5,$b6"
            if(kioskDB.isNotEmpty()) {
            if(kioskDB !in temp){
            if(kioskData_list.isEmpty()){
            kioskData_list = kioskData_list + kioskDB
            }
            else{
            kioskData_list = kioskData_list + "/" + kioskDB
            }
            }
            temp.add(kioskDB)
            }
            }
            editor_kioskData.putString("kioskData", kioskData_list)
            editor_kioskData.apply()
            }

            override fun onCancelled(databaseError: DatabaseError) {}})

             */






        // 테스트 -- 성공함 이거 쓰면 댐
            val shared_kiosk = getSharedPreferences("data_kiosk", 0)
        var cnt = 0
        var kioskNum:Int = 0
        val kiosk = shared_kiosk.getString("kiosk", "NoKiosk")
        val stKiosk: String = kiosk.toString()
        var mKioskArr = Array(2) { arrayOfNulls<String>(3) }
        var mm : String = "실패 씨발"
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
            if(kiosk!![0].toString() == "충"){
                mm = "씨발 성공"
            }

            kioskName = ""
            mKioskArr = kioskArr

        }















        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        binding.back.setOnClickListener {
            finish()
        }
        
        // kiosk string 테스트 코드
        binding.kioskTestB.setOnClickListener {
            binding.kioskTest.text = mKioskArr[0][0]
        //    binding.kioskTest2.text = kiosk!![0].toString()
        //    binding.kioskTest5.text = stKiosk.length.toString()
        //    binding.kioskTest4.text = mm
            binding.kioskTest2.text = mKioskArr[0][1]
            binding.kioskTest3.text = mKioskArr[0][2]
            binding.kioskTest4.text = mKioskArr[1][0]
            binding.kioskTest5.text = mKioskArr[1][1]
            binding.kioskTest6.text = mKioskArr[1][2]
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
        googleMap.uiSettings.isMyLocationButtonEnabled = true
        enableMyLocation()

        val shared_kiosk = getSharedPreferences("data_kiosk", 0)
        var cnt = 0
        var kioskNum = 0
        val kiosk = shared_kiosk.getString("kiosk", "NoKiosk")
        var mKioskArr = Array(kioskNum) { arrayOfNulls<String>(3) }
        var mm : Int = 0
        if (kiosk != null) {
            for (i in 0 until kiosk.length - 1) {
                if (kiosk[i].equals("/")) {
                    cnt++
                }
                kioskNum = cnt + 1

            }
            var kioskArr = Array(kioskNum) { arrayOfNulls<String>(3) }
            var n = 0
            var m = 0
            for (i in 0 until kiosk.length - 1) {
                if (kiosk[i].equals(",")) {
                    kioskArr[m][n] = kioskName
                    n++
                    kioskName = ""
                } else if (kiosk[i].equals("/")) {
                    kioskArr[m][n] = kioskName
                    n = 0
                    m++
                    kioskName = ""
                } else {
                    kioskName += kiosk[i]
                }
            }
            mKioskArr = kioskArr
            mm = m
        }
/*
        for(i in 0..mm){
            Map.addMarker(MarkerOptions().position(LatLng(mKioskArr[i][1]!!.toDouble(),
                mKioskArr[i][2]!!.toDouble())).title(mKioskArr[i][0]))
            Map.moveCamera(CameraUpdateFactory.newLatLng(LatLng(mKioskArr[i][1]!!.toDouble(),
                mKioskArr[i][2]!!.toDouble())))
        }

        Map.addMarker(MarkerOptions().position(LatLng(mKioskArr[0][1]!!.toDouble(),
            mKioskArr[0][2]!!.toDouble())).title(mKioskArr[0][0]))
        Map.moveCamera(CameraUpdateFactory.newLatLng(LatLng(mKioskArr[0][1]!!.toDouble(),
            mKioskArr[0][2]!!.toDouble())))

        Map.addMarker(MarkerOptions().position(LatLng(mKioskArr[1][1]!!.toDouble(),
            mKioskArr[1][2]!!.toDouble())).title(mKioskArr[1][0]))
        Map.moveCamera(CameraUpdateFactory.newLatLng(LatLng(mKioskArr[1][1]!!.toDouble(),
            mKioskArr[1][2]!!.toDouble())))
        */


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

    private fun startLocationUpdates() {

        //FusedLocationProviderClient의 인스턴스를 생성.
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return
        }
        // 기기의 위치에 관한 정기 업데이트를 요청하는 메서드 실행
        // 지정한 루퍼 스레드(Looper.myLooper())에서 콜백(mLocationCallback)으로 위치 업데이트를 요청
        mFusedLocationProviderClient!!.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper())
    }

    // 시스템으로 부터 위치 정보를 콜백으로 받음
    private val mLocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            // 시스템에서 받은 location 정보를 onLocationChanged()에 전달
            locationResult.lastLocation
            onLocationChanged(locationResult.lastLocation)
        }
    }

    fun onLocationChanged(location: Location) {
        mLastLocation = location
        // var latlat = mLastLocation.latitude // 갱신 된 위도
        // var longlong = mLastLocation.longitude // 갱신 된 경도
        // var latlnglat = LatLng(latlat,longlong)
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






