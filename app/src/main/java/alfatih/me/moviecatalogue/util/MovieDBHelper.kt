package alfatih.me.moviecatalogue.util

import alfatih.me.moviecatalogue.data.source.local.movie.MovieContract.MovieEntry
import alfatih.me.moviecatalogue.data.source.local.tv.TVContract.TVEntry
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class MovieDBHelper (context: Context): SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION){

    companion object {
        //Jika kamu mengubah skema database maka harus dinaikkan versi databasenya.
        const val DATABASE_VERSION = 1
        const val DATABASE_NAME = "movie.db"
    }

    private val SQL_CREATE_ENTRIES = "CREATE TABLE ${MovieEntry.TABLE_NAME}" +
            " (${MovieEntry.COLUMN_NAME_ADULT} TEXT," +
            " ${MovieEntry.COLUMN_NAME_BACKDROP_PATH} TEXT,"+
            " ${MovieEntry.COLUMN_NAME_GENRE_IDS} TEXT,"+
            " ${MovieEntry.COLUMN_NAME_ID} TEXT PRIMARY KEY,"+
            " ${MovieEntry.COLUMN_NAME_ORIGINAL_LANGUAGE} TEXT,"+
            " ${MovieEntry.COLUMN_NAME_ORIGINAL_ORIGINAL_TITLE} TEXT,"+
            " ${MovieEntry.COLUMN_NAME_OVERVIEW} TEXT,"+
            " ${MovieEntry.COLUMN_NAME_POPULARITY} TEXT,"+
            " ${MovieEntry.COLUMN_NAME_POSTER_PATH} TEXT,"+
            " ${MovieEntry.COLUMN_NAME_RELEASE_DATE} TEXT,"+
            " ${MovieEntry.COLUMN_NAME_TITLE} TEXT,"+
            " ${MovieEntry.COLUMN_NAME_VIDEO} TEXT,"+
            " ${MovieEntry.COLUMN_NAME_VOTE_AVERAGE} TEXT,"+
            " ${MovieEntry.COLUMN_NAME_VOTE_COUNT} TEXT," +
            " ${MovieEntry.COLUMN_NAME_BASE64_IMAGE} TEXT)"

    private val SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS $MovieEntry.TABLE_NAME"


    private val SQL_CREATE_ENTRIES_TV = "CREATE TABLE ${TVEntry.TABLE_NAME}" +
            " (${TVEntry.COLUMN_NAME_BACKDROP_PATH} TEXT," +
            " ${TVEntry.COLUMN_NAME_FIRST_AIR_DATE} TEXT,"+
            " ${TVEntry.COLUMN_NAME_GENRE_IDS} TEXT,"+
            " ${TVEntry.COLUMN_NAME_ID} TEXT PRIMARY KEY,"+
            " ${TVEntry.COLUMN_NAME_NAME} TEXT,"+
            " ${TVEntry.COLUMN_NAME_ORIGIN_COUNTRY} TEXT,"+
            " ${TVEntry.COLUMN_NAME_ORIGINAL_LANGUAGE} TEXT,"+
            " ${TVEntry.COLUMN_NAME_ORIGINAL_NAME} TEXT,"+
            " ${TVEntry.COLUMN_NAME_OVERVIEW} TEXT,"+
            " ${TVEntry.COLUMN_NAME_POPULARITY} TEXT,"+
            " ${TVEntry.COLUMN_NAME_POSTER_PATH} TEXT,"+
            " ${TVEntry.COLUMN_NAME_VOTE_AVERAGE} TEXT,"+
            " ${TVEntry.COLUMN_NAME_VOTE_COUNT} TEXT)"

    private val SQL_DELETE_ENTRIES_TV = "DROP TABLE IF EXISTS $TVEntry.TABLE_NAME"

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_ENTRIES)
        db.execSQL(SQL_CREATE_ENTRIES_TV)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        //Method ini untuk melakukan proses upgrade pada perubahan tabel dan skema database. Fokus migrasi data akan dilakukan disini
        db.execSQL(SQL_DELETE_ENTRIES)
        db.execSQL(SQL_DELETE_ENTRIES_TV)
        onCreate(db)
    }

    override fun onDowngrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        onUpgrade(db, oldVersion, newVersion)
    }

}