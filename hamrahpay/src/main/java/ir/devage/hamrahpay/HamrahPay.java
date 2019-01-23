package ir.devage.hamrahpay;

/**
 * Created by Devage on 7/1/2017.
 */


import android.Manifest;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;


import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.util.Patterns;

import java.util.List;
import java.util.regex.Pattern;

import android.widget.Toast;

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

import com.google.gson.Gson;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.util.ArrayList;

import static android.app.ProgressDialog.STYLE_HORIZONTAL;


public class HamrahPay {
    private static final String PAY_REQUEST_PAGE = "https://hamrahpay.com/rest-api/pay-request";
    private static final String VERIFY_PAYMENT_PAGE = "https://hamrahpay.com/rest-api/verify-payment";
    private static final String GET_TRANSACTION_INFO_PAGE = "https://hamrahpay.com/rest-api/trans-info";
    private static final String GET_SUPPORT_INFO_PAGE = "https://hamrahpay.com/rest-api/support-info";
    private static final String ERROR_TAG = "error";
    private static final String STATUS_TAG = "status";
    private static final String MESSAGE_TAG = "message";
    private static final String SKU_TAG = "sku";
    private static final String DEVICE_ID_TAG = "device_id";
    private static final String APPLICATION_PACKAGE_ID = "package_id";
    private static final String EMAIL_TAG = "email";
    private static final String PAY_CODE_TAG = "pay_code";
    private static final String VERIFICATION_TYPE_TAG = "verification_type";
    private static final String DEVICE_MODEL_TAG = "device_model";
    private static final String DEVICE_MANUFACTURER_TAG = "device_manufacturer";
    private static final String SDK_VERSION_TAG = "sdk_version";
    private static final String ANDROID_VERSION_TAG = "android_version";
    private static final String LIBRARY_VERSION = "library_version";
    private static final String LIBRARY_NAME    = "library_name";
    // Statuses Of JSON Response From Server
    public static final String STATUS_SUCCESSFUL_PAYMENT = "SUCCESSFUL_PAYMENT";
    public static final String STATUS_NO_NETWORK_OR_SERVER = "NO_NETWORK_OR_SERVER";
    public static final String STATUS_READY_TO_PAY = "READY_TO_PAY";
    public static final String STATUS_BEFORE_PAID = "BEFORE_PAID";

    // Verification Types That User Can Set
    public static final String DEVICE_VERIFICATION = "device_verification";
    public static final String EMAIL_VERIFICATION = "email_verification";

    private String sku = null;
    private Context context;
    private String verificationType = DEVICE_VERIFICATION;
    private RequestQueue mRequestQueue = null;
    private Listener listener = null;
    private String payCode = null;
    private String emailAddress = null;
    private int color = Color.parseColor("#2ecc71");
    private int titleColor = Color.WHITE;
    private String DeviceID = null;
    private static String realDeviceID = null;
    private static Handler PermissionHandler = null;

    private static boolean showHamrahpayDialog = true;
    private static boolean isChromeCustomeTabDisabled = false;
    private String productType = null;
    LastPurchase lastPurchase = null;
    SupportInfo supportInfo = null;
    private Gson gson;

    private ProgressDialog progressDialog;

    /**
     * Constructor For Passing Context
     *
     * @param context, Context
     */
    public HamrahPay(Context context) {
        this.context = context;
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(context);
        }
        gson = new Gson();
    }


    public static boolean shouldShowHamrahpayDialog() {
        return showHamrahpayDialog;
    }

    public HamrahPay setShouldShowHamrahpayDialog(boolean showHamrahpayDialog) {
        HamrahPay.showHamrahpayDialog = showHamrahpayDialog;
        return this;
    }


    public HamrahPay setCustomDeviceID(String did) {
        DeviceID = did;
        return this;
    }

    public HamrahPay setProductType(String pType) {
        this.productType = pType;
        return this;
    }

    public HamrahPay setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
        return this;
    }

    /**
     * Sets A Request Queue For Adding Request To That<br>
     * <b>If</b> You Dont Have A Request Queue Dont Use This Method And Library Automatically Handles This<br>
     * <b>If</b> You Have A Request Queue Created By Volley Class You Can Set It With This Method<br>
     *
     * @param requestQueue, Request Queue To Add Requests
     * @return Class Instance
     */
    public HamrahPay requestQueue(RequestQueue requestQueue) {
        this.mRequestQueue = requestQueue;
        return this;
    }

    public HamrahPay disableChromeCustomeTab() {
        this.isChromeCustomeTabDisabled = true;
        return this;
    }

    public HamrahPay enableChromeCustomeTab() {
        this.isChromeCustomeTabDisabled = false;
        return this;
    }

    public String getProductType() {
        return productType;
    }

    /**
     * A Listener To Notify Developer From Success And Errors
     */
    public interface Listener {
        void onErrorOccurred(String status, String message);

        void onPaymentSucceed(String payCode);

        void onGetLastPurchaseInfo(LastPurchase lastPurchase);

        void onGetSupportInfo(SupportInfo supportInfo);

        void onGetDeviceID(String deviceID);
    }

    /**
     * @return PayCode
     */
    public String getPayCode() {
        return payCode;
    }


    /**
     * @return Developer Given Verification Type <br>
     * <b>DEAFULT: </b> EMAIL_VERIFICATION
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
    public String getSku() {
        return sku;
    }

    /**
     * Sets Listener For Notifying Developer From Errors And Success Status
     *
     * @param listener, Listener For Notify Developer
     * @return Class Instance
     */
    public HamrahPay listener(Listener listener) {
        this.listener = listener;
        return this;
    }

    /**
     * Sets Developer's Product SKU Code<br>
     * <b>Necessary For Starting Payment</b>
     *
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
     *
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
     *
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
     *
     * @param color, ActionBar Color
     * @return Class Instance
     */
    public HamrahPay pageTopColor(int color) {
        this.color = color;
        return this;
    }

    public void showDeviceID(final Context context) {
        String did = (DeviceID != null) ? DeviceID : getDeviceID(context);

        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(context, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(context);
        }
        builder.setTitle("کد دستگاه")
                .setMessage("کد دستگاه شما : " + "\n" + did)
                .setPositiveButton("یادداشت کردم", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete
                    }
                })

                .setIcon(android.R.drawable.ic_dialog_info)
                .show();
    }

    /**
     * Don't Forget To Set This Permissions:<br>
     * "android.permission.READ_PHONE_STATE"<br>
     * "android.permission.ACCESS_WIFI_STATE"<br>
     * In AndroidManifest.xml
     *
     * @return Device Unique ID
     */

    @SuppressLint("MissingPermission")
    public static String getDeviceID(final Context context) {

        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

        realDeviceID = telephonyManager.getDeviceId();
        if (realDeviceID == null) {
            WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            realDeviceID = wifiManager.getConnectionInfo().getMacAddress();

            if (realDeviceID == null) {
                realDeviceID = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
            }
        }
        if ("9774d56d682e549c".equals(realDeviceID) || realDeviceID == null) {
            realDeviceID = ((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
        }
        realDeviceID = (realDeviceID != null) ? realDeviceID : "DEVICE_ID_ERROR";
        return ((realDeviceID.equals("0") || realDeviceID.isEmpty() || realDeviceID == null || realDeviceID.equals("DEVICE_ID_ERROR")) ? "DEVICE_ID_ERROR" : realDeviceID);


        //Log.e("device id method :",realDeviceID);


    }

    public void InitializePayment(final Context dialogContext) {

        PermissionListener permissionlistener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                final String deviceID = getDeviceID(context);
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
                        //Log.e("DEVICE ID ", deviceID);
                        String email = null;
                        email = getEmailAddress();
                        if (email != null)
                            params.put(EMAIL_TAG, email);

                        params.put(SKU_TAG, sku);
                        params.put(DEVICE_ID_TAG, deviceID);
                        params.put(VERIFICATION_TYPE_TAG, getVerificationType());

                        try {

                            params.put(APPLICATION_PACKAGE_ID, dialogContext.getPackageName());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        //
                        return params;
                    }
                };

                final ProgressDialog dialog = ProgressDialog.show(dialogContext, "",
                        "در حال دریافت اطلاعات...", true);
                dialog.setCancelable(false);

                mRequestQueue.addRequestFinishedListener(new RequestQueue.RequestFinishedListener<String>() {
                    @Override
                    public void onRequestFinished(Request<String> request) {
                        dialog.dismiss();
                    }
                });

                if (getDeviceID(dialogContext).equals("DEVICE_ID_ERROR"))
                    Toast.makeText(context, "لطفا سطح دسترسی لازم را به برنامه بدهید و مجددا اقدام به پرداخت نمایید.", Toast.LENGTH_LONG).show();
                else
                    mRequestQueue.add(request);

            }

            @Override
            public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                realDeviceID = "DEVICE_ID_ERROR";
                Toast.makeText(context, "لطفا سطح دسترسی لازم را به برنامه بدهید.", Toast.LENGTH_LONG).show();
            }

        };

        new TedPermission(context)
                .setPermissionListener(permissionlistener)
                .setRationaleConfirmText("لطفا سطح دسترسی لازم را برای ادامه به برنامه بدهید.")
                .setDeniedMessage("لطفا سطح دسترسی لازم را برای ادامه به برنامه بدهید.")
                .setGotoSettingButton(true)
                .setGotoSettingButtonText("مراجعه به تنظیمات")
                .setPermissions(Manifest.permission.READ_PHONE_STATE)
                .check();


    }

    /**
     * Handles Errors Comming From Volley
     *
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

    public void startPayment() {


        if (shouldShowHamrahpayDialog()) {
            Intent myIntent = new Intent(context, HamrahpayHelpDialogActivity.class);
            HamrahpayHelpDialogActivity.setClass(HamrahPay.this);
            context.startActivity(myIntent);
        } else {
            InitializePayment(context);
        }

    }

    private String getEmailAddress() {
        return this.emailAddress;
    }

    private void makePremium(String sku) {
        SharedPreferences prefs = context.getSharedPreferences("hp_premium", Context.MODE_PRIVATE);
        prefs.edit().putString("premium_key_" + sku, getDeviceID(context)).apply();
    }

    public static boolean isPremium(Context context, String sku) {
        SharedPreferences prefs = context.getSharedPreferences("hp_premium", Context.MODE_PRIVATE);
        String status = prefs.getString("premium_key_" + sku, "NOT_SET");
        return (!status.equals("NOT_SET")) && status.equals(getDeviceID(context));
    }

    public static void addScore(String sku, int value, Context context) {
        SharedPreferences prefs = context.getSharedPreferences("hp_consume", Context.MODE_PRIVATE);
        int score = prefs.getInt("consume_key_" + sku + "_" + getDeviceID(context), 0);
        prefs.edit().putInt("consume_key_" + sku + "_" + getDeviceID(context), score + value).apply();
    }

    public static void minusScore(String sku, int value, Context context) {
        SharedPreferences prefs = context.getSharedPreferences("hp_consume", Context.MODE_PRIVATE);
        int score = prefs.getInt("consume_key_" + sku + "_" + getDeviceID(context), 0);
        prefs.edit().putInt("consume_key_" + sku + "_" + getDeviceID(context), score - value).apply();
    }

    public static int getScore(String sku, Context context) {
        SharedPreferences prefs = context.getSharedPreferences("hp_consume", Context.MODE_PRIVATE);
        int score = prefs.getInt("consume_key_" + sku + "_" + getDeviceID(context), 0);
        return score;
    }


    public void getLastPurchase() {

        StringRequest request = new StringRequest(Request.Method.POST, GET_TRANSACTION_INFO_PAGE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        lastPurchase = gson.fromJson(response, LastPurchase.class);
                        listener.onGetLastPurchaseInfo(lastPurchase);
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
                String did = (DeviceID != null) ? DeviceID : getDeviceID(context);
                Map<String, String> params = new HashMap<>();
                params.put(SKU_TAG, getSku());
                params.put(DEVICE_ID_TAG, did);
                return params;
            }
        };
        mRequestQueue.add(request);
    }


    public void getSupportInfo() {
        StringRequest request = new StringRequest(Request.Method.POST, GET_SUPPORT_INFO_PAGE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        supportInfo = gson.fromJson(response.toString(), SupportInfo.class);

                        listener.onGetSupportInfo(supportInfo);
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
                String did = (DeviceID != null) ? DeviceID : getDeviceID(context);
                Map<String, String> params = new HashMap<>();
                params.put(SKU_TAG, getSku());
                return params;
            }
        };
        mRequestQueue.add(request);
    }

    private void verifyPayment(final String sku, final String payCode) {

        // Sending Request Via POST Method To verify_payment Page
        final String did = (DeviceID != null) ? DeviceID : getDeviceID(context);

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

                String email = null;
                email = getEmailAddress();
                if (email != null)
                    params.put(EMAIL_TAG, email);

                params.put(SKU_TAG, sku);
                params.put(PAY_CODE_TAG, payCode);
                params.put(VERIFICATION_TYPE_TAG, getVerificationType());
                params.put(DEVICE_ID_TAG, did);
                params.put(DEVICE_MODEL_TAG, Build.MODEL);
                params.put(DEVICE_MANUFACTURER_TAG, Build.MANUFACTURER);
                params.put(SDK_VERSION_TAG, Integer.toString(Build.VERSION.SDK_INT));
                params.put(ANDROID_VERSION_TAG, Build.VERSION.RELEASE);
                params.put(LIBRARY_VERSION, "3");
                params.put(LIBRARY_NAME,"ANDROID_STUDIO_JAVA");
                return params;
            }
        };
        mRequestQueue.add(request);
    }

    //==================================================================
    //               Dialog
    //==================================================================
    public static class HamrahpayDialogActivity extends Activity {


        ProgressDialog progressDialog = null;
        private static HamrahPay hamrahPay;

        private static void setClass(HamrahPay createdHamarahPay) {
            hamrahPay = createdHamarahPay;
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            setTitle(R.string.hamrahpay_title);

            String dialogType = getIntent().getStringExtra("dialog_type");
            if (dialogType.equals("help")) {
                setContentView(R.layout.hamrahpay_help);
                WebView hp_webview = (WebView) findViewById(R.id.hp_webview);
                hp_webview.getSettings().setJavaScriptEnabled(true);
                hp_webview.loadUrl("https://hamrahpay.com/pages/83");
                hp_webview.setWebViewClient(new MyWebViewClient());
                progressDialog = new ProgressDialog(this);

            } else {
                setContentView(R.layout.hamrahpay_support);
                Button hp_faq_btn = (Button) findViewById(R.id.hp_faq_btn);
                hp_faq_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.hamrahpay_faq)));
                        startActivity(intent);
                    }
                });

                final SupportInfo supportInfo;

                final TextView email_address, phone, mobile, telegram;
                email_address = (TextView) findViewById(R.id.hp_email_address);
                phone = (TextView) findViewById(R.id.hp_phone_number);
                mobile = (TextView) findViewById(R.id.hp_mobile_number);
                telegram = (TextView) findViewById(R.id.hp_telegram_id);

                hamrahPay.listener(new HamrahPay.Listener() {        // لیسنر برای آگاهی شما از موفق بودن یا نبودن پرداخت
                    @Override
                    public void onErrorOccurred(String status, String message) {
                    }

                    @Override
                    public void onPaymentSucceed(String payCode) {
                        // کاربر با موفقیت پرداخت را انجام داده است
                        //Log.i("HamrahPay", "payCode: " + payCode);
                    }

                    @Override
                    public void onGetLastPurchaseInfo(LastPurchase lastPurchase) {

                    }

                    @Override
                    public void onGetSupportInfo(final SupportInfo supportInfo) {
                        email_address.setText(supportInfo.getEmail());
                        phone.setText(supportInfo.getPhone());
                        mobile.setText(supportInfo.getMobile());
                        telegram.setText(supportInfo.getTelegram());

                        telegram.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (supportInfo.getTelegram().startsWith("http")) {
                                    Intent telegram = new Intent(Intent.ACTION_VIEW, Uri.parse(supportInfo.getTelegram()));
                                    startActivity(telegram);
                                } else {
                                    Intent telegram = new Intent(Intent.ACTION_VIEW, Uri.parse("https://telegram.me/" + supportInfo.getTelegram()));
                                    startActivity(telegram);
                                }
                            }
                        });
                    }

                    @Override
                    public void onGetDeviceID(String deviceID) {

                    }
                }).getSupportInfo();


            }
        }

        private class MyWebViewClient extends WebViewClient {
            @SuppressWarnings("deprecation")
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                final Uri uri = Uri.parse(url);
                return true;
            }

            @TargetApi(Build.VERSION_CODES.N)
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                final Uri uri = request.getUrl();
                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                progressDialog.setMessage("لطفا کمی صبور باشید");
                progressDialog.setTitle("در حال بارگذاری صفحه");
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressDialog.show();
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                progressDialog.dismiss();
            }
        }
    }

    //==================================================================
    //              Hamrahpay Help Dialog
    //==================================================================
    public static class HamrahpayHelpDialogActivity extends Activity {
        private static HamrahPay hamrahPay;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.activity_dialog);
            setTitle(R.string.hamrahpay_title);


            Button hamrahpay_pay_btn = (Button) findViewById(R.id.hamrahpay_pay_btn);
            Button hamrahpay_help_btn = (Button) findViewById(R.id.hamrahpay_help_btn);
            Button hamrahpay_support_btn = (Button) findViewById(R.id.hamrahpay_support_btn);


            hamrahpay_pay_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    hamrahPay.InitializePayment(HamrahpayHelpDialogActivity.this);
                }
            });

            hamrahpay_help_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent myIntent = new Intent(HamrahpayHelpDialogActivity.this, HamrahpayDialogActivity.class);
                    HamrahpayDialogActivity.setClass(hamrahPay);
                    myIntent.putExtra("dialog_type", "help");
                    startActivity(myIntent);
                }
            });

            hamrahpay_support_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent myIntent = new Intent(HamrahpayHelpDialogActivity.this, HamrahpayDialogActivity.class);
                    HamrahpayDialogActivity.setClass(hamrahPay);
                    myIntent.putExtra("dialog_type", "support");
                    startActivity(myIntent);
                }
            });
        }

        //  Gets Properties From Outer Class
        private static void setClass(HamrahPay createdHamarahPay) {
            hamrahPay = createdHamarahPay;
        }


    }

    //==================================================================
    //                   Pay Activity As Inner Class                  //
    //==================================================================
    public static class PayActivity extends Activity {

        // Pay Page
        String PAY_PAGE = "https://hamrahpay.com/cart/app/pay_v2/";

        ProgressDialog progress;
        ProgressBar webview_progressBar;
        WebView wbvwBrowser;
        TextView urlBar;
        TextView urlTitleBar;
        TextView txtProtocol;
        View devider;
        static HamrahPay hamrahPay;

        String certificate_info = "";
        String currentUrl = "";

        //  Gets Properties From Outer Class
        private static void setClass(HamrahPay createdHamarahPay) {
            hamrahPay = createdHamarahPay;
        }


        Context context;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_pay);

            String payURL = PAY_PAGE + hamrahPay.getPayCode();
            if (false/*!isChromeCustomeTabDisabled*/) {

            } else {


                // Initializing Url View
                urlBar = (TextView) findViewById(R.id.txt_url);
                urlTitleBar = (TextView) findViewById(R.id.txt_url_title);
                txtProtocol = (TextView) findViewById(R.id.txt_protocol);
                webview_progressBar = (ProgressBar) findViewById(R.id.webview_progressBar);
                devider = (View) findViewById(R.id.divider);

                txtProtocol.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (txtProtocol.getText().toString().startsWith("https"))
                            Toast.makeText(PayActivity.this, certificate_info, Toast.LENGTH_LONG).show();
                        else
                            Toast.makeText(PayActivity.this, getString(R.string.security_alert), Toast.LENGTH_LONG).show();
                    }
                });

                urlBar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            AlertDialog.Builder builder;
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                builder = new AlertDialog.Builder(PayActivity.this, android.R.style.Theme_Material_Dialog_Alert);
                            } else {
                                builder = new AlertDialog.Builder(PayActivity.this);
                            }
                            builder.setTitle("اطلاعات صفحه")
                                    .setMessage("آدرس صفحه \n" + currentUrl + "\nاطلاعات SSL\n" + certificate_info)
                                    .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            // continue with delete
                                        }
                                    })
                                    .setIcon(R.drawable.more_vector)
                                    .show();
                        } catch (Exception e) {

                        }

                    }
                });
                // Injecting And Initializing WebView
                //String payURL = PAY_PAGE + hamrahPay.getPayCode();
                wbvwBrowser = (WebView) findViewById(R.id.wbvw_acPay);
                wbvwBrowser.clearCache(true);
                startWebView(payURL);
            }


        }

        private void changeUrl(String url) {
            boolean isHttps = url.startsWith("https://");

            String urlWithoutProtocol = url.replace("https", "").replace("http", "");
            urlBar.setText(urlWithoutProtocol);
            if (isHttps) {
                txtProtocol.setText("https");
                txtProtocol.setTextColor(Color.parseColor("#4ada3b"));
                txtProtocol.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_https, 0, 0, 0);
            } else {
                txtProtocol.setText("http");
                txtProtocol.setTextColor(Color.parseColor("#1AFFFFFF"));
                txtProtocol.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_http, 0, 0, 0);
            }
        }

        @Override
        protected void onActivityResult(int requestCode, int resultCode, Intent data) {
            super.onActivityResult(requestCode, resultCode, data);
            this.onDestroy();
        }

        @SuppressLint("SetJavaScriptEnabled")
        private void startWebView(final String payURL) {

            wbvwBrowser.setWebChromeClient(new WebChromeClient() {
                @Override
                public void onProgressChanged(WebView view, int newProgress) {
                    super.onProgressChanged(view, newProgress);
                    if (newProgress < 100) {
                        devider.setVisibility(View.GONE);
                        webview_progressBar.setVisibility(View.VISIBLE);
                        webview_progressBar.setProgress(newProgress);
                    } else {
                        webview_progressBar.setVisibility(View.GONE);
                        devider.setVisibility(View.VISIBLE);
                    }

                }
            });

            wbvwBrowser.setWebViewClient(new WebViewClient() {

                @Override
                public void onPageStarted(WebView view, String url, Bitmap favicon) {
                    //progress.show();
                    changeUrl(url);

                    super.onPageStarted(view, url, favicon);
                }

                @TargetApi(Build.VERSION_CODES.N)
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                    final Uri uri = request.getUrl();


                    changeUrl(uri.toString());
                    if (uri.toString().contains("exit_page")) {
                        onBackPressed();
                        return true;
                    }
                    view.loadUrl(uri.toString());
                    //urlBar.setText(uri.toString());
                    urlTitleBar.setText(view.getTitle());
                    currentUrl = uri.toString();
                    return super.shouldOverrideUrlLoading(view, request);
                }

                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    if (url.contains("exit_page")) {
                        onBackPressed();
                        return true;
                    }
                    view.loadUrl(url);
                    //urlBar.setText(url);
                    changeUrl(url);
                    urlTitleBar.setText(view.getTitle());
                    currentUrl = url;
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
                public void onPageFinished(WebView view, String url) {
                    //if (progress.isShowing()) {
                    //    progress.dismiss();
                    //}
                    //urlBar.setText(url);
                    changeUrl(url);
                    urlTitleBar.setText(view.getTitle());
                    currentUrl = url;
                    super.onPageFinished(view, url);
                    wbvwBrowser.setVisibility(View.GONE);
                    wbvwBrowser.setVisibility(View.VISIBLE);
                    try {
                        if (android.os.Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT_WATCH) {
                            certificate_info = getString(R.string.hamrahpay_secure);
                        } else {
                            certificate_info = view.getCertificate().toString();
                        }
                    } catch (NullPointerException e) {
                        certificate_info = getString(R.string.hamrahpay_secure);
                    } catch (Exception e) {
                        certificate_info = getString(R.string.hamrahpay_secure);
                    }
                }
            });

            // Do not disable this line , because it used when user will redirect to bank
            wbvwBrowser.getSettings().setDomStorageEnabled(true);
            wbvwBrowser.getSettings().setJavaScriptEnabled(true);
            wbvwBrowser.getSettings().setDefaultTextEncodingName("utf-8");
            wbvwBrowser.loadUrl(payURL);
        }

        @Override
        public void onBackPressed() {
            if (currentUrl.contains("shaparak.ir")) {
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case DialogInterface.BUTTON_POSITIVE:
                                PayActivity.this.finish();
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                //No button clicked
                                break;
                        }
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(PayActivity.this);
                builder.setMessage(getString(R.string.shaparak_alert)).setPositiveButton("بله", dialogClickListener)
                        .setNegativeButton("خیر", dialogClickListener).show();

            } else {
                PayActivity.this.onDestroy();
            }
        }

        @Override
        protected void onDestroy() {
            hamrahPay.verifyPayment(hamrahPay.getSku(), hamrahPay.getPayCode());
            super.onDestroy();
            super.finish();
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
