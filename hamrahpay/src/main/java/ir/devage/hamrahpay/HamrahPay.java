/**
 * HamrahPay Android Library...
 * In App Purchase Service
 * www.Hamrahpay.com
 */
package ir.devage.hamrahpay;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.http.SslError;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Pay Class
 */
public class HamrahPay {

    // PHP Pages On Server Side
    private static final String PAY_REQUEST_PAGE = "https://hamrahpay.com/rest-api/pay-request";
    private static final String VERIFY_PAYMENT_PAGE = "https://hamrahpay.com/rest-api/verify-payment";

    // Parameter TAGs To Send Pages
    private static final String ERROR_TAG = "error";
    private static final String STATUS_TAG = "status";
    private static final String MESSAGE_TAG = "message";
    private static final String SKU_TAG = "sku";
    private static final String DEVICE_ID_TAG = "device_id";
    private static final String EMAIL_TAG = "email";
    private static final String PAY_CODE_TAG = "pay_code";
    private static final String VERIFICATION_TYPE_TAG = "verification_type";
    private static final String DEVICE_MODEL_TAG = "device_model";
    private static final String DEVICE_MANUFACTURER_TAG = "device_manufacturer";
    private static final String SDK_VERSION_TAG = "sdk_version";
    private static final String ANDROID_VERSION_TAG = "android_version";

    // Statuses Of JSON Response From Server
    public static final String STATUS_SUCCESSFUL_PAYMENT = "SUCCESSFUL_PAYMENT";
    public static final String STATUS_NO_NETWORK_OR_SERVER = "NO_NETWORK_OR_SERVER";
    public static final String STATUS_READY_TO_PAY = "READY_TO_PAY";
    public static final String STATUS_BEFORE_PAID = "BEFORE_PAID";
    public static final String STATUS_SELLER_BLOCKED = "SELLER_BLOCKED";
    public static final String STATUS_TRY_AGAIN = "TRY_AGAIN";
    public static final String STATUS_BAD_PARAMETERS = "BEFORE_PAID";
    public static final String STATUS_INVALID_TRANSACTION = "INVALID_TRANSACTION";

    // Verification Types That User Can Set
    public static final String DEVICE_VERIFICATION = "device_verification";
    public static final String EMAIL_VERIFICATION = "email_verification";

    private String sku = null;
    private Context context;
    private String verificationType = EMAIL_VERIFICATION;
    private RequestQueue mRequestQueue = null;
    private Listener listener = null;
    private String payCode = null;
    private int color = Color.parseColor("#F44336");
    private int titleColor = Color.WHITE;

    /**
     * Constructor For Passing Context
     * @param context, Context
     */
    public HamrahPay(Context context) {
        this.context = context;
    }

    /**
     * A Listener To Notify Developer From Success And Errors
     */
    public interface Listener {
        void onErrorOccurred(String status, String message);
        void onPaymentSucceed(String payCode);
    }

    /**
     * @return PayCode
     */
    public String getPayCode() {
        return payCode;
    }

    /**
     * @return Developer Given Verification Type <br>
     *  <b>DEAFULT: </b> EMAIL_VERIFICATION
     */
    public String getVerificationType() {
        return verificationType;
    }

    /**
     * @return ActionBar Color Of Pay Page
     */
    public int getPageTopColor() {
        return color;
    }

    /**
     * @return ActionBar Title Color Of Pay Page
     */
    public int getPageTitleColor() {
        return titleColor;
    }

    /**
     * @return Product SKU Code
     */
    public String getSku(){
        return sku;
    }

    /**
     * Sets Listener For Notifying Developer From Errors And Success Status
     * @param listener, Listener For Notify Developer
     * @return Class Instance
     */
    public HamrahPay listener(Listener listener) {
        this.listener = listener;
        return this;
    }

    /**
     * Sets A Request Queue For Adding Request To That<br>
     * <b>If</b> You Dont Have A Request Queue Dont Use This Method And Library Automatically Handles This<br>
     * <b>If</b> You Have A Request Queue Created By Volley Class You Can Set It With This Method<br>
     * @param requestQueue, Request Queue To Add Requests
     * @return Class Instance
     */
    public HamrahPay requestQueue(RequestQueue requestQueue) {
        this.mRequestQueue = requestQueue;
        return this;
    }

    /**
     * Sets Developer's Product SKU Code<br>
     * <b>Necessary For Starting Payment</b>
     * @param sku, SKU Code Of The Product
     * @return Class Instance
     */
    public HamrahPay sku(String sku) {
        this.sku = sku;
        return this;
    }

    /**
     * Sets Verification Type Of Payment <br>
     * Two Methods Can Be Applied <br>
     * <b>1: </b>HamrahPay.EMAIL_VERIFICATION<br>
     * <b>2: </b>HamrahPay.DEVICE_VERIFICATION<br>
     * <b>DEFAULT: </b>HamrahPay.EMAIL_VERIFICATION
     * @param verificationType, Verification Type Of Payment
     * @return Class Instance
     */
    public HamrahPay verificationType(String verificationType) {
        this.verificationType = verificationType;
        return this;
    }

    /**
     * Sets Pay Activity's ActionBar Title Color<br>
     * <b>DEFAULT: </b>Color.WHITE
     * @param titleColor, ActionBar Title Color
     * @return Class Instance
     */
    public HamrahPay pageTitleColor(int titleColor) {
        this.titleColor = titleColor;
        return this;
    }

    /**
     * Sets Pay Activity's ActionBar Color<br>
     * <b>DEFAULT: </b>Material Design Red500 Color = #F44336
     * @param color, ActionBar Color
     * @return Class Instance
     */
    public HamrahPay pageTopColor(int color) {
        this.color = color;
        return this;
    }

    /**
     * Starts Payment<br>
     * Use This Method After Setting Listener And SKU
     */
    public void startPayment() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(context);
        }
        // Sending Request Via POST Method To pay_request Page
        StringRequest request = new StringRequest(Request.Method.POST, PAY_REQUEST_PAGE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            String status = jsonResponse.getString(STATUS_TAG);
                            if (!jsonResponse.getBoolean(ERROR_TAG)) {
                                payCode = jsonResponse.getString(PAY_CODE_TAG);
                                switch (status) {
                                    case STATUS_READY_TO_PAY:
                                        // Go To Pay Activity
                                        Intent payIntent = new Intent(context, PayActivity.class);
                                        PayActivity.setClass(HamrahPay.this);
                                        context.startActivity(payIntent);
                                        break;
                                    case STATUS_BEFORE_PAID:
                                        // User Paid This Product Before, Check And Verify
                                        verifyPayment(getSku(), getPayCode());
                                        break;
                                }
                            } else {
                                // Error Occurred
                                String message = jsonResponse.getString(MESSAGE_TAG);
                                if (listener != null) {
                                    listener.onErrorOccurred(status, message);
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Error Occurred
                String status = STATUS_NO_NETWORK_OR_SERVER;
                String message = handleVolleyError(error);
                if (listener != null) {
                    listener.onErrorOccurred(status, message);
                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put(SKU_TAG, sku);
                params.put(DEVICE_ID_TAG, getDeviceID(context));
                params.put(EMAIL_TAG, getPrimaryEmailAddress());
                return params;
            }
        };
        mRequestQueue.add(request);
    }

    /**
     * Verify Payment<br>
     * This Is A Private Method So Just Library Can Use This Method
     * @param sku, Product SKU Code
     * @param payCode, PayCode
     */
    private void verifyPayment(final String sku, final String payCode) {
        // Sending Request Via POST Method To verify_payment Page
        StringRequest request = new StringRequest(Request.Method.POST, VERIFY_PAYMENT_PAGE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            String status = jsonResponse.getString(STATUS_TAG);
                            if (!jsonResponse.getBoolean(ERROR_TAG)) {
                                if (status.equals(STATUS_SUCCESSFUL_PAYMENT)) {
                                    // Payment Was Successful
                                    makePremium(sku);
                                    if (listener != null) {
                                        listener.onPaymentSucceed(payCode);
                                    }
                                }
                            } else {
                                // Error Occurred
                                String message = jsonResponse.getString(MESSAGE_TAG);
                                if (listener != null) {
                                    listener.onErrorOccurred(status, message);
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Error Occurred
                String status = STATUS_NO_NETWORK_OR_SERVER;
                String message = handleVolleyError(error);
                if (listener != null) {
                    listener.onErrorOccurred(status, message);
                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put(SKU_TAG, sku);
                params.put(PAY_CODE_TAG, payCode);
                params.put(VERIFICATION_TYPE_TAG, getVerificationType());
                params.put(EMAIL_TAG, getPrimaryEmailAddress());
                params.put(DEVICE_ID_TAG, getDeviceID(context));
                params.put(DEVICE_MODEL_TAG, Build.MODEL);
                params.put(DEVICE_MANUFACTURER_TAG, Build.MANUFACTURER);
                params.put(SDK_VERSION_TAG, Integer.toString(Build.VERSION.SDK_INT));
                params.put(ANDROID_VERSION_TAG, Build.VERSION.RELEASE);
                return params;
            }
        };
        mRequestQueue.add(request);
    }

    /**
     * Don't Forget To Set This Permissions:<br>
     * "android.permission.READ_PHONE_STATE"<br>
     * "android.permission.ACCESS_WIFI_STATE"<br>
     * In AndroidManifest.xml
     * @return Device Unique ID
     */
    private static String getDeviceID(Context context) {
        String deviceId;

        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        deviceId = telephonyManager.getDeviceId();

        if (deviceId == null) {
            WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            deviceId = wifiManager.getConnectionInfo().getMacAddress();

            if (deviceId == null) {
                deviceId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
            }
        }

        if ("9774d56d682e549c".equals(deviceId) || deviceId == null) {
            deviceId = ((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
        }

        return (deviceId != null) ? deviceId : "DEVICE_ID_ERROR";
    }

    /**
     * Don't Forget To Set This Permission:<br>
     * "android.permission.GET_ACCOUNTS"<br>
     * In AndroidManifest.xml
     * @return User Primary Email Address
     */
    private String getPrimaryEmailAddress() {
        Pattern emailPattern = Patterns.EMAIL_ADDRESS;
        String possibleEmail = null;
        Account[] accounts = AccountManager.get(context).getAccounts();
        for (Account account : accounts) {
            if (emailPattern.matcher(account.name).matches()) {
                possibleEmail = account.name;
                break;
            }
        }
        return possibleEmail;
    }

    /**
     * Adds A String To Pref<br>
     * Ex: premium_key_YOUR_SKU="User's Device ID"
     * @param sku, Your SKU
     */
    private void makePremium(String sku) {
        SharedPreferences prefs = context.getSharedPreferences("hp_premium", Context.MODE_PRIVATE);
        prefs.edit().putString("premium_key_"+sku,getDeviceID(context)).apply();
    }

    /**
     * An Static Method To Be Sure Of Premium State Of Selected SKU
     * @param context, Context
     * @param sku, SKU
     * @return True If User Already Paid Or Paid Now
     */
    public static boolean isPremium(Context context,String sku) {
        SharedPreferences prefs = context.getSharedPreferences("hp_premium", Context.MODE_PRIVATE);
        String status = prefs.getString("premium_key_" + sku, "NOT_SET");
        return (!status.equals("NOT_SET")) && status.equals(getDeviceID(context));
    }

    /**
     * Handles Errors Comming From Volley
     * @param error, Volley Error
     * @return Message For Volley Errors
     */
    private static String handleVolleyError(VolleyError error) {
        String volleyError = null;
        if (error instanceof NoConnectionError ||
                error instanceof TimeoutError ||
                error instanceof NetworkError) {
            volleyError = "ارتباط شما با اینترنت قطع میباشد. بعد از برقراری ارتباط مجددا تلاش نمایید.";
        } else if (error instanceof ServerError ||
                error instanceof ParseError ||
                error instanceof AuthFailureError) {
            volleyError = "خطا در برقراری ارتباط با سرور";
        }
        return volleyError;
    }


    //==================================================================
    //                   Pay Activity As Inner Class                  //
    //==================================================================
    public static class PayActivity extends AppCompatActivity {

        // Pay Page
        String PAY_PAGE = "https://hamrahpay.com/cart/app/pay_v2/";

        ProgressDialog progress;
        WebView wbvwBrowser;
        TextView urlBar;
        static HamrahPay hamrahPay;

        //  Gets Properties From Outer Class
        private static void setClass(HamrahPay createdHamarahPay) {
            hamrahPay = createdHamarahPay;
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_pay);

            // Setting Toolbar
            Toolbar toolbar = (Toolbar) findViewById(R.id.tlbr_acPay);
            toolbar.setBackgroundColor(hamrahPay.getPageTopColor());
            toolbar.setTitleTextColor(hamrahPay.getPageTitleColor());
            setSupportActionBar(toolbar);
            if (getSupportActionBar() != null) {
                getSupportActionBar().setTitle(R.string.pay);
                getSupportActionBar().setHomeButtonEnabled(true);
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            }

            // Creating Progress
            progress = new ProgressDialog(this);
            progress.setMessage(getResources().getString(R.string.pleaseWait));
            progress.setIndeterminate(true);
            progress.setCancelable(false);


            // Initializing Url View
            urlBar = (TextView) findViewById(R.id.txtv_acPay);
            urlBar.setBackgroundColor(hamrahPay.getPageTopColor());
            urlBar.setTextColor(hamrahPay.getPageTitleColor());


            // Injecting And Initializing WebView
            String payURL = PAY_PAGE + hamrahPay.getPayCode();
            wbvwBrowser = (WebView) findViewById(R.id.wbvw_acPay);
            wbvwBrowser.clearCache(true);
            startWebView(payURL);

        }

        @SuppressLint("SetJavaScriptEnabled")
        private void startWebView(final String payURL) {
            wbvwBrowser.setWebViewClient(new WebViewClient(){

                @Override
                public void onPageStarted(WebView view, String url, Bitmap favicon) {
                    progress.show();
                    super.onPageStarted(view,url,favicon);
                }

                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    if (url.contains("exit_page")) {
                        onBackPressed();
                        return true;
                    }
                    view.loadUrl(url);
                    urlBar.setText(url);
                    return super.shouldOverrideUrlLoading(view, url);
                }

                @TargetApi(Build.VERSION_CODES.M)
                @Override
                public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                    if (error.getErrorCode() == -2) {
                        String errorMessage = "<html><head><meta charset=\"utf-8\" /><style>body{font-family:tahoma;font-size:13px;directin:rtl;text-align:right}</style></head><body><div style=\"color: #a94442;background-color: #f2dede;border-color: #ebccd1;margin:5px; padding:8px\">متاسفانه اشکالی در ارتباط با بانک به وجود آمده است . لطفا دقایقی دیگر مجددا تلاش بفرمایید.</div></body></html>";
                        view.loadData(errorMessage, "text/html", "utf-8");
                        return;
                    }
                    super.onReceivedError(view, request, error);
                }

                @SuppressWarnings("deprecation")
                @Override
                public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                    if (errorCode == -2) {
                        String errorMessage = "<html><head><meta charset=\"utf-8\" /><style>body{font-family:tahoma;font-size:13px;directin:rtl;text-align:right}</style></head><body><div style=\"color: #a94442;background-color: #f2dede;border-color: #ebccd1;margin:5px; padding:8px\">متاسفانه اشکالی در ارتباط با بانک به وجود آمده است . لطفا دقایقی دیگر مجددا تلاش بفرمایید.</div></body></html>";
                        view.loadData(errorMessage, "text/html", "utf-8");
                        return;
                    }
                    super.onReceivedError(view, errorCode, description, failingUrl);
                }

                @Override
                public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                    handler.proceed();
                }

                @Override
                public void onPageFinished(WebView view, String url) {
                    if (progress.isShowing()) {
                        progress.dismiss();
                    }
                    urlBar.setText(url);
                    super.onPageFinished(view, url);
                    wbvwBrowser.setVisibility(View.GONE);
                    wbvwBrowser.setVisibility(View.VISIBLE);
                }
            });

            // Do not disable this line , because it used when user will redirect to bank
            wbvwBrowser.getSettings().setDomStorageEnabled(true);
            wbvwBrowser.getSettings().setJavaScriptEnabled(true);
            wbvwBrowser.getSettings().setDefaultTextEncodingName("utf-8");
            wbvwBrowser.loadUrl(payURL);
        }

        @Override
        protected void onDestroy() {
            // Check If User Paid Product And Notify Developer
            hamrahPay.verifyPayment(hamrahPay.getSku(),hamrahPay.getPayCode());
            super.onDestroy();
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            int id = item.getItemId();
            switch (id) {
                case android.R.id.home:
                    finish();
                    break;
            }
            return super.onOptionsItemSelected(item);
        }
    }
}
