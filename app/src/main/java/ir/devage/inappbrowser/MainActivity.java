package ir.devage.inappbrowser;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import ir.devage.hamrahpay.HamrahPay;
import ir.devage.hamrahpay.LastPurchase;
import ir.devage.hamrahpay.SupportInfo;

public class MainActivity extends AppCompatActivity {

    private HamrahPay hp=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //------- Start ------
        final String yourSKU = "hp_596c483885551620831476";   // شناسه کالای شما در سایت همراه پی
        hp= new HamrahPay(MainActivity.this)                // اکتیویتی که می خواهید از آنجا پرداخت انجام شود
                .sku(yourSKU)                               // اضافه کردن شناسه به صفحه پرداخت
                .setApplicationScheme("your_app_unique_id") // باید با مقدار دلخواه خود جایگزین نمایید.
                //.enableChromeCustomeTab()
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
                        Toast.makeText(MainActivity.this,"پرداخت موفقیت آمیز بوده است",Toast.LENGTH_SHORT).show();
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
                .startPayment(); // شروع عملیات پرداخت
        // ------ end ---------------
    }

    //----------------------------------------------------------------------------------------------
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if(intent.getScheme().equals("hamrahpay"))
        {
            hp.verifyPayment();
        }
    }
    //----------------------------------------------------------------------------------------------
}
