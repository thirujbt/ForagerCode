package com.forager.meal.bean;

import com.forager.meal.utitlity.Calculations;

/**
 * Created by Akbar on 9/3/2015.
 */
public class UserDetails {

    private static UserDetails mInstance;

    private String user_name;

    private String birth_date;

    private String country;

    private String units;

    private String age;

    private String weight;

    private String height;

    private String gender;

    private String activity_level;

    private int body_calories;

    private String goal_type;

    private String goal_calories;

   /* public int getGoal_index() {
        return goal_index;
    }

    public void setGoal_index(int goal_index) {
        this.goal_index = goal_index;
    }
*/
    private String goal_limit;

    private String plan_calorie;

    //private int goal_index;

    private Calculations.GOAL_ENUM goal_enum;


    private UserDetails()
    {


    }


    public static UserDetails getmInstance() {
        return mInstance;
    }

    public static void setmInstance(UserDetails mInstance) {
        UserDetails.mInstance = mInstance;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getBirth_date() {
        return birth_date;
    }

    public void setBirth_date(String birth_date) {
        this.birth_date = birth_date;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getUnits() {
        return units;
    }

    public void setUnits(String units) {
        this.units = units;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getActivity_level() {
        return activity_level;
    }

    public void setActivity_level(String activity_level) {
        this.activity_level = activity_level;
    }

    public int getBody_calories() {
        return body_calories;
    }

    public void setBody_calories(int body_calories) {
        this.body_calories = body_calories;
    }

    public String getGoal_type() {
        return goal_type;
    }

    public void setGoal_type(String goal_type) {
        this.goal_type = goal_type;
    }

    public String getGoal_calories() {
        return goal_calories;
    }

    public void setGoal_calories(String goal_calories) {
        this.goal_calories = goal_calories;
    }

    public String getGoal_limit() {
        return goal_limit;
    }

    public void setGoal_limit(String goal_limit) {
        this.goal_limit = goal_limit;
    }

    public String getPlan_calorie() {
        return plan_calorie;
    }

    public void setPlan_calorie(String plan_calorie) {
        this.plan_calorie = plan_calorie;
    }

    public String getProf_url() {
        return prof_url;
    }

    public void setProf_url(String prof_url) {
        this.prof_url = prof_url;
}

    private String prof_url;

    public static UserDetails getInstance() {
        if(mInstance == null) {
            mInstance = new UserDetails();
        }
        return mInstance;
    }

    public Calculations.GOAL_ENUM getGoal_enum() {
        return goal_enum;
    }

    public void setGoal_enum(Calculations.GOAL_ENUM goal_enum) {
        this.goal_enum = goal_enum;
    }
}
