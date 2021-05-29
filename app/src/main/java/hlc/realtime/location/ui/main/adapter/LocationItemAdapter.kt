package hlc.realtime.location.ui.main.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import hlc.realtime.location.R
import hlc.realtime.location.data.api.LocationData
import hlc.realtime.location.databinding.LayoutDateItemBinding
import hlc.realtime.location.databinding.LayoutLocationListItemBinding
import hlc.realtime.location.ui.main.MainFragmentViewModel
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*

class LocationItemAdapter(val context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var itemList= emptyList<LocationData.LocationDetail>()
    private lateinit var viewModel: MainFragmentViewModel

    fun setViewModel(viewModel: MainFragmentViewModel) {
        this.viewModel = viewModel
    }

    fun sumbit(item: List<LocationData.LocationDetail>){
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
            holder.bind( itemList[ position ] , position, context)
        }
    }

    class ViewHolder(val binding: LayoutLocationListItemBinding): RecyclerView.ViewHolder(binding.root){
        companion object{
            //透過Fragment(父元件)取得Binding的View元件
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = LayoutLocationListItemBinding.inflate(layoutInflater, parent,false)
                return ViewHolder(
                    binding
                )
            }
        }

        //顯示資料的規則/邏輯
        fun bind(item: LocationData.LocationDetail, position: Int, context: Context){

            if (position%2 == 0){
                binding.clBackground.background = context.getDrawable(R.color.linen)
            } else {
                binding.clBackground.background = context.getDrawable(R.color.antique_white)
            }

            Timber.tag("hlcDebug").d("bind item 2: $item")
            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.TAIWAN)
            binding.tvSeq.text = item.seq.toString().padStart(2, ' ')+". "
            binding.tvStartTime.text = item.startTime
            binding.tvEndTime.text = item.endTime
            binding.tvLocation.text = item.firstAddress

        }
    }
}

