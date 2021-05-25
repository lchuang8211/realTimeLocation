package hlc.realtime.location.data.api

import java.time.Year


data class LocationData(
    val date : Date,
    val locationDetail : MutableList<LocationDetail>
){

    data class LocationDetail(
        var startTime: String = "",
        var endTime: String = "",
        val locationX: Double,
        val locationY: Double,
        val address: String = ""
    )

    data class Date(
        val year: String = "",
        val month: String = "",
        val day: String = ""
    )

}