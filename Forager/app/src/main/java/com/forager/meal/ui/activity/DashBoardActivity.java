package com.forager.meal.ui.activity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.forager.meal.bean.PlanLists;
import com.forager.meal.constants.AppConstants;
import com.forager.meal.constants.JsonConstants;
import com.forager.meal.database.Database;
import com.forager.meal.service.ResetFood;
import com.forager.meal.ui.R;
import com.forager.meal.ui.fragment.AboutFragment;
import com.forager.meal.ui.fragment.BodyConsumptionFragment;
import com.forager.meal.ui.fragment.BreakFast_Activity;
import com.forager.meal.ui.fragment.ChangePasswordFragment;
import com.forager.meal.ui.fragment.DeleteShopListFragment;
import com.forager.meal.ui.fragment.GoalFragment;
import com.forager.meal.ui.fragment.GuideLinesFragment;
import com.forager.meal.ui.fragment.MealsListFrag;
import com.forager.meal.ui.fragment.MoreFragment;
import com.forager.meal.ui.fragment.PlanControllerFragment;
import com.forager.meal.ui.fragment.ProfileFragment;
import com.forager.meal.ui.fragment.ShopMealDetailFragment;
import com.forager.meal.ui.fragment.ShoppingFragment;
import com.forager.meal.ui.fragment.ShoppingMealPlanner;
import com.forager.meal.ui.fragment.SupplementsFragment;
import com.forager.meal.ui.fragment.TermsAndConditionFragment;
import com.forager.meal.ui.fragment.UpgradeFragment;
import com.forager.meal.ui.fragment.WorkFragment;
import com.forager.meal.ui.fragment.YourProfileFragment;
import com.forager.meal.utitlity.Utility;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;

public class DashBoardActivity extends FragmentActivity implements View.OnClickListener {


    //Alarm Manager work
    // AlarmManager alarmManager;
    // private PendingIntent pendingIntent;

    android.support.v4.app.FragmentTransaction transaction;
    private FrameLayout fragmentContainer;
    private LinearLayout footerIcLayout;
    private ImageButton homeIc;
    private ImageButton profileIc;
    private ImageButton storeIc;
    private ImageButton moreIc;
    Database mDatabase;

    public static ArrayList<String> mTitleList = new ArrayList<>();

    public static final String BREAKFASTTAG = "breakfasttag";
    public static final String SHOPPINGTAG = "Shoppinglisttag";
    public static final String ABOUTTAG = "abouttag";
    public static final String PROFILECHANGETAG = "profilechangetag";
    public static final String BODYCHANGETAG = "bodychangetag";
    public static final String GOALCHANGETAG = "goalchangetag";
    public static final String SUPPLEMENTSTAG = "supplementstag";
    public static final String GUIDELINESTAG = "guidelinestag";
    public static final String DETAILSTAG = "detailStoreMealTag";
    public static final String TERMSTAG = "termstag";
    public static final String CHANGEPWDTAG = "changepwdtag";
    public static final String WORKVIDEOTAG = "workvideotag";
    public static final String MEALSLISTTAG = "mealslisttag";
    public static final String SHOPPINGMEALPLANNERTAG = "shoppingmealplannertag";
    public static final String DELETESHOPTAG = "deleteshoptag";
    public static final String MORETAG = "moretag";
    public static final String EXTRAFOODTAG = "extrafoodtag";
    public static final String MARKASEATENTAG = "markeatentag";
    public static final String YOURPROFILETAG = "yourprofiletag";
    public static final String FOODLIST = "foodList";
    public static final String UPGRADETAG= "upgradetag";

    Fragment deleteShopListFragment;
    Fragment aboutFrag;
    Fragment detailFragment;
    Fragment currentFragment;
    Fragment profileChangeFrag;
    Fragment bodyChageFrag;
    Fragment goalChangeFrag;
    Fragment shoppingListFragment;
    Fragment breakFast_fragment;
    Fragment mainFragment;
    Fragment supplementsFrag;
    Fragment guidelinesFrag;
    Fragment termsFragment;
    Fragment changePasswordFragment;
    Fragment workFragment;
    Fragment mealsListFrag;
    Fragment shoppingMealPlanner;
    Fragment MoreFragment;
    Fragment ExtraFoodFragment;
    Fragment yourProfileFragment;
    Fragment upgradeFragment;

    public static String meal_plan_logo;

    String res, response;
    String extraFood_response;
    public static ArrayList<PlanLists> mItemsBeanArrayList = new ArrayList<>();
    public static ArrayList<PlanLists> mExtraFoodsBeanArrayList = new ArrayList<>();
    PlanLists mPlanList;

    //used for register alarm manager
    PendingIntent pendingIntent;
    //used to store running alarmmanager instance
    AlarmManager alarmManager;
    //Callback function for Alarmmanager event
    BroadcastReceiver mReceiver;
    Toast mToast;

    String getDetails = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);

        findViews();
        initInstance();

        // Alarm Manager Work
        Intent myIntent = new Intent(this, ResetFood.class);

        boolean alarmUp = (PendingIntent.getBroadcast(DashBoardActivity.this, 0,
                myIntent,
                PendingIntent.FLAG_NO_CREATE) != null);

        if (!alarmUp) {

            pendingIntent = PendingIntent.getBroadcast(this, 0, myIntent, 0);

            alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
            Calendar calendar = Calendar.getInstance();
            Calendar currentCal = Calendar.getInstance();
            long now = calendar.getTimeInMillis();

            if (DateFormat.is24HourFormat(this)) {
                calendar.set(Calendar.HOUR_OF_DAY, 0);
            } else {
                calendar.set(Calendar.HOUR, 0);
                calendar.set(Calendar.AM_PM, Calendar.PM);
            }

            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);

            long millisecondsUntilMidnight = calendar.getTimeInMillis() - now;

           /* if (calendar.getTimeInMillis() < System.currentTimeMillis()) {
                millisecondsUntilMidnight += AlarmManager.INTERVAL_DAY;
            }*/

            if (calendar.compareTo(currentCal) < 0) {
                calendar.add(Calendar.DAY_OF_MONTH, 1);
            }
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT)
                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                        AlarmManager.INTERVAL_DAY, pendingIntent);
            else
                alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                        AlarmManager.INTERVAL_DAY, pendingIntent);
        }

        // We want the alarm to go off 5 seconds from now.

        if (mainFragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .add(R.id.fragment_container, mainFragment).commit();
            homeIc.setImageResource(R.drawable.homeact_ic);
        } else {
            Log.e("MainActivity", "Error in creating fragment");
        }
        try {
            InputStream inputStream = openFileInput(AppConstants.PLANLISTS_FILENAME);
            res = Utility.getStringFromInputStream(inputStream);
            Log.e("Check File Value", res);

            InputStream inputStreams = openFileInput(AppConstants.EXTRAFOODPLAN);
            extraFood_response = Utility.getStringFromInputStream(inputStreams);
            Log.e("Check File Value", extraFood_response);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        responseReceived(res);
        Log.e("Title Size", " = " + mTitleList.size());
        extraFood_responseReceived(extraFood_response);
    }

    private void initInstance() {
        mainFragment = new PlanControllerFragment();
        shoppingListFragment = new ShoppingFragment();
        breakFast_fragment = new BreakFast_Activity();
        aboutFrag = new AboutFragment();
        profileChangeFrag = new ProfileFragment();
        bodyChageFrag = new BodyConsumptionFragment();
        goalChangeFrag = new GoalFragment();
        detailFragment = new ShopMealDetailFragment();
        supplementsFrag = new SupplementsFragment();
        guidelinesFrag = new GuideLinesFragment();
        termsFragment = new TermsAndConditionFragment();
        changePasswordFragment = new ChangePasswordFragment();
        workFragment = new WorkFragment();
        mealsListFrag = new MealsListFrag();
        shoppingMealPlanner = new ShoppingMealPlanner();
        deleteShopListFragment = new DeleteShopListFragment();
        MoreFragment = new MoreFragment();
        yourProfileFragment = new YourProfileFragment();
        upgradeFragment = new UpgradeFragment();
        ExtraFoodFragment = new com.forager.meal.ui.fragment.ExtraFoodFragment();

    }


    private void findViews() {
        fragmentContainer = (FrameLayout) findViewById(R.id.fragment_container);
        footerIcLayout = (LinearLayout) findViewById(R.id.footer_ic_layout);
        homeIc = (ImageButton) findViewById(R.id.home_ic);
        profileIc = (ImageButton) findViewById(R.id.profile_ic);
        storeIc = (ImageButton) findViewById(R.id.store_ic);
        moreIc = (ImageButton) findViewById(R.id.more_ic);
        homeIc.setOnClickListener(this);
        profileIc.setOnClickListener(this);
        storeIc.setOnClickListener(this);
        moreIc.setOnClickListener(this);
    }

    private void setImageIC(int id) {
        if (id == R.id.home_ic) {
            homeIc.setImageResource(R.drawable.homeact_ic);
            profileIc.setImageResource(R.drawable.profile_ic);
            storeIc.setImageResource(R.drawable.store_ic);
            moreIc.setImageResource(R.drawable.more_ic);

        } else if (id == R.id.profile_ic) {
            homeIc.setImageResource(R.drawable.home_ic);
            profileIc.setImageResource(R.drawable.profileact_ic);
            storeIc.setImageResource(R.drawable.store_ic);
            moreIc.setImageResource(R.drawable.more_ic);

        } else if (id == R.id.store_ic) {
            homeIc.setImageResource(R.drawable.home_ic);
            profileIc.setImageResource(R.drawable.profile_ic);
            storeIc.setImageResource(R.drawable.storeact_ic);
            moreIc.setImageResource(R.drawable.more_ic);
        } else if (id == R.id.more_ic) {
            homeIc.setImageResource(R.drawable.home_ic);
            profileIc.setImageResource(R.drawable.profile_ic);
            storeIc.setImageResource(R.drawable.store_ic);
            moreIc.setImageResource(R.drawable.moreact_ic);
        }
    }

    @Override
    public void onClick(View v) {
        Fragment fragment = null;
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        switch (v.getId()) {
            case R.id.home_ic:
                setImageIC(R.id.home_ic);
                fragment = mainFragment;
                getDetails = "markeaten";
                break;
            case R.id.profile_ic:
                setImageIC(R.id.profile_ic);
                fragment = yourProfileFragment;
                getDetails = "yourprofilefragment";
                break;
            case R.id.store_ic:
                setImageIC(R.id.store_ic);
                fragment = shoppingMealPlanner;
                getDetails = "shoppingmealplannertag";
                break;
            case R.id.more_ic:
                setImageIC(R.id.more_ic);
                fragment = MoreFragment;
                getDetails = "moretag";
                break;
        }
        if (fragment != null) {
            currentFragment = fragment;
            ft.replace(R.id.fragment_container, fragment);
            ft.addToBackStack(null);
            ft.commit();
        }
    }

    protected void onResume() {
        super.onResume();

        Bundle b;
        b = getIntent().getExtras();
        if (b != null) {
            getDetails = getIntent().getExtras().getString("dashboard");

            System.out.println("Checking current frag on resume..." + getDetails);

            if (getDetails.equalsIgnoreCase("breakfast")) {
                replaceFrag(BREAKFASTTAG);
            } else if (getDetails.equalsIgnoreCase("Shopping")) {
                replaceFrag(SHOPPINGTAG);
            } else if (getDetails.equalsIgnoreCase("changeprofile")) {
                replaceFrag(PROFILECHANGETAG);
            } else if (getDetails.equalsIgnoreCase("changebody")) {
                replaceFrag(BODYCHANGETAG);
            } else if (getDetails.equalsIgnoreCase("changegoal")) {
                replaceFrag(GOALCHANGETAG);
            } else if (getDetails.equalsIgnoreCase("detailStoreMealTag")) {
                replaceFrag(DETAILSTAG);
            } else if (getDetails.equalsIgnoreCase("Supplements")) {
                replaceFrag(SUPPLEMENTSTAG);
            } else if (getDetails.equalsIgnoreCase("GuideLines")) {
                replaceFrag(GUIDELINESTAG);
            } else if (getDetails.equalsIgnoreCase("termsandcondition")) {
                replaceFrag(TERMSTAG);
            } else if (getDetails.equalsIgnoreCase("changepassword")) {
                replaceFrag(CHANGEPWDTAG);
            } else if (getDetails.equalsIgnoreCase("worksvideo")) {
                replaceFrag(WORKVIDEOTAG);
            } else if (getDetails.equalsIgnoreCase("abouttag")) {
                replaceFrag(ABOUTTAG);
            } else if (getDetails.equalsIgnoreCase("upgradetag")) {
                replaceFrag(UPGRADETAG);
            } else if (getDetails.equalsIgnoreCase("mealslist")) {
                replaceFrag(MEALSLISTTAG);
            } else if (getDetails.equalsIgnoreCase("shoppingmealplannertag")) {
                replaceFrag(SHOPPINGMEALPLANNERTAG);
            } else if (getDetails.equalsIgnoreCase("deleteshoptag")) {
                replaceFrag(DELETESHOPTAG);
            } else if (getDetails.equalsIgnoreCase("moretag")) {
                replaceFrag(MORETAG);
            } else if (getDetails.equalsIgnoreCase("extrafoodtag")) {
                replaceFrag(EXTRAFOODTAG);
            } else if (getDetails.equalsIgnoreCase("markeaten")) {
                replaceFrag(MARKASEATENTAG);
            } else if (getDetails.equalsIgnoreCase("foodList")) {
                replaceFrag(BREAKFASTTAG);
            } else if (getDetails.equalsIgnoreCase("yourprofilefragment")) {
                replaceFrag(YOURPROFILETAG);
            }
        }
    }


    public void replaceFrag(String tag) {

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        if (tag.equalsIgnoreCase(BREAKFASTTAG)) {

            transaction.replace(R.id.fragment_container, breakFast_fragment, tag);
            transaction.addToBackStack(null);
            transaction.commit();
            setImageIC(R.id.home_ic);
        } else if (tag.equalsIgnoreCase(SHOPPINGTAG)) {

            transaction.replace(R.id.fragment_container, shoppingListFragment, tag);
            transaction.addToBackStack(null);
            transaction.commit();
            setImageIC(R.id.home_ic);
        } else if (tag.equalsIgnoreCase(DETAILSTAG)) {
            transaction.replace(R.id.fragment_container, detailFragment, tag);
            transaction.addToBackStack(null);
            transaction.commit();

            if (YourProfileFragment.profile_clicked_status)
                setImageIC(R.id.profile_ic);
            else
                setImageIC(R.id.store_ic);

        } else if (tag.equalsIgnoreCase(PROFILECHANGETAG)) {
            setImageIC(R.id.profile_ic);
            transaction.replace(R.id.fragment_container, profileChangeFrag, tag);
            transaction.addToBackStack(null);
            transaction.commit();
        } else if (tag.equalsIgnoreCase(BODYCHANGETAG)) {

            setImageIC(R.id.profile_ic);
            transaction.replace(R.id.fragment_container, bodyChageFrag, tag);
            transaction.addToBackStack(null);
            transaction.commit();
        } else if (tag.equalsIgnoreCase(GOALCHANGETAG)) {
            setImageIC(R.id.profile_ic);
            transaction.replace(R.id.fragment_container, goalChangeFrag, tag);
            transaction.addToBackStack(null);
            transaction.commit();
        } else if (tag.equalsIgnoreCase(ABOUTTAG)) {
            setImageIC(R.id.more_ic);
            transaction.replace(R.id.fragment_container, aboutFrag, tag);
            transaction.addToBackStack(null);
            transaction.commit();
        } else if (tag.equalsIgnoreCase(UPGRADETAG)) {
            setImageIC(R.id.more_ic);
            transaction.replace(R.id.fragment_container, upgradeFragment, tag);
            transaction.addToBackStack(null);
            transaction.commit();
        }else if (tag.equalsIgnoreCase(SUPPLEMENTSTAG)) {
            setImageIC(R.id.home_ic);
            transaction.replace(R.id.fragment_container, supplementsFrag, tag);
            transaction.addToBackStack(null);
            transaction.commit();
        } else if (tag.equalsIgnoreCase(GUIDELINESTAG)) {
            setImageIC(R.id.home_ic);
            transaction.replace(R.id.fragment_container, guidelinesFrag, tag);
            transaction.addToBackStack(null);
            transaction.commit();
        } else if (tag.equalsIgnoreCase(TERMSTAG)) {
            setImageIC(R.id.more_ic);
            transaction.replace(R.id.fragment_container, termsFragment, tag);
            transaction.addToBackStack(null);
            transaction.commit();
        } else if (tag.equalsIgnoreCase(CHANGEPWDTAG)) {
            setImageIC(R.id.more_ic);
            transaction.replace(R.id.fragment_container, changePasswordFragment, tag);
            transaction.addToBackStack(null);
            transaction.commit();
        } else if (tag.equalsIgnoreCase(WORKVIDEOTAG)) {
            setImageIC(R.id.more_ic);
            transaction.replace(R.id.fragment_container, workFragment, tag);
            transaction.addToBackStack(null);
            transaction.commit();
        } else if (tag.equalsIgnoreCase(MEALSLISTTAG)) {
            setImageIC(R.id.home_ic);
            transaction.replace(R.id.fragment_container, mealsListFrag, tag);
            transaction.addToBackStack(null);
            transaction.commit();
        } else if (tag.equalsIgnoreCase(SHOPPINGMEALPLANNERTAG)) {
            setImageIC(R.id.store_ic);
            transaction.replace(R.id.fragment_container, shoppingMealPlanner, tag);
            transaction.addToBackStack(null);
            transaction.commit();
        } else if (tag.equalsIgnoreCase(DELETESHOPTAG)) {
            setImageIC(R.id.home_ic);
            transaction.replace(R.id.fragment_container, deleteShopListFragment, tag);
            transaction.addToBackStack(null);
            transaction.commit();
        } else if (tag.equalsIgnoreCase(MORETAG)) {
            setImageIC(R.id.more_ic);
            transaction.replace(R.id.fragment_container, MoreFragment, tag);
            transaction.addToBackStack(null);
            transaction.commitAllowingStateLoss();
        } else if (tag.equalsIgnoreCase(EXTRAFOODTAG)) {
            setImageIC(R.id.home_ic);
            transaction.replace(R.id.fragment_container, ExtraFoodFragment, tag);
            transaction.addToBackStack(null);
            transaction.commit();
        } else if (tag.equalsIgnoreCase(MARKASEATENTAG)) {
            setImageIC(R.id.home_ic);
            transaction.replace(R.id.fragment_container, mainFragment, tag);
            transaction.addToBackStack(null);
            transaction.commit();
        } else if (tag.equalsIgnoreCase(YOURPROFILETAG)) {
            setImageIC(R.id.profile_ic);
            transaction.replace(R.id.fragment_container, yourProfileFragment, tag);
            transaction.addToBackStack(null);
            transaction.commit();
        }
    }


    public void responseReceived(String response) {

        mItemsBeanArrayList.clear();
        mTitleList.clear();

        try {
            JSONArray jsonArrayObject = new JSONArray(response);
            for (int i = 0; i < jsonArrayObject.length(); i++) {

                mPlanList = new PlanLists();
                JSONObject jsonObject = jsonArrayObject.getJSONObject(i);

                //  calories = jsonObject.getString("Category");
                if (!Utility.isBlank(jsonObject.getString(JsonConstants.FOOD_NAME))) {
                    mPlanList.setFoodName(jsonObject.getString(JsonConstants.FOOD_NAME));
                }
                if (!Utility.isBlank(jsonObject.getString(JsonConstants.CALORIES))) {
                    mPlanList.setCalories(jsonObject.getString(JsonConstants.CALORIES));
                }
                if (!Utility.isBlank(jsonObject.getString(JsonConstants.FAT))) {
                    mPlanList.setFat(jsonObject.getString(JsonConstants.FAT));
                }
                if (!Utility.isBlank(jsonObject.getString(JsonConstants.CARBOHYDRATE))) {
                    mPlanList.setCarbohydrate(jsonObject.getString(JsonConstants.CARBOHYDRATE));
                }
                if (!Utility.isBlank(jsonObject.getString(JsonConstants.PROTEIN))) {
                    mPlanList.setProtein(jsonObject.getString(JsonConstants.PROTEIN));
                }
                if (!Utility.isBlank(jsonObject.getString(JsonConstants.FIBER))) {
                    mPlanList.setFiber(jsonObject.getString(JsonConstants.FIBER));
                }
                if (!Utility.isBlank(jsonObject.getString(JsonConstants.CATEGORY))) {
                    mPlanList.setCategory(jsonObject.getString(JsonConstants.CATEGORY));
                }
                if (!Utility.isBlank(jsonObject.getString(JsonConstants.MEALTYPE))) {
                    mPlanList.setMealType(jsonObject.getString(JsonConstants.MEALTYPE));
                    if (!mTitleList.contains(mPlanList.getMealType())) {
                        if (mPlanList.getMealType().contains("Snack")) {
                            Log.e("Check", "Snack");
                        }
                        mTitleList.add(mPlanList.getMealType());
                    }
                }
                if (!Utility.isBlank(jsonObject.getString(JsonConstants.CALORIETYPE))) {
                    mPlanList.setCalorieType(jsonObject.getString(JsonConstants.CALORIETYPE));
                }
                if (!Utility.isBlank(jsonObject.getString(JsonConstants.PLANID))) {
                    mPlanList.setPlanID(jsonObject.getString(JsonConstants.PLANID));
                }
                if (!isSubCategoryBlank(jsonObject.getString(JsonConstants.SUBCATEGORY))) {
                    mPlanList.setSubCategory(jsonObject.getString(JsonConstants.SUBCATEGORY));
                }
                mItemsBeanArrayList.add(mPlanList);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static boolean isSubCategoryBlank(final String str) {
        int strLen;
        if (str == null || (strLen = str.length()) == 0) {
            return true;
        }
        for (int i = 0; i < strLen; i++) {
            if ((Character.isWhitespace(str.charAt(i)) == false)) {
                return false;
            }
        }
        return true;
    }

    public void extraFood_responseReceived(String response) {
        mExtraFoodsBeanArrayList.clear();

        try {

            JSONArray jsonArrayObject = new JSONArray(response);

            for (int i = 1; i < jsonArrayObject.length(); i++) {
                mPlanList = new PlanLists();
                JSONObject jsonObject = jsonArrayObject.getJSONObject(i);

                //  calories = jsonObject.getString("Category");
                if (!Utility.isBlank(jsonObject.getString(JsonConstants.FOOD_NAME))) {
                    mPlanList.setFoodName(jsonObject.getString(JsonConstants.FOOD_NAME));
                }
                if (!Utility.isBlank(jsonObject.getString(JsonConstants.CALORIES))) {
                    mPlanList.setCalories(jsonObject.getString(JsonConstants.CALORIES));
                }
                if (!Utility.isBlank(jsonObject.getString(JsonConstants.FAT))) {
                    mPlanList.setFat(jsonObject.getString(JsonConstants.FAT));
                }
                if (!Utility.isBlank(jsonObject.getString(JsonConstants.CARBOHYDRATE))) {
                    mPlanList.setCarbohydrate(jsonObject.getString(JsonConstants.CARBOHYDRATE));
                }
                if (!Utility.isBlank(jsonObject.getString(JsonConstants.PROTEIN))) {
                    mPlanList.setProtein(jsonObject.getString(JsonConstants.PROTEIN));
                }
                if (!Utility.isBlank(jsonObject.getString(JsonConstants.FIBER))) {
                    mPlanList.setFiber(jsonObject.getString(JsonConstants.FIBER));
                }
                if (!Utility.isBlank(jsonObject.getString(JsonConstants.CATEGORY))) {
                    mPlanList.setCategory(jsonObject.getString(JsonConstants.CATEGORY));
                }
                if (!Utility.isBlank(jsonObject.getString(JsonConstants.MEALPLANTYPE))) {
                    mPlanList.setMealPlanType(jsonObject.getString(JsonConstants.MEALPLANTYPE));
                }
                mExtraFoodsBeanArrayList.add(mPlanList);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {

            System.out.println("checking frag................" + getDetails);
            if (getDetails.equalsIgnoreCase("breakfast")) {
                Intent intent = getIntent();
                overridePendingTransition(0, 0);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                intent.putExtra("dashboard", "markeaten");
                finish();
                overridePendingTransition(0, 0);
                startActivity(intent);

            } else if (getDetails.equalsIgnoreCase("Shopping")) {
                Intent intent = getIntent();
                overridePendingTransition(0, 0);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                intent.putExtra("dashboard", "markeaten");
                finish();
                overridePendingTransition(0, 0);
                startActivity(intent);

            } else if (getDetails.equalsIgnoreCase("detailStoreMealTag")) {

                if (YourProfileFragment.profile_clicked_status) {

                    Intent intent = getIntent();
                    overridePendingTransition(0, 0);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    intent.putExtra("dashboard", "yourprofilefragment");
                    finish();
                    overridePendingTransition(0, 0);
                    startActivity(intent);

                    YourProfileFragment.profile_clicked_status = false;
                } else {
                    Intent intent = getIntent();
                    overridePendingTransition(0, 0);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    intent.putExtra("dashboard", "shoppingmealplannertag");
                    finish();
                    overridePendingTransition(0, 0);
                    startActivity(intent);
                }

            } else if (getDetails.equalsIgnoreCase("Supplements")) {
                Intent intent = getIntent();
                overridePendingTransition(0, 0);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                intent.putExtra("dashboard", "markeaten");
                finish();
                overridePendingTransition(0, 0);
                startActivity(intent);

            } else if (getDetails.equalsIgnoreCase("GuideLines")) {
                Intent intent = getIntent();
                overridePendingTransition(0, 0);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                intent.putExtra("dashboard", "markeaten");
                finish();
                overridePendingTransition(0, 0);
                startActivity(intent);

            } else if (getDetails.equalsIgnoreCase("termsandcondition")) {
                Intent intent = getIntent();
                overridePendingTransition(0, 0);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                intent.putExtra("dashboard", "moretag");
                finish();
                overridePendingTransition(0, 0);
                startActivity(intent);

            } else if (getDetails.equalsIgnoreCase("changepassword")) {
                Intent intent = getIntent();
                overridePendingTransition(0, 0);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                intent.putExtra("dashboard", "moretag");
                finish();
                overridePendingTransition(0, 0);
                startActivity(intent);

            } else if (getDetails.equalsIgnoreCase("worksvideo")) {

                Intent intent = getIntent();
                overridePendingTransition(0, 0);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                intent.putExtra("dashboard", "moretag");
                finish();
                overridePendingTransition(0, 0);
                startActivity(intent);
            } else if (getDetails.equalsIgnoreCase("abouttag")) {

                Intent intent = getIntent();
                overridePendingTransition(0, 0);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                intent.putExtra("dashboard", "moretag");
                finish();
                overridePendingTransition(0, 0);
                startActivity(intent);
            } else if (getDetails.equalsIgnoreCase("mealslist")) {
                Intent intent = getIntent();
                overridePendingTransition(0, 0);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                intent.putExtra("dashboard", "breakfast");
                finish();
                overridePendingTransition(0, 0);
                startActivity(intent);

            } else if (getDetails.equalsIgnoreCase("shoppingmealplannertag")) {
                finish();
            } else if (getDetails.equalsIgnoreCase("deleteshoptag")) {
                Intent intent = getIntent();
                overridePendingTransition(0, 0);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                intent.putExtra("dashboard", "Shopping");
                finish();
                overridePendingTransition(0, 0);
                startActivity(intent);
            } else if (getDetails.equalsIgnoreCase("moretag")) {
                finish();
            } else if (getDetails.equalsIgnoreCase("extrafoodtag")) {
                Intent intent = getIntent();
                overridePendingTransition(0, 0);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                intent.putExtra("dashboard", "breakfast");
                finish();
                overridePendingTransition(0, 0);
                startActivity(intent);

            } else if (getDetails.equalsIgnoreCase("markeaten")) {
                finish();
            } else if (getDetails.equalsIgnoreCase("foodList")) {

            } else if (getDetails.equalsIgnoreCase("changeprofile")) {

                Intent intent = getIntent();
                overridePendingTransition(0, 0);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                intent.putExtra("dashboard", "yourprofilefragment");
                finish();
                overridePendingTransition(0, 0);
                startActivity(intent);
            } else if (getDetails.equalsIgnoreCase("changebody")) {

                Intent intent = getIntent();
                overridePendingTransition(0, 0);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                intent.putExtra("dashboard", "yourprofilefragment");
                finish();
                overridePendingTransition(0, 0);
                startActivity(intent);
            } else if (getDetails.equalsIgnoreCase("changegoal")) {

                Intent intent = getIntent();
                overridePendingTransition(0, 0);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                intent.putExtra("dashboard", "yourprofilefragment");
                finish();
                overridePendingTransition(0, 0);
                startActivity(intent);
            } else if (getDetails.equalsIgnoreCase("yourprofilefragment")) {
                finish();
            } else if (getDetails.equalsIgnoreCase("")) {
                finish();
            }
        }
        return super.onKeyDown(keyCode, event);
    }

}
