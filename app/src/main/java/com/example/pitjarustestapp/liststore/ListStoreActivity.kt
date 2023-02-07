package com.example.pitjarustestapp.liststore

import android.Manifest
import android.app.SearchManager
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pitjarustestapp.R
import com.example.pitjarustestapp.ViewModelFactory
import com.example.pitjarustestapp.data.local.StoreEntity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.example.pitjarustestapp.databinding.ActivityListStoreBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CircleOptions

class ListStoreActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityListStoreBinding
    private lateinit var viewModel: ListStoreViewModel
    private lateinit var myLocation: Location
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        when {
            permissions[Manifest.permission.ACCESS_FINE_LOCATION] ?: false -> {
                // Precise location access granted.
                getMyLocation()
            }
            permissions[Manifest.permission.ACCESS_COARSE_LOCATION] ?: false -> {
                // Only approximate location access granted.
                getMyLocation()
            }
            else -> {
                // No location access granted.
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListStoreBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val factory = ViewModelFactory.getInstance(this@ListStoreActivity)
        viewModel = ViewModelProvider(this, factory)[ListStoreViewModel::class.java]

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        viewModel.getStores().observe(this) {
            var position = LatLng(37.4274745, -122.169719)
            it.forEach { store ->
                position = LatLng(store.latitude.toDouble(), store.longitude.toDouble())
                mMap.addMarker(
                    MarkerOptions()
                        .position(position)
                        .title(store.storeName)

                )?.showInfoWindow()
            }
            mMap.addCircle(
                CircleOptions()
                    .center(position)
                    .radius(100.0)
                    .fillColor(0x6685CDFD)
                    .strokeWidth(0f)

            )
        }
        getMyLocation()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.list_store_menu, menu)

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu.findItem(R.id.search).actionView as SearchView
        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.queryHint = "Cari toko/distributor..."

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                searchView.clearFocus()
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }
        })
        return true
    }

    private fun getMyLocation() {
        if(checkPermission(Manifest.permission.ACCESS_FINE_LOCATION) &&
            checkPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
        ) {
            mMap.isMyLocationEnabled = true
            val lastLoc = fusedLocationClient.lastLocation
            lastLoc.addOnSuccessListener(this) {
                if (it != null) {
                    myLocation = it
                    val myPosition = LatLng(it.latitude, it.longitude)
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myPosition, 17.2f))
                    showListOfStore()
                }
            }
        } else {
            requestPermissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }

    private fun showListOfStore() {
        viewModel.getStores().observe(this) {
            if (it.isNotEmpty() && it != null) {
                val layoutManager = LinearLayoutManager(this)
                binding.rvStore.layoutManager = layoutManager
                val layoutAdapter = ListStoreAdapter(it, myLocation)
                binding.rvStore.adapter = layoutAdapter
            }
        }
    }

    private fun checkPermission(permission: String): Boolean {
        return ContextCompat
            .checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED
    }
}