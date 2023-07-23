package eu.pl.snk.senseibunny.places.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import eu.pl.snk.senseibunny.places.R
import eu.pl.snk.senseibunny.places.databinding.ActivityMapBinding
import eu.pl.snk.senseibunny.places.models.PlaceModel

class MapActivity : AppCompatActivity(), OnMapReadyCallback{

    var binding: ActivityMapBinding ?=null


    private var place: PlaceModel?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapBinding.inflate(layoutInflater)
        setContentView(binding!!.root)

        setSupportActionBar(binding?.toolBar)

        if(supportActionBar!=null){
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }

        binding?.toolBar?.setNavigationOnClickListener{
            onBackPressedDispatcher.onBackPressed()

        }

        place = intent.getSerializableExtra("place") as PlaceModel

        if(place!=null){
            supportActionBar?.title = place?.title

            val supportMapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment

            supportMapFragment.getMapAsync(this)
        }


    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

    override fun onMapReady(googleMap: GoogleMap) { //what should happen when the map is ready
        val position = LatLng(place?.latitide!!, place?.longitude!!)
        googleMap.addMarker(MarkerOptions().position(position).title(place?.title)) //add a marker to the map

        val newLatLangZoom = CameraUpdateFactory.newLatLngZoom(position, 15f)//zoom in on the map

        googleMap.animateCamera(newLatLangZoom)//zooming animation


    }
}