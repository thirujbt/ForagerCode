package com.forager.meal.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import com.forager.meal.bean.UserDetails;
import com.forager.meal.constants.AppConstants;
import com.forager.meal.service.BackgroundService;
import com.forager.meal.ui.R;
import com.forager.meal.utitlity.Calculations;
import com.forager.meal.utitlity.Utility;

import java.io.File;

public class SplashScreen extends Activity {

    private static final int SPLASH_TIME_OUT = 1000 * 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        startServices();
        File baseDir = getDir("Base", Context.MODE_PRIVATE);
        AppConstants.baseDir = baseDir.getAbsolutePath();
        new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity

                SharedPreferences preferences = Utility.getSharedPreferences(SplashScreen.this, AppConstants.SHARED_PREFES_LOGIN_DETAILS);

                if (preferences.contains(AppConstants.SIGNED_IN)) {

                    if (preferences.getBoolean(AppConstants.SIGNED_IN, false)) {

                        preferences = Utility.getSharedPreferences(SplashScreen.this, AppConstants.SHARED_PREFES_USER_DETAILS);
                        UserDetails userDetails = UserDetails.getInstance();
                        userDetails.setUser_name(preferences.getString(AppConstants.USER_NAME, ""));
                        userDetails.setBirth_date(preferences.getString(AppConstants.USER_DOB, ""));
                        userDetails.setCountry(preferences.getString(AppConstants.USER_COUNTRY, ""));
                        userDetails.setProf_url(preferences.getString(AppConstants.USER_PROFILE_PIC, ""));
                        userDetails.setUnits(preferences.getString(AppConstants.BODY_UNITS, ""));
                        userDetails.setAge(preferences.getString(AppConstants.BODY_AGE, ""));
                        userDetails.setWeight(preferences.getString(AppConstants.BODY_WEIGHT, ""));
                        userDetails.setHeight(preferences.getString(AppConstants.BODY_HEIGHT, ""));
                        userDetails.setGender(preferences.getString(AppConstants.BODY_GENDER, ""));
                        userDetails.setActivity_level(preferences.getString(AppConstants.BODY_ACTIVITY_LEVEL, ""));
                        userDetails.setBody_calories((int) Math.round(Float.parseFloat(preferences.getString(AppConstants.BODY_CALORIES, ""))));
                        userDetails.setGoal_type(preferences.getString(AppConstants.GOAL_TYPE, ""));
                        userDetails.setGoal_limit(preferences.getString(AppConstants.GOAL_LIMIT, ""));
                        userDetails.setGoal_calories(preferences.getString(AppConstants.GOAL_CALORIES, ""));
                        userDetails.setPlan_calorie(preferences.getString(AppConstants.PLAN_CALORIES, ""));
                        //userDetails.setGoal_index(preferences.getInt(AppConstants.GOAL_INDEX, -1));

                        String units = userDetails.getUnits();

                        String goal_str = userDetails.getGoal_type();

                       /* int goal_index;

                        ArrayList<String> goal_array = new ArrayList<>();

                        if (units.equals(AppConstants.UNIT_METRIC)) {
                            goal_array = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.choose_goal_array_metric)));

                        } else if (units.equals(AppConstants.UNIT_USSYSTEM)) {
                            goal_array = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.choose_goal_array_ussystem)));
                        }

                        if (goal_array.contains(goal_str)) {
                            goal_index = goal_array.indexOf(goal_str);
                        } else {
                            if (units.equals(AppConstants.UNIT_METRIC)) {
                                goal_index = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.choose_goal_array_ussystem))).indexOf(goal_str);
                            } else {
                                goal_index = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.choose_goal_array_metric))).indexOf(goal_str);
                            }
                        }






//                        int goal_index = goal_array.indexOf(goal_str);

                        Calculations.GOAL_ENUM goalEnum = null;
                        switch (goal_index) {
                            case 0:
                                goalEnum = Calculations.GOAL_ENUM.MAINTAIN_WEIGHT;
                                System.out.println(goalEnum);
                                break;
                            case 1:
                                goalEnum = Calculations.GOAL_ENUM.LOSE_45_1;
                                System.out.println(goalEnum);
                                break;
                            case 2:
                                goalEnum = Calculations.GOAL_ENUM.LOSE_90_2;
                                System.out.println(goalEnum);
                                break;
                            case 3:
                                goalEnum = Calculations.GOAL_ENUM.GAIN_45_1;
                                System.out.println(goalEnum);
                                break;
                            case 4:
                                goalEnum = Calculations.GOAL_ENUM.GAIN_90_2;
                                System.out.println(goalEnum);
                                break;
                            case 5:
                                goalEnum = Calculations.GOAL_ENUM.AGGRESSIVE_MASS_GAIN;
                                break;
                            default:
                                break;

                        }*/
                        Calculations.GOAL_ENUM goal_enum = Utility.getGoalEnum(units, goal_str, SplashScreen.this);
                        userDetails.setGoal_enum(goal_enum );
                        Intent i = new Intent(SplashScreen.this, DashBoardActivity.class);
                        startActivity(i);

                    } else {
                        Intent i = new Intent(SplashScreen.this, IntroActivity.class);
                        i.putExtra("checkShownCount", true);
                        startActivity(i);
                    }
                } else {
                    Intent i = new Intent(SplashScreen.this, IntroActivity.class);
                    i.putExtra("checkShownCount", true);
                    startActivity(i);
                }
                // close this activity
                finish();
            }
        }, SPLASH_TIME_OUT);
    }

    public void startServices() {
        startService(new Intent(SplashScreen.this, BackgroundService.class));
    }

}
