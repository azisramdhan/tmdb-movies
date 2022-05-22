package alfatih.me.moviecatalogue.feature.detail.tv

import alfatih.me.moviecatalogue.data.model.tv.detail.DetailResponse
import alfatih.me.moviecatalogue.libs.base.BaseViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.launch

class DetailTVShowViewModel: BaseViewModel(){

    private val _detailTVShow = MutableLiveData<DetailResponse>()
    val detailTVShow: LiveData<DetailResponse> = _detailTVShow

    fun getDetailTVShow(tv_id: Int){
        scope.launch {
            val detail = repository.getDetailTVShow(tv_id)
            _detailTVShow.postValue(detail)
        }
    }
}