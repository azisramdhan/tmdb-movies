package alfatih.me.moviecatalogue.feature.list

import alfatih.me.moviecatalogue.R
import alfatih.me.moviecatalogue.feature.favorite.FavoriteActivity
import alfatih.me.moviecatalogue.feature.reminder.ReminderActivity
import alfatih.me.moviecatalogue.libs.base.BaseActivity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_list.*


class ListActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)
        setSupportActionBar(toolbar)
        supportActionBar?.title = resources.getString(R.string.app_name)
        setupView()
    }

    private fun setupView() {
        val title = arrayOf(resources.getString(R.string.movies), resources.getString(R.string.tv_show))
        vp_list.adapter = ListPagerAdapter(supportFragmentManager, title)
        tl_list.setupWithViewPager(vp_list)
        tl_list.setSelectedTabIndicatorColor(Color.parseColor("#ffffff"))
        tl_list.setTabTextColors(Color.parseColor("#60ffffff"), Color.parseColor("#ffffff"))
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.favorite){
            val mIntent = Intent(this, FavoriteActivity::class.java)
            startActivity(mIntent)
        }else if(item.itemId == R.id.language){
            val mIntent = Intent(Settings.ACTION_LOCALE_SETTINGS)
            startActivity(mIntent)
        }else if(item.itemId == R.id.reminder){
            startActivity(Intent(this, ReminderActivity::class.java))
        }
        return true
    }
}
