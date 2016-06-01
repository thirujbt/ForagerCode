package com.forager.meal.utitlity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.forager.meal.adapter.DeleteShoppingAdapter;
import com.forager.meal.adapter.ShoppingListAdapter;
import com.forager.meal.adapter.TitleListAdapter;
import com.forager.meal.adapter.WrapperExpandableListAdapter;
import com.forager.meal.bean.PlanLists;
import com.forager.meal.bean.UserDetails;
import com.forager.meal.constants.AppConstants;
import com.forager.meal.constants.JsonConstants;
import com.forager.meal.ui.R;
import com.forager.meal.widget.FloatingGroupExpandableListView;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Akbar on 9/1/2015.
 */
public final class Utility {

    private static ProgressDialog mDialog = null;

    private static AlertDialog mSelectIPDialog = null;

    private static AlertDialog mErrorDialog = null;

    private Utility() {

    }


    /**
     * Check str is empty string
     *
     * @param str
     * @return true if str is empty else false
     */
    public static boolean isBlank(final String str) {
        int strLen;
        if (str == null || (strLen = str.length()) == 0 || str.equalsIgnoreCase("null")) {
            return true;
        }
        for (int i = 0; i < strLen; i++) {
            if ((Character.isWhitespace(str.charAt(i)) == false)) {
                return false;
            }
        }
        return true;
    }

    public static void SelectiPAddress(Activity activity, final String msg) {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(activity, R.style.CustomAlertDialogStyle);
        mBuilder.setTitle("Select Beacon")
                .setMessage(msg)
                .setCancelable(true);
        if (mSelectIPDialog == null) {
            mSelectIPDialog = mBuilder.create();
        }
        if (!mSelectIPDialog.isShowing()) {
            mSelectIPDialog.show();
        }
    }

    public static void writeIntoFile(Context context, String content, String filePath) {
        try {
            FileOutputStream outputStream = context.openFileOutput(filePath, Context.MODE_PRIVATE);
            outputStream.write(content.getBytes());
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getContentFromFile(Context context, String filePath) {
        try {
            InputStream inputStream = context.openFileInput(filePath);
            return Utility.getStringFromInputStream(inputStream);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }


    public static void showAlert(final Activity activity, final String msg, final boolean finish) {
        if (activity != null) {
            activity.runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    AlertDialog.Builder mBuilder = new AlertDialog.Builder(activity, R.style.CustomAlertDialogStyle);
                    mBuilder
                            .setMessage(msg)
                            .setCancelable(false)
                            .setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if (finish) {
                                        activity.finish();
                                    }
                                }
                            });

                    AlertDialog mErrorDialog = mBuilder.create();
                    mErrorDialog.show();
                }

            });
        }
    }

   /* public static String calcBMI() {
        UserDetails userDetails = UserDetails.getInstance();
        return String.valueOf(new DecimalFormat("0.#").format(Calculations.calcGoal((float) userDetails.getBody_calories(),
                userDetails.getGoal_enum())));
    }*/


    public static String calcBMI() {
        UserDetails userDetails = UserDetails.getInstance();
        return String.valueOf(Math.round(Calculations.calcGoal((float) userDetails.getBody_calories(),
                userDetails.getGoal_enum())));
    }

    public static String createResourceBitmap(String url, Context context) {
        String sdPath;

      /*  BitmapDrawable bd = (BitmapDrawable) getResources().getDrawable(R.drawable.skiptoset);
        Bitmap bitMap = bd.getBitmap();
*/
        Bitmap bitMap = BitmapFactory.decodeResource(context.getResources(), R.drawable.skiptoset);

        //  Uri uri = Uri.parse("android.resource://com.missme.kissme/raw/kiss");

        String fileName = "Forager/Profile/temp.jpeg";

        File mFile2 = new File(AppConstants.baseDir + "/" + AppConstants.FORAGER_DIR + "/Profile", "1.png");
        if (mFile2.exists()) {
            mFile2.delete();
        }
        try {
            FileOutputStream outStream;

            outStream = new FileOutputStream(mFile2);

            bitMap.compress(Bitmap.CompressFormat.PNG, 100, outStream);

            outStream.flush();

            outStream.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        sdPath = mFile2.getAbsolutePath();

        File temp = new File(sdPath);
//
        if (!temp.exists()) {
            Log.e("file", "no image file at location :" + sdPath);
        }
        return sdPath;
    }

    public static void showPDialog(final Activity activity) {
        if (activity != null)
            activity.runOnUiThread(new Runnable() {

                @Override
                public void run() {

                    if (mDialog == null) {
                        mDialog = new ProgressDialog(activity, R.style.CustomAlertDialogStyle);
                        mDialog.setCancelable(false);
                        mDialog.setIndeterminate(false);
                        mDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {

                            @Override
                            public void onDismiss(DialogInterface dialog) {
                                mDialog = null;
                            }
                        });
                    }
                    if (!mDialog.isShowing()) {
                        Log.e(getClass().getSimpleName(), "Hello, Showing");
                        mDialog.show();

                    }

                }
            });
    }


    public static void dismissPDialog(final Activity activity) {
        if (activity != null)
            activity.runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    try {
                        if (mDialog != null && mDialog.isShowing()) {
                            Log.e("Dismiss", "Hello, Dismiss");
                            mDialog.dismiss();
                            mDialog = null;
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

    }

    public static Calculations.GOAL_ENUM getGoalEnum(String units, String goal_str, Context context) {


        int goal_index;

        ArrayList<String> goal_array = new ArrayList<>();

        if (units.equals(AppConstants.UNIT_METRIC)) {
            goal_array = new ArrayList<>(Arrays.asList(context.getResources().getStringArray(R.array.choose_goal_array_metric)));

        } else if (units.equals(AppConstants.UNIT_USSYSTEM)) {
            goal_array = new ArrayList<>(Arrays.asList(context.getResources().getStringArray(R.array.choose_goal_array_ussystem)));
        }

        if (goal_array.contains(goal_str)) {
            goal_index = goal_array.indexOf(goal_str);
        } else {
            if (units.equals(AppConstants.UNIT_METRIC)) {
                goal_index = new ArrayList<>(Arrays.asList(context.getResources().getStringArray(R.array.choose_goal_array_ussystem))).indexOf(goal_str);
            } else {
                goal_index = new ArrayList<>(Arrays.asList(context.getResources().getStringArray(R.array.choose_goal_array_metric))).indexOf(goal_str);
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

        }

        return goalEnum;


    }

    public static void setListViewHeight(FloatingGroupExpandableListView listView,
                                         int group) {
        WrapperExpandableListAdapter listAdapter = (WrapperExpandableListAdapter) listView.getExpandableListAdapter();
        int totalHeight = 0;
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(),
                View.MeasureSpec.EXACTLY);
        for (int i = 0; i < listAdapter.getGroupCount(); i++) {
            View groupItem = listAdapter.getGroupView(i, false, null, listView);
            groupItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);

            totalHeight += groupItem.getMeasuredHeight();

            if (((listView.isGroupExpanded(i)) && (i != group))
                    || ((!listView.isGroupExpanded(i)) && (i == group))) {
                for (int j = 0; j < listAdapter.getChildrenCount(i); j++) {
                    View listItem = listAdapter.getChildView(i, j, false, null,
                            listView);
                    listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);

                    totalHeight += listItem.getMeasuredHeight();

                }
            }
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        int height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getGroupCount() - 1));
        if (height < 10)
            height = 200;
        params.height = height;
        listView.setLayoutParams(params);
        listView.requestLayout();

    }

    public static boolean isValidDate(String pDateString) {

        try {
            Date date = new SimpleDateFormat("MM/dd/yyyy").parse(pDateString);
            return new Date().after(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


    /**
     * Method to write ascii text characters to file on SD card. Note that you must add a
     * WRITE_EXTERNAL_STORAGE permission to the manifest file or this method will throw
     * a FileNotFound Exception because you won't have write permission.
     */

    public static void writeToSDFile(String data, String directory, String fileName) {


        File dir = new File(AppConstants.baseDir + "/" + AppConstants.FORAGER_DIR + "/" + directory);
        dir.mkdirs();

        File file = new File(dir, fileName);


        try {
            FileOutputStream f = new FileOutputStream(file, false);
            PrintWriter pw = new PrintWriter(f);
            pw.println(data);

            pw.flush();
            pw.close();
            f.close();


        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Log.i("File Writing", "******* File not found. Did you" +
                    " add a WRITE_EXTERNAL_STORAGE permission to the   manifest?");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String readFile(String directory, String fileName) {
        StringBuilder mbBuilder = new StringBuilder();

        try {

            BufferedReader br = new BufferedReader(new java.io.FileReader(AppConstants.baseDir + "/" +
                    AppConstants.FORAGER_DIR + "/" + directory + "/" + fileName));
            String line;

            while ((line = br.readLine()) != null) {
                mbBuilder.append(line);
                mbBuilder.append('\n');
            }
            br.close();

            Log.e("File Content", mbBuilder.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return mbBuilder.toString();
    }


    public static String getStringFromInputStream(InputStream is) {

        BufferedReader br = null;
        StringBuilder sb = new StringBuilder();

        String line;
        try {

            br = new BufferedReader(new InputStreamReader(is));
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return sb.toString();

    }

    /**
     * Network connected return true else false
     *
     * @return boolean
     */

    public static boolean isOnline(final Activity activity) {

        if (activity != null) {
            ConnectivityManager cm =
                    (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (cm.getActiveNetworkInfo() != null &&
                    cm.getActiveNetworkInfo().isConnectedOrConnecting()) {
                return true;
            } else {
                showAlert(activity, "Check your Internet Connection", false);
                return false;
            }
        }
        return false;

    }

    public static final boolean validateEmailId(String email) {

        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public static SharedPreferences getSharedPreferences(Context activity, String prefesName) {
        return activity.getSharedPreferences(prefesName, Context.MODE_PRIVATE);
    }


    public static void SaveBitmap(Bitmap bmp) {
        long filename = System.currentTimeMillis();
        try {
            //String filename1 = "_" + filepath;
            File folder = new File(AppConstants.baseDir, "Forager/Profile");
            folder.mkdir();
            if (folder.exists()) {
                File file = new File(folder, "profile_img" + ".png");
                file.createNewFile();
                OutputStream os = null;
                os = new FileOutputStream(file);
                Log.e("isStored", "" + bmp.compress(Bitmap.CompressFormat.PNG, 100, os));
                //AppConstants.filepath = file.getAbsolutePath();
                os.close();
            }
        } catch (IOException e) {
        } catch (Error er) {
        } catch (Exception e) {
        }
    }

    public static void getBitmapFromURL(final String src) {

    }

    public static void showToast(Context context, String message) {
        Toast toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.BOTTOM, 0, 0);
        toast.show();
    }


    public static HashMap sortByValues(HashMap map) {
        List list = new LinkedList(map.entrySet());
        // Defined Custom Comparator here
        Collections.sort(list, new Comparator() {
            public int compare(Object o1, Object o2) {
                return ((Comparable) ((Map.Entry) (o1)).getValue())
                        .compareTo(((Map.Entry) (o2)).getValue());
            }
        });

        // Here I am copying the sorted list in HashMap
        // using LinkedHashMap to preserve the insertion order
        HashMap sortedHashMap = new LinkedHashMap();
        for (Iterator it = list.iterator(); it.hasNext(); ) {
            Map.Entry entry = (Map.Entry) it.next();
            sortedHashMap.put(entry.getKey(), entry.getValue());
        }
        return sortedHashMap;
    }

    public static void removeSharedPrefes(Activity activity, PlanLists planLists) {
        SharedPreferences preferences = Utility.getSharedPreferences(activity, AppConstants.SHARED_PREFES_SHOP_LIST_DETAILS);

        SharedPreferences.Editor editor = preferences.edit();
        String planListKey = getPlanListKey(planLists);
        if (preferences.contains(planListKey)) {
            editor.remove(planListKey);

        }
        editor.commit();
    }

    public static void deleteFromPrefes(Activity activity) {
        for (int val : DeleteShoppingAdapter.mDeletePositions) {
            PlanLists planLists = ShoppingListAdapter.mDeleteShoppingList.get(val);
            removeSharedPrefes(activity, planLists);

        }
        ShoppingListAdapter.mDeleteShoppingList.clear();
        DeleteShoppingAdapter.mDeletePositions.clear();

    }

    public static int getSizeOfPrefes(Activity activity) {
        SharedPreferences preferences = Utility.getSharedPreferences(activity, AppConstants.SHARED_PREFES_SHOP_LIST_DETAILS);

        return preferences.getAll().size();
    }

    public static void storeSharedPrefes(Activity activity, PlanLists planLists) {
        SharedPreferences preferences = Utility.getSharedPreferences(activity, AppConstants.SHARED_PREFES_SHOP_LIST_DETAILS);
        SharedPreferences.Editor editor = preferences.edit();
        String planListKey = getPlanListKey(planLists);
        editor.putString(planListKey, planLists.getFoodName());
        editor.commit();
    }


    public static String getPlanListKey(PlanLists planLists) {
        if (planLists == null) {
            return "";
        } else if (planLists.getMealPlanType() == null) {
            return "";
        } else if (planLists.getCategory() == null) {
            return "";
        } else if (planLists.getFoodName() == null) {
            return "";
        }
        return planLists.getMealPlanType().toString() + "," + planLists.getCategory().toString() + "," +
                planLists.getFoodName().toString();
    }


    public static void storeToSharedPrefes(Activity activity) {
        for (PlanLists planLists : ShoppingListAdapter.mDeleteShoppingList) {
            storeSharedPrefes(activity, planLists);
        }
    }

    public static void resetFood(Context context) {
        TitleListAdapter.mainChart_Status = false;

        SharedPreferences preferences = Utility.getSharedPreferences(context, AppConstants.SHARED_PREFES_MEAL_PLAN_CHART_STATUS);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.commit();


        SharedPreferences prefer_meal_plan = Utility.getSharedPreferences(context, AppConstants.SHARED_PREFES_MEAL_PLANNER_DETAILS);
        SharedPreferences.Editor editor_meal_plan = prefer_meal_plan.edit();
        editor_meal_plan.clear();
        editor_meal_plan.commit();

        SharedPreferences prefer_extra_foods = Utility.getSharedPreferences(context, AppConstants.SHARED_PREFES_EXTRA_FOOD_DETAILS);
        SharedPreferences.Editor editor1 = prefer_extra_foods.edit();
        editor1.clear();
        editor1.commit();


        SharedPreferences prefer_mark_eaten = Utility.getSharedPreferences(context, AppConstants.SHARED_PREFES_MARK_EATEN_DETAILS);
        SharedPreferences.Editor edit = prefer_mark_eaten.edit();
        edit.clear();
        edit.commit();


        SharedPreferences prefer_chart_status = Utility.getSharedPreferences(context, AppConstants.SHARED_PREFES_MEAL_PLAN_CHART_STATUS);
        SharedPreferences.Editor editor_status = prefer_chart_status.edit();
        editor_status.clear();
        editor_status.commit();


        SharedPreferences preferences1 = Utility.getSharedPreferences(context, AppConstants.SHARED_PREFES_MARK_EATEN);
        SharedPreferences.Editor mark_eaten_editor = preferences1.edit();
        mark_eaten_editor.clear();
        mark_eaten_editor.commit();





    }

      /*
    Resize the taken image to 1MB and using bitmap factory
     */

    public static Bitmap callPhotoUploadMethod(String imagePath1) {
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(imagePath1, o);
        // The new size we want to scale to
        final int REQUIRED_SIZE = 1024;
        // Find the correct scale value. It should be the power of 2.
        int width_tmp = o.outWidth, height_tmp = o.outHeight;
        int scale = 1;

        while (true) {
            if (width_tmp < REQUIRED_SIZE && height_tmp < REQUIRED_SIZE)
                break;
            width_tmp /= 2;
            height_tmp /= 2;
            scale *= 2;
        }
        // Decode with inSampleSize
        BitmapFactory.Options o2 = new BitmapFactory.Options();
        o2.inSampleSize = scale;
        Bitmap bmp = BitmapFactory.decodeFile(imagePath1, o2);
        OutputStream outputStream = new ByteArrayOutputStream();
        // bmp.compress(Bitmap.CompressFormat.JPEG, 40, outputStream);
        return bmp;
    }

    public static void resetMealPlan(Context context, String MealType, ArrayList<String> mealList) {

        SharedPreferences preferences = Utility.getSharedPreferences(context, AppConstants.SHARED_PREFES_MEAL_PLANNER_DETAILS);
        SharedPreferences.Editor editor = preferences.edit();

        for (String category : mealList) {
            editor.remove(MealType + "," + category);
            editor.remove(MealType + "," + category + "," + JsonConstants.CALORIES);
            editor.remove(MealType + "," + category + "," + JsonConstants.CARBOHYDRATE);
            editor.remove(MealType + "," + category + "," + JsonConstants.FIBER);
            editor.remove(MealType + "," + category + "," + JsonConstants.FAT);
            editor.remove(MealType + "," + category + "," + JsonConstants.PROTEIN);
            editor.commit();

        }

        SharedPreferences prefer_extra_foods = Utility.getSharedPreferences(context, AppConstants.SHARED_PREFES_EXTRA_FOOD_DETAILS);
        SharedPreferences.Editor editor1 = prefer_extra_foods.edit();
        editor1.clear();
        editor1.commit();


        SharedPreferences prefer_mark_eaten = Utility.getSharedPreferences(context, AppConstants.SHARED_PREFES_MARK_EATEN_DETAILS);
        SharedPreferences.Editor edit = prefer_mark_eaten.edit();
        edit.putBoolean(AppConstants.ALREADY_STORED_PREFERENCE, false);
        edit.remove(MealType + "," + AppConstants.STATUS);
        edit.remove(MealType);
        edit.commit();

        SharedPreferences prefer_chart_status = Utility.getSharedPreferences(context, AppConstants.SHARED_PREFES_MEAL_PLAN_CHART_STATUS);
        SharedPreferences.Editor editor_status = prefer_chart_status.edit();
        editor_status.remove(MealType);
        editor_status.commit();


        SharedPreferences prefer = Utility.getSharedPreferences(context, AppConstants.SHARED_PREFES_EXTRA_FOOD_DETAILS);
        SharedPreferences.Editor editor_extraFoods = prefer.edit();
        editor_extraFoods.clear();
        editor_extraFoods.commit();


        SharedPreferences preferences1 = Utility.getSharedPreferences(context, AppConstants.SHARED_PREFES_MARK_EATEN);
        SharedPreferences.Editor mark_eaten_editor = preferences1.edit();
        mark_eaten_editor.clear();
        mark_eaten_editor.commit();

    }
}