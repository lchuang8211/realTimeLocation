package hlc.realtime.location.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import hlc.realtime.location.data.api.LocationData
import hlc.realtime.location.data.local.defaultTabble.DefaultTableDao
import hlc.realtime.location.data.local.defaultTabble.RoomTableDemo
import io.reactivex.Observable
import io.reactivex.Single
import timber.log.Timber
import javax.inject.Inject

private const val TAG = "FruitInstance"

// 實作 Repository，UseCase 透過 Repositroy 連接 API 或 進一步的操作，來取得資料
class FruitRepository @Inject constructor(private val dao: DefaultTableDao) : LocationInteractor {

    @Inject
    override fun onDish() {
        Log.i(TAG, "onDish: " )
    }


    override fun getCount() :Int {
        // 回傳幾個 apple
        return 7
    }

    override fun insertLocation(data : RoomTableDemo) {
        return dao.insertLocation(data)
    }

    override fun getLocation(year: String, month: String, day: String) : Single<List<RoomTableDemo>> {
        return dao.getLocationListByDay(year, month, day)
    }

    override fun getTotalDay(): LiveData<List<LocationData.Date>> { //RoomTableDemo
        return dao.getTotalDay()
    }

    override fun getAllLocation(): LiveData<List<RoomTableDemo>> {
        return dao.getLocationList()
    }
}