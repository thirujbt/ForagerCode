package com.forager.meal.ui.fragment;

/**
 * Created by Chithra on 9/24/2015.
 */

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.forager.meal.application.MyApplication;
import com.forager.meal.constants.AppConstants;
import com.forager.meal.database.Database;
import com.forager.meal.ui.R;
import com.forager.meal.ui.activity.DashBoardActivity;
import com.forager.meal.utitlity.Utility;

import java.util.HashMap;

public class TermsAndConditionFragment extends Fragment implements View.OnClickListener {

    private WebView mTermsAndConditions;
    String termsText;
    Button mAgreeBtn;
    TextView txtTermsAndConditions;
    HashMap<String, String> mHashMap = new HashMap<>();
    Database db;
    ImageButton mBackbtn;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_termandconditon, container, false);
        PlanControllerFragment.flag=false;
        findViews(rootView);
        db = Database.getInstance(getActivity());
        db.getMyWritableDatabase();
        mTermsAndConditions = (WebView) rootView.findViewById(R.id.agree_content);
        mTermsAndConditions.setBackgroundColor(Color.TRANSPARENT);
        MyApplication.setContext(getActivity());
        txtTermsAndConditions = (TextView) rootView.findViewById(R.id.title_terms_conditions);
        txtTermsAndConditions.setTypeface(MyApplication.getFont());


        String termsText = Utility.getContentFromFile(getActivity(), AppConstants.TERMS_FILENAME);
        Spanned test = Html.fromHtml(termsText);

        mTermsAndConditions.loadData(Html.toHtml(test), "text/html; charset=UTF-8", null);

       /* try {
            JSONObject jsonObj = new JSONObject();
            jsonObj.put(JsonConstants.USER_ID, AppConstants.APP_USER_ID);
            jsonObj.put(JsonConstants.PLAN_ID, db.getActivePlanId());
            jsonObj.put(JsonConstants.CALORIE_TYPE, UserDetails.getInstance().getPlan_calorie());

            Utility.showPDialog(getActivity());

            System.out.println("response in terms and conditions............." + jsonObj);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
                new JsonAsync(this, jsonObj.toString(),).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, AppUrls.ACTIVE_PLAN_URL);
            else
                new JsonAsync(this, jsonObj.toString(),).execute(AppUrls.ACTIVE_PLAN_URL);


        } catch (Exception e) {
            e.printStackTrace();
        }*/
       /* mAgreeBtn = (Button)rootView.findViewById(R.id.agree_btn);
        mAgreeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               *//* android.support.v4.app.FragmentManager fm=getFragmentManager();
                FragmentTransaction ft=fm.beginTransaction();
                ft.replace(R.id.fragment_container,DashBoardActivity.class,"DashBoarActivity");
                ft.addToBackStack("DashBoardActivity");
                startActivity(new Intent(TermsAndConditions.this, DashBoardActivity.class));*//*
            }
        });*/
        return rootView;
    }


    private void findViews(View rootView) {
        mBackbtn = (ImageButton) rootView.findViewById(R.id.termsback);
        mBackbtn.setOnClickListener(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        db.closeDatabase();
    }



    /*@Override
    public void onResponseReceived(String response) {

        System.out.println("response in terms and conditions............." + response);

        Utility.dismissPDialog(this.getActivity());

        if (!Utility.isBlank(response)) {
            Log.e("Check Res in te and co", response);
            try {

                JSONObject mJsonObject = new JSONObject(response);
                String errorCode = mJsonObject.getString(JsonConstants.STATUS);

                if (errorCode.equals(AppConstants.STATUS_SUCCESS)) {

                    JSONObject dailyTips = mJsonObject.getJSONObject(JsonConstants.DAILYTIPS);
                    termsText = dailyTips.getString(JsonConstants.TERMS_CONDITIONS);
                    String aboutText = dailyTips.getString(JsonConstants.APP_ABOUT_US);
                    JSONArray aboutUsArray = dailyTips.getJSONArray(JsonConstants.APP_ABOUT_US_IMAGES);
                    for (int i = 0; i < aboutUsArray.length(); i++) {
                        mHashMap.put(aboutUsArray.get(i).toString(), AppConstants.ABOUT_DIR);
                    }
                    try {
                        FileOutputStream outputStream = getActivity().openFileOutput(AppConstants.TERMS_FILENAME, Context.MODE_PRIVATE);
                        outputStream.write(termsText.getBytes());
                        outputStream.close();

                        outputStream = getActivity().openFileOutput(AppConstants.ABOUT_FILENAME, Context.MODE_PRIVATE);
                        outputStream.write(aboutText.getBytes());
                        outputStream.close();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    File mFile = new File(AppConstants.baseDir + "/" + AppConstants.PROFILE_DIR + "/" + AppConstants.ABOUT_DIR);
                    int fileCount = 0;
                    if (mFile.exists()) {
                        fileCount = mFile.list().length;
                    }
                    if (fileCount != mHashMap.size()) {

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                            new DownloadImage(getActivity(), mHashMap).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                        } else {
                            new DownloadImage(getActivity(), mHashMap).execute();
                        }
                    } else {
                        mHashMap.clear();
                    }
                } else {
                    Utility.showAlert(getActivity(), AppConstants.SERVER_ERROR, false);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


        Spanned test = Html.fromHtml(termsText);

        mTermsAndConditions.loadData(Html.toHtml(test), "text/html; charset=UTF-8", null);
        mTermsAndConditions.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return true;
            }
        });


    }

    @Override
    public void onDownloadCompleted() {
        Utility.dismissPDialog(getActivity());
    }*/

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.termsback) {
            Intent supplementsIntent = new Intent(getActivity(), DashBoardActivity.class);
            getActivity().overridePendingTransition(0, 0);
            supplementsIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            supplementsIntent.putExtra("dashboard", "moretag");
            getActivity().finish();
            getActivity().overridePendingTransition(0, 0);
            getActivity().startActivity(supplementsIntent);
        }

    }

    public void onStop(){
        super.onStop();
        getActivity().finish();
    }
}