package com.forager.meal.ui.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.forager.meal.adapter.DeleteShoppingAdapter;
import com.forager.meal.adapter.ShoppingListAdapter;
import com.forager.meal.application.MyApplication;
import com.forager.meal.constants.AppConstants;
import com.forager.meal.database.Database;
import com.forager.meal.ui.R;
import com.forager.meal.ui.activity.DashBoardActivity;
import com.forager.meal.utitlity.Utility;

import java.io.File;

/**
 * Created by pickzy01 on 10/5/2015.
 */
public class DeleteShopListFragment extends Fragment {

    ListView mDeleteList;
    TextView mDeleteItemsText;
    ImageView meal_plan_logo;
    String active_plan;
    Database mDatabase;
    ImageView mBackButton;
    TextView mHeader;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.delete_shopping_fragment, container, false);

        init(rootView);

        return rootView;
    }

    private void init(View rootView) {

        mDatabase = Database.getInstance(getActivity());
        mDatabase.getMyWritableDatabase();
        MyApplication.setContext(getActivity());
        mDeleteList = (ListView) rootView.findViewById(R.id.deleteShoppingList);
        mHeader= (TextView)rootView.findViewById(R.id.title_delete_shopping_list);
        mHeader.setTypeface(MyApplication.getFont());
        mDeleteItemsText = (TextView) rootView.findViewById(R.id.txt_delete_list);


        mDeleteItemsText.setTypeface(MyApplication.getFont());
        meal_plan_logo = (ImageView) rootView.findViewById(R.id.meal_plan_logo);

        final DeleteShoppingAdapter deleteShoppingAdapter = new DeleteShoppingAdapter(getActivity());

        mDeleteList.setAdapter(deleteShoppingAdapter);

        mDeleteItemsText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utility.deleteFromPrefes(getActivity());
                ShoppingListAdapter.addPrefesToList();
                deleteShoppingAdapter.notifyDataSetChanged();
            }
        });

        mBackButton = (ImageView) rootView.findViewById(R.id.back_body);
        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent changebody = new Intent(getActivity(), DashBoardActivity.class);
                getActivity().overridePendingTransition(0, 0);
                changebody.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                changebody.putExtra("dashboard", "Shopping");
                getActivity().finish();
                getActivity().overridePendingTransition(0, 0);
                getActivity().startActivity(changebody);
            }
        });

        active_plan = mDatabase.getActivePlanType();
        File sourceFile = new File(AppConstants.baseDir + "/" + AppConstants.FORAGER_DIR + "/" + active_plan + "/" + AppConstants.SHOP_MEAL_PLAN_LOGO_DIR, "1.png");

        if (sourceFile.exists()) {
            Bitmap myBitmap = BitmapFactory.decodeFile(sourceFile.getAbsolutePath());
            meal_plan_logo.setImageBitmap(myBitmap);
        }

    }
}
