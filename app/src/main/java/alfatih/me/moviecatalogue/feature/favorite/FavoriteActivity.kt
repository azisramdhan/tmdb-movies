package alfatih.me.moviecatalogue.feature.favorite

import alfatih.me.moviecatalogue.R
import alfatih.me.moviecatalogue.feature.list.ListPagerAdapter
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_list.*

class FavoriteActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorite)
        setSupportActionBar(toolbar)
        supportActionBar?.title = resources.getString(R.string.favorite)
        setupView()
    }

    private fun setupView() {
        val title = arrayOf(resources.getString(R.string.movies), resources.getString(R.string.tv_show))
        vp_list.adapter = FavoritePagerAdapter(supportFragmentManager, title)
        tl_list.setupWithViewPager(vp_list)
        tl_list.setSelectedTabIndicatorColor(Color.parseColor("#ffffff"))
        tl_list.setTabTextColors(Color.parseColor("#60ffffff"), Color.parseColor("#ffffff"))
    }

}
