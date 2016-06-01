package com.forager.meal.ui.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.forager.meal.adapter.ShopMealPlannerAdapter;
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
import com.forager.meal.ui.activity.DashBoardActivity;
import com.forager.meal.utitlity.Utility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by pickzy01 on 9/28/2015.
 */
public class ShopMealDetailFragment extends Fragment implements View.OnClickListener, OnWebServiceResponse, OnImageDownloadCompleted {
    ImageButton home;
    private ImageButton back;
    private ImageView titleImage;
    private WebView mealPlanDetDesc;
    private Button activePlan;
    private Button activatePlan;
    Database mDatabase;
    HashMap<String, String> mHashMap = new HashMap<>();
    private String[] arrayValues;
    private String[] array = {AppConstants.PLANLISTS_FILENAME, AppConstants.EXTRAFOODPLAN, AppConstants.GROCERYFOODPLAN};
    public static int plan_id;
    public ArrayList<PlanDetails> mPlanDetailsArrayList = new ArrayList<>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.shop_det_desc, container, false);
        mDatabase = Database.getInstance(getActivity());
        mDatabase.getMyWritableDatabase();
        findViews(rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        mDatabase.closeDatabase();
        super.onDestroyView();
    }

    private void findViews(View rootView) {
        PlanControllerFragment.flag=false;
        back = (ImageButton) rootView.findViewById(R.id.back);
        titleImage = (ImageView) rootView.findViewById(R.id.title_image);
        mealPlanDetDesc = (WebView) rootView.findViewById(R.id.meal_plan_det_desc);
        activePlan = (Button) rootView.findViewById(R.id.active_plan);
        activatePlan = (Button) rootView.findViewById(R.id.activate_plan);

        back.setOnClickListener(this);
        titleImage.setOnClickListener(this);
        activePlan.setOnClickListener(this);
        activatePlan.setOnClickListener(this);

        titleImage.setImageBitmap(ShopMealPlannerAdapter.shopMealPlan.getBitmap());
        Spanned test = Html.fromHtml(ShopMealPlannerAdapter.shopMealPlan.getDesc());
        mealPlanDetDesc.setBackgroundColor(Color.TRANSPARENT);
        mealPlanDetDesc.loadData(Html.toHtml(test), "text/html; charset=UTF-8", null);

        if (ShopMealPlannerAdapter.shopMealPlan.getActive_status() == 1) {
            activePlan.setVisibility(View.VISIBLE);
            activatePlan.setVisibility(View.INVISIBLE);
        } else {
            activePlan.setVisibility(View.INVISIBLE);
            activatePlan.setVisibility(View.VISIBLE);
        }
    }

   /* public void onResume() {
        super.onResume();
        mealPlanDetDesc.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        // Toast.makeText(getActivity(), "back press", Toast.LENGTH_SHORT).show();
                        if (YourProfileFragment.profile_clicked_status) {
                           *//* FragmentManager fm = getFragmentManager();
                            FragmentTransaction ft = fm.beginTransaction();
                            ft.replace(R.id.fragment_container, new YourProfileFragment(), "MoreFragment");
                            fm.popBackStack("MoreFragment", 0);
                            ft.commit();*//*

                            Intent mealsList = new Intent(getActivity(), DashBoardActivity.class);
                            getActivity().overridePendingTransition(0, 0);
                            mealsList.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                            mealsList.putExtra("dashboard", "yourprofilefragment");
                            getActivity().finish();
                            getActivity().overridePendingTransition(0, 0);
                            getActivity().startActivity(mealsList);

                            YourProfileFragment.profile_clicked_status = false;
                        } else {
                            callBack();
                        }
                        return true;
                    }
                }
                return false;
            }
        });

    }*/
    private void callBack() {

        Intent mealsList = new Intent(getActivity(), DashBoardActivity.class);
        getActivity().overridePendingTransition(0, 0);
        mealsList.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        mealsList.putExtra("dashboard", "shoppingmealplannertag");
        getActivity().finish();
        getActivity().overridePendingTransition(0, 0);
        getActivity().startActivity(mealsList);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
               /* SharedPreferences preference = Utility.getSharedPreferences(getActivity(), AppConstants.SHARED_PREFES_PROFILE_CLICKED);
                boolean profile_clicked_status=preference.getBoolean(AppConstants.PROFILE_CLICKED,false);*/
                if(YourProfileFragment.profile_clicked_status)
                {
                    Intent mealsList = new Intent(getActivity(), DashBoardActivity.class);
                    getActivity().overridePendingTransition(0, 0);
                    mealsList.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    mealsList.putExtra("dashboard", "yourprofilefragment");
                    getActivity().finish();
                    getActivity().overridePendingTransition(0, 0);
                    getActivity().startActivity(mealsList);
                    YourProfileFragment.profile_clicked_status=false;
                }
                else
                callBack();
                break;
            case R.id.activate_plan:
                SharedPreferences preferences = Utility.getSharedPreferences(getActivity(), AppConstants.SHARED_PREFES_MARK_EATEN_DETAILS);
                boolean stored_preference= preferences.getBoolean(AppConstants.ALREADY_STORED_PREFERENCE,false);
               if(stored_preference)
                openAlert();
                else
                changeActivePlan();
                break;
        }
    }

    private void openAlert() {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity(), R.style.CustomAlertDialogStyle);
        alertDialogBuilder.setMessage("Changing your diet style now will reset your Meal Plan for the day.Is this OK ?");

        alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                changeActivePlan();

            }
        });

        alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

    }


    public void changeActivePlan()
    {
        Utility.resetFood(getActivity());
        plan_id = ShopMealPlannerAdapter.shopMealPlan.getId();
        mDatabase.changeActivePlanId(plan_id);

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
                new JsonAsync(ShopMealDetailFragment.this, jsonObj.toString(), AppConstants.UPDATE_ACTIVE_CALL).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, AppUrls.ACTIVE_PLAN_URL);
            else
                new JsonAsync(ShopMealDetailFragment.this, jsonObj.toString(), AppConstants.UPDATE_ACTIVE_CALL).execute(AppUrls.ACTIVE_PLAN_URL);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    @Override
    public void onResponseReceived(String response, String methodName) {

        Log.e("Response", response);
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
                new DownloadImage(ShopMealDetailFragment.this, mHashMap, AppConstants.ABOUT_IMAGE_CALL).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            } else {
                new DownloadImage(ShopMealDetailFragment.this, mHashMap, AppConstants.ABOUT_IMAGE_CALL).execute();
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
            Intent intent = new Intent(getActivity(), DashBoardActivity.class);
            startActivity(intent);
            getActivity().finish();
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

                new DownloadImage(ShopMealDetailFragment.this, mHashMap, AppConstants.PROFILE_PLAN_IMAGE_CALL).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            else
                new DownloadImage(ShopMealDetailFragment.this, mHashMap, AppConstants.PROFILE_PLAN_IMAGE_CALL).execute();

        } else if (methodName.equalsIgnoreCase(AppConstants.PROFILE_PLAN_IMAGE_CALL)) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
                new JsonFileAsync(getActivity(), arrayValues, fileDownloadComplete).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            else
                new JsonFileAsync(getActivity(), arrayValues, fileDownloadComplete).execute();
        }
    }


    public void onStop(){
        super.onStop();
        getActivity().finish();
    }
}