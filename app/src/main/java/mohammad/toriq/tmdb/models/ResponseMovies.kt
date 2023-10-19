package mohammad.toriq.tmdb.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


/***
 * Created By Mohammad Toriq on 18/10/2023
 */
data class ResponseMovies (
    @SerializedName("page")
    @Expose
    var page: Long = 0,

    @SerializedName("results")
    @Expose
    var movies: ArrayList<Movie> = ArrayList<Movie>(),

    @SerializedName("total_pages")
    @Expose
    var totalPages: Long = 0,

    @SerializedName("total_results")
    @Expose
    var totalResults: Long = 0,
)