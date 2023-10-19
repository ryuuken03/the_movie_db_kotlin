package mohammad.toriq.tmdb.views.activities

import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.adapters.ItemAdapter
import mohammad.toriq.tmdb.R
import mohammad.toriq.tmdb.databinding.ActivityGenreBinding
import mohammad.toriq.tmdb.models.Genre
import mohammad.toriq.tmdb.util.InitializerUi
import mohammad.toriq.tmdb.util.Resource
import mohammad.toriq.tmdb.viewmodels.GenreViewModel
import mohammad.toriq.tmdb.viewmodels.GenreViewModelFactory
import mohammad.toriq.tmdb.views.adapters.AdapterGenre
import java.util.Timer
import java.util.TimerTask

/***
 * Created By Mohammad Toriq on 18/10/2023
 */
class GenreActivity : AppCompatActivity() , InitializerUi {

    lateinit var binding: ActivityGenreBinding
    lateinit var adapter: ItemAdapter<AdapterGenre>
    lateinit var fastAdapter: FastAdapter<AdapterGenre>
    private lateinit var viewModel: GenreViewModel
    private var isFirst = true
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGenreBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
    }

    override fun onResume() {
        super.onResume()
        if(isFirst){
            initConfig()
        }else{
            runOnUiThread {
                showLoading(true)
                Timer().schedule(object : TimerTask() {
                    override fun run() {
                        if(adapter.adapterItemCount == 0){
                            viewModel.getGenres()
                        }
                        runOnUiThread {
                            showLoading(false)
                        }
                    }
                }, 500)
            }
        }
    }

    override fun initConfig() {
        val factory = GenreViewModelFactory(this)
        viewModel = ViewModelProvider(this, factory)[GenreViewModel::class.java]
        adapter = ItemAdapter()
        fastAdapter = FastAdapter.with(adapter)
        initUI()
    }

    override fun initUI() {
        if(isFirst){
            isFirst = false
        }
        binding.toolbar.btnBack.visibility = View.GONE
        binding.toolbar.view.visibility = View.GONE
        binding.toolbar.title.text = "Genre "+getString(R.string.app_name)
        binding.toolbar.title.gravity = Gravity.CENTER
        binding.revListData.layoutManager = GridLayoutManager(this,2)
        binding.revListData.adapter = fastAdapter
        binding.revListData.animation = null
        binding.revListData.isNestedScrollingEnabled = false

        viewModel.genres.observe(this){ resource ->
            when (resource) {
                is Resource.Loading -> showLoading(resource.state)
                is Resource.Success -> loadData(resource.data)
                is Resource.Failure -> showDataError(resource.errorMessage)
            }
        }

        viewModel.getGenres()

        setListener()
    }

    override fun setListener() {

        fastAdapter.onClickListener = { view, adapter, item, position ->
            var intent = Intent(this@GenreActivity, MovieActivity::class.java)
            intent.putExtra("genre",item.id)
            intent.putExtra("title",item.name)
            startActivity(intent)
            false
        }
    }

    fun showLoading(loading : Boolean){
        if(loading){
            binding.progress.visibility = View.VISIBLE
            binding.revListData.visibility = View.GONE
            binding.textDataNotFound.visibility = View.GONE
        }else{
            binding.progress.visibility = View.GONE
            binding.revListData.visibility = View.VISIBLE
            binding.textDataNotFound.visibility = View.GONE
        }
    }

    fun loadData(data : ArrayList<Genre>){
        data.forEach {
            var data = AdapterGenre(this)
            data.id = it.id
            data.name = it.name
            adapter.add(data)
        }
    }

    fun showDataError(message : String? = null){
        binding.progress.visibility = View.GONE
        binding.revListData.visibility = View.GONE
        binding.textDataNotFound.visibility = View.VISIBLE
        if(message!=null){
            binding.textDataNotFound.text = message
        }
    }

}