package com.example.hw1_daniel_gerbi

import android.Manifest
import android.location.LocationManager
import android.health.connect.datatypes.ExerciseRoute.Location
import android.location.LocationListener
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import com.example.hw1_daniel_gerbi.fragments.HighScoreFragment
import com.example.hw1_daniel_gerbi.fragments.MapFragment
import com.example.hw1_daniel_gerbi.interfaces.CallbackHighScoreItemClicked
import com.example.hw1_daniel_gerbi.logic.ScoreManager
import com.example.hw1_daniel_gerbi.model.Score
import com.example.hw1_daniel_gerbi.utilities.Constants
import com.example.hw1_daniel_gerbi.utilities.SignalManager
import com.example.hw1_daniel_gerbi.utilities.TiltDetector
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.CameraUpdateFactory.newLatLngZoom
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import com.vmadalin.easypermissions.EasyPermissions
import java.util.concurrent.TimeUnit

class HighScoreActivity : AppCompatActivity(), EasyPermissions.PermissionCallbacks {

    private lateinit var main_FRAME_list: FrameLayout
    private lateinit var main_FRAME_map: FrameLayout
    private lateinit var mapFragment: SupportMapFragment
    private var googleMap: GoogleMap? = null
    private lateinit var highScoreFragment: HighScoreFragment
    private var currentScore: Int = 0
    private lateinit var locationManager: LocationManager
    private lateinit var locationListener: LocationListener
    private var locationPermissionGranted = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_high_score)
        findViews()
        initViews()
    }

    private fun findViews() {
        main_FRAME_list = findViewById(R.id.main_FRAME_list)
        main_FRAME_map = findViewById(R.id.main_FRAME_map)
    }

    private fun initViews() {
        highScoreFragment = HighScoreFragment()
        mapFragment = SupportMapFragment.newInstance()

        setupHighScoreClickListener()
//        highScoreFragment.highScoreItemClicked = object : CallbackHighScoreItemClicked{
//            override fun highScoreItemClicked(lat: Double, lon: Double) {
//                Log.d("HighScoreActivity", "Callback triggered with Lat: $lat, Lon: $lon")
//                googleMap?.animateCamera(
//                    newLatLngZoom(
//                        LatLng(lat, lon),
//                        15f
//                    )
//                )
//                SignalManager.getInstance().toast("Zooming to: Lat=$lat, Lon=$lon")
//            }
//        }

        supportFragmentManager
            .beginTransaction()
            .replace(R.id.main_FRAME_list, highScoreFragment)
            .replace(R.id.main_FRAME_map, mapFragment)
            .commit()

        mapFragment.getMapAsync { map ->
            googleMap = map
            googleMap?.uiSettings?.isZoomControlsEnabled = true
            updateMapWithScores()
        }
//        googleMap?.setOnMarkerClickListener { marker ->
//            marker.showInfoWindow()
//            googleMap?.animateCamera(CameraUpdateFactory.newLatLngZoom(marker.position, 15f))
//            SignalManager.getInstance().toast("Clicked on: ${marker.title}")
//            updateMapWithScores()
//            true

       // }

        locationManager = getSystemService(LOCATION_SERVICE) as LocationManager
        initLocationListener()
    }

    private fun setupHighScoreClickListener() {
        highScoreFragment.highScoreItemClicked = object : CallbackHighScoreItemClicked {
            override fun highScoreItemClicked(lat: Double, lon: Double) {
                Log.d("HighScoreActivity", "Callback triggered with Lat: $lat, Lon: $lon")
                googleMap?.animateCamera(
                    CameraUpdateFactory.newLatLngZoom(
                        LatLng(lat, lon),
                        15f
                    )
                )
                SignalManager.getInstance().toast("Zooming to: Lat=$lat, Lon=$lon")
            }
        }
    }

    private fun initLocationListener() {
        locationListener = LocationListener { location ->
            saveScoreIfEligible(location.latitude, location.longitude)
        }
    }

    override fun onResume() {
        super.onResume()
        loadScoreFromIntent()
        checkLocationPermission()
        if (locationPermissionGranted) {
            debugScores()
            startLocationListener()
            fetchAndAddScore()
            updateMapWithScores()
            highScoreFragment.updateHighScore()
            if (ScoreManager.getInstance(this).scores.isEmpty() && currentScore == 0) {
                highScoreFragment.highScore_LBL_title.visibility = View.VISIBLE
            } else {
                highScoreFragment.highScore_LBL_title.visibility = View.GONE
            }
        }
    }

    override fun onPause() {
        super.onPause()
        stopLocationListener()
    }

    private fun loadScoreFromIntent() {
        val bundle = intent.extras ?: return
        currentScore = bundle.getInt(Constants.BundleKeys.SCORE_KEY, 0)
    }

    private fun checkLocationPermission() {
        if (EasyPermissions.hasPermissions(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
            locationPermissionGranted = true
        } else {
            EasyPermissions.requestPermissions(
                this,
                "Location permissions are needed to save high scores.",
                101,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        }
    }

    private fun startLocationListener() {
        try {
            locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                30000L, // 30 seconds
                10f, // 10 meters
                locationListener
            )
        } catch (e: SecurityException) {
            Log.e("LocationDebug", "Permission not granted for location updates", e)
        }
    }


    private fun stopLocationListener() {
        locationManager.removeUpdates(locationListener)
    }

    private fun fetchAndAddScore() {
        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            if (location != null) {
                val latitude = location.latitude
                val longitude = location.longitude
                saveScoreIfEligible(latitude, longitude)
            } else {
                SignalManager.getInstance().toast("Unable to fetch location")
            }
        }.addOnFailureListener {
            SignalManager.getInstance().toast("Failed to fetch location")
        }
    }

    private fun saveScoreIfEligible(latitude: Double, longitude: Double) {
        if (currentScore == 0) return

        val scoreManager = ScoreManager.getInstance(this)
        val scores = scoreManager.scores
        if (scores.size < 10 || currentScore > scores.last().scoreValue) {
            val newScore = Score(currentScore, latitude, longitude)
            scoreManager.updateScore(newScore)
            highScoreFragment.updateHighScore()
            googleMap?.addMarker(
                MarkerOptions()
                    .position(LatLng(latitude, longitude))
                    .title("Score: $currentScore")
            )
            googleMap?.animateCamera(
                CameraUpdateFactory.newLatLngZoom(
                    LatLng(latitude, longitude),
                    15f
                )
            )
        }
        currentScore = 0
    }

    private fun updateMapWithScores() {
        val scores = ScoreManager.getInstance(this).scores
        if (scores.isNotEmpty()) {
            googleMap?.clear() // נקה את כל הסימנים הקיימים
            val boundsBuilder = LatLngBounds.Builder()

            scores.forEach { score ->
                val location = LatLng(score.latitude, score.longitude)
                googleMap?.addMarker(
                    MarkerOptions()
                        .position(location)
                        .title("Score: ${score.scoreValue}")
                        .snippet("Lat: ${score.latitude}, Lon: ${score.longitude}")
                )
                boundsBuilder.include(location)
            }

            // התמקדות על כל השיאים
            val bounds = boundsBuilder.build()
            googleMap?.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100))

            // Listener ללחיצה על סימן במפה
            googleMap?.setOnMarkerClickListener { marker ->
                marker.showInfoWindow() // מציג מידע נוסף
                googleMap?.animateCamera(CameraUpdateFactory.newLatLngZoom(marker.position, 15f)) // מבצע זום למיקום
                SignalManager.getInstance().toast("Clicked on: ${marker.title}")
                true
            }
        } else {
            SignalManager.getInstance().toast("No scores to display on the map.")
        }
    }


    private fun debugScores() {
        val scores = ScoreManager.getInstance(this).scores
        Log.d("HighScoreActivity", "Scores loaded: $scores")
        if (scores.isEmpty()) {
            SignalManager.getInstance().toast("No scores found in ScoreManager")
        }
    }

    override fun onPermissionsDenied(requestCode: Int, perms: List<String>) {
        Log.d("HighScoreActivity", "Permissions denied: $perms")
        SignalManager.getInstance().toast("Permissions denied: $perms")
    }

    override fun onPermissionsGranted(requestCode: Int, perms: List<String>) {
        Log.d("HighScoreActivity", "Permissions granted: $perms")
        SignalManager.getInstance().toast("Permissions granted: $perms")
    }
}

