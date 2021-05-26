package hlc.realtime.location.ui.main

import android.location.Address
import androidx.lifecycle.MutableLiveData
import hlc.realtime.location.base.BaseViewModel
import hlc.realtime.location.data.api.LocationData
import hlc.realtime.location.support.GPSHelper
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class MainFragmentViewModel @Inject constructor(): BaseViewModel(){

    var startTime = ""
    var endTime = ""

    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.TAIWAN)
    val calendarTime = Calendar.getInstance()
    var cDay: Int = 1
    var cYear: Int = 2021
    var cMonth: Int = 1
    var cWMonth: Int = 1
    var cDWeek: Int = 0
    var today = ""

    val addressList = mutableListOf<Address>()

    // record: true , release: false
    val recordStatus = MutableLiveData<Boolean>(false)

    // Start : true, End : false
    fun getCurrentTime(status: Boolean){
        val calendarTime = Calendar.getInstance()
        cDay = calendarTime.get(Calendar.DAY_OF_MONTH)
        cYear = calendarTime.get(Calendar.YEAR)
        cMonth = calendarTime.get(Calendar.MONTH) + 1
        val hour = calendarTime.get(Calendar.HOUR_OF_DAY).toString().padStart(2, '0') // 24小時制
        val hour2 = calendarTime.get(Calendar.HOUR) // 12小時制
        val min = calendarTime.get(Calendar.MINUTE).toString().padStart(2, '0')
        today = dateFormat.format(calendarTime.time)
        Timber.tag("hlcDebug").d("getCurrentTime : $cYear / $cMonth / $cDay")
        Timber.tag("hlcDebug").d("getCurrentTime : $hour / $hour2 / $min")

        
        if (status) {
            startTime = "$hour:$min"
            recordStatus.value = true
            getCurrentLocation(true)
        }
        else{
            endTime = "$hour:$min"
            recordStatus.value = false
            getCurrentLocation(false)
        }
    }

    private val _list = mutableListOf<LocationData>()
    val list = MutableLiveData<List<LocationData>>()

    // Start : true, End : false
    fun getCurrentLocation(status: Boolean){
        addressList.clear()
        addressList.addAll( GPSHelper.getCurrentAddress() )
        if (addressList.isNotEmpty() && addressList.size > 1){

            val detail = LocationData.LocationDetail(
                startTime = this.startTime,
                endTime = " ",
                locationX = GPSHelper.getLongitude(),
                locationY = GPSHelper.getLatitude(),
                address = addressList[1].getAddressLine(0)
            )

            // select database
            val find = _list.find { it.date.year == cYear.toString().padStart(2, '0') && it.date.month == cMonth.toString().padStart(2, '0') && it.date.day == cDay.toString().padStart(2, '0') }

            if (find == null){
                _list.add(LocationData(
                    date = LocationData.Date(
                            year = cYear.toString().padStart(2, '0'),
                            month = cMonth.toString().padStart(2, '0'),
                            day = cDay.toString().padStart(2, '0')),
                    locationDetail = mutableListOf(detail))
                )
            } else {
                _list.filter { it.date.year == cYear.toString().padStart(2, '0') && it.date.month == cMonth.toString().padStart(2, '0') && it.date.day == cDay.toString().padStart(2, '0') }
                    .map {
                        if (recordStatus.value!!)
                            it.locationDetail.add(detail)
                        else
                            it.locationDetail[it.locationDetail.lastIndex].endTime = this.endTime
                    }
            }

            list.value = _list

        }
    }
}