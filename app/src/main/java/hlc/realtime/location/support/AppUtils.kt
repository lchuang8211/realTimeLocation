package hlc.realtime.location.support

import android.content.Context
import android.content.res.Resources

object AppUtils{

    lateinit var AppContext: Context

    //由最外層的 BaseActivity 去取得 Context，方便繼承後的使用
    @JvmStatic
    fun setContext(context: Context){
        AppContext = context
    }


    // @return application 的 context
    @JvmStatic
    fun getContext(): Context {
        return AppContext
    }


    // 取得資源(resources 資料夾)對象
    @JvmStatic
    fun getResources(): Resources {
        return getContext().resources
    }
}