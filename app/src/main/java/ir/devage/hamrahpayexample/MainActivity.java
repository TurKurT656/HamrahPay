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

        /*final HamrahPay   hamrahPay  = new HamrahPay(MainActivity.this).sku("hp_56e5d0f76ac62589589037").setCustomDeviceID("867781020048929");
        Handler handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == 1) {
                    lastPurchase = hamrahPay.getLastPurchase();
                    Toast.makeText(MainActivity.this,lastPurchase.toString(),Toast.LENGTH_LONG).show();
                }
            }
        };
        hamrahPay.LastPurchaseRequest(sku,MainActivity.this,handler);
*/

        /*final HamrahPay   hamrahPay  = new HamrahPay(MainActivity.this);
        Handler handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == 1) {
                    Device_ID = HamrahPay.getDeviceID(MainActivity.this);
                    Toast.makeText(MainActivity.this,Device_ID,Toast.LENGTH_LONG).show();
                }
            }
        };
        hamrahPay.LastPurchaseRequest(sku,MainActivity.this,handler);
*/

       /* final HamrahPay   hamrahPay  = new HamrahPay(MainActivity.this);
        hamrahPay.showDeviceID(MainActivity.this);*/

        /*final HamrahPay   hamrahPay  = new HamrahPay(MainActivity.this);
        Handler handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == 1) {
                    SupportInfo supportInfo;
                    supportInfo = hamrahPay.getSupportInfo();
                    Toast.makeText(MainActivity.this,supportInfo.toString(),Toast.LENGTH_LONG).show();
                }
            }
        };
        hamrahPay.SupportInfoRequest(sku,MainActivity.this,handler);*/




        /*ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        Call<PayRequest> call = apiService.PayRequest("hp_58370e982fc06024865817","xxadasdsad866");
        call.enqueue(new Callback<PayRequest>() {
            @Override
            public void onResponse(Call<PayRequest>call, Response<PayRequest> response) {
                PayRequest payRequest = response.body();
                Toast.makeText(getApplicationContext(),payRequest.getMessage()+payRequest.getPayCode(),Toast.LENGTH_LONG).show();
                //Log.d(TAG, "Number of movies received: " + movies.size());
            }

            @Override
            public void onFailure(Call<PayRequest>call, Throwable t) {
               // Toast.makeText(getApplicationContext(),t.getMessage(),Toast.LENGTH_LONG).show();
                Toast.makeText(getApplicationContext(),"لطفا از صحت اتصال به اینترنت اطمینان حاصل فرمایید و مجددا تلاش کنید.",Toast.LENGTH_LONG).show();
                // Log error here since request failed
                //Log.e(TAG, t.toString());
            }
        });*/

        //Toast.makeText(this,getIPAddress(true),Toast.LENGTH_LONG).show();


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
