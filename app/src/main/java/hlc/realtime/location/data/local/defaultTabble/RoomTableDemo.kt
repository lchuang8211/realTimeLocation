package hlc.realtime.location.data.local.defaultTabble

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "RoomTableDemo")
data class RoomTableDemo(
    @PrimaryKey
    var uid: String,

    //Colum nInfo = schema name 欄位名稱
    @ColumnInfo(name = "name") var name: String,
    @ColumnInfo(name = "number")var number: Int

)