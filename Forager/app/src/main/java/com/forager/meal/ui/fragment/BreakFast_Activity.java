package com.forager.meal.ui.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.forager.meal.adapter.ExtraFoodsAdapter;
import com.forager.meal.adapter.ListAdapter;
import com.forager.meal.adapter.TitleListAdapter;
import com.forager.meal.adapter.WrapperExpandableListAdapter;
import com.forager.meal.application.MyApplication;
import com.forager.meal.bean.PlanLists;
import com.forager.meal.constants.AppConstants;
import com.forager.meal.constants.JsonConstants;
import com.forager.meal.database.Database;
import com.forager.meal.ui.R;
import com.forager.meal.ui.activity.DashBoardActivity;
import com.forager.meal.ui.activity.PieChart;
import com.forager.meal.utitlity.Utility;
import com.forager.meal.widget.FloatingGroupExpandableListView;

import java.io.File;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Pattern;


public class BreakFast_Activity extends Fragment implements View.OnClickListener {

    public static boolean eaten_status = false;
    public static Boolean status = false;
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
    ArrayList<Integer> mChartValues = new ArrayList<>();
    public int count1 = 0;
    int left, right;


    // ExpandableListView list;
    FloatingGroupExpandableListView extraFood_List;
    ScrollView scrollView;
    TextView extraFoods, title, txt_eaten;
    static ImageView mImage, image_back, reset_food, meal_plan_logo;
    public Boolean flag = false;
    Timer t = new Timer();
    Handler handler = new Handler();
    static int count = 1;
    String[] textValues = {"Mark", "as Eaten"};
    ListAdapter adapter;
    ListView list;
    public static LinearLayout mChartImage, mchart;
    public static Boolean breakFastChartStatus, check_status = false, mark_eaten = false;
    Boolean planController_ChartStatus;
    public static String CURRENTTAG;
    public static float calVal, carbsVal, fatsVal, proteinVal, fiberVal, extraCalVal, eatraCarbsVal, extraFatsVal, extraProteinVal, extraFiberVal;
    public static float carbsPer, fatsPer, proteinPer;
    public static float carbsValToal;
    PlanControllerFragment food = new PlanControllerFragment();
    ArrayList<PlanLists> mItemsBeansCategory = new ArrayList<>();
    ArrayList<String> mealTypeList1 = new ArrayList<>();
    ArrayList<String> mealTypeList = new ArrayList<>();
    ArrayList<String> store = new ArrayList<>();
    String active_plan;
    Database mDatabase;
    TimerTask timerTask;
    int fileCount;

    protected FragmentActivity mActivity;

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
            //System.out.println("service started......" + date);
            if (date.equalsIgnoreCase("12:00 AM")) {
                try {
                    Log.i("Check Context", "check contexte " + getContext());

                    if (mActivity != null)
                        Utility.resetFood(mActivity);


                    if (mActivity != null) {
                        mActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (list != null) {
                                    adapter = new ListAdapter(mActivity, mealTypeList1, food.mealType);
                                    list.setAdapter(adapter);
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
                        File sourceFile1 = new File(AppConstants.baseDir + "/" + AppConstants.FORAGER_DIR + "/" + active_plan, count + ".png");
                        if (sourceFile1.exists()) {
                            Bitmap myBitmap = BitmapFactory.decodeFile(sourceFile1.getAbsolutePath());
                            mImage.setImageBitmap(myBitmap);
                        }
                        count++;
                        if (count >= fileCount - 1) {
                            count = 1;
                        }
                    }
                });

            }
        };

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_break_fast, container, false);
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

    private void breakFastStatus() {

        SharedPreferences preferences = Utility.getSharedPreferences(getActivity(), AppConstants.SHARED_PREFES_MEAL_PLAN_CHART_STATUS);
        breakFastChartStatus = preferences.getBoolean(food.mealType, false);

        if (breakFastChartStatus) {
            mChartImage.setVisibility(View.VISIBLE);
            mImage.setVisibility(View.GONE);
        } else {
            mChartImage.setVisibility(View.GONE);
            mImage.setVisibility(View.VISIBLE);
        }
    }


    @Override
    public void onDestroyView() {
        mDatabase.closeDatabase();
        super.onDestroyView();
    }

    private void init(View rootView) {
        PlanControllerFragment.flag = false;
        mDatabase = Database.getInstance(getActivity());
        mDatabase.getMyWritableDatabase();
        MyApplication.setContext(getActivity());
        image_back = (ImageView) rootView.findViewById(R.id.image_back);
        txt_eaten = (TextView) rootView.findViewById(R.id.txt_eaten);


        if (isMarkEatenPrefesAvailable(food.mealType, AppConstants.STATUS)) {
            txt_eaten.setBackgroundColor(Color.parseColor("#ffa3d86d"));
            txt_eaten.setEnabled(false);

           /* SharedPreferences preferences1 = Utility.getSharedPreferences(getActivity(), AppConstants.SHARED_PREFES_MARK_EATEN_DETAILS);
            SharedPreferences.Editor editor = preferences1.edit();
            editor.putBoolean(food.mealType + "," + AppConstants.STATUS, true);*/

        }


        meal_plan_logo = (ImageView) rootView.findViewById(R.id.meal_plan_logo);
        // meal_plan_logo.setImageResource();


        title = (TextView) rootView.findViewById(R.id.header_title);
        title.setTypeface(MyApplication.getFont());

        mImage = (ImageView) rootView.findViewById(R.id.chart_image);
        scrollView = (ScrollView) rootView.findViewById(R.id.scrollView);
        mChartImage = (LinearLayout) rootView.findViewById(R.id.layout_info);
        mchart = (LinearLayout) rootView.findViewById(R.id.linear);
        list = (ListView) rootView.findViewById(R.id.listView);

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

        reset_food = (ImageView) rootView.findViewById(R.id.imgbtn_reset);

        image_back.setOnClickListener(this);
        txt_eaten.setOnClickListener(this);
        reset_food.setOnClickListener(this);
        active_plan = mDatabase.getActivePlanType();

        try {
            t.schedule(timerTask, 0, 20000);
        } catch (Exception e) {
            t.cancel();
            t.purge();
            createTimerSchedule();
            t = new Timer();
            t.schedule(timerTask, 0, 20000);
        }

        active_plan = mDatabase.getActivePlanType();
        File sourceFile = new File(AppConstants.baseDir + "/" + AppConstants.FORAGER_DIR + "/" + active_plan + "/" + AppConstants.SHOP_MEAL_PLAN_LOGO_DIR, "1.png");

        if (sourceFile.exists()) {
            Bitmap myBitmap = Utility.callPhotoUploadMethod(sourceFile.getAbsolutePath());
            meal_plan_logo.setImageBitmap(myBitmap);
        }

        extraFoods = (TextView) rootView.findViewById(R.id.txt_extraFoods);
        extraFood_List = (FloatingGroupExpandableListView) rootView.findViewById(R.id.extraFoods_myList);
        extraFood_List.setChildDivider(new ColorDrawable(Color.parseColor("#C8C7CC")));
        extraFoods.setOnClickListener(this);

        if (isMarkEatenPrefesAvailable(food.mealType)) {
            SharedPreferences preferences = Utility.getSharedPreferences(getActivity(), AppConstants.SHARED_PREFES_MARK_EATEN_DETAILS);
            boolean reset_status = preferences.getBoolean(food.mealType + "," + AppConstants.STATUS, false);
            if (reset_status)
                reset_food.setVisibility(View.VISIBLE);
        }

     /*   SharedPreferences prefer = Utility.getSharedPreferences(getActivity(), AppConstants.SHARED_PREFES_EXTRA_FOODS_VALUES);
        boolean extra_food_values = prefer.getBoolean(AppConstants.EXTRA_FOODS, false);*/

        breakFastStatus();

       /* if (breakFastChartStatus) {
            mChartImage.setVisibility(View.VISIBLE);
            mImage.setVisibility(View.GONE);
        } else {
            mChartImage.setVisibility(View.GONE);
            mImage.setVisibility(View.VISIBLE);
        }*/


        HashMap<String, ArrayList<PlanLists>> menuItemsChild = new HashMap<>();

        for (int i = 0; i < DashBoardActivity.mItemsBeanArrayList.size(); i++) {
            PlanLists mBean = DashBoardActivity.mItemsBeanArrayList.get(i);

            if (mBean.getMealType().equalsIgnoreCase(food.mealType)) {
                mItemsBeansCategory.add(mBean);
                if (!mealTypeList1.contains(mBean.getCategory())) {
                    mealTypeList1.add(mBean.getCategory());
                }
            }
        }

        for (int i = 0; i < mealTypeList1.size(); i++) {
            ArrayList<PlanLists> mSubList = new ArrayList<>();
            for (PlanLists planLists : mItemsBeansCategory) {
                if (planLists.getCategory().toString().equalsIgnoreCase(mealTypeList1.get(i).toString())) {
                    mSubList.add(planLists);
                }
            }
            menuItemsChild.put(mealTypeList1.get(i).toString(), mSubList);
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < textValues.length; i++) {
            sb.append(textValues[i]);
            if (i == 0) {
                sb.append(" " + food.mealType + " ");
            }
        }
        title.setText(food.mealType);
        txt_eaten.setText(sb);
        adapter = new ListAdapter(getActivity(), mealTypeList1, food.mealType);

        //For Mark as Eaten
        for (String str : mealTypeList1) {
            if (isPlanListPrefesAvailable(food.mealType, str)) {
                if (!store.contains(food.mealType + "," + str)) {
                    store.add(str);
                    count1++;
                }
            }
        }
        if (count1 == mealTypeList1.size()) {
           /* txt_eaten.setEnabled(true);
            txt_eaten.setBackgroundColor(Color.parseColor("#ff63c036"));*/
            SharedPreferences pref = Utility.getSharedPreferences(getActivity(), AppConstants.SHARED_PREFES_MARK_EATEN_DETAILS);
            SharedPreferences.Editor editor1 = pref.edit();
            editor1.putString(food.mealType, "");
            editor1.commit();
        }

        //Mark as Eaten Completed

        list.setAdapter(adapter);


        setListViewHeightBasedOnChildren(list);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> a, View v, int position, long id) {
                CURRENTTAG = a.getItemAtPosition(position).toString();

                Intent mealsList = new Intent(getActivity(), DashBoardActivity.class);
                getActivity().overridePendingTransition(0, 0);
                mealsList.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                mealsList.putExtra("dashboard", "mealslist");
                getActivity().finish();
                getActivity().overridePendingTransition(0, 0);
                getActivity().startActivity(mealsList);

            }
        });

        if (ListAdapter.mProdValues.size() > 0) {

            if (ListAdapter.mProdValues.containsKey(ListAdapter.KEY_CALORIES)) {
                calVal = ListAdapter.mProdValues.get(ListAdapter.KEY_CALORIES);
            }

            if (ListAdapter.mProdValues.containsKey(ListAdapter.KEY_CARBS)) {
                carbsVal = ListAdapter.mProdValues.get(ListAdapter.KEY_CARBS);
            }

            if (ListAdapter.mProdValues.containsKey(ListAdapter.KEY_FIBER)) {
                fiberVal = ListAdapter.mProdValues.get(ListAdapter.KEY_FIBER);
            }

            if (ListAdapter.mProdValues.containsKey(ListAdapter.KEY_FAT)) {
                fatsVal = ListAdapter.mProdValues.get(ListAdapter.KEY_FAT);
            }

            if (ListAdapter.mProdValues.containsKey(ListAdapter.KEY_PROTEIN)) {
                proteinVal = ListAdapter.mProdValues.get(ListAdapter.KEY_PROTEIN);
            }

            try {

                if (ExtraFoodsAdapter.extraFoodStatus == true) {

                    HashMap<String, Float> map = getExtraFoodTotal(getActivity());
                    extraCalVal = map.get(JsonConstants.CALORIES);
                    eatraCarbsVal = map.get(JsonConstants.CARBOHYDRATE);
                    extraProteinVal = map.get(JsonConstants.PROTEIN);
                    extraFiberVal = map.get(JsonConstants.FIBER);
                    extraFatsVal = map.get(JsonConstants.FAT);
                }

            } catch (Exception e) {

            }

           /* if (ExtraFoodsAdapter.extraFoodStatus == true) {

                HashMap<String, Float> map = getExtraFoodTotal(getActivity());
                extraCalVal = map.get(JsonConstants.CALORIES);
                eatraCarbsVal = map.get(JsonConstants.CARBOHYDRATE);
                extraProteinVal = map.get(JsonConstants.PROTEIN);
                extraFiberVal = map.get(JsonConstants.FIBER);
                extraFatsVal = map.get(JsonConstants.FAT);
            }*/

            carbsValToal += carbsVal;

            System.out.println(carbsVal + "carbsValTotal :BA " + carbsValToal);

            caloriesValues.setText(String.valueOf(calVal + extraCalVal) + " cal");
            carbsValue.setText(String.valueOf(carbsVal + eatraCarbsVal) + " g");
            fatsValue.setText(String.valueOf(fatsVal + extraFatsVal) + " g");
            proteinValue.setText(String.valueOf(proteinVal + extraProteinVal) + " g");
            fiberValue.setText(String.valueOf(fiberVal + extraFiberVal) + " g");

            float totalVal = carbsVal + fatsVal + proteinVal;

            System.out.println("Check value " + carbsVal / totalVal);

            float temp = carbsVal / totalVal;

            carbsPer = temp * 100;
            fatsPer = (fatsVal / totalVal) * 100;
            proteinPer = (proteinVal / totalVal) * 100;
            float[] values = {carbsPer, 0.2f, fatsPer, 0.2f, proteinPer, 0.2f};
          /*  values[0] = carbsPer;
            values[2] = fatsPer;
            values[4] = proteinPer;*/

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


            mchart.addView(graphview);

            carbsPercentage.setText(getSingleDecPoint(carbsPer) + " %");
            fatsPercentage.setText(getSingleDecPoint(fatsPer) + " %");
            proteinPercentage.setText(getSingleDecPoint(proteinPer) + " %");
        } else {

        }

        list.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // Disallow the touch request for parent scroll on touch of child view
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });

        mealTypeList = new ArrayList<>();

        for (int i = 0; i < DashBoardActivity.mExtraFoodsBeanArrayList.size(); i++) {
            PlanLists mBean = DashBoardActivity.mExtraFoodsBeanArrayList.get(i);
            mItemsBeansCategory.add(mBean);
            if (!mealTypeList.contains(mBean.getCategory())) {
                mealTypeList.add(mBean.getCategory());
            }
        }

        for (int i = 0; i < mealTypeList.size(); i++) {
            ArrayList<PlanLists> mSubList = new ArrayList<>();
            for (PlanLists planLists : mItemsBeansCategory) {
                if (planLists.getCategory().toString().equalsIgnoreCase(mealTypeList.get(i).toString())) {
                    mSubList.add(planLists);
                }
                menuItemsChild.put(mealTypeList.get(i).toString(), mSubList);
            }
        }

        //final ShoppingListAdapter adapter1 = new ShoppingListAdapter(getActivity(), mealTypeList, menuItemsChild);

        ExtraFoodsAdapter adapter1 = new ExtraFoodsAdapter(getActivity(), mealTypeList, menuItemsChild, food.mealType);

        final WrapperExpandableListAdapter wrapperAdapter1 = new WrapperExpandableListAdapter(adapter1);

        extraFood_List.setAdapter(wrapperAdapter1);
        extraFood_List.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                Utility.setListViewHeight((FloatingGroupExpandableListView) parent, groupPosition);
                return false;
            }
        });
        extraFood_List.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
                LinearLayout.LayoutParams param = (LinearLayout.LayoutParams) extraFood_List.getLayoutParams();
                param.height = (extraFood_List.getChildCount() * extraFood_List.getHeight());
                extraFood_List.setLayoutParams(param);
                extraFood_List.refreshDrawableState();
                scrollView.refreshDrawableState();
            }
        });

        extraFood_List.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {
            @Override
            public void onGroupCollapse(int groupPosition) {
                LinearLayout.LayoutParams param = (LinearLayout.LayoutParams) extraFood_List.getLayoutParams();
                param.height = LinearLayout.LayoutParams.WRAP_CONTENT;
                extraFood_List.setLayoutParams(param);
                extraFood_List.refreshDrawableState();
                scrollView.refreshDrawableState();
            }
        });
    }


    private String getSingleDecPoint(float result) {
        return String.valueOf(new DecimalFormat("0.#").format(result));
    }


    /**
     * * Method for Setting the Height of the ListView dynamically.
     * *** Hack to fix the issue of not showing all the items of the ListView
     * *** when placed inside a ScrollView  ***
     */
    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = (ListAdapter) listView.getAdapter();
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

    private void setListViewHeight(FloatingGroupExpandableListView listView,
                                   int group) {
        WrapperExpandableListAdapter listAdapter = (WrapperExpandableListAdapter) listView.getExpandableListAdapter();
        int totalHeight = 0;
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(),
                View.MeasureSpec.EXACTLY);
        for (int i = 0; i < listAdapter.getGroupCount(); i++) {

            View groupItem = listAdapter.getGroupView(i, false, null, listView);
            groupItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);

            totalHeight += groupItem.getMeasuredHeight();
            if (((listView.isGroupExpanded(i)) && (i != group))
                    || ((!listView.isGroupExpanded(i)) && (i == group))) {
                for (int j = 0; j < listAdapter.getChildrenCount(i); j++) {
                    View listItem = listAdapter.getChildView(i, j, false, null,
                            listView);
                    listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
                    totalHeight += listItem.getMeasuredHeight();
                }
            }
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        int height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getGroupCount() - 1));
        if (height < 10)
            height = 200;
        params.height = height;
        listView.setLayoutParams(params);
        listView.requestLayout();

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.txt_extraFoods:
              /*  if (flag == false) {
                    extraFood_List.setVisibility(View.VISIBLE);
                    flag = true;
                } else {
                    extraFood_List.setVisibility(View.GONE);
                    flag = false;
                }*/

                Intent mealsList = new Intent(getActivity(), DashBoardActivity.class);
                getActivity().overridePendingTransition(0, 0);
                mealsList.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                mealsList.putExtra("dashboard", "extrafoodtag");
                getActivity().finish();
                getActivity().overridePendingTransition(0, 0);
                getActivity().startActivity(mealsList);
                break;


            case R.id.image_back:
                Intent shopping = new Intent(getActivity(), DashBoardActivity.class);
                getActivity().overridePendingTransition(0, 0);
                shopping.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                shopping.putExtra("dashboard", "markeaten");
                getActivity().finish();
                getActivity().overridePendingTransition(0, 0);
                getActivity().startActivity(shopping);
                break;
            case R.id.txt_eaten:

                if (isMarkEatenPrefesAvailable(food.mealType)) {

                    SharedPreferences preferences1 = Utility.getSharedPreferences(getActivity(), AppConstants.SHARED_PREFES_MARK_EATEN_DETAILS);
                    SharedPreferences.Editor editor = preferences1.edit();
                    editor.putBoolean(food.mealType + "," + AppConstants.STATUS, true);
                    editor.putBoolean(AppConstants.ALREADY_STORED_PREFERENCE, true);
                    editor.commit();

                    SharedPreferences mark_eaten_preference = Utility.getSharedPreferences(getActivity(), AppConstants.SHARED_PREFES_MARK_EATEN);
                    SharedPreferences.Editor mark_eaten_editor = mark_eaten_preference.edit();
                    mark_eaten_editor.putBoolean(AppConstants.MARK_EATEN_STATUS, true);
                    mark_eaten_editor.commit();

                    reset_food.setVisibility(View.VISIBLE);
                    txt_eaten.setBackgroundColor(Color.parseColor("#ffa3d86d"));
                    txt_eaten.setEnabled(false);

                    Intent mealsList1 = new Intent(getActivity(), DashBoardActivity.class);
                    getActivity().overridePendingTransition(0, 0);
                    mealsList1.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    mealsList1.putExtra("dashboard", "markeaten");
                    getActivity().finish();
                    getActivity().overridePendingTransition(0, 0);
                    getActivity().startActivity(mealsList1);

                } else {
                    for (String str : mealTypeList1) {
                        if (!store.contains(str)) {
                            Utility.showAlert(getActivity(), "select " + str, false);
                            break;
                        }
                    }
                }
                break;
            case R.id.imgbtn_reset:
                openAlert();
                break;
        }
    }

    public static boolean isSharedPrefesAvailable(Activity activity, PlanLists planLists) {
        SharedPreferences preferences = Utility.getSharedPreferences(activity, AppConstants.SHARED_PREFES_MEAL_PLANNER_DETAILS);

        String key = planLists.getMealType().toString() + "," + planLists.getCategory().toString();

        if (preferences.contains(key)) {
            return true;
        }
        return false;
    }

    public static void deleteSharedPrefes(Activity activity, PlanLists planLists) {
        SharedPreferences preferences = Utility.getSharedPreferences(activity, AppConstants.SHARED_PREFES_MEAL_PLANNER_DETAILS);
        SharedPreferences.Editor editor = preferences.edit();

        editor.remove(planLists.getMealType().toString() + "," + planLists.getCategory().toString());
        editor.remove(planLists.getMealType().toString() + "," + planLists.getCategory().toString() + "," + JsonConstants.CALORIES);
        editor.remove(planLists.getMealType().toString() + "," + planLists.getCategory().toString() + "," + JsonConstants.CARBOHYDRATE);
        editor.remove(planLists.getMealType().toString() + "," + planLists.getCategory().toString() + "," + JsonConstants.FAT);
        editor.remove(planLists.getMealType().toString() + "," + planLists.getCategory().toString() + "," + JsonConstants.FIBER);
        editor.remove(planLists.getMealType().toString() + "," + planLists.getCategory().toString() + "," + JsonConstants.PROTEIN);
        editor.remove(planLists.getMealType().toString() + "," + planLists.getCategory().toString() + "," + JsonConstants.CALORIES_TOTAL);
        editor.commit();
    }


    public static void storeSharedPrefes(Activity activity, PlanLists planLists) {
        SharedPreferences preferences = Utility.getSharedPreferences(activity, AppConstants.SHARED_PREFES_MEAL_PLANNER_DETAILS);
        SharedPreferences.Editor editor = preferences.edit();

        editor.putString(planLists.getMealType().toString() + "," + planLists.getCategory().toString(), planLists.getFoodName());
        editor.putString(planLists.getMealType().toString() + "," + planLists.getCategory().toString() + "," + JsonConstants.CALORIES, planLists.getCalories());
        editor.putString(planLists.getMealType().toString() + "," + planLists.getCategory().toString() + "," + JsonConstants.CARBOHYDRATE, planLists.getCarbohydrate());
        editor.putString(planLists.getMealType().toString() + "," + planLists.getCategory().toString() + "," + JsonConstants.FAT, planLists.getFat());
        editor.putString(planLists.getMealType().toString() + "," + planLists.getCategory().toString() + "," + JsonConstants.FIBER, planLists.getFiber());
        editor.putString(planLists.getMealType().toString() + "," + planLists.getCategory().toString() + "," + JsonConstants.PROTEIN, planLists.getProtein());
        editor.putString(planLists.getMealType().toString() + "," + planLists.getCategory().toString() + "," + JsonConstants.CALORIES_TOTAL, String.valueOf(carbsVal));
        editor.commit();

    }


    public static void storeExtraSharedPrefes(Activity activity, PlanLists planLists) {

        SharedPreferences preferences = Utility.getSharedPreferences(activity, AppConstants.SHARED_PREFES_EXTRA_FOOD_DETAILS);
        SharedPreferences.Editor editor = preferences.edit();

        editor.putString(PlanControllerFragment.mealType + "," + planLists.getCategory() + "," + planLists.getFoodName(), planLists.getFoodName());
        editor.putString(PlanControllerFragment.mealType + "," + planLists.getCategory() + "," + JsonConstants.CALORIES, planLists.getCalories());
        editor.putString(PlanControllerFragment.mealType + "," + planLists.getCategory() + "," + JsonConstants.CARBOHYDRATE, planLists.getCarbohydrate());
        editor.putString(PlanControllerFragment.mealType + "," + planLists.getCategory() + "," + JsonConstants.FAT, planLists.getFat());
        editor.putString(PlanControllerFragment.mealType + "," + planLists.getCategory() + "," + JsonConstants.FIBER, planLists.getFiber());
        editor.putString(PlanControllerFragment.mealType + "," + planLists.getCategory() + "," + JsonConstants.PROTEIN, planLists.getProtein());

        if (preferences.contains(PlanControllerFragment.mealType + "," + JsonConstants.CALORIES)) {
            float calo = Float.parseFloat(preferences.getString(PlanControllerFragment.mealType + "," + JsonConstants.CALORIES, ""));
            calo += Float.parseFloat(planLists.getCalories());
            editor.putString(PlanControllerFragment.mealType + "," + JsonConstants.CALORIES, String.valueOf(calo));

            float carbs = Float.parseFloat(preferences.getString(PlanControllerFragment.mealType + "," + JsonConstants.CARBOHYDRATE, ""));
            carbs += Float.parseFloat(planLists.getCarbohydrate());
            editor.putString(PlanControllerFragment.mealType + "," + JsonConstants.CARBOHYDRATE, String.valueOf(carbs));

            float fat = Float.parseFloat(preferences.getString(PlanControllerFragment.mealType + "," + JsonConstants.FAT, ""));
            fat += Float.parseFloat(planLists.getFat());
            editor.putString(PlanControllerFragment.mealType + "," + JsonConstants.FAT, String.valueOf(fat));

            float fiber = Float.parseFloat(preferences.getString(PlanControllerFragment.mealType + "," + JsonConstants.FIBER, ""));
            fiber += Float.parseFloat(planLists.getFiber());
            editor.putString(PlanControllerFragment.mealType + "," + JsonConstants.FIBER, String.valueOf(fiber));

            float protein = Float.parseFloat(preferences.getString(PlanControllerFragment.mealType + "," + JsonConstants.PROTEIN, ""));
            protein += Float.parseFloat(planLists.getProtein());
            editor.putString(PlanControllerFragment.mealType + "," + JsonConstants.PROTEIN, String.valueOf(protein));

        } else {
            editor.putString(PlanControllerFragment.mealType + "," + planLists.getCategory() + "," + planLists.getFoodName(), planLists.getFoodName());
            editor.putString(PlanControllerFragment.mealType + "," + JsonConstants.CALORIES, planLists.getCalories());
            editor.putString(PlanControllerFragment.mealType + "," + JsonConstants.CARBOHYDRATE, planLists.getCarbohydrate());
            editor.putString(PlanControllerFragment.mealType + "," + JsonConstants.FAT, planLists.getFat());
            editor.putString(PlanControllerFragment.mealType + "," + JsonConstants.FIBER, planLists.getFiber());
            editor.putString(PlanControllerFragment.mealType + "," + JsonConstants.PROTEIN, planLists.getProtein());
        }

        editor.commit();
    }

  /*  public static float getExtraFoodCalorie(Activity activity) {
        SharedPreferences preferences = Utility.getSharedPreferences(activity, AppConstants.SHARED_PREFES_EXTRA_FOOD_DETAILS);
        if (preferences.contains(PlanControllerFragment.mealType + "," + JsonConstants.CALORIES)) {
            return Float.parseFloat(preferences.getString(PlanControllerFragment.mealType + "," + JsonConstants.CALORIES, ""));
        } else {
            return 0;
        }
    }*/

    public static int getExtraFoodCalorie(Activity activity) {
        int calories = 0;
        SharedPreferences preferences = Utility.getSharedPreferences(activity, AppConstants.SHARED_PREFES_EXTRA_FOOD_DETAILS);
        for (String mealType : DashBoardActivity.mTitleList) {
            if (preferences.contains(mealType + "," + JsonConstants.CALORIES)) {
                calories += (int) Math.round(Float.parseFloat(preferences.getString(mealType + "," + JsonConstants.CALORIES, "")));
            }
        }
        return calories;
    }
       /* if (preferences.contains(PlanControllerFragment.mealType + "," + JsonConstants.CALORIES)) {
            return (int) Math.round(Float.parseFloat(preferences.getString(PlanControllerFragment.mealType + "," + JsonConstants.CALORIES, "")));
        } else {
            return 0;
        }*/


    private boolean isPlanListPrefesAvailable(String mealType, String category) {
        SharedPreferences preferences = Utility.getSharedPreferences(getActivity(), AppConstants.SHARED_PREFES_MEAL_PLANNER_DETAILS);
        String planListKey = mealType + "," + category;
        if (preferences.contains(planListKey)) {
            return true;
        }
        return false;
    }

    private boolean isMarkEatenPrefesAvailable(String mealType) {
        SharedPreferences preferences1 = Utility.getSharedPreferences(getActivity(), AppConstants.SHARED_PREFES_MARK_EATEN_DETAILS);

        if (preferences1.contains(mealType)) {
            return true;
        }
        return false;
    }

    private boolean isMarkEatenPrefesAvailable(String mealType, String status) {
        SharedPreferences preferences1 = Utility.getSharedPreferences(getActivity(), AppConstants.SHARED_PREFES_MARK_EATEN_DETAILS);
        String planListKey = mealType + "," + status;
        if (preferences1.contains(planListKey)) {
            return true;
        }
        return false;
    }


    private boolean isExtraFoodPrefesAvailable(String cal) {
        SharedPreferences preferences = Utility.getSharedPreferences(getActivity(), AppConstants.SHARED_PREFES_EXTRA_FOOD_DETAILS);
        String planListKey = cal;
        if (preferences.contains(planListKey)) {
            return true;
        }
        return false;
    }

    private String getContentFromPrefes(String mealType, String category) {
        SharedPreferences preferences = Utility.getSharedPreferences(getActivity(), AppConstants.SHARED_PREFES_EXTRA_FOOD_DETAILS);
        String planListKey = mealType + "," + category;
        return preferences.getString(planListKey, "");
    }


    private static final Pattern DOUBLE_PATTERN = Pattern.compile(
            "[\\x00-\\x20]*[+-]?(NaN|Infinity|((((\\p{Digit}+)(\\.)?((\\p{Digit}+)?)" +
                    "([eE][+-]?(\\p{Digit}+))?)|(\\.((\\p{Digit}+))([eE][+-]?(\\p{Digit}+))?)|" +
                    "(((0[xX](\\p{XDigit}+)(\\.)?)|(0[xX](\\p{XDigit}+)?(\\.)(\\p{XDigit}+)))" +
                    "[pP][+-]?(\\p{Digit}+)))[fFdD]?))[\\x00-\\x20]*");

    public static boolean isFloat(String s) {
        return DOUBLE_PATTERN.matcher(s).matches();
    }


    public static HashMap<String, Float> getExtraFoodTotal(Context context) {

        SharedPreferences preferences = Utility.getSharedPreferences(context, AppConstants.SHARED_PREFES_EXTRA_FOOD_DETAILS);
        HashMap<String, Float> hashMap = new HashMap<>();
        Map<String, ?> keys = preferences.getAll();
        if (keys != null) {
            for (Map.Entry<String, ?> entry : keys.entrySet()) {
                Log.d("map values", entry.getKey() + ": " +
                        entry.getValue().toString());
                if (isFloat(entry.getValue().toString())) {
                    if (entry.getKey().contains(JsonConstants.CALORIES)) {
                        if (hashMap.containsKey(JsonConstants.CALORIES)) {
                            float calo = hashMap.get(JsonConstants.CALORIES);
                            calo += Float.parseFloat(entry.getValue().toString());
                            hashMap.put(JsonConstants.CALORIES, calo);

                        } else {
                            hashMap.put(JsonConstants.CALORIES, Float.parseFloat(entry.getValue().toString()));
                        }
                    } else if (entry.getKey().contains(JsonConstants.CARBOHYDRATE)) {
                        if (hashMap.containsKey(JsonConstants.CARBOHYDRATE)) {
                            float carbs = hashMap.get(JsonConstants.CARBOHYDRATE);
                            carbs += Float.parseFloat(entry.getValue().toString());
                            hashMap.put(JsonConstants.CARBOHYDRATE, carbs);
                        } else {
                            hashMap.put(JsonConstants.CARBOHYDRATE, Float.parseFloat(entry.getValue().toString()));
                        }
                    } else if (entry.getKey().contains(JsonConstants.FIBER)) {
                        if (hashMap.containsKey(JsonConstants.FIBER)) {
                            float fiber = hashMap.get(JsonConstants.FIBER);
                            fiber += Float.parseFloat(entry.getValue().toString());
                            hashMap.put(JsonConstants.FIBER, fiber);
                        } else {
                            hashMap.put(JsonConstants.FIBER, Float.parseFloat(entry.getValue().toString()));
                        }

                    } else if (entry.getKey().contains(JsonConstants.PROTEIN)) {
                        if (hashMap.containsKey(JsonConstants.PROTEIN)) {
                            float protein = hashMap.get(JsonConstants.PROTEIN);
                            protein += Float.parseFloat(entry.getValue().toString());
                            hashMap.put(JsonConstants.PROTEIN, protein);
                        } else {
                            hashMap.put(JsonConstants.PROTEIN, Float.parseFloat(entry.getValue().toString()));
                        }
                    } else if (entry.getKey().contains(JsonConstants.FAT)) {
                        if (hashMap.containsKey(JsonConstants.FAT)) {
                            float fat = hashMap.get(JsonConstants.FAT);
                            fat += Float.parseFloat(entry.getValue().toString());
                            hashMap.put(JsonConstants.FAT, fat);
                        } else {
                            hashMap.put(JsonConstants.FAT, Float.parseFloat(entry.getValue().toString()));
                        }
                    }
                }
            }
        }
        return hashMap;
    }

    private void openAlert() {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity(), R.style.CustomAlertDialogStyle);
        alertDialogBuilder.setMessage("Are you sure you want to refresh this meal?");
        alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {


             //   Utility.resetFood(getActivity());

                Utility.resetMealPlan(getActivity(), food.mealType, mealTypeList1);
                reset_food.setVisibility(View.GONE);
                adapter.notifyDataSetChanged();
                adapter = new ListAdapter(getActivity(), mealTypeList1, food.mealType);
                list.setAdapter(adapter);

                mChartImage.setVisibility(View.GONE);
                mImage.setVisibility(View.VISIBLE);
                TitleListAdapter.mainChart_Status = false;
                TitleListAdapter.extraFoodStatus = false;
                txt_eaten.setBackgroundColor(Color.parseColor("#ff63c036"));
                txt_eaten.setEnabled(true);
                store.clear();

            }
        });

        // set negative button: No message
        alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
                // Toast.makeText(getApplicationContext(), "You chose a negative answer",  Toast.LENGTH_LONG).show();
            }

        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
}

