package com.forager.meal.ui.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.forager.meal.application.MyApplication;
import com.forager.meal.bean.UserDetails;
import com.forager.meal.constants.AppConstants;
import com.forager.meal.listener.AsyncResponse;
import com.forager.meal.ui.R;
import com.forager.meal.ui.fragment.ProfileFragment;
import com.forager.meal.utitlity.ImageLoader1;
import com.forager.meal.utitlity.Utility;
import com.forager.meal.widget.RoundedImageView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.peaksware.wheel.OnWheelScrollListener;
import com.peaksware.wheel.WheelView;
import com.peaksware.wheel.adapter.ArrayWheelAdapter;
import com.peaksware.wheel.adapter.NumericWheelAdapter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static android.provider.MediaStore.ACTION_IMAGE_CAPTURE;
import static android.provider.MediaStore.EXTRA_OUTPUT;


public class ProfileActivity extends Activity implements View.OnFocusChangeListener, View.OnClickListener, AsyncResponse {

    DisplayImageOptions options;

    String prof_image_path;
    RoundedImageView profileImage;

    ImageView profileImage1;
    ImageLoader imageLoader1;
    ImageLoader1 imageLoader;


    private ArrayWheelAdapter mMonthAdapter;
    private NumericWheelAdapter mDateAdapter;
    private NumericWheelAdapter mYearAdapter;
    private ArrayWheelAdapter mCountryAdapter;

    private WheelView mDateWheel;
    private WheelView mMonthWheel;
    private WheelView mYearWheel;
    private WheelView mCountryWheel;

    private ImageButton mDone;
    private ImageButton mClose;


    private EditText mBirthdate_Edit;
    private EditText mCountry_Edit;
    private TextView mPickerTitle_Edit;
    private EditText mName_Edit;


    private LinearLayout mPickerLayout;
    private LinearLayout mDOBLayout;

    private static final int DATE_MIN = 1;
    private static final int DATE_MAX = 31;

    private static final int YEAR_MIN = 1;
    private static final int YEAR_MAX = 10000;

    private int curYear;
    private int curMonth;
    private int curDate;

    private static final int STATIC_DATE = 0;
    private static final int STATIC_MONTH = 0;

    private static int STATIC_YEAR = 1980 - 1;

    private Context context;

    Button mSaveBtn;

    private boolean isProfileChoosen;

    private static final int birthday = 0;

    private static final int country = 1;

    private int visible_view = -1;

    ArrayList<String> countries;

    ArrayList<String> monthList;

    private int date_index = 0;

    private int month_index = 0;

    private int year_index = 0;

    private int country_index = 0;

    private static final int PICK_IMAGE__GALLERY_REQUEST = 1;

    private static final int PICK_IMAGE__CAMERA_REQUEST = 2;

    private boolean isPictureTaken;

    private TextView titleCreateProfile;

    private Button saveBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.out.println("oncreate .....");
        setContentView(R.layout.activity_profile);

        context = ProfileActivity.this;
        findViews();
        // prof_image_path=String.valueOf(R.drawable.launch_icon);


        // getDetails();
        init();


//        loadProfilCoverImage();
    }

    private void findViews() {
        MyApplication.setContext(ProfileActivity.this);

        titleCreateProfile = (TextView) findViewById(R.id.title_create_profile);
        titleCreateProfile.setTypeface(MyApplication.getFont());
        saveBtn = (Button) findViewById(R.id.save_btn);
        saveBtn.setOnClickListener(this);

    }


   /* public void loadProfilCoverImage(){

        imageLoader1.loadImage(prof_image_path, new SimpleImageLoadingListener() {
            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                super.onLoadingComplete(imageUri, view, loadedImage);
                profileImage.setBackgroundDrawable(new BitmapDrawable(loadedImage));
            }
        });
    }*/

    private void showPickerDialog() {
        final Dialog mDialog = new Dialog(this, R.style.CustomAlertDialogStyle);
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        WindowManager.LayoutParams wmlp = mDialog.getWindow().getAttributes();
        wmlp.gravity = Gravity.BOTTOM;
        mDialog.setContentView(R.layout.dialog_profile_picker);
        TextView mSkip = (TextView) mDialog.findViewById(R.id.profile_skip);
        TextView mTakePhoto = (TextView) mDialog.findViewById(R.id.take_photo);
        TextView mUploadLib = (TextView) mDialog.findViewById(R.id.upload_library);

        TextView mCancel = (TextView) mDialog.findViewById(R.id.cancel);

        mSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                profileImage.setVisibility(View.GONE);
                profileImage1.setVisibility(View.VISIBLE);
                profileImage1.setImageResource(R.drawable.skiptoset);
                ProfileFragment.setSharedPreference(ProfileActivity.this, AppConstants.SKIP, true);
                isPictureTaken = false;
                mDialog.dismiss();
                isProfileChoosen = true;
            }
        });

        mTakePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                camera();
                mDialog.dismiss();
            }
        });

        mUploadLib.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gallery();
                mDialog.dismiss();
            }
        });

        mCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
            }
        });
        mDialog.show();
    }

    private void gallery() {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, PICK_IMAGE__GALLERY_REQUEST);
    }

   /* private void camera() {
        PackageManager pm = getPackageManager();
        if(pm.hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            File file = new File(Environment.getExternalStorageDirectory() + "/DCIM/", "image" + new Date().getTime() + ".png");
            prof_image_path = file.getAbsolutePath();
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file)); // set the image file name
            startActivityForResult(intent, PICK_IMAGE__CAMERA_REQUEST);
        } else {
            Toast.makeText(this, "Camera feature not available in your device", Toast.LENGTH_LONG).show();
        }
    }*/

    private void camera() {

        try {
            PackageManager pm = getPackageManager();
            if (pm.hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
                Intent intent = new Intent(ACTION_IMAGE_CAPTURE);
                File file = new File(Environment.getExternalStorageDirectory() + "/DCIM/", "image" + new Date().getTime() + ".png");
                prof_image_path = file.getAbsolutePath();
                intent.putExtra(EXTRA_OUTPUT, Uri.fromFile(file)); // set the image file name
                startActivityForResult(intent, PICK_IMAGE__CAMERA_REQUEST);
            } else {
                Utility.showAlert(ProfileActivity.this, "Camera feature not available in your device", false);
                //Toast.makeText(getActivity(), "Camera feature not available in your device", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Utility.showAlert(ProfileActivity.this, "Camera open error in your device", false);
            Toast.makeText(ProfileActivity.this, "Camera open error in your device", Toast.LENGTH_LONG).show();

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == PICK_IMAGE__GALLERY_REQUEST) {
                Uri selectedImage = data.getData();

                Cursor cur = getContentResolver().query(selectedImage, new String[]{MediaStore.MediaColumns.DATA}, null, null, null);
                if (cur != null) {
                    cur.moveToFirst();
                    prof_image_path = cur.getString(0);
                    System.out.println("from galary.................." + prof_image_path);
                }

                ExifInterface ei;
                float angleValue = 0;
                try {
                    ei = new ExifInterface(prof_image_path);
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
                    isProfileChoosen = true;
                } catch (IOException e) {
                    e.printStackTrace();
                }
                callPhotoUploadMethod(prof_image_path, angleValue);
            } else if (requestCode == PICK_IMAGE__CAMERA_REQUEST) {
                ExifInterface ei;
                float angleValue = 0;
                try {
                    ei = new ExifInterface(prof_image_path);
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
                    isProfileChoosen = true;
                } catch (IOException e) {
                    e.printStackTrace();
                }
                callPhotoUploadMethod(prof_image_path, angleValue);
                //loadImageFromFile(prof_image_path);
                System.out.println("from camera.............." + prof_image_path);

            } else {
                super.onActivityResult(requestCode, resultCode, data);
            }

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
            ProfileFragment.setSharedPreference(ProfileActivity.this, AppConstants.SKIP, false);
            profileImage1.setVisibility(View.GONE);
            profileImage.setVisibility(View.VISIBLE);

            if (!ProfileFragment.getBooleanPreference(ProfileActivity.this, AppConstants.SKIP)) {
                profileImage.setImageBitmap(bitmapOrg);
            } else {
                profileImage.setVisibility(View.GONE);
                profileImage1.setVisibility(View.VISIBLE);
            }

            isPictureTaken = true;

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

    private void init() {
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.profile)
                .showImageForEmptyUri(R.drawable.profile)
                .showImageOnFail(R.drawable.profile)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();

        profileImage = (RoundedImageView) findViewById(R.id.profile_img);
        profileImage1 = (ImageView) findViewById(R.id.profile_img_second);
        imageLoader = new ImageLoader1(ProfileActivity.this);
        imageLoader1 = ImageLoader.getInstance();
        imageLoader1.init(ImageLoaderConfiguration.createDefault(ProfileActivity.this));
        profileImage1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPickerDialog();
            }
        });
        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPickerDialog();
            }
        });

        mName_Edit = (EditText) findViewById(R.id.edit_name);
        mBirthdate_Edit = (EditText) findViewById(R.id.edit_birthdate);
        mCountry_Edit = (EditText) findViewById(R.id.edit_country);
        mBirthdate_Edit.setKeyListener(null);
        mCountry_Edit.setKeyListener(null);


        mBirthdate_Edit.setOnClickListener(this);
        mCountry_Edit.setOnClickListener(this);

        mPickerTitle_Edit = (TextView) findViewById(R.id.picker_title);
        mPickerLayout = (LinearLayout) findViewById(R.id.picker_layout);
        mDOBLayout = (LinearLayout) findViewById(R.id.dob_layout);

        mBirthdate_Edit.setOnFocusChangeListener(this);
        mCountry_Edit.setOnFocusChangeListener(this);

        mSaveBtn = (Button) findViewById(R.id.save_btn);
        mSaveBtn.setOnClickListener(this);

        Calendar mCalendar = Calendar.getInstance();
        curDate = mCalendar.get(Calendar.DATE) - 1;
        curMonth = mCalendar.get(Calendar.MONTH);
        curYear = mCalendar.get(Calendar.YEAR) - 1;

        monthList = new ArrayList<String>(Arrays.asList(getResources().getStringArray(R.array.months_array)));
        mDateWheel = (WheelView) findViewById(R.id.date_wheel);
        mDateAdapter = new NumericWheelAdapter(context, DATE_MIN, DATE_MAX);
        mDateWheel.setViewAdapter(mDateAdapter);
        mDateWheel.setCyclic(true);
        mDateWheel.setCurrentItem(STATIC_DATE);
        mDateAdapter.setIndex(mDateWheel.getCurrentItem());
        mDateWheel.addScrollingListener(mOnWheelScrollListener);
        date_index = mDateWheel.getCurrentItem();

        mMonthWheel = (WheelView) findViewById(R.id.month_wheel);
        mMonthAdapter = new ArrayWheelAdapter(context, monthList.toArray());
        mMonthWheel.setViewAdapter(mMonthAdapter);
        mMonthWheel.setCyclic(true);
        mMonthWheel.setCurrentItem(STATIC_MONTH);
        mMonthAdapter.setIndex(mMonthWheel.getCurrentItem());
        mMonthWheel.addScrollingListener(mOnWheelScrollListener);
        month_index = mMonthWheel.getCurrentItem();

        mYearWheel = (WheelView) findViewById(R.id.year_wheel);
        mYearAdapter = new NumericWheelAdapter(context, YEAR_MIN, YEAR_MAX);
        mYearWheel.setViewAdapter(mYearAdapter);
        mYearWheel.setCyclic(true);
       /* int year = Calendar.getInstance().get(Calendar.YEAR);
        STATIC_YEAR = (year - 20) - 1;*/
        mYearWheel.setCurrentItem(STATIC_YEAR);
        mYearAdapter.setIndex(mYearWheel.getCurrentItem());
        mYearWheel.addScrollingListener(mOnWheelScrollListener);
        year_index = mYearWheel.getCurrentItem();

        String[] locales = Locale.getISOCountries();
        countries = new ArrayList();
        int i = 0;
        for (String countryCode : locales) {

            Locale obj = new Locale("", countryCode);

            countries.add(obj.getDisplayCountry());
            if (obj.getCountry().equalsIgnoreCase("US")) {
                countries.add(0, obj.getDisplayCountry());
            }
            System.out.println("Country Code = " + obj.getCountry()
                    + ", Country Name = " + obj.getDisplayCountry());

        }
        mCountryWheel = (WheelView) findViewById(R.id.country_wheel);
        mCountryAdapter = new ArrayWheelAdapter(context, countries.toArray());
        mCountryWheel.setViewAdapter(mCountryAdapter);
        mCountryAdapter.setIndex(mCountryWheel.getCurrentItem());
        mCountryWheel.addScrollingListener(mOnWheelScrollListener);
        country_index = mCountryWheel.getCurrentItem();

        mClose = (ImageButton) findViewById(R.id.picker_close);
        mDone = (ImageButton) findViewById(R.id.picker_select);
        mClose.setOnClickListener(this);
        mDone.setOnClickListener(this);

       /* mBirthdate_Edit.setText(formDatefromWheel());
        mCountry_Edit.setText(mCountryAdapter.getItemText(mCountryWheel.getCurrentItem()));*/

        fillDatasFromPrefes();
    }

    private void fillDatasFromPrefes() {
        SharedPreferences preferences = Utility.getSharedPreferences(ProfileActivity.this, AppConstants.SHARED_PREFES_USER_DETAILS);
        if (!Utility.isBlank(preferences.getString(AppConstants.USER_NAME, ""))) {
            mName_Edit.setText(preferences.getString(AppConstants.USER_NAME, ""));
        }

        if (!Utility.isBlank(preferences.getString(AppConstants.USER_DOB, ""))) {
            String date = preferences.getString(AppConstants.USER_DOB, "");
            mBirthdate_Edit.setText(date);
            String[] splitString = date.split("-");
            if (splitString.length == 3) {
                int date1 = Integer.parseInt(splitString[0]);
                mDateWheel.setCurrentItem(date1 - 1, false);
                mDateAdapter.setIndex(mDateWheel.getCurrentItem());
                date_index = mDateWheel.getCurrentItem();
                mMonthWheel.setCurrentItem(monthList.indexOf(splitString[1]), false);
                mMonthAdapter.setIndex(mMonthWheel.getCurrentItem());
                month_index = mMonthWheel.getCurrentItem();
                int year1 = Integer.parseInt(splitString[2]);
                mYearWheel.setCurrentItem(year1 - 1, false);
                mYearAdapter.setIndex(mYearWheel.getCurrentItem());
                year_index = mYearWheel.getCurrentItem();
            }
        }

        if (!Utility.isBlank(preferences.getString(AppConstants.USER_COUNTRY, ""))) {
            String country1 = preferences.getString(AppConstants.USER_COUNTRY, "");
            mCountry_Edit.setText(country1);
            mCountryWheel.setCurrentItem(countries.indexOf(country1));
            mCountryAdapter.setIndex(mCountryWheel.getCurrentItem());
            country_index = mCountryWheel.getCurrentItem();
        }

        prof_image_path = preferences.getString(AppConstants.USER_PROFILE_PIC, "");

        if (!Utility.isBlank(prof_image_path)) {
            prof_image_path = AppConstants.baseDir + "/" + AppConstants.FORAGER_DIR + "/" + AppConstants.PROFILE_DIR + "/1.png";

            System.out.println("checking inside direc.............." + prof_image_path);
            Bitmap bitmap = BitmapFactory.decodeFile(prof_image_path);
            profileImage1.setVisibility(View.GONE);
            profileImage.setVisibility(View.VISIBLE);
            if (!ProfileFragment.getBooleanPreference(ProfileActivity.this, AppConstants.SKIP)) {
                profileImage.setImageBitmap(bitmap);
            } else {
                profileImage.setVisibility(View.GONE);
                profileImage1.setVisibility(View.VISIBLE);
            }
            isProfileChoosen = true;
        }

       /* if(Utility.isBlank(prof_image_path)){
            prof_image_path = AppConstants.baseDir+"/"+AppConstants.PROFILE_DIR+AppConstants.PROFILE_FILENAME;

            System.out.println("checking inside direc.............."+prof_image_path);
            Bitmap bitmap = BitmapFactory.decodeFile(prof_image_path);
            profileImage.setImageBitmap(bitmap);
        }else{
            prof_image_path = preferences.getString(AppConstants.USER_PROFILE_PIC, "");
            System.out.println("checking.............."+prof_image_path);
            loadProfilImage(prof_image_path);
        }*/

       /* if(!prof_image_path.equalsIgnoreCase("") && prof_image_path != null && prof_image_path.length() > 0) {
            isProfileChoosen = true;
        }*/


        System.out.println("checking shared pref.............." + prof_image_path);


    }

    final Handler mHandler = new Handler();

    Runnable r = new Runnable() {
        @Override
        public void run() {
          /*
            profileImage1.setVisibility(View.GONE);
            profileImage.setVisibility(View.VISIBLE);
            profileImage.setImageBitmap(myBitmap);*/

            if (!ProfileFragment.getBooleanPreference(ProfileActivity.this, AppConstants.SKIP)) {
                profileImage.setImageBitmap(myBitmap);
                profileImage1.setVisibility(View.GONE);
            } else {
                profileImage.setVisibility(View.GONE);
                profileImage1.setVisibility(View.VISIBLE);
            }

        }
    };
    Bitmap myBitmap;

    public void getBitmapFromURL(final String src) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL(src);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setDoInput(true);
                    connection.connect();
                    InputStream input = connection.getInputStream();
                    myBitmap = BitmapFactory.decodeStream(input);
                    mHandler.post(r);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private String formDatefromWheel() {
        StringBuilder mDOBBuilder = new StringBuilder();
        mDOBBuilder.append(mDateAdapter.getItemText(mDateWheel.getCurrentItem()) + "-");
        date_index = mDateWheel.getCurrentItem();
        mDOBBuilder.append(mMonthAdapter.getItemText(mMonthWheel.getCurrentItem()) + "-");
        month_index = mMonthWheel.getCurrentItem();
        mDOBBuilder.append(mYearAdapter.getItemText(mYearWheel.getCurrentItem()));
        year_index = mYearWheel.getCurrentItem();
        return mDOBBuilder.toString();
    }

  /*  OnWheelChangedListener mOnWheelChangedListener = new OnWheelChangedListener() {
        @Override
        public void onChanged(WheelView wheel, int oldValue, int newValue) {

        }
    };*/

    OnWheelScrollListener mOnWheelScrollListener = new OnWheelScrollListener() {
        @Override
        public void onScrollingStarted(WheelView wheel) {

        }

        @Override
        public void onScrollingFinished(WheelView wheel) {

            Calendar calendar = Calendar.getInstance();

            int thisYear = calendar.get(Calendar.YEAR);

            int thisMonth = calendar.get(Calendar.MONTH);

            int thisDay = calendar.get(Calendar.DAY_OF_MONTH);

            String string = null;
            int temp_year_index = 0;
            int temp_month_index = 0;
            int temp_date_index = 0;
            if (wheel == mYearWheel || wheel == mMonthWheel || wheel == mDateWheel) {

                temp_year_index = mYearWheel.getCurrentItem() + 1;
                temp_month_index = mMonthWheel.getCurrentItem() + 1;
                temp_date_index = mDateWheel.getCurrentItem() + 1;
                string = new String(temp_month_index + "/" + temp_date_index + "/" + temp_year_index);

            }


            if (wheel == mYearWheel) {

                mYearAdapter.setIndex(wheel.getCurrentItem());


            } else if (wheel == mMonthWheel) {
                mMonthAdapter.setIndex(wheel.getCurrentItem());

            } else if (wheel == mDateWheel) {
                mDateAdapter.setIndex(wheel.getCurrentItem());

            } else if (wheel == mCountryWheel) {
                mCountryAdapter.setIndex(wheel.getCurrentItem());
            }

            if ((wheel == mYearWheel || wheel == mMonthWheel || wheel == mDateWheel) && !Utility.isValidDate(string)) {
                if (mYearWheel.getCurrentItem() >= thisYear) {
                    int yearScroll = temp_year_index - thisYear;
                    mYearWheel.scroll(-yearScroll, 1000);
                    mYearAdapter.setIndex(--thisYear);
                }
                if (mMonthWheel.getCurrentItem() >= thisMonth) {
                    int monthScroll = temp_month_index - (thisMonth + 1);
                    mMonthWheel.scroll(-monthScroll, 1000);
                    mMonthAdapter.setIndex(thisMonth);
                }
                if (mDateWheel.getCurrentItem() >= thisDay) {
                    int dateScroll = temp_date_index - thisDay;
                    mDateWheel.scroll(-dateScroll, 1000);
                    mDateAdapter.setIndex(--thisDay);
                }
            }

        }
    };


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void loadProfilImage(String prof_image) {

        ImageLoader.getInstance().displayImage(prof_image, profileImage, options, new SimpleImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {
            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
            }

            @Override
            public void onLoadingComplete(final String imageUri, View view, final Bitmap loadedImage) {
                final ImageView imageView = (ImageView) view;
                ViewTreeObserver observerProfileImg = imageView.getViewTreeObserver();
                observerProfileImg.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {

                    @Override
                    public boolean onPreDraw() {
                        imageView.getViewTreeObserver().removeOnPreDrawListener(this);
                        if (imageUri != null && !imageUri.equals("null") && loadedImage != null && !imageUri.equalsIgnoreCase("")) {
                            Bitmap bitmap = ImageLoader1.getRoundCroppedBitmapimg(loadedImage, imageView.getWidth());
                            imageView.setImageBitmap(bitmap);
                        }
                        return true;
                    }
                });
            }
        });
    }

    private void changeIntoCountryView(boolean showCountry) {
        if (showCountry) {
            mPickerTitle_Edit.setText(getResources().getString(R.string.country));
            mDOBLayout.setVisibility(View.GONE);
            mCountryWheel.setCurrentItem(country_index, false);
            mCountryAdapter.setIndex(country_index);
            mCountryWheel.setVisibility(View.VISIBLE);

        } else {
            mPickerTitle_Edit.setText(getResources().getString(R.string.dob));
            mDOBLayout.setVisibility(View.VISIBLE);
            mDateWheel.setCurrentItem(date_index, false);
            mMonthWheel.setCurrentItem(month_index, false);
            mYearWheel.setCurrentItem(year_index, false);
            mDateAdapter.setIndex(date_index);
            mMonthAdapter.setIndex(month_index);
            mYearAdapter.setIndex(year_index);
            mCountryWheel.setVisibility(View.GONE);

        }
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {

        if (hasFocus) {
            mPickerLayout.setVisibility(View.VISIBLE);
            hideKeyboard(ProfileActivity.this);
            if (mBirthdate_Edit == v) {
                visible_view = birthday;
                changeIntoCountryView(false);
            } else if (mCountry_Edit == v) {
                visible_view = country;
                changeIntoCountryView(true);
            }
        } else {
            visible_view = -1;
            mPickerLayout.setVisibility(View.GONE);

        }
    }


    public static void hideKeyboard(final Activity activity) {
        if (activity != null) {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
                }
            });
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.picker_close:
                mPickerLayout.setVisibility(View.GONE);
                break;
            case R.id.picker_select:
                if (visible_view != -1) {
                    if (visible_view == birthday) {
                        mBirthdate_Edit.setText(formDatefromWheel());
                    } else if (visible_view == country) {
                        country_index = mCountryWheel.getCurrentItem();
                        mCountry_Edit.setText(mCountryAdapter.getItemText(mCountryWheel.getCurrentItem()));
                    }
                }
                mPickerLayout.setVisibility(View.GONE);
                break;
            case R.id.edit_country:
                changeIntoCountryView(true);
                mPickerLayout.setVisibility(View.VISIBLE);
                break;
            case R.id.edit_birthdate:
                changeIntoCountryView(false);
                mPickerLayout.setVisibility(View.VISIBLE);
                break;
            case R.id.save_btn:
                validate_Fields();
                break;
        }
    }

    private void validate_Fields() {
        if (Utility.isBlank(mName_Edit.getText().toString())) {
            Utility.showAlert(ProfileActivity.this, AppConstants.ENTER_NAME, false);
        } else if (Utility.isBlank(mBirthdate_Edit.getText().toString())) {
            Utility.showAlert(ProfileActivity.this, AppConstants.ENTER_DOB, false);
        } else if (Utility.isBlank(mCountry_Edit.getText().toString())) {
            Utility.showAlert(ProfileActivity.this, AppConstants.ENTER_COUNTRY, false);
        } else if (!isProfileChoosen) {
            Utility.showAlert(ProfileActivity.this, AppConstants.ENTER_PROFILE, false);
        } else {
            UserDetails mUserDetails = UserDetails.getInstance();
            mUserDetails.setUser_name(mName_Edit.getText().toString());
            mUserDetails.setBirth_date(mBirthdate_Edit.getText().toString());
            mUserDetails.setCountry(mCountry_Edit.getText().toString());

            //mUserDetails.setIsPictureTaken(isPictureTaken);
            if (!isPictureTaken) {
//                mUserDetails.setProfile_pic(prof_image_path);
                //mUserDetails.setProf_image(BitmapFactory.decodeFile(prof_image_path));
                // mUserDetails.setProf_image(BitmapFactory.decodeResource(getResources(), R.drawable.launch_icon));
//                Uri uri = Uri.parse("android.resource://com.forager.pickzy.ui/drawable/launch_icon.png");
              /*   final File sourceFile = new File("android.resource://com.forager.pickzy.ui/raw/forager_leaf.png");
                 Uri uri = Uri.parse("android.resource://com.forager.pickzy.ui/raw/forager_leaf");
//                    Uri path = Uri.parse("android.resource://com.forager.pickzy.ui/drawable/launch_icon.png");

              System.out.println("checking path........."+sourceFile+":"+uri);
//               Uri uri =Uri.parse("android.resource://com.forager.pickzy.ui/raw/forager_leaf.png");

               prof_image_path = uri.toString() ;
//                prof_image_path = sourceFile.getAbsolutePath() ;
               mUserDetails.setProf_url(prof_image_path);
*/
                //  profileImage.setImageResource(uri);
                prof_image_path = imagePath();


            }

//            mUserDetails.setProf_image(BitmapFactory.decodeFile(prof_image_path));
            mUserDetails.setProf_url(prof_image_path);
            //   String image=   mUserDetails.getProf_url();
//            profileImage.setImageResource(uri);
            startActivity(new Intent(context, BodyConsumptionActivty.class));
        }
    }

 /* @Override
    public void onResponseReceived(String response) {
        Log.i("Server Response", response);

    }*/

    @Override
    public void onProcessFinish(String serverResp, int RespValue) {

        Log.i("Server Response .", serverResp);

    }


    private String imagePath() {
        String sdPath;

      /*  BitmapDrawable bd = (BitmapDrawable) getResources().getDrawable(R.drawable.skiptoset);
        Bitmap bitMap = bd.getBitmap();
*/
        Bitmap bitMap = BitmapFactory.decodeResource(getResources(), R.drawable.skiptoset);

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
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
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

}
