package com.forager.meal.ui.activity;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.forager.meal.async.JsonAsync;
import com.forager.meal.constants.AppConstants;
import com.forager.meal.constants.AppUrls;
import com.forager.meal.constants.JsonConstants;
import com.forager.meal.listener.OnWebServiceResponse;
import com.forager.meal.ui.R;
import com.forager.meal.utitlity.Utility;

import org.json.JSONException;
import org.json.JSONObject;

public class ForgotPasswordActivity extends Activity implements View.OnClickListener, OnWebServiceResponse{

    Button mNewPassword_btn;
    Button mCancel_btn;
    ImageButton mBack_btn;
    EditText mEmail_edit;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        initView();
    }

    private void initView() {
        mNewPassword_btn = (Button) findViewById(R.id.newpassword_btn);
        mCancel_btn = (Button) findViewById(R.id.cancel_btn);
        mEmail_edit = (EditText) findViewById(R.id.edit_name);
        mBack_btn = (ImageButton) findViewById(R.id.back);
        mBack_btn.setOnClickListener(this);
        mNewPassword_btn.setOnClickListener(this);
        mCancel_btn.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_forgot_password, menu);
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
    public void onClick(View v) {
        {
            int id = v.getId();
            switch (id) {
                case R.id.newpassword_btn:
                    validate_Fields();
                    break;
                case R.id.cancel_btn:
                    finish();
                    break;
                case R.id.back:
                    finish();
                    break;
                default:
                    break;
            }
        }
    }

    private void validate_Fields() {
        if(Utility.isBlank(mEmail_edit.getText().toString())) {
            Utility.showAlert(ForgotPasswordActivity.this, AppConstants.ENTER_EMAIL, false);
        } else if(!Utility.validateEmailId(mEmail_edit.getText().toString())){
            Utility.showAlert(ForgotPasswordActivity.this, AppConstants.ENTER_VALID_EMAIL, false);
        } else {
            try {
                JSONObject jsonObj = new JSONObject();
                jsonObj.put(JsonConstants.EMAIL, mEmail_edit.getText().toString());

                Utility.showPDialog(this);

                if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.HONEYCOMB)
                    new JsonAsync(ForgotPasswordActivity.this, jsonObj.toString(), AppConstants.FORGET_PASSWORD_CALL).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, AppUrls.FORGET_PASSWORD_URL);
                else
                    new JsonAsync(ForgotPasswordActivity.this, jsonObj.toString(), AppConstants.FORGET_PASSWORD_CALL).execute(AppUrls.FORGET_PASSWORD_URL);




            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onResponseReceived(String response, String methodName) {


        Utility.dismissPDialog(this);

        if(!Utility.isBlank(response)) {

            try {

                JSONObject mJsonObject = new JSONObject(response);
                String errorCode = mJsonObject.getString(JsonConstants.STATUS);

                if(errorCode.equals(AppConstants.STATUS_SUCCESS)) {

                    String message = mJsonObject.getString(JsonConstants.MESSAGE);
                    Utility.showAlert(ForgotPasswordActivity.this, message, false);

                } else {
                    Utility.showAlert(ForgotPasswordActivity.this, AppConstants.SERVER_ERROR, true);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
