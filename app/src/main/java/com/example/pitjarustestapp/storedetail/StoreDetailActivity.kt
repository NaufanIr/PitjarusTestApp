package com.example.pitjarustestapp.storedetail

import android.Manifest
import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.Matrix
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.withStarted
import com.bumptech.glide.Glide
import com.example.pitjarustestapp.R
import com.example.pitjarustestapp.ViewModelFactory
import com.example.pitjarustestapp.databinding.ActivityStoreDetailBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt

class StoreDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityStoreDetailBinding
    private lateinit var viewModel: StoreDetailViewModel
    private lateinit var locationManager: LocationManager
    private lateinit var storeLocation: Location

    private lateinit var id: String
    private lateinit var visitDate: String
    private var visitPhoto: String? = null

    private var distance: Int? = null

    private var lat: String? = null
    private var lon: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStoreDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        id = intent.getStringExtra(EXTRA_ID).toString()

        val factory = ViewModelFactory.getInstance(this)
        viewModel = ViewModelProvider(this, factory)[StoreDetailViewModel::class.java]

        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

        setupView()

        actionViewPerform()
    }

    private fun setupView() {
        viewModel.getData(id.toInt()).observe(this) {
            visitDate = it.visitDate ?: "-"
            visitPhoto = it.visitPhoto
            lat = it.latitude
            lon = it.longitude
            val outletName = "${it.storeName} ${it.id}"
            val outlet = "Tipe Outlet : ${it.channelName}"
            val display = "Tipe Display : ${it.dcName}"
            val subDisplay = "Sub Tipe Display : ${it.subchannelName}"
            val visitDateDisplay = "Last visit : $visitDate"


            storeLocation = Location("Store Location").also { location ->
                location.latitude = it.latitude.toDouble()
                location.longitude = it.longitude.toDouble()
            }

            binding.apply {
                tvStoreName.text = outletName
                tvAddress.text = it.address
                tvOutletType.text = outlet
                tvDisplayType.text = display
                tvSubDisplayType.text = subDisplay
                tvLastVisit.text = visitDateDisplay

                Glide.with(this@StoreDetailActivity)
                    .load(visitPhoto)
                    .placeholder(R.drawable.add_photo)
                    .error(R.drawable.add_photo)
                    .into(imageView)
            }
        }
    }

    private fun actionViewPerform() {
        //GET USER LOCATION
        binding.btnFindMyloc.setOnClickListener {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // Request location permission
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    0
                )
            } else {
                val myLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                if (myLocation != null) {
                    distance = myLocation.distanceTo(storeLocation).roundToInt()
                    if (distance!! > 100) {
                        binding.tvMyLocation.text = "Jarak anda dengan outlet terlalu jauh!"
                        binding.tvMyLocation.setTextColor(Color.parseColor("#D61506"))
                    } else {
                        binding.tvMyLocation.text = "Anda berada pada radius outlet"
                        binding.tvMyLocation.setTextColor(Color.parseColor("#0A8A0C"))
                    }
                }
            }
        }

        //OPEN CAMERA
        binding.imageView.setOnClickListener {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            intent.resolveActivity(packageManager)

            createTempFile(application).also {
                val photoURI: Uri = FileProvider.getUriForFile(
                    this@StoreDetailActivity,
                    "com.example.pitjarustestapp",
                    it
                )
                visitPhoto = it.absolutePath
                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                resultLauncher.launch(intent)
            }
        }

        //VISIT STORE
        binding.btnVisit.setOnClickListener {
            if (visitPhoto != null && distance != null) {
                if (distance!! > 100) {
                    Toast.makeText(this, "Gagal melakukan verifikasi toko, lokasi anda diluar jangkauan!", Toast.LENGTH_SHORT).show()
                } else {
                    visitDate = SimpleDateFormat("d MMMM yyyy • HH:mm", Locale.US).format(Date())
                    viewModel.visitStore(id.toInt(), visitDate, visitPhoto!!)
                    Toast.makeText(this, "Verifikasi toko berhasil", Toast.LENGTH_SHORT).show()
                    finish()
                }
            } else {
                if (visitPhoto == null) {
                    Toast.makeText(this, "Anda belum mengambil foto!", Toast.LENGTH_SHORT).show()
                }
                if (distance == null) {
                    Toast.makeText(this, "Anda belum menetapkan lokasi!", Toast.LENGTH_SHORT).show()
                }
            }
        }

        //NAVIGATE USE GOOGLE MAPS
        binding.btnNavigate.setOnClickListener {
            val mapIntent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("google.navigation:q=$lat,$lon")
            )
            mapIntent.setPackage("com.google.android.apps.maps")
            startActivity(mapIntent)
        }
    }

    private var resultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val imageFile = File(visitPhoto ?: "")
            val imageUri = Uri.fromFile(imageFile)

            binding.imageView.setImageURI(imageUri)
        }
    }

    private fun createTempFile(context: Context): File {
        val timeStamp: String = SimpleDateFormat("ddMMyyyy_HHmmss", Locale.US)
            .format(System.currentTimeMillis())
        val storageDir: File? = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile("pitjrus_${timeStamp}", ".jpg", storageDir)
    }

    companion object {
        const val EXTRA_ID = "EXTRA_ID"
    }
}