package team.jot.favoriteplaces

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


class MainActivity : AppCompatActivity() {
    //Needed variables in order to instantiate activities, recyclerview and database.
    private val TAG = "MainActivity"
    var destinations = mutableListOf<Destination>()
    lateinit private var recyclerView: RecyclerView
    lateinit var destinationCreationLauncher: ActivityResultLauncher<Intent>
    lateinit var newPlacesOnMapLauncher: ActivityResultLauncher<Intent>
    var adapter = MyRecyclerAdapter(destinations)

    //To access your database, instantiate your subclass of SQLiteOpenHelper
    private val dbHelper = DestinationDbHelper(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        getAllData()
        

        // Store the the recyclerView widget in a variable
        recyclerView = findViewById<RecyclerView>(R.id.recycler_view)
        // specify an viewAdapter for the dataset (we use dummy data containing 20 contacts)
        recyclerView.adapter = adapter

        // use a linear layout manager, you can use different layouts as well
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Add a divider between rows -- Optional
        val dividerItemDecoration = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        recyclerView.addItemDecoration(dividerItemDecoration)

        destinationCreationLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                getAllData()
            }
        }
        newPlacesOnMapLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                getAllData()
            }
        }


    }


    //Function that opens the creation of a favorite destination on button click
    fun createDestination(view: View){
        var myIntent = Intent(this, DestinationCreation::class.java)
        destinationCreationLauncher.launch(myIntent)
    }
    //Function that opens the map search activity on button click
    fun findNewDestination(view: View){
        var myIntent = Intent(this, MapsActivity::class.java)
        newPlacesOnMapLauncher.launch(myIntent)
    }

    /*
     * Queries the database to get all records in the database
     * This function uses the dbHelper class to interact with the database.
     */
    fun getAllData() {
        var newDestinations = mutableListOf<Destination>()
        try {
            val cursor = dbHelper.viewAllData
            if (cursor.count == 0) {
                return
            }

            while (cursor.moveToNext()) {
                newDestinations.add(Destination(cursor.getInt(0), cursor.getString(1),cursor.getString(2),cursor.getString(3),cursor.getFloat(4),cursor.getDouble(5),cursor.getDouble(6)))
            }
            destinations.removeAll(destinations)
            newDestinations.reverse()
            destinations.addAll(newDestinations)
            adapter.notifyDataSetChanged()
        } catch (e: Exception) {
            Log.e(TAG, "error: $e")
        }
    }

    /*
     * Since getWritableDatabase() and getReadableDatabase() are expensive to call when the database
     * is closed, you should leave your database connection open for as long as you possibly need to access it.
     * Typically, it is optimal to close the database in the onDestroy() of the calling Activity.
     */
    override fun onDestroy() {
        dbHelper.close()
        super.onDestroy()
    }
}