package hlc.realtime.location.dagger.viewModule

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap
import hlc.realtime.location.dagger.ViewModelBuilder
import hlc.realtime.location.dagger.ViewModelKey
import hlc.realtime.location.ui.main.MainFragment
import hlc.realtime.location.ui.main.MainFragmentViewModel

@Module
abstract class MainFragmentModule {

    // 將 ViewModelFactory 注入指定 Activity 當中，實現初始化
    @ContributesAndroidInjector(modules = [ViewModelBuilder::class])
    internal abstract fun MainFragment(): MainFragment

    // 將
    @Binds
    @IntoMap
    @ViewModelKey(MainFragmentViewModel::class)
    internal abstract fun bindViewModel(viewModel: MainFragmentViewModel): ViewModel

}