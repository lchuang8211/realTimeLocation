package hlc.realtime.location.data.repository

import androidx.lifecycle.LiveData
import hlc.realtime.location.data.api.LocationData
import hlc.realtime.location.data.local.defaultTabble.RoomTableDemo
import io.reactivex.Observable
import io.reactivex.Single
import java.util.*

// 定義 Repository
interface LocationInteractor {

    fun onDish()

    fun getCount(): Int

    fun insertLocation(data : RoomTableDemo)

    fun getAllLocation(): LiveData<List<RoomTableDemo>>

    fun getLocation(year: String, month: String, day: String): Single<List<RoomTableDemo>>

    fun getTotalDay(): LiveData<List<LocationData.Date>> //RoomTableDemo
}