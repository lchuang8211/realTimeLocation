package hlc.realtime.location.ui.main

import android.content.Context.LOCATION_SERVICE
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.tbruyelle.rxpermissions2.RxPermissions
import hlc.realtime.location.base.BaseDaggerFragment
import hlc.realtime.location.databinding.FragmentMainBinding
import hlc.realtime.location.support.GPSHelper
import hlc.realtime.location.ui.main.adapter.DateItemAdapter
import hlc.realtime.location.ui.main.adapter.LocationItemAdapter
import timber.log.Timber

class MainFragment : BaseDaggerFragment(){

    override val viewModel by viewModels<MainFragmentViewModel> { viewModelFactory }

    val activityViewModel by activityViewModels<MainViewModel> { viewModelFactory }

    override lateinit var binding : FragmentMainBinding

    lateinit var mLocationManager : LocationManager
    lateinit var locationListener : LocationListener



    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMainBinding.inflate(inflater, null, false).apply {
            this.viewModel = this@MainFragment.viewModel
        }

        RxPermissions(requireActivity())
            .requestEachCombined(
                android.Manifest.permission.ACCESS_FINE_LOCATION,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            )
            .subscribe({
                Timber.tag("hlcDebug").d("permission get : $it")

            },{
                Timber.tag("hlcDebug").d("throw : $it")
            })

        getlocationListener()
        init()
        initObserver()
        return binding.root
    }

    private fun initObserver() {
        viewModel.list.observe(this, androidx.lifecycle.Observer {
            val adapter = DateItemAdapter(requireActivity())
            adapter.sumbit(it)
            binding.rvLocationList.adapter = adapter
            binding.rvLocationList.layoutManager = LinearLayoutManager(activity)
        })
        viewModel.recordStatus.observe(this, androidx.lifecycle.Observer {
            if (it){
                binding.btnStart.isEnabled = false
                binding.btnEnd.isEnabled = true
            } else {
                binding.btnStart.isEnabled = true
                binding.btnEnd.isEnabled = false
            }
        })
    }

    @RequiresApi(Build.VERSION_CODES.R)
    private fun init() {
        binding.btnStart.setOnClickListener {
            GPSHelper.getLocation()
            viewModel.getCurrentTime(true)
        }

        binding.btnEnd.setOnClickListener {
            viewModel.getCurrentTime(false)
        }
    }

    fun getlocationListener(){
        locationListener = LocationListener {
            var vertical = it.longitude // 經度
            var horizontal = it.latitude // 緯度
            Timber.tag("hlcDebug").d("locationListener : $vertical / $horizontal")
        }
    }

}

