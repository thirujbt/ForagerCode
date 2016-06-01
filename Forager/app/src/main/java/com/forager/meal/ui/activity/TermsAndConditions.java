package com.forager.meal.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;

import com.forager.meal.application.MyApplication;
import com.forager.meal.async.DownloadImage;
import com.forager.meal.async.JsonAsync;
import com.forager.meal.bean.UserDetails;
import com.forager.meal.constants.AppConstants;
import com.forager.meal.constants.AppUrls;
import com.forager.meal.constants.JsonConstants;
import com.forager.meal.database.Database;
import com.forager.meal.listener.OnImageDownloadCompleted;
import com.forager.meal.listener.OnWebServiceResponse;
import com.forager.meal.ui.R;
import com.forager.meal.utitlity.Utility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;

public class TermsAndConditions extends AppCompatActivity implements OnWebServiceResponse, OnImageDownloadCompleted {

    private WebView mTermsAndConditions;
    String termsText;
    Button mAgreeBtn;
    TextView txtTermsAndConditions;
    HashMap<String, String> mHashMap = new HashMap<>();
    Database db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms_and_conditions);
        db = Database.getInstance(TermsAndConditions.this);
        db.getMyWritableDatabase();
        mTermsAndConditions =(WebView) findViewById(R.id.agree_content);
        mTermsAndConditions.setBackgroundColor(Color.TRANSPARENT);
        MyApplication.setContext(TermsAndConditions.this);
        txtTermsAndConditions=(TextView)findViewById(R.id.title_terms_conditions);
        txtTermsAndConditions.setTypeface(MyApplication.getFont());
        try {
            JSONObject jsonObj = new JSONObject();
            jsonObj.put(JsonConstants.USER_ID, AppConstants.APP_USER_ID);
            jsonObj.put(JsonConstants.PLAN_ID, db.getActivePlanId());
            jsonObj.put(JsonConstants.CALORIE_TYPE, UserDetails.getInstance().getPlan_calorie());
            Utility.showPDialog(this);

            if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.HONEYCOMB)
                new JsonAsync(TermsAndConditions.this, jsonObj.toString(), AppConstants.UPDATE_ACTIVE_CALL).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, AppUrls.ACTIVE_PLAN_URL);
            else
                new JsonAsync(TermsAndConditions.this, jsonObj.toString(), AppConstants.UPDATE_ACTIVE_CALL).execute(AppUrls.ACTIVE_PLAN_URL);


        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        mAgreeBtn = (Button) findViewById(R.id.agree_btn);
        mAgreeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TermsAndConditions.this, DashBoardActivity.class));
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        db.closeDatabase();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_terms_and_conditions, menu);
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



    @Override
    public void onResponseReceived(String response, String methodName) {

        Utility.dismissPDialog(this);

        if(!Utility.isBlank(response)) {
            Log.e("Check Response", response);
            try {

                JSONObject mJsonObject = new JSONObject(response);
                String errorCode = mJsonObject.getString(JsonConstants.STATUS);

                if(errorCode.equals(AppConstants.STATUS_SUCCESS)) {

                    JSONObject dailyTips = mJsonObject.getJSONObject(JsonConstants.DAILYTIPS);
                    termsText = dailyTips.getString(JsonConstants.TERMS_CONDITIONS);
                    String aboutText = dailyTips.getString(JsonConstants.APP_ABOUT_US);
                    JSONArray aboutUsArray = dailyTips.getJSONArray(JsonConstants.APP_ABOUT_US_IMAGES);
                    for (int i = 0; i < aboutUsArray.length(); i++) {
                        mHashMap.put(aboutUsArray.get(i).toString(), AppConstants.ABOUT_DIR);
                    }
                    Utility.writeIntoFile(TermsAndConditions.this, termsText, AppConstants.TERMS_FILENAME);
                    Utility.writeIntoFile(TermsAndConditions.this, aboutText, AppConstants.ABOUT_FILENAME);

                    File mFile = new File(AppConstants.baseDir + "/" + AppConstants.FORAGER_DIR + "/" + AppConstants.ABOUT_DIR);
                    int fileCount = 0;
                    if (mFile.exists()) {
                        fileCount = mFile.list().length;
                    }
                    if (fileCount != mHashMap.size()) {

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                            new DownloadImage(TermsAndConditions.this, mHashMap, AppConstants.ABOUT_IMAGE_CALL).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                        } else {
                            new DownloadImage(TermsAndConditions.this, mHashMap, AppConstants.ABOUT_IMAGE_CALL).execute();
                        }
                    } else  {
                        mHashMap.clear();
                    }
                } else {
                    Utility.showAlert(TermsAndConditions.this, AppConstants.SERVER_ERROR, false);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        Spanned test= Html.fromHtml(termsText);

        mTermsAndConditions.loadData(Html.toHtml(test), "text/html; charset=UTF-8", null);
        mTermsAndConditions.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return true;
            }
        });

    }

    @Override
    public void onDownloadCompleted(String methodName) {
        Utility.dismissPDialog(this);
    }
}
