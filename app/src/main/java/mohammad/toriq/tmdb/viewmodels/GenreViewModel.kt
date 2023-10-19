package mohammad.toriq.tmdb.viewmodels

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import mohammad.toriq.tmdb.R
import mohammad.toriq.tmdb.models.Genre
import mohammad.toriq.tmdb.models.ResponseGenres
import mohammad.toriq.tmdb.util.Repository
import mohammad.toriq.tmdb.util.Resource
import mohammad.toriq.tmdb.util.Util
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/***
 * Created By Mohammad Toriq on 18/10/2023
 */
public class GenreViewModel(
    context : Context
) : ViewModel() {
    private val context = context
    private val repository =  Repository(context)
    private val token  = "bearer "+context.getString(R.string.token)
    val _genres = MutableLiveData<Resource<ArrayList<Genre>>>()
    val genres : LiveData<Resource<ArrayList<Genre>>>
        get() = _genres

    fun getGenres() = viewModelScope.launch {
        _genres.postValue(Resource.Loading(true))
        try {
            repository.getGenres(token)?.enqueue(
                object : Callback<ResponseGenres> {
                    override fun onResponse(
                        call: Call<ResponseGenres>,
                        response: Response<ResponseGenres>
                    ) {
                        _genres.postValue(Resource.Loading(false))
                        if (response.isSuccessful) {
                            var tmp = response.body()!!
                            _genres.postValue(Resource.Success(tmp.genres))
                        }else{
                            _genres.postValue(Resource.Failure(Util.parseError(response).toString()))
                        }
                    }
                    @SuppressLint("ResourceType")
                    override fun onFailure(call: Call<ResponseGenres>, t: Throwable) {
                        _genres.postValue(Resource.Loading(false))
                        var errorMessage = Util.parseThrowable(context,t)
                        _genres.postValue(Resource.Failure(errorMessage))
                    }
                }
            )
        } catch (e: Exception) {
            _genres.postValue(Resource.Failure(e.message.toString()))
        } finally {
            _genres.postValue(Resource.Loading(false))
        }
    }
}