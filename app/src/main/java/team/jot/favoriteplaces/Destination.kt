package team.jot.favoriteplaces

import android.graphics.drawable.Drawable
import android.location.Location
import android.media.Image

data class Destination(val id: Int,
                       val name: String,
                       val image: String,
                       val description: String,
                       val rating: Float,
                       val lat: Double,
                       val lng: Double
)