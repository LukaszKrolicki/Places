package eu.pl.snk.senseibunny.places.activities

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import eu.pl.snk.senseibunny.places.R
import eu.pl.snk.senseibunny.places.databinding.ActivityPlaceDetailBinding
import eu.pl.snk.senseibunny.places.models.PlaceModel

class PlaceDetailActivity : AppCompatActivity() {

    private var binding: ActivityPlaceDetailBinding?=null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityPlaceDetailBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        setSupportActionBar(binding?.toolBar)

        if(supportActionBar!=null){
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }

        binding?.toolBar?.setNavigationOnClickListener{
            onBackPressed()
        }

        val place = intent.getSerializableExtra("place") as PlaceModel;

        binding?.title?.text=place.title
        binding?.desc?.text=place.description
        binding?.image?.setImageURI(Uri.parse(place.image))

        binding?.mapButton?.setOnClickListener{
            val intent = Intent(this, MapActivity::class.java)
            intent.putExtra("place", place)
            startActivity(intent)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding=null
    }
}