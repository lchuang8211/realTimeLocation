package hlc.realtime.location.data.local.defaultTabble

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "RoomTableDemo")
data class RoomTableDemo(

    //Colum nInfo = schema name 欄位名稱
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id") var id: Int = 0,

    @ColumnInfo(name = "year") var year: String,
    @ColumnInfo(name = "month") var month: String,
    @ColumnInfo(name = "day") var day: String,

    @ColumnInfo(name = "seq") var seq: Int,
    @ColumnInfo(name = "start_time")var startTime: String,
    @ColumnInfo(name = "end_time")var endTime: String,
    @ColumnInfo(name = "longitude") var longitude: Double, // 經度
    @ColumnInfo(name = "latitude") var latitude: Double, // 緯度
    @ColumnInfo(name = "custom_address") var customAddress: String,
    @ColumnInfo(name = "first_address") var firstAddress: String,
    @ColumnInfo(name = "second_address") var secondAddress: String,
    @ColumnInfo(name = "third_address") var thirdAddress: String

)