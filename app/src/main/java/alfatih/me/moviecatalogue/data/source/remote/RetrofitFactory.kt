package alfatih.me.moviecatalogue.data.source.remote
import alfatih.me.moviecatalogue.util.AppConstant
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import timber.log.Timber

object RetrofitFactory{
    private val authInterceptor = Interceptor {
        val url = it.request().url()
            .newBuilder()
            .addQueryParameter("api_key", AppConstant.API_KEY)
            .build()
        val request = it.request()
            .newBuilder()
            .url(url)
            .build()
        Timber.d(request.toString())
        it.proceed(request)
    }

    private val okHttpClient = OkHttpClient().newBuilder()
                                .addInterceptor(authInterceptor)
                                .build()

    private fun createApi() : Retrofit = Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(AppConstant.BASE_URL)
                .addConverterFactory(MoshiConverterFactory.create())
                .addCallAdapterFactory(CoroutineCallAdapterFactory())
                .build()

    val TMDBApi : MovieDataSource = createApi()
        .create(MovieDataSource::class.java)
}