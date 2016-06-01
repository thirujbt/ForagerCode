package com.forager.meal.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.forager.meal.ui.R;
import com.forager.meal.ui.fragment.IntroFragment;
import com.viewpagerindicator.CirclePageIndicator;

/**
 * Created by Akbar on eight/25/2015.
 */
public class IntroActivity extends FragmentActivity implements View.OnClickListener {
    ViewPager viewPager;
    MyPagerAdapter myPagerAdapter;
    CirclePageIndicator mIndicator;
    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;
    private int totalCount;
    Button mSignup, mSignin;
    int NumberOfPages = 7;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        prefs = getPreferences(Context.MODE_PRIVATE);
        editor = prefs.edit();
        setContentView(R.layout.activity_intro);
        mSignup = (Button) findViewById(R.id.sign_up_btn);
        mSignin = (Button) findViewById(R.id.sign_btn);
        mSignup.setOnClickListener(this);
        mSignin.setOnClickListener(this);


        viewPager = (ViewPager) findViewById(R.id.myviewpager);
        mIndicator = (CirclePageIndicator) findViewById(R.id.indicator);
        myPagerAdapter = new MyPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(myPagerAdapter);
        mIndicator.setViewPager(viewPager);

        mIndicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        if (getIntent().getBooleanExtra("checkShownCount", false)) {
            count();
        }
    }

    public void count() {
        totalCount = prefs.getInt("counter", 0);
        totalCount++;
        editor.putInt("counter", totalCount);
        editor.commit();
        if (totalCount > 1) {
           /* finish();
            Intent intent = new Intent(IntroActivity.this, HomeScreen.class);
            startActivity(intent);*/
        }
    }

    @Override
    public void onClick(View v) {
        if (v == mSignup) {
            Intent intent = new Intent(IntroActivity.this, RegisterActivity.class);
            startActivity(intent);
        }
        if (v == mSignin) {
            Intent intent = new Intent(IntroActivity.this, SignInActivity.class);
            startActivity(intent);
        }


    }

    private class MyPagerAdapter extends FragmentPagerAdapter {

        LayoutInflater mLayoutInflater;

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
            mLayoutInflater = (LayoutInflater) IntroActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }


        @Override
        public int getCount() {
            return NumberOfPages;
        }
        /*@Override
        public boolean isViewFromObject(View view, Object object) {
            return view == ((LinearLayout) object);
        }*/

        @Override
        public Fragment getItem(int position) {
            Fragment f = new IntroFragment();

            System.out.println("Entered intro activity");
            Bundle args = new Bundle();
            args.putInt("Pos", position);
            f.setArguments(args);

            return f;
        }


    }

}
