package mohammad.toriq.tmdb.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

/***
 * Created By Mohammad Toriq on 18/10/2023
 */
data class ResponseGenres (
    @SerializedName("genres")
    @Expose
    var genres : ArrayList<Genre> = ArrayList<Genre>(),
)