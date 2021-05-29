package hlc.realtime.location.dagger

import hlc.realtime.location.data.repository.LocationInteractor
import hlc.realtime.location.data.repository.FruitRepository
import dagger.Module
import dagger.Provides
import hlc.realtime.location.data.local.defaultTabble.DefaultTableDao
import javax.inject.Singleton

// 存放所有 Repository
@Module
object RepositoryModule {

    @JvmStatic
    @Singleton
    @Provides
    fun provideFruitRepository(dao: DefaultTableDao): LocationInteractor {
        return FruitRepository(dao)
    }

    /**
     *  other Repository function
     * */

}