package com.forager.meal.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.forager.meal.bean.PlanLists;
import com.forager.meal.ui.R;
import com.forager.meal.ui.activity.DashBoardActivity;

import java.util.ArrayList;
import java.util.HashMap;

public class SampleAdapter extends BaseExpandableListAdapter {



    private final Context mContext;
    private final LayoutInflater mLayoutInflater;
    DashBoardActivity response1=new DashBoardActivity();
    ArrayList<String> mGroupItems=new ArrayList<>();
    HashMap<String, ArrayList<PlanLists>> mChildItems= new HashMap<>();


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

    public SampleAdapter(Context context,ArrayList<String> menuItemCategory, HashMap<String, ArrayList<PlanLists>> menuItemsChild){
        mContext = context;
        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mGroupItems=menuItemCategory;
        mChildItems=menuItemsChild;

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
        if(convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.group_list_item, parent, false);
        }

		/*final ImageView image = (ImageView) convertView.findViewById(R.id.sample_activity_list_group_item_image);
		image.setImageResource(mGroupDrawables[groupPosition]);*/

        final TextView text = (TextView) convertView.findViewById(R.id.group_text);
        text.setText(mGroupItems.get(groupPosition));
//        ImageView image= (ImageView) convertView.findViewById(R.id.group_image);

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

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        if(convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.child_list_item, parent, false);
        }
        final PlanLists planLists = (PlanLists) getChild(groupPosition,childPosition);
        final TextView text = (TextView) convertView.findViewById(R.id.child_text);
        text.setText(planLists.getFoodName().toString());

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

}
