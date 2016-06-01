package com.forager.meal.ui.fragment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.forager.meal.adapter.ShopMealPlannerAdapter;
import com.forager.meal.application.MyApplication;
import com.forager.meal.bean.ShopMealPlan;
import com.forager.meal.constants.AppConstants;
import com.forager.meal.database.Database;
import com.forager.meal.ui.R;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by pickzy01 on 9/25/2015.
 */
public class ShoppingMealPlanner extends Fragment {


    private ListView shopMealList;
    private ShopMealPlannerAdapter mMealPlannerAdapter;

    ArrayList<String> folderList;

    HashMap<String, Bitmap> mHashMap = new HashMap<>();

    ArrayList<String> descriptions;

    Database db;

    ArrayList<ShopMealPlan> mList;

    private void findViews(View rootView) {
        PlanControllerFragment.flag=false;
        shopMealList = (ListView)rootView.findViewById(R.id.shop_meal_list);

        folderList = db.getPlanType();
        MyApplication.setContext(getActivity());

        descriptions = db.getDescription();
        //mList.clear();
        mList = formDatas(db.getShopMealPlan());

        mMealPlannerAdapter = new ShopMealPlannerAdapter(getActivity(),mList);
        shopMealList.setAdapter(mMealPlannerAdapter);
    }

    @Override
    public void onPause() {
        super.onPause();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_shop_meal_planner, container, false);
        db = Database.getInstance(getActivity());
        db.getMyWritableDatabase();
        findViews(rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        db.closeDatabase();
        super.onDestroyView();
    }
    private ArrayList<ShopMealPlan> formDatas(ArrayList<ShopMealPlan> mList) {
        //ArrayList<ShopMealPlan> mResult = new ArrayList<>();
        for(int i=0;i<folderList.size();i++) {
            File sourceFile = new File(AppConstants.baseDir + "/" + AppConstants.FORAGER_DIR + "/" + folderList.get(i) + "/" + AppConstants.SHOP_MEAL_PLAN_DIR + "/" + 1 + ".png");
          /*  if (sourceFile.exists()) {
                Bitmap myBitmap = BitmapFactory.decodeFile(sourceFile.getAbsolutePath());
                String detDesc = descriptions.get(i).toString();
                mHashMap.put(detDesc,myBitmap);
            }*/
            if (sourceFile.exists()) {
                ShopMealPlan shopMealPlan = mList.get(i);
                Bitmap myBitmap = BitmapFactory.decodeFile(sourceFile.getAbsolutePath());
                shopMealPlan.setBitmap(myBitmap);
            }
        }
        return mList;
    }
}
