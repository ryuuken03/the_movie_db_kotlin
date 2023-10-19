package mohammad.toriq.tmdb.views.adapters

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.RelativeLayout
import com.mikepenz.fastadapter.binding.AbstractBindingItem
import mohammad.toriq.tmdb.R
import mohammad.toriq.tmdb.configs.Constants
import mohammad.toriq.tmdb.databinding.ItemAdapterMovieBinding
import mohammad.toriq.tmdb.models.Movie
import mohammad.toriq.tmdb.util.InitializerUi
import mohammad.toriq.tmdb.util.Util

/***
 * Created By Mohammad Toriq on 16/02/2023
 */
open class AdapterMovie(context: Context) : AbstractBindingItem<ItemAdapterMovieBinding>(), InitializerUi{
    var context: Context = context
    var data: Movie? = null

    lateinit var binding: ItemAdapterMovieBinding

    /** defines the type defining this item. must be unique. preferably an id */
    override val type: Int
        get() = R.id.item_adapter_movie

    override fun bindView(binding: ItemAdapterMovieBinding, payloads: List<Any>) {
        this.binding = binding
        initConfig()
    }

    override fun createBinding(inflater: LayoutInflater, parent: ViewGroup?): ItemAdapterMovieBinding {
        return ItemAdapterMovieBinding.inflate(inflater, parent, false)
    }

    override fun initConfig() {
        initUI()
    }

    override fun initUI() {
        binding.title.text = data?.title
        binding.release.text = Util.convertDate(data?.releaseDate, Constants.DATE_OUT_FORMAT_DEF2, Constants.DATE_OUT_FORMAT_DEF3)
        var width = Util.getDisplay(context as Activity)!!.widthPixels-Util.convertDpToPx(38,context)
        var divW = 2.0
        var divh = 1.5
        var wResult : Double = width.toDouble()/divW
        var hResult : Double = width.toDouble()/divh

        var widthR = (Math.round(wResult)).toInt()
        var heightR = (Math.round(hResult)).toInt()
        var param = binding.photo.layoutParams as RelativeLayout.LayoutParams
        param.width = widthR
        param.height = heightR
        Util.loadImage(
            context,
            Constants.IMAGE_BASE_URL+data?.imageUrl,
            binding.photo,
            "",
            4,
            widthR,
            heightR,
            1,
            true)
        setListener()
    }

    override fun setListener() {
    }

}