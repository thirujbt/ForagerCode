package com.forager.meal.ui.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.forager.meal.ui.activity.DashBoardActivity;
import com.forager.meal.utitlity.Utility;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Chithra on 9/24/2015.
 */
public class ChangePasswordFragment extends Fragment implements View.OnClickListener, OnWebServiceResponse {

    Button mChangePassword_btn;
    ImageButton mBack_btn;
    EditText mChangePassword_edit;
    EditText mNewPassword_edit;
    EditText mConfirmPassword_edit;
    TextView mChangePassword_title;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_changepassword, container, false);
        initView(rootView);

        return rootView;
    }

    private void initView(View rootView) {
        PlanControllerFragment.flag=false;
        MyApplication.setContext(getActivity());
        mChangePassword_title = (TextView) rootView.findViewById(R.id.title_change_password);
        mChangePassword_title.setTypeface(MyApplication.getFont());
        mChangePassword_btn = (Button) rootView.findViewById(R.id.btn_change_password);
        mChangePassword_edit = (EditText) rootView.findViewById(R.id.edit_change_password);
        mNewPassword_edit = (EditText) rootView.findViewById(R.id.edit_new_password);
        mConfirmPassword_edit = (EditText) rootView.findViewById(R.id.edit_confirm_password);
        mBack_btn = (ImageButton) rootView.findViewById(R.id.back);
        mBack_btn.setOnClickListener(this);
        mChangePassword_btn.setOnClickListener(this);

    }

    private void callBack() {
        /*FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.fragment_container, new MoreFragment(), "MoreFragment");
        fm.popBackStack("MoreFragment", 0);
        ft.commit();*/
        Intent supplementsIntent = new Intent(getActivity(), DashBoardActivity.class);
        getActivity().overridePendingTransition(0, 0);
        supplementsIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        supplementsIntent.putExtra("dashboard", "moretag");
        getActivity().finish();
        getActivity().overridePendingTransition(0, 0);
        getActivity().startActivity(supplementsIntent);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.btn_change_password:
                validatePassword();
                break;
            case R.id.back:
                callBack();
                break;
        }

    }


    private void validatePassword() {
        if (Utility.isBlank(mChangePassword_edit.getText().toString())) {
            Utility.showAlert(this.getActivity(), AppConstants.ENTER_CHANGE_PASSWORD, false);
        } else if (Utility.isBlank(mNewPassword_edit.getText().toString())) {
            Utility.showAlert(this.getActivity(), AppConstants.ENTER_NEW_PASSWORD, false);
        } else if (Utility.isBlank(mConfirmPassword_edit.getText().toString())) {
            Utility.showAlert(this.getActivity(), AppConstants.ENTER_CONFIRM_PASSWORD, false);
        } else if (!mNewPassword_edit.getText().toString().equals(mConfirmPassword_edit.getText().toString())) {
            Utility.showAlert(this.getActivity(), AppConstants.PASSWORD_NOT_MATCH, false);
        } else {
            try {

                JSONObject jsonObj = new JSONObject();
                SharedPreferences preferences = Utility.getSharedPreferences(getActivity(), AppConstants.SHARED_PREFES_USER_DETAILS);
                if (preferences.contains(JsonConstants.USER_ID)) {
                    jsonObj.put(JsonConstants.USER_ID, preferences.getString(JsonConstants.USER_ID, ""));
                }

                jsonObj.put(JsonConstants.OLD_PASSWORD, mChangePassword_edit.getText().toString());
                jsonObj.put(JsonConstants.NEW_PASSWORD, mNewPassword_edit.getText().toString());


                Utility.showPDialog(getActivity());

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
                    new JsonAsync(this, jsonObj.toString(), AppConstants.CHANGE_PASSWORD_CALL).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, AppUrls.CHANGE_PASSWORD_URL);
                else
                    new JsonAsync(this, jsonObj.toString(), AppConstants.CHANGE_PASSWORD_CALL).execute(AppUrls.CHANGE_PASSWORD_URL);


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public void onResponseReceived(String response, String methodName) {
        Utility.dismissPDialog(getActivity());
        Log.e("Check Response", response);
        if (!response.equals("")) {
            try {
                JSONObject mJsonObject = new JSONObject(response);
                String errorCode = mJsonObject.getString(JsonConstants.ERROR_CODE);
                String message = mJsonObject.getString(JsonConstants.MESSAGE);
                if (errorCode.equals(AppConstants.ERROR_CODE_SUCCESS)) {
                    mChangePassword_edit.setText("");
                    mNewPassword_edit.setText("");
                    mConfirmPassword_edit.setText("");
                    Utility.showAlert(getActivity(), message, false);
                } else if (errorCode.equals(AppConstants.ERROR_CODE_FAILURE)) {
                    Utility.showAlert(this.getActivity(), message, false);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

}

