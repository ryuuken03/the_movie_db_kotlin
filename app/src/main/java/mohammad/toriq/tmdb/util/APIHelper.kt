package mohammad.toriq.tmdb.util

import mohammad.toriq.tmdb.models.Movie
import mohammad.toriq.tmdb.models.ResponseGenres
import mohammad.toriq.tmdb.models.ResponseMovies
import mohammad.toriq.tmdb.models.ResponseReviews
import mohammad.toriq.tmdb.models.ResponseVideos
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

/***
 * Created By Mohammad Toriq on 18/10/2023
 */
interface APIHelper {

    @GET("movie/{category}")
    fun getMovies(
        @Header("Authorization") token: String,
        @Path(value = "category", encoded = true) category: String,
        @Query(value = "language", encoded = true) language: String,
    ): Call<ResponseMovies>

    @GET("discover/movie")
    fun getDiscoverMovies(
        @Header("Authorization") token: String,
        @Query(value = "page", encoded = true) page: Int,
        @Query(value = "with_genres", encoded = true) withGenres: String?,
        @Query(value = "language", encoded = true) language: String,
    ): Call<ResponseMovies>

    @GET("genre/movie/list")
    fun getGenres(
        @Header("Authorization") token: String,
        @Query(value = "language", encoded = true) language: String,
    ): Call<ResponseGenres>

    @GET("movie/{id}")
    fun getDetailMovie(
        @Header("Authorization") token: String,
        @Path(value = "id", encoded = true) id: Long,
    ): Call<Movie>


    @GET("movie/{id}/reviews")
    fun getMovieReviews(
        @Header("Authorization") token: String,
        @Path(value = "id", encoded = true) id: Long,
        @Query(value = "page", encoded = true) page: Int,
    ): Call<ResponseReviews>


    @GET("movie/{id}/videos")
    fun getMovieVideos(
        @Header("Authorization") token: String,
        @Path(value = "id", encoded = true) id: Long,
    ): Call<ResponseVideos>

}