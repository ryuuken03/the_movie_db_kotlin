package mohammad.toriq.tmdb.viewmodels

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import mohammad.toriq.tmdb.R
import mohammad.toriq.tmdb.models.Movie
import mohammad.toriq.tmdb.models.ResponseVideos
import mohammad.toriq.tmdb.models.Video
import mohammad.toriq.tmdb.util.Repository
import mohammad.toriq.tmdb.util.Resource
import mohammad.toriq.tmdb.util.Util
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/***
 * Created By Mohammad Toriq on 18/10/2023
 */
public class DetailMovieViewModel(
    context : Context
) : ViewModel() {
    private val context = context
    private val repository =  Repository(context)
    private val token  = "bearer "+context.getString(R.string.token)
    val _movie = MutableLiveData<Resource<Movie>>()
    val movie : LiveData<Resource<Movie>>
        get() = _movie
    val _videos = MutableLiveData<ArrayList<Video>>()
    val videos : LiveData<ArrayList<Video>>
        get() = _videos

    fun getDetailMovie(id : Long) = viewModelScope.launch {
        _movie.postValue(Resource.Loading(true))
        try {
            repository.getDetailMovie(token,id)?.enqueue(
                object : Callback<Movie> {
                    override fun onResponse(
                        call: Call<Movie>,
                        response: Response<Movie>
                    ) {
                        _movie.postValue(Resource.Loading(false))
                        if (response.isSuccessful) {
                            var tmp = response.body()!!
                            _movie.postValue(Resource.Success(tmp))
                        }else{
                            _movie.postValue(Resource.Failure(Util.parseError(response).toString()))
                        }
                    }
                    @SuppressLint("ResourceType")
                    override fun onFailure(call: Call<Movie>, t: Throwable) {
                        _movie.postValue(Resource.Loading(false))
                        var errorMessage = Util.parseThrowable(context,t)
                        _movie.postValue(Resource.Failure(errorMessage))
                    }
                }
            )
        } catch (e: Exception) {
            _movie.postValue(Resource.Failure(e.message.toString()))
        } finally {
            _movie.postValue(Resource.Loading(false))
        }
    }
    fun getMovieVideos(id : Long) = viewModelScope.launch {
        try {
            repository.getMovieVideos(token,id)?.enqueue(
                object : Callback<ResponseVideos> {
                    override fun onResponse(
                        call: Call<ResponseVideos>,
                        response: Response<ResponseVideos>
                    ) {
                        if (response.isSuccessful) {
                            var tmp = response.body()!!
                            _videos.postValue(tmp.videos)
                        }else{
                        }
                    }
                    @SuppressLint("ResourceType")
                    override fun onFailure(call: Call<ResponseVideos>, t: Throwable) {
                    }
                }
            )
        } catch (e: Exception) {
        } finally {
        }
    }
}