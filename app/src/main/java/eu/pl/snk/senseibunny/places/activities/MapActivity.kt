package eu.pl.snk.senseibunny.places.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import eu.pl.snk.senseibunny.places.databinding.ActivityMapBinding

class MapActivity : AppCompatActivity() {

    var binding: ActivityMapBinding ?=null

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


    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}