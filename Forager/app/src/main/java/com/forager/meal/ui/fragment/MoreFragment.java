package com.forager.meal.ui.fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.forager.meal.constants.AppConstants;
import com.forager.meal.constants.AppUrls;
import com.forager.meal.listener.OnImageDownloadCompleted;
import com.forager.meal.listener.OnWebServiceResponse;
import com.forager.meal.ui.R;
import com.forager.meal.ui.activity.DashBoardActivity;
import com.forager.meal.ui.activity.IntroActivity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Chithra on 9/23/2015.
 */
public class MoreFragment extends Fragment implements View.OnClickListener, OnWebServiceResponse, OnImageDownloadCompleted {

    private ImageView imageView;
    private TextView textView;
    private Button btnAbout;
    private Button btnWorks;
    private Button btnFeedback;
    private Button btnShare;
    private Button btnChangePassword;
    private Button btnTermsConditions;
    private Button btnSignOut;
    private Button btnBlog;
    private Button btnUpgrade;
    private TextView title_more;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_more, container, false);
        findViews(rootView);
        storeImage();
        return rootView;
    }


    private void findViews(View rootView) {
        PlanControllerFragment.flag = false;
        imageView = (ImageView) rootView.findViewById(R.id.imageView);

        btnAbout = (Button) rootView.findViewById(R.id.btn_about);
        btnWorks = (Button) rootView.findViewById(R.id.btn_works);
        btnFeedback = (Button) rootView.findViewById(R.id.btn_feedback);
        btnShare = (Button) rootView.findViewById(R.id.btn_share);
        btnChangePassword = (Button) rootView.findViewById(R.id.btn_change_password);
        btnTermsConditions = (Button) rootView.findViewById(R.id.btn_terms_conditions);
        btnSignOut = (Button) rootView.findViewById(R.id.btn_sign_out);
        btnBlog = (Button) rootView.findViewById(R.id.btn_blog);
        btnUpgrade = (Button) rootView.findViewById(R.id.btn_upgrade);

        btnAbout.setOnClickListener(this);
        btnWorks.setOnClickListener(this);
        btnFeedback.setOnClickListener(this);
        btnShare.setOnClickListener(this);
        btnChangePassword.setOnClickListener(this);
        btnTermsConditions.setOnClickListener(this);
        btnSignOut.setOnClickListener(this);
        btnBlog.setOnClickListener(this);
        btnUpgrade.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btn_about:

                Intent changeabout = new Intent(getActivity(), DashBoardActivity.class);
                getActivity().overridePendingTransition(0, 0);
                changeabout.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                changeabout.putExtra("dashboard", "abouttag");
                getActivity().finish();
                getActivity().overridePendingTransition(0, 0);
                getActivity().startActivity(changeabout);

                break;
            case R.id.btn_works:

                Intent process = new Intent(getActivity(), DashBoardActivity.class);
                getActivity().overridePendingTransition(0, 0);
                process.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                process.putExtra("dashboard", "worksvideo");
                getActivity().finish();
                getActivity().overridePendingTransition(0, 0);
                getActivity().startActivity(process);
                break;
            case R.id.btn_feedback:

                Intent terms1 = new Intent(getActivity(), DashBoardActivity.class);
                getActivity().overridePendingTransition(0, 0);
                terms1.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                terms1.putExtra("dashboard", "moretag");
                getActivity().finish();
                getActivity().overridePendingTransition(0, 0);
                getActivity().startActivity(terms1);

                email(getActivity(), AppConstants.FEEDBACK_TO, AppConstants.FEEDBACK_SUBJECT, AppConstants.FEEDBACK_CONTENT);
                break;
            case R.id.btn_share:
                showPickerDialog();
                break;
            case R.id.btn_change_password:

                Intent changepwd = new Intent(getActivity(), DashBoardActivity.class);
                getActivity().overridePendingTransition(0, 0);
                changepwd.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                changepwd.putExtra("dashboard", "changepassword");
                getActivity().finish();
                getActivity() .overridePendingTransition(0, 0);
                getActivity().startActivity(changepwd);
                break;
            case R.id.btn_terms_conditions:
                Intent terms = new Intent(getActivity(), DashBoardActivity.class);
                getActivity().overridePendingTransition(0, 0);
                terms.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                terms.putExtra("dashboard", "termsandcondition");
                getActivity().finish();
                getActivity().overridePendingTransition(0, 0);
                getActivity().startActivity(terms);
                break;
            case R.id.btn_sign_out:
                Intent signout = new Intent(getActivity(), IntroActivity.class);
                /*SharedPreferences.Editor Editor = Utility.getSharedPreferences(getActivity(), AppConstants.SHARED_PREFES_LOGIN_DETAILS).edit();
                Editor.putBoolean(AppConstants.SIGNED_IN, false);
                Editor.commit();*/
                signout.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                getActivity().startActivity(signout);
                getActivity().finish();
                break;
            case R.id.btn_upgrade:
                Intent upgrade = new Intent(getActivity(), DashBoardActivity.class);
                getActivity().overridePendingTransition(0, 0);
                upgrade.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                upgrade.putExtra("dashboard", "upgradetag");
                getActivity().finish();
                getActivity().overridePendingTransition(0, 0);
                getActivity().startActivity(upgrade);
                break;
            case R.id.btn_blog:
                blogUrl();
             /*   String url = "http://foragerpro.com/Forager/blog/";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);*/
                break;
        }
    }

    public void blogUrl() {
        Uri uri = Uri.parse(AppUrls.BLOG_URL); // missing 'http://' will cause crashed
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }

    @Override
    public void onDownloadCompleted(String methodName) {

    }

    @Override
    public void onResponseReceived(String response, String methodName) {

    }

    public static void email(Context context, String to, String subject, String body) {

        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                "mailto", to, null));
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
        emailIntent.putExtra(Intent.EXTRA_TEXT, body);
        context.startActivity(Intent.createChooser(emailIntent, "Send email..."));

       /* Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.setData(Uri.parse("mailto:"));
        intent.putExtra(Intent.EXTRA_TEXT,body);
        	intent.putExtra(Intent.EXTRA_SUBJECT,subject );
        intent.setType("text/plain");
        context.startActivity( intent);*/

       /* StringBuilder builder = new StringBuilder("mailto:" + Uri.encode(to));
        if (subject != null) {
            builder.append("?subject=" + Uri.encode(Uri.encode(subject)));
            if (body != null) {
                builder.append("&body=" + Uri.encode(Uri.encode(body)));
            }
        }
        String uri = builder.toString();
        Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse(uri));
        context.startActivity(intent);*/
    }

    public void share(String nameApp, String message) {

        try {
            List<Intent> targetedShareIntents = new ArrayList<Intent>();
            Intent share = new Intent(android.content.Intent.ACTION_SEND);
            share.setType("image/jpeg");
            List<ResolveInfo> resInfo = getActivity().getPackageManager().queryIntentActivities(share, 0);
            if (!resInfo.isEmpty()) {
                for (ResolveInfo info : resInfo) {
                    Intent targetedShare = new Intent(android.content.Intent.ACTION_SEND);
                    targetedShare.setType("image/jpeg"); // put here your mime type
                    if (info.activityInfo.packageName.toLowerCase().contains(nameApp) || info.activityInfo.name.toLowerCase().contains(nameApp)) {
                        targetedShare.putExtra(Intent.EXTRA_TITLE, "Forager");

                        // targetedShare.putExtra(Intent.EXTRA_TEXT, "http://foragerpro.com/Forager");

                        targetedShare.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(new File(AppConstants.baseDir + "/" + AppConstants.FORAGER_DIR + "/" + AppConstants.PROFILE_DIR + "/" + "shareImage.png")));
                        targetedShare.setPackage(info.activityInfo.packageName);
                        targetedShareIntents.add(targetedShare);
                    }
                }

                Intent terms = new Intent(getActivity(), DashBoardActivity.class);
                terms.putExtra("dashboard", "moretag");
                getActivity().startActivity(terms);
                getActivity().finish();

                Intent chooserIntent = Intent.createChooser(targetedShareIntents.remove(0), "Select app to share");

                chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, targetedShareIntents.toArray(new Parcelable[]{}));
                startActivity(chooserIntent);
            }
        } catch (Exception e) {
            Log.v("VM", "Exception while sending image on" + nameApp + " " + e.getMessage());
        }
    }


    public void onBackPressed() {
        Intent intent = new Intent(getActivity(), MoreFragment.class);
        startActivity(intent);
    }


    private void showPickerDialog() {
        final Dialog mDialog = new Dialog(getActivity(), R.style.CustomAlertDialogStyle);
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        WindowManager.LayoutParams wmlp = mDialog.getWindow().getAttributes();
        wmlp.gravity = Gravity.BOTTOM;
        mDialog.setContentView(R.layout.dialog_profile_picker);
        TextView mTitle = (TextView) mDialog.findViewById(R.id.profile_skip);
        TextView mFacebook = (TextView) mDialog.findViewById(R.id.take_photo);
        TextView mTwitter = (TextView) mDialog.findViewById(R.id.upload_library);
        TextView mCancel = (TextView) mDialog.findViewById(R.id.cancel);

        mTitle.setText("Share ForagerPro with your friends!");
        mTitle.setTextColor(0xFF959594);
        mTitle.setTextSize(14);
        mFacebook.setText("Facebook");
        mTwitter.setText("Twitter");

        mFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // storeimage();
                share("facebook", "https://play.google.com/store/apps/details?id=com.forager.pickzy.ui");
                mDialog.dismiss();
            }
        });

        mTwitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // storeimage();
                share("twitter", "https://play.google.com/store/apps/details?id=com.forager.pickzy.ui");
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

    public void storeImage() {
        String commonPath = Environment.getExternalStorageDirectory().toString() + "/leaf";
        File direct = new File(AppConstants.baseDir + "/" + AppConstants.FORAGER_DIR + "/" + AppConstants.PROFILE_DIR + "/");

        if (!direct.exists()) {
            if (direct.mkdir()) {
                Log.d("tag", "directory created");
                //directory is created;
            }
        }
        Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.forager_leaf);
        OutputStream outStream = null;
        File savingFile = new File(AppConstants.baseDir + "/" + AppConstants.FORAGER_DIR + "/" + AppConstants.PROFILE_DIR + "/", "shareImage.png");
        if (!savingFile.exists()) {
            Log.d("tag", "file is created");

            try {
                savingFile.createNewFile();
                outStream = new FileOutputStream(savingFile);
                bm.compress(Bitmap.CompressFormat.PNG, 100, outStream);
                outStream.flush();
                outStream.close();
                //  Log.d("tag", "Saved");

            } catch (FileNotFoundException e) {
                e.printStackTrace();

            } catch (IOException e) {
                e.printStackTrace();

            }
        }

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

    };

    public void onStop() {
        super.onStop();
        // getActivity().finish();
    }
}

