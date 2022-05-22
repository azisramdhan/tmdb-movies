package alfatih.me.moviecatalogue.data.source.local.tv

import android.net.Uri
import android.provider.BaseColumns

object TVContract {
    const val AUTHORITY = "alfatih.me.moviecatalogue"
    const val SCHEME = "content"
    class TVEntry: BaseColumns{
        companion object {
            const val TABLE_NAME = "tv"
            const val  COLUMN_NAME_BACKDROP_PATH = "backdrop_path"
            const val  COLUMN_NAME_FIRST_AIR_DATE = "first_air_date"
            const val  COLUMN_NAME_GENRE_IDS = "genre_ids"
            const val  COLUMN_NAME_ID = BaseColumns._ID
            const val  COLUMN_NAME_NAME = "name"
            const val  COLUMN_NAME_ORIGIN_COUNTRY = "origin_country"
            const val  COLUMN_NAME_ORIGINAL_LANGUAGE = "original_language"
            const val  COLUMN_NAME_ORIGINAL_NAME = "original_name"
            const val  COLUMN_NAME_OVERVIEW = "overview"
            const val  COLUMN_NAME_POPULARITY = "popularity"
            const val  COLUMN_NAME_POSTER_PATH = "poster_path"
            const val  COLUMN_NAME_VOTE_AVERAGE = "vote_average"
            const val  COLUMN_NAME_VOTE_COUNT = "vote_count"

            val CONTENT_URI: Uri = Uri.Builder().scheme(SCHEME)
                .authority(AUTHORITY)
                .appendPath(TABLE_NAME)
                .build()
        }
    }
}