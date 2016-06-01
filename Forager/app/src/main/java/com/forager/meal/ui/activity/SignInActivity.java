package com.forager.meal.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.forager.meal.application.MyApplication;
import com.forager.meal.async.DownloadImage;
import com.forager.meal.async.JsonAsync;
import com.forager.meal.async.JsonFileAsync;
import com.forager.meal.bean.PlanDetails;
import com.forager.meal.bean.UserDetails;
import com.forager.meal.constants.AppConstants;
import com.forager.meal.constants.AppUrls;
import com.forager.meal.constants.JsonConstants;
import com.forager.meal.database.Database;
import com.forager.meal.listener.OnImageDownloadCompleted;
import com.forager.meal.listener.OnJsonFileDownloadComplete;
import com.forager.meal.listener.OnWebServiceResponse;
import com.forager.meal.ui.R;
import com.forager.meal.utitlity.Calculations;
import com.forager.meal.utitlity.Utility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

public class SignInActivity extends Activity implements View.OnClickListener, OnWebServiceResponse, OnImageDownloadCompleted {

    ImageButton mBack;

    EditText mEmail_edit;

    EditText mPassword_edit;

    CheckBox mRemember_chckbx;

    TextView mForgot_text;

    Button mSignIn_btn;

    TextView mSignIn;

    Database mDatabase;

    File file;

    String path;

    public static ArrayList<PlanDetails> mPlanDetailsArrayList = new ArrayList<>();

    HashMap<String, String> mHashMap = new HashMap<>();

    public static String active_folder;

    String user_status;

    private String[] array = {AppConstants.PLANLISTS_FILENAME, AppConstants.EXTRAFOODPLAN, AppConstants.GROCERYFOODPLAN};

    private String[] arrayValues;

    String getPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        mDatabase = Database.getInstance(this);
        mDatabase.getMyWritableDatabase();
        init();
        String filepath = Environment.getExternalStorageDirectory().getPath();
        file = new File(filepath, "Forager");
        if (!file.exists()) {
            file.mkdirs();
        }
    }


    private void init() {

        MyApplication.setContext(SignInActivity.this);
        mSignIn = (TextView) findViewById(R.id.title_signin);
        mSignIn.setTypeface(MyApplication.getFont());

        mBack = (ImageButton) findViewById(R.id.back);
        mEmail_edit = (EditText) findViewById(R.id.edit_name);
        mPassword_edit = (EditText) findViewById(R.id.edit_password);
        mRemember_chckbx = (CheckBox) findViewById(R.id.chckbox_remember_me);
        mForgot_text = (TextView) findViewById(R.id.txt_forget_password);
        mSignIn_btn = (Button) findViewById(R.id.sign_btn);

        mBack.setOnClickListener(this);
        mSignIn_btn.setOnClickListener(this);
        mForgot_text.setOnClickListener(this);
        mRemember_chckbx.setButtonDrawable(R.drawable.checkbox_selector);
        SharedPreferences preferences = Utility.getSharedPreferences(SignInActivity.this, AppConstants.SHARED_PREFES_LOGIN_DETAILS);
        String email_id = preferences.getString(AppConstants.EMAIL_ID, "");
        String password = preferences.getString(AppConstants.PASSWORD, "");
        if (!Utility.isBlank(email_id) && !Utility.isBlank(password)) {
            mEmail_edit.setText(email_id);
            mPassword_edit.setText(password);
            mRemember_chckbx.setChecked(true);
        }
    }

    @Override
    public void onDestroy() {
        mDatabase.closeDatabase();
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_sign_in, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void validate_Fields() {
        if (Utility.isBlank(mEmail_edit.getText().toString())) {
            Utility.showAlert(SignInActivity.this, AppConstants.ENTER_EMAIL, false);
        } else if (!Utility.validateEmailId(mEmail_edit.getText().toString())) {
            Utility.showAlert(SignInActivity.this, AppConstants.ENTER_VALID_EMAIL, false);
        } else if (Utility.isBlank(mPassword_edit.getText().toString())) {
            Utility.showAlert(SignInActivity.this, AppConstants.ENTER_PASSWORD, false);
        } else {
            try {
                mPlanDetailsArrayList.clear();
                JSONObject jsonObj = new JSONObject();
                jsonObj.put(JsonConstants.EMAIL, mEmail_edit.getText().toString());
                jsonObj.put(JsonConstants.PASSWORD, mPassword_edit.getText().toString());

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
                    new JsonAsync(SignInActivity.this, jsonObj.toString(), AppConstants.LOGIN_CALL).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, AppUrls.LOGIN_URL);
                else
                    new JsonAsync(SignInActivity.this, jsonObj.toString(), AppConstants.LOGIN_CALL).execute(AppUrls.LOGIN_URL);

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {

            case R.id.back:
                finish();
                break;

            case R.id.txt_forget_password:
                Intent mForgotPasswordIntent = new Intent(SignInActivity.this, ForgotPasswordActivity.class);
                startActivity(mForgotPasswordIntent);
                break;

            case R.id.sign_btn:
                validate_Fields();


        }
    }

    @Override
    public void onResponseReceived(String response, String methodName) {
        if (!Utility.isBlank(response)) {
            Log.e("Check Response", response);

            if (methodName.equalsIgnoreCase(AppConstants.LOGIN_CALL)) {

                try {
                    JSONObject mJsonObject = new JSONObject(response);
                    String errorCode = mJsonObject.getString(JsonConstants.ERROR_CODE);
                    if (errorCode.equals(AppConstants.ERROR_CODE_SUCCESS)) {

                        SharedPreferences.Editor Editor = Utility.getSharedPreferences(SignInActivity.this, AppConstants.SHARED_PREFES_LOGIN_DETAILS).edit();
                        if (mRemember_chckbx.isChecked()) {
                            Editor.putString(AppConstants.EMAIL_ID, mEmail_edit.getText().toString());
                            Editor.putString(AppConstants.PASSWORD, mPassword_edit.getText().toString());
                        } else {
                            Editor.putString(AppConstants.EMAIL_ID, "");
                            Editor.putString(AppConstants.PASSWORD, "");
                        }
                        Editor.commit();
                        mEmail_edit.setText("");
                        mPassword_edit.setText("");
                        parseJson(response);

                    } else if (errorCode.equals(AppConstants.ERROR_CODE_FAILURE)) {
                        if (mJsonObject.has(JsonConstants.MESSAGE)) {
                            String message = mJsonObject.getString(JsonConstants.MESSAGE);
                            Utility.showAlert(SignInActivity.this, message, false);
                        } else {
                            Utility.showAlert(SignInActivity.this, AppConstants.LOGIN_FAILED, false);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else if (methodName.equalsIgnoreCase(AppConstants.UPDATE_ACTIVE_CALL)) {
                try {
                    JSONObject mJsonObject = new JSONObject(response);

                    if (mJsonObject.has("Message") && mJsonObject.getString("Message").contains("No Records Found")) {
                        Utility.dismissPDialog(SignInActivity.this);
                        Utility.showAlert(SignInActivity.this, "Member Details Error", false);
                        return;
                    }
                    String planLists = mJsonObject.getString(JsonConstants.PLANLISTS);
                    String extraFoodPlan = mJsonObject.getString(JsonConstants.EXTRAFOODPLAN);
                    String groceryFoodPlan = mJsonObject.getString(JsonConstants.GROCERYFOODPLAN);
                    arrayValues = new String[]{planLists, extraFoodPlan, groceryFoodPlan};
                    JSONObject dailyTips = mJsonObject.getJSONObject(JsonConstants.DAILYTIPS);
                    String termsText = dailyTips.getString(JsonConstants.TERMS_CONDITIONS);
                    String aboutText = dailyTips.getString(JsonConstants.APP_ABOUT_US);
                    JSONArray aboutUsArray = dailyTips.getJSONArray(JsonConstants.APP_ABOUT_US_IMAGES);
                    for (int i = 0; i < aboutUsArray.length(); i++) {
                        mHashMap.put(aboutUsArray.get(i).toString(), AppConstants.ABOUT_DIR);

                    }
                    Utility.writeIntoFile(SignInActivity.this, termsText, AppConstants.TERMS_FILENAME);
                    Utility.writeIntoFile(SignInActivity.this, aboutText, AppConstants.ABOUT_FILENAME);

                    File mFile = new File(AppConstants.baseDir + "/" + AppConstants.FORAGER_DIR + "/" + AppConstants.ABOUT_DIR);
                    int fileCount = 0;
                    if (mFile.exists()) {
                        fileCount = mFile.list().length;
                    }

                    if (fileCount != mHashMap.size()) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                            new DownloadImage(SignInActivity.this, mHashMap, AppConstants.ABOUT_IMAGE_CALL).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                        } else {
                            new DownloadImage(SignInActivity.this, mHashMap, AppConstants.ABOUT_IMAGE_CALL).execute();
                        }
                    } else {
                        mHashMap.clear();
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
                            new JsonFileAsync(SignInActivity.this, arrayValues, fileDownloadComplete).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                        else
                            new JsonFileAsync(SignInActivity.this, arrayValues, fileDownloadComplete).execute();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    OnJsonFileDownloadComplete fileDownloadComplete = new OnJsonFileDownloadComplete() {
        @Override
        public void onJsonDownloadComplete(String[] response) {

            Log.e("File Downloaded.....", response.toString());


            for (int i = 0; i < response.length; i++) {
                if (!Utility.isBlank(response[i])) {
                    FileOutputStream outputStream = null;
                    try {
                        outputStream = openFileOutput(array[i], Context.MODE_PRIVATE);
                        outputStream.write(response[i].getBytes());
                        outputStream.close();

                        InputStream inputStream = openFileInput(array[i]);
                        String res = Utility.getStringFromInputStream(inputStream);
                        Log.e("Check File Value....", res);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            Intent intent = new Intent(SignInActivity.this, DashBoardActivity.class);
            startActivity(intent);
            finish();
        }
    };


    private void parseJson(String response) throws JSONException {

        active_folder = "";

        SharedPreferences.Editor Editor = Utility.getSharedPreferences(SignInActivity.this, AppConstants.SHARED_PREFES_USER_DETAILS).edit();
        JSONObject mJsonObject = new JSONObject(response);
        Editor.putString(AppConstants.USER_ID, mJsonObject.getString(JsonConstants.USER_ID));
        AppConstants.APP_USER_ID = mJsonObject.getString(JsonConstants.USER_ID);
        Editor.putString(AppConstants.USER_STATUS, mJsonObject.getString(JsonConstants.USER_STATUS));
        user_status = mJsonObject.getString(JsonConstants.USER_STATUS);
        JSONObject userDetailsJsonObject = mJsonObject.getJSONObject(JsonConstants.USER_DETAILS);
        Editor.putString(AppConstants.USER_NAME, userDetailsJsonObject.getString(JsonConstants.USER_NAME));
        Editor.putString(AppConstants.USER_DOB, userDetailsJsonObject.getString(JsonConstants.USER_DOB));
        Editor.putString(AppConstants.USER_COUNTRY, userDetailsJsonObject.getString(JsonConstants.USER_COUNTRY));
        final String profile_pic_url = userDetailsJsonObject.getString(JsonConstants.USER_PROFILE_PIC);
        System.out.println("checking profile in sign in.............." + profile_pic_url);
        Editor.putString(AppConstants.USER_PROFILE_PIC, profile_pic_url);


        Editor.putString(AppConstants.BODY_UNITS, userDetailsJsonObject.getString(JsonConstants.BODY_UNITS));
        Editor.putString(AppConstants.BODY_AGE, userDetailsJsonObject.getString(JsonConstants.BODY_AGE));
        Editor.putString(AppConstants.BODY_WEIGHT, userDetailsJsonObject.getString(JsonConstants.BODY_WEIGHT));
        Editor.putString(AppConstants.BODY_HEIGHT, userDetailsJsonObject.getString(JsonConstants.BODY_HEIGHT));

        Editor.putString(AppConstants.BODY_GENDER, userDetailsJsonObject.getString(JsonConstants.BODY_GENDER));
        Editor.putString(AppConstants.BODY_ACTIVITY_LEVEL, userDetailsJsonObject.getString(JsonConstants.BODY_ACTIVITY_LEVEL));
        Editor.putString(AppConstants.BODY_CALORIES, userDetailsJsonObject.getString(JsonConstants.BODY_CALORIES));
        Editor.putString(AppConstants.GOAL_TYPE, userDetailsJsonObject.getString(JsonConstants.GOAL_TYPE));
        Editor.putString(AppConstants.GOAL_LIMIT, userDetailsJsonObject.getString(JsonConstants.GOAL_LIMIT));
        Editor.putString(AppConstants.GOAL_CALORIES, userDetailsJsonObject.getString(JsonConstants.GOAL_CALORIES));
        Editor.putString(AppConstants.PLAN_CALORIES, userDetailsJsonObject.getString(JsonConstants.PLAN_CALORIES));

        JSONArray mPlanArray = mJsonObject.getJSONArray(JsonConstants.PLAN);

        for (int i = 0; i < mPlanArray.length(); i++) {
            JSONObject mPlanObject = mPlanArray.getJSONObject(i);
            PlanDetails mPlanDetails = new PlanDetails();
            mPlanDetails.setId(mPlanObject.getString(JsonConstants.ID));
            mPlanDetails.setType(mPlanObject.getString(JsonConstants.PLAN_TYPE));
//            mPlanDetails.setProduct_id(mPlanObject.getString(JsonConstants.PLAN_IMAGE));
            mPlanDetails.setImage(mPlanObject.getString(JsonConstants.PLAN_IMAGE));
            mPlanDetails.setMeal_plan_logo(mPlanObject.getString(JsonConstants.PLAN_MEAL_PLAN_LOGO));
//            mPlanDetails.setPrice(mPlanObject.getString(JsonConstants.PLAN_PRICE));
            mPlanDetails.setDescription(mPlanObject.getString(JsonConstants.PLAN_DESC));
            mPlanDetails.setTips_guidelines(mPlanObject.getString(JsonConstants.PLAN_TIPS_GUIDELINES));
            mPlanDetails.setDaily_supplements(mPlanObject.getString(JsonConstants.PLAN_DAILY_SUPPLEMENTS));
            JSONArray mPlanImagesArray = mPlanObject.getJSONArray(JsonConstants.PLAN_IMAGES);


           /* boolean smallDesc = new File(AppConstants.baseDir+"/"+AppConstants.FORAGER_DIR +"/"+mPlanDetails.getType()+"/"+
                    AppConstants.SHOP_MEAL_PLAN_DIR, AppConstants.SMALL_DESC).exists();

            boolean detailDesc = new File(AppConstants.baseDir+"/"+AppConstants.FORAGER_DIR +"/"+mPlanDetails.getType()+"/"+
                    AppConstants.SHOP_MEAL_PLAN_DIR, AppConstants.DETAIL_DESC).exists();

            if(!smallDesc || !detailDesc) {
                String detDesc = mPlanDetails.getDescription().toString();
                int fIndex = detDesc.indexOf("<p>");
                int f1Index = detDesc.indexOf("</p>");
                String smaDesc = detDesc.substring(fIndex,f1Index);
                Utility.writeToSDFile(detDesc,"/"+mPlanDetails.getType()+"/"+AppConstants.SHOP_MEAL_PLAN_DIR,
                        AppConstants.DETAIL_DESC);

                Utility.writeToSDFile(smaDesc,"/"+mPlanDetails.getType()+"/"+AppConstants.SHOP_MEAL_PLAN_DIR,
                        AppConstants.SMALL_DESC);

            }*/

            File mFile = new File(AppConstants.baseDir + "/" + AppConstants.FORAGER_DIR + "/" + mPlanDetails.getType() + "/" +
                    AppConstants.SHOP_MEAL_PLAN_DIR);

            if (mFile.exists()) {

                int fileCount = mFile.list().length;

                if (fileCount != AppConstants.SHOP_MEAL_PLAN_COUNT) {
                    mHashMap.put(mPlanDetails.getMeal_plan_logo(), mPlanDetails.getType() + "/" + AppConstants.SHOP_MEAL_PLAN_DIR);
                    mHashMap.put(mPlanDetails.getImage(), mPlanDetails.getType() + "/" + AppConstants.SHOP_MEAL_PLAN_LOGO_DIR);
                } /*else {
                    mHashMap.put(mPlanDetails.getMeal_plan_logo(), mPlanDetails.getType() + "/" + AppConstants.SHOP_MEAL_PLAN_DIR);
                }*/
            } else {
                mHashMap.put(mPlanDetails.getMeal_plan_logo(), mPlanDetails.getType() + "/" + AppConstants.SHOP_MEAL_PLAN_DIR);
                mHashMap.put(mPlanDetails.getImage(), mPlanDetails.getType() + "/" + AppConstants.SHOP_MEAL_PLAN_LOGO_DIR);
            }

            //HashMap<Integer, String> mHashMap = new HashMap<>();
            //String[][] mArray = new String[][];
            //mPlanDetails.setPurchased_status(mPlanObject.getInt(JsonConstants.PLAN_PURCHASED_STATUS));
            mPlanDetails.setActive_status(String.valueOf(mPlanObject.getInt(JsonConstants.PLAN_ACTIVE_STATUS)));
            StringBuilder sb = new StringBuilder();
            for (int j = 0; j < mPlanImagesArray.length(); j++) {
                if (Integer.parseInt(mPlanDetails.getActive_status()) == 1) {
                    active_folder = mPlanDetails.getType();
                    mHashMap.put(mPlanImagesArray.get(j).toString(), mPlanDetails.getType());
                }
                sb.append(mPlanImagesArray.get(j));
                if (j + 1 != mPlanImagesArray.length()) {
                    sb.append(",");
                }
            }
            mPlanDetails.setPlan_images(sb.toString());
            mPlanDetailsArrayList.add(mPlanDetails);
        }
        File mFile = new File(AppConstants.baseDir + "/" + AppConstants.FORAGER_DIR + "/" + active_folder);
        SharedPreferences preferences = Utility.getSharedPreferences(SignInActivity.this, AppConstants.ACTIVE_FOLDER);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(AppConstants.ACTIVE_PLAN, active_folder);
        editor.commit();
        if (mFile.exists()) {

            int fileCount = mFile.list().length;

            System.out.println("FileCount" + fileCount);

            if (fileCount == mHashMap.size()) {
                mHashMap.clear();
            } else {
                mFile.delete();
            }
        }



        /*  if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.HONEYCOMB)
                new DownloadImage(SignInActivity.this, hashMap).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            else
                new DownloadImage(SignInActivity.this, hashMap).execute();*/


        if (!profile_pic_url.isEmpty()) {

            //new DownloadImage(SignInActivity.this,true).execute(profile_pic_url);
            mHashMap.put(profile_pic_url, AppConstants.PROFILE_DIR);
        }

        HashMap<String, String> mSortedHashMap = Utility.sortByValues(mHashMap);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)

            new DownloadImage(SignInActivity.this, mSortedHashMap, AppConstants.PROFILE_PLAN_IMAGE_CALL).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        else
            new DownloadImage(SignInActivity.this, mSortedHashMap, AppConstants.PROFILE_PLAN_IMAGE_CALL).execute();

        mDatabase.insertPlanDB(mPlanDetailsArrayList);
        Editor.commit();

    }

    @Override
    public void onDownloadCompleted(String methodName) {

        mHashMap.clear();

        if (methodName.equalsIgnoreCase(AppConstants.PROFILE_PLAN_IMAGE_CALL)) {
            if (user_status.equalsIgnoreCase(AppConstants.USER_ACTIVE)) {
                UserDetails mUserDetails = UserDetails.getInstance();
                try {
                    SharedPreferences preferences = Utility.getSharedPreferences(SignInActivity.this, AppConstants.SHARED_PREFES_USER_DETAILS);
                    File profile = new File(AppConstants.baseDir + "/" + AppConstants.FORAGER_DIR + "/" + AppConstants.PROFILE_DIR + "/" + 1 + ".png");

                    mUserDetails.setUser_name(preferences.getString(AppConstants.USER_NAME, ""));
                    mUserDetails.setBirth_date(preferences.getString(AppConstants.USER_DOB, ""));
                    if (profile.exists()) {
                        mUserDetails.setProf_url(profile.getAbsolutePath());
                        SharedPreferences.Editor Editor = Utility.getSharedPreferences(SignInActivity.this, AppConstants.SHARED_PREFES_USER_DETAILS).edit();
                        Editor.putString(AppConstants.USER_PROFILE_PIC, profile.getAbsolutePath());
                        Editor.commit();
                    }
                    mUserDetails.setCountry(preferences.getString(AppConstants.USER_COUNTRY, ""));
                    mUserDetails.setUnits(preferences.getString(AppConstants.BODY_UNITS, ""));
                    mUserDetails.setAge(preferences.getString(AppConstants.BODY_AGE, ""));
                    mUserDetails.setWeight(preferences.getString(AppConstants.BODY_WEIGHT, ""));
                    mUserDetails.setHeight(preferences.getString(AppConstants.BODY_HEIGHT, ""));
                    mUserDetails.setGender(preferences.getString(AppConstants.BODY_GENDER, ""));
                    mUserDetails.setActivity_level(preferences.getString(AppConstants.BODY_ACTIVITY_LEVEL, ""));
                    mUserDetails.setBody_calories((int) (Math.round(Float.parseFloat(preferences.getString(AppConstants.BODY_CALORIES, "")))));
                    mUserDetails.setGoal_type(preferences.getString(AppConstants.GOAL_TYPE, ""));
                    mUserDetails.setGoal_limit(preferences.getString(AppConstants.GOAL_LIMIT, ""));
                    mUserDetails.setGoal_calories(preferences.getString(AppConstants.GOAL_CALORIES, ""));
                    mUserDetails.setPlan_calorie(preferences.getString(AppConstants.PLAN_CALORIES, ""));
                    //mUserDetails.setGoal_index(preferences.getInt(AppConstants.GOAL_INDEX, -1));
                    String units = mUserDetails.getUnits();

                    String goal_str = mUserDetails.getGoal_type();
                    Calculations.GOAL_ENUM goal_enum = Utility.getGoalEnum(units, goal_str, SignInActivity.this);
                    mUserDetails.setGoal_enum(goal_enum);


                    SharedPreferences.Editor Editor = Utility.getSharedPreferences(SignInActivity.this, AppConstants.SHARED_PREFES_LOGIN_DETAILS).edit();
                    Editor.putBoolean(AppConstants.SIGNED_IN, true);
                    Editor.commit();

                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    try {
                        JSONObject jsonObj = new JSONObject();
                        jsonObj.put(JsonConstants.USER_ID, AppConstants.APP_USER_ID);
                        jsonObj.put(JsonConstants.PLAN_ID, mDatabase.getActivePlanId());
                        jsonObj.put(JsonConstants.CALORIE_TYPE, mUserDetails.getPlan_calorie());

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
                            new JsonAsync(SignInActivity.this, jsonObj.toString(), AppConstants.UPDATE_ACTIVE_CALL).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, AppUrls.ACTIVE_PLAN_URL);
                        else
                            new JsonAsync(SignInActivity.this, jsonObj.toString(), AppConstants.UPDATE_ACTIVE_CALL).execute(AppUrls.ACTIVE_PLAN_URL);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            } else {
                startActivity(new Intent(SignInActivity.this, ProfileActivity.class));
                finish();
            }


        } else if (methodName.equalsIgnoreCase(AppConstants.ABOUT_IMAGE_CALL)) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
                new JsonFileAsync(SignInActivity.this, arrayValues, fileDownloadComplete).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            else
                new JsonFileAsync(SignInActivity.this, arrayValues, fileDownloadComplete).execute();
        }

    }
}
