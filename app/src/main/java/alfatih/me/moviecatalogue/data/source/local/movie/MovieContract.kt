package alfatih.me.moviecatalogue.data.source.local.movie

import android.net.Uri
import android.provider.BaseColumns

object MovieContract {
    const val AUTHORITY = "alfatih.me.moviecatalogue"
    const val SCHEME = "content"
    class MovieEntry: BaseColumns{
        companion object {
            const val TABLE_NAME = "movie"
            const val  COLUMN_NAME_ADULT = "adult"
            const val  COLUMN_NAME_BACKDROP_PATH = "backdrop_path"
            const val  COLUMN_NAME_GENRE_IDS = "genre_ids"
            const val  COLUMN_NAME_ID = BaseColumns._ID
            const val  COLUMN_NAME_ORIGINAL_LANGUAGE = "original_language"
            const val  COLUMN_NAME_ORIGINAL_ORIGINAL_TITLE = "original_title"
            const val  COLUMN_NAME_OVERVIEW = "overview"
            const val  COLUMN_NAME_POPULARITY = "popularity"
            const val  COLUMN_NAME_POSTER_PATH = "poster_path"
            const val  COLUMN_NAME_RELEASE_DATE = "release_date"
            const val  COLUMN_NAME_TITLE = "title"
            const val  COLUMN_NAME_VIDEO = "video"
            const val  COLUMN_NAME_VOTE_AVERAGE = "vote_average"
            const val  COLUMN_NAME_VOTE_COUNT = "vote_count"
            const val  COLUMN_NAME_BASE64_IMAGE = "base64_image"

            val CONTENT_URI: Uri = Uri.Builder().scheme(SCHEME)
                .authority(AUTHORITY)
                .appendPath(TABLE_NAME)
                .build()
        }
    }
}