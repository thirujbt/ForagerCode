package com.forager.meal.ui.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.forager.meal.adapter.ShoppingListAdapter;
import com.forager.meal.adapter.WrapperExpandableListAdapter;
import com.forager.meal.application.MyApplication;
import com.forager.meal.bean.PlanLists;
import com.forager.meal.constants.AppConstants;
import com.forager.meal.constants.JsonConstants;
import com.forager.meal.database.Database;
import com.forager.meal.listener.OnShoppingButtonClick;
import com.forager.meal.ui.R;
import com.forager.meal.ui.activity.DashBoardActivity;
import com.forager.meal.utitlity.Utility;
import com.forager.meal.widget.FloatingGroupExpandableListView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

public class ShoppingFragment extends Fragment {

    public String data;
    FloatingGroupExpandableListView list;
    String res;
    PlanLists mPlanList;
    public ArrayList<PlanLists> mItemsBeanArrayList = new ArrayList<>();
    ScrollView scrollView;
    public static TextView mtxt_add_shopping, mtxt_go_shopping;
    ImageView back, forward;
    boolean item = false;
    String active_plan;
    Database mDatabase;
    ImageView meal_plan_logo;
    TextView title_shopping_list;
    int width;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_shopping_list, container, false);
        try {
            InputStream inputStreams = getActivity().openFileInput(AppConstants.GROCERYFOODPLAN);
            res = Utility.getStringFromInputStream(inputStreams);
            Log.e("Check File Value", res);
            responseReceived(res);

        } catch (Exception e) {
            e.printStackTrace();
        }

        DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
        width = metrics.widthPixels;

        init(rootView);
        store();
        return rootView;
    }

    public int GetPixelFromDips(float pixels) {
        // Get the screen's density scale
        final float scale = getResources().getDisplayMetrics().density;
        // Convert the dps to pixels, based on density scale
        return (int) (pixels * scale + 0.5f);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Display newDisplay = getActivity().getWindowManager().getDefaultDisplay();
        int width = newDisplay.getWidth();
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.JELLY_BEAN_MR2) {
            list.setIndicatorBounds(width - GetPixelFromDips(70), width - GetPixelFromDips(10));
        } else {
            list.setIndicatorBoundsRelative(width - GetPixelFromDips(70), width - GetPixelFromDips(10));
        }
    }

    private void init(View rootView) {
        PlanControllerFragment.flag=false;
        mDatabase = Database.getInstance(getActivity());
        mDatabase.getMyWritableDatabase();
        MyApplication.setContext(getActivity());
        title_shopping_list = (TextView) rootView.findViewById(R.id.title_shopping_list);
        title_shopping_list.setTypeface(MyApplication.getFont());

        list = (FloatingGroupExpandableListView) rootView.findViewById(R.id.myList);
        list.setChildDivider(new ColorDrawable(Color.parseColor("#C8C7CC")));
        scrollView = (ScrollView) rootView.findViewById(R.id.scrollView);
        mtxt_add_shopping = (TextView) rootView.findViewById(R.id.txt_add_shopping_list1);
        mtxt_add_shopping.setEnabled(false);
        mtxt_go_shopping = (TextView) rootView.findViewById(R.id.txt_go_shopping_list1);
        back = (ImageView) rootView.findViewById(R.id.back_body);
        forward = (ImageView) rootView.findViewById(R.id.forward_body);
        meal_plan_logo = (ImageView) rootView.findViewById(R.id.meal_plan_logo);

        active_plan = mDatabase.getActivePlanType();
        File sourceFile = new File(AppConstants.baseDir + "/" + AppConstants.FORAGER_DIR + "/" + active_plan + "/" + AppConstants.SHOP_MEAL_PLAN_LOGO_DIR, "1.png");

        if (sourceFile.exists()) {
            Bitmap myBitmap = BitmapFactory.decodeFile(sourceFile.getAbsolutePath());
            meal_plan_logo.setImageBitmap(myBitmap);
        }
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callBack();
            }
        });
        forward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onForwardClick();
            }
        });

        if (Utility.getSizeOfPrefes(getActivity()) > 0) {
               // mtxt_go_shopping.setVisibility(View.VISIBLE);
            forward.setVisibility(View.VISIBLE);
        }

        mtxt_add_shopping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mtxt_go_shopping.setVisibility(View.VISIBLE);
                mtxt_add_shopping.setVisibility(View.GONE);
                ShoppingListAdapter.addTempDataToDeleteList();
                Utility.storeToSharedPrefes(getActivity());
                // onShoppingButtonClick.onButtonClick();
                forward.setVisibility(View.VISIBLE);
                item = true;

                //ShoppingListAdapter.selected_item=false;

            }
        });

        mtxt_go_shopping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onForwardClick();
            }
        });
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

    OnShoppingButtonClick onShoppingButtonClick = new OnShoppingButtonClick() {
        @Override
        public void onButtonClick() {
            if (ShoppingListAdapter.mDeleteShoppingList.size() > 0) {
                //mtxt_add_shopping.setEnabled(true);
                mtxt_go_shopping.setVisibility(View.GONE);
                mtxt_add_shopping.setVisibility(View.VISIBLE);
                if (Utility.getSizeOfPrefes(getActivity()) > 0) {
                    forward.setVisibility(View.VISIBLE);
                }
            } else {
               // mtxt_add_shopping.setEnabled(false);
                forward.setVisibility(View.INVISIBLE);
            }
        }
    };

    public void store() {
        ArrayList<PlanLists> mItemsBeansCategory = new ArrayList<>();
        ArrayList<String> mealTypeList = new ArrayList<>();
        HashMap<String, ArrayList<PlanLists>> menuItemsChild = new HashMap<>();
        // PlanControllerFragment food =new PlanControllerFragment();
        for (int i = 0; i < mItemsBeanArrayList.size(); i++) {
            PlanLists mBean = mItemsBeanArrayList.get(i);
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
            }
            menuItemsChild.put(mealTypeList.get(i).toString(), mSubList);
        }

        ShoppingListAdapter adapter = new ShoppingListAdapter(getActivity(), mealTypeList, menuItemsChild, onShoppingButtonClick, mtxt_add_shopping);
        WrapperExpandableListAdapter wrapperAdapter = new WrapperExpandableListAdapter(adapter);
        list.setIndicatorBounds(width - GetPixelFromDips(50), width - GetPixelFromDips(10));
        list.setAdapter(wrapperAdapter);
    }

    private void onForwardClick() {
        Intent changebody = new Intent(getActivity(), DashBoardActivity.class);
        getActivity().overridePendingTransition(0, 0);
        changebody.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        changebody.putExtra("dashboard", "deleteshoptag");
        getActivity().finish();
        getActivity().overridePendingTransition(0, 0);
        getActivity().startActivity(changebody);
    }

    public void responseReceived(String response) {

        try {
            JSONArray jsonArrayObject = new JSONArray(response);

            for (int i = 0; i < jsonArrayObject.length(); i++) {

                mPlanList = new PlanLists();
                JSONObject jsonObject = jsonArrayObject.getJSONObject(i);

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
                mItemsBeanArrayList.add(mPlanList);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
