package eu.pl.snk.senseibunny.places.rvplaces

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import eu.pl.snk.senseibunny.places.R
import eu.pl.snk.senseibunny.places.activities.AddHappyPlaceActivity
import eu.pl.snk.senseibunny.places.activities.MainActivity
import eu.pl.snk.senseibunny.places.activities.PlaceDetailActivity
import eu.pl.snk.senseibunny.places.database.PlaceEntity
import eu.pl.snk.senseibunny.places.databinding.PlaceItemBinding
import eu.pl.snk.senseibunny.places.models.PlaceModel
import eu.pl.snk.senseibunny.places.utils.SwipeToEditCallback


class PlaceItemAdapter (private val items: ArrayList<PlaceEntity>): RecyclerView.Adapter<PlaceItemAdapter.ViewHolder>() {

    var place1: PlaceModel? = null

    class ViewHolder(binding: PlaceItemBinding): RecyclerView.ViewHolder(binding.root){
        val rl=binding.rl
        val image=binding.image
        val title=binding.title
        val description=binding.description
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(PlaceItemBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun getItemCount(): Int {
        return items.size
    }




    override fun onBindViewHolder(holder: ViewHolder, position: Int) { // what should do for every item when view is bind
        val context = holder.itemView.context
        val item=items[position]

        val place = PlaceModel(item.id,item.title,item.image,item.description,item.date,item.location,item.latitude,item.longitude)

        holder.title.text=item.title.toString()
        holder.description.text=item.description.toString()
        holder.image.setImageURI(Uri.parse(item.image))

        if(position%2!=0){
            holder.rl.setBackgroundColor(ContextCompat.getColor(holder.itemView.context, R.color.white))
        }

        holder.rl.setOnClickListener{
            val intent = Intent(context, PlaceDetailActivity::class.java)
            intent.putExtra("place",place)
            context.startActivity(intent)

        }



    }


    fun notifyEditItem(activity: Activity, position: Int, requestCode: Int) {
        val intent = Intent(activity.applicationContext, AddHappyPlaceActivity::class.java)

        val item=items[position]
        val place = PlaceModel(item.id,item.title,item.image,item.description,item.date,item.location,item.latitude,item.longitude)

        intent.putExtra("place", place)
        intent.putExtra("request", requestCode)
        activity.startActivityForResult(
            intent,
            requestCode
        ) // Activity is started with requestCode

        notifyItemChanged(position) // Notify any registered observers that the item at position has changed.
    }


}