package hlc.realtime.location.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import hlc.realtime.location.data.local.defaultTabble.RoomTableDemo
import hlc.realtime.location.data.local.defaultTabble.DefaultTableDao


private const val TAG = "RoomDataBase"

// 指定 DataBase 中定義資料表的 class ， ＝ DataBase 下所含的 Table
@Database(
    entities = [
        RoomTableDemo::class
    ],version = 1 ,exportSchema = false
)
abstract class RoomDataBase : RoomDatabase(){

    // 指定 Table 對應的操作 class Dao
    abstract fun userInfoDao(): DefaultTableDao

    // singleton 避免重複多次實體化資料庫  // how to instance ?
//    companion object{
//
//        //Volatile 用來避免編譯器優化
//        @Volatile
//        private var INSTANCE: RoomDataBase ?= null
//
//        fun getInstance(application: Context) : RoomDataBase{
//
//            if (INSTANCE!=null)
//                return INSTANCE!!
//
//            INSTANCE = Room.databaseBuilder(application, RoomDataBase::class.java,"RoomDemo.db")
//                .build()
//            Log.i(TAG, "getInstance: " + INSTANCE)
//            return INSTANCE!!
//        }
////        fun getInstance(application: Application) = INSTANCE ?:
////                Room.databaseBuilder(application,mDataBase::class.java, "RoomDemo")
////                    .build()
////                    .also {
////                        INSTANCE = it
////                    }
//
//    }
}
