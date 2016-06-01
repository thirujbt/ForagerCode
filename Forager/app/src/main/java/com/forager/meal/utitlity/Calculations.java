package com.forager.meal.utitlity;

import java.util.ArrayList;

/**
 * Created by Akbar on 9/4/2015.
 */
public final class Calculations {

    private static float BMR_USSYS_WOMEN_X1 = 655f;

    private static final float BMR_USSYS_WOMEN_X2 = 4.35f;

    private static final float BMR_USSYS_WOMEN_X3 = 4.7f;

    private static final float BMR_USSYS_WOMEN_X4 = 4.7f;

    private static final float BMR_USSYS_MEN_X1 = 66f;

    private static final float BMR_USSYS_MEN_X2 = 6.23f;

    private static final float BMR_USSYS_MEN_X3 = 12.7f;

    private static final float BMR_USSYS_MEN_X4 = 6.8f;

    private static final float BMR_METRIC_WOMEN_X1 = 655f;

    private static final float BMR_METRIC_WOMEN_X2 = 9.6f;

    private static final float BMR_METRIC_WOMEN_X3 = 1.8f;

    private static final float BMR_METRIC_WOMEN_X4 = 4.7f;

    private static final float BMR_METRIC_MEN_X1 = 66f;

    private static final float BMR_METRIC_MEN_X2 = 13.7f;

    private static final float BMR_METRIC_MEN_X3 = 5f;

    private static final float BMR_METRIC_MEN_X4 = 6.8f;

    private static final float BMR_LITTLE_TO_NO_EXERCISE = 1.2f;

    private static final float BMR_1_3_WORKOUTS_PER_WEEK = 1.375f;

    private static final float BMR_3_5_WORKOUTS_PER_WEEK = 1.55f;

    private static final float BMR_6_7_WORKOUTS_PER_WEEK = 1.725f;

    private static final float BMR_WORKOUT_TWICE_A_DAY = 1.9f;

    private static final float FEET_1 = 12f;

    private static final float LB_1LOSS = 0.20f;

    private static final float LB_2LOSS = 0.30f;

    private static final float LB_1GAIN = 115;

    private static final float LB_2GAIN = 230;

    private static final float AGGRESSIVE_MASS_GAIN = 900;

    private static final float MAINTAIN_WEIGHT = 0;

    public static enum ACTIVITY_LEVEL_ENUM {BMR_LITTLE_TO_NO_EXERCISE, BMR_1_3_WORKOUTS_PER_WEEK ,
        BMR_3_5_WORKOUTS_PER_WEEK, BMR_6_7_WORKOUTS_PER_WEEK, BMR_WORKOUT_TWICE_A_DAY};

    public static enum GOAL_ENUM {MAINTAIN_WEIGHT, LOSE_45_1,LOSE_90_2, GAIN_45_1, GAIN_90_2,
        AGGRESSIVE_MASS_GAIN};

    public static final float getBMR(boolean isMEN, boolean isMetric, float weight, float height,
                                     int age, ACTIVITY_LEVEL_ENUM activityLevelEnum) {

        float bmr = 0.0f;

        if(isMEN) {

            if(isMetric) {
                bmr = BMR_METRIC_MEN_X1 + (BMR_METRIC_MEN_X2 * weight) + (BMR_METRIC_MEN_X3 * height) - (BMR_METRIC_MEN_X4 * age);
            } else {
                bmr = BMR_USSYS_MEN_X1 + (BMR_USSYS_MEN_X2 * weight) + (BMR_USSYS_MEN_X3 * height) - (BMR_USSYS_MEN_X4 * age);
            }

        } else {

            if(isMetric) {
                bmr = BMR_METRIC_WOMEN_X1 + (BMR_METRIC_WOMEN_X2 * weight) + (BMR_METRIC_WOMEN_X3 * height) - (BMR_METRIC_WOMEN_X4 * age);
            } else {
                bmr = BMR_USSYS_WOMEN_X1 + (BMR_USSYS_WOMEN_X2 * weight) + (BMR_USSYS_WOMEN_X3 * height) - (BMR_USSYS_WOMEN_X4 * age);
            }
        }

        switch (activityLevelEnum) {
            case BMR_LITTLE_TO_NO_EXERCISE:
                bmr *= BMR_LITTLE_TO_NO_EXERCISE;
                break;
            case BMR_1_3_WORKOUTS_PER_WEEK:
                bmr *= BMR_1_3_WORKOUTS_PER_WEEK;
                break;
            case BMR_3_5_WORKOUTS_PER_WEEK:
                bmr *= BMR_3_5_WORKOUTS_PER_WEEK;
                break;
            case BMR_6_7_WORKOUTS_PER_WEEK:
                bmr *= BMR_6_7_WORKOUTS_PER_WEEK;
                break;
            case BMR_WORKOUT_TWICE_A_DAY:
                bmr *= BMR_WORKOUT_TWICE_A_DAY;
                break;
            default:
                break;
        }
        return bmr;
    }

    public static final float calcFeetToInch(int feet, int inch) {
        float feet_calc = feet * FEET_1;
        return feet_calc + inch;
    }

    public static final float calcGoal(float dialyCalc, GOAL_ENUM goalEnum) {
        float meal_plan = 0.0f;
        float x = 0.0f;
        switch (goalEnum) {
            case MAINTAIN_WEIGHT:
                meal_plan = dialyCalc + MAINTAIN_WEIGHT;
                               break;
            case LOSE_45_1:
                x = dialyCalc * LB_1LOSS;
                meal_plan = dialyCalc - x;
                break;
            case LOSE_90_2:
                x = dialyCalc * LB_2LOSS;
                meal_plan = dialyCalc - x;
                break;
            case GAIN_45_1:
                meal_plan = dialyCalc + LB_1GAIN;
                break;
            case GAIN_90_2:
                meal_plan = dialyCalc + LB_2GAIN;
                break;
            case AGGRESSIVE_MASS_GAIN:
                meal_plan = dialyCalc + AGGRESSIVE_MASS_GAIN;
                break;
            default:
                break;
        }
        return meal_plan;
    }

    public static int calcPlanClarie(float myCalorie) {
        ArrayList<Integer> plan_calorie_array = new ArrayList<>();
        for(int i = 1400;i<=5000;i+=200) {
            plan_calorie_array.add(i);
        }
        int distance = (int) Math.abs(plan_calorie_array.get(0) - myCalorie);
        int idx = 0;
        for(int c = 1; c < plan_calorie_array.size(); c++){
            int cdistance = (int) Math.abs(plan_calorie_array.get(c) - myCalorie);
            if(cdistance < distance){
                idx = c;
                distance = cdistance;
            }
        }
        return plan_calorie_array.get(idx);
    }
}
