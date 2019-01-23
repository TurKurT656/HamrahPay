package ir.devage.hamrahpayexample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import ir.devage.hamrahpay.HamrahPay;
import ir.devage.hamrahpay.LastPurchase;
import ir.devage.hamrahpay.SupportInfo;



public class MainActivity extends AppCompatActivity {


    //LastPurchase lastPurchase=null;
    String  Device_ID=null;
    String sku="hp_56e5d0f76ac62589589037";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Integer value=100;

        /*final String yourSKU = "hp_596c483885551620831476";   // شناسه کالای شما در سایت همراه پی
        new HamrahPay(MainActivity.this)                // اکتیویتی که می خواهید از آنجا پرداخت انجام شود
                .sku(yourSKU)                               // اضافه کردن شناسه به صفحه پرداخت
                .listener(new HamrahPay.Listener() {        // لیسنر برای آگاهی شما از موفق بودن یا نبودن پرداخت
                    @Override
                    public void onErrorOccurred(String status, String message) {
                        // مشکلی در پرداخت روی داده است یا کاربر پرداخت را انجام نداده است
                        Toast.makeText(MainActivity.this,message,Toast.LENGTH_SHORT).show();
                        Log.e("HamrahPay", status + ": " + message);
                    }

                    @Override
                    public void onPaymentSucceed(String payCode) {
                        // کاربر با موفقیت پرداخت را انجام داده است
                        Log.i("HamrahPay", "payCode: " + payCode);
                    }

                    @Override
                    public void onGetLastPurchaseInfo(LastPurchase lastPurchase) {

                    }

                    @Override
                    public void onGetSupportInfo(SupportInfo supportInfo) {

                    }

                    @Override
                    public void onGetDeviceID(String deviceID) {

                    }
                })
                .getLastPurchase()*/;

        final String yourSKU = "hp_596c483885551620831476";   // شناسه کالای شما در سایت همراه پی
        new HamrahPay(MainActivity.this)                // اکتیویتی که می خواهید از آنجا پرداخت انجام شود
                .sku(yourSKU)                               // اضافه کردن شناسه به صفحه پرداخت
                .listener(new HamrahPay.Listener() {        // لیسنر برای آگاهی شما از موفق بودن یا نبودن پرداخت
                    @Override
                    public void onErrorOccurred(String status, String message) {
                        // مشکلی در پرداخت روی داده است یا کاربر پرداخت را انجام نداده است
                        Toast.makeText(MainActivity.this,message,Toast.LENGTH_SHORT).show();
                        Log.e("HamrahPay", status + ": " + message);
                    }

                    @Override
                    public void onPaymentSucceed(String payCode) {
                        // کاربر با موفقیت پرداخت را انجام داده است
                        Log.i("HamrahPay", "payCode: " + payCode);
                    }

                    @Override
                    public void onGetLastPurchaseInfo(LastPurchase lastPurchase) {

                    }

                    @Override
                    public void onGetSupportInfo(SupportInfo supportInfo) {

                    }

                    @Override
                    public void onGetDeviceID(String deviceID) {

                    }
                })
                .setShouldShowHamrahpayDialog(false)
                .startPayment();
    }




}
