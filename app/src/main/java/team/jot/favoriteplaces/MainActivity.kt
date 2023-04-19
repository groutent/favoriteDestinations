package team.jot.favoriteplaces

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class MainActivity : AppCompatActivity() {

    private val FILE_NAME = "Destinations"
    lateinit var viewModel: DestinationViewModel
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

    // Load the previously saved destinations
    fun loadData() {
        // Create an instance of getSharedPreferences for retrieve the data
        val sharedPreferences = getSharedPreferences(FILE_NAME, MODE_PRIVATE)
        // Retrieve data using the key, default value is empty string in case no saved data in there
        val tasks = sharedPreferences.getString("ratings", "") ?: ""

        if (tasks.isNotEmpty()) {
            val gson = Gson()
            val sType = object : TypeToken<MutableList<Destination>>() {}.type
            viewModel.setAllRatings(gson.fromJson<MutableList<Destination>>(tasks, sType))
        }
    }
}