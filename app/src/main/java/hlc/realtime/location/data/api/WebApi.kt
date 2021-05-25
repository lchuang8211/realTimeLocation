package hlc.realtime.location.data.api

import io.reactivex.Observable
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST

// 定義連線API 的方式及規格


interface WebApi {

    // GET, POST 使用的傳輸方法
    @GET("/demoVM.php")
    fun getWebData(): Observable<WebReponse>


    @Headers("Content-Type: application/json")
    @POST("/demoVM.php")
    fun postData(@Body request: WebRequest): Observable<ArrayList<WebReponse>>
    // @Body : 定義要傳出去的內容，及使用格式


}