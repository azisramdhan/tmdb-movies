package alfatih.me.moviecatalogue.feature.list.content

import alfatih.me.moviecatalogue.data.model.movie.MovieItem
import alfatih.me.moviecatalogue.data.model.tv.TVItem
import alfatih.me.moviecatalogue.data.source.local.movie.MovieContract.MovieEntry
import alfatih.me.moviecatalogue.data.source.local.movie.MovieContract.MovieEntry.Companion.CONTENT_URI
import alfatih.me.moviecatalogue.data.source.local.tv.TVContract.TVEntry
import alfatih.me.moviecatalogue.libs.base.BaseViewModel
import alfatih.me.moviecatalogue.util.MovieDBHelper
import android.content.Context
import android.database.Cursor
import androidx.core.database.getDoubleOrNull
import androidx.core.database.getIntOrNull
import androidx.core.database.getStringOrNull
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class ListViewModel: BaseViewModel() {

    // not accessible by view, modifiable
    private val _movieList = MutableLiveData<List<MovieItem>>()
    // accessible by view, not modifiable
    val movieList: LiveData<List<MovieItem>> = _movieList

    private val _tvShowList = MutableLiveData<List<TVItem>>()
    val tvShowList: LiveData<List<TVItem>> = _tvShowList

    fun searchMovie(query: String){
        scope.launch {
            val movies = repository.searchMovies(query)
            _movieList.postValue(movies)
        }
    }

    fun searchTVShow(query: String){
        scope.launch {
            val tvShow = repository.searchTVShow(query)
            _tvShowList.postValue(tvShow)
        }
    }

    fun fetchData() {
        scope.launch {
            val movies = repository.getPopularMovies()
            _movieList.postValue(movies)

            val tvShow = repository.getPopularTVShow()
            _tvShowList.postValue(tvShow)
        }
    }

    fun fetchLocalDataMovie(context: Context) {
        val movieList = mutableListOf<MovieItem>()
        val sortOrder = MovieEntry.COLUMN_NAME_TITLE + " ASC"

        val cursor = context.contentResolver?.query(CONTENT_URI, null, null, null, sortOrder) as Cursor

        if (cursor.count>0) {
            cursor.moveToFirst()
            do {
                val adult = cursor.getInt(cursor.getColumnIndex(MovieEntry.COLUMN_NAME_ADULT)) > 0
                val backdropPath = cursor.getStringOrNull(cursor.getColumnIndex(MovieEntry.COLUMN_NAME_BACKDROP_PATH))
                val genreIds = cursor.getStringOrNull(cursor.getColumnIndex(MovieEntry.COLUMN_NAME_GENRE_IDS))
                val id = cursor.getIntOrNull(cursor.getColumnIndex(MovieEntry.COLUMN_NAME_ID))
                val originalLanguage = cursor.getStringOrNull(cursor.getColumnIndex(
                    MovieEntry.COLUMN_NAME_ORIGINAL_LANGUAGE))
                val originalTitle = cursor.getStringOrNull(cursor.getColumnIndex(MovieEntry.COLUMN_NAME_ORIGINAL_ORIGINAL_TITLE))
                val overview = cursor.getStringOrNull(cursor.getColumnIndex(MovieEntry.COLUMN_NAME_OVERVIEW))
                val popularity = cursor.getDoubleOrNull(cursor.getColumnIndex(MovieEntry.COLUMN_NAME_POPULARITY))
                val posterPath = cursor.getStringOrNull(cursor.getColumnIndex(MovieEntry.COLUMN_NAME_POSTER_PATH))
                val releaseDate = cursor.getStringOrNull(cursor.getColumnIndex(MovieEntry.COLUMN_NAME_RELEASE_DATE))
                val title = cursor.getStringOrNull(cursor.getColumnIndex(MovieEntry.COLUMN_NAME_TITLE))
                val video = cursor.getInt(cursor.getColumnIndex(MovieEntry.COLUMN_NAME_VIDEO)) > 0
                val voteAverage = cursor.getDoubleOrNull(cursor.getColumnIndex(MovieEntry.COLUMN_NAME_VOTE_AVERAGE))
                val voteCount = cursor.getIntOrNull(cursor.getColumnIndex(MovieEntry.COLUMN_NAME_VOTE_COUNT))

                val result = genreIds?.map { it.toInt() }
                val item = MovieItem(adult, backdropPath, result, id, originalLanguage, originalTitle,overview, popularity, posterPath,releaseDate, title,video, voteAverage, voteCount)
                movieList.add(item)
                cursor.moveToNext()
            } while (!cursor.isAfterLast)
        }

        _movieList.postValue(movieList)
    }

    fun fetchLocalDataTV(context: Context) {
        val mDbHelper = MovieDBHelper(context)
        val db = mDbHelper.readableDatabase
        val tvList = mutableListOf<TVItem>()

        // Definisikan kolom mana saja yang akan digunakan untuk menampilkan data
        val projection = arrayOf(
            TVEntry.COLUMN_NAME_BACKDROP_PATH,
            TVEntry.COLUMN_NAME_FIRST_AIR_DATE,
            TVEntry.COLUMN_NAME_GENRE_IDS,
            TVEntry.COLUMN_NAME_ID,
            TVEntry.COLUMN_NAME_NAME,
            TVEntry.COLUMN_NAME_ORIGIN_COUNTRY,
            TVEntry.COLUMN_NAME_ORIGINAL_LANGUAGE,
            TVEntry.COLUMN_NAME_ORIGINAL_NAME,
            TVEntry.COLUMN_NAME_OVERVIEW,
            TVEntry.COLUMN_NAME_POPULARITY,
            TVEntry.COLUMN_NAME_POSTER_PATH,
            TVEntry.COLUMN_NAME_VOTE_AVERAGE,
            TVEntry.COLUMN_NAME_VOTE_COUNT
        )

        val sortOrder = TVEntry.COLUMN_NAME_NAME + " ASC"

        val cursor = db.query(
            TVEntry.TABLE_NAME,  // Tabel yang digunakan
            projection,             // Kolom yang ingin ditampilkan
            null,          // Kolom yang akan kita seleksi untuk kebutuhan filter dalam klausa WHERE
            null,       // Nilai pembanding untuk klausa WHERE untuk proses seleksi
            null,           // apakah akan dilakukan pengelompokan hasil (group), null untuk tidak
            null,            // apakah dilakukan proses filter berdasarkan hasil pengelompokan, null untuk tidak
            sortOrder               // Urutan hasil yang diharapkan
        )

        if (cursor.count>0) {
            cursor.moveToFirst()
            do {
                val backdropPath = cursor.getStringOrNull(cursor.getColumnIndex(TVEntry.COLUMN_NAME_BACKDROP_PATH))
                val firstAirDate = cursor.getStringOrNull(cursor.getColumnIndex(TVEntry.COLUMN_NAME_FIRST_AIR_DATE))
                val genreIds = cursor.getStringOrNull(cursor.getColumnIndex(TVEntry.COLUMN_NAME_GENRE_IDS))
                val id = cursor.getIntOrNull(cursor.getColumnIndex(TVEntry.COLUMN_NAME_ID))
                val name = cursor.getStringOrNull(cursor.getColumnIndex(
                    TVEntry.COLUMN_NAME_NAME))
                val originCountry = cursor.getStringOrNull(cursor.getColumnIndex(TVEntry.COLUMN_NAME_ORIGIN_COUNTRY))
                val originalLanguage = cursor.getStringOrNull(cursor.getColumnIndex(TVEntry.COLUMN_NAME_ORIGINAL_LANGUAGE))
                val originalName = cursor.getStringOrNull(cursor.getColumnIndex(TVEntry.COLUMN_NAME_ORIGINAL_NAME))
                val overview = cursor.getStringOrNull(cursor.getColumnIndex(TVEntry.COLUMN_NAME_OVERVIEW))
                val popularity = cursor.getDoubleOrNull(cursor.getColumnIndex(TVEntry.COLUMN_NAME_POPULARITY))
                val posterPath = cursor.getStringOrNull(cursor.getColumnIndex(TVEntry.COLUMN_NAME_POSTER_PATH))
                val voteAverage = cursor.getDoubleOrNull(cursor.getColumnIndex(TVEntry.COLUMN_NAME_VOTE_AVERAGE))
                val voteCount = cursor.getIntOrNull(cursor.getColumnIndex(TVEntry.COLUMN_NAME_VOTE_COUNT))
                val result = genreIds?.map { it.toInt() }
                val oc = originCountry?.map { it.toString() }
                val item = TVItem(backdropPath,firstAirDate,result,id,name,oc,originalLanguage,
                    originalName,overview,popularity,posterPath,voteAverage,voteCount)
                tvList.add(item)
                cursor.moveToNext()
            } while (!cursor.isAfterLast)
        }

        _tvShowList.postValue(tvList)
    }

    fun cancelAllRequests() = coroutineContext.cancel()
}