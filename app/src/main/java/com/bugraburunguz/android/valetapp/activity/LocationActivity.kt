package com.bugraburunguz.android.valetapp.activity

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import com.bugraburunguz.android.valetapp.R
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class LocationActivity : FragmentActivity(), OnMapReadyCallback {
    private var mMap: GoogleMap? = null
    private val mDefaultLocation = LatLng(43.773236, -79.4059391)
    private var mLocationPermissionGranted = false
    private var mLastKnownLocation: Location? = null

    private var mFusedLocationProviderClient: FusedLocationProviderClient? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_location)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment!!.getMapAsync(this)

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        googleMap.mapType = GoogleMap.MAP_TYPE_SATELLITE
        googleMap.uiSettings.isZoomGesturesEnabled = true

        locationPermission

        updateLocationUI()

        deviceLocation
    }

    private val locationPermission: Unit
        get() {
            if (ContextCompat.checkSelfPermission(
                    this.applicationContext,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
                == PackageManager.PERMISSION_GRANTED
            ) {
                mLocationPermissionGranted = true
            } else {
                ActivityCompat.requestPermissions(
                    this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION
                )
            }
        }

    private fun updateLocationUI() {
        if (mMap == null) {
            return
        }
        try {
            if (mLocationPermissionGranted) {
                mMap!!.isMyLocationEnabled = true
                mMap!!.uiSettings.isMyLocationButtonEnabled = true
            } else {
                mMap!!.isMyLocationEnabled = false
                mMap!!.uiSettings.isMyLocationButtonEnabled = false
                mLastKnownLocation = null
                locationPermission
            }
        } catch (e: SecurityException) {
            Log.e("Exception: %s", e.message!!)
        }
    }

    private val deviceLocation: Unit
        get() {
            try {
                if (mLocationPermissionGranted) {
                    val locationResult = mFusedLocationProviderClient!!.lastLocation
                    locationResult.addOnCompleteListener(
                        this
                    ) { task ->
                        if (task.isSuccessful) {
                            // Set the map's camera position to the current location of the device.
                            mLastKnownLocation = task.result
                            val currentLocation = LatLng(
                                mLastKnownLocation!!.latitude,
                                mLastKnownLocation!!.longitude
                            )
                            mMap!!.moveCamera(
                                CameraUpdateFactory.newLatLngZoom(
                                    currentLocation,
                                    DEFAULT_ZOOM.toFloat()
                                )
                            )
                            mMap!!.addMarker(
                                MarkerOptions().position(currentLocation).title("Current Location")
                            )
                            val spotA = LatLng(43.7774392, -79.336438)
                            mMap!!.addMarker(MarkerOptions().position(spotA).title("Lot A"))
                            val spotB = LatLng(43.773523, -79.334563)
                            mMap!!.addMarker(MarkerOptions().position(spotB).title("Lot B"))
                            val spotC = LatLng(43.773223, -79.331834)
                            mMap!!.addMarker(MarkerOptions().position(spotC).title("Lot C"))
                            Log.v("Location", mLastKnownLocation!!.latitude.toString())
                            Log.v(
                                "Location",
                                mLastKnownLocation!!.longitude.toString()
                            )
                        } else {
                            Log.d(
                                "LocationActivity",
                                "Current location is null. Using defaults."
                            )
                            Log.e("LocationActivity", "Exception: %s", task.exception)
                            mMap!!.moveCamera(
                                CameraUpdateFactory
                                    .newLatLngZoom(
                                        mDefaultLocation,
                                        DEFAULT_ZOOM.toFloat()
                                    )
                            )
                            mMap!!.uiSettings.isMyLocationButtonEnabled = false
                        }
                    }
                }
            } catch (e: SecurityException) {
                Log.e("Exception: %s", e.message!!)
            }
        }

    companion object {
        private const val DEFAULT_ZOOM = 18
        private const val PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1
    }
}