package mohammad.toriq.tmdb.views.adapters

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import com.mikepenz.fastadapter.binding.AbstractBindingItem
import mohammad.toriq.tmdb.R
import mohammad.toriq.tmdb.configs.Constants
import mohammad.toriq.tmdb.databinding.ItemAdapterMovieBinding
import mohammad.toriq.tmdb.databinding.ItemAdapterReviewBinding
import mohammad.toriq.tmdb.models.Movie
import mohammad.toriq.tmdb.models.Review
import mohammad.toriq.tmdb.util.InitializerUi
import mohammad.toriq.tmdb.util.Util

/***
 * Created By Mohammad Toriq on 16/02/2023
 */
open class AdapterReview(context: Context) : AbstractBindingItem<ItemAdapterReviewBinding>(), InitializerUi{
    var context: Context = context
    var data: Review? = null

    lateinit var binding: ItemAdapterReviewBinding

    /** defines the type defining this item. must be unique. preferably an id */
    override val type: Int
        get() = R.id.item_adapter_review

    override fun bindView(binding: ItemAdapterReviewBinding, payloads: List<Any>) {
        this.binding = binding
        initConfig()
    }

    override fun createBinding(inflater: LayoutInflater, parent: ViewGroup?): ItemAdapterReviewBinding {
        return ItemAdapterReviewBinding.inflate(inflater, parent, false)
    }

    override fun initConfig() {
        initUI()
    }

    override fun initUI() {
        binding.author.text = data?.author
        binding.release.text = "On "+Util.convertDate(data?.updatedAt, Constants.DATE_OUT_FORMAT_DEF2, Constants.DATE_OUT_FORMAT_DEF3)
        if(data?.authorDetails?.avatarPath != null){
            binding.initial.visibility = View.GONE
            Util.loadCircleImage(
                context,
                Constants.IMAGE_BASE_URL+data?.authorDetails?.avatarPath,
                binding.photo,
                "")
        }else{
            binding.initial.visibility = View.VISIBLE
            binding.initial.text = data?.author!!.get(0).toString()
        }

        binding.content.text = data?.content
        setListener()
    }

    override fun setListener() {
    }

}