package eu.pl.snk.senseibunny.places.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import eu.pl.snk.senseibunny.places.R
import eu.pl.snk.senseibunny.places.databinding.ActivityPlaceDetailBinding

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


    }

    override fun onDestroy() {
        super.onDestroy()
        binding=null
    }
}