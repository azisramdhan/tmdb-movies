package alfatih.me.moviecatalogue.data.source.remote

import alfatih.me.moviecatalogue.data.model.movie.detail.DetailResponse
import alfatih.me.moviecatalogue.data.model.movie.MovieResponse
import alfatih.me.moviecatalogue.data.model.tv.TVResponse
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

//A retrofit RetrofitFactory Interface for the Api
interface MovieDataSource{
    @GET("movie/popular")
    fun getPopularMovie(@Query("language") language: String): Deferred<Response<MovieResponse>>

    @GET("search/movie")
    fun getSearchMovie(@Query("language") language: String, @Query("query") query: String): Deferred<Response<MovieResponse>>

    @GET("search/tv")
    fun getSearchTVShow(@Query("language") language: String, @Query("query") query: String): Deferred<Response<TVResponse>>

    @GET("movie/{id}")
    fun getDetailMovie(@Path("id") id: Int): Deferred<Response<DetailResponse>>

    @GET("tv/popular")
    fun getPopularTVShow(@Query("language") language: String): Deferred<Response<TVResponse>>

    @GET("tv/{tv_id}")
    fun getDetailTVShow(@Path("tv_id") id: Int): Deferred<Response<alfatih.me.moviecatalogue.data.model.tv.detail.DetailResponse>>
}