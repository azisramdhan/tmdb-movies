package alfatih.me.moviecatalogue.libs.base

import alfatih.me.moviecatalogue.R
import alfatih.me.moviecatalogue.util.MovieDBHelper
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

abstract class BaseActivity : AppCompatActivity() {

    lateinit var mDbHelper: MovieDBHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base)
        mDbHelper = MovieDBHelper(this)

    }
    fun isNetworkAvailable(): Boolean {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE)
        return if (connectivityManager is ConnectivityManager) {
            val networkInfo: NetworkInfo? = connectivityManager.activeNetworkInfo
            networkInfo?.isConnected ?: false
        } else false
    }

    fun showPopup() {
        val builder = AlertDialog.Builder(this)
            .setCancelable(false)
            .setTitle("You are offline")
            .setMessage("This application requires an internet connection")
            .setPositiveButton("OK"){dialog, which ->
                finish()
            }
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }
}