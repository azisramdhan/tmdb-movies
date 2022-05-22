package alfatih.me.moviecatalogue.feature.detail.movie

import alfatih.me.moviecatalogue.data.model.movie.detail.DetailResponse
import alfatih.me.moviecatalogue.libs.base.BaseViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.launch

class DetailMovieViewModel : BaseViewModel(){

    private val _detailMovie = MutableLiveData<DetailResponse>()
    val detailMovie: LiveData<DetailResponse> = _detailMovie

    fun getDetailMovie(id: Int){
        scope.launch {
            val detail = repository.getDetailMovie(id)
            _detailMovie.postValue(detail)
        }
    }
}