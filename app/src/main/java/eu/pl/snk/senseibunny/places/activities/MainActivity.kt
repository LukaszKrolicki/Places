package eu.pl.snk.senseibunny.places.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import eu.pl.snk.senseibunny.places.database.PlaceApp
import eu.pl.snk.senseibunny.places.database.PlaceDao
import eu.pl.snk.senseibunny.places.database.PlaceEntity
import eu.pl.snk.senseibunny.places.databinding.ActivityMainBinding
import eu.pl.snk.senseibunny.places.rvplaces.PlaceItemAdapter
import eu.pl.snk.senseibunny.places.utils.SwipeToDeleteCallback
import eu.pl.snk.senseibunny.places.utils.SwipeToEditCallback
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
                setUpRVdata(list,placeDao)
            }
        }
    }

    private fun setUpRVdata(placesList:ArrayList<PlaceEntity>,placeDao: PlaceDao){
        if(placesList.isNotEmpty()){

            val itemAdapter= PlaceItemAdapter(placesList)

            binding?.rvData?.layoutManager= LinearLayoutManager(this)
            binding?.rvData?.adapter=itemAdapter
            binding?.rvData?.visibility= View.VISIBLE


            val editSwipeHandler = object : SwipeToEditCallback(this) {

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {


                    itemAdapter.notifyEditItem(
                        this@MainActivity,
                        viewHolder.adapterPosition,
                        0,
                    )

                }
            }

            val editItemTouchHelper = ItemTouchHelper(editSwipeHandler)
            editItemTouchHelper.attachToRecyclerView(binding?.rvData)


            val deleteSwipeHandler = object : SwipeToDeleteCallback(this) {
                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

                    lifecycleScope.launch {
                        placeDao.fetchAllPlaces().collect() {
                            val list = ArrayList(it)
                            itemAdapter.updateList(list)
                        }
                    }
                    val x=itemAdapter.notifyDeleteItem(
                        this@MainActivity,
                        viewHolder.adapterPosition,
                        0
                    )


                    lifecycleScope.launch{
                        placeDao.delete(x)
                        Toast.makeText(this@MainActivity,"Place deleted ${x.id}",Toast.LENGTH_SHORT).show()
                    }
                }
            }

            val delteItemTouchHelper = ItemTouchHelper(deleteSwipeHandler)
            delteItemTouchHelper.attachToRecyclerView(binding?.rvData)

        }

    }


    override fun onDestroy() {
        super.onDestroy()
        binding=null
    }
}