package com.forager.meal.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.forager.meal.bean.CurrentPlanDatas;
import com.forager.meal.bean.PlanLists;
import com.forager.meal.constants.AppConstants;
import com.forager.meal.ui.R;
import com.forager.meal.ui.fragment.BreakFast_Activity;
import com.forager.meal.ui.fragment.PlanControllerFragment;
import com.forager.meal.utitlity.Utility;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;


public class MealsListItems extends BaseAdapter {

    android.support.v4.app.FragmentTransaction transaction;

    private final Context mContext;

    private LayoutInflater inflater;

    ArrayList<PlanLists> mItems;

    private final PopupWindow pwindo;

    public static HashMap<String, CurrentPlanDatas> mCurrentList = new HashMap<>();

    DismissScreen mDismissScreen;

    private static final String TITLE_TAG = "SubCategory, ";

    String temp_title = "";

    PlanControllerFragment food = new PlanControllerFragment();



    public MealsListItems(Context context, ArrayList<PlanLists> items, DismissScreen dismissScreen) {
        mContext = context;
        this.mItems = items;
        pwindo = new PopupWindow(mContext);
        this.mDismissScreen = dismissScreen;
        //  Collections.sort(mItems, new CompareName());

        ArrayList<PlanLists> list = new ArrayList<>();

        for (PlanLists planLists : mItems) {
            if (!temp_title.equalsIgnoreCase(planLists.getSubCategory()) &&
                    !planLists.getSubCategory().toString().equalsIgnoreCase("null")) {

                PlanLists lists = new PlanLists();
                temp_title = planLists.getSubCategory();
                lists.setFoodName(TITLE_TAG + planLists.getSubCategory());
                list.add(lists);


                list.add(planLists);
            } else {

                list.add(planLists);
            }
        }

        mItems = new ArrayList<>();
        mItems.addAll(list);


    }

    @Override
    public int getCount() {
        return mItems.size();
    }

    @Override
    public Object getItem(int location) {
        return mItems.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    static class ViewHolderItem {
        TextView textViewItem;
        ImageView icon;
        TextView titleTextView;
    }

    @Override
    public View getView(int position, View convertView, final ViewGroup parent) {
        final ViewHolderItem viewHolder;
        if (convertView == null) {
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            convertView = inflater.inflate(R.layout.child_list_item, parent, false);
            viewHolder = new ViewHolderItem();
            viewHolder.textViewItem = (TextView) convertView.findViewById(R.id.child_text);
            viewHolder.icon = (ImageView) convertView.findViewById(R.id.tick_icon);

            viewHolder.titleTextView = (TextView) convertView.findViewById(R.id.child_title_text);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolderItem) convertView.getTag();
        }

        final PlanLists planLists = (PlanLists) getItem(position);

        if (planLists.getFoodName().startsWith(TITLE_TAG)) {
            String[] split = planLists.getFoodName().toString().split(TITLE_TAG);
            viewHolder.textViewItem.setText(split[1]);
            viewHolder.textViewItem.setGravity(Gravity.CENTER_VERTICAL);
            viewHolder.textViewItem.setTypeface(null, Typeface.BOLD);
            viewHolder.icon.setVisibility(View.INVISIBLE);
        } else {
            viewHolder.textViewItem.setText(planLists.getFoodName().toString());
            viewHolder.textViewItem.setGravity(Gravity.CENTER);
            viewHolder.textViewItem.setTypeface(null, Typeface.NORMAL);
            if (isPlanListPrefesAvailable(planLists.getMealType().toString(), planLists.getCategory().toString())) {

                String str = getContentFromPrefes(planLists.getMealType().toString(), planLists.getCategory().toString());
                if (str.equalsIgnoreCase(planLists.getFoodName().toString())) {
                    viewHolder.icon.setVisibility(View.VISIBLE);
                   /* viewHolder.icon.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Toast.makeText(mContext, planLists.getFoodName(), Toast.LENGTH_SHORT).show();
                            if (BreakFast_Activity.isSharedPrefesAvailable((Activity) mContext, planLists)) {
                                BreakFast_Activity.deleteSharedPrefes((Activity) mContext, planLists);
                                viewHolder.icon.setVisibility(View.INVISIBLE);
                                notifyDataSetChanged();
                                mDismissScreen.closeExtendedScreen();
                            }
                        }
                    });*/

                } else {
                    viewHolder.icon.setVisibility(View.INVISIBLE);
                }

            }
            viewHolder.textViewItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (pwindo != null && !pwindo.isShowing())
                        initiatePopupWindow(planLists, parent, v);
                    else if (pwindo != null && pwindo.isShowing())
                        pwindo.dismiss();
                }
            });
        }

      /*  if(!planLists.getSubCategory().toString().equalsIgnoreCase("null") && !temp_title.equalsIgnoreCase(planLists.getSubCategory())) {
            temp_title = planLists.getSubCategory();
            viewHolder.titleTextView.setVisibility(View.VISIBLE);
            viewHolder.titleTextView.setText(planLists.getSubCategory().toString());
        } else {
            viewHolder.titleTextView.setVisibility(View.GONE);
        }*/


        return convertView;
    }

    private void initiatePopupWindow(final PlanLists planLists, final ViewGroup parent, final View v) {

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

            final View layout = inflater.inflate(R.layout.show_food_data, null);
            if (values[1] > height) {
                Log.e("position", " No 2");
                layout.setBackgroundResource(R.drawable.popup_bg_down);
            } else {
                Log.e("position", " No 1");
            }

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

          /*  Log.e("width = "+width,"width = "+v.getX());
            Log.e("height = "+height,"height = "+v.getY());*/


          /*  if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                Animation animm = AnimationUtils.loadAnimation(mContext, R.anim.flyin);
                //View popupView = inflater.inflate(R.layout.show_food_data, null);
                layout.setAnimation(animm);
                animm.start();
            }*/


            final PopupWindow popup = new PopupWindow(layout, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT, true);
            popup.setBackgroundDrawable(new BitmapDrawable());
            popup.setOutsideTouchable(true);

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

            add_btn.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    SharedPreferences preferences1 = Utility.getSharedPreferences(mContext, AppConstants.SHARED_PREFES_MARK_EATEN_DETAILS);
                    boolean mark_eaten = preferences1.getBoolean(food.mealType + "," + AppConstants.STATUS, false);


                    if (mark_eaten) {
                        Utility.showAlert((Activity) mContext, "Meal Already Eaten", false);
                    } else {
                        SharedPreferences pref = Utility.getSharedPreferences(mContext, AppConstants.SHARED_PREFES_MEAL_PLAN_CHART_STATUS);
                        SharedPreferences.Editor editor = pref.edit();
                        editor.putBoolean(planLists.getMealType(), true);
                        editor.commit();

                        BreakFast_Activity.status = true;
                        PlanControllerFragment.status = true;
                        CurrentPlanDatas currentPlanDatas = new CurrentPlanDatas();

                        currentPlanDatas.setCalories(Integer.parseInt(planLists.getCalories()));
                        currentPlanDatas.setCarbs(Integer.parseInt(planLists.getCarbohydrate()));
                        currentPlanDatas.setFiber(Integer.parseInt(planLists.getFiber()));
                        currentPlanDatas.setFat(Integer.parseInt(planLists.getFat()));
                        currentPlanDatas.setProtein(Integer.parseInt(planLists.getProtein()));
                        currentPlanDatas.setFoodName(planLists.getFoodName());
                        currentPlanDatas.setMealType(planLists.getMealType());
                        currentPlanDatas.setCategory(planLists.getCategory());

                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append(planLists.getCategory() + "," + planLists.getMealType());

                        mCurrentList.put(stringBuilder.toString(), currentPlanDatas);
                        pwindo.dismiss();
                        popup.dismiss();
                        BreakFast_Activity.storeSharedPrefes((android.app.Activity) mContext, planLists);
                        mDismissScreen.closeExtendedScreen();

                    }
                }
            });


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

    private boolean isPlanListPrefesAvailable(String mealType, String category) {
        SharedPreferences preferences = Utility.getSharedPreferences((Activity) mContext, AppConstants.SHARED_PREFES_MEAL_PLANNER_DETAILS);
        String planListKey = mealType + "," + category;
        if (preferences.contains(planListKey)) {
            return true;
        }
        return false;
    }

    private boolean isChartPrefesAvailable(String mealType, String category) {
        SharedPreferences preferences = Utility.getSharedPreferences((Activity) mContext, AppConstants.SHARED_PREFES_MEAL_PLANNER_DETAILS);
        String planListKey = mealType + "," + category;
        if (preferences.contains(planListKey)) {
            return true;
        }
        return false;
    }

    private String getContentFromPrefes(String mealType, String category) {
        SharedPreferences preferences = Utility.getSharedPreferences((Activity) mContext, AppConstants.SHARED_PREFES_MEAL_PLANNER_DETAILS);
        String planListKey = mealType + "," + category;
        return preferences.getString(planListKey, "");
    }

    public interface DismissScreen {
        void closeExtendedScreen();
    }

    public static class CompareName implements Comparator<PlanLists> {
        @Override
        public int compare(PlanLists lhs, PlanLists rhs) {
            return lhs.getSubCategory().compareToIgnoreCase(rhs.getSubCategory());
        }
    }

}
