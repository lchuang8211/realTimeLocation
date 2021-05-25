package hlc.realtime.location.data.local.defaultTabble

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.reactivex.Observable

// 操作用

@Dao
interface DefaultTableDao{

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertWebData(list : List<RoomTableDemo>) // ?? WebReponse ??

    @Query("SELECT * FROM RoomTableDemo")
    fun getAll(): List<RoomTableDemo>

    @Query("SELECT * FROM RoomTableDemo WHERE name = :inputName ")
    fun getName(inputName: String): Observable<List<RoomTableDemo>>

    @Query("SELECT * FROM RoomTableDemo WHERE number = :inputNumber ")
    fun getNumber(inputNumber: String): List<RoomTableDemo>

}