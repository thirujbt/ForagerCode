package com.forager.meal.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.text.format.DateFormat;

import com.forager.meal.utitlity.Utility;

import java.util.Calendar;

/**
 * Created by Catherin on 10/14/2015.
 */
public class ResetFood extends WakefulBroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

     //  Toast.makeText(context, "Alarm Start", Toast.LENGTH_SHORT).show();


        String action = intent.getAction();
        if (action != null && action.equalsIgnoreCase(Intent.ACTION_BOOT_COMPLETED)) {
            Intent myIntent = new Intent(context, ResetFood.class);
            context.startService(new Intent(context, BackgroundService.class));
            //alarmManager.set( AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 10000 , pendingIntent );

            boolean alarmUp = (PendingIntent.getBroadcast(context, 0,
                    myIntent,
                    PendingIntent.FLAG_NO_CREATE) != null);

            if (!alarmUp) {

                PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, myIntent, 0);
                AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                Calendar calendar = Calendar.getInstance();
                Calendar currentCal = Calendar.getInstance();

                long now = calendar.getTimeInMillis();

                if (DateFormat.is24HourFormat(context)) {
                    calendar.set(Calendar.HOUR_OF_DAY, 0);
                } else {
                    calendar.set(Calendar.HOUR, 0);
                    calendar.set(Calendar.AM_PM, Calendar.PM);
                }
              /*  calendar.set(Calendar.MINUTE, 0);
                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.MILLISECOND, 0);*/

                calendar.set(Calendar.MINUTE,0);
                calendar.set(Calendar.SECOND,0);
                calendar.set(Calendar.MILLISECOND,0);

                long millisecondsUntilMidnight = calendar.getTimeInMillis() - now;

           /* if (calendar.getTimeInMillis() < System.currentTimeMillis()) {
                millisecondsUntilMidnight += AlarmManager.INTERVAL_DAY;
            }*/
                if (calendar.compareTo(currentCal) < 0) {
                    calendar.add(Calendar.DAY_OF_MONTH, 1);
                }
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT)
                    alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                            AlarmManager.INTERVAL_DAY, pendingIntent);
                else
                    alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, millisecondsUntilMidnight,
                            AlarmManager.INTERVAL_DAY, pendingIntent);
            }
        }

           /* Calendar calendar = Calendar.getInstance();
            long now = calendar.getTimeInMillis();

            calendar.set(Calendar.HOUR_OF_DAY, 0); // For 1 PM or 2 PM
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);

            long millisecondsUntilMidnight = calendar.getTimeInMillis() - now;

            Intent myIntent = new Intent(context, ResetFood.class);

            //alarmManager.set( AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 10000 , pendingIntent );

            boolean alarmUp = (PendingIntent.getBroadcast(context, 0,
                    myIntent,
                    PendingIntent.FLAG_NO_CREATE) != null);



            if (!alarmUp) {

                PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, myIntent, 0);

                if (calendar.getTimeInMillis() < System.currentTimeMillis()) {
                    millisecondsUntilMidnight += AlarmManager.INTERVAL_DAY;
                }


                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT)
                    alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, millisecondsUntilMidnight,
                            AlarmManager.INTERVAL_DAY, pendingIntent);
else
                    alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, millisecondsUntilMidnight,
                            AlarmManager.INTERVAL_DAY, pendingIntent);            }
        }*/




      /*  Uri alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        if (alarmUri == null) {
            alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        }
        Ringtone ringtone = RingtoneManager.getRingtone(context, alarmUri);
        ringtone.play();

        //this will send a notification message
        ComponentName comp = new ComponentName(context.getPackageName(),
                AlarmService.class.getName());
        startWakefulService(context, (intent.setComponent(comp)));
        setResultCode(Activity.RESULT_OK);*/

        Utility.resetFood(context);
    }
}
