package team.jot.favoriteplaces

import android.content.Intent
import android.graphics.BitmapFactory
import android.media.MediaPlayer
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class MyRecyclerAdapter(private val destinations: MutableList<Destination>): RecyclerView.Adapter<MyRecyclerAdapter.MyViewHolder>() {

    private val TAG = "MyRecyclerAdapter"
    var count = 0 //This variable is used for just testing purpose to understand how RecyclerView works

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder.
    inner class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        // This class will represent a single row in our recyclerView list
        // This class also allows caching views and reuse them
        // Each MyViewHolder object keeps a reference to 3 view items in our row_item.xml file
        val destinationName = itemView.findViewById<TextView>(R.id.destination_name)
        val destinationImage = itemView.findViewById<ImageView>(R.id.destination_image)
        val description = itemView.findViewById<TextView>(R.id.description)
        val rating = itemView.findViewById<RatingBar>(R.id.ratingBar)


        init {
            itemView.setOnClickListener {
                val selectedItem = adapterPosition
                Toast.makeText(itemView.context, "You clicked on $selectedItem", Toast.LENGTH_SHORT).show()
            }



            // Make sure to change the  MyViewHolder class to inner class to get a reference to an object of outer class
            itemView.setOnLongClickListener {
                val selectedItem = adapterPosition
                val dbHelper = DestinationDbHelper(itemView.context)
                dbHelper.deleteData(destinations[selectedItem].id)
                destinations.removeAt(selectedItem)
                notifyItemRemoved(selectedItem)
                Toast.makeText(itemView.context, "Long press, deleting $selectedItem", Toast.LENGTH_SHORT).show()
                val mp = MediaPlayer.create(itemView.context, R.raw.deletesound)
                mp.start()
                return@setOnLongClickListener true
            }
        }
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        // Inflate a layout from our XML (row_item.XML) and return the holder

        Log.d(TAG, "onCreateViewHolder: ${count++}")

        // create a new view
        val view = LayoutInflater.from(parent.context).inflate(R.layout.row_item, parent, false)
        return MyViewHolder(view)
    }

    // Replace/bind the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        val currentItem = destinations[position]
        holder.destinationName.text = currentItem.name
        Glide.with(holder.itemView.context)
            .load(currentItem.image)
            .into(holder.destinationImage)
        holder.description.text = currentItem.description
        holder.rating.rating = currentItem.rating

    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount(): Int {
        return destinations.size -1
    }
}