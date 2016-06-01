package com.forager.meal.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.forager.meal.bean.PlanLists;
import com.forager.meal.constants.AppConstants;
import com.forager.meal.listener.OnShoppingButtonClick;
import com.forager.meal.ui.R;
import com.forager.meal.ui.fragment.ShoppingFragment;
import com.forager.meal.utitlity.Utility;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class ShoppingListAdapter extends BaseExpandableListAdapter {

    private final Context mContext;
    private final LayoutInflater mLayoutInflater;
    ArrayList<String> mGroupItems = new ArrayList<>();
    static HashMap<String, ArrayList<PlanLists>> mChildItems = new HashMap<>();

    public static ArrayList<PlanLists> mDeleteShoppingList;

    public static ArrayList<PlanLists> mTempDeleteShoppingList;

    private static SharedPreferences preferences;

    OnShoppingButtonClick mOnShoppingButtonClick;

    public static boolean selected_item = false;

    ImageView iconItem;

    TextView add_button;

    public HashMap<Integer, Integer[]> mChildCheckStates;

    // public String[] mGroups=response1.string_array;
    /*private final String[] mGroups = {"Cupcake",
            "Donut",
			"Eclair",
			"Froyo",
			"Gingerbread",
			"Honeycomb",
			"Ice Cream Sandwich",
			"Jelly Bean",
			"KitKat"
	};*/


    /*private final String[][] mChilds = {
            {"1.5","2.3","2.3.1","2.3.2","2.3.3","2.3.4","2.3.5","2.3.6","2.3.7"},
            {"1.6","2.3","2.3.1","2.3.2","2.3.3","2.3.4","2.3.5","2.3.6","2.3.7"},
            {"2.0","2.0.1","2.1","2.3","2.3.1","2.3.2","2.3.3","2.3.4","2.3.5","2.3.6","2.3.7"},
            {"2.2","2.2.1","2.2.2","2.2.3","2.3","2.3.1","2.3.2","2.3.3","2.3.4","2.3.5","2.3.6","2.3.7"},
            {"2.3","2.3.1","2.3.2","2.3.3","2.3.4","2.3.5","2.3.6","2.3.7","2.3","2.3.1","2.3.2","2.3.3","2.3.4","2.3.5","2.3.6","2.3.7"},
            {"3.0","3.1","3.2","3.2.1","3.2.2","3.2.3","3.2.4","3.2.5","3.2.6","2.3","2.3.1","2.3.2","2.3.3","2.3.4","2.3.5","2.3.6","2.3.7"},
            {"4.0", "4.0.1", "4.0.2", "4.0.3", "4.0.4","2.3","2.3.1","2.3.2","2.3.3","2.3.4","2.3.5","2.3.6","2.3.7"},
            {"4.1", "4.1.1", "4.1.2", "4.2", "4.2.1", "4.2.2", "4.3", "4.3.1","2.3","2.3.1","2.3.2","2.3.3","2.3.4","2.3.5","2.3.6","2.3.7"},
            {"4.4","2.3","2.3.1","2.3.2","2.3.3","2.3.4","2.3.5","2.3.6","2.3.7"}
    };
*/
    public ShoppingListAdapter(Context context, ArrayList<String> menuItemCategory, HashMap<String, ArrayList<PlanLists>> menuItemsChild, OnShoppingButtonClick buttonClick, TextView add_btn) {
        mContext = context;
        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mGroupItems = menuItemCategory;
        mChildItems = menuItemsChild;
        preferences = Utility.getSharedPreferences((Activity) mContext, AppConstants.SHARED_PREFES_SHOP_LIST_DETAILS);
        mDeleteShoppingList = new ArrayList<>();
        mTempDeleteShoppingList = new ArrayList<>();
        mOnShoppingButtonClick = buttonClick;
        add_button = add_btn;
        addPrefesToList();

        mChildCheckStates = new HashMap<>();
    }

    /*public ShoppingListAdapter(Context context,ArrayList<String> menuItemCategory, HashMap<String, ArrayList<PlanLists>> menuItemsChild){
        mContext = context;
        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mGroupItems=menuItemCategory;
        mChildItems=menuItemsChild;
        preferences = Utility.getSharedPreferences((Activity) mContext, AppConstants.SHARED_PREFES_SHOP_LIST_DETAILS);
        mOnShoppingButtonClick = null;
        mDeleteShoppingList = new ArrayList<>();
        addPrefesToList();
    }*/

    public static void addPrefesToList() {
        mDeleteShoppingList.clear();
        for (Map.Entry<String, ArrayList<PlanLists>> entry : mChildItems.entrySet()) {
            ArrayList<PlanLists> value = entry.getValue();
            for (PlanLists planLists : value) {
                if (isPlanListPrefesAvailable(planLists)) {
                    if (!mDeleteShoppingList.contains(planLists))
                        mDeleteShoppingList.add(planLists);
                }
            }
        }
    }

    public static void addTempDataToDeleteList() {
        mDeleteShoppingList.addAll(mTempDeleteShoppingList);
        mTempDeleteShoppingList.clear();
    }

    @Override
    public int getGroupCount() {
        return mGroupItems.size();
    }

    @Override
    public Object getGroup(int groupPosition) {

        return mGroupItems.get(groupPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.group_list_item, parent, false);
        }

		/*final ImageView image = (ImageView) convertView.findViewById(R.id.sample_activity_list_group_item_image);
        image.setImageResource(mGroupDrawables[groupPosition]);*/

        final TextView text = (TextView) convertView.findViewById(R.id.group_text);
        text.setText(mGroupItems.get(groupPosition));
        iconItem = (ImageView) convertView.findViewById(R.id.group_title_image);

        iconItem.setVisibility(View.GONE);
        //ImageView image= (ImageView) convertView.findViewById(R.id.group_image);

      /*  if(isExpanded) {
            RotateAnimation rotate = new RotateAnimation(0, 90, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            rotate.setDuration(500);
            rotate.setInterpolator(new LinearInterpolator());



            image.startAnimation(rotate);
        } else {
            RotateAnimation rotate = new RotateAnimation(0, 0, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            rotate.setDuration(500);
            rotate.setInterpolator(new LinearInterpolator());
            image.startAnimation(rotate);
        }*/
		
	/*	final ImageView expandedImage = (ImageView) convertView.findViewById(R.id.sample_activity_list_group_expanded_image);
		final int resId = isExpanded ? R.drawable.minus : R.drawable.plus;
		expandedImage.setImageResource(resId);*/

        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {

        return mChildItems.get(mGroupItems.get(groupPosition)).size();
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {

        return this.mChildItems.get(this.mGroupItems.get(groupPosition)).get(childPosition);
    }

    class childViewHolderItem {

        TextView textViewItem;
        ImageView icon;

    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final childViewHolderItem viewHolder;
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.shopping_child_list, parent, false);
            viewHolder = new childViewHolderItem();
            viewHolder.textViewItem = (TextView) convertView.findViewById(R.id.child_text);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (childViewHolderItem) convertView.getTag();
        }

        final PlanLists planLists = (PlanLists) getChild(groupPosition, childPosition);
        viewHolder.textViewItem = (TextView) convertView.findViewById(R.id.child_text);
        viewHolder.textViewItem.setText(planLists.getFoodName().toString());
        if (isPlanListPrefesAvailable(planLists) || isListContains(groupPosition, childPosition)) {
            viewHolder.textViewItem.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.shop_selected_checkbox, 0);
        } else {
            viewHolder.textViewItem.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.shop_unselected_checkbox, 0);
        }
        viewHolder.textViewItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mDeleteShoppingList.contains(planLists) || mTempDeleteShoppingList.contains(planLists)) {
                    viewHolder.textViewItem.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.shop_unselected_checkbox, 0);
                    if(mDeleteShoppingList.contains(planLists))
                    mDeleteShoppingList.remove(planLists);
                    if(mTempDeleteShoppingList.contains(planLists))
                        mTempDeleteShoppingList.remove(planLists);
                   if(isListContains(groupPosition, childPosition)) {
                       ArrayList<Integer> childPos = new ArrayList<>(Arrays.asList(mChildCheckStates.get(groupPosition)));
                       childPos.remove((Object) childPosition);
                       mChildCheckStates.put(groupPosition, childPos.toArray(new Integer[childPos.size()]));
                   }
                 //   add_button.setEnabled(false);
                    ShoppingFragment.mtxt_add_shopping.setEnabled(false);
                } else {
                    viewHolder.textViewItem.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.shop_selected_checkbox, 0);
                   // add_button.setEnabled(true);
                    ShoppingFragment.mtxt_add_shopping.setEnabled(true);

                    add_button.setBackgroundColor(Color.parseColor("#ff63c036"));
                    //mDeleteShoppingList.add(planLists);

                    mTempDeleteShoppingList.add(planLists);
                    if(!isListContains(groupPosition, childPosition)) {
                        if(mChildCheckStates.containsKey(groupPosition)) {
                            ArrayList<Integer> childPos = new ArrayList<>(Arrays.asList(mChildCheckStates.get(groupPosition)));
                            childPos.add(childPosition);
                            mChildCheckStates.put(groupPosition, childPos.toArray(new Integer[childPos.size()]));
                        } else {
                            mChildCheckStates.put(groupPosition, new Integer[] {childPosition});
                        }
                    }

                    if (mOnShoppingButtonClick != null) {
                        mOnShoppingButtonClick.onButtonClick();
                    }
                }
            }
        });
        return convertView;
    }

    private boolean isListContains(int groupPos, int childPosition) {
        if(mChildCheckStates.containsKey(groupPos)) {
            ArrayList<Integer> childPos = new ArrayList<>(Arrays.asList(mChildCheckStates.get(groupPos)));
            if(childPos.contains(childPosition)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

    private static boolean isPlanListPrefesAvailable(PlanLists planLists) {
        String planListKey = Utility.getPlanListKey(planLists);
        if (preferences.contains(planListKey)) {
            return true;
        }
        return false;
    }
}
