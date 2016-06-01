package com.forager.meal.adapter;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.forager.meal.bean.ShopMealPlan;
import com.forager.meal.constants.AppConstants;
import com.forager.meal.ui.R;
import com.forager.meal.ui.activity.DashBoardActivity;

import java.util.ArrayList;

/**
 * Created by pickzy01 on 9/25/2015.
 */
public class ShopMealPlannerAdapter extends BaseAdapter {

    Activity context;
    //HashMap<String, Bitmap> mShopMeal;
    // int activated;
    LayoutInflater mLayoutInflater;
    ArrayList<ShopMealPlan> mealPlanArrayList;
    // String[] keys;

    public static ShopMealPlan shopMealPlan;

    public ShopMealPlannerAdapter(Activity context, ArrayList<ShopMealPlan> shopMealPlans) {
        this.context = context;
        //this.mShopMeal = map;
        // this.activated = activated;
        mLayoutInflater = context.getLayoutInflater();
        //  keys = mShopMeal.keySet().toArray(new String[mShopMeal.size()]);
        mealPlanArrayList = shopMealPlans;
    }

    @Override
    public int getCount() {
        return mealPlanArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return mealPlanArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHolder mViewHolder;

        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.shop_meal_row, parent, false);
            mViewHolder = new ViewHolder();
            mViewHolder.mShopMealImage = (ImageView) convertView.findViewById(R.id.meal_plan_img);
            mViewHolder.mActivatedImage = (ImageView) convertView.findViewById(R.id.activated_img);
            mViewHolder.mDescriptionText = (WebView) convertView.findViewById(R.id.meal_plan_desc_txt);
            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (ViewHolder) convertView.getTag();
        }

        final ShopMealPlan mealPlan = mealPlanArrayList.get(position);

        if (mealPlan.getActive_status() == 1) {
            mViewHolder.mActivatedImage.setImageResource(R.drawable.activated_btn);
        } else {
            mViewHolder.mActivatedImage.setImageResource(R.drawable.not_activate_btn);
        }

        /*if(position == activated) {
            mViewHolder.mActivatedImage.setImageResource(R.drawable.activated_btn);
        } else{
            mViewHolder.mActivatedImage.setImageResource(R.drawable.not_activate_btn);
        }*/

      /*  String key = keys[position];
        Bitmap value = (Bitmap) getItem(position);*/

        Spanned test = Html.fromHtml(mealPlan.getDesc());
        mViewHolder.mDescriptionText.setBackgroundColor(Color.TRANSPARENT);
        mViewHolder.mDescriptionText.loadData(Html.toHtml(test), "text/html; charset=UTF-8", null);
        mViewHolder.mShopMealImage.setImageBitmap(mealPlan.getBitmap());
       /* mViewHolder.mDescriptionText.setFocusable(false);
        mViewHolder.mDescriptionText.setClickable(false);
        mViewHolder.mDescriptionText.setEnabled(false);
        mViewHolder.mDescriptionText.setFocusableInTouchMode(false);*/


        mViewHolder.mDescriptionText.setOnTouchListener(new View.OnTouchListener() {

            public final static int FINGER_RELEASED = 0;
            public final static int FINGER_TOUCHED = 1;
            public final static int FINGER_DRAGGING = 2;
            public final static int FINGER_UNDEFINED = 3;

            private int fingerState = FINGER_RELEASED;

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()) {

                    case MotionEvent.ACTION_DOWN:
                        if (fingerState == FINGER_RELEASED) fingerState = FINGER_TOUCHED;
                        else fingerState = FINGER_UNDEFINED;
                        break;

                    case MotionEvent.ACTION_UP:
                        if (fingerState != FINGER_DRAGGING || fingerState == FINGER_DRAGGING) {
                            fingerState = FINGER_RELEASED;

                            startDetailsActivity(mealPlan);
                        } else fingerState = FINGER_UNDEFINED;
                        break;

                    case MotionEvent.ACTION_MOVE:
                        if (fingerState == FINGER_TOUCHED || fingerState == FINGER_DRAGGING)
                            fingerState = FINGER_DRAGGING;
                        else fingerState = FINGER_UNDEFINED;
                        break;

                    default:
                        fingerState = FINGER_UNDEFINED;

                }

                return false;
            }
        });

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences prefs = context.getSharedPreferences(AppConstants.PURCHASE_STATUS, context.MODE_PRIVATE);

                String name = prefs.getString(AppConstants.IN_APP, "");
                String idName = prefs.getString(AppConstants.SUBSCRIPTION, "");

                if (name.equals("0")) {

                } else {
                    startDetailsActivity(mealPlan);
                }


            }
        });


        return convertView;
    }

    private void startDetailsActivity(ShopMealPlan mealPlan) {
        shopMealPlan = mealPlan;
        Intent changeabout = new Intent(context, DashBoardActivity.class);
        changeabout.putExtra("dashboard", "detailStoreMealTag");
        context.startActivity(changeabout);
        context.finish();

    }

    class ViewHolder {
        ImageView mShopMealImage;
        ImageView mActivatedImage;
        WebView mDescriptionText;

    }

}
