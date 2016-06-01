package com.forager.meal.ui.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.forager.meal.application.MyApplication;
import com.forager.meal.constants.AppConstants;
import com.forager.meal.database.Database;
import com.forager.meal.ui.R;
import com.forager.meal.ui.activity.DashBoardActivity;
import com.forager.meal.utitlity.Utility;

import java.io.File;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Chithra on 9/23/2015.
 */
/*
public class AboutFragment extends Fragment  {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView=inflater.inflate(R.layout.fragment_about,container,false);
        return rootView;
    }
}
*/

public class AboutFragment extends Fragment implements View.OnClickListener {

/*
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_about, container, false);
        return rootView;
    }*/

    private TextView title_about;

    private ImageView image_about,img_back;
    private WebView aboutContent;
    String text;
    public static int imageCount = 0;
    int fileCount;
    static int count = 1;
    Database db;
    HashMap<String, String> mHashMap = new HashMap<>();
    android.support.v4.app.FragmentTransaction transaction;

    public AboutFragment()
    {

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_about, container, false);

       PlanControllerFragment.flag=false;
        db = Database.getInstance(getActivity());
        db.getMyWritableDatabase();
        findViews(rootView);

        String aboutText = Utility.getContentFromFile(getActivity(), AppConstants.ABOUT_FILENAME);
        Spanned test = Html.fromHtml(aboutText);

        aboutContent.loadData(Html.toHtml(test), "text/html; charset=UTF-8", null);

        fileCount = new File(AppConstants.baseDir + "/" + AppConstants.FORAGER_DIR + "/" + AppConstants.ABOUT_DIR).list().length;
        t.schedule(timerTask, 0, 20000);
        return rootView;

       /* try {
            JSONObject jsonObj = new JSONObject();
            jsonObj.put(JsonConstants.USER_ID, AppConstants.APP_USER_ID);
            jsonObj.put(JsonConstants.PLAN_ID, db.getActivePlanId());
            jsonObj.put(JsonConstants.CALORIE_TYPE, UserDetails.getInstance().getPlan_calorie());


            if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.HONEYCOMB)
                new JsonAsync(AboutActivity.this, jsonObj.toString()).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, AppUrls.ACTIVE_PLAN_URL);
            else
                new JsonAsync(AboutActivity.this, jsonObj.toString()).execute(AppUrls.ACTIVE_PLAN_URL);


        } catch (Exception e) {
            e.printStackTrace();
        }*/

    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        db.closeDatabase();
    }


    private void findViews(View rootView) {

        MyApplication.setContext(getActivity());
        title_about=(TextView)rootView.findViewById(R.id.title_about);
        title_about.setTypeface(MyApplication.getFont());

        img_back=(ImageView)rootView.findViewById(R.id.about_back);
        image_about = (ImageView)rootView.findViewById(R.id.image_about);
        aboutContent = (WebView)rootView.findViewById(R.id.about_content);
        aboutContent.setBackgroundColor(Color.TRANSPARENT);

        img_back.setOnClickListener(this);
    }


    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getActivity().getMenuInflater().inflate(R.menu.menu_about, menu);
        return true;
    }
*/
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


    Timer t = new Timer();
    Handler handler = new Handler();


    TimerTask timerTask = new TimerTask() {
        @Override
        public void run() {
            handler.post(new Runnable() {
                @Override
                public void run() {

                    File sourceFile = new File(AppConstants.baseDir + "/" + AppConstants.FORAGER_DIR + "/" + AppConstants.ABOUT_DIR + "/" + count + ".png");

                    if (sourceFile.exists()) {
                        Bitmap myBitmap = BitmapFactory.decodeFile(sourceFile.getAbsolutePath());
                        image_about.setImageBitmap(myBitmap);
                    }
                    count++;
                    if (count > fileCount) {
                        count = 1;
                    }

                }
            });
        }
    };


    private void callBack() {
        Intent supplementsIntent = new Intent(getActivity(), DashBoardActivity.class);
        getActivity().overridePendingTransition(0, 0);
        supplementsIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        supplementsIntent.putExtra("dashboard", "moretag");
        getActivity().finish();
        getActivity().overridePendingTransition(0, 0);
        getActivity().startActivity(supplementsIntent);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId())
        {
            case R.id.about_back:
               /* android.support.v4.app.FragmentManager fm = getFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.fragment_container, new MoreFragment(), "MoreFragment");
                ft.addToBackStack("MoreFragment");
                ft.commit();*/
                callBack();
        }

    }

    public void onStop(){
        super.onStop();
        getActivity().finish();
    }
}
