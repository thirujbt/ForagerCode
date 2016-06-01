package com.forager.meal.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.forager.meal.application.MyApplication;
import com.forager.meal.bean.UserDetails;
import com.forager.meal.constants.AppConstants;
import com.forager.meal.ui.R;
import com.forager.meal.utitlity.Calculations;
import com.forager.meal.utitlity.Utility;
import com.peaksware.wheel.OnWheelScrollListener;
import com.peaksware.wheel.WheelView;
import com.peaksware.wheel.adapter.ArrayWheelAdapter;
import com.peaksware.wheel.adapter.NumericWheelAdapter;

import java.util.ArrayList;
import java.util.Arrays;

public class BodyConsumptionActivty extends Activity implements View.OnClickListener, View.OnFocusChangeListener{

    private WheelView mUnitsWheel;
    private WheelView mAgeWheel;
    private WheelView mWeightWheel;
    private WheelView mWeightUnitWheel;
    private WheelView mHeightWheel;
    private WheelView mHeightUnitWheel;
    private WheelView mHeightWheel1;
    private WheelView mHeightUnitWheel1;
    private WheelView mGenderWheel;
    private WheelView mActivityWheel;

    private ArrayWheelAdapter mUnitsAdapter;
    private NumericWheelAdapter mAgeAdapter;
    private NumericWheelAdapter mWeightAdapter;
    private ArrayWheelAdapter mWeightUnitAdapter;
    private NumericWheelAdapter mHeightAdapter;
    private ArrayWheelAdapter mHeightUnitAdapter;
    private NumericWheelAdapter mHeightAdapter1;
    private ArrayWheelAdapter mHeightUnitAdapter1;
    private ArrayWheelAdapter mGenderAdapter;
    private ArrayWheelAdapter mActivityAdapter;

    private Context context;

    private EditText units_edit;
    private EditText age_edit;
    private EditText weight_edit;
    private EditText height_edit;
    private EditText gender_edit;
    private EditText activity_edit;

    private TextView edit_daily_cal;

    private enum Wheel_Enum {UNITS, AGE, WEIGHT, HEIGHT, GENDER, ACTIVITY_LEVEL};

    private LinearLayout mPickerLayout;
    private LinearLayout mWeightLayout;
    private LinearLayout mHeightLayout;

    private ImageButton mPicker_Close;
    private ImageButton mPicker_Select;
    private ImageButton mBack;
    private TextView mPicker_Title;
    private Button mNextBtn;

 private TextView titleBodyConsumption;

    private static final int AGE_MIN = 10;
    private static final int AGE_MAX = 99;
    private static final int WEIGHT_MIN_METRIC =  20;
    private static final int WEIGHT_MAX_METRIC = 230;
    private static final int WEIGHT_MIN_USSYSTEM = 40;
    private static final int WEIGHT_MAX_USSYSTEM = 500;
    private static final int HEIGHT_MIN_METRIC =  90;
    private static final int HEIGHT_MAX_METRIC = 240;
    private static final int HEIGHT_MIN_USSYSTEM = 3;
    private static final int HEIGHT_MAX_USSYSTEM = 8;
    private static final int HEIGHT1_MIN_USSYSTEM = 0;
    private static final int HEIGHT1_MAX_USSYSTEM = 11;
    private ArrayList<String> HEIGHT_UNIT_METRIC = new ArrayList<>();
    public ArrayList<String>  HEIGHT_UNIT_USSYSTEM = new ArrayList<>();
    public ArrayList<String>  HEIGHT1_UNIT_USSYSTEM = new ArrayList<>();
    public ArrayList<String>  WEIGHT_UNIT_METRIC = new ArrayList<>();
    public ArrayList<String>  WEIGHT_UNIT_USSYSTEM = new ArrayList<>();
    public ArrayList<String>  UNITS_ARRAY = new ArrayList<>();
    public ArrayList<String>  GENDER_ARRAY = new ArrayList<>();
    public ArrayList<String>  ACTIVITY_ARRAY = new ArrayList<>();
    private static final int METRIC_INDEX = 0;
    private static final int U_S_SYSTEM_INDEX = 1;

    private int units_index = 1;

    private int age_index = 0;

    private int weight_lbs_index = 0;

    private int weight_kg_index = 0;

    private int height_cm_index = 0;

    private int height_ft_index = 0;

    private int height_inch_index = 0;

    private int gender_index = 0;

    private int activity_index = 0;

    private int weight_metrix_index = -1;

    private int weight_ussystem_index = -1;

    private int height_metrix_cm_index = -1;

    private int height_ussystem_ft_index = -1;

    private int height_ussystem_inch_index = -1;

    private Wheel_Enum currentViewIndex;

    SharedPreferences preferences;

    private Activity mCurrentAcitivty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_body_consumption);
        context = BodyConsumptionActivty.this;
        mCurrentAcitivty = BodyConsumptionActivty.this;
        HEIGHT_UNIT_METRIC.add("cm");
        HEIGHT_UNIT_USSYSTEM.add("ft");
        HEIGHT1_UNIT_USSYSTEM.add("inch");
        WEIGHT_UNIT_METRIC.add("kg");
        WEIGHT_UNIT_USSYSTEM.add("lbs");
        init();
        findViews();
    }

    private void findViews() {

        MyApplication.setContext(BodyConsumptionActivty.this);
        titleBodyConsumption = (TextView)findViewById( R.id.title_body_consumption );
        titleBodyConsumption.setTypeface(MyApplication.getFont());

    }

    private void init() {
        units_edit = (EditText) findViewById(R.id.edit_units);
        units_edit.setKeyListener(null);
        units_edit.setOnFocusChangeListener(this);
        units_edit.setOnClickListener(this);

        UNITS_ARRAY = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.units_array)));
        mUnitsWheel = (WheelView) findViewById(R.id.units_wheel);
        mUnitsAdapter = new ArrayWheelAdapter(context, UNITS_ARRAY.toArray());
        mUnitsWheel.setViewAdapter(mUnitsAdapter);
        mUnitsWheel.setCyclic(false);
        mUnitsWheel.setCurrentItem(U_S_SYSTEM_INDEX, false);
        mUnitsAdapter.setIndex(mUnitsWheel.getCurrentItem());
        mUnitsWheel.addScrollingListener(mOnWheelScrollListener);
      //  units_edit.setText(mUnitsAdapter.getItemText(mUnitsWheel.getCurrentItem()));

        mBack = (ImageButton) findViewById(R.id.back);
        mBack.setOnClickListener(this);

        mNextBtn = (Button) findViewById(R.id.next_btn);
        mNextBtn.setOnClickListener(this);

        mPickerLayout = (LinearLayout) findViewById(R.id.picker_layout);
        mWeightLayout = (LinearLayout) findViewById(R.id.weight_layout);
        mHeightLayout = (LinearLayout) findViewById(R.id.height_layout);
        mPicker_Close = (ImageButton) findViewById(R.id.picker_close);
        mPicker_Select = (ImageButton) findViewById(R.id.picker_select);
        mPicker_Title = (TextView) findViewById(R.id.picker_title);

        mPicker_Close.setOnClickListener(this);
        mPicker_Select.setOnClickListener(this);

        age_edit = (EditText) findViewById(R.id.edit_age);
        age_edit.setKeyListener(null);
        age_edit.setOnFocusChangeListener(this);
        age_edit.setOnClickListener(this);

        mAgeWheel = (WheelView) findViewById(R.id.age_wheel);
        mAgeAdapter = new NumericWheelAdapter(context, AGE_MIN, AGE_MAX);
        mAgeWheel.setViewAdapter(mAgeAdapter);
        mAgeWheel.setCyclic(false);
        mAgeAdapter.setIndex(mAgeWheel.getCurrentItem());
        mAgeWheel.addScrollingListener(mOnWheelScrollListener);


        weight_edit = (EditText) findViewById(R.id.edit_weight);
        weight_edit.setKeyListener(null);
        weight_edit.setOnFocusChangeListener(this);
        weight_edit.setOnClickListener(this);

        mWeightWheel = (WheelView) findViewById(R.id.weight_wheel);
        mWeightAdapter = new NumericWheelAdapter(context, WEIGHT_MIN_METRIC, WEIGHT_MAX_METRIC);
        mWeightWheel.setViewAdapter(mWeightAdapter);
        mWeightWheel.setCyclic(false);
        mWeightAdapter.setIndex(mWeightWheel.getCurrentItem());

        mWeightWheel.addScrollingListener(mOnWheelScrollListener);

        mWeightUnitWheel = (WheelView) findViewById(R.id.weight_unit_wheel);
        mWeightUnitAdapter = new ArrayWheelAdapter(context, WEIGHT_UNIT_METRIC.toArray());
        mWeightUnitWheel.setViewAdapter(mWeightUnitAdapter);
        mWeightUnitWheel.setCyclic(false);
        mWeightUnitAdapter.setIndex(mWeightUnitWheel.getCurrentItem());
        mWeightUnitWheel.addScrollingListener(mOnWheelScrollListener);


        height_edit = (EditText) findViewById(R.id.edit_height);
        height_edit.setKeyListener(null);
        height_edit.setOnFocusChangeListener(this);
        height_edit.setOnClickListener(this);

        mHeightWheel = (WheelView) findViewById(R.id.height_wheel);
        mHeightAdapter = new NumericWheelAdapter(context, HEIGHT_MIN_METRIC, HEIGHT_MAX_METRIC);
        mHeightWheel.setViewAdapter(mHeightAdapter);
        mHeightWheel.setCyclic(false);
        mHeightAdapter.setIndex(mHeightWheel.getCurrentItem());
        mHeightWheel.addScrollingListener(mOnWheelScrollListener);

        mHeightUnitWheel = (WheelView) findViewById(R.id.height_unit_wheel);
        mHeightUnitAdapter = new ArrayWheelAdapter(context, HEIGHT_UNIT_METRIC.toArray());
        mHeightUnitWheel.setViewAdapter(mHeightUnitAdapter);
        mHeightUnitWheel.setCyclic(false);
        mHeightUnitAdapter.setIndex(mHeightUnitWheel.getCurrentItem());
        mHeightUnitWheel.addScrollingListener(mOnWheelScrollListener);

        mHeightWheel1 = (WheelView) findViewById(R.id.height_wheel1);
        mHeightAdapter1 = new NumericWheelAdapter(context, HEIGHT1_MIN_USSYSTEM, HEIGHT1_MAX_USSYSTEM);
        mHeightWheel1.setViewAdapter(mHeightAdapter1);
        mHeightWheel1.setCyclic(false);
        mHeightAdapter1.setIndex(mHeightWheel1.getCurrentItem());
        mHeightWheel1.addScrollingListener(mOnWheelScrollListener);

        mHeightUnitWheel1 = (WheelView) findViewById(R.id.height_unit_wheel1);
        mHeightUnitAdapter1 = new ArrayWheelAdapter(context, HEIGHT1_UNIT_USSYSTEM.toArray());
        mHeightUnitWheel1.setViewAdapter(mHeightUnitAdapter1);
        mHeightUnitWheel1.setCyclic(false);
        mHeightUnitAdapter1.setIndex(mHeightUnitWheel1.getCurrentItem());
        mHeightUnitWheel1.addScrollingListener(mOnWheelScrollListener);

        gender_edit = (EditText) findViewById(R.id.edit_gender);
        gender_edit.setKeyListener(null);
        gender_edit.setOnFocusChangeListener(this);
        gender_edit.setOnClickListener(this);
        GENDER_ARRAY = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.gender_array)));
        mGenderWheel = (WheelView) findViewById(R.id.gender_wheel);
        mGenderAdapter = new ArrayWheelAdapter(context, GENDER_ARRAY.toArray());
        mGenderWheel.setViewAdapter(mGenderAdapter);
        mGenderWheel.setCyclic(false);
        mGenderAdapter.setIndex(mGenderWheel.getCurrentItem());
        mGenderWheel.addScrollingListener(mOnWheelScrollListener);

        activity_edit = (EditText) findViewById(R.id.edit_activity_level);
        activity_edit.setKeyListener(null);
        activity_edit.setOnFocusChangeListener(this);
        activity_edit.setOnClickListener(this);

        ACTIVITY_ARRAY = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.activity_level_array)));
        mActivityWheel = (WheelView) findViewById(R.id.activity_wheel);
        mActivityAdapter = new ArrayWheelAdapter(context, ACTIVITY_ARRAY.toArray());
        mActivityWheel.setViewAdapter(mActivityAdapter);
        mActivityWheel.setCyclic(false);
        mActivityAdapter.setIndex(mActivityWheel.getCurrentItem());
        mActivityWheel.addScrollingListener(mOnWheelScrollListener);

        edit_daily_cal = (TextView) findViewById(R.id.edit_daily_cal);


        mPickerLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {

                    if (mPickerLayout != null && mPickerLayout.getVisibility() == View.VISIBLE) {
                        // touch coordinates
                        int touchX = (int) event.getX();
                        int touchY = (int) event.getY();
                        // get your view coordinates
                        final int[] viewLocation = new int[2];
                        mPickerLayout.getLocationOnScreen(viewLocation);

                        // The left coordinate of the view
                        int viewX1 = viewLocation[0];
                        // The right coordinate of the view
                        int viewX2 = viewLocation[0] + mPickerLayout.getWidth();
                        // The top coordinate of the view
                        int viewY1 = viewLocation[1];
                        // The bottom coordinate of the view
                        int viewY2 = viewLocation[1] + mPickerLayout.getHeight();

                        Log.e("Check", "on Touch outside");

                        if (!((touchX >= viewX1 || touchX <= viewX2) && (touchY >= viewY1 || touchY <= viewY2))) {

                            // Do what you want...

                            // If you don't want allow touch outside (for example, only hide keyboard or dismiss popup)
                            Log.e("Check", "on Touch inside");
                            mPickerLayout.setVisibility(View.GONE);
                            return false;
                        }
                    }
                }
                return false;
            }
        });

        setHint(mUnitsWheel.getCurrentItem());

        fillDatasFromPrefes();
    }

    private void fillDatasFromPrefes() {
        preferences = Utility.getSharedPreferences(BodyConsumptionActivty.this, AppConstants.SHARED_PREFES_USER_DETAILS);

        if(!Utility.isBlank(preferences.getString(AppConstants.BODY_UNITS,""))) {
            String units_str = preferences.getString(AppConstants.BODY_UNITS, "");
            units_edit.setText(units_str);
            changeViewVisibility(Wheel_Enum.UNITS);
            units_index = UNITS_ARRAY.indexOf(units_str);
            mUnitsWheel.setCurrentItem(units_index, false);
            mUnitsAdapter.setIndex(mUnitsWheel.getCurrentItem());
            setHint(mUnitsWheel.getCurrentItem());
        }

        if(!Utility.isBlank(preferences.getString(AppConstants.BODY_AGE, ""))) {
            String age_str = preferences.getString(AppConstants.BODY_AGE, "");
            age_edit.setText(age_str);
            String[] splitstr = age_str.split(" ");
            if(splitstr.length == 2) {
                changeViewVisibility(Wheel_Enum.AGE);
                mAgeWheel.setCurrentItem(getAgeIndex(splitstr[0]), false);
            }
            mAgeAdapter.setIndex(mAgeWheel.getCurrentItem());
        }

        String weight_str = null;
        if(units_index == METRIC_INDEX &&
                !Utility.isBlank(preferences.getString(AppConstants.BODY_WEIGHT, ""))) {
            weight_str = preferences.getString(AppConstants.BODY_WEIGHT, "");

        } else if(units_index == U_S_SYSTEM_INDEX &&
                !Utility.isBlank(preferences.getString(AppConstants.BODY_WEIGHT, ""))) {
            weight_str = preferences.getString(AppConstants.BODY_WEIGHT, "");
        }

        if(weight_str != null) {
            changeViewVisibility(Wheel_Enum.WEIGHT);
            weight_edit.setText(weight_str);
            String[] splitStr = weight_str.split(" ");
            if (splitStr.length == 2) {
                mWeightWheel.setCurrentItem(getWeightIndex(splitStr[0]), false);
                mWeightAdapter.setIndex(mWeightWheel.getCurrentItem());
                mWeightUnitAdapter.setIndex(0);
            }
        }

        if(!Utility.isBlank(preferences.getString(AppConstants.BODY_HEIGHT, ""))) {

                String height_str = null;

                if (units_index == METRIC_INDEX &&
                        !Utility.isBlank(preferences.getString(AppConstants.BODY_HEIGHT, ""))) {

                    height_str = preferences.getString(AppConstants.BODY_HEIGHT, "");

            } else if (units_index == U_S_SYSTEM_INDEX &&
                    !Utility.isBlank(preferences.getString(AppConstants.BODY_HEIGHT, ""))) {

                height_str = preferences.getString(AppConstants.BODY_HEIGHT, "");

            }
            changeViewVisibility(Wheel_Enum.HEIGHT);
            height_edit.setText(height_str);
            String[] splitStr = height_str.split(" ");
            if (units_index == METRIC_INDEX && splitStr.length == 2) {
                mHeightWheel.setCurrentItem(getHeightCMIndex(splitStr[0]), false);
                mHeightAdapter.setIndex(mHeightWheel.getCurrentItem());
                mHeightUnitAdapter.setIndex(0);
            } else if (units_index == U_S_SYSTEM_INDEX && splitStr.length == 4) {
                mHeightWheel.setCurrentItem(getHeightFTIndex(splitStr[0]), false);
                mHeightAdapter.setIndex(mHeightWheel.getCurrentItem());
                mHeightUnitAdapter.setIndex(0);
                mHeightWheel1.setCurrentItem(getHeightInchIndex(splitStr[3]), false);
                mHeightAdapter1.setIndex(mHeightWheel1.getCurrentItem());
                mHeightUnitAdapter1.setIndex(0);
            }
        }

        if(!Utility.isBlank(preferences.getString(AppConstants.BODY_GENDER, ""))) {
            String gender_str = preferences.getString(AppConstants.BODY_GENDER, "");
            int gender_inx = GENDER_ARRAY.indexOf(gender_str);
            gender_edit.setText(gender_str);
            changeViewVisibility(Wheel_Enum.GENDER);
            mGenderWheel.setCurrentItem(gender_inx, false);
            mGenderAdapter.setIndex(mGenderWheel.getCurrentItem());
        }

        if(!Utility.isBlank(preferences.getString(AppConstants.BODY_ACTIVITY_LEVEL, ""))) {
            String activity_level_str = preferences.getString(AppConstants.BODY_ACTIVITY_LEVEL, "");
            int activity_inx = ACTIVITY_ARRAY.indexOf(activity_level_str);
            changeViewVisibility(Wheel_Enum.ACTIVITY_LEVEL);
            activity_edit.setText(activity_level_str);
            mActivityWheel.setCurrentItem(activity_inx, false);
            mGenderAdapter.setIndex(mAgeWheel.getCurrentItem());
        }

        if(!Utility.isBlank(preferences.getString(AppConstants.BODY_CALORIES,""))) {
            edit_daily_cal.setText(preferences.getString(AppConstants.BODY_CALORIES,""));
        }
        calcBMI();
    }

    private void doUnitWork() {
        if(units_index == METRIC_INDEX) {
            mWeightAdapter.setMinValue(WEIGHT_MIN_METRIC);
            mWeightAdapter.setMaxValue(WEIGHT_MAX_METRIC);
            mWeightUnitAdapter.setItems(WEIGHT_UNIT_METRIC.toArray());

            if(weight_metrix_index != -1) {
                mWeightWheel.setCurrentItem(weight_metrix_index, false);
                mWeightAdapter.setIndex(mWeightWheel.getCurrentItem());
                mWeightUnitAdapter.setIndex(0);
                setWeightToEdit(mWeightAdapter.getItemText(weight_metrix_index).toString());
            } else {
                weight_edit.setText("");
            }

            mHeightAdapter.setMinValue(HEIGHT_MIN_METRIC);
            mHeightAdapter.setMaxValue(HEIGHT_MAX_METRIC);
            mHeightUnitAdapter.setItems(HEIGHT_UNIT_METRIC.toArray());

            if(height_metrix_cm_index != -1) {
                mHeightWheel.setCurrentItem(height_metrix_cm_index, false);
                mHeightAdapter.setIndex(mHeightWheel.getCurrentItem());
                mHeightUnitAdapter.setIndex(0);
                setHeightToEdit(mHeightAdapter.getItemText(height_metrix_cm_index).toString());
            } else {
                height_edit.setText("");
            }

        } else if(units_index == U_S_SYSTEM_INDEX) {

            mWeightAdapter.setMinValue(WEIGHT_MIN_USSYSTEM);
            mWeightAdapter.setMaxValue(WEIGHT_MAX_USSYSTEM);
            mWeightUnitAdapter.setItems(WEIGHT_UNIT_USSYSTEM.toArray());
            if(weight_ussystem_index != -1) {
                mWeightWheel.setCurrentItem(weight_ussystem_index, false);
                mWeightAdapter.setIndex(mWeightWheel.getCurrentItem());
                mWeightUnitAdapter.setIndex(0);
                setWeightToEdit(mWeightAdapter.getItemText(weight_ussystem_index).toString());
            } else {
                weight_edit.setText("");
            }

            mHeightAdapter.setMinValue(HEIGHT_MIN_USSYSTEM);
            mHeightAdapter.setMaxValue(HEIGHT_MAX_USSYSTEM);
            mHeightUnitAdapter.setItems(HEIGHT_UNIT_USSYSTEM.toArray());

            mHeightAdapter1.setMinValue(HEIGHT1_MIN_USSYSTEM);
            mHeightAdapter1.setMaxValue(HEIGHT1_MAX_USSYSTEM);
            mHeightUnitAdapter1.setItems(HEIGHT1_UNIT_USSYSTEM.toArray());

            if(height_ussystem_ft_index != -1 && height_ussystem_inch_index != -1) {
                mHeightWheel.setCurrentItem(height_ussystem_ft_index, false);
                mHeightAdapter.setIndex(mHeightWheel.getCurrentItem());
                mHeightUnitAdapter.setIndex(0);
                mHeightWheel1.setCurrentItem(height_ussystem_inch_index, false);
                mHeightAdapter1.setIndex(mHeightWheel1.getCurrentItem());
                mHeightUnitAdapter1.setIndex(0);
                setHeightToEdit(mHeightAdapter.getItemText(height_ussystem_ft_index).toString(),
                        mHeightAdapter1.getItemText(height_ussystem_inch_index).toString());
            } else {
                height_edit.setText("");
            }
        }
    }

    private void setWeightToEdit(String weight) {
        if(units_index == METRIC_INDEX) {
            weight_edit.setText(weight+" "+WEIGHT_UNIT_METRIC.get(0));
        } else if(units_index == U_S_SYSTEM_INDEX) {
            weight_edit.setText(weight+" "+WEIGHT_UNIT_USSYSTEM.get(0));
        }
    }

    private void setHeightToEdit(String height_cm) {
        height_edit.setText(height_cm+ " "+ HEIGHT_UNIT_METRIC.get(0));
    }

    private void setHeightToEdit(String height_ft, String height_inch) {
        height_edit.setText(height_ft+" "+HEIGHT_UNIT_USSYSTEM.get(0)+ " - "+height_inch+" "+HEIGHT1_UNIT_USSYSTEM.get(0));
    }

    private int getHeightCMIndex(String cm) {
        int index = Integer.parseInt(cm) - HEIGHT_MIN_METRIC;
        height_metrix_cm_index = index;
        return index;
    }

    private int getHeightFTIndex(String ft) {
        int index = Integer.parseInt(ft) - HEIGHT_MIN_USSYSTEM;
        height_ussystem_ft_index = index;
        return index;
    }

    private int getHeightInchIndex(String inch) {
        int index = Integer.parseInt(inch) - HEIGHT1_MIN_USSYSTEM;
        height_ussystem_inch_index = index;
        return index;
    }

    private int getWeightIndex(String weight) {
        int index = -1;
        if(units_index == METRIC_INDEX) {
            index = Integer.parseInt(weight)-WEIGHT_MIN_METRIC;
            weight_metrix_index = index;
        } else if(units_index == U_S_SYSTEM_INDEX) {
            index = Integer.parseInt(weight)-WEIGHT_MIN_USSYSTEM;
            weight_ussystem_index = index;
        }
        return index;
    }

    private int getAgeIndex(String age) {
        return Integer.parseInt(age)-AGE_MIN;
    }

    OnWheelScrollListener mOnWheelScrollListener = new OnWheelScrollListener() {
        @Override
        public void onScrollingStarted(WheelView wheel) {

        }

        @Override
        public void onScrollingFinished(WheelView wheel) {

            changeWheelValues(wheel);

        }
    };

    public void changeWheelValues(WheelView wheel) {
        if(wheel == mUnitsWheel) {
            mUnitsAdapter.setIndex(wheel.getCurrentItem());
        } else if(wheel == mAgeWheel) {
            mAgeAdapter.setIndex(wheel.getCurrentItem());
        } else if(wheel == mWeightWheel) {
            mWeightAdapter.setIndex(wheel.getCurrentItem());
        } else if(wheel == mWeightUnitWheel) {
            mWeightUnitAdapter.setIndex(wheel.getCurrentItem());
        } else if(wheel == mHeightWheel) {
            mHeightAdapter.setIndex(wheel.getCurrentItem());
        } else if(wheel == mHeightUnitWheel) {
            mHeightUnitAdapter.setIndex(wheel.getCurrentItem());
        } else if(wheel == mHeightWheel1) {
            mHeightAdapter1.setIndex(wheel.getCurrentItem());
        } else if(wheel == mHeightUnitWheel1) {
            mHeightUnitAdapter1.setIndex(wheel.getCurrentItem());
        } else if(wheel == mGenderWheel) {
            mGenderAdapter.setIndex(wheel.getCurrentItem());
        } else if(wheel == mActivityWheel) {
            mActivityAdapter.setIndex(wheel.getCurrentItem());
        }
    }

    private void storeViewValue() {
        switch (currentViewIndex) {
            case HEIGHT:

                if(units_index == METRIC_INDEX) {
                    height_metrix_cm_index = mHeightWheel.getCurrentItem();
                    setHeightToEdit(mHeightAdapter.getItemText(
                            mHeightWheel.getCurrentItem()).toString());
                    height_cm_index = mHeightWheel.getCurrentItem();
                } else if(units_index == U_S_SYSTEM_INDEX) {
                    height_ussystem_ft_index= mHeightWheel.getCurrentItem();
                    height_ussystem_inch_index = mHeightWheel1.getCurrentItem();
                    setHeightToEdit(mHeightAdapter.getItemText(mHeightWheel.getCurrentItem()).toString(),
                            mHeightAdapter1.getItemText(mHeightWheel1.getCurrentItem()).toString());

                    height_ft_index = mHeightWheel.getCurrentItem();;
                    height_inch_index = mHeightWheel1.getCurrentItem();
                }

                break;
            case WEIGHT:
                if(units_index == METRIC_INDEX) {
                    weight_metrix_index = mWeightWheel.getCurrentItem();

                    weight_kg_index = mWeightWheel.getCurrentItem();

                } else if(units_index == U_S_SYSTEM_INDEX) {
                    weight_ussystem_index = mWeightWheel.getCurrentItem();
                    weight_lbs_index = mWeightWheel.getCurrentItem();
                }
                setWeightToEdit(mWeightAdapter.getItemText(mWeightWheel.getCurrentItem()).toString());
                break;
            case UNITS:
                units_edit.setText(mUnitsAdapter.getItemText(mUnitsWheel.getCurrentItem()));
                units_index = mUnitsWheel.getCurrentItem();
                setHint(mUnitsWheel.getCurrentItem());
                doUnitWork();
                break;
            case AGE:
                age_edit.setText(mAgeAdapter.getItemText(mAgeWheel.getCurrentItem()) + " years");
                age_index = mAgeWheel.getCurrentItem();
                break;
            case GENDER:
                gender_edit.setText(mGenderAdapter.getItemText(mGenderWheel.getCurrentItem()));
                gender_index = mGenderWheel.getCurrentItem();
                break;
            case ACTIVITY_LEVEL:
                activity_edit.setText(mActivityAdapter.getItemText(mActivityWheel.getCurrentItem()));
                activity_index = mActivityWheel.getCurrentItem();
                break;
            default:
                break;
        }

       calcBMI();
    }


    private void calcBMI() {
        if(!Utility.isBlank(units_edit.getText().toString()) &&
                !Utility.isBlank(age_edit.getText().toString()) &&
                !Utility.isBlank(weight_edit.getText().toString()) &&
                !Utility.isBlank(height_edit.getText().toString()) &&
                !Utility.isBlank(gender_edit.getText().toString()) &&
                !activity_edit.getText().toString().equals(getResources().getString(R.string.activity_level))) {

            float calc_height =0.0f;

            Calculations.ACTIVITY_LEVEL_ENUM calc_activity_level = null;

            boolean calc_isMen = false;

            boolean calc_isMetric = false;

            int calc_age = 0;

            float calc_weight = 0.0f;

            if(units_index == U_S_SYSTEM_INDEX) {

                int feet = Integer.parseInt(mHeightAdapter.getItemText(mHeightWheel.getCurrentItem()).toString());
                int inch = Integer.parseInt(mHeightAdapter1.getItemText(mHeightWheel1.getCurrentItem()).toString());
                calc_height = Calculations.calcFeetToInch(feet, inch);
                calc_isMetric = false;
            } else {
                calc_height = Integer.parseInt(mHeightAdapter.getItemText(mHeightWheel.getCurrentItem()).toString());
                calc_isMetric = true;
            }

            if(mGenderWheel.getCurrentItem() == 0) {
                calc_isMen = true;
            } else if(mGenderWheel.getCurrentItem() == 1) {
                calc_isMen = false;
            }

            calc_age = Integer.parseInt(mAgeAdapter.getItemText(mAgeWheel.getCurrentItem()).toString());

            calc_weight = Integer.parseInt(mWeightAdapter.getItemText(mWeightWheel.getCurrentItem()).toString());



            switch (mActivityWheel.getCurrentItem()) {
                case 0:
                    calc_activity_level = Calculations.ACTIVITY_LEVEL_ENUM.BMR_LITTLE_TO_NO_EXERCISE;
                    break;
                case 1:
                    calc_activity_level = Calculations.ACTIVITY_LEVEL_ENUM.BMR_1_3_WORKOUTS_PER_WEEK;
                    break;
                case 2:
                    calc_activity_level = Calculations.ACTIVITY_LEVEL_ENUM.BMR_3_5_WORKOUTS_PER_WEEK;
                    break;
                case 3:
                    calc_activity_level = Calculations.ACTIVITY_LEVEL_ENUM.BMR_6_7_WORKOUTS_PER_WEEK;
                    break;
                case 4:
                    calc_activity_level = Calculations.ACTIVITY_LEVEL_ENUM.BMR_WORKOUT_TWICE_A_DAY;
                    break;
                default:
                    break;
            }

            float bmi_value = Calculations.getBMR(calc_isMen, calc_isMetric, calc_weight, calc_height, calc_age, calc_activity_level);

            int result = ((int)(Math.round(2.0f * bmi_value) / 2.0f)) ;
            edit_daily_cal.setText(String.valueOf(result));


        } else {
            edit_daily_cal.setText("0");
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
                storeViewValue();
                mPickerLayout.setVisibility(View.GONE);
                break;
            case R.id.edit_units:
                changeViewVisibility(Wheel_Enum.UNITS);
                mPickerLayout.setVisibility(View.VISIBLE);
                break;
            case R.id.edit_age:
                changeViewVisibility(Wheel_Enum.AGE);
                mPickerLayout.setVisibility(View.VISIBLE);
                break;
            case R.id.edit_weight:
                changeViewVisibility(Wheel_Enum.WEIGHT);
                mPickerLayout.setVisibility(View.VISIBLE);
                break;
            case R.id.edit_height:
                changeViewVisibility(Wheel_Enum.HEIGHT);
                mPickerLayout.setVisibility(View.VISIBLE);
                break;
            case R.id.edit_gender:
                changeViewVisibility(Wheel_Enum.GENDER);
                mPickerLayout.setVisibility(View.VISIBLE);
                break;
            case R.id.edit_activity_level:
                changeViewVisibility(Wheel_Enum.ACTIVITY_LEVEL);
                mPickerLayout.setVisibility(View.VISIBLE);
                break;
            case R.id.next_btn:
                validate_Fields();
                break;
            case R.id.back:
                finish();
                break;

        }

    }

    private void setHint(int hintValue) {
        if(hintValue == METRIC_INDEX) {
            weight_edit.setHint(getResources().getString(R.string.weight_hint_metric));
            height_edit.setHint(getResources().getString(R.string.height_hint_metric));
        } else if(hintValue == U_S_SYSTEM_INDEX){
            weight_edit.setHint(getResources().getString(R.string.weight_hint_ussystem));
            height_edit.setHint(getResources().getString(R.string.height_hint_ussystem));
        }
    }

    private void changeViewVisibility(Wheel_Enum wheel_enum) {
        currentViewIndex = wheel_enum;
        switch (wheel_enum) {
            case UNITS:
                mAgeWheel.setVisibility(View.GONE);
                mWeightLayout.setVisibility(View.GONE);
                mHeightLayout.setVisibility(View.GONE);
                mGenderWheel.setVisibility(View.GONE);
                mActivityWheel.setVisibility(View.GONE);
                mUnitsWheel.setVisibility(View.VISIBLE);
                mUnitsWheel.setCurrentItem(units_index, false);
                mUnitsAdapter.setIndex(mUnitsWheel.getCurrentItem());
                mPicker_Title.setText(getResources().getString(R.string.units));
                break;
            case AGE:
                mUnitsWheel.setVisibility(View.GONE);
                mWeightLayout.setVisibility(View.GONE);
                mHeightLayout.setVisibility(View.GONE);
                mGenderWheel.setVisibility(View.GONE);
                mActivityWheel.setVisibility(View.GONE);
                mAgeWheel.setVisibility(View.VISIBLE);
                mAgeWheel.setCurrentItem(age_index, false);
                mAgeAdapter.setIndex(mAgeWheel.getCurrentItem());
                mPicker_Title.setText(getResources().getString(R.string.age));
                break;
            case WEIGHT:
                mUnitsWheel.setVisibility(View.GONE);
                mAgeWheel.setVisibility(View.GONE);
                mHeightLayout.setVisibility(View.GONE);
                mGenderWheel.setVisibility(View.GONE);
                mActivityWheel.setVisibility(View.GONE);

                if(units_index == METRIC_INDEX) {

                    mWeightAdapter.setMinValue(WEIGHT_MIN_METRIC);
                    mWeightAdapter.setMaxValue(WEIGHT_MAX_METRIC);
                    mWeightUnitAdapter.setItems(WEIGHT_UNIT_METRIC.toArray());
                    mWeightWheel.setCurrentItem(weight_kg_index, false);
                    mWeightAdapter.setIndex(weight_kg_index);

                } else if(units_index == U_S_SYSTEM_INDEX) {

                    mWeightAdapter.setMinValue(WEIGHT_MIN_USSYSTEM);
                    mWeightAdapter.setMaxValue(WEIGHT_MAX_USSYSTEM);
                    mWeightUnitAdapter.setItems(WEIGHT_UNIT_USSYSTEM.toArray());
                    mWeightWheel.setCurrentItem(weight_lbs_index, false);
                    mWeightAdapter.setIndex(weight_lbs_index);
                }

                mWeightLayout.setVisibility(View.VISIBLE);
                mPicker_Title.setText(getResources().getString(R.string.weight));
                break;
            case HEIGHT:
                mUnitsWheel.setVisibility(View.GONE);
                mAgeWheel.setVisibility(View.GONE);
                mWeightLayout.setVisibility(View.GONE);
                mGenderWheel.setVisibility(View.GONE);
                mActivityWheel.setVisibility(View.GONE);
                if(units_index == METRIC_INDEX) {
                    mHeightAdapter.setMinValue(HEIGHT_MIN_METRIC);
                    mHeightAdapter.setMaxValue(HEIGHT_MAX_METRIC);
                    mHeightUnitAdapter.setItems(HEIGHT_UNIT_METRIC.toArray());
                    mHeightUnitWheel1.setVisibility(View.GONE);
                    mHeightWheel1.setVisibility(View.GONE);
                    mHeightWheel.setCurrentItem(height_cm_index, false);
                    mHeightAdapter.setIndex(height_cm_index);
                } else if(units_index == U_S_SYSTEM_INDEX) {
                    mHeightAdapter.setMinValue(HEIGHT_MIN_USSYSTEM);
                    mHeightAdapter.setMaxValue(HEIGHT_MAX_USSYSTEM);
                    mHeightUnitAdapter.setItems(HEIGHT_UNIT_USSYSTEM.toArray());
                    mHeightAdapter1.setMinValue(HEIGHT1_MIN_USSYSTEM);
                    mHeightAdapter1.setMaxValue(HEIGHT1_MAX_USSYSTEM);
                    mHeightUnitAdapter1.setItems(HEIGHT1_UNIT_USSYSTEM.toArray());
                    mHeightUnitWheel1.setVisibility(View.VISIBLE);
                    mHeightWheel1.setVisibility(View.VISIBLE);
                    mHeightWheel.setCurrentItem(height_ft_index, false);
                    mHeightWheel1.setCurrentItem(height_inch_index, false);
                    mHeightAdapter.setIndex(height_ft_index);
                    mHeightAdapter1.setIndex(height_inch_index);
                }

                mHeightLayout.setVisibility(View.VISIBLE);
                mPicker_Title.setText(getResources().getString(R.string.height));
                break;
            case GENDER:
                mAgeWheel.setVisibility(View.GONE);
                mWeightLayout.setVisibility(View.GONE);
                mHeightLayout.setVisibility(View.GONE);
                mUnitsWheel.setVisibility(View.GONE);
                mActivityWheel.setVisibility(View.GONE);
                mGenderWheel.setVisibility(View.VISIBLE);
                mGenderWheel.setCurrentItem(gender_index, false);
                mGenderAdapter.setIndex(gender_index);
                mPicker_Title.setText(getResources().getString(R.string.gender));
                break;
            case ACTIVITY_LEVEL:
                mUnitsWheel.setVisibility(View.GONE);
                mWeightLayout.setVisibility(View.GONE);
                mHeightLayout.setVisibility(View.GONE);
                mAgeWheel.setVisibility(View.GONE);
                mGenderWheel.setVisibility(View.GONE);
                mActivityWheel.setVisibility(View.VISIBLE);
                mActivityWheel.setCurrentItem(activity_index, false);
                mActivityAdapter.setIndex(activity_index);
                mPicker_Title.setText(getResources().getString(R.string.activity_level));
                break;
        }

    }



    private void validate_Fields() {
        if(Utility.isBlank(units_edit.getText().toString())) {
            Utility.showAlert(mCurrentAcitivty, AppConstants.ENTER_UNITS, false);
        } else if(Utility.isBlank(age_edit.getText().toString())) {
            Utility.showAlert(mCurrentAcitivty, AppConstants.ENTER_AGE, false);
        } else if(Utility.isBlank(weight_edit.getText().toString())) {
            Utility.showAlert(mCurrentAcitivty, AppConstants.ENTER_WEIGHT, false);
        } else if(Utility.isBlank(height_edit.getText().toString())) {
            Utility.showAlert(mCurrentAcitivty, AppConstants.ENTER_HEIGHT, false);
        } else if(Utility.isBlank(gender_edit.getText().toString())) {
            Utility.showAlert(mCurrentAcitivty, AppConstants.ENTER_GENDER, false);
        } else if(activity_edit.getText().toString().equals(getResources().getString(R.string.activity_level))) {
            Utility.showAlert(mCurrentAcitivty, AppConstants.ENTER_ACTIVITY_LEVEL, false);
        } else {
            /*try {
                JSONObject jsonObj = new JSONObject();
                jsonObj.put(JsonConstants.USER_ID, AppConstants.APP_USER_ID);
                jsonObj.put(JsonConstants.USER_NAME, mName_Edit.getText().toString());
                jsonObj.put(JsonConstants.USER_DOB, mBirthdate_Edit.getText().toString());
                jsonObj.put(JsonConstants.USER_COUNTRY, mCountry_Edit.getText().toString());

                new JsonAsync(ProfileActivity.this, jsonObj.toString()).execute(AppUrls.PROFILE_URL);

            } catch (JSONException e) {
                e.printStackTrace();
            }*/

            UserDetails mUserDetails = UserDetails.getInstance();
            mUserDetails.setUnits(units_edit.getText().toString());
            mUserDetails.setAge(age_edit.getText().toString());
            mUserDetails.setWeight(weight_edit.getText().toString());
            mUserDetails.setHeight(height_edit.getText().toString());
            mUserDetails.setGender(gender_edit.getText().toString());
            mUserDetails.setActivity_level(activity_edit.getText().toString());
            mUserDetails.setBody_calories((int)Math.round(Float.parseFloat(edit_daily_cal.getText().toString())));
            startActivity(new Intent(context, GoalActivity.class));
        }
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if(hasFocus) {
            mPickerLayout.setVisibility(View.VISIBLE);
            if(v == units_edit) {
                changeViewVisibility(Wheel_Enum.UNITS);
            } else if(v == age_edit) {
                changeViewVisibility(Wheel_Enum.AGE);
            } else if(v == weight_edit) {
                changeViewVisibility(Wheel_Enum.WEIGHT);
            } else if(v == height_edit) {
                changeViewVisibility(Wheel_Enum.HEIGHT);
            } else if(v == gender_edit) {
                changeViewVisibility(Wheel_Enum.GENDER);
            } else if(v == activity_edit) {
                changeViewVisibility(Wheel_Enum.ACTIVITY_LEVEL);
            }
        } else {
            mPickerLayout.setVisibility(View.GONE);
        }
    }
}
