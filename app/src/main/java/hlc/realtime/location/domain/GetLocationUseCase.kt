package hlc.realtime.location.domain

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import hlc.realtime.location.data.api.LocationData
import hlc.realtime.location.data.local.defaultTabble.RoomTableDemo
import hlc.realtime.location.data.repository.LocationInteractor
import io.reactivex.Single
import javax.inject.Inject

private const val TAG = "AppleUseCase"

//實作 UseCase 的商業邏輯
class GetLocationUseCase @Inject constructor(
    private val repo: LocationInteractor
){
    operator fun invoke(year: String, month: String, day: String) : Single<List<RoomTableDemo>> {
        return repo.getLocation(year, month, day)
    }

    fun getAllLocation() : LiveData<List<RoomTableDemo>>{
        return repo.getAllLocation()
    }

    fun insertLocation(data : RoomTableDemo) {
        return repo.insertLocation(data)
    }
//RoomTableDemo
    fun getTotalDay() : LiveData<List<LocationData.Date>>{
        return repo.getTotalDay()
    }
}