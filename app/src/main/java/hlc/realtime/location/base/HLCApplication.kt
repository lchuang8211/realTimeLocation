package hlc.realtime.location.base

import dagger.android.AndroidInjector
import dagger.android.support.DaggerApplication
import hlc.realtime.location.BuildConfig
import hlc.realtime.location.dagger.DaggerApplicationComponent
import hlc.realtime.location.support.GPSHelper
import timber.log.Timber

// 建立整個 Applicayion 的 class，有最長的生命週期，可用來存放一些特定資料或設定
// ex: 取得整個Application 的 Context，可以在View之外的地方呼叫需要Context的Function

// DaggerApplication() = extends Application() implements HasAndroidInjector
 class HLCApplication : DaggerApplication() {

    // in, out Kotlin 的限定泛型用法，讓資料限制僅做 回傳輸出(out) 或 參數輸入(in) 使用
    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return DaggerApplicationComponent.factory().create(applicationContext)
    }

    override fun onCreate() {
        super.onCreate()
        GPSHelper.setInit(this)
        //檢查環境為 Debug 版時初始化 Timber(Log) 訊息，在正式版則不會初始化
        if (BuildConfig.DEBUG)
            Timber.plant(Timber.DebugTree())
    }

}
