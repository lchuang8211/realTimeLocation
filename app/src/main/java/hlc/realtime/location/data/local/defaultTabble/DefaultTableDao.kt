package hlc.realtime.location.data.local.defaultTabble

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import hlc.realtime.location.data.api.LocationData
import io.reactivex.Observable
import io.reactivex.Single

// 操作用

@Dao
interface DefaultTableDao{

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertLocation(data : RoomTableDemo)

    @Query("SELECT * FROM RoomTableDemo ORDER BY year, month, day, seq ASC")
    fun getLocationList(): LiveData<List<RoomTableDemo>>

    // step 1
    @Query("SELECT year, month, day FROM RoomTableDemo GROUP BY year, month, day")
    fun getTotalDay() : LiveData<List<LocationData.Date>>

    // step 2
    @Query("SELECT * FROM RoomTableDemo WHERE year is :year and month is :month and day is :day ORDER BY day ASC")
    fun getLocationListByDay(year: String, month: String, day: String): Single<List<RoomTableDemo>>

    @Query("SELECT * FROM RoomTableDemo WHERE year is :year and month is :month and day is :day GROUP BY year, month, day")
    fun getDayLocationSeq(year: String, month: String, day: String): LiveData<List<RoomTableDemo>>

}