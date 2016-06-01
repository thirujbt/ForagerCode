package com.forager.meal.async;

import android.app.Activity;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.forager.meal.constants.AppConstants;
import com.forager.meal.listener.OnImageDownloadCompleted;
import com.forager.meal.utitlity.Utility;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by Akbar on 9/8/2015.
 */
public class DownloadImage extends AsyncTask<Void, Void, Void> {

    Activity mActivity;
    OnImageDownloadCompleted onImageDownloadCompleted;
    HashMap<String, String> mHashMap;

    String methodName;

    public DownloadImage(Activity activity, HashMap<String, String> mUrl, String methodName) {
        super();
        this.mActivity = activity;
        mHashMap = mUrl;
        this.onImageDownloadCompleted = (OnImageDownloadCompleted) mActivity;
        this.methodName = methodName;
    }

    public DownloadImage(Fragment fragment, HashMap<String, String> mUrl, String methodName) {
        super();
        this.mActivity = fragment.getActivity();
        mHashMap = mUrl;
        this.onImageDownloadCompleted = (OnImageDownloadCompleted) fragment;
        this.methodName = methodName;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if (Utility.isOnline(mActivity)) {
            Utility.showPDialog(mActivity);
        } else {
            Utility.showAlert(mActivity, AppConstants.CHECK_INTERNET_CONNECTION, false);
        }
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        Utility.dismissPDialog(mActivity);
        onImageDownloadCompleted.onDownloadCompleted(methodName);
    }

    @Override
    protected Void doInBackground(Void... params) {
        int count;
        int tempCount = 1;
        int checkCount = 0;
        try {
            synchronized (this) {

                ArrayList<String> arrayList = new ArrayList<>();
                Iterator<String> iterator = mHashMap.keySet().iterator();
                while (iterator.hasNext()) {
                    String key = iterator.next().toString();
                    String value = mHashMap.get(key);
                    Log.e("Check Key and Value", key + " = " + value);
                    Log.e("Check Count", " = " + checkCount++);
                    URL url = new URL(key);
                    URLConnection connection = url.openConnection();
                    connection.connect();

                    if (arrayList.contains(value)) {
                        tempCount++;
                        Log.e("Temp Count", " = " + tempCount);
                    } else {
                        arrayList.add(value);
                        tempCount = 1;
                    }

                    InputStream input = new BufferedInputStream(url.openStream(), 8192);

                    File file = new File(AppConstants.baseDir, AppConstants.FORAGER_DIR + "/" + value + "/");

                    if (!file.exists()) {
                        file.mkdirs();
                    }

                    OutputStream output = new FileOutputStream(file + "/" + String.valueOf(tempCount) + ".png");

                    byte data[] = new byte[1024];
                    long total = 0;
                    while ((count = input.read(data)) != -1) {
                        total += count;

                        output.write(data, 0, count);
                    }

                    output.flush();

//                        AppConstants.filepath = file.getAbsolutePath();

                    output.close();
                    input.close();
                }


            }
        } catch (Exception e) {
            e.printStackTrace();
        }



            /*Iterator<String> it = mHashMap.keySet().iterator();
            ArrayList<String> tempList = null;



            while(it.hasNext()){
                String key = it.next().toString();
                tempList = mHashMap.get(key);
                if(tempList != null)*//*{

                    for(int i=0;i<tempList.size();i++) {
                        String uri = tempList.get(i);

                        Log.e(key,uri);

                        URL url = new URL(uri);
                        URLConnection connection = url.openConnection();
                        connection.connect();

                        InputStream input = new BufferedInputStream(url.openStream(), 8192);

                        File file = new File(AppConstants.baseDir, AppConstants.FORAGER_DIR+"/"+key+"/");

                        if(!file.exists()){
                            file.mkdirs();
                        }

                        OutputStream output = new FileOutputStream(file+"/"+String.valueOf(i + 1)+".png");


                        byte data[] = new byte[1024];

                        long total = 0;
                        while ((count = input.read(data)) != -1) {
                            total += count;

                            output.write(data, 0, count);
                        }

                        output.flush();

//                        AppConstants.filepath = file.getAbsolutePath();

                        output.close();
                        input.close();

                    }
*//*
                    for(String value: tempList){
                        System.out.println("Key : "+key+ " ,  Value : "+value); }
                //}
              // it.remove();
            }*/


        return null;
    }
}
