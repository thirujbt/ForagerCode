package com.forager.meal.ui.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.forager.meal.adapter.ShopMealPlannerAdapter;
import com.forager.meal.async.PostAsync;
import com.forager.meal.bean.ShopMealPlan;
import com.forager.meal.bean.UserDetails;
import com.forager.meal.constants.AppConstants;
import com.forager.meal.constants.AppUrls;
import com.forager.meal.database.Database;
import com.forager.meal.listener.OnWebServiceResponse;
import com.forager.meal.ui.R;
import com.forager.meal.ui.activity.DashBoardActivity;
import com.forager.meal.utitlity.Utility;
import com.forager.meal.widget.RoundedImageView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by Chithra on 9/18/2015.
 */
public class YourProfileFragment extends Fragment implements View.OnClickListener, OnWebServiceResponse {

    private TextView mprofile_text, mbody_text, mgoal_text;
    private ImageButton profilepic;
    private ImageView mLoadImg;
    RoundedImageView mProfilePicImg;
    ImageView mProfilePicImg1;

    private TextView mCalories;
    Database mDatabase;
    TextView mUserName;
    TextView mAge;
    TextView mCountry;
    LinearLayout mealPlanImageLayout;
    UserDetails mUserDetails = UserDetails.getInstance();
    public static boolean profile_clicked_status = false;
    PlanControllerFragment food = new PlanControllerFragment();
    public static String calo;

    public static boolean updateCheck;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.activity_your_profile, container, false);
        PlanControllerFragment.flag = false;
        mDatabase = Database.getInstance(getActivity());
        mDatabase.getMyWritableDatabase();
        Log.e("User Details", " = " + mUserDetails.getCountry());
        findViews(rootView);

        return rootView;
    }


    @Override
    public void onResume() {
        super.onResume();

        if (updateCheck && isProfileModified()) {
            if (Utility.isOnline(getActivity())) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
                    new PostAsync(this, mUserDetails,
                            AppConstants.PROFILE_CALL).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, AppUrls.PROFILE_URL);
            } else
                new PostAsync(this, mUserDetails, AppConstants.PROFILE_CALL).execute(AppUrls.PROFILE_URL);
        } else {
            fillForms();
        }

    }

    @Override
    public void onDestroyView() {
        mDatabase.closeDatabase();
        super.onDestroyView();
    }

    private void findViews(View rootView) {
        mprofile_text = (TextView) rootView.findViewById(R.id.chg_txtprofile);
        mbody_text = (TextView) rootView.findViewById(R.id.chg_txtbody);
        mgoal_text = (TextView) rootView.findViewById(R.id.chg_txtgoal);
        mLoadImg = (ImageView) rootView.findViewById(R.id.load_img);
        mCalories = (TextView) rootView.findViewById(R.id.cal_text);
        mealPlanImageLayout = (LinearLayout) rootView.findViewById(R.id.plan_image_layout);
        mProfilePicImg = (RoundedImageView) rootView.findViewById(R.id.load_details_img);
        mProfilePicImg1 = (ImageView) rootView.findViewById(R.id.load_details_img_dupl);
        mUserName = (TextView) rootView.findViewById(R.id.load_txt1);
        mAge = (TextView) rootView.findViewById(R.id.load_txt2);
        mCountry = (TextView) rootView.findViewById(R.id.load_txt3);


        mProfilePicImg.setOnClickListener(this);
        mprofile_text.setOnClickListener(this);
        mbody_text.setOnClickListener(this);
        mgoal_text.setOnClickListener(this);
        mealPlanImageLayout.setOnClickListener(this);
        mProfilePicImg1.setOnClickListener(this);

    }


    private void fillForms() {
        SharedPreferences sharedPreferences = Utility.getSharedPreferences(getActivity(), AppConstants.SHARED_PREFES_USER_DETAILS);
        if (!Utility.isBlank(sharedPreferences.getString(AppConstants.USER_NAME, ""))) {
            mUserName.setText(sharedPreferences.getString(AppConstants.USER_NAME, ""));
        }
        if (!Utility.isBlank(sharedPreferences.getString(AppConstants.BODY_AGE, ""))) {
            String age = sharedPreferences.getString(AppConstants.BODY_AGE, "");
            String[] split = age.split(" ");
            age = "Age : " + split[0];
            mAge.setText(age);
        }
        if (!Utility.isBlank(sharedPreferences.getString(AppConstants.USER_COUNTRY, ""))) {
            mCountry.setText(sharedPreferences.getString(AppConstants.USER_COUNTRY, ""));
        }
        File profile = new File(AppConstants.baseDir + "/" + AppConstants.FORAGER_DIR + "/" + AppConstants.PROFILE_DIR + "/" + 1 + ".png");
        System.out.println("checking inside direc.............." + profile.getAbsolutePath());
          /*  if (sourceFile.exists()) {
                Bitmap myBitmap = BitmapFactory.decodeFile(sourceFile.getAbsolutePath());
                String detDesc = descriptions.get(i).toString();
                mHashMap.put(detDesc,myBitmap);
            }*/
        if (profile.exists()) {
          // Bitmap myBitmap = BitmapFactory.decodeFile(profile.getAbsolutePath());
           /* try {
                Bitmap myBitmap = callPhotoUploadMethod(profile.getAbsolutePath());
                mProfilePicImg.setImageBitmap(myBitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }*/
            ExifInterface ei;
            float angleValue = 0;
            try {
                ei = new ExifInterface(profile.toString());
                int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                        ExifInterface.ORIENTATION_NORMAL);
                switch (orientation) {
                    case ExifInterface.ORIENTATION_ROTATE_90:
                        angleValue = 90;
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_180:
                        angleValue = 180;
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_270:
                        angleValue = 270;
                        break;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            callPhotoUploadMethod(profile.toString(), angleValue);
        }
        String folder = mDatabase.getActivePlanType();
        calo = sharedPreferences.getString(AppConstants.PLAN_CALORIES, "1400");
        mCalories.setText(calo + " cal");

        File sourceFile = new File(AppConstants.baseDir + "/" + AppConstants.FORAGER_DIR + "/" + folder + "/" + AppConstants.SHOP_MEAL_PLAN_DIR + "/" + 1 + ".png");
        System.out.println("Source file is dowloaded");
          /*  if (sourceFile.exists()) {
                Bitmap myBitmap = BitmapFactory.decodeFile(sourceFile.getAbsolutePath());
                String detDesc = descriptions.get(i).toString();
                mHashMap.put(detDesc,myBitmap);
            }*/
        if (sourceFile.exists()) {
            Bitmap myBitmap = BitmapFactory.decodeFile(sourceFile.getAbsolutePath());
            mLoadImg.setImageBitmap(myBitmap);
            ShopMealPlan mealPlan = new ShopMealPlan();
            mealPlan.setBitmap(myBitmap);
            mealPlan.setActive_status(1);
            mealPlan.setDesc(mDatabase.getActivePlanDesc());
            ShopMealPlannerAdapter.shopMealPlan = mealPlan;
        }
    }

    private boolean isProfileModified() {

        SharedPreferences preferences = Utility.getSharedPreferences(getActivity(), AppConstants.SHARED_PREFES_USER_DETAILS);
        if (!preferences.getString(AppConstants.USER_NAME, "").equalsIgnoreCase(mUserDetails.getUser_name()) ||
                !preferences.getString(AppConstants.USER_DOB, "").equalsIgnoreCase(mUserDetails.getBirth_date()) ||
                !preferences.getString(AppConstants.USER_PROFILE_PIC, "").equalsIgnoreCase(mUserDetails.getProf_url()) ||
                !preferences.getString(AppConstants.USER_COUNTRY, "").equalsIgnoreCase(mUserDetails.getCountry()) ||
                !preferences.getString(AppConstants.BODY_UNITS, "").equalsIgnoreCase(mUserDetails.getUnits()) ||
                !preferences.getString(AppConstants.BODY_AGE, "").equalsIgnoreCase(mUserDetails.getAge()) ||
                !preferences.getString(AppConstants.BODY_WEIGHT, "").equalsIgnoreCase(mUserDetails.getWeight()) ||
                !preferences.getString(AppConstants.BODY_HEIGHT, "").equalsIgnoreCase(mUserDetails.getHeight()) ||
                !preferences.getString(AppConstants.BODY_GENDER, "").equalsIgnoreCase(mUserDetails.getGender()) ||
                !preferences.getString(AppConstants.BODY_ACTIVITY_LEVEL, "").equalsIgnoreCase(mUserDetails.getActivity_level()) ||
                !preferences.getString(AppConstants.BODY_CALORIES, "").equalsIgnoreCase(String.valueOf(mUserDetails.getBody_calories())) ||
                !preferences.getString(AppConstants.GOAL_TYPE, "").equalsIgnoreCase(mUserDetails.getGoal_type()) ||
                !preferences.getString(AppConstants.GOAL_LIMIT, "").equalsIgnoreCase(mUserDetails.getGoal_limit()) ||
                !preferences.getString(AppConstants.GOAL_CALORIES, "").equalsIgnoreCase(mUserDetails.getGoal_calories()) ||
                !preferences.getString(AppConstants.PLAN_CALORIES, "").equalsIgnoreCase(mUserDetails.getPlan_calorie())) {
            return true;
        }
        return false;
    }

    private void updateSharedPreference() {
        try {
            SharedPreferences.Editor editor = Utility.getSharedPreferences(getActivity(), AppConstants.SHARED_PREFES_USER_DETAILS).edit();
            editor.putString(AppConstants.USER_NAME, mUserDetails.getUser_name());
            editor.putString(AppConstants.USER_DOB, mUserDetails.getBirth_date());
            editor.putString(AppConstants.USER_COUNTRY, mUserDetails.getCountry());

            File profile = new File(AppConstants.baseDir + "/" + AppConstants.FORAGER_DIR + "/" + AppConstants.PROFILE_DIR + "/" + 1 + ".png");

            if (!profile.getAbsolutePath().equalsIgnoreCase(mUserDetails.getProf_url())) {
                try {
                    Bitmap bmp = callPhotoUploadMethod(mUserDetails.getProf_url());
                    File file = new File(AppConstants.baseDir, AppConstants.FORAGER_DIR + "/" + AppConstants.PROFILE_DIR + "/");

                    if (!file.exists()) {
                        file.mkdirs();
                    }

                    OutputStream output = new FileOutputStream(file + "/" + String.valueOf(1) + ".png");
                    bmp.compress(Bitmap.CompressFormat.PNG, 85, output);
                    output.flush();
                    output.close();
                    File file_temp = new File(mUserDetails.getProf_url());
                    if (file_temp.getAbsolutePath().contains("1_temp.png") && file_temp.exists()) {
                        file_temp.delete();
                    }
                    profile = new File(AppConstants.baseDir + "/" + AppConstants.FORAGER_DIR + "/" + AppConstants.PROFILE_DIR + "/" + 1 + ".png");
                    mUserDetails.setProf_url(profile.getAbsolutePath());
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            editor.putString(AppConstants.USER_PROFILE_PIC, mUserDetails.getProf_url());
            editor.putString(AppConstants.BODY_UNITS, mUserDetails.getUnits());
            editor.putString(AppConstants.BODY_AGE, mUserDetails.getAge());
            editor.putString(AppConstants.BODY_WEIGHT, mUserDetails.getWeight());
            editor.putString(AppConstants.BODY_HEIGHT, mUserDetails.getHeight());
            editor.putString(AppConstants.BODY_GENDER, mUserDetails.getGender());
            editor.putString(AppConstants.BODY_ACTIVITY_LEVEL, mUserDetails.getActivity_level());
            editor.putString(AppConstants.BODY_CALORIES, String.valueOf(mUserDetails.getBody_calories()));
            editor.putString(AppConstants.GOAL_TYPE, mUserDetails.getGoal_type());
            editor.putString(AppConstants.GOAL_LIMIT, mUserDetails.getGoal_limit());
            editor.putString(AppConstants.GOAL_CALORIES, mUserDetails.getGoal_calories());
            editor.putString(AppConstants.PLAN_CALORIES, mUserDetails.getPlan_calorie());
            //editor.putInt(AppConstants.GOAL_INDEX, mUserDetails.getGoal_index());
            editor.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void callPhotoUploadMethod(String imagePath1, float angle) {

        try {
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
            Bitmap bitmapOrg = BitmapFactory.decodeFile(imagePath1, o2);
            if (angle != 0) {
                bitmapOrg = rotateImage(bitmapOrg, angle);
            }
            if (ProfileFragment.getBooleanPreference(getActivity(), AppConstants.SKIP)) {
                mProfilePicImg.setImageBitmap(bitmapOrg);
                mProfilePicImg.setVisibility(View.GONE);
                mProfilePicImg1.invalidate();
                mProfilePicImg1.setVisibility(View.VISIBLE);
                mProfilePicImg1.setImageResource(R.drawable.skiptoset);

            } else {
                mProfilePicImg.setImageBitmap(bitmapOrg);
                mProfilePicImg1.setVisibility(View.GONE);
                mProfilePicImg.setVisibility(View.VISIBLE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private Bitmap rotateImage(Bitmap source, float angle) {
        Bitmap bitmap = null;
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        try {

            bitmap = Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
        } catch (OutOfMemoryError err) {
            err.printStackTrace();
        }
        // profileImage.setImageBitmap(bitmap);
        return bitmap;
    }

    public Bitmap callPhotoUploadMethod(String imagePath1) {
        BitmapFactory.Options o2 = null;
        try {
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
            o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return BitmapFactory.decodeFile(imagePath1, o2);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onClick(View view) {
        Intent changeProf = new Intent(getActivity(), DashBoardActivity.class);
        if (view == mprofile_text || view == mProfilePicImg || view == mProfilePicImg1) {
            changeProf.putExtra("dashboard", "changeprofile");
        } else if (view == mbody_text) {
            changeProf.putExtra("dashboard", "changebody");
        } else if (view == mgoal_text) {
            changeProf.putExtra("dashboard", "changegoal");
        } else if (view == mealPlanImageLayout) {
            profile_clicked_status = true;
            changeProf.putExtra("dashboard", "detailStoreMealTag");
        }

        getActivity().overridePendingTransition(0, 0);
        changeProf.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        getActivity().finish();
        getActivity().overridePendingTransition(0, 0);
        getActivity().startActivity(changeProf);

    }

    private boolean isMarkEatenPrefesAvailable(String mealType) {
        SharedPreferences preferences = Utility.getSharedPreferences(getActivity(), AppConstants.SHARED_PREFES_MARK_EATEN);
        String planListKey = mealType;
        if (preferences.contains(planListKey)) {
            return true;
        }
        return false;
    }

    @Override
    public void onResponseReceived(String response, String methodName) {
        updateSharedPreference();
        fillForms();
        updateCheck = false;
    }
}
