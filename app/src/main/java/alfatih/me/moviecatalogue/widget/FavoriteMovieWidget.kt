package alfatih.me.moviecatalogue.widget

import alfatih.me.moviecatalogue.R
import alfatih.me.moviecatalogue.data.source.local.movie.MovieContract
import alfatih.me.moviecatalogue.util.BitmapUtil
import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.graphics.Bitmap
import android.os.Binder
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import android.widget.Toast
import androidx.core.net.toUri
import androidx.core.os.bundleOf

/**
 * Implementation of App Widget functionality.
 */
class FavoriteMovieWidget : AppWidgetProvider() {

    companion object {

        private const val TOAST_ACTION = "alfatih.me.TOAST_ACTION"
        const val EXTRA_ITEM = "alfatih.me.EXTRA_ITEM"

        private fun updateAppWidget(context: Context, appWidgetManager: AppWidgetManager, appWidgetId: Int) {
            val intent = Intent(context, StackWidgetService::class.java)
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
            intent.data = intent.toUri(Intent.URI_INTENT_SCHEME).toUri()

            val views = RemoteViews(context.packageName, R.layout.favorite_movie_widget)
            views.setRemoteAdapter(R.id.stack_view, intent)
            views.setEmptyView(R.id.stack_view, R.id.empty_view)

            val toastIntent = Intent(context, FavoriteMovieWidget::class.java)
            toastIntent.action = TOAST_ACTION
            toastIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
            intent.data = intent.toUri(Intent.URI_INTENT_SCHEME).toUri()
            val toastPendingIntent = PendingIntent.getBroadcast(context, 0, toastIntent, PendingIntent.FLAG_UPDATE_CURRENT)
            views.setPendingIntentTemplate(R.id.stack_view, toastPendingIntent)

            appWidgetManager.updateAppWidget(appWidgetId, views)
        }
    }

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        // There may be multiple widgets active, so update all of them
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        if (intent.action != null) {
            if (intent.action == TOAST_ACTION) {
                val viewIndex = intent.getIntExtra(EXTRA_ITEM, 0)
                Toast.makeText(context, "Touched view $viewIndex", Toast.LENGTH_SHORT).show()
            }
        }
    }

    internal class StackRemoteViewsFactory(private val mContext: Context) : RemoteViewsService.RemoteViewsFactory {

        private val mWidgetItems = ArrayList<Bitmap>()

        override fun onDataSetChanged() {
            fetchLocalDataMovie(mContext)
        }

        override fun onCreate() {

        }

        override fun onDestroy() {

        }

        override fun getCount(): Int = mWidgetItems.size

        override fun getViewAt(position: Int): RemoteViews {
            val rv = RemoteViews(mContext.packageName, R.layout.widget_item)
            rv.setImageViewBitmap(R.id.iv_poster, mWidgetItems[position])
            val extras = bundleOf(
                EXTRA_ITEM to position
            )
            val fillInIntent = Intent()
            fillInIntent.putExtras(extras)
            rv.setOnClickFillInIntent(R.id.iv_poster, fillInIntent)
            return rv
        }

        override fun getLoadingView(): RemoteViews? = null

        override fun getViewTypeCount(): Int = 1

        override fun getItemId(i: Int): Long = 0

        override fun hasStableIds(): Boolean = false

        private fun fetchLocalDataMovie(context: Context) {
            val sortOrder = MovieContract.MovieEntry.COLUMN_NAME_TITLE + " ASC"
            val identityToken = Binder.clearCallingIdentity()
            val cursor = context.contentResolver?.query(MovieContract.MovieEntry.CONTENT_URI, null, null, null, sortOrder) as Cursor
            Binder.restoreCallingIdentity(identityToken)
            if (cursor.count>0) {
                cursor.moveToFirst()
                do {
                    val base64 = cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_NAME_BASE64_IMAGE))
                    val byte = BitmapUtil.getByte(base64)
                    val bitmap = BitmapUtil.getBitmapImage(byte)
                    mWidgetItems.add(bitmap)
                    cursor.moveToNext()
                } while (!cursor.isAfterLast)
            }

        }

    }
}

