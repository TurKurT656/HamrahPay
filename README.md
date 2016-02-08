# کتابخانه پرداخت درون برنامه ای همراه پی
[![Release](https://jitpack.io/v/turkurt656/hamrahpay.svg)](https://jitpack.io/#turkurt656/hamrahpay)

کتابخانه مخصوص اندروید استودیو

---
## نحوه اضافه کردن کنابخانه
برای استفاده از این کتابخانه ابتدا خط های زیر رو به قسمت زیر اضافه کنید:

`build.gradle`

نکته: دقت کنید که دوتا از این فایل ها موجود هست و شما باید او کدهای زیر را به فایلی که در زیرشاخه برنامه هست اضافه کنید

```gradle
  repositories {
		...
		maven { url "https://jitpack.io" }
	}
	
	dependencies {
    ...
    compile 'com.github.TurKurT656:Hamrahpay:1.0.0'
}
```
---
## نحوه استفاده از کتابخانه
کافیست کد زیر را در اکتیویتی یا سرویس مورد نظرتان اضافه کنید:

بعنوان مثال در رویداد کلیک دکمه پرداخت


### کد ساده
```java
  String yourSKU = "hp_56b24ebcdf274339534223";   // شناسه کالای شما در سایت همراه پی
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
      })
      .startPayment();    // با اضافه کردن این دستور عملیات پرداخت آغاز خواهد شد
```
```java
  String yourSKU = "hp_56b24ebcdf274339534223";   // شناسه کالای شما در سایت همراه پی
  new HamrahPay(SampleActivity.this)              // اکتیویتی که می خواهید از آنجا پرداخت انجام شود  
      .sku(yourSKU)                               // اضافه کردن شناسه به صفحه پرداخت
      .verificationType(HamrahPay.DEVICE_VERIFICATION)    // You Can Use HamrahPay.EMAIL_VERIFICATION
      .pageTopColor(Color.parseColor("#F44336"))     // ActionBar Color Of PayActivity
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
              Log.i("HamrahPay", "payCode: " + payCode);
          }
      })
      .startPayment();    // Start Payment And Library Will Do The Rest ;)
```
---
## پشتیبانی
لینک سایت همراه پی:

[![](https://hamrahpay.com/assets/home/theme/img/logo-red.png)](https://hamrahpay.com)

هرگونه سوالی در مورد کتابخانه دارید در قسمت مشکلات بپرسید.

<a href="https://github.com/TurKurT656/HamrahPay/issues" target="_blank">
لینک صفحه مشکلات
</a>
