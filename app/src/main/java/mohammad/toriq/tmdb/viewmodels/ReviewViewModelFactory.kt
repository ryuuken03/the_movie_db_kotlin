package mohammad.toriq.tmdb.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

/***
 * Created By Mohammad Toriq on 18/10/2023
 */
public class ReviewViewModelFactory (
    private val context: Context
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ReviewViewModel(context) as T
    }
}