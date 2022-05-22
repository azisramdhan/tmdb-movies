package alfatih.me.moviecatalogue.util.cache

import com.orhanobut.hawk.Hawk

object PrefHelper {
    var token: String?
        get() = Hawk.get(PrefKey.token)
        set(value) {
            Hawk.put(PrefKey.token, value)
        }

    var dailyReminder: Boolean?
        get() = Hawk.get(PrefKey.daily_reminder)
        set(value){
            Hawk.put(PrefKey.daily_reminder, value)
        }

    var releaseReminder: Boolean?
        get() = Hawk.get(PrefKey.release_reminder)
        set(value) {
            Hawk.put(PrefKey.release_reminder, value)
        }
}