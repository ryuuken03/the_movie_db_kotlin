package mohammad.toriq.tmdb.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/***
 * Created By Mohammad Toriq on 19/10/2023
 */
data class Video (
    @SerializedName("iso_639_1")
    @Expose
    var iso_639_1: String? = null,

    @SerializedName("iso_3166_1")
    @Expose
    var iso_3166_1: String? = null,

    @SerializedName("name")
    @Expose
    var name: String? = null,

    @SerializedName("key")
    @Expose
    var key: String? = null,

    @SerializedName("site")
    @Expose
    var site: String? = null,

    @SerializedName("size")
    @Expose
    var size: Int = 0,

    @SerializedName("type")
    @Expose
    var type: String? = null,

    @SerializedName("official")
    @Expose
    var official: Boolean? = null,

    @SerializedName("published_at")
    @Expose
    var publishedAt: String? = null,

    @SerializedName("id")
    @Expose
    var id: String? = null,
)