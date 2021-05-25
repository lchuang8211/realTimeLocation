package hlc.realtime.location.dagger.viewModule

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap
import hlc.realtime.location.dagger.ViewModelBuilder
import hlc.realtime.location.dagger.ViewModelKey
import hlc.realtime.location.ui.main.MainActivity
import hlc.realtime.location.ui.main.MainViewModel

@Module
abstract class MainActivityModule {

    // 將 ViewModelFactory 注入指定 Activity 當中，實現初始化
    @ContributesAndroidInjector(modules = [ViewModelBuilder::class])
    internal abstract fun MainActivity(): MainActivity

    // 將
    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel::class)
    internal abstract fun bindViewModel(viewModel: MainViewModel): ViewModel

}