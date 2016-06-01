package com.forager.meal.ui.activity;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.forager.meal.application.MyApplication;
import com.forager.meal.async.JsonAsync;
import com.forager.meal.constants.AppConstants;
import com.forager.meal.constants.AppUrls;
import com.forager.meal.constants.JsonConstants;
import com.forager.meal.listener.OnWebServiceResponse;
import com.forager.meal.ui.R;
import com.forager.meal.utitlity.Utility;

import org.json.JSONException;
import org.json.JSONObject;

public class RegisterActivity extends Activity implements View.OnClickListener, OnWebServiceResponse{

    Button mSubmit_btn;
    Button mCancel_btn;
    ImageButton mBack_btn;
    EditText mEmail_edit;
    EditText mPassword_edit;
    EditText mConfirm_edit;
    TextView mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        MyApplication.setContext(this);
        initView();
    }

    private void initView() {
        mSubmit_btn = (Button) findViewById(R.id.submit_btn);
        mCancel_btn = (Button) findViewById(R.id.cancel_btn);
        mEmail_edit = (EditText) findViewById(R.id.edit_email);
        mPassword_edit = (EditText) findViewById(R.id.edit_password);
        mConfirm_edit = (EditText) findViewById(R.id.edit_confirm_password);
        mBack_btn = (ImageButton) findViewById(R.id.back);
        mTitle=(TextView)findViewById(R.id.title_register);
        mTitle.setTypeface(MyApplication.getFont());

        mBack_btn.setOnClickListener(this);
        mSubmit_btn.setOnClickListener(this);
        mCancel_btn.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_register, menu);

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
        int id = v.getId();
        switch (id) {
            case R.id.submit_btn:
                validate_Fields();

                break;
            case R.id.cancel_btn:
            case R.id.back:
                finish();
                break;
            default:
                break;
        }
    }
    private void validate_Fields() {
        if(Utility.isBlank(mEmail_edit.getText().toString())) {
            Utility.showAlert(RegisterActivity.this, AppConstants.ENTER_EMAIL, false);
        } else if(!Utility.validateEmailId(mEmail_edit.getText().toString())){
            Utility.showAlert(RegisterActivity.this, AppConstants.ENTER_VALID_EMAIL, false);
        }else if(Utility.isBlank(mPassword_edit.getText().toString())) {
            Utility.showAlert(RegisterActivity.this, AppConstants.ENTER_PASSWORD, false);
        } else if(Utility.isBlank(mConfirm_edit.getText().toString())) {
            Utility.showAlert(RegisterActivity.this, AppConstants.ENTER_CONFIRM_PASSWORD, false);
        } else if(!mPassword_edit.getText().toString().equals(mConfirm_edit.getText().toString())) {
            Utility.showAlert(RegisterActivity.this, AppConstants.PASSWORD_NOT_MATCH, false);
        } else {
            try {
                JSONObject jsonObj = new JSONObject();
                jsonObj.put(JsonConstants.EMAIL, mEmail_edit.getText().toString());
                jsonObj.put(JsonConstants.PASSWORD, mPassword_edit.getText().toString());

                Utility.showPDialog(this);
                if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.HONEYCOMB)
                    new JsonAsync(RegisterActivity.this, jsonObj.toString(), AppConstants.REGISTER_CALL).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, AppUrls.REGISTER_URL);
                else
                    new JsonAsync(RegisterActivity.this, jsonObj.toString(), AppConstants.REGISTER_CALL).execute(AppUrls.REGISTER_URL);

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }
    @Override
    public void onResponseReceived(String response, String methodName) {
        Utility.dismissPDialog(this);
        Log.e("Check Response", response);
        if(!response.equals("")) {
            try {
                JSONObject mJsonObject = new JSONObject(response);
                String errorCode = mJsonObject.getString(JsonConstants.ERROR_CODE);
                if(errorCode.equals(AppConstants.ERROR_CODE_SUCCESS)) {
                    mEmail_edit.setText("");
                    mPassword_edit.setText("");
                    mConfirm_edit.setText("");
                    Utility.showAlert(RegisterActivity.this, AppConstants.VERIFICATION_LINK_SENT, true);

                } else if(errorCode.equals(AppConstants.ERROR_CODE_FAILURE)){
                    Utility.showAlert(RegisterActivity.this, AppConstants.EMAIL_ALREADY_EXISTS, false);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
