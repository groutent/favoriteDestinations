package team.jot.favoriteplaces

import android.Manifest
import android.annotation.SuppressLint
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.location.Location
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.location.CurrentLocationRequest
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import org.w3c.dom.Text
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.ByteArrayOutputStream

class DestinationCreation : AppCompatActivity() {

    val index = 0

    //To access your database, instantiate your subclass of SQLiteOpenHelper
    private val dbHelper = ContactDbHelper(this)

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_destination_creation)

        //val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        //startActivity(cameraIntent)
    }


    //Cancel Button, don't save any changes simply return to main activity
    fun cancel(view: View) {
        finish()
    }


    //Save Button
    fun returnDataToFirstActivity(view: View) {
        val mp = MediaPlayer.create(this, R.raw.createsound)
        mp.start()
        dbHelper.insertData(
            findViewById<EditText>(R.id.destinationName).text.toString(),
            findViewById<EditText>(R.id.destinationDescription).text.toString(),
            getByteArray(),
            findViewById<RatingBar>(R.id.rating).rating, "0", "0"
        )
        val myIntent = Intent()
        setResult(Activity.RESULT_OK, myIntent)
        finish()
    }


    fun getByteArray(): ByteArray {
        val bitmap =
            (findViewById<ImageView>(R.id.destinationImage).drawable as BitmapDrawable).getBitmap()
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 90, stream)
        val image = stream.toByteArray()
        return image
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

    fun updateIcon(view: View) {
        // Retrofit build information
        val BASE_URL = "https://picsum.photos/id/"
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val randomColorAPI = retrofit.create(RandomColorService::class.java)

        //Generate random int within range of 0 to 160
        val randInt = (0..160).random()

        randomColorAPI.getColorInfo(randInt).enqueue(object :
            Callback<ColorData> {

            override fun onFailure(call: Call<ColorData>, t: Throwable) {
                Log.d(TAG, "onFailure : $t")
            }

            override fun onResponse(call: Call<ColorData>, response: Response<ColorData>) {
                Log.d(TAG, "onResponse: $response")

                val body = response.body()
                if (body == null) {
                    Log.w(TAG, "Valid response was not received")
                    return
                }

                // The following log messages are just for testing purpose
                //  Log.d(TAG, ": ${body.r}")
               // findViewById<ImageView>(R.id.destinationImage).setImageView
            }

        })
    }
}