package com.forager.meal.async;

import android.app.Activity;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;

import com.forager.meal.constants.AppConstants;
import com.forager.meal.listener.OnWebServiceResponse;
import com.forager.meal.utitlity.Utility;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

/**
 * Created by Akbar on 9/1/2015.
 */

public class JsonAsync extends AsyncTask<String, Void, String> {


    Activity mActivity;

    OnWebServiceResponse mResponseReceived;

    String jsonParam;

    String methodName;

    private static final String TAG = JsonAsync.class.getSimpleName();

    public JsonAsync(Activity activity, String jsonObjectString, String methodName) {
        mActivity = activity;
        mResponseReceived = (OnWebServiceResponse) mActivity;
        jsonParam = jsonObjectString;
        this.methodName = methodName;
    }

    public JsonAsync(Fragment fragment, String jsonObjectString, String methodName) {
        mActivity = fragment.getActivity();
        mResponseReceived = (OnWebServiceResponse) fragment;
        jsonParam = jsonObjectString;
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
    protected String doInBackground(String... params) {
        String responseBody = null;
        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(params[0]);
            StringEntity entity = new StringEntity(jsonParam);
            httppost.setEntity(entity);
            //httppost.setHeader("Accept", "application/json");
            //httppost.setHeader(HTTP.CONTENT_TYPE,HTTP.DEFAULT_CONTENT_TYPE);
            HttpResponse response = httpclient.execute(httppost);

            //  Log.e("JsonAsync......", responseBody);
            if (response.getStatusLine().getStatusCode() == 200) {
                //responseBody = EntityUtils.toString(response.getEntity());
                // Log.e("JsonAsync..........................................", responseBody);
                responseBody = EntityUtils.toString(response.getEntity());
                return responseBody;
            } else {
                Utility.dismissPDialog(mActivity);
                Utility.showAlert(mActivity, AppConstants.SERVER_ERROR, false);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e1) {
            e1.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return responseBody;

    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        Utility.dismissPDialog(mActivity);
        mResponseReceived.onResponseReceived(s, methodName);

    }

    private String getQuery(List<NameValuePair> params) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;

        for (NameValuePair pair : params) {
            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(pair.getName(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(pair.getValue(), "UTF-8"));
        }
        return result.toString();
    }

}
