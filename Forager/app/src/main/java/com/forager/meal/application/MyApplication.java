package com.forager.meal.application;

import android.app.Application;
import android.content.Context;
import android.graphics.Typeface;

/**
 * Created by Akbar on 9/22/2015.
 */
public class MyApplication extends Application {

    public static Context getContext() {
        return context;
    }

    public static void setContext(Context context) {
        MyApplication.context = context;
    }

    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public static Typeface getFont() {
        return  Typeface.createFromAsset(getContext().getResources().getAssets(), "fonts/philosopher_regular.ttf");
    }
}
