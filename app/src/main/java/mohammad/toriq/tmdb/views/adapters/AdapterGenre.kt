package mohammad.toriq.tmdb.views.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import com.mikepenz.fastadapter.binding.AbstractBindingItem
import mohammad.toriq.tmdb.R
import mohammad.toriq.tmdb.databinding.ItemAdapterGenreBinding
import mohammad.toriq.tmdb.util.InitializerUi

/***
 * Created By Mohammad Toriq on 16/02/2023
 */
open class AdapterGenre(context: Context) : AbstractBindingItem<ItemAdapterGenreBinding>(), InitializerUi{
    var context: Context = context
    var id: Long? = null
    var name: String? = null
    var value: String? = null

    lateinit var binding: ItemAdapterGenreBinding

    /** defines the type defining this item. must be unique. preferably an id */
    override val type: Int
        get() = R.id.item_adapter_genre

    override fun bindView(binding: ItemAdapterGenreBinding, payloads: List<Any>) {
        this.binding = binding
        initConfig()
    }

    override fun createBinding(inflater: LayoutInflater, parent: ViewGroup?): ItemAdapterGenreBinding {
        return ItemAdapterGenreBinding.inflate(inflater, parent, false)
    }

    override fun initConfig() {
        initUI()
    }

    override fun initUI() {
        binding.genre.text = name
        setListener()
    }

    override fun setListener() {
    }

}