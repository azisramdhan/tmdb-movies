package alfatih.me.moviecatalogue.data.source.remote

import alfatih.me.moviecatalogue.data.model.movie.detail.DetailResponse
import alfatih.me.moviecatalogue.data.model.movie.MovieItem
import alfatih.me.moviecatalogue.data.model.tv.TVItem
import alfatih.me.moviecatalogue.libs.base.data.BaseRepository
import timber.log.Timber
import java.util.*

class MovieRepository(private val dataSource : MovieDataSource) : BaseRepository() {

    private var language: String = if(Locale.getDefault().toString() == "in_ID"){
        "id-ID"
    }else{
        "en-US"
    }

    suspend fun searchMovies(query: String): MutableList<MovieItem>?{
        val response = apiCall(
            call = {dataSource.getSearchMovie(language, query).await()},
            errorMessage = "Error Search Movies"
        )
        Timber.d(response.toString())
        return response?.results?.toMutableList()
    }

    suspend fun searchTVShow(query: String): MutableList<TVItem>?{
        val response = apiCall(
            call = {dataSource.getSearchTVShow(language, query).await()},
            errorMessage = "Error Search TVShow"
        )
        Timber.d(response.toString())
        return response?.results?.toMutableList()
    }

    suspend fun getPopularMovies() : MutableList<MovieItem>?{
        val response = apiCall(
            call = {dataSource.getPopularMovie(language).await()},
            errorMessage = "Error Fetching Popular Movies"
        )
        Timber.d(response.toString())
        return response?.results?.toMutableList()
    }

    suspend fun getDetailMovie(id: Int): DetailResponse?{
        val response = apiCall(
            call = {dataSource.getDetailMovie(id).await()},
            errorMessage = "Error Fetching Detail Movie"
        )
        Timber.d(response.toString())
        return response
    }

    suspend fun getPopularTVShow() : MutableList<TVItem>?{
        val response = apiCall(
            call = {dataSource.getPopularTVShow(language).await()},
            errorMessage = "Error Fetching Popular TV Show"
        )
        Timber.d(response.toString())
        return response?.results?.toMutableList()
    }

    suspend fun getDetailTVShow(tv_id: Int): alfatih.me.moviecatalogue.data.model.tv.detail.DetailResponse?{
        val response = apiCall(
            call = {dataSource.getDetailTVShow(tv_id).await()},
            errorMessage = "Error Fetching Detail TV Show"
        )
        Timber.d(response.toString())
        return response
    }

}