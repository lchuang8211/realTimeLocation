package hlc.realtime.location.data.api

import java.util.*

data class LocationData(
    val date : Date ?=null ,
    val locationDetail : MutableList<LocationDetail> ?=null
){
    data class Date(
        val year: String = "",
        val month: String = "",
        val day: String = ""
    )

    data class LocationDetail(
        var seq : Int = 0,
        var startTime: String = "",
        var endTime: String = "",
        val longitude: Double,  // x 經度
        val latitude: Double,  // y 緯度
        var customAddress: String = "",
        val firstAddress: String,
        val secondAddress: String,
        val thirdAddress: String
    )
}