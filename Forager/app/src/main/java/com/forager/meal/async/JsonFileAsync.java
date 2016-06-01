package com.forager.meal.async;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;

import com.forager.meal.constants.AppConstants;
import com.forager.meal.listener.OnJsonFileDownloadComplete;
import com.forager.meal.utitlity.Utility;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

/**
 * Created by Akbar on 9/19/2015.
 */
public class JsonFileAsync extends AsyncTask<String, Void, String[]> {

    Activity mActivity;

    OnJsonFileDownloadComplete mListener;
    String[] mUrls;

    public JsonFileAsync(Activity activity, String[] urls, OnJsonFileDownloadComplete listener) {

        super();
        this.mActivity = activity;
        mUrls = urls;
        mListener = listener;
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
    protected void onPostExecute(String[] response) {
        super.onPostExecute(response);
        Utility.dismissPDialog(mActivity);
        mListener.onJsonDownloadComplete(response);

       /* try{
            if (android.os.Build.VERSION.SDK_INT >= 17) {
                if(mActivity!=null && !mActivity.isDestroyed())
                    mListener.onJsonDownloadComplete(response);
            }else{
                if(mActivity!=null)
                    mListener.onJsonDownloadComplete(response);
            }
            if (alertProgressDialog != null && alertProgressDialog.isShowing()&& !activity.isFinishing()) {
                alertProgressDialog.dismiss();
                alertProgressDialog = null;
            }
        }
        catch(Exception ex){
        }*/
    }

    @Override
    protected String[] doInBackground(String... params) {

        String[] responseArray = new String[mUrls.length];

        try {
            for (int i = 0; i < mUrls.length; i++) {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost(mUrls[i]);
                // httppost.setHeader("Accept", "application/json");
                //httppost.setHeader(HTTP.CONTENT_TYPE,HTTP.DEFAULT_CONTENT_TYPE);
                HttpResponse response = httpclient.execute(httppost);
                String responseBody = EntityUtils.toString(response.getEntity());
                Log.e("JsonFileAsync.......", responseBody);
                if (response.getStatusLine().getStatusCode() == 200) {
                    // String responseBody = EntityUtils.toString(response.getEntity());
                    // Log.e("JsonAsync..........................................", responseBody);
                    Utility.dismissPDialog(mActivity);

                    responseArray[i] = responseBody;
                } else {
                    Utility.dismissPDialog(mActivity);
                    Utility.showAlert(mActivity, AppConstants.SERVER_ERROR, false);
                }
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return responseArray;
    }
}




       /* int count;
        int tempCount = 1;
        int checkCount = 0;
        try {
            synchronized(this) {

                ArrayList<String> arrayList = new ArrayList<>();

                Iterator<String> iterator = mHashMap.keySet().iterator();
                while (iterator.hasNext()) {
                    String key = iterator.next().toString();
                    String value = mHashMap.get(key);
                    Log.e("Check Key and Value", key + " = " + value);

                    Log.e("Check Count", " = " + checkCount++);

                    synchronized (this) {

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

                        HttpClient httpclient = new DefaultHttpClient();
                        HttpPost httppost = new HttpPost(params[0]);
                        // httppost.setHeader("Accept", "application/json");
                        //httppost.setHeader(HTTP.CONTENT_TYPE,HTTP.DEFAULT_CONTENT_TYPE);
                        HttpResponse response = httpclient.execute(httppost);
                        String responseBody = EntityUtils.toString(response.getEntity());
                        Log.e("JsonFileAsync.......", responseBody);
                        if (response.getStatusLine().getStatusCode() == 200) {
                            // String responseBody = EntityUtils.toString(response.getEntity());
                            // Log.e("JsonAsync..........................................", responseBody);
                            Utility.dismissPDialog(mActivity);

                            return responseBody;
                        } else {
                            Utility.dismissPDialog(mActivity);
                            Utility.showAlert(mActivity, AppConstants.SERVER_ERROR, false);
                        }
                    }
                }
            }
    } catch (UnsupportedEncodingException e) {
        e.printStackTrace();
    } catch (ClientProtocolException e1) {
        e1.printStackTrace();
    } catch (IOException e) {

    }

        return null;*/




