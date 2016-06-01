package com.forager.meal.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.forager.meal.application.MyApplication;
import com.forager.meal.async.JsonFileAsync;
import com.forager.meal.async.PostAsync;
import com.forager.meal.bean.UserDetails;
import com.forager.meal.constants.AppConstants;
import com.forager.meal.constants.AppUrls;
import com.forager.meal.constants.JsonConstants;
import com.forager.meal.listener.OnJsonFileDownloadComplete;
import com.forager.meal.listener.OnWebServiceResponse;
import com.forager.meal.ui.R;
import com.forager.meal.utitlity.Calculations;
import com.forager.meal.utitlity.Utility;
import com.peaksware.wheel.OnWheelScrollListener;
import com.peaksware.wheel.WheelView;
import com.peaksware.wheel.adapter.ArrayWheelAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;

public class GoalActivity extends Activity implements View.OnClickListener, View.OnFocusChangeListener, OnWebServiceResponse {

    private WheelView mGoalWheel;

    private ArrayWheelAdapter mGoalAdapter;

    private Context context;

    private EditText goal_edit;

    private TextView calo_per_day;

    private TextView maintain_weight;

    private TextView reach_goal;

    private LinearLayout mPickerLayout;

    private ImageButton mPicker_Close;

    private ImageButton mPicker_Select;

    private ImageButton mBack;

    private Button mCreateBtn;

    int i;

    SharedPreferences preferences;

    public static ArrayList<String> goal_array = new ArrayList<>();

    private ArrayList<String> goal_array_values = new ArrayList<>();

    UserDetails mUserDetails;

    String units;

    int goal_index;

    float daily_calc;

    String goal_calo_str;

    TextView txtTiltleGoal;

    public String[] arrayValues;

    private static final String MAINTAIN_WEIGHT_AGGRESSIVE_GOAL_LIMIT = "";

    private static final String LB_1_GOAL_LIMIT = "1 lb";

    private static final String LB_2_GOAL_LIMIT = "2 lb";

    private static final String KG_45_GOAL_LIMIT = ".45 kg";

    private static final String KG_90_GOAL_LIMIT = ".90 kg";

//    HashMap<String, String> mHashMap = new HashMap<>();

    String[] array = {AppConstants.PLANLISTS_FILENAME, AppConstants.EXTRAFOODPLAN, AppConstants.GROCERYFOODPLAN};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goal);
        context = GoalActivity.this;

        mUserDetails = UserDetails.getInstance();
        units = mUserDetails.getUnits();
        if (units.equals(AppConstants.UNIT_METRIC)) {
            goal_array = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.choose_goal_array_metric)));
            goal_array_values = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.goal_array_metric_value)));
        } else if (units.equals(AppConstants.UNIT_USSYSTEM)) {
            goal_array = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.choose_goal_array_ussystem)));
            goal_array_values = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.goal_array_ussystem_value)));
        }
        init();
    }

    private void init() {
        MyApplication.setContext(GoalActivity.this);
        txtTiltleGoal = (TextView) findViewById(R.id.title_goal);
        txtTiltleGoal.setTypeface(MyApplication.getFont());


        goal_edit = (EditText) findViewById(R.id.edit_goal);
        goal_edit.setKeyListener(null);
        goal_edit.setOnClickListener(this);

        calo_per_day = (TextView) findViewById(R.id.calo_per_day);
        maintain_weight = (TextView) findViewById(R.id.maintain_weight);
        reach_goal = (TextView) findViewById(R.id.reach_goal);

        mGoalWheel = (WheelView) findViewById(R.id.goal_wheel);
        mGoalAdapter = new ArrayWheelAdapter(context, goal_array.toArray());
        mGoalWheel.setViewAdapter(mGoalAdapter);
        mGoalWheel.setCyclic(false);
        mGoalAdapter.setIndex(mGoalWheel.getCurrentItem());
        mGoalWheel.addScrollingListener(mOnWheelScrollListener);

        mBack = (ImageButton) findViewById(R.id.back);
        mBack.setOnClickListener(this);

        mCreateBtn = (Button) findViewById(R.id.create_goal_btn);
        mCreateBtn.setOnClickListener(this);

        mPickerLayout = (LinearLayout) findViewById(R.id.picker_layout);
        mPicker_Close = (ImageButton) findViewById(R.id.picker_close);
        mPicker_Select = (ImageButton) findViewById(R.id.picker_select);

        mPicker_Close.setOnClickListener(this);
        mPicker_Select.setOnClickListener(this);

        fillDatasFromPrefes();
    }

    private void fillDatasFromPrefes() {
        preferences = Utility.getSharedPreferences(GoalActivity.this, AppConstants.SHARED_PREFES_USER_DETAILS);

        if (!Utility.isBlank(preferences.getString(AppConstants.GOAL_TYPE, ""))) {
            String goal_str = preferences.getString(AppConstants.GOAL_TYPE, "");
            goal_edit.setText(goal_str);
            //goal_index = goal_array.indexOf(goal_str);
            if (goal_array.contains(goal_str)) {
                goal_index = goal_array.indexOf(goal_str);
            } else {
                if (units.equals(AppConstants.UNIT_METRIC)) {
                    goal_index = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.choose_goal_array_ussystem))).indexOf(goal_str);
                } else {
                    goal_index = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.choose_goal_array_metric))).indexOf(goal_str);
                }
            }
            System.out.println("Hi cat pref......................"+goal_index);
            mGoalWheel.setCurrentItem(goal_index, false);
            mGoalAdapter.setIndex(mGoalWheel.getCurrentItem());
            if (goal_index == 0) {
                reach_goal.setVisibility(View.GONE);
            } else {
                reach_goal.setVisibility(View.VISIBLE);
            }
        }
        if (mUserDetails.getBody_calories() > 0.0d) {
            daily_calc = (float) mUserDetails.getBody_calories();
            goal_calo_str = String.valueOf(daily_calc);
            calo_per_day.setText(getCalPerDay());
        } else if (!Utility.isBlank(preferences.getString(AppConstants.GOAL_CALORIES, ""))) {
            daily_calc = Float.parseFloat(preferences.getString(AppConstants.GOAL_CALORIES, ""));
            goal_calo_str = String.valueOf(daily_calc);
            calo_per_day.setText(getCalPerDay());

        }
        if(!goal_edit.getText().toString().equalsIgnoreCase(getResources().getString(R.string.choose_goal_hint))&&
                (goal_index>=0)) {
            maintain_weight.setText(goal_array_values.get(goal_index));
            storeViewValue();
        }


    }

    private String getCalPerDay() {
        return Html.fromHtml("<u><b>" + goal_calo_str + " calories </b>per day</u>").toString();
    }

    OnWheelScrollListener mOnWheelScrollListener = new OnWheelScrollListener() {
        @Override
        public void onScrollingStarted(WheelView wheel) {

        }

        @Override
        public void onScrollingFinished(WheelView wheel) {

            if (wheel == mGoalWheel) {
                mGoalAdapter.setIndex(wheel.getCurrentItem());
            }

        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_goal, menu);
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


    private void storeViewValue() {

        goal_index = mGoalWheel.getCurrentItem();

        goal_edit.setText(mGoalAdapter.getItemText(goal_index));

        Calculations.GOAL_ENUM goalEnum = null;

        switch (goal_index) {
            case 0:
                goalEnum = Calculations.GOAL_ENUM.MAINTAIN_WEIGHT;
                break;
            case 1:
                goalEnum = Calculations.GOAL_ENUM.LOSE_45_1;
                break;
            case 2:
                goalEnum = Calculations.GOAL_ENUM.LOSE_90_2;
                break;
            case 3:
                goalEnum = Calculations.GOAL_ENUM.GAIN_45_1;
                break;
            case 4:
                goalEnum = Calculations.GOAL_ENUM.GAIN_90_2;
                break;
            case 5:
                goalEnum = Calculations.GOAL_ENUM.AGGRESSIVE_MASS_GAIN;
                break;
            default:
                break;
        }
        mUserDetails.setGoal_enum(goalEnum);

        goal_calo_str = Utility.calcBMI();
        calo_per_day.setText(getCalPerDay());


        maintain_weight.setText(goal_array_values.get(goal_index));

        if (goal_index == 0) {
            reach_goal.setVisibility(View.GONE);
        } else {
            reach_goal.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.picker_close:
                mPickerLayout.setVisibility(View.GONE);
                break;
            case R.id.picker_select:
                storeViewValue();
                mPickerLayout.setVisibility(View.GONE);
                break;
            case R.id.edit_goal:
                mGoalWheel.setCurrentItem(goal_index, false);
                mGoalAdapter.setIndex(mGoalWheel.getCurrentItem());
                mPickerLayout.setVisibility(View.VISIBLE);
                break;
            case R.id.create_goal_btn:
                validate_Fields();
                break;
            case R.id.back:
                finish();
                break;
        }
    }

    private void validate_Fields() {
        if (goal_edit.getText().toString().equals(getResources().getString(R.string.choose_goal_hint))) {
            Utility.showAlert(GoalActivity.this, AppConstants.ENTER_GOAL, false);
        } else {
            mUserDetails.setGoal_type(goal_edit.getText().toString());
            mUserDetails.setGoal_calories(goal_calo_str);
//            mUserDetails.setGoal_index(goal_index);

            String goal_limit_str = "";
            switch (goal_index) {
                case 0:
                case 5:
                    goal_limit_str = MAINTAIN_WEIGHT_AGGRESSIVE_GOAL_LIMIT;
                    break;
                case 1:
                case 3:
                    if (units.equals(AppConstants.UNIT_METRIC)) {
                        goal_limit_str = KG_45_GOAL_LIMIT;
                    } else if (units.equals(AppConstants.UNIT_USSYSTEM)) {
                        goal_limit_str = LB_1_GOAL_LIMIT;
                    }
                    break;
                case 2:
                case 4:
                    if (units.equals(AppConstants.UNIT_METRIC)) {
                        goal_limit_str = KG_90_GOAL_LIMIT;
                    } else if (units.equals(AppConstants.UNIT_USSYSTEM)) {
                        goal_limit_str = LB_2_GOAL_LIMIT;
                    }
                    break;
                default:
                    break;

            }
            mUserDetails.setGoal_limit(goal_limit_str);
            mUserDetails.setPlan_calorie(String.valueOf(
                    Calculations.calcPlanClarie(Float.parseFloat(goal_calo_str))));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
                new PostAsync(GoalActivity.this, mUserDetails ,AppConstants.PROFILE_CALL ).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, AppUrls.PROFILE_URL);
            else
                new PostAsync(GoalActivity.this, mUserDetails ,AppConstants.PROFILE_CALL).execute(AppUrls.PROFILE_URL);
        }
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {
            mPickerLayout.setVisibility(View.VISIBLE);
        } else {
            mPickerLayout.setVisibility(View.GONE);
        }
    }

    OnJsonFileDownloadComplete fileDownloadComplete = new OnJsonFileDownloadComplete() {
        @Override
        public void onJsonDownloadComplete(String[] response) {

            Log.e("File Downloaded.....", response.toString());

            for (int i = 0; i < response.length; i++) {
                if (!Utility.isBlank(response[i])) {
                    FileOutputStream outputStream = null;
                    // for(i=0;i<array.length;i++) {
                    try {
                        outputStream = openFileOutput(array[i], Context.MODE_PRIVATE);
                        outputStream.write(response[i].getBytes());
                        outputStream.close();

                        InputStream inputStream = openFileInput(array[i]);
                        String res = Utility.getStringFromInputStream(inputStream);
                        Log.e("Check File Value....", res);

                    } catch (Exception e) {

                        e.printStackTrace();

                        //}
                    }
                }

            }

            Utility.dismissPDialog(GoalActivity.this);
            Intent intent = new Intent(GoalActivity.this, TermsAndConditions.class);
            startActivity(intent);
        }
    };



    public void callPhotoUploadMethod(String imagePath1) throws FileNotFoundException, IOException{
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(imagePath1, o);
        // The new size we want to scale to
        final int REQUIRED_SIZE = 1024;
        // Find the correct scale value. It should be the power of 2.
        int width_tmp = o.outWidth, height_tmp = o.outHeight;
        int scale = 1;
        while (true) {
            if (width_tmp < REQUIRED_SIZE && height_tmp < REQUIRED_SIZE)
                break;
            width_tmp /= 2;
            height_tmp /= 2;
            scale *= 2;
        }
        // Decode with inSampleSize
        BitmapFactory.Options o2 = new BitmapFactory.Options();
        o2.inSampleSize = scale;
        Bitmap bmp = BitmapFactory.decodeFile(imagePath1, o2);

        File file = new File(AppConstants.baseDir, AppConstants.FORAGER_DIR + "/" + AppConstants.PROFILE_DIR + "/");

        if (!file.exists()) {
            file.mkdirs();
        }

        OutputStream output = new FileOutputStream(file + "/" + String.valueOf(1) + ".png");

        bmp.compress(Bitmap.CompressFormat.PNG, 85, output);
        output.flush();
        output.close();


    }

    @Override
    public void onResponseReceived(String response, String methodName) {
        if (!Utility.isBlank(response)) {
            SharedPreferences.Editor Editor = Utility.getSharedPreferences(GoalActivity.this, AppConstants.SHARED_PREFES_USER_DETAILS).edit();

            Editor.putString(AppConstants.USER_NAME, mUserDetails.getUser_name());
            Editor.putString(AppConstants.USER_DOB, mUserDetails.getBirth_date());
            Editor.putString(AppConstants.USER_COUNTRY, mUserDetails.getCountry());

            File profile = new File(AppConstants.baseDir + "/" + AppConstants.FORAGER_DIR +"/"+AppConstants.PROFILE_DIR + "/" + 1 + ".png");

            if(!profile.getAbsolutePath().equalsIgnoreCase(mUserDetails.getProf_url())) {
                try {
                    callPhotoUploadMethod(mUserDetails.getProf_url());
                    File file_temp = new File(mUserDetails.getProf_url());
                    if(file_temp.getAbsolutePath().contains("1_temp.png") && file_temp.exists()) {
                        file_temp.delete();
                    }
                    profile = new File(AppConstants.baseDir + "/" + AppConstants.FORAGER_DIR +"/"+AppConstants.PROFILE_DIR + "/" + 1 + ".png");
                    mUserDetails.setProf_url(profile.getAbsolutePath());
                } catch (FileNotFoundException e ) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            Editor.putString(AppConstants.USER_PROFILE_PIC, mUserDetails.getProf_url());
            Editor.putString(AppConstants.BODY_UNITS, mUserDetails.getUnits());
            Editor.putString(AppConstants.BODY_AGE, mUserDetails.getAge());
            Editor.putString(AppConstants.BODY_WEIGHT, mUserDetails.getWeight());
            Editor.putString(AppConstants.BODY_HEIGHT, mUserDetails.getHeight());

            Editor.putString(AppConstants.BODY_GENDER, mUserDetails.getGender());
            Editor.putString(AppConstants.BODY_ACTIVITY_LEVEL, mUserDetails.getActivity_level());
            Editor.putString(AppConstants.BODY_CALORIES, String.valueOf(mUserDetails.getBody_calories()));
            Editor.putString(AppConstants.GOAL_TYPE, mUserDetails.getGoal_type());
            Editor.putString(AppConstants.GOAL_LIMIT, mUserDetails.getGoal_limit());
            Editor.putString(AppConstants.GOAL_CALORIES, mUserDetails.getGoal_calories());
            Editor.putString(AppConstants.PLAN_CALORIES, mUserDetails.getPlan_calorie());
//            Editor.putInt(AppConstants.GOAL_INDEX, mUserDetails.getGoal_index());
            Editor.commit();

            SharedPreferences.Editor LoginEditor = Utility.getSharedPreferences(GoalActivity.this, AppConstants.SHARED_PREFES_LOGIN_DETAILS).edit();
            LoginEditor.putBoolean(AppConstants.SIGNED_IN, true);
            LoginEditor.commit();

            Log.e("Check Response", response);
            try {
                JSONObject mJsonObject = new JSONObject(response);
                String errorCode = mJsonObject.getString(JsonConstants.ERROR_CODE);
                if (errorCode.equals(AppConstants.ERROR_CODE_SUCCESS)) {
                    String planLists = mJsonObject.getString(JsonConstants.PLANLISTS);
                    String extraFoodPlan = mJsonObject.getString(JsonConstants.EXTRAFOODPLAN);
                    String groceryFoodPlan = mJsonObject.getString(JsonConstants.GROCERYFOODPLAN);
                    arrayValues = new String[]{planLists, extraFoodPlan, groceryFoodPlan};

                  /*  for(i=0;i<arrayValues.length;i++) {
                        mHashMap.put(arrayValues[i].toString(), "url");
                    }*/
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
                        new JsonFileAsync(GoalActivity.this, arrayValues, fileDownloadComplete).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                    else
                        new JsonFileAsync(GoalActivity.this, arrayValues, fileDownloadComplete).execute();

                } else if (errorCode.equals(AppConstants.ERROR_CODE_FAILURE)) {
                    Utility.showAlert(GoalActivity.this, AppConstants.PROFILE_UPDATE_FAILED, false);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
