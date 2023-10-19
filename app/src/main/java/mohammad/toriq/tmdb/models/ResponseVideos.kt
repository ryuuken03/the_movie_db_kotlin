package mohammad.toriq.tmdb.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


/***
 * Created By Mohammad Toriq on 18/10/2023
 */
data class ResponseVideos (

    @SerializedName("results")
    @Expose
    var videos: ArrayList<Video> = ArrayList<Video>(),
)