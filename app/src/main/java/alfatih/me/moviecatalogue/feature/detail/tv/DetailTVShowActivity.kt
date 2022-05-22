package alfatih.me.moviecatalogue.feature.detail.tv

import alfatih.me.moviecatalogue.R
import alfatih.me.moviecatalogue.data.model.tv.TVItem
import alfatih.me.moviecatalogue.data.source.local.tv.TVContract.TVEntry
import alfatih.me.moviecatalogue.libs.base.BaseActivity
import alfatih.me.moviecatalogue.util.AppConstant
import android.content.ContentValues
import android.os.Build
import android.os.Bundle
import android.text.Layout.JUSTIFICATION_MODE_INTER_WORD
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.github.ybq.android.spinkit.style.Wave
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.activity_detail.iv_poster
import kotlinx.android.synthetic.main.activity_detail.spin_kit
import kotlinx.android.synthetic.main.activity_detail.tv_overview
import kotlinx.android.synthetic.main.activity_detail.tv_title
import timber.log.Timber

class DetailTVShowActivity : BaseActivity() {

    private lateinit var detailTVShowVM: DetailTVShowViewModel
    private lateinit var tv: TVItem
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
            tv_overview.justificationMode = JUSTIFICATION_MODE_INTER_WORD
        }
        val wave = Wave()
        spin_kit.setIndeterminateDrawable(wave)
    }

    private fun initData() {
        detailTVShowVM = ViewModelProviders.of(this)[DetailTVShowViewModel::class.java]
        intent.getBundleExtra("bundle")?.let { it ->
            it.getParcelable<TVItem>("tv")?.let {
                Timber.d(it.name)
                tv = it
                Glide.with(this)
                    .load("${AppConstant.BASE_IMAGE_URL}${it.poster_path}")
                    .apply(RequestOptions().transform(CenterCrop(), RoundedCorners(10)))
                    .into(iv_poster)
                it.id?.let {
                    val id = it
                    if(detailTVShowVM.detailTVShow.value == null ){
                        if(isNetworkAvailable()){
                            spin_kit.visibility = View.VISIBLE
                            detailTVShowVM.getDetailTVShow(it)
                        }else{
                            showPopup()
                            return
                        }
                    }
                }
                detailTVShowVM.detailTVShow.observe(this, Observer {
                    spin_kit.visibility = View.INVISIBLE
                    tv_title.text = it.original_name
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
        val db = mDbHelper.readableDatabase
        val projection = arrayOf(TVEntry.COLUMN_NAME_ID)
        var selection = TVEntry.COLUMN_NAME_ID + " = ?"
        var selectionArgs = arrayOf(tv.id.toString())
        val cursor = db.query(
            TVEntry.TABLE_NAME,  // Tabel yang digunakan
            projection,             // Kolom yang ingin ditampilkan
            selection,          // Kolom yang akan kita seleksi untuk kebutuhan filter dalam klausa WHERE
            selectionArgs,       // Nilai pembanding untuk klausa WHERE untuk proses seleksi
            null,           // apakah akan dilakukan pengelompokan hasil (group), null untuk tidak
            null,            // apakah dilakukan proses filter berdasarkan hasil pengelompokan, null untuk tidak
            null               // Urutan hasil yang diharapkan
        )
        return cursor.count > 0
    }

    private fun removeFromFavorite() {
        val db = mDbHelper.writableDatabase
        val selection = TVEntry.COLUMN_NAME_ID + " = ?"
        val selectionArgs = arrayOf(tv.id.toString())
        val deleteStatus = db.delete(TVEntry.TABLE_NAME, selection, selectionArgs)
        Timber.d(deleteStatus.toString())
        if(deleteStatus > 0){
            Toast.makeText(this, getString(R.string.success_response_delete), Toast.LENGTH_LONG).show()
            updateMenu()
        }else{
            Toast.makeText(this, getString(R.string.failed_response_delete), Toast.LENGTH_LONG).show()
        }
    }

    private fun markAsFavorite() {
        // Gets the data repository in write mode
        val db = mDbHelper.writableDatabase

        // Create a new map of values, where column names are the keys
        val values = ContentValues()
        values.put(TVEntry.COLUMN_NAME_BACKDROP_PATH, tv.backdrop_path)
        values.put(TVEntry.COLUMN_NAME_FIRST_AIR_DATE, tv.first_air_date)
        values.put(TVEntry.COLUMN_NAME_GENRE_IDS, tv.genre_ids.toString())
        values.put(TVEntry.COLUMN_NAME_ID, tv.id)
        values.put(TVEntry.COLUMN_NAME_NAME, tv.name)
        values.put(TVEntry.COLUMN_NAME_ORIGIN_COUNTRY, tv.origin_country.toString())
        values.put(TVEntry.COLUMN_NAME_ORIGINAL_LANGUAGE, tv.original_language)
        values.put(TVEntry.COLUMN_NAME_ORIGINAL_NAME, tv.original_name)
        values.put(TVEntry.COLUMN_NAME_OVERVIEW, tv.overview)
        values.put(TVEntry.COLUMN_NAME_POPULARITY, tv.popularity)
        values.put(TVEntry.COLUMN_NAME_POSTER_PATH, tv.poster_path)
        values.put(TVEntry.COLUMN_NAME_VOTE_AVERAGE, tv.vote_average)
        values.put(TVEntry.COLUMN_NAME_VOTE_COUNT, tv.vote_count)

        // Insert the new row, returning the primary key value of the new row
        val insertedRowId = db.insert(TVEntry.TABLE_NAME, null, values)
        Timber.d("INSERTED ROW: $insertedRowId")

        if(insertedRowId > 0){
            Toast.makeText(this, getString(R.string.success_response_favorite), Toast.LENGTH_LONG).show()
            updateMenu()
        }else{
            Toast.makeText(this, getString(R.string.failed_response_favorite), Toast.LENGTH_LONG).show()
        }
    }
}
