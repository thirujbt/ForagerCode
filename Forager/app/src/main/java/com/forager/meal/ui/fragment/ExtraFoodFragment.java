package com.forager.meal.ui.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.forager.meal.adapter.ExtraFoodsAdapter;
import com.forager.meal.adapter.TitleListAdapter;
import com.forager.meal.adapter.WrapperExpandableListAdapter;
import com.forager.meal.application.MyApplication;
import com.forager.meal.bean.PlanLists;
import com.forager.meal.constants.AppConstants;
import com.forager.meal.database.Database;
import com.forager.meal.ui.R;
import com.forager.meal.ui.activity.DashBoardActivity;
import com.forager.meal.utitlity.Utility;
import com.forager.meal.widget.FloatingGroupExpandableListView;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Catherin on 10/15/2015.
 */
public class ExtraFoodFragment extends Fragment implements View.OnClickListener {

   public static FloatingGroupExpandableListView extraFood_List;
    TextView header;
    ImageView back_button;
    ImageView meal_plan_logo;
    Database mDatabase;
    String active_plan;
    PlanControllerFragment food = new PlanControllerFragment();
    ArrayList<PlanLists> mItemsBeansCategory = new ArrayList<>();
    ArrayList<String> mealTypeList = new ArrayList<>();
    HashMap<String, ArrayList<PlanLists>> menuItemsChild = new HashMap<>();
    TextView extraFoods_header;
    int width;


    protected FragmentActivity mActivity;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = (FragmentActivity) activity;
    }

    private Timer mTimer;
    TimerTask timerTask1 = new TimerTask() {
        public void run() {
            DateFormat df = new SimpleDateFormat("hh:mm a");
            String date = df.format(Calendar.getInstance().getTime());
            //System.out.println("service started......" + date);
            if (date.equalsIgnoreCase("12:00 AM")) {
                try {
                    if (mActivity != null)
                        Utility.resetFood(mActivity);
                    if (mActivity != null) {
                        mActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (extraFood_List != null) {
                                    ExtraFoodsAdapter adapter1 = new ExtraFoodsAdapter(mActivity, mealTypeList, menuItemsChild, food.mealType);

                                    final WrapperExpandableListAdapter wrapperAdapter1 = new WrapperExpandableListAdapter(adapter1);

                                    extraFood_List.setAdapter(wrapperAdapter1);
                                }
                            }
                        });
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    };

    /* @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        extraFood_List.setIndicatorBounds(width - GetPixelFromDips(50), width - GetPixelFromDips(10));
    }
*/
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.extra_food, container, false);
        PlanControllerFragment.flag=false;
     /*   DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
        width = metrics.widthPixels;*/


       // dipToPixels(getActivity())
        mDatabase = Database.getInstance(getActivity());
        mDatabase.getMyWritableDatabase();
        init(rootView);
        try {
            mTimer = new Timer();
            mTimer.schedule(timerTask1, 0, 6000 * 10 * 1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rootView;
    }


    public static float dipToPixels(Context context, float dipValue) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dipValue, metrics);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Display newDisplay = getActivity().getWindowManager().getDefaultDisplay();
        int width = newDisplay.getWidth();
        if(TitleListAdapter.extraFoodStatus) {

        } else {

        }
        if(android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.JELLY_BEAN_MR2) {
            extraFood_List.setIndicatorBounds( width - GetPixelFromDips(70), width - GetPixelFromDips(10));
/*
 extraFood_List.setIndicatorBounds(width-((int)(getActivity().getResources().
                    getDimension(R.dimen.expand_indicator_width))+((int)(getActivity().getResources().
                    getDimension(R.dimen.expand_indicator_right_margin)))), width-((int)(getActivity().
                    getResources().getDimension(R.dimen.expand_indicator_right_margin))));
*/

/*
            extraFood_List.setChildIndicatorBounds(width - ((int)dipToPixels(getActivity(), ((int)(getActivity().getResources().
                    getDimension(R.dimen.expand_indicator_width))))+((int)(getActivity().getResources().
                    getDimension(R.dimen.expand_indicator_right_margin)))), width-((int)(getActivity().
                    getResources().getDimension(R.dimen.expand_indicator_right_margin))));*/



        } else {
            extraFood_List.setIndicatorBoundsRelative( width - GetPixelFromDips(70), width - GetPixelFromDips(10));
        }



    }

  /*  public static float dipToPixels(Context context, float dipValue) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dipValue, metrics);
    }
*/

    public int GetPixelFromDips(float pixels) {
        // Get the screen's density scale
        final float scale = getResources().getDisplayMetrics().density;
        // Convert the dps to pixels, based on density scale
        return (int) (pixels * scale + 0.5f);
    }

    private void init(View rootView) {

        MyApplication.setContext(getActivity());
        extraFoods_header=(TextView)rootView.findViewById(R.id.extraFoods_header);
        extraFoods_header.setTypeface(MyApplication.getFont());
        extraFood_List = (FloatingGroupExpandableListView) rootView.findViewById(R.id.extraFoods_myList);
        extraFood_List.setChildDivider(new ColorDrawable(Color.parseColor("#C8C7CC")));
        header=(TextView)rootView.findViewById(R.id.extraFoods_header);
        back_button=(ImageView)rootView.findViewById(R.id.extraFoods_backBtn);
        meal_plan_logo=(ImageView)rootView.findViewById(R.id.meal_plan_logo);
        header.setText(food.mealType);



        back_button.setOnClickListener(this);
        mealTypeList = new ArrayList<>();
        active_plan =mDatabase.getActivePlanType();
        File sourceFile = new File(AppConstants.baseDir + "/" + AppConstants.FORAGER_DIR + "/" + active_plan+"/"+ AppConstants.SHOP_MEAL_PLAN_LOGO_DIR, "1.png");

        if (sourceFile.exists()) {
            Bitmap myBitmap = Utility.callPhotoUploadMethod(sourceFile.getAbsolutePath());
            meal_plan_logo.setImageBitmap(myBitmap);
        }

        for (int i = 0; i < DashBoardActivity.mExtraFoodsBeanArrayList.size(); i++) {
            PlanLists mBean = DashBoardActivity.mExtraFoodsBeanArrayList.get(i);
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
                menuItemsChild.put(mealTypeList.get(i).toString(), mSubList);
            }
        }
        ExtraFoodsAdapter adapter1 = new ExtraFoodsAdapter(getActivity(), mealTypeList, menuItemsChild,food.mealType);

        final WrapperExpandableListAdapter wrapperAdapter1 = new WrapperExpandableListAdapter(adapter1);

        extraFood_List.setAdapter(wrapperAdapter1);

       // extraFood_List.setGroupIndicator(null);

        extraFood_List.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {

             /*  ViewGroup viewGroup = (ViewGroup) extraFood_List.getChildAt(groupPosition);
               ViewGroup viewGroup1 = (ViewGroup) viewGroup.getChildAt(0);
               final ImageView icon = (ImageView) viewGroup1.getChildAt(1);
                Animation animation = AnimationUtils.loadAnimation(getActivity(), R.anim.rotate_image);
                icon.setImageResource(R.drawable.right_arrow);
                icon.startAnimation(animation);


               animation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
    }

                    @Override
                    public void onAnimationEnd(Animation animation) {

                        Log.e("Animation Completed", "Animation Completed");
                        icon.setImageResource(R.drawable.right_reverse_arrow);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });*/
            }
        });



        extraFood_List.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {
            @Override
            public void onGroupCollapse(int groupPosition) {

               /* ViewGroup viewGroup = (ViewGroup) extraFood_List.getChildAt(groupPosition);
                ViewGroup viewGroup1 = (ViewGroup) viewGroup.getChildAt(0);
                final ImageView icon = (ImageView) viewGroup1.getChildAt(1);
                Animation animation = AnimationUtils.loadAnimation(getActivity(), R.anim.rotate_reverse);
                icon.setImageResource(R.drawable.right_reverse_arrow);
                icon.startAnimation(animation);

                animation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }
                    @Override
                    public void onAnimationEnd(Animation animation) {
                        Log.e("Animation COmpleted", "Animation Completed");

                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });*/
            }
        });

    }

 @Override
    public void onClick(View v) {
        switch(v.getId())
        {
            case R.id.extraFoods_backBtn:
                Intent mealsList = new Intent(getActivity(), DashBoardActivity.class);
                getActivity().overridePendingTransition(0, 0);
                mealsList.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                mealsList.putExtra("dashboard", "breakfast");
                getActivity().finish();
                getActivity().overridePendingTransition(0, 0);
                getActivity().startActivity(mealsList);
        }
    }
}
