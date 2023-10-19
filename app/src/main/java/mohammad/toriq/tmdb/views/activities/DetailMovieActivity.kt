package mohammad.toriq.tmdb.views.activities

import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.adapters.ItemAdapter
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import io.reactivex.rxjava3.annotations.NonNull
import mohammad.toriq.tmdb.configs.Constants
import mohammad.toriq.tmdb.databinding.ActivityDetailMovieBinding
import mohammad.toriq.tmdb.databinding.DialogListTrailerBinding
import mohammad.toriq.tmdb.databinding.DialogTrailerMovieBinding
import mohammad.toriq.tmdb.models.Movie
import mohammad.toriq.tmdb.models.Video
import mohammad.toriq.tmdb.util.InitializerUi
import mohammad.toriq.tmdb.util.Resource
import mohammad.toriq.tmdb.util.Util
import mohammad.toriq.tmdb.viewmodels.DetailMovieViewModel
import mohammad.toriq.tmdb.viewmodels.DetailMovieViewModelFactory
import mohammad.toriq.tmdb.views.adapters.AdapterVideoName
import java.util.Timer
import java.util.TimerTask


/***
 * Created By Mohammad Toriq on 19/10/2023
 */
class DetailMovieActivity : AppCompatActivity() , InitializerUi {

    lateinit var binding: ActivityDetailMovieBinding
    lateinit var adapterVideo: ItemAdapter<AdapterVideoName>
    lateinit var fastAdapterVideo: FastAdapter<AdapterVideoName>
    private lateinit var viewModel: DetailMovieViewModel
    private var isFirst = true
    private var title = ""
    private var trailers = ArrayList<Video>()
    private var id : Long = -1
    var detail : Movie ?= null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailMovieBinding.inflate(layoutInflater)
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
//                        if(adapter.adapterItemCount == 0){
//                            viewModel.getMovies()
//                        }
                        runOnUiThread{
                            binding.progress.visibility = View.GONE
                            binding.swipeRefresh.visibility = View.VISIBLE
                            binding.layoutData.visibility = View.VISIBLE
                            binding.textDataNotFound.visibility = View.GONE
                        }
                    }
                }, 500)
            }
        }
    }

    override fun initConfig() {
        val factory = DetailMovieViewModelFactory(this)
        viewModel = ViewModelProvider(this, factory)[DetailMovieViewModel::class.java]
        adapterVideo = ItemAdapter()
        fastAdapterVideo = FastAdapter.with(adapterVideo)
        if(intent.getStringExtra("title")!=null){
            title = intent.getStringExtra("title")!!
        }
        if(intent.getLongExtra("id",-1) != -1.toLong()){
            id = intent.getLongExtra("id",-1)
            initUI()
        }else{
            finish()
        }
    }

    override fun initUI() {
        if(isFirst){
            isFirst = false
        }
        binding.toolbar.title.text = "Detail Movie"
        viewModel.movie.observe(this){ resource ->
            when (resource) {
                is Resource.Loading -> showLoading(resource.state)
                is Resource.Success -> loadData(resource.data)
                is Resource.Failure -> showDataError(resource.errorMessage)
            }
        }
        viewModel.videos.observe(this){
            if(trailers.size == 0){
                trailers = it
                trailers.forEach { video ->
                    var data = AdapterVideoName(this)
                    data.data = video
                    adapterVideo.add(data)
                }
                showDialogListTrailer()
            }
        }

        viewModel.getDetailMovie(id)
        setListener()
    }

    override fun setListener() {
        setupSwipeRefresh()
        binding.toolbar.btnBack.setOnClickListener {
            finish()
        }
        binding.showTrailer.setOnClickListener {
            if(trailers.size > 0){
                showDialogListTrailer()
            }else{
                viewModel.getMovieVideos(id)
            }
        }
        binding.showReviews.setOnClickListener {
            if(detail!=null){
                var intent = Intent(this@DetailMovieActivity, ReviewActivity::class.java)
                intent.putExtra("title",detail?.title)
                intent.putExtra("id",detail?.id)
                startActivity(intent)
            }
        }
    }
    fun setupSwipeRefresh() {
        try {
            binding.swipeRefresh.setOnRefreshListener(object :
                SwipeRefreshLayout.OnRefreshListener {
                override fun onRefresh() {
                    viewModel.getDetailMovie(id)

                    object : CountDownTimer(2000, 1000) {
                        override fun onTick(millisUntilFinished: Long) {
                            try{
                                binding.swipeRefresh.setRefreshing(false)
                            }catch (e : Exception){
                            }catch (e : KotlinNullPointerException){
                            }
                        }

                        override fun onFinish() {
                            try{
                                binding.swipeRefresh.setRefreshing(false)
                            }catch (e : Exception){
                            }catch (e : KotlinNullPointerException){
                            }
                        }
                    }.start()
                }
            })
        } catch (e: Exception) {
        } catch (e: KotlinNullPointerException) {
        }
    }


    fun showLoading(loading : Boolean){
        binding.progress.visibility = View.VISIBLE
        binding.swipeRefresh.visibility = View.GONE
        binding.layoutData.visibility = View.GONE
        binding.textDataNotFound.visibility = View.GONE
    }

    fun loadData(data : Movie){
        detail = data
        binding.progress.visibility = View.GONE
        binding.swipeRefresh.visibility = View.VISIBLE
        binding.layoutData.visibility = View.VISIBLE
        binding.textDataNotFound.visibility = View.GONE
        var width = Util.getDisplay(this)!!.widthPixels-Util.convertDpToPx(38,this)
        var divW = 3.0
        var divh = 2.2
        var wResult : Double = width.toDouble()/divW
        var hResult : Double = width.toDouble()/divh

        var widthR = (Math.round(wResult)).toInt()
        var heightR = (Math.round(hResult)).toInt()
        var param = binding.photo.layoutParams as RelativeLayout.LayoutParams
        param.width = widthR
        param.height = heightR
        Util.loadImage(
            this,
            Constants.IMAGE_BASE_URL+data.imageUrl,
            binding.photo,
            "",
            4,
            widthR,
            heightR,
            1,
            false)
        binding.titleMovie.text = data.title
        binding.overview.text = data.desc

        var genre = ""
        data.genres!!.forEach {
            if(!genre.equals("")){
                genre += ", "
            }
            genre += it.name
        }
        binding.genre.text = "Genre : "+genre
        binding.duration.text = "Duratation : "+Util.getShowTime(data.runtime)
        binding.release.text = "Release : "+
                Util.convertDate(data.releaseDate,
                    Constants.DATE_OUT_FORMAT_DEF2,
                    Constants.DATE_OUT_FORMAT_DEF3)
    }

    fun showDataError(message : String? = null){
        binding.progress.visibility = View.GONE
        binding.swipeRefresh.visibility = View.GONE
        binding.layoutData.visibility = View.GONE
        binding.textDataNotFound.visibility = View.VISIBLE
        if(message!=null){
            binding.textDataNotFound.text = message
        }
    }

    fun showDialogListTrailer(){
        var dialogBuilder = AlertDialog.Builder(this)
        var binding = DialogListTrailerBinding.inflate(layoutInflater)
        val view = binding.root
        dialogBuilder.setView(view)
        val alertDialog: AlertDialog = dialogBuilder.create()
        binding.revListData.layoutManager = LinearLayoutManager(this)
        binding.revListData.adapter = fastAdapterVideo
        binding.revListData.animation = null
        binding.revListData.isNestedScrollingEnabled = false

        fastAdapterVideo.onClickListener = { view, adapter, item, position ->
            alertDialog.dismiss()
            showDialogTrailer(item.data?.key!!)
            false
        }
        binding.close.setOnClickListener {
            alertDialog.dismiss()
        }
        alertDialog.show()
        alertDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

    fun showDialogTrailer(trailerId : String){
        var dialogBuilder = AlertDialog.Builder(this)
        var binding = DialogTrailerMovieBinding.inflate(layoutInflater)
        val view = binding.root
        dialogBuilder.setView(view)
        val alertDialog: AlertDialog = dialogBuilder.create()
        binding.youtubePlayerView.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
            override fun onReady(@NonNull youTubePlayer: YouTubePlayer) {
                youTubePlayer.loadVideo(trailerId, 0f)
            }
        })

        alertDialog.show()
        alertDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }
}