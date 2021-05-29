package hlc.realtime.location.domain

import android.util.Log
import hlc.realtime.location.data.repository.LocationInteractor
import javax.inject.Inject

private const val TAG = "AppleUseCase"

//實作 UseCase 的商業邏輯
class AppleUseCase @Inject constructor(
    val fruit: LocationInteractor
){
    fun doSometing(): String{
        Log.i(TAG, "doSometing: count: " + fruit.getCount() )
        return fruit.getCount().toString()+" apples"
    }
}