package mohammad.toriq.tmdb.views.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.adapters.ItemAdapter
import mohammad.toriq.tmdb.R
import mohammad.toriq.tmdb.databinding.ActivityMovieBinding
import mohammad.toriq.tmdb.models.ResponseMovies
import mohammad.toriq.tmdb.util.InitializerUi
import mohammad.toriq.tmdb.util.Resource
import mohammad.toriq.tmdb.viewmodels.MovieViewModel
import mohammad.toriq.tmdb.viewmodels.MovieViewModelFactory
import mohammad.toriq.tmdb.views.adapters.AdapterMovie
import java.util.Timer
import java.util.TimerTask

/***
 * Created By Mohammad Toriq on 18/10/2023
 */
class MovieActivity : AppCompatActivity() , InitializerUi {

    lateinit var binding: ActivityMovieBinding
    lateinit var adapter: ItemAdapter<AdapterMovie>
    lateinit var fastAdapter: FastAdapter<AdapterMovie>
    private lateinit var viewModel: MovieViewModel
    private var isFirst = true
    private var isMax = false
    private var page = 1
    private var title = ""
    private var genre : Long = -1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMovieBinding.inflate(layoutInflater)
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
                            page = 1
                            viewModel.getMovies(1,genre.toString())
                        }
                        runOnUiThread{
                            showLoading(false)
                        }
                    }
                }, 500)
            }
        }
    }

    override fun initConfig() {
        val factory = MovieViewModelFactory(this)
        viewModel = ViewModelProvider(this, factory)[MovieViewModel::class.java]
        if(intent.getStringExtra("title")!=null){
            title = intent.getStringExtra("title")!!
        }
        if(intent.getLongExtra("genre",-1) != -1.toLong()){
            genre = intent.getLongExtra("genre",-1)
            adapter = ItemAdapter()
            fastAdapter = FastAdapter.with(adapter)
            initUI()
        }else{
            finish()
        }
    }

    override fun initUI() {
        if(isFirst){
            isFirst = false
        }
        binding.toolbar.title.text = title+" "+getString(R.string.app_name)
        binding.revListData.layoutManager = GridLayoutManager(this,2)
        binding.revListData.adapter = fastAdapter
        binding.revListData.animation = null
        binding.revListData.isNestedScrollingEnabled = false

        viewModel.movies.observe(this){ resource ->
            when (resource) {
                is Resource.Loading -> showLoading(resource.state)
                is Resource.Success -> loadData(resource.data)
                is Resource.Failure -> showDataError(resource.errorMessage)
            }
        }

        viewModel.getMovies(page,genre.toString())

        setListener()
    }

    override fun setListener() {
        binding.toolbar.btnBack.setOnClickListener {
            finish()
        }

        fastAdapter.onClickListener = { view, adapter, item, position ->
            var intent = Intent(this@MovieActivity, DetailMovieActivity::class.java)
            intent.putExtra("title",item.data?.title)
            intent.putExtra("id",item.data?.id)
            startActivity(intent)
            false
        }

        binding.revListData.setOnScrollListener(object : RecyclerView.OnScrollListener(){

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                var mLayoutManager = binding.revListData.layoutManager as LinearLayoutManager
                val firstPos: Int = mLayoutManager.findFirstCompletelyVisibleItemPosition()
                val lastPost: Int = mLayoutManager.findLastCompletelyVisibleItemPosition()
                if(adapter.adapterItemCount == lastPost+1){
                    if(!isMax){
                        page++
                        viewModel.getMovies(page,genre.toString())
                    }
                }
            }
        })
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

    fun refreshData(){
        adapter.clear()
    }

    fun loadData(response : ResponseMovies){
        var data = response.movies
        if(response.page == response.totalPages){
            isMax = true
        }
        data.forEach {
            var data = AdapterMovie(this)
            data.data = it
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