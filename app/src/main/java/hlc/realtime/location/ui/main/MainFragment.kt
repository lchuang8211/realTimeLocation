package hlc.realtime.location.ui.main

import android.content.Intent
import android.location.LocationListener
import android.location.LocationManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import android.widget.PopupWindow
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.Constraints
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.tbruyelle.rxpermissions2.RxPermissions
import hlc.realtime.location.base.BaseDaggerFragment
import hlc.realtime.location.data.api.LocationData
import hlc.realtime.location.databinding.FragmentMainBinding
import hlc.realtime.location.databinding.PopupwindowMapBinding
import hlc.realtime.location.support.GPSHelper
import hlc.realtime.location.support.autobanner.StartSnapHelper
import hlc.realtime.location.ui.main.adapter.DateItemAdapter
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import timber.log.Timber

class MainFragment : BaseDaggerFragment(){

    override val viewModel by viewModels<MainFragmentViewModel> { viewModelFactory }

    val activityViewModel by activityViewModels<MainViewModel> { viewModelFactory }

    override lateinit var binding : FragmentMainBinding

    lateinit var mLocationManager : LocationManager
    lateinit var locationListener : LocationListener

    private lateinit var mapPopupWindow : PopupWindow
    private lateinit var mapPopupWindowBinding : PopupwindowMapBinding

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMainBinding.inflate(inflater, null, false).apply {
            this.viewModel = this@MainFragment.viewModel
        }

        /** 請求權限 */
        RxPermissions(requireActivity())
            .requestEachCombined(
                android.Manifest.permission.ACCESS_FINE_LOCATION,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            )
            .subscribe({
                Timber.tag("hlcDebug").d("permission get : $it")
            },{
                Timber.tag("hlcDebug").d("throw : $it")
            }).addTo(CompositeDisposable())

        GPSHelper.getLocation()
        init()
        initObserver()
        return binding.root
    }

    private fun initObserver() {
        viewModel.addressList.observe(this, androidx.lifecycle.Observer {
            val adapter = DateItemAdapter(requireActivity(), viewModel)
            it?.let {
                Timber.tag("hlcDebug").d("addressList observe : $it")
                adapter.sumbit(it)
                binding.rvLocationList.adapter = adapter
                binding.rvLocationList.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
                binding.rvLocationList.scrollToPosition(it.size-1)
                if (binding.rvLocationList.onFlingListener == null)
                    StartSnapHelper().attachToRecyclerView(binding.rvLocationList)
            }
        })

        /** record: true, release: false
         * 切換按鈕狀態
         * */
        viewModel.recordStatus.observe(this, androidx.lifecycle.Observer {
            if (it){
                binding.btnStart.isEnabled = false
                binding.btnEnd.isEnabled = true
            } else {
                binding.btnStart.isEnabled = true
                binding.btnEnd.isEnabled = false
            }
        })

        /** select DB and group by 判斷總共有幾天的紀錄 */
        viewModel.totalDay.observe(this, Observer {
            // TODO 優化流程
            viewModel._oldList.value = MutableList<LocationData>(it.size){ LocationData(null,null) }
            for (i in 0..it.size-1){
                viewModel.initRrcycler(it[i].year, it[i].month, it[i].day, i)
            }
        })

        /** recycler item clicked show popupWindow */
        viewModel.showMapOneLocation.observe(this, Observer {
            it?.let {
                if (viewModel.showMapOne) {
                    /** web view 開 google map */
//                    mapPopupWindow.showAtLocation(
//                        binding.clBackground,
//                        Gravity.CENTER,
//                        0,
//                        0
//                    )

                    /** get location(X, Y) and show on google map*/
                    val longitude: Double = it.longitude  // x 經度
                    val latitude: Double = it.latitude  // y 緯度
                    /** Google Map URL format
                     * http://maps.google.com/maps?&z={INSERT_MAP_ZOOM}&mrt={INSERT_TYPE_OF_SEARCH}&t={INSERT_MAP_TYPE}&q={INSERT_MAP_LAT_COORDINATES}+{INSERT_MAP_LONG_COORDINATES}
                     * */
                    mapPopupWindowBinding.wvMap.loadUrl("http://maps.google.com/maps?&z=19&mrt=loc&t=p&q=${latitude},${longitude}")

                    /** intent 開 google map App */
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse("http://maps.google.com/maps?&z=19&mrt=loc&t=p&q=${latitude},${longitude}"))
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.setPackage("com.google.android.apps.maps")
                    // com.google.android.apps.maps
                    // com.android.chrome
                    startActivity(intent)
                }
            }
        })

        viewModel.showMapAllLocation.observe(this, Observer {
            it?.let {list->
                var size = 0
                var string = ""
                var latitude :Double = .0
                var longitude :Double = .0
                if(list.isNotEmpty()){
                    /** google map 免費方式有限制，不能使用多 marker 的方式
                     * 多 marker 僅能在 google map API 中使用，為收費API
                     * 故使用多點路徑 URL 代替
                     * */
                    list.forEach {
                        if (it.latitude != latitude && it.longitude != longitude) {
                            string = "$string${it.latitude},${it.longitude}/"
                            latitude = it.latitude
                            longitude = it.longitude
                            size+=1
                        }
                    }
                    string = "$string"
                }
                if (viewModel.showMapAll){
                    /** web view 開 google map */
//                    mapPopupWindow.showAtLocation(
//                        binding.clBackground,
//                        Gravity.CENTER,
//                        0,
//                        0
//                    )

                    var uriString = ""
                    if (size > 1) {
                        mapPopupWindowBinding.wvMap.loadUrl("http://maps.google.com/maps/dir/$string")
                        uriString = "http://www.google.co.in/maps/dir/$string"
                    } else {
                        mapPopupWindowBinding.wvMap.loadUrl("http://maps.google.com/maps?&z=19&mrt=loc&t=p&q=${string.replace("/","")}")
                        string = string.replace("/","")
                        uriString = "http://maps.google.com/maps?&z=19&mrt=loc&t=p&q=$string"
                    }
                    /** intent 開 google map App */
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uriString))
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.setPackage("com.google.android.apps.maps")
                    // com.google.android.apps.maps
                    // com.android.chrome
                    startActivity(intent)
                }
            }
        })

        mapPopupWindow.setOnDismissListener {
            viewModel.showMapOne = false
            viewModel.showMapAll = false
            Timber.tag("hlcDebug").d("showMap : ${viewModel.showMapOne}")
        }
    }

    @RequiresApi(Build.VERSION_CODES.R)
    private fun init() {

        binding.btnStart.setOnClickListener {
            viewModel.getGPSLocationFlow(true)
        }

        binding.btnEnd.setOnClickListener {
            viewModel.getGPSLocationFlow(false)
        }

        mapPopupWindowBinding = PopupwindowMapBinding.inflate(LayoutInflater.from(context), null, false)
        mapPopupWindow = PopupWindow(
            mapPopupWindowBinding.root,
            Constraints.LayoutParams.WRAP_CONTENT,
            Constraints.LayoutParams.WRAP_CONTENT,
            true
        )
        mapPopupWindow.isOutsideTouchable = true

        /** set web view config */
        mapPopupWindowBinding.wvMap.apply {
            this.settings.javaScriptEnabled = true
            this.webViewClient = WebViewClient()
        }

        mapPopupWindowBinding.ivClose.setOnClickListener {
            mapPopupWindow.dismiss()
        }

    }

}

