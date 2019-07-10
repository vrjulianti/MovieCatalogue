package com.vrjulianti.moviedatabase.Reminder;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import com.vrjulianti.moviedatabase.Model.Movie;

import java.util.Calendar;
import java.util.List;

import static android.content.ContentValues.TAG;

public class ReleaseTodayReceiver extends BroadcastReceiver {

    private static final int NOTIF_ID_REPEATING = 101;
    private static int notificationId;

    public ReleaseTodayReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        notificationId = intent.getIntExtra("id", 0);
        String title = intent.getStringExtra("movieTitle");

        showAlarmNotification(context, title, notificationId);
    }

    private void showAlarmNotification(Context context, String title, int notifId) {

        NotificationManager notificationManagerCompat = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        Log.d(TAG, "showAlarmNotification: " + notifId);

        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setContentTitle(title)
                .setContentText("Today " + title + " release")
                .setAutoCancel(true)
                .setColor(ContextCompat.getColor(context, android.R.color.transparent))
                .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                .setSound(alarmSound);

        if (notificationManagerCompat != null) {
            notificationManagerCompat.notify(notifId, builder.build());
        }
    }


    public void setRepeatingAlarm(Context context, List<Movie> movieList) {

        Log.d(TAG, "setRepeatingAlarm: " + movieList.size());

        int notificationDelay = 0;

        for (int i = 0; i < movieList.size(); i++) {
            cancelAlarm(context);
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

            Intent intent = new Intent(context, ReleaseTodayReceiver.class);
            intent.putExtra("movieTitle", movieList.get(i).getTitle());
            intent.putExtra("id", notificationId);

            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 100, intent, PendingIntent.FLAG_UPDATE_CURRENT);

            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, 8);
            calendar.set(Calendar.MINUTE, 5);

            if (alarmManager != null) {
                alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis() + notificationDelay, AlarmManager.INTERVAL_DAY, pendingIntent);
            }

            notificationId ++;
            notificationDelay += 1000;
        }

        Toast.makeText(context, "Release reminder set up", Toast.LENGTH_SHORT).show();

    }

    public void cancelAlarm(Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (alarmManager != null) {
            alarmManager.cancel(getPendingIntent(context));
        }
    }

    private static PendingIntent getPendingIntent(Context context) {

        Intent intent = new Intent(context, DailyReceiver.class);
        return PendingIntent.getBroadcast(context, 101, intent, PendingIntent.FLAG_CANCEL_CURRENT);

    }
}
