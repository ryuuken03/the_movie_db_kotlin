package mohammad.toriq.tmdb.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable


/***
 * Created By Mohammad Toriq on 18/10/2023
 */
data class Genre (

    @SerializedName("id")
    @Expose
    var id: Long = 0,

    @SerializedName("name")
    @Expose
    var name: String? = null,

)