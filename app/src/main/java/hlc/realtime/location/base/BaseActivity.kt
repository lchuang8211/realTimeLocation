package hlc.realtime.location.base

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import dagger.android.AndroidInjection
import hlc.realtime.location.support.AppUtils
import javax.inject.Inject
import kotlin.reflect.KClass

abstract class BaseActivity : AppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    protected abstract val viewModel : BaseViewModel

    protected abstract val binding : ViewDataBinding


    // onCtreate 的第二種方法 onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?)
    // 區別? 使用方法? 無法Dagger?
    override fun onCreate(savedInstanceState: Bundle?) {
        // 替整個 Application 注入 Dagger 相關的資訊，要在 Super 之前
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        // Utils 整個 App 的工具包
        AppUtils.setContext(this)
    }

    protected fun setupStartActivityEvent(event: LiveData<Void>, clss: KClass<*>, requestCode:Int? = null, bundle: Bundle?=null){
        event.observe(this, Observer {
            startActivityLogoutUsingFlag(clss, requestCode, bundle)
//            finish()
        })
    }

    protected fun startActivityLogoutUsingFlag(clss: KClass<*>, requestCode:Int? = null, bundle: Bundle? = null) {
        Intent(this, clss.java).apply {
//            this.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
//            this.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            if(bundle!=null) this.putExtras(bundle)
            if(requestCode != null) {
                startActivityForResult(this, requestCode)
            } else {
                startActivity(this)
            }
        }
    }
}