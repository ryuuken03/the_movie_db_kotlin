package mohammad.toriq.tmdb.views.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import com.mikepenz.fastadapter.binding.AbstractBindingItem
import mohammad.toriq.tmdb.R
import mohammad.toriq.tmdb.databinding.ItemAdapterVideoNameBinding
import mohammad.toriq.tmdb.models.Video
import mohammad.toriq.tmdb.util.InitializerUi

/***
 * Created By Mohammad Toriq on 16/02/2023
 */
open class AdapterVideoName(context: Context) : AbstractBindingItem<ItemAdapterVideoNameBinding>(), InitializerUi{
    var context: Context = context
    var data: Video? = null

    lateinit var binding: ItemAdapterVideoNameBinding

    /** defines the type defining this item. must be unique. preferably an id */
    override val type: Int
        get() = R.id.item_adapter_video_name

    override fun bindView(binding: ItemAdapterVideoNameBinding, payloads: List<Any>) {
        this.binding = binding
        initConfig()
    }

    override fun createBinding(inflater: LayoutInflater, parent: ViewGroup?): ItemAdapterVideoNameBinding {
        return ItemAdapterVideoNameBinding.inflate(inflater, parent, false)
    }

    override fun initConfig() {
        initUI()
    }

    override fun initUI() {
        binding.name.text = data?.name
        setListener()
    }

    override fun setListener() {
    }

}