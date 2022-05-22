package alfatih.me.moviecatalogue.feature.reminder

import alfatih.me.moviecatalogue.R
import alfatih.me.moviecatalogue.libs.base.BaseActivity
import alfatih.me.moviecatalogue.util.cache.PrefHelper
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_reminder.*
import java.text.SimpleDateFormat
import java.util.*

class ReminderActivity : BaseActivity() {

    private lateinit var alarmReceiver: AlarmReceiver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reminder)
        setupView()
        initData()
    }

    private fun setupView() {
        setSupportActionBar(toolbar)
        supportActionBar?.title = resources.getString(R.string.reminder)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun initData() {
        alarmReceiver = AlarmReceiver()

        PrefHelper.dailyReminder?.let {
            sw_daily.isChecked = it
        }

        PrefHelper.releaseReminder?.let {
            sw_release.isChecked = it
        }

        sw_daily.setOnCheckedChangeListener { _, isChecked ->
            PrefHelper.dailyReminder = isChecked
            if(isChecked){
                setDailyReminder()
            }else{
                alarmReceiver.cancelAlarm(this, AlarmReceiver.DAILY_REMINDER)
            }
        }
        sw_release.setOnCheckedChangeListener { _, isChecked ->
            PrefHelper.releaseReminder = isChecked
            if(isChecked){
                setReleaseReminder()
            }else{
                alarmReceiver.cancelAlarm(this, AlarmReceiver.RELEASE_REMINDER)
            }
        }
    }

    private fun setDailyReminder() {
        alarmReceiver.setRepeatingAlarm(this, AlarmReceiver.DAILY_REMINDER, getString(R.string.daily_notif_message))
    }

    private fun setReleaseReminder() {
        alarmReceiver.setRepeatingAlarm(this, AlarmReceiver.RELEASE_REMINDER,getString(R.string.daily_notif_message))
    }
}
