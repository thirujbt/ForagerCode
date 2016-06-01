package com.forager.meal.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.forager.meal.constants.AppConstants;
import com.forager.meal.ui.R;
import com.forager.meal.ui.activity.DashBoardActivity;
import com.forager.meal.utitlity.Utility;

/**
 * Created by Catherin on 10/14/2015.
 */
public class TitleListAdapter extends BaseAdapter {

    Context mContext;

    public static boolean mainChart_Status = false;

    public static boolean extraFoodStatus = false;

    public TitleListAdapter(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return DashBoardActivity.mTitleList.size();
    }

    @Override
    public String getItem(int position) {
        return DashBoardActivity.mTitleList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    static class ViewHolderItem {

        TextView textViewItem;
        ImageView icon;
        ImageView tick;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolderItem viewHolder;
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.group_title_list_item, null);
            viewHolder = new ViewHolderItem();
            viewHolder.textViewItem = (TextView) convertView.findViewById(R.id.group_title_text);
            viewHolder.icon = (ImageView) convertView.findViewById(R.id.group_title_image);
            viewHolder.tick = (ImageView) convertView.findViewById(R.id.group_tick_image);
            convertView.setTag(viewHolder);


        } else {
            viewHolder = (ViewHolderItem) convertView.getTag();
        }

        viewHolder.textViewItem.setText(getItem(position));


        //viewHolder.textViewItem.setGravity(Gravity.CENTER);


        if (isMarkEatenPrefesAvailable(getItem(position))) {
            if (getContent(getItem(position)))
                // viewHolder.textViewItem.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.tick,0);
                viewHolder.tick.setVisibility(View.VISIBLE);
            viewHolder.icon.setVisibility(View.GONE);
            mainChart_Status = true;
           // extraFoodStatus = true;

        }
        return convertView;
    }

    private boolean isMarkEatenPrefesAvailable(String mealType) {
        SharedPreferences preferences1 = Utility.getSharedPreferences(mContext, AppConstants.SHARED_PREFES_MARK_EATEN_DETAILS);
        if (preferences1.contains(mealType + "," + AppConstants.STATUS)) {
            return true;
        }
        return false;
    }

    private boolean getContent(String mealType) {
        SharedPreferences preferences1 = Utility.getSharedPreferences(mContext, AppConstants.SHARED_PREFES_MARK_EATEN_DETAILS);
        return preferences1.getBoolean(mealType + "," + AppConstants.STATUS, false);
    }
}
