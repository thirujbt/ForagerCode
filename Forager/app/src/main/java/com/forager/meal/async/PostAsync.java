package com.forager.meal.async;

import android.app.Activity;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.forager.meal.bean.UserDetails;
import com.forager.meal.constants.AppConstants;
import com.forager.meal.constants.JsonConstants;
import com.forager.meal.listener.OnWebServiceResponse;
import com.forager.meal.utitlity.Utility;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * Created by pickzy01 on 9/7/2015.
 */
public class PostAsync extends AsyncTask<String, Void, String> {

    Activity mActivity;


    OnWebServiceResponse mResponseReceived;

    UserDetails mUserDetails;

    String methodName;

    public PostAsync(Activity activity, UserDetails mDetails, String methodName) {
        this.mActivity = (Activity) activity;
        mResponseReceived = (OnWebServiceResponse) mActivity;
        this.mUserDetails = mDetails;
        this.methodName = methodName;
    }

    public PostAsync(Fragment fragment, UserDetails mUserDetails, String methodName) {
        this.mActivity = fragment.getActivity();
        mResponseReceived = (OnWebServiceResponse) fragment;
        this.mUserDetails = mUserDetails;
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if(Utility.isOnline(mActivity)) {
            Utility.showPDialog(mActivity);
        } else {
            Utility.showAlert(mActivity,AppConstants.CHECK_INTERNET_CONNECTION,false);
        }    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        Utility.dismissPDialog(mActivity);
        mResponseReceived.onResponseReceived(s, methodName);
    }

    @Override
    protected String doInBackground(String... params) {

      /*  ByteArrayOutputStream stream = new ByteArrayOutputStream();
        mUserDetails.getProf_image().compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        ByteArrayBody bab = new ByteArrayBody(byteArray, "profile.jpg");
        Log.e("Check byte Array", byteArray.toString());*/




        /**
         * try {
         AndroidMultiPartEntity entity = new AndroidMultiPartEntity(
         new ProgressListener() {
        @Override
        public void transferred(long num) {
        publishProgress((int) ((num / (float) totalSize) * 100));
        }
        });
         // Adding file data to http body
         // Extra parameters if you want to pass to server
         String fromUserId = data[1];
         String toUserId =data[2];
         String kissType = data[3];
         String msg = data[4];
         String attachTypeName = data[5];
         String toUserLat = data[6];
         String toUSerLang = data[7];
         String fromLat = data[8];
         String toLat = data[9];
         final File sourceFile = new File(data[0]);
         if(attachFile == null){
         entity.addPart("fromauthkey", new StringBody(fromUserId));
         entity.addPart("toauthkey", new StringBody(toUserId));
         entity.addPart("type", new StringBody(kissType));
         entity.addPart("message", new StringBody(msg));
         entity.addPart("attach_type", new StringBody(attachTypeName));
         entity.addPart("to_lat", new StringBody(toUserLat));
         entity.addPart("to_lang", new StringBody(toUSerLang));
         entity.addPart("from_lat", new StringBody(fromLat));
         entity.addPart("from_lang", new StringBody(toLat));
         }else{
         entity.addPart(Constants.ATR_VIDEO_FILE, new FileBody(sourceFile));
         entity.addPart("fromauthkey", new StringBody(fromUserId));
         entity.addPart("toauthkey", new StringBody(toUserId));
         entity.addPart("type", new StringBody(kissType));
         entity.addPart("message", new StringBody(msg));
         entity.addPart("attach_type", new StringBody(attachTypeName));
         entity.addPart("to_lat", new StringBody(toUserLat));
         entity.addPart("to_lang", new StringBody(toUSerLang));
         entity.addPart("from_lat", new StringBody(fromLat));
         entity.addPart("from_lang", new StringBody(toLat));
         }
         totalSize = entity.getContentLength();
         httppost.setEntity(entity);

         // Making server call
         HttpResponse response = httpclient.execute(httppost);
         HttpEntity r_entity = response.getEntity();
         responseString = EntityUtils.toString(r_entity);
         } catch (ClientProtocolException e) {
         responseString = e.toString();
         } catch (Exception e) {
         responseString = e.toString();
         }
         */

        try {
            //File cacheFile = new File(mActivity.getCacheDir(), "robot.png");
            // File cacheFiles = new File(String.valueOf(mActivity.getAssets()), "robot.png");
            // Uri uri = Uri.parse("android.resource://com.forager.pickzy.ui/raw/forager_leaf");
//            final File sourceFile = new File(AppConstants.baseDir+"/"+AppConstants.FORAGER_DIR+"/profile_img.png");

            final File sourceFile = new File(mUserDetails.getProf_url());


            MultipartEntity entity = new MultipartEntity();
            System.out.println("checking text............" + sourceFile);
//            entity.addPart(JsonConstants.USER_ID, new StringBody("322"));
            entity.addPart(JsonConstants.USER_ID, new StringBody(AppConstants.APP_USER_ID));
            if(!Utility.isBlank(mUserDetails.getUser_name()))
                entity.addPart(JsonConstants.USER_NAME_POST, new StringBody(mUserDetails.getUser_name()));

            if(!Utility.isBlank(mUserDetails.getBirth_date()))
                entity.addPart(JsonConstants.USER_DOB_POST, new StringBody(mUserDetails.getBirth_date()));
            if(!Utility.isBlank(mUserDetails.getCountry()))
                entity.addPart(JsonConstants.USER_COUNTRY, new StringBody(mUserDetails.getCountry()));
            if(sourceFile.exists())
                entity.addPart(JsonConstants.USER_PROFILE_PIC, new FileBody(sourceFile));
            if(!Utility.isBlank(mUserDetails.getUnits()))
                entity.addPart(JsonConstants.BODY_UNITS, new StringBody( mUserDetails.getUnits()));
            if(!Utility.isBlank(mUserDetails.getAge()))
                entity.addPart(JsonConstants.BODY_AGE, new StringBody( mUserDetails.getAge()));
            if(!Utility.isBlank(mUserDetails.getWeight()))
                entity.addPart(JsonConstants.BODY_WEIGHT, new StringBody(mUserDetails.getWeight()));
            if(!Utility.isBlank(mUserDetails.getHeight()))
                entity.addPart(JsonConstants.BODY_HEIGHT, new StringBody(mUserDetails.getHeight()));
            if(!Utility.isBlank(mUserDetails.getGender()))
                entity.addPart(JsonConstants.BODY_GENDER, new StringBody(mUserDetails.getGender()));
            if(!Utility.isBlank(mUserDetails.getActivity_level()))
                entity.addPart(JsonConstants.BODY_ACTIVITY_LEVEL, new StringBody(mUserDetails.getActivity_level()));
            if(!Utility.isBlank(String.valueOf(mUserDetails.getBody_calories())))
                entity.addPart(JsonConstants.BODY_CALORIES_POST, new StringBody(String.valueOf(mUserDetails.getBody_calories())));
            if(!Utility.isBlank(mUserDetails.getGoal_type()))
                entity.addPart(JsonConstants.GOAL_TYPE, new StringBody(mUserDetails.getGoal_type()));
            if(!Utility.isBlank(mUserDetails.getGoal_limit()))
                entity.addPart(JsonConstants.GOAL_LIMIT, new StringBody( mUserDetails.getGoal_limit()));
            if(!Utility.isBlank(mUserDetails.getGoal_calories()))
                entity.addPart(JsonConstants.GOAL_CALORIES, new StringBody(mUserDetails.getGoal_calories()));
            if(!Utility.isBlank(mUserDetails.getPlan_calorie()))
                entity.addPart(JsonConstants.PLAN_CALORIES, new StringBody(mUserDetails.getPlan_calorie()));
//            entity.addPart(JsonConstants.PLAN_CALORIES, new StringBody("2200"));
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(params[0]);
//            httppost.setHeader("Accept", "*/*");
//            httppost.setHeader("Content-type", "multipart/form-data");
            // UrlEncodedFormEntity ent = new UrlEncodedFormEntity(entity);
            httppost.setEntity(entity);
            HttpResponse response = httpclient.execute(httppost);
            String responseBody;
            if (response.getStatusLine().getStatusCode() == 200) {
                responseBody = EntityUtils.toString(response.getEntity());
                Log.e("Check Response",responseBody);
            } else {
                responseBody = "";
            }
            return responseBody;

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;

    }

}
