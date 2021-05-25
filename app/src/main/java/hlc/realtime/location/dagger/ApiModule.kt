package hlc.realtime.location.dagger

import hlc.realtime.location.data.api.WebApi
import dagger.Module
import dagger.Provides
import hlc.realtime.location.support.ApiInfo
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

// 存放 Retrofit OKhttp 和所有 Api 的 Provide
@Module
object ApiModule {

    @JvmStatic
    @Singleton
    @Provides
    fun provideRetrofit(
        client: OkHttpClient
    ): Retrofit {
        return Retrofit.Builder()
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(ApiInfo.API_URL)
            .client(client)
            .build()
    }
    @JvmStatic
    @Singleton
    @Provides
    fun getOKHttpClient(): OkHttpClient {
        var logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY
        return OkHttpClient.Builder()
            .addInterceptor(logging)
            .addInterceptor(TempInterceptor())
            .connectTimeout(45, TimeUnit.SECONDS)
            .writeTimeout(45, TimeUnit.SECONDS)
            .readTimeout(45, TimeUnit.SECONDS)
            .build()
    }

    // OKhttp 攔截器
    class TempInterceptor: Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            val temp = chain.request()
            return chain.proceed(temp)
        }
    }

    @JvmStatic
    @Singleton
    @Provides
    fun prevideWebApi(retrofit: Retrofit): WebApi {
        return retrofit.create(WebApi::class.java)
    }

}