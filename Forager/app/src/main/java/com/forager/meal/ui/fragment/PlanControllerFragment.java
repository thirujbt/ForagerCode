package com.forager.meal.ui.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.forager.meal.adapter.TitleListAdapter;
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
import com.forager.meal.service.BackgroundService;
import com.forager.meal.ui.R;
import com.forager.meal.ui.activity.DashBoardActivity;
import com.forager.meal.ui.activity.IntroActivity;
import com.forager.meal.ui.activity.PieChart;
import com.forager.meal.utitlity.Utility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;


public class PlanControllerFragment extends Fragment implements View.OnClickListener, OnWebServiceResponse, OnImageDownloadCompleted {

    //  private final Context mContext;
    public static boolean flag = false;


    ArrayList<String> store = new ArrayList<>();

    TextView mDateTxt;

    ShoppingFragment shoppingListFragment;

    ImageView mImage;

    TextView mShoppingList;

    TextView mSupplements;

    TextView mGuidelines;

    LinearLayout mChart_layout;

    int left, top, right, bottom, circleLeft, circleTop, circleRight, circleBottom;

    Timer t = new Timer();

    Handler handler = new Handler();

    public static boolean status = false;

    Boolean breakFastChartStatus;

    Boolean planController_ChartStatus;

    public static String mealType;

    private ListView mTitleList;

    private LinearLayout linear;

    private TextView calories;

    private TextView caloriesValues;

    private TextView carbs;

    private TextView carbsValue;

    private TextView carbsPercentage;

    private TextView fats;

    private TextView fatsValue;

    private TextView fatsPercentage;

    private TextView protein;

    private TextView proteinValue;

    private TextView proteinPercentage;

    private TextView fiber;

    private TextView fiberValue;

    public static float carbsValTotal = 0;

    public ImageView planLogo;

    public HashMap<String, Float> mProdValuesTotal = new HashMap<>();

    public static final String KEY_CARBSVAL_TOTAL = "carbsVal_total";

    public static final String KEY_CALORIESVAL_TOTAL = "caloriesVal_total";

    public static final String KEY_FIBERVAL_TOTAL = "fiberVal_total";

    public static final String KEY_FATVAL_TOTAL = "fatVal_total";

    public static final String KEY_PROTEINVAL_TOTAL = "proteinVal_total";

    public static final String KEY_CARBSVAL = "carbsVal";

    public static final String KEY_CALORIESVAL = "caloriesVal";

    public static final String KEY_FIBERVAL = "fiberVal";

    public static final String KEY_FATVAL = "fatVal";

    public static final String KEY_PROTEINVAL = "proteinVal";

    LinearLayout info, mChart;

    Database mDatabase;

    String value;

    String active_plan;

    TimerTask timerTask;

    int fileCount;

    float calTotal, carbsTotal, fatsTotal, proteinTotal, fiberTotal;

    public float calVal, carbsVal, fatsVal, proteinVal, fiberVal;

    public float carbsPer, fatsPer, proteinPer;

    float values[] = {43.9f, 0.5f, 27.3f, 0.5f, 29.0f, 0.2f};

    static int count = 1;

    SharedPreferences pref1;

    protected FragmentActivity mActivity;
    public static int plan_id;
    HashMap<String, String> mHashMap = new HashMap<>();
    private String[] arrayValues;
    private String[] array = {AppConstants.PLANLISTS_FILENAME, AppConstants.EXTRAFOODPLAN, AppConstants.GROCERYFOODPLAN};


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = (FragmentActivity) activity;
    }

    private Timer mTimer;
    TimerTask timerTask1 = new TimerTask() {
        public void run() {

            DateFormat df = new SimpleDateFormat("hh:mm a");
            String date = df.format(Calendar.getInstance().getTime());

           /* Date todayDate = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yy");
            mDateTxt.setText(String.valueOf(sdf.format(todayDate)));*/
            //System.out.println("service started......" + date);
            if (date.equalsIgnoreCase("12:00 AM")) {
                try {
                    Log.i("Check Context", "check context " + getContext());

                    if (mActivity != null)
                        Utility.resetFood(mActivity);
                    if (mActivity != null) {
                        mActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (mTitleList != null) {
                                    titleListAdapter = new TitleListAdapter(mActivity);
                                    mTitleList.setAdapter(titleListAdapter);
                                    Date date = new Date();
                                    SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yy");
                                    mDateTxt.setText(String.valueOf(sdf.format(date)));
                                    mDateTxt.invalidate();
                                }
                            }
                        });
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    };


    private void createTimerSchedule() {

        timerTask = new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {

                        try {
                            fileCount = new File(AppConstants.baseDir + "/" + AppConstants.FORAGER_DIR + "/" + active_plan).list().length;
                        } catch (Exception e) {
                            fileCount = 5;
                            e.printStackTrace();
                        }

                        try {
                            File sourceFile1 = new File(AppConstants.baseDir + "/" + AppConstants.FORAGER_DIR + "/" + active_plan, count + ".png");
                            if (sourceFile1.exists()) {
                                Bitmap myBitmap = Utility.callPhotoUploadMethod(sourceFile1.getAbsolutePath());
                                mImage.setImageBitmap(myBitmap);
                            }
                            count++;
                            if (count >= fileCount - 1) {
                                count = 1;
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        };

    }

    public BackgroundService backgroundService;
    private boolean bound = false;

    TitleListAdapter titleListAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_plan_controller, container, false);
        createTimerSchedule();
        init(rootView);

        try {
            mTimer = new Timer();
            mTimer.schedule(timerTask1, 0, 6000 * 10 * 1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rootView;
    }

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        TitleListAdapter listAdapter = (TitleListAdapter) listView.getAdapter();
        if (listAdapter == null)
            return;

        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.UNSPECIFIED);
        int totalHeight = 0;
        View view = null;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            view = listAdapter.getView(i, view, listView);
            if (i == 0)
                view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, ViewGroup.LayoutParams.WRAP_CONTENT));

            view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += view.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onDestroyView() {
        mDatabase.closeDatabase();
        super.onDestroyView();
    }

    private void init(View rootView) {
        mDatabase = Database.getInstance(getActivity());
        mDatabase.getMyWritableDatabase();
        active_plan = mDatabase.getActivePlanType();
        mTitleList = (ListView) rootView.findViewById(R.id.listView);
        titleListAdapter = new TitleListAdapter(getActivity());
        mTitleList.setAdapter(titleListAdapter);
        planLogo = (ImageView) rootView.findViewById(R.id.plan_logo);
        pref1 = getActivity().getSharedPreferences(AppConstants.SHARED_PREFES_USER_DETAILS, Context.MODE_PRIVATE);
        Purchasestatus();


        setListViewHeightBasedOnChildren(mTitleList);

        mTitleList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                mealType = parent.getItemAtPosition(position).toString();
                Intent changebody = new Intent(getActivity(), DashBoardActivity.class);
                getActivity().overridePendingTransition(0, 0);
                changebody.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                changebody.putExtra("dashboard", "breakfast");
                getActivity().finish();
                getActivity().overridePendingTransition(0, 0);
                getActivity().startActivity(changebody);
            }
        });
        MyApplication.setContext(getActivity());
        mDateTxt = (TextView) rootView.findViewById(R.id.txt_date);
        mDateTxt.setTypeface(MyApplication.getFont());
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yy");
        mDateTxt.setText(String.valueOf(sdf.format(date)));
        mImage = (ImageView) rootView.findViewById(R.id.chart_img);

//        if(timerTask)
        try {
            t.schedule(timerTask, 0, 20000);
        } catch (Exception e) {
            t.cancel();
            t.purge();
            createTimerSchedule();
            t = new Timer();
            t.schedule(timerTask, 0, 20000);
        }
//        t.schedule(new ReminderT(timer, seconds), seconds*200);
        active_plan = mDatabase.getActivePlanType();
        File sourceFile = new File(AppConstants.baseDir + "/" + AppConstants.FORAGER_DIR + "/" + active_plan + "/" + AppConstants.SHOP_MEAL_PLAN_LOGO_DIR, "1.png");

        if (sourceFile.exists()) {
            Bitmap myBitmap = Utility.callPhotoUploadMethod(sourceFile.getAbsolutePath());
            planLogo.setImageBitmap(myBitmap);
        }
        mChart_layout = (LinearLayout) rootView.findViewById(R.id.layout_info);
        mChart = (LinearLayout) rootView.findViewById(R.id.linear);

        if (TitleListAdapter.mainChart_Status == true) {
            mChart_layout.setVisibility(View.VISIBLE);
            mImage.setVisibility(View.GONE);
        }

        mShoppingList = (TextView) rootView.findViewById(R.id.txt_add_shopping_list);
        mSupplements = (TextView) rootView.findViewById(R.id.txt_supplements);
        mGuidelines = (TextView) rootView.findViewById(R.id.txt_guidelines);

        calories = (TextView) rootView.findViewById(R.id.calories);
        caloriesValues = (TextView) rootView.findViewById(R.id.caloriesValues);
        carbs = (TextView) rootView.findViewById(R.id.carbs);
        carbsValue = (TextView) rootView.findViewById(R.id.carbsValue);
        carbsPercentage = (TextView) rootView.findViewById(R.id.carbsPercentage);
        fats = (TextView) rootView.findViewById(R.id.fats);
        fatsValue = (TextView) rootView.findViewById(R.id.fatsValue);
        fatsPercentage = (TextView) rootView.findViewById(R.id.fatsPercentage);
        protein = (TextView) rootView.findViewById(R.id.protein);
        proteinValue = (TextView) rootView.findViewById(R.id.proteinValue);
        proteinPercentage = (TextView) rootView.findViewById(R.id.proteinPercentage);
        fiber = (TextView) rootView.findViewById(R.id.fiber);
        fiberValue = (TextView) rootView.findViewById(R.id.fiberValue);

        SharedPreferences preferences = Utility.getSharedPreferences(getActivity(), AppConstants.SHARED_PREFES_MEAL_PLAN_CHART_STATUS);
        breakFastChartStatus = preferences.getBoolean(AppConstants.BREAK_FAST_CHART, false);


        if (breakFastChartStatus) {
            mChart_layout.setVisibility(View.VISIBLE);
            mImage.setVisibility(View.GONE);
        }

        mShoppingList.setOnClickListener(this);
        mSupplements.setOnClickListener(this);
        mGuidelines.setOnClickListener(this);

        if ((!flag)) {
            for (String str : DashBoardActivity.mTitleList)
                store.add(str);
        }
        if (!BreakFast_Activity.eaten_status) {
            for (String str : DashBoardActivity.mTitleList)
                store.add(str);
            BreakFast_Activity.eaten_status = true;
        }
        for (String str : DashBoardActivity.mTitleList) {
            if (isMarkEatenPrefesAvailable(str) && (store.contains(str))) {

                SharedPreferences prefer = Utility.getSharedPreferences(getActivity(), AppConstants.SHARED_PREFES_TOTAL_CHART_VALUES);
                calVal = Float.parseFloat(prefer.getString(str + "," + JsonConstants.CALORIES, ""));
                carbsVal = Float.parseFloat(prefer.getString(str + "," + JsonConstants.CARBOHYDRATE, ""));
                fatsVal = Float.parseFloat(prefer.getString(str + "," + JsonConstants.FAT, ""));
                proteinVal = Float.parseFloat(prefer.getString(str + "," + JsonConstants.PROTEIN, ""));
                fiberVal = Float.parseFloat(prefer.getString(str + "," + JsonConstants.FIBER, ""));


                if (mProdValuesTotal.containsKey(KEY_CALORIESVAL)) {
                    float temp = mProdValuesTotal.get(KEY_CALORIESVAL);
                    mProdValuesTotal.put(KEY_CALORIESVAL, temp + calVal);
                    calTotal += calVal;
                    mProdValuesTotal.put(KEY_CALORIESVAL_TOTAL, calTotal);

                } else {

                    mProdValuesTotal.put(KEY_CALORIESVAL, calVal);
                    calTotal += calVal;
                    mProdValuesTotal.put(KEY_CALORIESVAL_TOTAL, calTotal);

                }

                if (mProdValuesTotal.containsKey(KEY_CARBSVAL)) {
                    float temp = mProdValuesTotal.get(KEY_CARBSVAL);
                    mProdValuesTotal.put(KEY_CARBSVAL, temp + carbsVal);
                    carbsTotal += carbsVal;
                    mProdValuesTotal.put(KEY_CARBSVAL_TOTAL, carbsTotal);

                } else {
                    mProdValuesTotal.put(KEY_CARBSVAL, carbsVal);
                    carbsTotal += carbsVal;
                    mProdValuesTotal.put(KEY_CARBSVAL_TOTAL, carbsTotal);

                }
                if (mProdValuesTotal.containsKey(KEY_FIBERVAL)) {
                    float temp = mProdValuesTotal.get(KEY_FIBERVAL);
                    mProdValuesTotal.put(KEY_FIBERVAL, temp + fiberVal);
                    fiberTotal += fiberVal;
                    mProdValuesTotal.put(KEY_FIBERVAL_TOTAL, fiberTotal);

                } else {
                    mProdValuesTotal.put(KEY_FIBERVAL, fiberVal);
                    fiberTotal += fiberVal;
                    mProdValuesTotal.put(KEY_FIBERVAL_TOTAL, fiberTotal);

                }

                if (mProdValuesTotal.containsKey(KEY_FATVAL)) {
                    float temp = mProdValuesTotal.get(KEY_FATVAL);
                    mProdValuesTotal.put(KEY_FATVAL, temp + fatsVal);
                    fatsTotal += fatsVal;
                    mProdValuesTotal.put(KEY_FATVAL_TOTAL, fatsTotal);

                } else {
                    mProdValuesTotal.put(KEY_FATVAL, fatsVal);
                    fatsTotal += fatsVal;
                    mProdValuesTotal.put(KEY_FATVAL_TOTAL, fatsTotal);

                }

                if (mProdValuesTotal.containsKey(KEY_PROTEINVAL)) {
                    float temp = mProdValuesTotal.get(KEY_PROTEINVAL);
                    mProdValuesTotal.put(KEY_PROTEINVAL, temp + proteinVal);
                    proteinTotal += proteinVal;
                    mProdValuesTotal.put(KEY_PROTEINVAL_TOTAL, proteinTotal);

                } else {
                    mProdValuesTotal.put(KEY_PROTEINVAL, proteinVal);
                    proteinTotal += proteinVal;
                    mProdValuesTotal.put(KEY_PROTEINVAL_TOTAL, proteinTotal);

                }
                store.remove(str);
                flag = true;
            }
        }

        caloriesValues.setText(String.valueOf(calTotal) + " cal");
        carbsValue.setText(String.valueOf(carbsTotal) + " g");
        fatsValue.setText(String.valueOf(fatsTotal) + " g");
        proteinValue.setText(String.valueOf(proteinTotal) + " g");
        fiberValue.setText(String.valueOf(fiberTotal) + " g");

        float totalVal = carbsTotal + fatsTotal + proteinTotal;

        System.out.println("Check value " + carbsTotal / totalVal);

        float temp = carbsTotal / totalVal;

        carbsPer = temp * 100;

        fatsPer = (fatsTotal / totalVal) * 100;
        proteinPer = (proteinTotal / totalVal) * 100;

        values[0] = carbsPer;
        values[1] = 0.2f;
        values[2] = fatsPer;
        values[3] = 0.2f;
        values[4] = proteinPer;
        values[5] = 0.2f;
        WindowManager wm = (WindowManager) getActivity().getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        final int width = size.x;
        final int height = size.y;
        if (width <= 480) {
            left = 20;
            right = 180;

        } else if (width <= 720) {
            left = 50;
            right = 250;

        } else if (width <= 1440) {
            left = 50;
            right = 350;
        }

        ArrayList<Integer> list = new ArrayList<Integer>();
        list.add(0, left);
        list.add(1, right);

        PieChart chart = new PieChart();
        values = chart.calculateData(values);
        PieChart.MyGraphview graphview = new PieChart.MyGraphview(getActivity(), values, list);

        mChart.addView(graphview);

        carbsPercentage.setText(getSingleDecPoint(carbsPer) + " %");
        fatsPercentage.setText(getSingleDecPoint(fatsPer) + " %");
        proteinPercentage.setText(getSingleDecPoint(proteinPer) + " %");

    }

    private String getSingleDecPoint(float result) {
        return String.valueOf(new DecimalFormat("0.#").format(result));
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        BreakFast_Activity.eaten_status = false;
        calTotal = 0f;
        carbsTotal = 0f;
        fatsTotal = 0f;
        proteinTotal = 0f;
        fiberTotal = 0f;

    }


    @Override
    public void onClick(View v) {

        int id = v.getId();
        switch (id) {

            case R.id.txt_add_shopping_list:
                Intent shopping = new Intent(getActivity(), DashBoardActivity.class);
                getActivity().overridePendingTransition(0, 0);
                shopping.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                shopping.putExtra("dashboard", "Shopping");
                getActivity().finish();
                getActivity().overridePendingTransition(0, 0);
                getActivity().startActivity(shopping);

                break;

            case R.id.txt_supplements:
                Intent supplementsIntent = new Intent(getActivity(), DashBoardActivity.class);
                getActivity().overridePendingTransition(0, 0);
                supplementsIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                supplementsIntent.putExtra("dashboard", "Supplements");
                getActivity().finish();
                getActivity().overridePendingTransition(0, 0);
                getActivity().startActivity(supplementsIntent);
                break;

            case R.id.txt_guidelines:
                Intent guideLines = new Intent(getActivity(), DashBoardActivity.class);
                getActivity().overridePendingTransition(0, 0);
                guideLines.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                guideLines.putExtra("dashboard", "GuideLines");
                getActivity().finish();
                getActivity().overridePendingTransition(0, 0);
                getActivity().startActivity(guideLines);
                break;
        }
    }

    private boolean isMarkEatenPrefesAvailable(String mealType) {
        SharedPreferences preferences1 = Utility.getSharedPreferences(getActivity(), AppConstants.SHARED_PREFES_MARK_EATEN_DETAILS);
        if (preferences1.contains(mealType + "," + AppConstants.STATUS)) {
            return true;
        }
        return false;
    }

    private void Purchasestatus() {
        String url = AppUrls.FORAGER_PURCHASE_SAVE_URL;
        String userid = pref1.getString(AppConstants.USER_ID, null);
        System.out.println(userid);
        String act = "1";
        // Set AuthKey and Status
        try {
            //    {"user_id":"1090","action":"","in_app":"1","cur_date":"2016-05-11 17:00:00","subs_month":"1","subs_year":""}
            JSONObject jsonObject = new JSONObject();
            jsonObject.accumulate("user_id", userid);
            jsonObject.accumulate("action", act);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
                new JsonAsync(PlanControllerFragment.this, jsonObject.toString(), AppConstants.LOGIN_CALL).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, url);
            else
                new JsonAsync(PlanControllerFragment.this, jsonObject.toString(), AppConstants.LOGIN_CALL).execute(url);


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onResponseReceived(String response, String methodName) {
        //0 -Old users
        //1 -Subscribed user
        //2 -Expired
        // {"status":0,"in_app":"1","subscription":"1 Months","message":"Refresh Successfully"}
        SharedPreferences.Editor editor = getActivity().getSharedPreferences(AppConstants.PURCHASE_STATUS, getActivity().MODE_PRIVATE).edit();


        if (methodName.equals(AppConstants.LOGIN_CALL)) {

            try {
                JSONObject mstatus = new JSONObject(response);
                String in_app = mstatus.getString(AppConstants.IN_APP);
                String subscription = mstatus.getString(AppConstants.SUBSCRIPTION);
                editor.putString(AppConstants.IN_APP, in_app);
                editor.putString(AppConstants.SUBSCRIPTION, subscription);
                editor.commit();



                if (in_app.equals("0"))
                {
                    showalert(AppConstants.UPDATED_USER);
                }else if(in_app.equals("2"))
                {
                    showalert_withsignout(status(subscription));
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }


        }else if(methodName.equals(AppConstants.UPDATE_ACTIVE_CALL))
        {
            try {
                JSONObject mJsonObject = new JSONObject(response);

                if (mJsonObject.has("Message") && mJsonObject.getString("Message").contains("No Record")) {
                    Utility.dismissPDialog(getActivity());
                    Utility.showAlert(getActivity(), "Member Details Error", false);
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
                mHashMap.clear();
                for (int i = 0; i < aboutUsArray.length(); i++) {
                    mHashMap.put(aboutUsArray.get(i).toString(), AppConstants.ABOUT_DIR);
                }
                Utility.writeIntoFile(getActivity(), termsText, AppConstants.TERMS_FILENAME);
                Utility.writeIntoFile(getActivity(), aboutText, AppConstants.ABOUT_FILENAME);

                File mFile = new File(AppConstants.baseDir + "/" + AppConstants.FORAGER_DIR + "/" + AppConstants.ABOUT_DIR);
                mFile.delete();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                    new DownloadImage(PlanControllerFragment.this, mHashMap, AppConstants.ABOUT_IMAGE_CALL).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                } else {
                    new DownloadImage(PlanControllerFragment.this, mHashMap, AppConstants.ABOUT_IMAGE_CALL).execute();
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
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

    public String status(String test)
    {
        return "Your free "+test+"Pro Membership has ended.Please sign out and sign back in to activate your basic membership.Thanks!.";
    }


    public void showalert(String msg) {
        AlertDialog alertDialog = new AlertDialog.Builder(
                this.getActivity()).create();

        // Setting Dialog Title
        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);


        // Setting Dialog Message
        alertDialog.setMessage(msg);

        // Setting Icon to Dialog
        // alertDialog.setIcon(R.drawable.tick);

        // Setting OK Button
        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // Write your code here to execute after dialog closed
                Utility.resetFood(getActivity());
                changeActivePlan();
               /* Intent signout = new Intent(getActivity(), IntroActivity.class);
                signout.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                getActivity().startActivity(signout);
                getActivity().finish();*/
            }
        });

        // Showing Alert Message
        alertDialog.show();
    }
    public void showalert_withsignout(String msg) {
        AlertDialog alertDialog = new AlertDialog.Builder(
                this.getActivity()).create();
       /* Intent signout = new Intent(getActivity(), IntroActivity.class);
        signout.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        getActivity().startActivity(signout);
        getActivity().finish();*/
        // Setting Dialog Title
        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);


        // Setting Dialog Message
        alertDialog.setMessage(msg);

        // Setting Icon to Dialog
        // alertDialog.setIcon(R.drawable.tick);

        // Setting OK Button
        alertDialog.setButton("Sign Out", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // Write your code here to execute after dialog closed
                Utility.resetFood(getActivity());
                changeActivePlan();

            }
        });

        // Showing Alert Message
        alertDialog.show();
    }
    public void changeActivePlan()
    {
        Utility.resetFood(getActivity());
        //plan_id = ShopMealPlannerAdapter.shopMealPlan.getId();
        mDatabase.changeActivePlanId(1);

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
                new JsonAsync(PlanControllerFragment.this, jsonObj.toString(), AppConstants.UPDATE_ACTIVE_CALL).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, AppUrls.ACTIVE_PLAN_URL);
            else
                new JsonAsync(PlanControllerFragment.this, jsonObj.toString(), AppConstants.UPDATE_ACTIVE_CALL).execute(AppUrls.ACTIVE_PLAN_URL);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

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

                new DownloadImage(PlanControllerFragment.this, mHashMap, AppConstants.PROFILE_PLAN_IMAGE_CALL).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            else
                new DownloadImage(PlanControllerFragment.this, mHashMap, AppConstants.PROFILE_PLAN_IMAGE_CALL).execute();

        } else if (methodName.equalsIgnoreCase(AppConstants.PROFILE_PLAN_IMAGE_CALL)) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
                new JsonFileAsync(getActivity(), arrayValues, fileDownloadComplete).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            else
                new JsonFileAsync(getActivity(), arrayValues, fileDownloadComplete).execute();
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

                    } catch (Exception e) {
                        e.printStackTrace();

                    }
                }
            }
            Intent signout = new Intent(getActivity(), IntroActivity.class);
            signout.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            getActivity().startActivity(signout);
            getActivity().finish();
         /*   Intent intent = new Intent(getActivity(), DashBoardActivity.class);
            startActivity(intent);
            getActivity().finish();*/
        }
    };
}
