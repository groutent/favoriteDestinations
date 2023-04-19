package team.jot.favoriteplaces

import android.graphics.drawable.Drawable
import android.location.Location
import android.media.Image

data class Destination(val name: String,
                       val image: Int,
                       val description: String,
                       val rating: Float
)