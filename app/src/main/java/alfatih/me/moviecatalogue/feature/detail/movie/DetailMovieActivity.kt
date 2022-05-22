package alfatih.me.moviecatalogue.feature.detail.movie

import alfatih.me.moviecatalogue.R
import alfatih.me.moviecatalogue.data.model.movie.MovieItem
import alfatih.me.moviecatalogue.data.source.local.movie.MovieContract.MovieEntry
import alfatih.me.moviecatalogue.data.source.local.movie.MovieContract.MovieEntry.Companion.CONTENT_URI
import alfatih.me.moviecatalogue.libs.base.BaseActivity
import alfatih.me.moviecatalogue.util.AppConstant
import alfatih.me.moviecatalogue.util.BitmapUtil
import android.content.ContentValues
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.Layout
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.github.ybq.android.spinkit.style.Wave
import kotlinx.android.synthetic.main.activity_detail.*
import timber.log.Timber



class DetailMovieActivity : BaseActivity() {

    private lateinit var detailMovieVM: DetailMovieViewModel
    private lateinit var movie: MovieItem
    private lateinit var menu: Menu

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        setupView()
        initData()
    }

    private fun setupView() {
        setSupportActionBar(toolbar)
        supportActionBar?.title = ""
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            tv_overview.justificationMode = Layout.JUSTIFICATION_MODE_INTER_WORD
        }
        val wave = Wave()
        spin_kit.setIndeterminateDrawable(wave)
    }

    private fun initData() {
        detailMovieVM = ViewModelProviders.of(this)[DetailMovieViewModel::class.java]
        intent.getBundleExtra("bundle")?.let { it ->
            it.getParcelable<MovieItem>("movie")?.let{
                Timber.d(it.title)
                movie = it
                Glide.with(this)
                    .load("${AppConstant.BASE_IMAGE_URL}${it.poster_path}")
                    .apply(RequestOptions().transform(CenterCrop(), RoundedCorners(10)))
                    .into(iv_poster)
                it.id?.let {
                    val id = it
                    if(detailMovieVM.detailMovie.value == null ){
                        if(isNetworkAvailable()){
                            spin_kit.visibility = View.VISIBLE
                            detailMovieVM.getDetailMovie(id)
                        }else{
                            showPopup()
                            return
                        }
                    }
                }
                detailMovieVM.detailMovie.observe(this, Observer {
                    spin_kit.visibility = View.INVISIBLE
                    tv_title.text = it.original_title
                    tv_overview.text = it.overview
                })
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_favorite, menu)
        this.menu = menu
        updateMenu()
        return true
    }

    private fun updateMenu(){
        if(isFavorite()){
            menu.findItem(R.id.mark_favorite).title = resources.getString(R.string.remove_from_favorite)
        }else{
            menu.findItem(R.id.mark_favorite).title = resources.getString(R.string.mark_as_favorite)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.mark_favorite){
            if(isFavorite()){
                removeFromFavorite()
            }else{
                markAsFavorite()
            }
        }else if(item.itemId == android.R.id.home){
            finish()
        }
        return true
    }

    private fun isFavorite(): Boolean {
        val uriWithId = Uri.parse(CONTENT_URI.toString() + "/" + movie.id)
        val cursor = contentResolver?.query(uriWithId, null, null, null, null) as Cursor
        Timber.d(cursor.count.toString())
        return cursor.count > 0
    }

    private fun removeFromFavorite() {
        val uriWithId = Uri.parse(CONTENT_URI.toString() + "/" + movie.id)
        val selection = MovieEntry.COLUMN_NAME_ID + " = ?"
        val selectionArgs = arrayOf(movie.id.toString())
        contentResolver.delete(uriWithId, selection, selectionArgs)
        updateMenu()
    }

    private fun markAsFavorite() {
        Glide.with(this)
            .asBitmap()
            .load("${AppConstant.BASE_IMAGE_URL}${movie.poster_path}")
            .into(object : CustomTarget<Bitmap>(){
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    val base64 = BitmapUtil.getBase64(BitmapUtil.getBytes(resource))
                    // Create a new map of values, where column names are the keys
                    val values = ContentValues()
                    values.put(MovieEntry.COLUMN_NAME_ADULT, movie.adult)
                    values.put(MovieEntry.COLUMN_NAME_BACKDROP_PATH, movie.backdrop_path)
                    values.put(MovieEntry.COLUMN_NAME_GENRE_IDS, movie.genre_ids.toString())
                    values.put(MovieEntry.COLUMN_NAME_ID, movie.id)
                    values.put(MovieEntry.COLUMN_NAME_ORIGINAL_LANGUAGE, movie.original_language)
                    values.put(MovieEntry.COLUMN_NAME_ORIGINAL_ORIGINAL_TITLE, movie.original_title)
                    values.put(MovieEntry.COLUMN_NAME_OVERVIEW, movie.overview)
                    values.put(MovieEntry.COLUMN_NAME_POPULARITY, movie.popularity)
                    values.put(MovieEntry.COLUMN_NAME_POSTER_PATH, movie.poster_path)
                    values.put(MovieEntry.COLUMN_NAME_RELEASE_DATE, movie.release_date)
                    values.put(MovieEntry.COLUMN_NAME_TITLE, movie.title)
                    values.put(MovieEntry.COLUMN_NAME_VIDEO, movie.video)
                    values.put(MovieEntry.COLUMN_NAME_VOTE_AVERAGE, movie.vote_average)
                    values.put(MovieEntry.COLUMN_NAME_VOTE_COUNT, movie.vote_count)
                    values.put(MovieEntry.COLUMN_NAME_BASE64_IMAGE, base64)
                    contentResolver.insert(CONTENT_URI, values)
                    Timber.d("INSERT_MOVIE_SUCCESS")
                    updateMenu()
                }
                override fun onLoadCleared(placeholder: Drawable?) {
                    // this is called when imageView is cleared on lifecycle call or for
                    // some other reason.
                    // if you are referencing the bitmap somewhere else too other than this imageView
                    // clear it here as you can no longer have the bitmap
                }
            })


    }
}
