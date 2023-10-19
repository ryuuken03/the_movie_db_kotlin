package mohammad.toriq.tmdb.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/***
 * Created By Mohammad Toriq on 19/10/2023
 */
data class Author(
    @SerializedName("name")
    @Expose
    var name: String? = null,

    @SerializedName("username")
    @Expose
    var username: String? = null,

    @SerializedName("avatar_path")
    @Expose
    var avatarPath: String? = null,

    @SerializedName("rating")
    @Expose
    var rating: Double? = null,

    )