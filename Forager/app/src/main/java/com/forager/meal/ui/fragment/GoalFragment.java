package com.forager.meal.ui.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.forager.meal.application.MyApplication;
import com.forager.meal.async.DownloadImage;
import com.forager.meal.async.JsonAsync;
import com.forager.meal.async.JsonFileAsync;
import com.forager.meal.bean.UserDetails;
import com.forager.meal.constants.AppConstants;
import com.forager.meal.constants.AppUrls;
import com.forager.meal.constants.JsonConstants;
import com.forager.meal.database.Database;
import com.forager.meal.listener.OnImageDownloadCompleted;
import com.forager.meal.listener.OnJsonFileDownloadComplete;
import com.forager.meal.listener.OnWebServiceResponse;
import com.forager.meal.ui.R;
import com.forager.meal.ui.activity.DashBoardActivity;
import com.forager.meal.utitlity.Calculations;
import com.forager.meal.utitlity.Utility;
import com.peaksware.wheel.OnWheelScrollListener;
import com.peaksware.wheel.WheelView;
import com.peaksware.wheel.adapter.ArrayWheelAdapter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;


public class GoalFragment extends Fragment implements View.OnClickListener, View.OnFocusChangeListener, OnWebServiceResponse, OnImageDownloadCompleted {


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
    private Button mUpdateBtn;

    SharedPreferences preferences;

    private ArrayList<String> goal_array = new ArrayList<>();

    private ArrayList<String> goal_array_values = new ArrayList<>();

    UserDetails mUserDetails;

    String units;

    int goal_index;

    float daily_calc;

    String goal_calo_str;

    private static final String MAINTAIN_WEIGHT_AGGRESSIVE_GOAL_LIMIT = "";

    private static final String LB_1_GOAL_LIMIT = "1 lb";

    private static final String LB_2_GOAL_LIMIT = "2 lb";

    private static final String KG_45_GOAL_LIMIT = ".45 kg";

    private static final String KG_90_GOAL_LIMIT = ".90 kg";

    private TextView titleGoal;

    Database mDatabase;

    private String[] arrayValues;

    HashMap<String, String> mHashMap = new HashMap<>();

    FragmentManager fm;
    FragmentTransaction fragmentTransaction;

    private String[] array = {AppConstants.PLANLISTS_FILENAME, AppConstants.EXTRAFOODPLAN, AppConstants.GROCERYFOODPLAN};

    public static boolean updatecheck = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        PlanControllerFragment.flag = false;
        View rootView = inflater.inflate(R.layout.fragment_goal, container, false);
        context = getContext();
        mDatabase = Database.getInstance(getActivity());
        mDatabase.getMyWritableDatabase();
        mUserDetails = UserDetails.getInstance();
        units = mUserDetails.getUnits();
        if (units.equals(AppConstants.UNIT_METRIC)) {
            goal_array = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.choose_goal_array_metric)));
            goal_array_values = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.goal_array_metric_value)));

        } else if (units.equals(AppConstants.UNIT_USSYSTEM)) {
            goal_array = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.choose_goal_array_ussystem)));
            goal_array_values = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.goal_array_ussystem_value)));
        }


        init(rootView);
        return rootView;
    }

    private void init(View rootView) {

        titleGoal = (TextView) rootView.findViewById(R.id.title_goal);
        MyApplication.setContext(getActivity());
        titleGoal.setTypeface(MyApplication.getFont());

        goal_edit = (EditText) rootView.findViewById(R.id.edit_goal);
        goal_edit.setKeyListener(null);
        goal_edit.setOnClickListener(this);

        calo_per_day = (TextView) rootView.findViewById(R.id.calo_per_day);
        maintain_weight = (TextView) rootView.findViewById(R.id.maintain_weight);
        reach_goal = (TextView) rootView.findViewById(R.id.reach_goal);

        mGoalWheel = (WheelView) rootView.findViewById(R.id.goal_wheel);
        mGoalAdapter = new ArrayWheelAdapter(context, goal_array.toArray());
        mGoalAdapter.setIndex(mGoalWheel.getCurrentItem());
        mGoalWheel.setViewAdapter(mGoalAdapter);
        mGoalWheel.setCyclic(false);
        mGoalWheel.addScrollingListener(mOnWheelScrollListener);

        mBack = (ImageButton) rootView.findViewById(R.id.back_goal);
        mBack.setOnClickListener(this);

        mUpdateBtn = (Button) rootView.findViewById(R.id.update_goal_btn);
        mUpdateBtn.setOnClickListener(this);

        mPickerLayout = (LinearLayout) rootView.findViewById(R.id.picker_layout);
        mPicker_Close = (ImageButton) rootView.findViewById(R.id.picker_close);
        mPicker_Select = (ImageButton) rootView.findViewById(R.id.picker_select);

        mPicker_Close.setOnClickListener(this);
        mPicker_Select.setOnClickListener(this);

        fillDatasFromPrefes();
    }

    private void fillDatasFromPrefes() {

        try {

            preferences = Utility.getSharedPreferences(getActivity(), AppConstants.SHARED_PREFES_USER_DETAILS);

            if (!Utility.isBlank(preferences.getString(AppConstants.GOAL_TYPE, ""))) {


                String goal_str = preferences.getString(AppConstants.GOAL_TYPE, "");

              /*  goal_index=preferences.getInt(AppConstants.GOAL_INDEX, -1);
                System.out.println("Hi cat pref......................"+goal_index);*/

                goal_edit.setText(goal_str);


                if (goal_array.contains(goal_str)) {
                    goal_index = goal_array.indexOf(goal_str);
                } else {
                    if (units.equals(AppConstants.UNIT_METRIC)) {
                        goal_index = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.choose_goal_array_ussystem))).indexOf(goal_str);
                    } else {
                        goal_index = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.choose_goal_array_metric))).indexOf(goal_str);
                    }
                }

                //goal_index = goal_array.indexOf(goal_str);

                mGoalWheel.setCurrentItem(goal_index, false);

                mGoalAdapter.setIndex(mGoalWheel.getCurrentItem());
                if (goal_index == 0) {
                    reach_goal.setVisibility(View.GONE);
                } else {
                    reach_goal.setVisibility(View.VISIBLE);
                }
            }

            if (!Utility.isBlank(preferences.getString(AppConstants.GOAL_CALORIES, ""))) {
                daily_calc = Float.parseFloat(preferences.getString(AppConstants.GOAL_CALORIES, ""));
                goal_calo_str = String.valueOf(daily_calc);
                calo_per_day.setText(getCalPerDay());

                maintain_weight.setText(goal_array_values.get(goal_index));
            } else if (mUserDetails.getBody_calories() > 0.0d) {
                daily_calc = (float) mUserDetails.getBody_calories();
                goal_calo_str = String.valueOf(daily_calc);
                calo_per_day.setText(getCalPerDay());
            }

            if (!goal_edit.getText().toString().equalsIgnoreCase(getResources().getString(R.string.choose_goal_hint)) && (goal_index >= 0)) {
                maintain_weight.setText(goal_array_values.get(goal_index));
            }

            storeViewValue();

        } catch (Exception e) {
            e.printStackTrace();
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

            case R.id.update_goal_btn:
                validate_Fields();
                break;

            case R.id.back_goal:
                callBack();
                break;
        }
    }

    private void callBack() {

        Intent intentProf = new Intent(getActivity(), DashBoardActivity.class);
        getActivity().overridePendingTransition(0, 0);
        intentProf.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        intentProf.putExtra("dashboard", "yourprofilefragment");
        getActivity().finish();
        getActivity().overridePendingTransition(0, 0);
        getActivity().startActivity(intentProf);
    }

    private boolean isGoalModified() {

        SharedPreferences preferences = Utility.getSharedPreferences(getActivity(), AppConstants.SHARED_PREFES_USER_DETAILS);
        if (!preferences.getString(AppConstants.GOAL_TYPE, "").equalsIgnoreCase(mUserDetails.getGoal_type()) ||
                !preferences.getString(AppConstants.GOAL_LIMIT, "").equalsIgnoreCase(mUserDetails.getGoal_limit())) {
            return true;
        }
        return false;
    }


    private void validate_Fields() {
        if (goal_edit.getText().toString().equals(getResources().getString(R.string.choose_goal_hint))) {
            Utility.showAlert(getActivity(), AppConstants.ENTER_GOAL, false);
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
            // new PostAsync(getActivity(), mUserDetails).execute(AppUrls.PROFILE_URL);

            if (isGoalModified()) {
//                boolean status1 = false;
                if (DashBoardActivity.mTitleList != null) {
                    if (!isMarkEatenPrefesAvailable(DashBoardActivity.mTitleList, AppConstants.STATUS)) {
                        changeActivePlan();
                    } else {
                        AlertDialog.Builder mBuilder = new AlertDialog.Builder(getActivity(), R.style.CustomAlertDialogStyle);
                        mBuilder
                                .setMessage(getActivity().getResources().getString(R.string.alert_message_goal))
                                .setCancelable(false)
                                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        changeActivePlan();

                                    }
                                });

                        AlertDialog mErrorDialog = mBuilder.create();
                        mErrorDialog.show();
                        mErrorDialog.setCancelable(true);
                        mErrorDialog.setCanceledOnTouchOutside(true);
                    }
                }
/*
                if (isMarkEatenPrefesAvailable(AppConstants.MARK_EATEN_STATUS)) {
                    SharedPreferences preferences1 = Utility.getSharedPreferences(getActivity(), AppConstants.SHARED_PREFES_MARK_EATEN);
                    boolean status = preferences1.getBoolean(AppConstants.MARK_EATEN_STATUS, false);
                    if (status) {


                    }

                } else {
                    changeActivePlan();
                }*/
            }
            YourProfileFragment.updateCheck = true;

            fm = getFragmentManager();
            fragmentTransaction = fm.beginTransaction();
            fragmentTransaction.add(R.id.fragment_container, new YourProfileFragment());
            fragmentTransaction.commit();
        }
    }


    private boolean isMarkEatenPrefesAvailable(String mealType) {
        SharedPreferences preferences = Utility.getSharedPreferences(getActivity(), AppConstants.SHARED_PREFES_MARK_EATEN);
        String planListKey = mealType;
        if (preferences.contains(planListKey)) {
            return true;
        }
        return false;
    }

    private boolean isMarkEatenPrefesAvailable(ArrayList<String> titleList, String status) {
        SharedPreferences preferences = Utility.getSharedPreferences(getActivity(), AppConstants.SHARED_PREFES_MARK_EATEN_DETAILS);
        for (String str : titleList) {
            String key = str + "," + status;
            if (preferences.contains(key) &&
                    preferences.getBoolean(key, false)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {
            mPickerLayout.setVisibility(View.VISIBLE);
        } else {
            mPickerLayout.setVisibility(View.GONE);
        }
    }


    public void changeActivePlan() {
        Utility.resetFood(getActivity());
       /* plan_id = ShopMealPlannerAdapter.shopMealPlan.getId();
        mDatabase.changeActivePlanId(plan_id);*/

        UserDetails mUserDetails = UserDetails.getInstance();
        try {
            JSONObject jsonObj = new JSONObject();
            SharedPreferences preferences = Utility.getSharedPreferences(getActivity(), AppConstants.SHARED_PREFES_USER_DETAILS);
            if (preferences.contains(JsonConstants.USER_ID)) {
                jsonObj.put(JsonConstants.USER_ID, preferences.getString(JsonConstants.USER_ID, ""));
            }
            jsonObj.put(JsonConstants.PLAN_ID, mDatabase.getActivePlanId());
            jsonObj.put(JsonConstants.CALORIE_TYPE, mUserDetails.getPlan_calorie());

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
                new JsonAsync(GoalFragment.this, jsonObj.toString(), AppConstants.UPDATE_ACTIVE_CALL).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, AppUrls.ACTIVE_PLAN_URL);
            else
                new JsonAsync(GoalFragment.this, jsonObj.toString(), AppConstants.UPDATE_ACTIVE_CALL).execute(AppUrls.ACTIVE_PLAN_URL);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onResponseReceived(String response, String methodName) {

        System.out.println("Response " + response);
        try {
            JSONObject mJsonObject = new JSONObject(response);

        /*    if (mJsonObject.has("Message") && mJsonObject.getString("Message").contains("No Record")) {
                Utility.dismissPDialog(getActivity());
                Utility.showAlert(getActivity(), "Member Details Error", false);
                return;
            }*/
            String planLists = mJsonObject.getString(JsonConstants.PLANLISTS);
            String extraFoodPlan = mJsonObject.getString(JsonConstants.EXTRAFOODPLAN);
            String groceryFoodPlan = mJsonObject.getString(JsonConstants.GROCERYFOODPLAN);
            arrayValues = new String[]{planLists, extraFoodPlan, groceryFoodPlan};
            JSONObject dailyTips = mJsonObject.getJSONObject(JsonConstants.DAILYTIPS);
            String termsText = dailyTips.getString(JsonConstants.TERMS_CONDITIONS);
            String aboutText = dailyTips.getString(JsonConstants.APP_ABOUT_US);
            JSONArray aboutUsArray = dailyTips.getJSONArray(JsonConstants.APP_ABOUT_US_IMAGES);
            mHashMap.clear();
            for (int i = 0; i < aboutUsArray.length(); i++) {
                mHashMap.put(aboutUsArray.get(i).toString(), AppConstants.ABOUT_DIR);
            }
            Utility.writeIntoFile(getActivity(), termsText, AppConstants.TERMS_FILENAME);
            Utility.writeIntoFile(getActivity(), aboutText, AppConstants.ABOUT_FILENAME);

            File mFile = new File(AppConstants.baseDir + "/" + AppConstants.FORAGER_DIR + "/" + AppConstants.ABOUT_DIR);
            mFile.delete();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                new DownloadImage(GoalFragment.this, mHashMap, AppConstants.ABOUT_IMAGE_CALL).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            } else {
                new DownloadImage(GoalFragment.this, mHashMap, AppConstants.ABOUT_IMAGE_CALL).execute();
            }
           /* int fileCount = 0;
            if (mFile.exists()) {
                fileCount = mFile.list().length;
            }

            if (fileCount != mHashMap.size()) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                    new DownloadImage(getActivity(), mHashMap, AppConstants.ABOUT_IMAGE_CALL).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                } else {
                    new DownloadImage(getActivity(), mHashMap, AppConstants.ABOUT_IMAGE_CALL).execute();
                }
            } else {
                mHashMap.clear();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
                    new JsonFileAsync(getActivity(), arrayValues, fileDownloadComplete).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                else
                    new JsonFileAsync(getActivity(), arrayValues, fileDownloadComplete).execute();
            }*/
        } catch (Exception e) {
            e.printStackTrace();
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
                        outputStream = getActivity().openFileOutput(array[i], Context.MODE_PRIVATE);
                        outputStream.write(response[i].getBytes());
                        outputStream.close();

                        InputStream inputStream = getActivity().openFileInput(array[i]);
                        String res = Utility.getStringFromInputStream(inputStream);
                        Log.e("Check File Value....", res);
                        Utility.dismissPDialog(getActivity());
                    } catch (Exception e) {
                        e.printStackTrace();

                    }
                }
            }
            callBack();

        }
    };

    @Override
    public void onDownloadCompleted(String methodName) {
        if (methodName.equalsIgnoreCase(AppConstants.ABOUT_IMAGE_CALL)) {
            String urls = mDatabase.getActivePlanImageUrls();
            Log.e("URLS", " = " + urls);
            String activeFolder = mDatabase.getActivePlanType();
            String[] str = urls.split(",");
            mHashMap.clear();
            for (int i = 0; i < str.length; i++) {
                mHashMap.put(str[i], activeFolder);
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
                new DownloadImage(GoalFragment.this, mHashMap, AppConstants.PROFILE_PLAN_IMAGE_CALL).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            else
                new DownloadImage(GoalFragment.this, mHashMap, AppConstants.PROFILE_PLAN_IMAGE_CALL).execute();

        } else if (methodName.equalsIgnoreCase(AppConstants.PROFILE_PLAN_IMAGE_CALL)) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
                new JsonFileAsync(getActivity(), arrayValues, fileDownloadComplete).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            else
                new JsonFileAsync(getActivity(), arrayValues, fileDownloadComplete).execute();
        }
    }
}