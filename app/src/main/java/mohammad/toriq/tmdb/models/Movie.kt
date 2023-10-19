package mohammad.toriq.tmdb.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


/***
 * Created By Mohammad Toriq on 18/10/2023
 */
data class Movie (

    @SerializedName("id")
    @Expose
    var id: Long = 0,

    @SerializedName("title")
    @Expose
    var title: String? = null,

    @SerializedName("overview")
    @Expose
    var desc: String? = null,

    @SerializedName("original_title")
    @Expose
    var originalTitle: String? = null,

    @SerializedName("popularity")
    @Expose
    var popularity: Double = 0.0,

    @SerializedName("backdrop_path")
    @Expose
    var imageUrlBackDrop: String? = null,

    @SerializedName("poster_path")
    @Expose
    var imageUrl: String? = null,

    @SerializedName("vote_average")
    @Expose
    var voteAverage: Float = 0f,

    @SerializedName("vote_count")
    @Expose
    var voteCount: Long = 0,

    @SerializedName("release_date")
    @Expose
    var releaseDate: String? = null,

    @SerializedName("runtime")
    @Expose
    var runtime: Int = 0,

    @SerializedName("budget")
    @Expose
    var budget: Long = 0,

    @SerializedName("genres")
    @Expose
    var genres: ArrayList<Genre>? = null,

    @SerializedName("homepage")
    @Expose
    var homepage: String? = null,

    @SerializedName("status")
    @Expose
    var status: String? = null,

    )