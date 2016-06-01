package com.forager.meal.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.forager.meal.bean.PlanLists;
import com.forager.meal.constants.AppConstants;
import com.forager.meal.ui.R;
import com.forager.meal.utitlity.Utility;

import java.util.ArrayList;

public class DeleteShoppingAdapter extends BaseAdapter {

    private final Context mContext;
    private final LayoutInflater mLayoutInflater;
    private SharedPreferences preferences;

    public static ArrayList<Integer> mDeletePositions;

    public DeleteShoppingAdapter(Context mContext) {
            this.mContext = mContext;
        this.mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        preferences = Utility.getSharedPreferences((Activity) mContext, AppConstants.SHARED_PREFES_SHOP_LIST_DETAILS);
        mDeletePositions = new ArrayList<>();
    }

    @Override
    public int getCount() {
        return ShoppingListAdapter.mDeleteShoppingList.size();
    }

    @Override
    public Object getItem(int position) {
        return ShoppingListAdapter.mDeleteShoppingList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        if(convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.shopping_child_list, parent, false);
        }
        final PlanLists planLists = ShoppingListAdapter.mDeleteShoppingList.get(position);
        final TextView text = (TextView) convertView.findViewById(R.id.child_text);
        text.setText(planLists.getFoodName().toString());
        text.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.shop_unselected_checkbox, 0);

        text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mDeletePositions.contains(position)) {
                    text.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.shop_selected_checkbox, 0);
                    mDeletePositions.add(position);
                } else {
                    text.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.shop_unselected_checkbox, 0);
                    mDeletePositions.remove((Object) position);
                }
            }
        });
        return convertView;
    }
}
