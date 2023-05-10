package team.jot.favoriteplaces;

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RandomColorService {

    // Requesting a random User info based on specified  nationality, https://randomuser.me/api/?nat=us
    // Adding the nationality parameter to your request
    // get(".") indicates that your url is the same as the base url
    @GET("url")
    fun getColorInfo(@Query("id") id: Int) : Call<ColorData>
}
