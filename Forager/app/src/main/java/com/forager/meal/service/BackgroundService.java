package com.forager.meal.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import com.forager.meal.listener.ServiceCallbacks;
import com.forager.meal.utitlity.Utility;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

public class BackgroundService extends Service {

    Context context = this;
    // Binder given to clients
    private final IBinder binder = new LocalBinder();
    // Registered callbacks
    private ServiceCallbacks serviceCallbacks;

    @Override
    public IBinder onBind(Intent intent) {

        return binder;
    }

    // Class used for the client Binder.
    public class LocalBinder extends Binder {
        public BackgroundService getService() {

            // Return this instance of MyService so clients can call public methods
            return BackgroundService.this;
        }
    }

    public void setCallbacks(ServiceCallbacks callbacks) {
        serviceCallbacks = callbacks;
    }

    public BackgroundService() {
    }

    public void onCreate() {
        super.onCreate();

        mTimer = new Timer();
        mTimer.schedule(timerTask, 0, 6000 * 10 * 1);
        //mTimer.schedule(timerTask, 0, 2000);
    }

    private Timer mTimer;
    TimerTask timerTask = new TimerTask() {
        public void run() {

            DateFormat df = new SimpleDateFormat("hh:mm a");
            String date = df.format(Calendar.getInstance().getTime());
            //System.out.println("service started......" + date);
            if (date.equalsIgnoreCase("12:00 AM")) {
                Utility.resetFood(context);
                if (serviceCallbacks != null) {
                    serviceCallbacks.refreshView();
                }


            }


        }
    };

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Let it continue running until it is stopped.
        //System.out.println("service started......");
        return 0;
    }

    public void onDestroy() {
        super.onDestroy();
        try {
            //mTimer.cancel();
            //timerTask.cancel();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}