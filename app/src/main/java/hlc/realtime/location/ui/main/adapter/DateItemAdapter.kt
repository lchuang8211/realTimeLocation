package hlc.realtime.location.ui.main.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import hlc.realtime.location.data.api.LocationData
import hlc.realtime.location.databinding.LayoutDateItemBinding
import hlc.realtime.location.databinding.LayoutLocationListItemBinding
import hlc.realtime.location.ui.main.MainFragmentViewModel
import timber.log.Timber

class DateItemAdapter(val context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var itemList= emptyList<LocationData>()
    private lateinit var viewModel: MainFragmentViewModel

    fun sumbit(item: List<LocationData>){
        itemList = item
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ViewHolder.from( parent )
    }

    override fun getItemCount(): Int {
        return itemList.size
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        //多型應用 對應相應Class取得對應資料
        if (holder is ViewHolder){
            holder.bind( itemList[ position ] , context)
        }
    }

    class ViewHolder(val binding: LayoutDateItemBinding): RecyclerView.ViewHolder(binding.root){
        companion object{
            //透過Fragment(父元件)取得Binding的View元件
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = LayoutDateItemBinding.inflate(layoutInflater, parent,false)
                return ViewHolder(
                    binding
                )
            }
        }

        //顯示資料的規則/邏輯
        fun bind(item: LocationData, context: Context){
            val date = item.date
            binding.tvDate.text = "${date?.year}/${date?.month}/${date?.day}"

            val adapter = LocationItemAdapter(context)
            binding.rvLocationDetail.adapter = adapter
            item.locationDetail?.let { adapter.sumbit(item.locationDetail) }
            binding.rvLocationDetail.layoutManager = LinearLayoutManager(context)
        }
    }
}

