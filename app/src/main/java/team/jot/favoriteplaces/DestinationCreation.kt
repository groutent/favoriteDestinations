package team.jot.favoriteplaces

import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Intent
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.RatingBar
import android.widget.Toast
import com.bumptech.glide.Glide
import com.google.android.gms.location.FusedLocationProviderClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.random.Random

class DestinationCreation : AppCompatActivity() {

    val index = 0

    //To access your database, instantiate your subclass of SQLiteOpenHelper
    private val dbHelper = DestinationDbHelper(this)
    private var image = ""
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_destination_creation)
        //val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        //startActivity(cameraIntent)

        // Retrofit stuff
        val BASE_URL = "https://picsum.photos/id/"
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()


        val randomImageAPI = retrofit.create(RandomImageService::class.java)
        randomImageAPI.getImage(Random.nextInt(0,1000)).enqueue(object :
            Callback<ImageData> {

            override fun onFailure(call: Call<ImageData>, t: Throwable) {
                Log.d(TAG, "onFailure : $t")
            }

            override fun onResponse(call: Call<ImageData>, response: Response<ImageData>) {
                Log.d(TAG, "onResponse: $response")

                val body = response.body()
                if (body == null){
                    Log.w(TAG, "Valid response was not received")
                    return
                }
                Log.w(TAG, body.url)
                image = body.download_url
                Glide.with(this@DestinationCreation)
                    .load(image)
                    .into(findViewById(R.id.imageButton))
            }

        })
    }



    //Cancel Button, don't save any changes simply return to main activity
    fun cancel(view: View){
        finish()
    }


    //Save Button
    fun returnDataToFirstActivity(view: View) {
        val desName = findViewById<EditText>(R.id.destinationName).text.toString()
        // validates user input to make sure a destination name was received
        if(desName == ""){
            Toast.makeText(this, "Please enter a destination name :)", Toast.LENGTH_SHORT).show()
        }
        // validates that image from API has been loaded in
        else if(image == "") {
            Toast.makeText(this, "Please wait for the image to load :)", Toast.LENGTH_SHORT).show()
        }
        else {
            // media player for sound of creation of destination place
            val mp = MediaPlayer.create(this, R.raw.createsound)
            mp.start()

            dbHelper.insertData(
                findViewById<EditText>(R.id.destinationName).text.toString(),
                findViewById<EditText>(R.id.destinationDescription).text.toString(),
                image,
                findViewById<RatingBar>(R.id.rating).rating, "0", "0"
            )

            val myIntent = Intent()
            setResult(Activity.RESULT_OK, myIntent)
            finish()
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