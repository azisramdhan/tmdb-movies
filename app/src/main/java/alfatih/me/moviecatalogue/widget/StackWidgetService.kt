package alfatih.me.moviecatalogue.widget

import android.content.Intent
import android.widget.RemoteViewsService

class StackWidgetService : RemoteViewsService() {
    override fun onGetViewFactory(intent: Intent): RemoteViewsFactory =
        FavoriteMovieWidget.StackRemoteViewsFactory(this.applicationContext)
}