package ir.devage.hamrahpay.sample;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.android.volley.toolbox.Volley;

import ir.devage.hamrahpay.HamrahPay;

public class SampleActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample);

        Button payButton = (Button) findViewById(R.id.payButton);

        final String yourSKU = "hp_5415e384f37bf802917441";   // Your SKU

        if (HamrahPay.isPremium(this,yourSKU)) {        // Check If User Have Premium Key
            payButton.setEnabled(false);                // Disable Button
        }

        payButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new HamrahPay(SampleActivity.this)
                        .sku(yourSKU)       // Set SKU
                        .verificationType(HamrahPay.DEVICE_VERIFICATION)    // You Can Use HamrahPay.EMAIL_VERIFICATION
                        .pageTopColor(Color.parseColor("#2ecc71"))     // ActionBar Color Of PayActivity
                        .pageTitleColor(Color.WHITE)        // ActionBar Title Color Of PayActivity
                        .requestQueue(Volley.newRequestQueue(SampleActivity.this))  // Optional Method (Use If Have A Queue)
                        .listener(new HamrahPay.Listener() {
                            @Override
                            public void onErrorOccurred(String status, String message) {
                                // Handle Errors
                                switch (status) {
                                    case HamrahPay.STATUS_BAD_PARAMETERS:
                                        break;
                                    case HamrahPay.STATUS_INVALID_TRANSACTION:
                                        break;
                                    case HamrahPay.STATUS_NO_NETWORK_OR_SERVER:
                                        break;
                                    case HamrahPay.STATUS_SELLER_BLOCKED:
                                        break;
                                    case HamrahPay.STATUS_TRY_AGAIN:
                                        break;
                                }
                                Log.e("HamrahPay", status + ": " + message);
                            }

                            @Override
                            public void onPaymentSucceed(String payCode) {
                                // Save Your Payment Or Do After Payment Success
                                Toast.makeText(SampleActivity.this, "پرداخت با موفقیت انجام پذیرفت", Toast.LENGTH_SHORT).show();
                                Log.i("HamrahPay", "payCode: " + payCode);
                            }
                        })
                        .startPayment();    // Start Payment And Library Will Do The Rest ;)
            }
        });
    }
}
