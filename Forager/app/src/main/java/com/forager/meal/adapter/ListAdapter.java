package com.forager.meal.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.forager.meal.bean.PlanLists;
import com.forager.meal.constants.AppConstants;
import com.forager.meal.constants.JsonConstants;
import com.forager.meal.ui.R;
import com.forager.meal.utitlity.Utility;

import java.util.ArrayList;
import java.util.HashMap;

public class ListAdapter extends BaseAdapter {

    private final Context mContext;

    Activity mActivity;

    private LayoutInflater inflater;

    ArrayList<String> mCurrrentInfo;
    private ArrayList<String> mItems;
    public static String title;

    public static ImageView mark_icon;


    ArrayList<PlanLists> mChartInfo = new ArrayList<>();

    public static HashMap<String, Float> mProdValues = new HashMap<>();

    public float carbsTotal, calTotal, proteinTotal, fiberTotal, fatsTotal;

    public static final String KEY_CALORIES = "calories";

    public static final String KEY_CARBS = "carbs";

    public static final String KEY_FIBER = "fiber";

    public static final String KEY_FAT = "fat";

    public static final String KEY_PROTEIN = "protein";

    public static final String KEY_CARBS_TOTAL = "carbs_total";

    public static final String KEY_CALORIES_TOTAL = "calories_total";

    public static final String KEY_FIBER_TOTAL = "fiber_total";

    public static final String KEY_FAT_TOTAL = "fat_total";

    public static final String KEY_PROTEIN_TOTAL = "protein_total";
    public int count;
    public static Boolean plan_status = false;

    public int tempCount;

    public ListAdapter(Context context, ArrayList<String> Items, String title) {
        this.mContext = context;
        this.mItems = Items;
        this.title = title;
        mProdValues.clear();
        addPrefesToList();
        Log.e("Check Size", " = " + mProdValues.size());

        for (String str : mItems) {
            if (isPlanListPrefesAvailable(title, str)) {
                tempCount++;
            }
        }
        SharedPreferences pref = Utility.getSharedPreferences(context, AppConstants.SHARED_PREFES_MEAL_PLAN_CHART_STATUS);
        SharedPreferences.Editor editor = pref.edit();
        if (tempCount == 0) {

            editor.putBoolean(title, false);

        } else {

            editor.putBoolean(title, true);
        }
        editor.commit();

    }

    @Override
    public int getCount() {
        return mItems.size();
    }

    @Override
    public Object getItem(int location) {
        return mItems.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (inflater == null)
            inflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.group_list_item, null);

        final TextView text = (TextView) convertView.findViewById(R.id.group_text);
        mark_icon = (ImageView) convertView.findViewById(R.id.group_title_image);
        ImageView tick_icon = (ImageView) convertView.findViewById(R.id.group_tick_image);


        if (isPlanListPrefesAvailable(title, mItems.get(position))) {
            count++;
            if (getCount() == count) {
                plan_status = true;
            }
            String str = getContentFromPrefes(title, mItems.get(position));
            text.setText(str);

            if (isMarkEatenPrefesAvailable(title)) {
                if (getContent(title))

                    mark_icon.setVisibility(View.GONE);
                tick_icon.setVisibility(View.VISIBLE);

            }
        } else {
            text.setText("Select " + mItems.get(position));
        }

        tick_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mActivity, "test value", Toast.LENGTH_SHORT).show();
            }
        });

        return convertView;

    }

    private boolean isPlanListPrefesAvailable(String mealType, String category) {
        SharedPreferences preferences = Utility.getSharedPreferences((android.app.Activity) mContext, AppConstants.SHARED_PREFES_MEAL_PLANNER_DETAILS);
        String planListKey = mealType + "," + category;
        if (preferences.contains(planListKey)) {
            return true;
        }
        return false;
    }

    private String getContentFromPrefes(String mealType, String category) {
        SharedPreferences preferences = Utility.getSharedPreferences((android.app.Activity) mContext, AppConstants.SHARED_PREFES_MEAL_PLANNER_DETAILS);
        String planListKey = mealType + "," + category;
        return preferences.getString(planListKey, "");
    }

    private String getContentFromPrefes(String mealType, String category, String constants) {
        SharedPreferences preferences = Utility.getSharedPreferences((android.app.Activity) mContext, AppConstants.SHARED_PREFES_MEAL_PLANNER_DETAILS);
        String planListKey = mealType + "," + category + "," + constants;
        return preferences.getString(planListKey, "");
    }

    public void addPrefesToList() {

        for (String str : mItems) {

            if (isPlanListPrefesAvailable(title, str)) {

                float calories = Float.parseFloat(getContentFromPrefes(title, str, JsonConstants.CALORIES));
                float carbs = Float.parseFloat(getContentFromPrefes(title, str, JsonConstants.CARBOHYDRATE));
                float protein = Float.parseFloat(getContentFromPrefes(title, str, JsonConstants.PROTEIN));
                float fiber = Float.parseFloat(getContentFromPrefes(title, str, JsonConstants.FIBER));
                float fats = Float.parseFloat(getContentFromPrefes(title, str, JsonConstants.FAT));

                if (mProdValues.containsKey(KEY_CALORIES)) {
                    float temp = mProdValues.get(KEY_CALORIES);
                    mProdValues.put(KEY_CALORIES, temp + calories);
                    calTotal += calories;
                    System.out.println(calTotal);
                    mProdValues.put(KEY_CALORIES_TOTAL, calTotal);
                    storeSharedPrefes(title, KEY_CALORIES_TOTAL, calTotal);
                } else {
                    mProdValues.put(KEY_CALORIES, calories);
                    calTotal += calories;
                    mProdValues.put(KEY_CALORIES_TOTAL, calTotal);
                    storeSharedPrefes(title, KEY_CALORIES_TOTAL, calTotal);
                }

                if (mProdValues.containsKey(KEY_CARBS)) {
                    float temp = mProdValues.get(KEY_CARBS);
                    mProdValues.put(KEY_CARBS, temp + carbs);
                    carbsTotal += carbs;
                    mProdValues.put(KEY_CARBS_TOTAL, carbsTotal);
                    storeSharedPrefes(title, KEY_CARBS_TOTAL, carbsTotal);
                } else {
                    mProdValues.put(KEY_CARBS, carbs);
                    carbsTotal += carbs;
                    mProdValues.put(KEY_CARBS_TOTAL, carbsTotal);
                    storeSharedPrefes(title, KEY_CARBS_TOTAL, carbsTotal);
                }
                if (mProdValues.containsKey(KEY_FIBER)) {
                    float temp = mProdValues.get(KEY_FIBER);
                    mProdValues.put(KEY_FIBER, temp + fiber);
                    fiberTotal += fiber;
                    mProdValues.put(KEY_FIBER_TOTAL, fiberTotal);
                    storeSharedPrefes(title, KEY_FIBER_TOTAL, fiberTotal);
                } else {
                    mProdValues.put(KEY_FIBER, fiber);
                    fiberTotal += fiber;
                    mProdValues.put(KEY_FIBER_TOTAL, fiberTotal);
                    storeSharedPrefes(title, KEY_FIBER_TOTAL, fiberTotal);
                }

                if (mProdValues.containsKey(KEY_FAT)) {
                    float temp = mProdValues.get(KEY_FAT);
                    mProdValues.put(KEY_FAT, temp + fats);
                    fatsTotal += fats;
                    mProdValues.put(KEY_FAT_TOTAL, fatsTotal);
                    storeSharedPrefes(title, KEY_FAT_TOTAL, fatsTotal);
                } else {
                    mProdValues.put(KEY_FAT, fats);
                    fatsTotal += fats;
                    mProdValues.put(KEY_FAT_TOTAL, fatsTotal);
                    storeSharedPrefes(title, KEY_FAT_TOTAL, fatsTotal);
                }

                if (mProdValues.containsKey(KEY_PROTEIN)) {
                    float temp = mProdValues.get(KEY_PROTEIN);
                    mProdValues.put(KEY_PROTEIN, temp + protein);
                    proteinTotal += protein;
                    mProdValues.put(KEY_PROTEIN_TOTAL, proteinTotal);
                    storeSharedPrefes(title, KEY_PROTEIN_TOTAL, proteinTotal);

                } else {
                    mProdValues.put(KEY_PROTEIN, protein);
                    proteinTotal += protein;
                    mProdValues.put(KEY_PROTEIN_TOTAL, proteinTotal);
                    storeSharedPrefes(title, KEY_PROTEIN_TOTAL, proteinTotal);
                }
            }
        }
        storeTotalValueSharedPrefer(title);
    }

    public void storeTotalValueSharedPrefer(String mealPlan) {
        SharedPreferences preferences = Utility.getSharedPreferences(mContext, AppConstants.SHARED_PREFES_TOTAL_CHART_VALUES);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(mealPlan + "," + AppConstants.CHART_VALUES_TOTAL, true);
        editor.putString(mealPlan + "," + JsonConstants.CALORIES, String.valueOf(calTotal));
        editor.putString(mealPlan + "," + JsonConstants.CARBOHYDRATE, String.valueOf(carbsTotal));
        editor.putString(mealPlan + "," + JsonConstants.PROTEIN, String.valueOf(proteinTotal));
        editor.putString(mealPlan + "," + JsonConstants.FAT, String.valueOf(fatsTotal));
        editor.putString(mealPlan + "," + JsonConstants.FIBER, String.valueOf(fiberTotal));
        editor.commit();
    }

    public void storeSharedPrefes(String mealPlan, String Key, float totalValues) {
        SharedPreferences preferences = Utility.getSharedPreferences((Activity) mContext, AppConstants.SHARED_PREFES_MEAL_PLANNER_CHART_VALUES);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(mealPlan + "," + Key, String.valueOf(totalValues));
        editor.commit();
    }

    private boolean isMarkEatenPrefesAvailable(String mealType) {
        SharedPreferences preferences1 = Utility.getSharedPreferences(mContext, AppConstants.SHARED_PREFES_MARK_EATEN_DETAILS);

        if (preferences1.contains(mealType + "," + AppConstants.STATUS)) {
            return true;
        }
        return false;
    }

    private boolean getContent(String mealType) {
        SharedPreferences preferences1 = Utility.getSharedPreferences(mContext, AppConstants.SHARED_PREFES_MARK_EATEN_DETAILS);
        return preferences1.getBoolean(mealType + "," + AppConstants.STATUS, false);
    }

}