package com.forager.meal.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.forager.meal.bean.PlanLists;
import com.forager.meal.constants.AppConstants;
import com.forager.meal.ui.R;
import com.forager.meal.ui.fragment.BreakFast_Activity;
import com.forager.meal.ui.fragment.ExtraFoodFragment;
import com.forager.meal.ui.fragment.PlanControllerFragment;
import com.forager.meal.utitlity.Utility;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;


public class ExtraFoodsAdapter extends BaseExpandableListAdapter {

    private final Context mContext;
    private final LayoutInflater mLayoutInflater;

    ArrayList<String> mGroupItems = new ArrayList<>();
    HashMap<String, ArrayList<PlanLists>> mChildItems = new HashMap<>();

    private static SharedPreferences preferences;

    private final PopupWindow pwindo;

    private static final float MAX_LOSE_CALO = 120;
    PlanControllerFragment food = new PlanControllerFragment();

    PopupWindow popup;

    String mTitle;
    private TextView textViewItem;
    private ImageView iconItem, tick_icon;

    public float calories, carbs, protein, fiber, fats;

    public static boolean extraFoodStatus = false;

    public HashMap<Integer, Integer[]> mChildCheckStates;

    public ExtraFoodsAdapter(Context context, ArrayList<String> menuItemCategory, HashMap<String, ArrayList<PlanLists>> menuItemsChild, String title) {
        mContext = context;
        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mGroupItems = menuItemCategory;
        mChildItems = menuItemsChild;
        mTitle = title;
        preferences = Utility.getSharedPreferences(mContext, AppConstants.SHARED_PREFES_SHOP_LIST_DETAILS);
        pwindo = new PopupWindow(mContext);
        //addPrefesToList();
        mChildCheckStates = new HashMap<>();
    }

    @Override
    public int getGroupCount() {
        return mGroupItems.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return mChildItems.get(mGroupItems.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return mGroupItems.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return this.mChildItems.get(this.mGroupItems.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    /* static class GroupViewHolderItem {

         TextView textViewItem;
         ImageView icon;

     }*/
    class childViewHolderItem {
        TextView textViewItem1;
        ImageView icon1;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.group_list_item, parent, false);
            textViewItem = (TextView) convertView.findViewById(R.id.group_text);
            iconItem = (ImageView) convertView.findViewById(R.id.group_title_image);
            tick_icon = (ImageView) convertView.findViewById(R.id.group_tick_image);


            iconItem.setVisibility(View.GONE);

  /*  if (TitleListAdapter.extraFoodStatus) {

                //  iconItem.setVisibility(View.INVISIBLE);
                ExtraFoodFragment.extraFood_List.setGroupIndicator(null);
                tick_icon.setVisibility(View.VISIBLE);
                // iconItem.setVisibility(View.GONE);
            } */

            if (isMarkEatenPrefesAvailable(ListAdapter.title)) {
                if (getContent(ListAdapter.title))
                    // viewHolder.textViewItem.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.tick,0);
                    ExtraFoodFragment.extraFood_List.setGroupIndicator(null);
                tick_icon.setVisibility(View.VISIBLE);
                iconItem.setVisibility(View.GONE);
                extraFoodStatus = true;
            } else {
                // iconItem.setVisibility(View.VISIBLE);
                //              ExtraFoodFragment.extraFood_List.setGroupIndicator(R.drawable.expandable_animation_slelctor););

                ExtraFoodFragment.extraFood_List.setGroupIndicator(ContextCompat.getDrawable(mContext, R.drawable.expandable_animation_slelctor));

                tick_icon.setVisibility(View.GONE);
            }
        }

        String str = "";
        int childCount = getChildrenCount(groupPosition);
        for (int i = 0; i < childCount; i++) {

            if (isPrefContainsExtraFood(food.mealType, (PlanLists) getChild(groupPosition, i))) {
                if (!str.equalsIgnoreCase("")) {
                    str += " and ";
                }
                str += getContentFromPrefes(food.mealType, (PlanLists) getChild(groupPosition, i));

            }
        }

        if (!str.equalsIgnoreCase(""))
            textViewItem.setText(str);
        else {
            textViewItem.setText(mGroupItems.get(groupPosition));
        }


        return convertView;
    }

    @Override
    public void onGroupExpanded(int groupPosition) {

        Object obj = this.getGroup(groupPosition);

     /*  Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.rotate_image);
        image.startAnimation(animation);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                image.setImageResource(R.drawable.right_reverse_arrow);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
*/
    }


    @Override
    public void onGroupCollapsed(int groupPosition) {
       /* Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.rotate_reverse);
        image.startAnimation(animation);

        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                image.setImageResource(R.drawable.right_arrow);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });*/
    }

    @Override
    public View getChildView(final int groupPosition, int childPosition, boolean isLastChild, View convertView, final ViewGroup parent) {

        try {
            final childViewHolderItem childViewHolderItem;

            if (convertView == null) {
                convertView = mLayoutInflater.inflate(R.layout.shopping_child_list, parent, false);
                childViewHolderItem = new childViewHolderItem();
/*
            int[] positions = new int[2];
            positions[0] = childPosition;
            positions[1] = groupPosition;*/

                childViewHolderItem.textViewItem1 = (TextView) convertView.findViewById(R.id.child_text);
                childViewHolderItem.icon1 = (ImageView) convertView.findViewById(R.id.image_tickIcon);

//            viewHolder1.textViewItem1.setTag(positions);

                convertView.setTag(childViewHolderItem);
            } else {
                childViewHolderItem = (childViewHolderItem) convertView.getTag();
            }

            final PlanLists planLists = (PlanLists) getChild(groupPosition, childPosition);
            childViewHolderItem.textViewItem1.setText(planLists.getFoodName().toString());

            if (isExtraFoodPrefesAvailable(food.mealType, planLists)) {

                String str = getContentFromPrefes(food.mealType, planLists);
                if (str.equalsIgnoreCase(planLists.getFoodName().toString())) {
                    childViewHolderItem.icon1.setVisibility(View.VISIBLE);

                    SharedPreferences pref = Utility.getSharedPreferences(mContext, AppConstants.STORE_EXTRA_FOODS);
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putString(planLists.getMealPlanType(), planLists.getFoodName().toString());
                    editor.commit();

                } else {
                    childViewHolderItem.icon1.setVisibility(View.INVISIBLE);
                }

            } else {
                childViewHolderItem.icon1.setVisibility(View.INVISIBLE);
            }/*else if(isListContains(groupPosition, childPosition)) {
            childViewHolderItem.icon1.setVisibility(View.VISIBLE);
        }*/
            childViewHolderItem.textViewItem1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
/*
                CheckBox box = (CheckBox) v;
                int[] posTag = (int[]) v.getTag();*/
                    if (pwindo != null && !pwindo.isShowing()) {
                        initiatePopupWindow(planLists, parent, v, groupPosition);
                        childViewHolderItem.icon1.setVisibility(View.GONE);
                    } else if (pwindo != null && pwindo.isShowing())
                        pwindo.dismiss();
                }
            });

            return convertView;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private boolean isListContains(int groupPos, int childPosition) {
        if (mChildCheckStates.containsKey(groupPos)) {
            ArrayList<Integer> childPos = new ArrayList<>(Arrays.asList(mChildCheckStates.get(groupPos)));
            if (childPos.contains(childPosition)) {
                return true;
            }
        }
        return false;
    }

    private void initiatePopupWindow(final PlanLists planLists, final ViewGroup parent, final View v, final int position) {

        try {

            WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
            Display display = wm.getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            final int width = size.x / 2;
            final int height = size.y / 2;
            final int[] values = new int[2];
            v.getLocationOnScreen(values);
            Log.d("X & Y", values[0] + " " + values[1]);


            // We need to get the instance of the LayoutInflater
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View layout = null;

            if (planLists.getCategory().equalsIgnoreCase("Free Foods")) {
                layout = inflater.inflate(R.layout.free_food_data, null);
                popup = new PopupWindow(layout, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT, true);
            } else {

                layout = inflater.inflate(R.layout.show_food_data, null);
                popup = new PopupWindow(layout, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT, true);
                TextView calories, carbs, fiber, fat, protein;

                calories = (TextView) layout.findViewById(R.id.calories_value);
                carbs = (TextView) layout.findViewById(R.id.carbs_value);
                fiber = (TextView) layout.findViewById(R.id.fiber_value);
                fat = (TextView) layout.findViewById(R.id.fat_value);
                protein = (TextView) layout.findViewById(R.id.protien_value);
                Button add_btn = (Button) layout.findViewById(R.id.btn_add);

                calories.setText(planLists.getCalories());
                carbs.setText(planLists.getCarbohydrate() + "g");
                fiber.setText(planLists.getFiber() + "g");
                fat.setText(planLists.getFat() + "g");
                protein.setText(planLists.getProtein() + "g");

                add_btn.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {


                       /* childViewHolderItem childViewHolderItem;
                        childViewHolderItem = new childViewHolderItem();
                        childViewHolderItem.icon1 = (ImageView) v.findViewById(R.id.image_tickIcon);*/


                        SharedPreferences preferences1 = Utility.getSharedPreferences(mContext, AppConstants.SHARED_PREFES_MARK_EATEN_DETAILS);
                        boolean mark_eaten = preferences1.getBoolean(food.mealType + "," + AppConstants.STATUS, false);

                        if (mark_eaten) {
                            Utility.showAlert((Activity) mContext, "Meal Already Eaten", false);
                        } else {
                            extraFoodStatus = true;
                            String goalType = "";
                            SharedPreferences pref = Utility.getSharedPreferences(mContext, AppConstants.SHARED_PREFES_EXTRA_FOODS_VALUES);
                            SharedPreferences.Editor editor = pref.edit();
                            editor.putBoolean(AppConstants.EXTRA_FOODS, true);
                            editor.commit();

                            SharedPreferences prefUserDet = Utility.getSharedPreferences(mContext, AppConstants.SHARED_PREFES_USER_DETAILS);
                            if (prefUserDet.contains(AppConstants.GOAL_TYPE)) {
                                goalType = prefUserDet.getString(AppConstants.GOAL_TYPE, "");
                            }
                       /* if (!Utility.isBlank(goalType) && goalType.contains("Lose")) {
                            float checkCalo = BreakFast_Activity.getExtraFoodCalorie((android.app.Activity) mContext);
                            if (checkCalo > MAX_LOSE_CALO) {
                                System.out.println("check calories" + checkCalo);
                                Utility.showAlert((android.app.Activity) mContext, "The weight loss plan limits you to " + MAX_LOSE_CALO + "  calories of extra food per day.", false);
                                return;
                            }
                        }*/
                            if (!Utility.isBlank(goalType) && goalType.contains("Lose")) {

                                int checkCalo = BreakFast_Activity.getExtraFoodCalorie((android.app.Activity) mContext);
                                int maxCalories = checkCalo + Integer.parseInt(planLists.getCalories());

                                if (maxCalories > MAX_LOSE_CALO) {
                                    System.out.println("check calories" + checkCalo);
                                    Utility.showAlert((android.app.Activity) mContext, "The weight loss plan limits you to " + MAX_LOSE_CALO + "  calories of extra food per day.", false);
                                    return;
                                }
                            }
                            BreakFast_Activity.storeExtraSharedPrefes((android.app.Activity) mContext, planLists);
                            BreakFast_Activity.getExtraFoodTotal(mContext);

                        }

                        pwindo.dismiss();
                        popup.dismiss();

                        notifyDataSetChanged();
                    }
                });
            }

            if (values[1] > height) {
                Log.e("position", " No 2");
                layout.setBackgroundResource(R.drawable.popup_bg_down);

            } else {
                Log.e("position", " No 1");

            }


            popup.setBackgroundDrawable(new BitmapDrawable());
            popup.setOutsideTouchable(true);

          /*  if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                Animation animm = AnimationUtils.loadAnimation(mContext, R.anim.flyin);
                //View popupView = inflater.inflate(R.layout.show_food_data, null);
                layout.setAnimation(animm);
                animm.start();
            }*/


          /*  Log.e("width = "+width,"width = "+v.getX());
            Log.e("height = "+height,"height = "+v.getY());*/

           /* ViewTreeObserver vto = layout.getViewTreeObserver();
            vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

                @Override
                public void onGlobalLayout() {

                    int lay_height = layout.getHeight();
                    if(values[1] > height) {
                        popup.showAtLocation(v, Gravity.NO_GRAVITY, width,  (values[1] - lay_height));
                    }
                    ViewTreeObserver obs = layout.getViewTreeObserver();

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        obs.removeOnGlobalLayoutListener(this);
                    } else {
                        obs.removeGlobalOnLayoutListener(this);
                    }
                }

            });*/

            if (values[1] > height) {
                Log.e("position", " No 2");
                //tv.getHeight()
                popup.showAtLocation(v, Gravity.NO_GRAVITY, width, ((values[1] - (v.getHeight() + 150)) - (int) v.getY()));
            } else {
                Log.e("position", " No 1");
                popup.showAsDropDown(v);
            }
            //popup.showAsDropDown(v);
            popup.update();


          /*  pwindo.setContentView(layout);
            pwindo.setWidth(WindowManager.LayoutParams.MATCH_PARENT);
            pwindo.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
            pwindo.setFocusable(true);

            pwindo.setTouchable(true);
            pwindo.setOutsideTouchable(true);
            pwindo.setBackgroundDrawable(new ShapeDrawable());
            pwindo.showAtLocation(layout, Gravity.CENTER, 0, 0);*/

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


   /* public void addPrefesToList() {
        if (isExtraFoodPrefesAvailable(mTitle, JsonConstants.CALORIES)) {
             calories = Float.parseFloat(getContentFromPrefes(mTitle, JsonConstants.CALORIES));
             carbs = Float.parseFloat(getContentFromPrefes(mTitle, JsonConstants.CARBOHYDRATE));
             protein = Float.parseFloat(getContentFromPrefes(mTitle, JsonConstants.PROTEIN));
             fiber = Float.parseFloat(getContentFromPrefes(mTitle, JsonConstants.FIBER));
             fats = Float.parseFloat(getContentFromPrefes(mTitle, JsonConstants.FAT));
        }
    }*/

            /*if (mProdValues.containsKey(KEY_PROTEIN)) {
                float temp = mProdValues.get(KEY_PROTEIN);
                mProdValues.put(KEY_PROTEIN, temp + protein);
                proteinTotal += protein;
                mProdValues.put(KEY_PROTEIN_TOTAL, proteinTotal);
                storeSharedPrefes(title, KEY_PROTEIN_TOTAL, proteinTotal);

            } else {
                mProdValues.put(KEY_PROTEIN, protein);
                proteinTotal += protein;
                mProdValues.put(KEY_PROTEIN_TOTAL, proteinTotal);
                storeSharedPrefes(title, KEY_PROTEIN_TOTAL, proteinTotal);
            }*/

    private boolean isExtraFoodPrefesAvailable(String mealType, PlanLists planLists) {
        SharedPreferences preferences = Utility.getSharedPreferences(mContext, AppConstants.SHARED_PREFES_EXTRA_FOOD_DETAILS);
        String planListKey = mealType + "," + planLists.getCategory() + "," + planLists.getFoodName();
        if (preferences.contains(planListKey)) {
            return true;
        }
        return false;
    }

    public void setIcon() {

    }

    private String getContentFromPrefes(String mealType, PlanLists planLists) {
        SharedPreferences preferences = Utility.getSharedPreferences(mContext, AppConstants.SHARED_PREFES_EXTRA_FOOD_DETAILS);
        String planListKey = mealType + "," + planLists.getCategory() + "," + planLists.getFoodName();
        return preferences.getString(planListKey, "");
    }

    private boolean isPrefContainsExtraFood(String mealType, PlanLists planLists) {
        SharedPreferences preferences = Utility.getSharedPreferences(mContext, AppConstants.SHARED_PREFES_EXTRA_FOOD_DETAILS);
        String planListKey = mealType + "," + planLists.getCategory() + "," + planLists.getFoodName();
        if (preferences.contains(planListKey)) {
            return true;
        }
        return false;
    }

   /* private String getContentFromPrefes(String mealType, String category) {
        SharedPreferences preferences = Utility.getSharedPreferences(mContext, AppConstants.SHARED_PREFES_EXTRA_FOOD_DETAILS);
        String planListKey = mealType + "," + category;
        return preferences.getString(planListKey, "");
    }*/

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

    private boolean isExtraFoodPrefesAvailable(String mealType) {
        SharedPreferences preferences = Utility.getSharedPreferences(mContext, AppConstants.STORE_EXTRA_FOODS);
        String planListKey = mealType;
        if (preferences.contains(planListKey)) {
            return true;
        }
        return false;
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