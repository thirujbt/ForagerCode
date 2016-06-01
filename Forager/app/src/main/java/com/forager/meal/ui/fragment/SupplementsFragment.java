package com.forager.meal.ui.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import com.forager.meal.bean.PlanDetails;
import com.forager.meal.constants.AppConstants;
import com.forager.meal.database.Database;
import com.forager.meal.ui.R;
import com.forager.meal.ui.activity.DashBoardActivity;

import java.io.File;

/**
 * Created by Catherin on 9/28/2015.
 */
public class SupplementsFragment extends Fragment {

    private TextView titleSupplements;
    private ScrollView layoutSupplementContent;
    private WebView supplementContent;
    PlanDetails mPlanDetails = new PlanDetails();
    Database database;
    ImageView back;
    String active_plan;
    Database mDatabase;
    ImageView meal_plan_logo;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_supplements, container, false);
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
        titleSupplements = (TextView) rootView.findViewById(R.id.title_supplements);
        titleSupplements.setTypeface(MyApplication.getFont());
        supplementContent = (WebView) rootView.findViewById(R.id.supplement_content);
        supplementContent.setBackgroundColor(Color.TRANSPARENT);
        meal_plan_logo=(ImageView)rootView.findViewById(R.id.meal_plan_logo);

        active_plan =mDatabase.getActivePlanType();
        File sourceFile = new File(AppConstants.baseDir + "/" + AppConstants.FORAGER_DIR + "/" + active_plan+"/"+ AppConstants.SHOP_MEAL_PLAN_LOGO_DIR, "1.png");

        if (sourceFile.exists()) {
            Bitmap myBitmap = BitmapFactory.decodeFile(sourceFile.getAbsolutePath());
            meal_plan_logo.setImageBitmap(myBitmap);
        }
        //PlanDetails mBean = SignInActivity.mPlanDetailsArrayList.get(0);
        String supplementsText = database.getActivePlanSupplements();
        Spanned test = Html.fromHtml(supplementsText);
        supplementContent.loadData(Html.toHtml(test), "text/html; charset=UTF-8", null);
    }

}