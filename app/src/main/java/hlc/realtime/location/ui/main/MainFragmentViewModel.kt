package hlc.realtime.location.ui.main

import android.location.Address
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import hlc.realtime.location.base.BaseViewModel
import hlc.realtime.location.data.api.LocationData
import hlc.realtime.location.data.local.defaultTabble.RoomTableDemo
import hlc.realtime.location.domain.GetLocationUseCase
import hlc.realtime.location.support.GPSHelper
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

class MainFragmentViewModel @Inject constructor(
    private val locationUseCase: GetLocationUseCase
): BaseViewModel(){

    private var startTime = ""
    private var endTime = ""

    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.TAIWAN)

    private var cDay : String = "01"
    private var cYear : String = "2021"
    private var cMonth : String = "01"
    private var cHour : String = "01"
    private var cMin : String = "01"
    private var today = ""

    val _oldList = MutableLiveData<MutableList<LocationData>>(arrayListOf())
    var addressList : MutableLiveData<MutableList<LocationData>> = _oldList

    private val _newList = MutableLiveData<ArrayList<LocationData>>()

    private val newAddressListByGPS = mutableListOf<Address>()

    // record: true , release: false
    val recordStatus = MutableLiveData<Boolean>(false)

    var totalDay : LiveData<List<LocationData.Date>> = locationUseCase.getTotalDay()
    var todaySeq = -1

    init {
        updateTime()
    }

    var showMapOne :Boolean = false
    val showMapOneLocation = MutableLiveData<LocationData.LocationDetail>(null)

    var showMapAll :Boolean = false
    val showMapAllLocation = MutableLiveData<List<LocationData.LocationDetail>>(null)

    /** recycler item clicked show popupWindow */
    fun showOneLocationOnMap(item: LocationData.LocationDetail){
        showMapOne = true
        showMapOneLocation.value = item
    }

    fun showAllLocationOnMap(item: List<LocationData.LocationDetail>){
        showMapAll = true
        showMapAllLocation.value = item
    }

    fun updateTime(){
        val calendar = Calendar.getInstance()
        today = dateFormat.format(calendar.time)
        cYear = (calendar.get(Calendar.YEAR)).toString().padStart(2, '0')
        cMonth = (calendar.get(Calendar.MONTH) + 1).toString().padStart(2, '0')
        cDay = (calendar.get(Calendar.DAY_OF_MONTH)).toString().padStart(2, '0')
        cHour = calendar.get(Calendar.HOUR_OF_DAY).toString().padStart(2, '0') // 24小時制
        val hour2 = calendar.get(Calendar.HOUR) // 12小時制
        cMin = calendar.get(Calendar.MINUTE).toString().padStart(2, '0')

        Timber.tag("hlcDebug").d("getCurrentTime : $cYear / $cMonth / $cDay")
        Timber.tag("hlcDebug").d("getCurrentTime : $cHour / $hour2 / $cMin")
    }

    fun initRrcycler(year: String = cYear, month: String = cMonth, day: String = cDay, position: Int = 0){

        val locationDetail = mutableListOf<LocationData.LocationDetail>()

        locationUseCase(year, month, day)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                it.forEach { item->
                    locationDetail.add(LocationData.LocationDetail(
                        seq = item.seq,
                        startTime = item.startTime,
                        endTime = item.endTime,
                        longitude = item.longitude,
                        latitude = item.latitude,
                        customAddress = item.customAddress,
                        firstAddress = item.firstAddress,
                        secondAddress = item.secondAddress,
                        thirdAddress = item.thirdAddress
                    ))
                }
                if (year == cYear && month == cMonth && day == cDay) todaySeq = it.size
                val data = LocationData(
                    date = LocationData.Date(year, month, day),
                    locationDetail = locationDetail
                )
                _oldList.value?.set(position, data)
                addressList.postValue(_oldList.value)
            },{
                Timber.tag("hlcDebug").d("locationUseCase throw : $it")
            }).addTo(compositeDisposable)

    }

    // Start : true, End : false
    fun getGPSLocationFlow(status: Boolean){
        getCurrentTime(status)
    }

    fun getCurrentTime(status: Boolean){
        updateTime()
        if (status) {
            startTime = "$cHour:$cMin"
            recordStatus.value = true
            getCurrentLocation(true)
        }
        else{
            endTime = "$cHour:$cMin"
            recordStatus.value = false
            getCurrentLocation(false)
            setLocationAfterEnd()
        }
    }

    // Start : true, End : false
    fun getCurrentLocation(status: Boolean) {

        newAddressListByGPS.clear()
        newAddressListByGPS.addAll( GPSHelper.getCurrentAddress() )

        if (newAddressListByGPS.isNotEmpty() && newAddressListByGPS.size > 1) {
            todaySeq+=1

            val detail = LocationData.LocationDetail(
                seq = todaySeq,
                startTime = this.startTime,
                endTime = " ",
                longitude = GPSHelper.getLongitude(),
                latitude = GPSHelper.getLatitude(),
                customAddress = "",
                firstAddress = "${newAddressListByGPS[0].countryName}${newAddressListByGPS[0].adminArea}${newAddressListByGPS[0].locality}${newAddressListByGPS[0].thoroughfare}${newAddressListByGPS[0].subThoroughfare}",
                secondAddress = "${newAddressListByGPS[1].countryName}${newAddressListByGPS[1].adminArea}${newAddressListByGPS[1].locality}${newAddressListByGPS[1].thoroughfare}${newAddressListByGPS[1].subThoroughfare}",
                thirdAddress = "${newAddressListByGPS[2].countryName}${newAddressListByGPS[2].adminArea}${newAddressListByGPS[2].locality}${newAddressListByGPS[2].thoroughfare}${newAddressListByGPS[2].subThoroughfare}"
            )
            // TODO 優化流程

            // select database
            _newList.value?.add(
                LocationData(
                    date = LocationData.Date(year = cYear, month = cMonth, day = cDay),
                    locationDetail = mutableListOf(detail))
            )

            // 0 = 今天第一筆
            if (todaySeq == 0){
                _newList.value?.add(
                    LocationData(
                    date = LocationData.Date(year = cYear, month = cMonth, day = cDay),
                    locationDetail = mutableListOf(detail))
                )
            }

//            val find = _oldList.find { it.date.year == cYear.toString().padStart(2, '0') && it.date.month == cMonth.toString().padStart(2, '0') && it.date.day == cDay.toString().padStart(2, '0') }
//
//            if (find == null){
//                _oldList.add(LocationData(
//                    date = LocationData.Date(
//                            year = cYear.toString().padStart(2, '0'),
//                            month = cMonth.toString().padStart(2, '0'),
//                            day = cDay.toString().padStart(2, '0')),
//                    locationDetail = mutableListOf(detail))
//                )
//            } else {
//                _oldList.filter { it.date.year == cYear.toString().padStart(2, '0') && it.date.month == cMonth.toString().padStart(2, '0') && it.date.day == cDay.toString().padStart(2, '0') }
//                    .map {
//                        if (recordStatus.value!!)
//                            it.locationDetail.add(detail)
//                        else
//                            it.locationDetail[it.locationDetail.lastIndex].endTime = this.endTime
//                    }
//            }
//
//            addressList.value = _oldList

        }
    }

    fun setLocationAfterEnd(){

        val currentLocation = RoomTableDemo(
            year = cYear,
            month = cMonth,
            day = cDay,
            seq = todaySeq,
            startTime = startTime,
            endTime = endTime,
            longitude = GPSHelper.getLongitude(),
            latitude = GPSHelper.getLatitude(),
            customAddress = "",
            firstAddress = newAddressListByGPS[0].getAddressLine(0),
            secondAddress = newAddressListByGPS[1].getAddressLine(0),
            thirdAddress = newAddressListByGPS[2].getAddressLine(0)
        )

        /** 奇怪的流程，insert data 進DB會自動回傳扁動的table (locationUseCase(year, month, day)) */
        Single.just(1)
            .subscribeOn(Schedulers.newThread())
            .observeOn(Schedulers.io())
            .subscribe({
                // TODO 優化流程
//              _list.clear()
                locationUseCase.insertLocation(currentLocation)
                Timber.tag("hlcDebug").d("insert ok: ")
            }, {
                Timber.tag("hlcDebug").d("insert throw: $it")
            }).apply { compositeDisposable.add(this) }
        }

}