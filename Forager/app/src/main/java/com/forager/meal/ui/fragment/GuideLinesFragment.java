package com.forager.meal.ui.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.forager.meal.application.MyApplication;
import com.forager.meal.constants.AppConstants;
import com.forager.meal.database.Database;
import com.forager.meal.ui.R;
import com.forager.meal.ui.activity.DashBoardActivity;

import java.io.File;

/**
 * Created by Catherin on 9/28/2015.
 */
public class GuideLinesFragment extends android.support.v4.app.Fragment {
    private TextView titleGuidelines;
    private ScrollView layoutGuidelinesContent;
    private WebView guidelinesContent;
    Database database;
    ImageView back;
    ImageView meal_plan_logo;
    String active_plan;
    Database mDatabase;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_guidelines, container, false);
        database = Database.getInstance(getActivity());
        database.getMyWritableDatabase();
        init(rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        database.closeDatabase();
        super.onDestroyView();
    }

    void callBack() {
        Intent shopping = new Intent(getActivity(), DashBoardActivity.class);
        getActivity().overridePendingTransition(0, 0);
        shopping.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        shopping.putExtra("dashboard", "markeaten");
        getActivity().finish();
        getActivity().overridePendingTransition(0, 0);
        getActivity().startActivity(shopping);
    }

    private void init(View rootView) {
        PlanControllerFragment.flag=false;
        mDatabase = Database.getInstance(getActivity());
        mDatabase.getMyWritableDatabase();
        back = (ImageView) rootView.findViewById(R.id.back_body);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              callBack();
            }
        });

        MyApplication.setContext(getActivity());
        meal_plan_logo=(ImageView) rootView.findViewById(R.id.meal_plan_logo);

        titleGuidelines = (TextView) rootView.findViewById(R.id.title_guideLines);
        titleGuidelines.setTypeface(MyApplication.getFont());

        active_plan =mDatabase.getActivePlanType();
        File sourceFile = new File(AppConstants.baseDir + "/" + AppConstants.FORAGER_DIR + "/" + active_plan+"/"+ AppConstants.SHOP_MEAL_PLAN_LOGO_DIR, "1.png");

        if (sourceFile.exists()) {
            Bitmap myBitmap = BitmapFactory.decodeFile(sourceFile.getAbsolutePath());
            meal_plan_logo.setImageBitmap(myBitmap);
        }

        guidelinesContent = (WebView) rootView.findViewById(R.id.guidelines_content);
        guidelinesContent.setBackgroundColor(Color.TRANSPARENT);
        String guidelinesText = database.getActivePlanGuideLines();
       /* PlanDetails mBean = SignInActivity.mPlanDetailsArrayList.get(0);
        String guidelinesText = mBean.getTips_guidelines();*/

        Spanned content = Html.fromHtml(guidelinesText);
        guidelinesContent.loadData(Html.toHtml(content), "text/html; charset=UTF-8", null);

    }
}





