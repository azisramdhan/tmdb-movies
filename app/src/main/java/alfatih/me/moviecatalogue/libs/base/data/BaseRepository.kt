package alfatih.me.moviecatalogue.libs.base.data

import retrofit2.Response
import timber.log.Timber
import java.io.IOException
import alfatih.me.moviecatalogue.libs.data.Result

open class BaseRepository {

    suspend fun <T : Any> apiCall(call: suspend () -> Response<T>, errorMessage: String): T? {
        val result: Result<T> = apiResult(call, errorMessage)
        var data: T? = null
        when (result) {
            is Result.Success ->
                data = result.data
            is Result.Error -> {
                Timber.d("BaseRepository - $errorMessage & Exception - ${result.exception}")
            }
        }
        return data
    }

    private suspend fun <T : Any> apiResult(call: suspend () -> Response<T>, errorMessage: String): Result<T> {
        val response = call.invoke()
        if (response.isSuccessful) return Result.Success(response.body()!!)
        return Result.Error(IOException("Error Occurred during getting safe Api result, Custom ERROR - $errorMessage"))
    }
}