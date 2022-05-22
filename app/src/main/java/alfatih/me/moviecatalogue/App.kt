package alfatih.me.moviecatalogue

import alfatih.me.moviecatalogue.feature.detail.movie.module.DetailMovieModule
import alfatih.me.moviecatalogue.feature.detail.tv.module.DetailTVShowModule
import alfatih.me.moviecatalogue.feature.list.module.ListModule
import android.app.Application
import com.orhanobut.hawk.Hawk
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import timber.log.Timber
import timber.log.Timber.DebugTree

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        setupTimber()
        setupKoin()
        setupHawk()
    }

    private fun setupHawk() {
        Hawk.init(this).build()
    }

    private fun setupKoin() {
        startKoin {
            androidContext(this@App)
            ListModule.module
            DetailMovieModule.module
            DetailTVShowModule.module
        }
    }

    private fun setupTimber(){
        if (BuildConfig.DEBUG) {
            Timber.plant(DebugTree())
        }
    }

}