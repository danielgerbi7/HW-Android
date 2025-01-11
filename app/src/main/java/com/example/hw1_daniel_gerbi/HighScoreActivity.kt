package com.example.hw1_daniel_gerbi

import android.Manifest
import android.os.Bundle
import android.util.Log
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import com.example.hw1_daniel_gerbi.fragments.HighScoreFragment
import com.example.hw1_daniel_gerbi.fragments.MapFragment
import com.example.hw1_daniel_gerbi.logic.ScoreManager
import com.example.hw1_daniel_gerbi.model.Score
import com.example.hw1_daniel_gerbi.utilities.Constants
import com.example.hw1_daniel_gerbi.utilities.SignalManager
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class HighScoreActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var main_FRAME_list: FrameLayout
    //private lateinit var main_FRAME_map: FrameLayout
    private lateinit var highScoreFragment: HighScoreFragment
    private var currentScore: Int = 0
    private lateinit var googleMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_high_score)
        findViews()
        initViews()
        val mapFragment = supportFragmentManager.findFragmentById(R.id.main_FRAME_map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(p0: GoogleMap) {
        googleMap = p0
        googleMap.uiSettings.isZoomControlsEnabled = true
    }

    fun zoomToLocation(latitude: Double, longitude: Double) {
        val location = LatLng(latitude, longitude)
        googleMap.addMarker(MarkerOptions().position(location).title("New High Score"))
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15f))
    }

    private fun findViews() {
        main_FRAME_list = findViewById(R.id.main_FRAME_list)
    }

    private fun initViews() {
        highScoreFragment = HighScoreFragment()
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.main_FRAME_list, highScoreFragment)
            .commit()
    }

    override fun onResume() {
        super.onResume()
        loadScoreFromIntent()
        //locationPermissions()
        addScoreIfNeeded(31.0, 35.0)
        val fragment =
            supportFragmentManager.findFragmentById(R.id.main_FRAME_list) as? HighScoreFragment
        fragment?.updateHighScore()
    }

    private fun loadScoreFromIntent() {
        val bundle = intent.extras ?: return
        currentScore = bundle.getInt(Constants.BundleKeys.SCORE_KEY, 0)
    }

    private fun addScoreIfNeeded(latitude: Double, longitude: Double) {
        if (currentScore == 0) return

        val scores = ScoreManager.getInstance(this).scores
        if (scores.size < 10 || currentScore > scores.last().scoreValue) {
            val newScore = Score(currentScore, latitude, longitude)
            ScoreManager.getInstance(this).updateScore(newScore)
            highScoreFragment.updateHighScore()
            fun updateScores(newScores: List<Score>) {
                val fragment = supportFragmentManager.findFragmentById(R.id.main_FRAME_list) as? HighScoreFragment
                fragment?.updateHighScore()
            }
        }
        currentScore = 0
    }



//    private fun locationPermissions() {
//        if (EasyPermissions.hasPermissions(
//                this,
//                Manifest.permission.ACCESS_FINE_LOCATION
//            )
//        ) {
//            fetchAndAddScore()
//        } else {
//            EasyPermissions.requestPermissions(
//                this,
//                "Location permissions are required to save your score",
//                1001,
//                Manifest.permission.ACCESS_FINE_LOCATION
//            )
//        }
//    }
//
//    private fun fetchAndAddScore() {
//        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
//        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
//            if (location != null) {
//                val latitude = location.latitude
//                val longitude = location.longitude
//                addScoreIfNeeded(latitude, longitude)
//            } else {
//                SignalManager.getInstance().toast("Unable to fetch location")
//            }
//        }.addOnFailureListener {
//            SignalManager.getInstance().toast("Failed to fetch location")
//        }
//    }
}
