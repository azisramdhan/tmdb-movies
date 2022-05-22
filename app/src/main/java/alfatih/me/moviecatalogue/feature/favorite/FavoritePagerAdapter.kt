package alfatih.me.moviecatalogue.feature.favorite

import alfatih.me.moviecatalogue.feature.list.content.ListFragment
import alfatih.me.moviecatalogue.util.AppConstant.CATEGORY_MOVIES
import alfatih.me.moviecatalogue.util.AppConstant.CATEGORY_TV_SHOW
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter

class FavoritePagerAdapter(fm: FragmentManager, private val title: Array<String>): FragmentStatePagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    override fun getItem(position: Int): Fragment
            = when(position) {
        0 -> ListFragment.newInstance(CATEGORY_MOVIES, true)
        1 -> ListFragment.newInstance(CATEGORY_TV_SHOW, true)
        else -> ListFragment.newInstance(CATEGORY_MOVIES, true)
    }

    override fun getCount(): Int = 2

    override fun getPageTitle(position: Int): CharSequence? {
        return when(position){
            0 -> title[0]
            1 -> title[1]
            else -> title[0]
        }
    }
}