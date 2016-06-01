package com.forager.meal.ui.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.forager.meal.adapter.MealsListItems;
import com.forager.meal.application.MyApplication;
import com.forager.meal.bean.PlanLists;
import com.forager.meal.constants.AppConstants;
import com.forager.meal.database.Database;
import com.forager.meal.ui.R;
import com.forager.meal.ui.activity.DashBoardActivity;
import com.forager.meal.utitlity.Utility;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;


public class MealsListFrag extends Fragment implements View.OnClickListener {
    MealsListItems adapter;
    ListView list;
    ArrayList<PlanLists> mSubList;
    TextView header;
    ImageView back_button, meal_logo;
    PlanControllerFragment food = new PlanControllerFragment();
    Database mDatabase;
    String active_plan;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.meals_list_items, container, false);
        init(rootView);
        return rootView;
    }

    private void init(View rootView) {
        PlanControllerFragment.flag = false;
        mDatabase = Database.getInstance(getActivity());
        mDatabase.getMyWritableDatabase();
        MyApplication.setContext(getActivity());
        list = (ListView) rootView.findViewById(R.id.meals_list_items);
        header = (TextView) rootView.findViewById(R.id.header_title);
        header.setTypeface(MyApplication.getFont());
        back_button = (ImageView) rootView.findViewById(R.id.image_back);
        meal_logo = (ImageView) rootView.findViewById(R.id.meal_imageplan);
        header.setText(food.mealType);
        ArrayList<PlanLists> mItemsBeansCategory = new ArrayList<>();
        ArrayList<String> mealTypeList = new ArrayList<>();
        ArrayList<String> mealItemChildList = new ArrayList<>();
        back_button.setOnClickListener(this);

        active_plan = mDatabase.getActivePlanType();
        File sourceFile = new File(AppConstants.baseDir + "/" + AppConstants.FORAGER_DIR + "/" + active_plan + "/" + AppConstants.SHOP_MEAL_PLAN_LOGO_DIR, "1.png");

        if (sourceFile.exists()) {
            Bitmap myBitmap = Utility.callPhotoUploadMethod(sourceFile.getAbsolutePath());
            meal_logo.setImageBitmap(myBitmap);
        }
        HashMap<String, ArrayList<PlanLists>> menuItemsChild = new HashMap<>();

        for (int i = 0; i < DashBoardActivity.mItemsBeanArrayList.size(); i++) {
            PlanLists mBean = DashBoardActivity.mItemsBeanArrayList.get(i);

            if (mBean.getMealType().equalsIgnoreCase(food.mealType)) {
                mItemsBeansCategory.add(mBean);

                if (!mealTypeList.contains(mBean.getCategory())) {
                    mealTypeList.add(mBean.getCategory());
                }
            }
        }
        mSubList = new ArrayList<>();

        // for (int i = 0; i < mealTypeList.size(); i++) {

        for (PlanLists planLists : mItemsBeansCategory) {
               /* if (planLists.getCategory().toString().equalsIgnoreCase(mealTypeList.get(i).toString())) {
                    mSubList.add(planLists);
                }*/
            if (planLists.getCategory().toString().equalsIgnoreCase(BreakFast_Activity.CURRENTTAG)) {
                mSubList.add(planLists);
            }
        }
        // menuItemsChild.put(mealTypeList.get(i).toString(), mSubList);
        //}
        StringBuilder sb = new StringBuilder();
        adapter = new MealsListItems(getActivity(), mSubList, dismissScreen);
        //final WrapperExpandableListAdapter wrapperAdapter = new WrapperExpandableListAdapter(adapter);
        list.setAdapter(adapter);
    }

    MealsListItems.DismissScreen dismissScreen = new MealsListItems.DismissScreen() {
        @Override
        public void closeExtendedScreen() {
            /*FragmentManager fm = getFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.fragment_container, new BreakFast_Activity(), "breakfasttag");
            fm.popBackStack("dashboard", 0);
            ft.commit();*/

            Intent mealsList = new Intent(getActivity(), DashBoardActivity.class);
            getActivity().overridePendingTransition(0, 0);
            mealsList.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            mealsList.putExtra("dashboard", "breakfast");
            getActivity().finish();
            getActivity().overridePendingTransition(0, 0);
            getActivity().startActivity(mealsList);
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.image_back:
                Intent mealsList = new Intent(getActivity(), DashBoardActivity.class);
                getActivity().overridePendingTransition(0, 0);
                mealsList.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                mealsList.putExtra("dashboard", "breakfast");
                getActivity().finish();
                getActivity().overridePendingTransition(0, 0);
                getActivity().startActivity(mealsList);
        }
    }
}

