package hlc.realtime.location.dagger

import hlc.realtime.location.data.repository.FruitInteractor
import hlc.realtime.location.data.repository.FruitRepository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

// 存放所有 Repository
@Module
object RepositoryModule {

    @JvmStatic
    @Singleton
    @Provides
    fun provideFruitRepository(): FruitInteractor {
        return FruitRepository()
    }

    /**
     *  other Repository function
     * */

}