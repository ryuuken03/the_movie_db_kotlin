package mohammad.toriq.tmdb.util

import android.content.Context
import mohammad.toriq.tmdb.models.Movie
import mohammad.toriq.tmdb.models.ResponseGenres
import mohammad.toriq.tmdb.models.ResponseMovies
import mohammad.toriq.tmdb.models.ResponseReviews
import mohammad.toriq.tmdb.models.ResponseVideos
import retrofit2.Call

/***
 * Created By Mohammad Toriq on 18/10/2023
 */
class Repository (context: Context) {
    var service = Util.getAPIHelper(context)

    fun getMovies(token : String,
                  category : String,
                  language : String = "en-Us"): Call<ResponseMovies>? {
        return service?.getMovies(
            token,
            category,
            language
        )
    }
    fun getDiscoverMovies(token : String,
                          page : Int,
                          withGenres : String? = null,
                          language : String = "en-Us"): Call<ResponseMovies>? {
        return service?.getDiscoverMovies(
            token,
            page,
            withGenres,
            language
        )
    }
    fun getGenres(token : String,
                          language : String = "en-Us"): Call<ResponseGenres>? {
        return service?.getGenres(
            token,
            language
        )
    }
    fun getDetailMovie(token : String,
                          id : Long): Call<Movie>? {
        return service?.getDetailMovie(
            token,
            id
        )
    }
    fun getMovieReviews(token : String,
                        id : Long,
                        page : Int,): Call<ResponseReviews>? {
        return service?.getMovieReviews(
            token,
            id,
            page
        )
    }
    fun getMovieVideos(token : String,
                          id : Long): Call<ResponseVideos>? {
        return service?.getMovieVideos(
            token,
            id
        )
    }
}