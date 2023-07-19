package eu.pl.snk.senseibunny.places.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import eu.pl.snk.senseibunny.places.database.PlaceApp
import eu.pl.snk.senseibunny.places.database.PlaceEntity
import eu.pl.snk.senseibunny.places.databinding.ActivityMainBinding
import eu.pl.snk.senseibunny.places.rvplaces.PlaceItemAdapter
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private var binding: ActivityMainBinding?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root) // top element of xml file is root

        binding?.fabAddHappyPlace?.setOnClickListener{
            val intent = Intent(this, AddHappyPlaceActivity::class.java)
            startActivity(intent)
        }

        val placeDao = (application as PlaceApp).db.placesDao()

        lifecycleScope.launch{
            placeDao.fetchAllPlaces().collect(){
                val list = ArrayList(it)
                setUpRVdata(list)
            }
        }
    }

    private fun setUpRVdata(placesList:ArrayList<PlaceEntity>){
        if(placesList.isNotEmpty()){

            val itemAdapter= PlaceItemAdapter(placesList)

            binding?.rvData?.layoutManager= LinearLayoutManager(this)
            binding?.rvData?.adapter=itemAdapter
            binding?.rvData?.visibility= View.VISIBLE


        }

    }


    override fun onDestroy() {
        super.onDestroy()
        binding=null
    }
}