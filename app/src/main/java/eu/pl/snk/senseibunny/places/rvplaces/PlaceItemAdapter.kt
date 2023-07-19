package eu.pl.snk.senseibunny.places.rvplaces

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import eu.pl.snk.senseibunny.places.R
import eu.pl.snk.senseibunny.places.database.PlaceEntity
import eu.pl.snk.senseibunny.places.databinding.PlaceItemBinding

class PlaceItemAdapter (private val items: ArrayList<PlaceEntity>): RecyclerView.Adapter<PlaceItemAdapter.ViewHolder>() {


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

        holder.title.text=item.title.toString()
        holder.description.text=item.description.toString()
        holder.image.setImageURI(Uri.parse(item.image))

        if(position%2!=0){
            holder.rl.setBackgroundColor(ContextCompat.getColor(holder.itemView.context, R.color.white))
        }

    }
}