package alfatih.me.moviecatalogue.feature.reminder;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import alfatih.me.moviecatalogue.R;
import alfatih.me.moviecatalogue.feature.list.ListActivity;
import alfatih.me.moviecatalogue.util.AppConstant;
import alfatih.me.moviecatalogue.util.DateUtil;
import timber.log.Timber;

public class AlarmReceiver extends BroadcastReceiver {
    public static final String DAILY_REMINDER = "daily_reminder";
    public static final String RELEASE_REMINDER = "release_reminder";
    private static final String EXTRA_MESSAGE = "message";
    private static final String EXTRA_TYPE = "type";
 
    private final static int ID_RELEASE = 100;
    private final static int ID_DAILY = 101;

    private PendingResult pendingResult;

    @Override
    public void onReceive(Context context, Intent intent) {
        String type = intent.getStringExtra(EXTRA_TYPE);
        String message = intent.getStringExtra(EXTRA_MESSAGE);

        String title = null;
        if (type != null) {
            if(type.equalsIgnoreCase(RELEASE_REMINDER)){
                //create a pending intend that you will pass to the Async task so you can tell the system when the Async Task finished so that it can recycle.
                pendingResult = goAsync();
                requestRelease(context);
                return;
            }else{
                title = context.getResources().getString(R.string.app_name);
            }
        }

        int notifId = 0;
        if (type != null) {
            notifId = type.equalsIgnoreCase(RELEASE_REMINDER) ? ID_RELEASE : ID_DAILY;
        }

        // showToast(context, title, message);
 
        showAlarmNotification(context, title, message, notifId);
    }
 
    private void showToast(Context context, String title, String message) {
        Toast.makeText(context, title + " : " + message, Toast.LENGTH_LONG).show();
    }
 
    private void showAlarmNotification(Context context, String title, String message, int notifId) {
        String CHANNEL_ID = "Channel_1";
        String CHANNEL_NAME = "AlarmManager channel";

        Intent notificationIntent = new Intent(context, ListActivity.class);

        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_SINGLE_TOP);

        PendingIntent intent = PendingIntent.getActivity(context, 0,
                notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
 
        NotificationManager notificationManagerCompat = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_access_time_black)
                .setContentTitle(title)
                .setContentText(message)
                .setColor(ContextCompat.getColor(context, android.R.color.transparent))
                .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                .setSound(alarmSound)
                .setContentIntent(intent);
 
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
 
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID,
                    CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_DEFAULT);
 
            channel.enableVibration(true);
            channel.setVibrationPattern(new long[]{1000, 1000, 1000, 1000, 1000});
 
            builder.setChannelId(CHANNEL_ID);
 
            if (notificationManagerCompat != null) {
                notificationManagerCompat.createNotificationChannel(channel);
            }
        }
 
        Notification notification = builder.
                build();
        notification.flags = Notification.FLAG_AUTO_CANCEL;
 
        if (notificationManagerCompat != null) {
            notificationManagerCompat.notify(notifId, notification);
        }
 
    }

    public void setRepeatingAlarm(Context context, String type,String message) {

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.putExtra(EXTRA_MESSAGE, message);
        intent.putExtra(EXTRA_TYPE, type);

        int broadcastID = -1;
        Calendar calendar = Calendar.getInstance();
        if (type != null) {
            if(type.equalsIgnoreCase(RELEASE_REMINDER)){
                calendar.set(Calendar.HOUR_OF_DAY, 8);
            }else{
                calendar.set(Calendar.HOUR_OF_DAY, 7);
            }
            broadcastID = type.equalsIgnoreCase(RELEASE_REMINDER) ? ID_RELEASE : ID_DAILY;
        }
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, broadcastID, intent, 0);
        if (alarmManager != null) {
            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
        }
    }

    private void requestRelease(Context context) {
        String url = "https://api.themoviedb.org/3/discover/movie?api_key="+ AppConstant.API_KEY +"&primary_release_date.gte="+ DateUtil.getCurrentTimeStamp() +"&primary_release_date.lte="+DateUtil.getCurrentTimeStamp();
        new RequestReleaseMovie(context).execute(url);
    }

    @SuppressLint("StaticFieldLeak")
    private class RequestReleaseMovie extends AsyncTask<String, Void, String>{
        final String REQUEST_METHOD = "GET";
        final int READ_TIMEOUT = 15000;
        final int CONNECTION_TIMEOUT = 15000;

        private Context context;

        RequestReleaseMovie(Context context){
            this.context = context;
        }

        @Override
        protected String doInBackground(String... params) {
            String url = params[0];
            String result;
            String inputLine;
            try {
                URL myUrl = new URL(url);
                Timber.d(url);
                HttpURLConnection connection =(HttpURLConnection)
                        myUrl.openConnection();
                connection.setRequestMethod(REQUEST_METHOD);
                connection.setReadTimeout(READ_TIMEOUT);
                connection.setConnectTimeout(CONNECTION_TIMEOUT);

                connection.connect();
                InputStreamReader streamReader = new
                        InputStreamReader(connection.getInputStream());
                BufferedReader reader = new BufferedReader(streamReader);
                StringBuilder stringBuilder = new StringBuilder();
                while((inputLine = reader.readLine()) != null){
                    stringBuilder.append(inputLine);
                }
                //Close our InputStream and Buffered reader
                reader.close();
                streamReader.close();
                //Set our result equal to our stringBuilder
                result = stringBuilder.toString();
                Timber.d(result);
            }catch (IOException e) {
                e.printStackTrace();
                result = null;
                Timber.e("IOException");
            }

            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            try {
                JSONObject object = new JSONObject(result);
                int totalResults = object.getInt("total_results");
                if (totalResults > 3) {
                    totalResults = 3;
                }
                JSONArray array = object.getJSONArray("results");
                // showToast(context, "RESULT ", String.valueOf(totalResults));
                for (int i=0; i<totalResults; i++) {
                    JSONObject obj = array.getJSONObject(i);
                    showAlarmNotification(context, obj.getString("title"), obj.getString("overview"), i);
                }
                pendingResult.finish();
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    public void cancelAlarm(Context context, String type) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmReceiver.class);
        int requestCode = type.equalsIgnoreCase(RELEASE_REMINDER) ? ID_RELEASE : ID_DAILY;
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, requestCode, intent, 0);
        pendingIntent.cancel();
        if (alarmManager != null) {
            alarmManager.cancel(pendingIntent);
        }
    }
 
    private final static String DATE_FORMAT = "yyyy-MM-dd";
    private final static String TIME_FORMAT = "HH:mm";
 
    private boolean isDateInvalid(String date, String format) {
        try {
            DateFormat df = new SimpleDateFormat(format, Locale.getDefault());
            df.setLenient(false);
            df.parse(date);
            return false;
        } catch (ParseException e) {
            return true;
        }
    }
}