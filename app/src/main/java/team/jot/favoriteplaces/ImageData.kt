package team.jot.favoriteplaces

data class ImageData(
    val id: Int,
    val author: String,
    val width: Int,
    val height: Int,
    val url: String,
    val download_url: String
)