package com.forager.meal.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Toast;

import com.forager.meal.adapter.CarouselAdapter;
import com.forager.meal.async.JsonAsync;
import com.forager.meal.constants.AppConstants;
import com.forager.meal.constants.AppUrls;
import com.forager.meal.constants.JsonConstants;
import com.forager.meal.inAppPurchase.IabHelper;
import com.forager.meal.inAppPurchase.IabResult;
import com.forager.meal.inAppPurchase.Inventory;
import com.forager.meal.inAppPurchase.Purchase;
import com.forager.meal.listener.OnWebServiceResponse;
import com.forager.meal.listener.PurchaseResponse;
import com.forager.meal.ui.R;
import com.forager.meal.utitlity.Utility;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import it.moondroid.coverflow.components.ui.containers.FeatureCoverFlow;

/**
 * Created by Chithra on 5/11/2016.
 */
public class UpgradeFragment extends Fragment implements View.OnClickListener, OnWebServiceResponse, PurchaseResponse {

    Button oneMonth, threeMonth, oneYear;
    private IabHelper iabHelper;

    private int selectposi;
    private String duration;

    //Key getting from google play developer console---->>Services and APIs tab
    private static final String base64EncodedPublicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAsGzzu0x3x25zr+ASLXivFyvz5baynCPxVK4cM7GBHQUZ1+L0Rg0Tn70cLBYW/TQaRHzESiL/SXH7qqA4oV6wbb+bKQHvgaPtUNkp5/vLOBrc06K+42DyVvtwp4U3tzd/FSGvpc5U1cKPWMwbVWpT4qmdcYJ5H6MuydGz+cKf6JZtQYhItXc/0JaKopX1/uzZIwohRNASBFG9P82hRMuD0+BA/dXGtRDgILXmtyagutvWBddMjfJ7D2Zjgx4OmN31A8mKP4/M3E/G/Xy9vHqgRsKYQoo9jSvH5ZkaLAYEvXVCkdel49I3d1Luz+pF+IYKj7tGchKjQ6Y+9neKwql6vQIDAQAB";

    public static String purchaseClicked = "";


    public static final int RC_REQUEST = 10001;

    //SET this values in google play developer console---->>in app product-->>Add new Product-->>
    private static final String SKU_ONE_MON_PRO_ID = "pidonemnth";
    private static final String SKU_THREE_MON_PRO_ID = "pidthreemnth";
    private static final String SKU_ONE_YEAR_PRO_ID = "pidoneyr";
    // private static final String SKU_ONE_MON_PRO_ID = "testproduct"; //for testing purpose
    // private static final String SKU_ONE_MON_PRO_ID = "test_new";//for testing purpose

    SharedPreferences pref1;

    private FeatureCoverFlow coverFlow;

    private CarouselAdapter adapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_purchase, container, false);
        findViews(rootView);


        adapter = new CarouselAdapter(getActivity());
        coverFlow.setAdapter(adapter);
        coverFlow.setOnScrollPositionListener(onScrollListener());
        coverFlow.setReflectionOpacity(0);



     /*   iabHelper = new IabHelper(this.getActivity(), base64EncodedPublicKey);
        // Start setup. This is asynchronous and the specified listener
        // will be called once setup completes.


        iabHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
            @Override
            public void onIabSetupFinished(IabResult result) {
                if (result.isFailure()) {
                    return;
                }
                iabHelper.queryInventoryAsync(queryInventoryFinishedListener);
            }
        });*/
        coverFlow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int status, long l) {
                // Toast.makeText(view.getContext(), "test", Toast.LENGTH_SHORT).show();
                if (status == 0) {
                    oneMonthPurchase();
                }
                if (status == 1) {
                    threeMonthPurchase();
                }
                if (status == 2) {
                    oneYearPurchase();
                }

            }
        });


        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        try {
            iabHelper = new IabHelper(getActivity(), base64EncodedPublicKey);
            // Start setup. This is asynchronous and the specified listener

            // will be called once setup completes.
           /* iabHelper.startSetup(new OnIabSetupFinishedListener() {
                @Override
                public void onIabSetupFinished(IabResult result) {
                    if (result.isFailure()) {
                        System.out.println("checking...........");
                        return;
                    }
                    iabHelper.queryInventoryAsync(queryInventoryFinishedListener);
                }
            });*/
            //iabHelper.enableDebugLogging(false);

            iabHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
                public void onIabSetupFinished(IabResult result) {
                    Log.e("-->>", "Setup finished.");
                    // Have we been disposed of in the meantime? If so, quit.
                    if (iabHelper == null)
                        return;

                    // IAP is fully set up. Now, let's get an inventory of stuff we own.

                    Log.e("--->>", "Setup successful. Querying inventory.");
                    iabHelper.queryInventoryAsync(queryInventoryFinishedListener);
                }
            });


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private FeatureCoverFlow.OnScrollPositionListener onScrollListener() {
        return new FeatureCoverFlow.OnScrollPositionListener() {
            @Override
            public void onScrolledToPosition(int position) {
                // Toast.makeText(getActivity(), "test"+position, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onScrolling() {
                Log.i("MainActivity", "scrolling");
            }
        };
    }

    private void findViews(View rootView) {
        coverFlow = (FeatureCoverFlow) rootView.findViewById(R.id.coverflow);

        pref1 = getActivity().getSharedPreferences(AppConstants.SHARED_PREFES_USER_DETAILS, Context.MODE_PRIVATE);

      /*  oneMonth = (Button) rootView.findViewById(R.id.onemonth_btn);

        threeMonth = (Button) rootView.findViewById(R.id.threemonth_btn);
        oneYear = (Button) rootView.findViewById(R.id.oneyear_btn);

        oneMonth.setOnClickListener(this);
        threeMonth.setOnClickListener(this);
        oneYear.setOnClickListener(this);
*/

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (!iabHelper.handleActivityResult(requestCode,
                resultCode, data)) {
            super.onActivityResult(requestCode, resultCode, data);
        }else{

        }
    }
    @Override
    public void onClick(View view) {
        int v = view.getId();
     /*   if (v == R.id.onemonth_btn) {
            purchaseClicked="onemonth";
            oneMonthPurchase();

        } else if (v == R.id.threemonth_btn) {
            purchaseClicked="threemonth";
            threeMonthPurchase();
        } else if (v == R.id.oneyear_btn) {
            purchaseClicked="oneyear";
            oneYearPurchase();
        }*/
    }


    // Listener that's called when we finish querying the items and subscriptions we own
/*
    IabHelper.QueryInventoryFinishedListener queryInventoryFinishedListener = new IabHelper.QueryInventoryFinishedListener() {
        @Override
        public void onQueryInventoryFinished(IabResult result, Inventory inventory) {
            if (iabHelper == null) {
                return;
            }
            if (result.isFailure()) {
                return;
            }
            Purchase adsPurchase = inventory.getPurchase(SKU_ONE_MON_PRO_ID);
            if ((adsPurchase != null) && (verifyDeveloperPayload(adsPurchase))) {
                iabHelper.consumeAsync(adsPurchase,
                        onConsumeFinishedListener);
                return;
            }
            Purchase adsUpdateFrq = inventory.getPurchase(SKU_THREE_MON_PRO_ID);
            if ((adsUpdateFrq != null) && (verifyDeveloperPayload(adsUpdateFrq))) {
                iabHelper.consumeAsync(adsUpdateFrq, onConsumeFinishedListener);
                return;
            }
            Purchase adsUpdateyear = inventory.getPurchase(SKU_ONE_YEAR_PRO_ID);
            if ((adsUpdateyear != null) && (verifyDeveloperPayload(adsUpdateyear))) {
                iabHelper.consumeAsync(adsUpdateyear, onConsumeFinishedListener);
                return;
            }
        }
    };
*/


    // Listener that's called when we finish querying the items and
    // subscriptions we own
    IabHelper.QueryInventoryFinishedListener queryInventoryFinishedListener = new IabHelper.QueryInventoryFinishedListener() {
        @Override
        public void onQueryInventoryFinished(final IabResult result, final Inventory inventory) {
            Log.e("--->>>", "Query inventory finished.");

            // Have we been disposed of in the meantime? If so, quit.
            if (iabHelper == null) {
                return;
            }

            // Is it a failure?
            if (result.isFailure()) {
                Toast.makeText(getActivity(), "Failed to query inventory: " + result, Toast.LENGTH_LONG).show();
                return;
            }
            Log.e("--->>>", "Query inventory was successful."+result);

            /**
             * Check for items we own. Notice that for each purchase, we check
             * the developer payload to see if it's correct! See
             * verifyDeveloperPayload().
             */

            Purchase ONE_MON = inventory.getPurchase(SKU_ONE_MON_PRO_ID);
            if (ONE_MON != null && verifyDeveloperPayload(ONE_MON)) {
                Log.e("-->>", "We have ONE_MON. Consuming it.");
                iabHelper.consumeAsync(ONE_MON, mConsumeFinishedListener);
                return;
            }
            Purchase THREE_MON = inventory.getPurchase(SKU_THREE_MON_PRO_ID);
            if (THREE_MON != null && verifyDeveloperPayload(THREE_MON)) {
                Log.e("-->>", "We have Coins. Consuming it.");
                iabHelper.consumeAsync(THREE_MON, mConsumeFinishedListener);
                return;
            }
            Purchase ONE_YEAR = inventory.getPurchase(SKU_ONE_YEAR_PRO_ID);
            if (ONE_YEAR != null && verifyDeveloperPayload(ONE_YEAR)) {
                Log.e("-->>", "We have Coins. Consuming it.");
                iabHelper.consumeAsync(ONE_YEAR, mConsumeFinishedListener);
                return;
            }

            Log.e("-->>", "Initial inventory query finished; enabling main UI.");
        }
    };

    IabHelper.OnConsumeFinishedListener mConsumeFinishedListener = new IabHelper.OnConsumeFinishedListener() {
        public void onConsumeFinished(Purchase purchase, IabResult result) {
            Log.e("-->>", "Consumption finished. Purchase: " + purchase + ", result: " + result);
            Calendar c = Calendar.getInstance();
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            String formattedDate = df.format(c.getTime());
            // if we were disposed of in the meantime, quit.

            if (iabHelper == null) {
                Log.e("-->>", "iabHelper == null");
                return;
            }

            // We know this is the "gas" sku because it's the only one we consume,
            // so we don't check which sku was consumed. If you have more than one
            // sku, you probably should check...

            //if (result.isSuccess()) {
            // successfully consumed, so we apply the effects of the item in our
            // game world's logic, which in our case means filling the gas tank a bit

            if(result.isSuccess()){
                if (purchase.getSku().equals(SKU_ONE_MON_PRO_ID)) {
                    Log.e("purchase-TIME", "---->>>" + purchase.getPurchaseTime());
                    Utility.showToast(getActivity(), "You have purchased for one month");
                    //Call Web Service
                    startUpdatePurchaseStatus(formattedDate + " " + purchase.getPurchaseTime());
                } else if (purchase.getSku().equals(SKU_THREE_MON_PRO_ID)) {
                    Log.e("purchase-TIME", "---->>>" + purchase.getPurchaseTime());
                    Utility.showToast(getActivity(), "You have purchased for three month");
                    //Call Web Service
                    startUpdatePurchaseStatus(formattedDate + " " + purchase.getPurchaseTime());
                } else if (purchase.getSku().equals(SKU_ONE_YEAR_PRO_ID)) {

                    Utility.showToast(getActivity(), "You have purchased for one year");
                    Log.e("purchase-TIME", "---->>>" + purchase.getPurchaseTime());

                    //Call Web Service
                    startUpdatePurchaseStatus(formattedDate + " " + purchase.getPurchaseTime());
                }
            }else{
                Toast.makeText(getActivity(), "Faild purchasing", Toast.LENGTH_LONG).show();
            }



               /*

                Log.e("-->>", "Consumption successful. Provisioning.");

            } else {
                Toast.makeText(getActivity(), "Failed Purchase", Toast.LENGTH_SHORT).show();
            }
            Log.e("-->>", "End consumption flow.");
        */
        }
    };


    IabHelper.OnIabPurchaseFinishedListener mPurchaseFinishedListener = new IabHelper.OnIabPurchaseFinishedListener() {
        public void onIabPurchaseFinished(IabResult result, Purchase purchase) {

            Log.e("pu", ">>>>>" + SKU_ONE_MON_PRO_ID);

            Log.e("-->", "Purchase finished: " + result + ", purchase: " + purchase);

            // if we were disposed of in the meantime, quit.
            if (iabHelper == null)
                return;

            if (result.isFailure()) {
                return;
            }
           /* if (!verifyDeveloperPayload(purchase)) {
                return;
            }*/

            Log.e("-->", "Purchase successful."+result);

            if (purchase.getSku().equals(SKU_ONE_MON_PRO_ID) ||
                    purchase.getSku().equals(SKU_THREE_MON_PRO_ID) ||
                    purchase.getSku().equals(SKU_ONE_YEAR_PRO_ID)) {
                iabHelper.consumeAsync(purchase, mConsumeFinishedListener);
            }

        }
    };




    // Called when consumption is complete
/*IabHelper.OnConsumeFinishedListener onConsumeFinishedListener = new IabHelper.OnConsumeFinishedListener() {
        @Override
        public void onConsumeFinished(Purchase purchase, IabResult result) {
            Calendar c = Calendar.getInstance();
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            String formattedDate = df.format(c.getTime());
            if (iabHelper == null) {
                return;
            }
            if (result.isSuccess()) {
                // Call service
                if (purchase.getSku().equals(SKU_ONE_MON_PRO_ID)) {
                    Log.e("purchase-TIME", "---->>>" + purchase.getPurchaseTime());
                    Utility.showToast(getActivity(), "You have purchased for one month");
                    //Call Web Service
                    startUpdatePurchaseStatus(formattedDate + " " + purchase.getPurchaseTime());
                } else if (purchase.getSku().equals(SKU_THREE_MON_PRO_ID)) {
                    Log.e("purchase-TIME", "---->>>" + purchase.getPurchaseTime());

                    Utility.showToast(getActivity(), "You have purchased for three month");
                    //Call Web Service
                    startUpdatePurchaseStatus(formattedDate + " " + purchase.getPurchaseTime());
                } else if (purchase.getSku().equals(SKU_ONE_YEAR_PRO_ID)) {

                    Utility.showToast(getActivity(), "You have purchased for one year");
                    Log.e("purchase-TIME", "---->>>" + purchase.getPurchaseTime());

                    //Call Web Service
                    startUpdatePurchaseStatus(formattedDate + " " + purchase.getPurchaseTime());
                }
            } else {
                Utility.showToast(getActivity(), "Faild purchasing");
            }
        }
    };*/

    /**
     * Update purchase status in server
     */
    private void startUpdatePurchaseStatus(String date) {
        String status;
        /*if (purchaseClicked.equalsIgnoreCase(SKU_ONE_MON_PRO_ID)) {
            //Start alarm and get status
            status = "2";
        } else if (purchaseClicked.equalsIgnoreCase(SKU_THREE_MON_PRO_ID)) {
            //Start alarm and get status
            status = "1";

        } else if (purchaseClicked.equalsIgnoreCase(SKU_ONE_YEAR_PRO_ID)) {
            // status alarm and get status
            status = "0";
        } else {
            status = "";
        }*/
        String url = AppUrls.FORAGER_PURCHASE_SAVE_URL;
        String userid = pref1.getString(AppConstants.USER_ID, null);
        System.out.println(userid);
        String act = "1";
        // Set AuthKey and Status

        try {

            //{"user_id":"1090","action":"","in_app":"1","cur_date":"2016-05-11 17:00:00","subs_month":"1","subs_year":""}
            JSONObject jsonObject = new JSONObject();
            jsonObject.accumulate("user_id", userid);
            //jsonObject.accumulate("action", act);
            jsonObject.accumulate("in_app", "1");

            jsonObject.accumulate("cur_date", date);
            if (purchaseClicked.equalsIgnoreCase(SKU_ONE_MON_PRO_ID)) {
                jsonObject.accumulate("subs_month", 1);

            } else if (purchaseClicked.equalsIgnoreCase(SKU_THREE_MON_PRO_ID)) {
                jsonObject.accumulate("subs_month", 3);

            } else if (purchaseClicked.equalsIgnoreCase(SKU_ONE_YEAR_PRO_ID)) {
                jsonObject.accumulate("subs_year", 1);
            }
            if (purchaseClicked.length() > 0) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
                    new JsonAsync(UpgradeFragment.this, jsonObject.toString(), AppConstants.UPDATE_ACTIVE_CALL).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, AppUrls.ACTIVE_PLAN_URL);
                else
                    new JsonAsync(UpgradeFragment.this, jsonObject.toString(), AppConstants.UPDATE_ACTIVE_CALL).execute(AppUrls.ACTIVE_PLAN_URL);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    //ONACTIVITYRESULT




    // Callback for when a purchase is finished
     /*IabHelper.OnIabPurchaseFinishedListener iabPurchaseFinishedListener = new IabHelper.OnIabPurchaseFinishedListener() {
        @Override
        public void onIabPurchaseFinished(IabResult result, Purchase purchase) {
            // if we were disposed of in the meantime, quit.
            if (iabHelper == null) {
                return;
            }
            if (result.isFailure()) {
                return;
            }
            if (purchase.getSku().equals(SKU_ONE_MON_PRO_ID) ||
                    purchase.getSku().equals(SKU_THREE_MON_PRO_ID) ||
                    purchase.getSku().equals(SKU_ONE_YEAR_PRO_ID)) {
                iabHelper.consumeAsync(purchase, onConsumeFinishedListener);

            }
        }
    };*/

    private boolean verifyDeveloperPayload(Purchase p) {
        String payload = p.getDeveloperPayload();
        //Set Server code
        return true;
    }


/* @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (!iabHelper.handleActivityResult(requestCode, resultCode, data)) {
            super.onActivityResult(requestCode, resultCode, data);
            Log.e("--------------", "data" + data);


        } else {

        }
    }*/


    /**
     *  Check user type and Open the in app purchase for Ads and Three Minute Frequency Update
     *
     */
    /*private void limitedPurchase() {
            try{
				String payload = "";
				iabHelper.launchPurchaseFlow(this.getActivity(), SKU_LIMITED_KISS_PRO_ID,
						RC_REQUEST, iabPurchaseFinishedListener, payload);
			}catch(Exception e){
				e.printStackTrace();
			}
	}*/

    /**
     * Check user type and Open the in app purchase for Full purchase
     */
    private void oneMonthPurchase() {
        /*if (iabHelper != null) {
            iabHelper.flagEndAsync();
        }*/
        try {
            purchaseClicked = SKU_ONE_MON_PRO_ID;
            String payload = "";
            Log.e("SKU_ONE_MON_PRO_ID", ">>>>>" + SKU_ONE_MON_PRO_ID);
            iabHelper.launchPurchaseFlow(getActivity(), SKU_ONE_MON_PRO_ID,
                    RC_REQUEST, mPurchaseFinishedListener, payload);

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getActivity(), "Please retry in a few seconds.", Toast.LENGTH_SHORT).show();
        }

    }

    private void threeMonthPurchase() {
       /* if (iabHelper != null) {
            iabHelper.flagEndAsync();
        }*/
        try {
            purchaseClicked = SKU_THREE_MON_PRO_ID;
            String payload = "";
            Log.e("SKU_THREE_MON_PRO_ID", ">>>" + SKU_THREE_MON_PRO_ID);

            iabHelper.launchPurchaseFlow(getActivity(), SKU_THREE_MON_PRO_ID,
                    RC_REQUEST, mPurchaseFinishedListener, payload);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getActivity(), "Please retry in a few seconds.", Toast.LENGTH_SHORT).show();

        }
    }

    private void oneYearPurchase() {
       /* if (iabHelper != null) {
            iabHelper.flagEndAsync();
        }*/
        try {
            purchaseClicked = SKU_ONE_YEAR_PRO_ID;
            String payload = "";
            Log.e("SKU_ONE_YEAR_PRO_ID", ">>>>" + SKU_ONE_YEAR_PRO_ID);

            iabHelper.launchPurchaseFlow(getActivity(), SKU_ONE_YEAR_PRO_ID,
                    RC_REQUEST, mPurchaseFinishedListener, payload);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getActivity(), "Please retry in a few seconds.", Toast.LENGTH_SHORT).show();
        }
    }



    @Override
    public void onDestroy() {
        super.onDestroy();
        if (iabHelper != null) {
            iabHelper.dispose();
            iabHelper = null;
        }
    }

    @Override
    public void startpurchase(int status) {
        if (status == 0) {
            oneMonthPurchase();
        }
        if (status == 1) {
            threeMonthPurchase();
        }
        if (status == 2) {
            oneYearPurchase();
        }
    }

    @Override
    public void onResponseReceived(String response, String methodName) {

        try {
            Log.e("RESPONSE","--->>>"+response);
            String statusCode = "0";
            if (response != null) {
                JSONObject jObjServerResp;
                if (methodName.equals( AppConstants.UPDATE_ACTIVE_CALL) ){
                    jObjServerResp = new JSONObject(response);
                    statusCode = jObjServerResp.getString(JsonConstants.STATUS);
                    if (statusCode.equalsIgnoreCase("250")) {
                        Utility.showToast(getActivity(), "Purchased successfully");
                    } else {
                        Utility.showToast(getActivity(), "No response from server");
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

   /* @Override
    public void onButtonClickListner(int position) {
        Toast.makeText(getActivity(), "test buy value" + position, Toast.LENGTH_SHORT).show();
    }*/

    /*private void fullPurchaseOneMonth() {

        if (purchaseStatus.equalsIgnoreCase("3") && duration.equalsIgnoreCase("OneMonth")) {
            Utility.showToast(getActivity(),"Already Purchased");
            startAlarmService(selectposi);
        } else {
            String payload = "";
            iabHelper.launchPurchaseFlow(getActivity(), SKU_ONE_MIN_UPT_FRQ_PRO_ID_ONE_MONTH,
                    RC_REQUEST, iabPurchaseFinishedListener, payload);
          *//*  iabHelper.launchPurchaseFlow(getActivity(), SKU_ONE_MIN_UPT_FRQ_PRO_ID_ONE_MONTH,
                    RC_REQUEST, iabPurchaseFinishedListener, payload);*//*

        }
    }

    private void fullPurchaseThreeMonth() {
        if (purchaseStatus.equalsIgnoreCase("3") && duration.equalsIgnoreCase("ThreeMonth")) {
            Utility.showToast(getActivity(),"Already Purchased");
            startAlarmService(selectposi);
        } else {
            String payload = "";
            iabHelper.launchPurchaseFlow(getActivity(), SKU_ONE_MIN_UPT_FRQ_PRO_ID_THREE_MONTH,
                    RC_REQUEST, iabPurchaseFinishedListener, payload);
       *//*     iabHelper.launchPurchaseFlow(getActivity(), SKU_ONE_MIN_UPT_FRQ_PRO_ID_THREE_MONTH,
                    RC_REQUEST, iabPurchaseFinishedListener, payload);*//*

        }
    }

    private void fullPurchaseOneYear() {
        if (purchaseStatus.equalsIgnoreCase("3") && duration.equalsIgnoreCase("OneYear")) {
            Utility.showToast(getActivity(),"Already Purchased");
            startAlarmService(selectposi);
        } else {
            String payload = "";
            iabHelper.launchPurchaseFlow(getActivity(), SKU_ONE_MIN_UPT_FRQ_PRO_ID_ONE_YEAR,
                    RC_REQUEST, iabPurchaseFinishedListener, payload);

        }
    }

    // Listener that's called when we finish querying the items and subscriptions we own
    IabHelper.QueryInventoryFinishedListener queryInventoryFinishedListener = new IabHelper.QueryInventoryFinishedListener() {
        @Override
        public void onQueryInventoryFinished(IabResult result, Inventory inventory) {
            if (iabHelper == null) {
                return;
            }
            if (result.isFailure()) {
                return;
            }
            Purchase adsPurchase = inventory.getPurchase(SKU_ADS_THREE_MIN_PRO_ID);
            if ((adsPurchase != null) && (verifyDeveloperPayload(adsPurchase))) {
                iabHelper.consumeAsync(adsPurchase,
                        onConsumeFinishedListener);
                return;
            }
            Purchase adsUpdateFrq = inventory.getPurchase(SKU_ONE_MIN_UPT_FRQ_PRO_ID);
            if ((adsUpdateFrq != null) && (verifyDeveloperPayload(adsUpdateFrq))) {
                iabHelper.consumeAsync(adsUpdateFrq, onConsumeFinishedListener);
                return;
            }
        }
    };

    // Called when consumption is complete
    IabHelper.OnConsumeFinishedListener onConsumeFinishedListener = new IabHelper.OnConsumeFinishedListener() {
        @Override
        public void onConsumeFinished(Purchase purchase, IabResult result) {
            if (iabHelper == null) {
                return;
            }
            if (result.isSuccess()) {
                // Call service
                if (purchase.getSku().equals(SKU_ADS_THREE_MIN_PRO_ID)) {
                    Utility.showToast(getActivity(),"You have purchased for removing ads from your app.");
                    //Call Web Service
                    startUpdatePurchaseStatus();
                } else if (purchase.getSku().equals(SKU_ONE_MIN_UPT_FRQ_PRO_ID)) {
                    Utility.showToast(getActivity(),"You have purchased for removing ads and enable Update frequency from your app");
                    //Call Web Service
                    startUpdatePurchaseStatus();
                }
            } else {
            }
        }
    };

    // Callback for when a purchase is finished
    IabHelper.OnIabPurchaseFinishedListener iabPurchaseFinishedListener = new IabHelper.OnIabPurchaseFinishedListener() {
        @Override
        public void onIabPurchaseFinished(IabResult result, Purchase purchase) {
            // if we were disposed of in the meantime, quit.
            if (iabHelper == null){
                return;
            }
            if (result.isFailure()) {
                return;
            }
           *//* if(purchase.getSku().equals(SKU_ADS_THREE_MIN_PRO_ID) || purchase.getSku().equals(SKU_ONE_MIN_UPT_FRQ_PRO_ID)){
                iabHelper.consumeAsync(purchase, onConsumeFinishedListener);
            }*//*
        }
    };



    *//**
     * Verifies the developer payload of a purchase.
     *//*
    @SuppressWarnings("unused")
    private boolean verifyDeveloperPayload(Purchase p) {
        String payload = p.getDeveloperPayload();
        //Set Server code
        return true;
    }*/


}
