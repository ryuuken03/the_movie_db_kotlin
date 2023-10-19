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
import mohammad.toriq.tmdb.models.ResponseMovies
import mohammad.toriq.tmdb.util.Repository
import mohammad.toriq.tmdb.util.Resource
import mohammad.toriq.tmdb.util.Util
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/***
 * Created By Mohammad Toriq on 18/10/2023
 */
public class MovieViewModel(
    context : Context
) : ViewModel() {
    private val context = context
    private val repository =  Repository(context)
    private val token  = "bearer "+context.getString(R.string.token)
    val _movies = MutableLiveData<Resource<ResponseMovies>>()
    val movies : LiveData<Resource<ResponseMovies>>
        get() = _movies

    fun getMovies(page : Int = 1, withGenres :String? = null) = viewModelScope.launch {
        _movies.postValue(Resource.Loading(true))
        try {
            repository.getDiscoverMovies(token,page, withGenres)?.enqueue(
                object : Callback<ResponseMovies> {
                    override fun onResponse(
                        call: Call<ResponseMovies>,
                        response: Response<ResponseMovies>
                    ) {
                        _movies.postValue(Resource.Loading(false))
                        if (response.isSuccessful) {
                            var tmp = response.body()!!
                            _movies.postValue(Resource.Success(tmp))
                        }else{
                            _movies.postValue(Resource.Failure(Util.parseError(response).toString()))
                        }
                    }
                    @SuppressLint("ResourceType")
                    override fun onFailure(call: Call<ResponseMovies>, t: Throwable) {
                        _movies.postValue(Resource.Loading(false))
                        var errorMessage = Util.parseThrowable(context,t)
                        _movies.postValue(Resource.Failure(errorMessage))
                    }
                }
            )
        } catch (e: Exception) {
            _movies.postValue(Resource.Failure(e.message.toString()))
        } finally {
            _movies.postValue(Resource.Loading(false))
        }
    }
}