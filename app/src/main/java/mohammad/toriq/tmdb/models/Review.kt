package mohammad.toriq.tmdb.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/***
 * Created By Mohammad Toriq on 19/10/2023
 */
data class Review (

    @SerializedName("id")
    @Expose
    var id: String? = null,

    @SerializedName("author")
    @Expose
    var author: String? = null,

    @SerializedName("author_details")
    @Expose
    var authorDetails: Author? = null,

    @SerializedName("content")
    @Expose
    var content: String? = null,

    @SerializedName("created_at")
    @Expose
    var createdAt: String? = null,

    @SerializedName("updated_at")
    @Expose
    var updatedAt: String? = null,

    @SerializedName("url")
    @Expose
    var url: String? = null,

    )