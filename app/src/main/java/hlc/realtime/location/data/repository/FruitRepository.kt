package hlc.realtime.location.data.repository

import android.util.Log
import javax.inject.Inject

private const val TAG = "FruitInstance"

// 實作 Repository，UseCase 透過 Repositroy 連接 API 或 進一步的操作，來取得資料
class FruitRepository @Inject constructor() : FruitInteractor {

    @Inject
    override fun onDish() {
        Log.i(TAG, "onDish: " )
    }


    override fun getCount() :Int {
        // 回傳幾個 apple
        return 7
    }

}