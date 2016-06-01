package com.forager.meal.constants;

import android.os.Environment;

/**
 * Created by Akbar on 9/1/2015.
 */
public final class AppConstants {

    private AppConstants() {

    }

    public static final String CHECK_INTERNET_CONNECTION = "Please check your internet connection";

    public static final String SHARED_PREFES_USER_DETAILS = "user details";

    public static final String SHARED_PREFES_LOGIN_DETAILS = "login details";

    public static final String SHARED_PREFES_MEAL_PLANNER_DETAILS = "meal planner details";

    public static final String SHARED_PREFES_SHOP_LIST_DETAILS = "shop list details";

    public static final String SHARED_PREFES_EXTRA_FOOD_DETAILS = "extra food details";

    public static final String SHARED_PREFES_MEAL_PLAN_CHART_STATUS = "meal_plan_status";

    public static final String SHARED_PREFES_MEAL_PLANNER_CHART_VALUES = "meal_planner_chart_values";

    public static final String SHARED_PREFES_MARK_EATEN_DETAILS = "mark eaten details";

    public static final String SHARED_PREFES_MARK_EATEN = "mark eaten";

    public static final String SHARED_PREFES_TOTAL_CHART_VALUES = "total chart values";

    public static final String SHARED_PREFES_EXTRA_FOODS_VALUES = "extra food values";

    public static final String SHARED_PREFES_SKIP = "Skip image";

    public static final String STORE_EXTRA_FOODS = "store extra food";


    public static final String EXTRA_FOODS = "extra foods";

    public static final String ACTIVE_FOLDER = "active_folder";

    public static final String ACTIVE_PLAN = "active_plan";

    public static final String CHART_VALUES_TOTAL = "Calories_total";

    public static final String STATUS = "status";

    public static final String EMAIL_ID = "email_id";

    public static final String PASSWORD = "password";

    public static final String SIGNED_IN = "signed_in";

    public static final String BREAK_FAST_CHART = "break_fast_chart";

    public static final String PLAN_CONTROLLER_CHART = "plan_controller_chart";

    public static final String FIELD_EMPTY = "All Fields are Mandatory";

    public static final String ENTER_NAME = "Please enter name";

    public static final String ENTER_DOB = "Please enter birthdate";

    public static final String ENTER_COUNTRY = "Please enter country";

    public static final String ENTER_PROFILE = "choose profile picture";

    public static final String ENTER_EMAIL = "Please enter email id";

    public static final String ENTER_VALID_EMAIL = "Please enter valid email id";

    public static final String ENTER_PASSWORD = "Please enter password";

    public static final String ENTER_CONFIRM_PASSWORD = "Please enter confirm password";

    public static final String PASSWORD_NOT_MATCH = "Password and Confirm Password not match";

    public static final String SERVER_ERROR = "Server error, Please try again later";

    public static final String ERROR_CODE_SUCCESS = "0";

    public static final String ERROR_CODE_FAILURE = "1";

    public static final String STATUS_SUCCESS = "200";

    public static final String VERIFICATION_LINK_SENT = "We have sent you an activation link. Please check your email to activate your account!";

    public static final String EMAIL_ALREADY_EXISTS = "Email id already exists";

    public static final String LOGIN_FAILED = "Email id and Password not match";

    public static final String PROFILE_UPDATE_FAILED = "Update failed";

    public static final String ENTER_UNITS = "Please enter units";

    public static final String ENTER_AGE = "Please enter age";

    public static final String ENTER_WEIGHT = "Please enter weight";

    public static final String ENTER_HEIGHT = "Please enter height";

    public static final String ENTER_GENDER = "Please enter gender";

    public static final String ENTER_ACTIVITY_LEVEL = "Please enter activty level";

    public static final String ENTER_GOAL = "Please select goal";

    public static final String USER_NAME = "user_name";

    public static final String USER_STATUS = "user_status";

    public static final String USER_ID = "user_id";

    public static final String USER_DOB = "dob";

    public static final String USER_COUNTRY = "country";

    public static final String USER_PROFILE_PIC = "profile_pic";

    public static final String BODY_UNITS = "units";

    public static final String BODY_AGE = "age";

    public static final String BODY_WEIGHT = "weight";

    public static final String BODY_HEIGHT = "height";

    public static final String ALREADY_STORED_PREFERENCE = "already stored preference";

    public static final String MARK_EATEN_STATUS = "mark eaten status";

    public static final String SHARED_PREFES_PROFILE_CLICKED = "profile clicked";

    public static final String PROFILE_CLICKED = "profile selected";


    /* public static final String BODY_WEIGHT_KG = "weight_kg";

     public static final String BODY_WEIGHT_LBS = "weight_lbs";

     public static final String BODY_HEIGHT_CM = "height_cm";

     public static final String BODY_HEIGHT_FT_INCH = "body_height_ft_inch";
 */
    public static final String BODY_GENDER = "gender";

    public static final String BODY_ACTIVITY_LEVEL = "activity_level";

    public static final String BODY_CALORIES = "body_calories";

    public static final String GOAL_TYPE = "goal_type";

    public static final String GOAL_LIMIT = "goal_limit";

    public static final String GOAL_CALORIES = "goal_calories";

    public static final String PLAN_CALORIES = "plan_calorie";

//    public static final String GOAL_INDEX = "goal_index";

    public static String APP_USER_ID = "";

    public static final String UNIT_METRIC = "Metric";

    public static final String UNIT_USSYSTEM = "U.S. System";

    public static final String USER_ACTIVE = "Active";

    public static final String USER_INACTIVE = "Inactive";

    public static String baseDir = Environment.getExternalStorageDirectory().getAbsolutePath();

    //    public static String filepath = "";
    //------------------------------------HIDE FORAGER FOLDER------------------------------------------//
    public static final String FORAGER_DIR = "Forager";

    public static final String PROFILE_DIR = "Profile";

    public static final String PROFILE_FILENAME = "/profile_img.png";

    public static final String ABOUT_DIR = "About";

    public static final String SHOP_MEAL_PLAN_DIR = "Shopping Meal Plan";

    public static final String SHOP_MEAL_PLAN_LOGO_DIR = "Shopping Meal Plan Logo";


    public static final String PLANLISTS_FILENAME = "PlanLists";

    public static final String EXTRAFOODPLAN = "extraFoodPlan";

    public static final String TERMS_FILENAME = "TermsContent";

    public static final String GROCERYFOODPLAN = "groceryFoodPlan";


    public static final String ABOUT_FILENAME = "AboutContent";

    public static final int SHOP_MEAL_PLAN_COUNT = 2;

    public static final String SMALL_DESC = "small_desc";

    public static final String DETAIL_DESC = "detail_desc";


    public static final String ENTER_CHANGE_PASSWORD = "Please enter change password";

    public static final String ENTER_NEW_PASSWORD = "Please enter new password";


    public static final String LOGIN_CALL = "login";

    public static final String PROFILE_PLAN_IMAGE_CALL = "profile_plan image";

    public static final String UPDATE_ACTIVE_CALL = "update active";

    public static final String ABOUT_IMAGE_CALL = "about image call";

    public static final String FORGET_PASSWORD_CALL = "forget password call";

    public static final String REGISTER_CALL = "register call";

    public static final String CHANGE_PASSWORD_CALL = "change password call";


    public static final String PROFILE_CALL = "profile call";

    public static final String FEEDBACK_TO = "Forager@ForagerPro.com";
    public static final String FEEDBACK_SUBJECT = "Feedback";
    public static final String FEEDBACK_CONTENT =
            "Hello Forager Admin,\n" +
                    "\n" +
                    "I would like to suggest a new feature and leave feedback for ForagerPro:\n" +
                    "\n" +
                    "\n" +
                    "\n" +
                    "Thanks!";

    public static final String PASSWORD_CHANGED_SUCCESSFULLY = "Password has been updated successfully";
    public static final String BASIC_PLAN_GROCERY_LIST = "Basic Plan Grocery List";

    public static final String SKIP = "skip";

    public static final String FREQ_UPDATE_PREF = "";

    public static final int FORAGER_PURCHASE_SAVE_RESPONSE = 1021;

    public static final int FORAGER_PURCHASE_STATUS_RESPONSE = 1992;

    public static final  String PURCHASE_STATUS="STATUS";

    public static final  String IN_APP="in_app";

    public static final  String SUBSCRIPTION="subscription";
    public static final  String UPDATED_USER= "Due to the upgrade you will be reverted to the Basic Membership.Please Subscribe to the Pro membership for full functionalities.Thanks!";



}


