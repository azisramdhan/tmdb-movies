package alfatih.me.moviecatalogue.data.source.local.movie

import alfatih.me.moviecatalogue.data.source.local.movie.MovieContract.AUTHORITY
import alfatih.me.moviecatalogue.data.source.local.movie.MovieContract.MovieEntry.Companion.CONTENT_URI
import alfatih.me.moviecatalogue.data.source.local.movie.MovieContract.MovieEntry.Companion.TABLE_NAME
import alfatih.me.moviecatalogue.util.MovieDBHelper
import android.content.ContentProvider
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.net.Uri
import android.provider.BaseColumns._ID

class MovieProvider : ContentProvider() {

    companion object {
        private const val MOVIE = 1
        private const val MOVIE_ID = 2
        private lateinit var db: SQLiteDatabase
        private val sUriMatcher = UriMatcher(UriMatcher.NO_MATCH)
        init {
            // content://alfatih.me.moviecatalogue/movie
            sUriMatcher.addURI(AUTHORITY, TABLE_NAME, MOVIE)
            // content://alfatih.me.moviecatalogue/movie/id
            sUriMatcher.addURI(AUTHORITY,
                "$TABLE_NAME/#",
                MOVIE_ID)
        }
    }

    override fun onCreate(): Boolean {
        context?.let {
            db = MovieDBHelper(it).writableDatabase
        }
        return true
    }
    override fun query(uri: Uri, projection: Array<String>?, selection: String?, selectionArgs: Array<String>?, sortOrder: String?): Cursor? {
        return when (sUriMatcher.match(uri)) {
            MOVIE -> db.query(
                TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                "$_ID ASC",
                null)
            MOVIE_ID -> db.query(TABLE_NAME,
                null, "$_ID = ?", arrayOf(uri.lastPathSegment.toString()), null, null, null, null)
            else -> null
        }
    }

    override fun getType(uri: Uri): String? {
        return null
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        val added: Long = when (MOVIE) {
            sUriMatcher.match(uri) -> db.insert(TABLE_NAME, null, values)
            else -> 0
        }
        context?.contentResolver?.notifyChange(CONTENT_URI, null)
        return Uri.parse("$CONTENT_URI/$added")
    }
    override fun update(uri: Uri, values: ContentValues?, selection: String?, selectionArgs: Array<String>?): Int {
        val updated: Int = when (MOVIE_ID) {
            sUriMatcher.match(uri) -> db.update(TABLE_NAME, values, "$_ID = ?", arrayOf(uri.lastPathSegment.toString()))
            else -> 0
        }
        context?.contentResolver?.notifyChange(CONTENT_URI, null)
        return updated
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        val deleted: Int = when (MOVIE_ID) {
            sUriMatcher.match(uri) -> db.delete(TABLE_NAME, "$_ID = ${uri.lastPathSegment.toString()}", null)
            else -> 0
        }
        context?.contentResolver?.notifyChange(CONTENT_URI, null)
        return deleted
    }
}
