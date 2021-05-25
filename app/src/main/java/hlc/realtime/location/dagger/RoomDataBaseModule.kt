package hlc.realtime.location.dagger

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager

import androidx.room.Room
import hlc.realtime.location.data.local.defaultTabble.DefaultTableDao
import dagger.Module
import dagger.Provides
import hlc.realtime.location.data.local.RoomDataBase
import javax.inject.Singleton


@Module
object RoomDataBaseModule {

    // 自訂資料庫名稱
    private val DB_NAME = "DefaultDataBase.db"

    @JvmStatic
    @Singleton
    @Provides
    fun provideRoomDataBase(context: Context): RoomDataBase {

        // 實作 RoomDataBase -> Biuld()
        return Room.databaseBuilder(context.applicationContext, RoomDataBase::class.java,DB_NAME)
//            .allowMainThreadQueries()
            .build()
    }

    @JvmStatic
    @Singleton
    @Provides
    fun provideSharedPreferences(
        context: Context
    ): SharedPreferences {
        return PreferenceManager.getDefaultSharedPreferences(context.applicationContext)
    }

    // Dagger Dao 操作實體

    @JvmStatic
    @Singleton
    @Provides
    fun providesUserInfoDao(roomDataBase: RoomDataBase): DefaultTableDao {
        return roomDataBase.userInfoDao()
    }

}