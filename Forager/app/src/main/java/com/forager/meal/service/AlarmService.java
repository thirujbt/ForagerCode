package com.forager.meal.service;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.forager.meal.ui.R;
import com.forager.meal.ui.activity.DashBoardActivity;

/**
 * Created by Catherin on 10/14/2015.
 */
public class AlarmService extends IntentService {



    private NotificationManager alarmNotificationManager;

    public AlarmService() {
        super("Alarm Service");

    }

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public AlarmService(String name) {
        super("Alarm Service");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        System.out.println("service started......");



       /* String mydate = java.text.DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());
        System.out.println("service started......"+mydate);*/
        sendNotification("Wake Up! Wake Up!");
    }

    private void sendNotification(String msg) {
        Log.d("AlarmService", "Preparing to send notification...: " + msg);
        System.out.println("service started11111111111111111......");


        alarmNotificationManager = (NotificationManager) this
                .getSystemService(Context.NOTIFICATION_SERVICE);

        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, DashBoardActivity.class), 0);

        NotificationCompat.Builder alamNotificationBuilder = new NotificationCompat.Builder(
                this).setContentTitle("Alarm").setSmallIcon(R.drawable.launch_icon)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(msg))
                .setContentText(msg);

        alamNotificationBuilder.setContentIntent(contentIntent);
        alarmNotificationManager.notify(1, alamNotificationBuilder.build());
        Log.d("AlarmService", "Notification sent.");

    }
}
