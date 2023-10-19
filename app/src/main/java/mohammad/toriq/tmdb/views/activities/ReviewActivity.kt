package mohammad.toriq.tmdb.views.activities

import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.adapters.ItemAdapter
import mohammad.toriq.tmdb.R
import mohammad.toriq.tmdb.databinding.ActivityReviewBinding
import mohammad.toriq.tmdb.models.Genre
import mohammad.toriq.tmdb.models.ResponseReviews
import mohammad.toriq.tmdb.util.InitializerUi
import mohammad.toriq.tmdb.util.Resource
import mohammad.toriq.tmdb.viewmodels.ReviewViewModel
import mohammad.toriq.tmdb.viewmodels.ReviewViewModelFactory
import mohammad.toriq.tmdb.views.adapters.AdapterGenre
import mohammad.toriq.tmdb.views.adapters.AdapterReview
import java.util.Timer
import java.util.TimerTask

/***
 * Created By Mohammad Toriq on 18/10/2023
 */
class ReviewActivity : AppCompatActivity() , InitializerUi {

    lateinit var binding: ActivityReviewBinding
    lateinit var adapter: ItemAdapter<AdapterReview>
    lateinit var fastAdapter: FastAdapter<AdapterReview>
    private lateinit var viewModel: ReviewViewModel
    private var isFirst = true
    private var isMax = false
    private var id : Long = -1
    private var title : String? = null
    private var page = 1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReviewBinding.inflate(layoutInflater)
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
                            viewModel.getMovieReviews(id,page)
                        }
                        runOnUiThread {
                            binding.progress.visibility = View.GONE
                            binding.revListData.visibility = View.VISIBLE
                            binding.textDataNotFound.visibility = View.GONE
                        }
                    }
                }, 500)
            }
        }
    }

    override fun initConfig() {
        val factory = ReviewViewModelFactory(this)
        viewModel = ViewModelProvider(this, factory)[ReviewViewModel::class.java]
        if(intent.getLongExtra("id",-1) != -1.toLong()){
            id = intent.getLongExtra("id",-1)
            title = intent.getStringExtra("title")
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
        binding.toolbar.title.text = "Reviews "
        binding.revListData.layoutManager = LinearLayoutManager(this)
        binding.revListData.adapter = fastAdapter
        binding.revListData.animation = null
        binding.revListData.isNestedScrollingEnabled = false

        viewModel.reviews.observe(this){ resource ->
            when (resource) {
                is Resource.Loading -> showLoading(resource.state)
                is Resource.Success -> loadData(resource.data)
                is Resource.Failure -> showDataError(resource.errorMessage)
            }
        }

        viewModel.getMovieReviews(id)

        setListener()
    }

    override fun setListener() {
        binding.toolbar.btnBack.setOnClickListener {
            finish()
        }

        binding.revListData.setOnScrollListener(object : RecyclerView.OnScrollListener(){

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                var mLayoutManager = binding.revListData.layoutManager as LinearLayoutManager
                val firstPos: Int = mLayoutManager.findFirstCompletelyVisibleItemPosition()
                val lastPost: Int = mLayoutManager.findLastCompletelyVisibleItemPosition()
                if(adapter.adapterItemCount == lastPost+1){
                    if(!isMax){
                        page++
                        viewModel.getMovieReviews(id,page)
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
        }
    }

    fun loadData(response : ResponseReviews){
        binding.progress.visibility = View.GONE
        binding.revListData.visibility = View.VISIBLE
        binding.textDataNotFound.visibility = View.GONE
        var data = response.reviews
        if(response.page == response.totalPages){
            isMax = true
        }
        data.forEach {
            var data = AdapterReview(this)
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