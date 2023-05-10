package team.jot.favoriteplaces

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import team.jot.favoriteplaces.databinding.ActivityMapsBinding

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private val TAG = "MainActivity"

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding

    //convert physical address to long/lat
    private lateinit var geocoder: Geocoder
    private val ACCESS_LOCATION_CODE = 123


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Add a marker in Sydney and move the camera
        //val sydney = LatLng(-34.0, 151.0)
        //mMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))

        // Request location permission and show user's current location on the Map
        getLocationPermission()
    }
    //Uses the permissions granted in the manifest for fine/coarse location
    private fun getLocationPermission() {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED) {
            // Permission is granted
            enableUserLocation()
        } else {

            // Permission is not granted
            // show an explanation
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                // Show an explanation to the user
                ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    ACCESS_LOCATION_CODE)
            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    ACCESS_LOCATION_CODE)
            }
        }
    }
    //Enablement of the users location
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>,
                                            grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            ACCESS_LOCATION_CODE -> {
                enableUserLocation()
            }
        }
    }
    //Error handling
    @SuppressLint("MissingPermission")
    private fun enableUserLocation() {
        mMap.isMyLocationEnabled = true
    }




    //Grabbing the input in order to convert using geocoder
    @Suppress("DEPRECATION")
    fun searchButton(view: View) {

        val addressText = findViewById<EditText>(R.id.address_text)

        // Get address from the user
        val locationName = addressText.text.toString()
        addressText.hideKeyboard()
        //Log.d(TAG, "$locationName")

        if (locationName.isEmpty()){
            return
        }

        geocoder = Geocoder(this)

        try {
            val addressList = geocoder.getFromLocationName(locationName, 1)

            if (!addressList.isNullOrEmpty()) {
                val address = addressList[0]
                Log.d(TAG, "$address")

                // Convert to latitude and latitude to LatLng
                val latLng = LatLng(address.latitude, address.longitude)

                // Set the marker options
                val markerOptions = MarkerOptions()
                    .position(latLng)
                    .title(address.locality)
                    .snippet("Interesting place!") // You can change this text to something else

                // Add the marker
                mMap.addMarker(markerOptions)

                // Move and animate the camera, 12f is the zoom level, the higher number, it zooms more closely
                val cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 12f)
                mMap.animateCamera(cameraUpdate)

            }
        } catch (e: Exception) {
            Log.e(TAG, "${e.message}")
        }

    }

    private fun View.hideKeyboard() {
        val inputMethodManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(windowToken, 0)
    }
}