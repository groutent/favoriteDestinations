package team.jot.favoriteplaces

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider

class SecondActivity : AppCompatActivity() {

    private val FILE_NAME = "Destinations"
    lateinit var viewModel: DestinationViewModel
    private var destinationId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_destination_creation)

        // The following code accesses any extras passed in intent using methods like getIntExtra(), getStringExtra
        // Remember: intent is like a global variable that can be accessed in an Activity
        destinationId = intent.getIntExtra("destinationId", -1)

        // Update activity data for current destination
        if(destinationId > -1){
            initializeActivity()
        }
    }

    fun initializeActivity(){
        TODO("Using viewModel and destinationId fill in all elements for user to view/edit")
    }

    //Cancel Button, don't save any changes simply return to main activity
    fun cancel(){
        finish()
    }

    //Delete button, remove current destination from model, save, and return
    fun delete(){
        viewModel.removeDestination(destinationId)
        saveData()
        finish()
    }


    //Save Button
    fun returnDataToFirstActivity(view: View) {
        saveData()
        finish()
    }

    //save the data to SharedPreferences
    fun saveData() {
        // Create an instance of getSharedPreferences for edit
        val sharedPreferences = getSharedPreferences(FILE_NAME, MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        // Create an instance of Gson (make sure to include its dependency first to be able use gson)
        val gson = Gson()

        editor.putString("destinations", gson.toJson(viewModel.getAllDestinations()))
        // Apply the changes
        editor.apply()
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

            TODO("Initialize current activity if editing an exisiting destination. Otherwise leave as default/empty")
        }
    }
}