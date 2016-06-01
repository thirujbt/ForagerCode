package com.forager.meal.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.forager.meal.bean.PlanDetails;
import com.forager.meal.bean.ShopMealPlan;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Akbar on 9/2/2015.
 */
public class Database extends SQLiteOpenHelper {

    public static final String USER_TABLE = "user";

    public static final String PLAN_TABLE = "plan";

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

    public static final String BODY_GENDER = "gender";

    public static final String BODY_ACTIVITY_LEVEL = "activity_level";

    public static final String BODY_CALORIES = "body_calories";

    public static final String GOAL_TYPE = "goal_type";

    public static final String GOAL_LIMIT = "goal_limit";

    public static final String GOAL_CALORIES = "goal_calories";

    public static final String PLAN_CALORIES = "plan_calorie";

    public static final String  PLAN_ID= "id";

    public static final String PLAN_TYPE = "type";

    public static final String PLAN_PRODUCT_ID = "product_id";

    public static final String PLAN_IMAGE = "image";

    public static final String PLAN_MEAL_PLAN_LOGO = "meal_plan_logo";

    public static final String PLAN_PRICE = "price";

    public static final String PLAN_DESC = "description";

    public static final String PLAN_TIPS_GUIDELINES = "tips_guidelines";

    public static final String PLAN_DAILY_SUPPLEMENTS = "daily_supplements";

    public static final String PLAN_IMAGES = "plan_images";

    public static final String PLAN_IMAGES1 = "plan_images1";

    public static final String PLAN_IMAGES2 = "plan_images2";

    public static final String PLAN_IMAGES3 = "plan_images3";

    public static final String PLAN_IMAGES4 = "plan_images4";

    public static final String PLAN_IMAGES5 = "plan_images5";

    public static final String PLAN_IMAGES6 = "plan_images6";

    public static final String PLAN_IMAGES7 = "plan_images7";

    public static final String PLAN_IMAGES8 = "plan_images8";

    public static final String PLAN_IMAGES9 = "plan_images9";

    public static final String PLAN_IMAGES10 = "plan_images0";

    public static final String PLAN_PURCHASED_STATUS = "purchased_status";

    public static final String PLAN_ACTIVE_STATUS = "active_status";

    private AtomicInteger mOpenCounter = new AtomicInteger();

    private static Database mInstance;

    private static SQLiteDatabase myWritableDb;

    private static final String DATABASE_NAME = "user_plans";

    private static final int DATABASE_VERSION = 1;

    public static final String CREATE_USER_TABLE = "create table "+USER_TABLE+"( "+USER_NAME+" text, "
            +USER_STATUS+" text, "+USER_ID+" text, "+USER_DOB+" text, "+USER_COUNTRY+" text, "
            +USER_PROFILE_PIC+" text, "+BODY_UNITS+" text, "+BODY_AGE+" text, "+BODY_WEIGHT+" text"
            +BODY_HEIGHT+" text, "+BODY_GENDER+" text, "+BODY_ACTIVITY_LEVEL+" text, "+BODY_CALORIES+" text, "
            +GOAL_TYPE+" text, "+GOAL_LIMIT+" text, "+GOAL_CALORIES+" text, "+PLAN_CALORIES+" text);";

   /* public static final String CREATE_PLAN_TABLE = "create table "+PLAN_TABLE+"( "+ID+" text, "
            +PLAN_TYPE+" text, "+PLAN_PRODUCT_ID+" text, "+PLAN_IMAGE+" text, "+PLAN_MEAL_PLAN_LOGO+" text, "
            +PLAN_PRICE+" text, "+PLAN_DESC+" text, "+PLAN_TIPS_GUIDELINES+" text, "+PLAN_DAILY_SUPPLEMENTS+" text"
            +PLAN_IMAGES1+" text, "+PLAN_IMAGES2+" text, "+PLAN_IMAGES3+" text, "+PLAN_IMAGES4+" text, "
            +PLAN_IMAGES5+" text, "+PLAN_IMAGES6+" text, "+PLAN_IMAGES7+" text, "+PLAN_IMAGES8+" text, "
            +PLAN_IMAGES9+" text, "+PLAN_IMAGES10+" text, "+PLAN_PURCHASED_STATUS+" text, "+PLAN_ACTIVE_STATUS+" text);";*/

   /* public static final String CREATE_PLAN_TABLE = "create table "+PLAN_TABLE+"( "+ID+" text, "
            +PLAN_TYPE+" text, "+PLAN_PRODUCT_ID+" text, "+PLAN_IMAGE+" text, "+PLAN_MEAL_PLAN_LOGO+" text, "
            +PLAN_PRICE+" text, "+PLAN_DESC+" text, "+PLAN_TIPS_GUIDELINES+" text, "+PLAN_DAILY_SUPPLEMENTS+" text"
            +PLAN_IMAGES+" text, "+PLAN_PURCHASED_STATUS+" text, "+PLAN_ACTIVE_STATUS+" text);";*/

    public static final String CREATE_PLAN_TABLE = "create table "+PLAN_TABLE+"( "+PLAN_ID+" text, "
            +PLAN_TYPE+" text, "+PLAN_IMAGE+" text, "+PLAN_MEAL_PLAN_LOGO+" text, "
            +PLAN_DESC+" text, "+PLAN_TIPS_GUIDELINES+" text, "+PLAN_DAILY_SUPPLEMENTS+" text, "
            +PLAN_IMAGES+" text, "+PLAN_ACTIVE_STATUS+" text);";

    public Database(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_PLAN_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+PLAN_TABLE);

        onCreate(db);
    }

    public static Database getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new Database(context);
        }
        return mInstance;
    }

    /**
     * Returns a writable database instance in order not to open and close many
     * SQLiteDatabase objects simultaneously
     *
     * @return a writable instance to SQLiteDatabase
     */
    public synchronized  SQLiteDatabase getMyWritableDatabase() {
        if(mOpenCounter.incrementAndGet() == 1) {
            // Opening new database
            myWritableDb = this.getWritableDatabase();
        }
        return myWritableDb;
    }

    public synchronized void closeDatabase(){
        if(mOpenCounter.decrementAndGet() == 0) {
            // Closing database
            myWritableDb.close();

        }
    }

    public String getActivePlanType() {
        String planId = "";
        Cursor c = myWritableDb.rawQuery("select "+ PLAN_TYPE +" from " + PLAN_TABLE +" where "+PLAN_ACTIVE_STATUS +" = 1" , null);
        if(c != null && c.moveToFirst()) {
            planId = c.getString(0);
        }
        c.close();
        return planId;
    }


    public String getActivePlanDesc() {
        String planId = "";
        Cursor c = myWritableDb.rawQuery("select "+ PLAN_DESC +" from " + PLAN_TABLE +" where "+PLAN_ACTIVE_STATUS +" = 1" , null);
        if(c != null && c.moveToFirst()) {
            planId = c.getString(0);
        }
        c.close();
        return planId;
    }

    public String getActivePlanGuideLines() {
        String planSupp = "";
        Cursor c = myWritableDb.rawQuery("select "+ PLAN_TIPS_GUIDELINES +" from " + PLAN_TABLE +" where "+PLAN_ACTIVE_STATUS +" = 1" , null);
        if(c != null && c.moveToFirst()) {
            planSupp = c.getString(0);
        }
        c.close();
        return planSupp;
    }

    public String getActivePlanSupplements() {
        String planSupp = "";
        Cursor c = myWritableDb.rawQuery("select "+ PLAN_DAILY_SUPPLEMENTS +" from " + PLAN_TABLE +" where "+PLAN_ACTIVE_STATUS +" = 1" , null);
        if(c != null && c.moveToFirst()) {
            planSupp = c.getString(0);
        }
        c.close();
        return planSupp;
    }

    public String getActivePlanImageUrls() {
        String planImagesUrl = "";
        Cursor c = myWritableDb.rawQuery("select "+ PLAN_IMAGES +" from " + PLAN_TABLE +" where "+PLAN_ACTIVE_STATUS +" = 1" , null);
        if(c != null && c.moveToFirst()) {
            planImagesUrl = c.getString(0);
        }
        c.close();
        return planImagesUrl;
    }

    public void changeActivePlanId(int id) {
        String updateTemp = "UPDATE "+ PLAN_TABLE +" SET " + PLAN_ACTIVE_STATUS +" = 0 where "+PLAN_ID +" = \'"+getActivePlanId()+"\' ";
        myWritableDb.execSQL(updateTemp);
        updateTemp = "UPDATE "+ PLAN_TABLE +" SET " + PLAN_ACTIVE_STATUS +" = 1 where "+PLAN_ID +" = \'"+id+"\' ";
        myWritableDb.execSQL(updateTemp);
    }

    public String getActivePlanId() {
        String planId = "";
        Cursor c = myWritableDb.rawQuery("select "+ PLAN_ID +" from " + PLAN_TABLE +" where "+PLAN_ACTIVE_STATUS +" = 1" , null);
        if(c != null && c.moveToFirst()) {
            planId = c.getString(0);
        }
        c.close();
        return planId;
    }

    public ArrayList<ShopMealPlan> getShopMealPlan() {
        ArrayList<ShopMealPlan> resList = new ArrayList<>();
        resList.clear();
        Cursor c = myWritableDb.rawQuery("select "+PLAN_ID+", "+PLAN_DESC+", "+PLAN_ACTIVE_STATUS+" from "+PLAN_TABLE, null);
        System.out.println("checking count....."+c.getCount());
        if(c != null && c.getCount() > 0) {
            while(c.moveToNext()) {
                ShopMealPlan shopMealPlan = new ShopMealPlan();
                shopMealPlan.setId(Integer.parseInt(c.getString(c.getColumnIndex(PLAN_ID))));
                shopMealPlan.setDesc(c.getString(c.getColumnIndex(PLAN_DESC)));
                shopMealPlan.setActive_status(Integer.parseInt(c.getString(c.getColumnIndex(PLAN_ACTIVE_STATUS))));
                resList.add(shopMealPlan);
            }
        }
        return resList;
    }

    public ArrayList<String> getDescription() {

        ArrayList<String> resList = new ArrayList<>();
        Cursor c = myWritableDb.rawQuery("select "+PLAN_DESC+" from "+PLAN_TABLE, null);
        if(c != null && c.getCount() > 0) {
            while (c.moveToNext()) {
                resList.add(c.getString(0));
            }
        }
        return resList;
    }

    public ArrayList<String> getPlanType() {
        ArrayList<String> resList = new ArrayList<>();

        Cursor c = myWritableDb.rawQuery("select " + PLAN_TYPE + " from " + PLAN_TABLE, null);
        if(c != null && c.getCount() > 0) {
            while (c.moveToNext()) {
                resList.add(c.getString(0));
            }
        }
        return resList;
    }

    /**
     * method to insert the citation in DB when user click's done button
     * @param planDetailsList
     */
    public void insertPlanDB(List<PlanDetails> planDetailsList) {

        Cursor c = myWritableDb.rawQuery("select * from " + PLAN_TABLE, null);
        int count = c.getCount();

        if (planDetailsList.size() > 0) {
            if (count > 0) {
                deletePlanTableOldRecord();
            }

            for (PlanDetails mPlanDetails : planDetailsList) {

                ContentValues mValues = new ContentValues();
                mValues.put(PLAN_ID, mPlanDetails.getId());
                mValues.put(PLAN_TYPE, mPlanDetails.getType());
//            mValues.put(PLAN_PRODUCT_ID, mPlanDetails.getProduct_id());
                mValues.put(PLAN_IMAGE, mPlanDetails.getImage());
                mValues.put(PLAN_MEAL_PLAN_LOGO, mPlanDetails.getMeal_plan_logo());
//            mValues.put(PLAN_PRICE, mPlanDetails.getPrice());
                mValues.put(PLAN_DESC, mPlanDetails.getDescription());
                mValues.put(PLAN_TIPS_GUIDELINES, mPlanDetails.getTips_guidelines());
                mValues.put(PLAN_DAILY_SUPPLEMENTS, mPlanDetails.getDaily_supplements());
          /*  String args[] = mPlanDetails.getPlan_images();
            StringBuilder sb = new StringBuilder();
            for(int i=0;i<args.length;i++) {
                sb.append(args[i]);
                if(i+1 != args.length) {
                    sb.append(",");
                }
            }*/
                mValues.put(PLAN_IMAGES, mPlanDetails.getPlan_images());
//            mValues.put(PLAN_PURCHASED_STATUS, mPlanDetails.getPurchased_status());
                mValues.put(PLAN_ACTIVE_STATUS, mPlanDetails.getActive_status());
                myWritableDb.insert(PLAN_TABLE, null, mValues);
            }
        }
    }

    /**
     * delete the userdata table records
     * @return boolean value
     */

    public boolean deletePlanTableOldRecord() {
        return myWritableDb.delete(PLAN_TABLE, null, null) > 0;
    }

}

