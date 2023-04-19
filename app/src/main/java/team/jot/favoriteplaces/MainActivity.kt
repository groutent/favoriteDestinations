package team.jot.favoriteplaces

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout

class MainActivity : AppCompatActivity() {

    private val TAG = "MainActivity"
    private var refreshCount = 0
    private var destinations = mutableListOf<Destination>()

    //Updates current recycler view with new dataset
    //recycler_view.adapter = MyRecyclerAdapter(updatedDataSet)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Store the the recyclerView widget in a variable
        val recyclerView = findViewById<RecyclerView>(R.id.recycler_view)

        destinations = generateContact(20)
        // specify an viewAdapter for the dataset (we use dummy data containing 20 contacts)
        recyclerView.adapter = MyRecyclerAdapter(destinations)

        // use a linear layout manager, you can use different layouts as well
        recyclerView.layoutManager = LinearLayoutManager(this)

        // if you want, you can make the layout of the recyclerview horizontal as follows
        //recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)


        // Add a divider between rows -- Optional
        val dividerItemDecoration = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        recyclerView.addItemDecoration(dividerItemDecoration)
    }


    // A helper function to create specified amount of dummy contact data
    private fun generateContact(size: Int) : MutableList<Destination>{
        // Create an ArrayList to store contacts
        val destinations = mutableListOf<Destination>()

        // The for loop will generate [size] amount of contact data and store in a list
        for (i in 1..size) {
            val location = Destination("Test Location-$i", R.drawable.test, "This is a test description. Users will be able to create their own.", 2.5F)
            destinations.add(location)
        }

        // Reverse the array so that the newly added data can be seen at the top of the list
        destinations.reverse()

        // return the list of contacts
        return destinations
    }
}