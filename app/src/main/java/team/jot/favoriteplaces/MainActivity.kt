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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Store the the recyclerView widget in a variable
        val recyclerView = findViewById<RecyclerView>(R.id.recycler_view)

        // specify an viewAdapter for the dataset (we use dummy data containing 20 contacts)
        recyclerView.adapter = MyRecyclerAdapter(generateContact(20))

        // use a linear layout manager, you can use different layouts as well
        recyclerView.layoutManager = LinearLayoutManager(this)

        // if you want, you can make the layout of the recyclerview horizontal as follows
        //recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)


        // Add a divider between rows -- Optional
        val dividerItemDecoration = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        recyclerView.addItemDecoration(dividerItemDecoration)
    }


    // A helper function to create specified amount of dummy contact data
    private fun generateContact(size: Int) : ArrayList<Destination>{
        // Create an ArrayList to store contacts
        val contacts = ArrayList<Destination>()

        // The for loop will generate [size] amount of contact data and store in a list
        for (i in 1..size) {
            val person = Destination("Test Location-$i", R.drawable.test, "This is a test description. Users will be able to create their own.", 2.5F)
            contacts.add(person)
        }

        // Reverse the array so that the newly added data can be seen at the top of the list
        contacts.reverse()

        // return the list of contacts
        return contacts
    }
}