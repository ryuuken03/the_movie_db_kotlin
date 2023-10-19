package mohammad.toriq.tmdb.viewmodels

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import mohammad.toriq.tmdb.R
import mohammad.toriq.tmdb.models.ResponseReviews
import mohammad.toriq.tmdb.util.Repository
import mohammad.toriq.tmdb.util.Resource
import mohammad.toriq.tmdb.util.Util
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/***
 * Created By Mohammad Toriq on 18/10/2023
 */
public class ReviewViewModel(
    context : Context
) : ViewModel() {
    private val context = context
    private val repository =  Repository(context)
    private val token  = "bearer "+context.getString(R.string.token)
    val _reviews = MutableLiveData<Resource<ResponseReviews>>()
    val reviews : LiveData<Resource<ResponseReviews>>
        get() = _reviews

    fun getMovieReviews(id : Long, page : Int = 1) = viewModelScope.launch {
        _reviews.postValue(Resource.Loading(true))
        try {
            repository.getMovieReviews(token, id, page)?.enqueue(
                object : Callback<ResponseReviews> {
                    override fun onResponse(
                        call: Call<ResponseReviews>,
                        response: Response<ResponseReviews>
                    ) {
                        _reviews.postValue(Resource.Loading(false))
                        if (response.isSuccessful) {
                            var tmp = response.body()!!
                            _reviews.postValue(Resource.Success(tmp))
                        }else{
                            _reviews.postValue(Resource.Failure(Util.parseError(response).toString()))
                        }
                    }
                    @SuppressLint("ResourceType")
                    override fun onFailure(call: Call<ResponseReviews>, t: Throwable) {
                        _reviews.postValue(Resource.Loading(false))
                        var errorMessage = Util.parseThrowable(context,t)
                        _reviews.postValue(Resource.Failure(errorMessage))
                    }
                }
            )
        } catch (e: Exception) {
            _reviews.postValue(Resource.Failure(e.message.toString()))
        } finally {
            _reviews.postValue(Resource.Loading(false))
        }
    }
}