package alfatih.me.moviecatalogue.libs.base
import alfatih.me.moviecatalogue.data.source.remote.MovieRepository
import alfatih.me.moviecatalogue.data.source.remote.RetrofitFactory
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext

abstract class BaseViewModel : ViewModel(){

    private val _loadingStatus = MutableLiveData<Boolean>()

    val showLoading: LiveData<Boolean> = _loadingStatus
    val parentJob = Job()

    val coroutineContext: CoroutineContext
        get() = parentJob + Dispatchers.Default

    val scope = CoroutineScope(coroutineContext)

    val repository : MovieRepository = MovieRepository(RetrofitFactory.TMDBApi)

    protected fun showLoading() {
        _loadingStatus.value = true
    }

    protected fun hideLoading() {
        if (_loadingStatus.value == true) {
            _loadingStatus.value = false
        }
    }
}