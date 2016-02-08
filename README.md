# کتابخانه پرداخت درون برنامه ای همراه پی
[![Release](https://jitpack.io/v/turkurt656/hamrahpay.svg)](https://jitpack.io/#turkurt656/hamrahpay)

کتابخانه مخصوص اندروید استودیو



---
## نحوه اضافه کردن کتابخانه
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
فقط با اضافه کردن این دستورات، میتوانید برنامه خود را اجرا کنید
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
### پیکربندی نوع پرداخت
دو نوع پرداخت در همراه پی پشتیبانی میگردد:

1. پرداخت به ازای هر دستگاه :‌با این نوع پرداخت هر فردی که نرم افزار را خریداری میکند فقط بر روی همان دستگاهی که خریداری کرده است میتواند از نرم افزار استفاده نماید و پرداخت برای همان دستگاه قابل شناسایی میباشد.

2. پرداخت به ازای ایمیل :‌ در این نوع پرداخت مکانیزمی طراحی شده است که هر فردی که نرم افزار را خریداری میکند بتواند بر روی گوشی دیگری هم نصب کنم . به طور مثال با یک بار نرم افزار شما را روی گوشی و تبلت خود نصب نماید. این روش توسط گوگل پلی و دیگر مارکت ها استفاده میگردد.

برای اینکار به کد ساده کد زیر را اضافه کنید:
```java
.verificationType(HamrahPay.DEVICE_VERIFICATION)    // حالت اول
.verificationType(HamrahPay.EMAIL_VERIFICATION)     // حالت دوم - حالت پیشفرض
```

### تغییر رنگ نوار بالای صفحه پرداخت
```java
.pageTopColor(Color.parseColor("#F44336"))      // رنگ نوار
.pageTitleColor(Color.WHITE)        		// رنگ متن روی نوار
```

### کنترل خطا
در هنگام رویداد خطا میتوانید آنرا بصورت دستی کنترل کنید
```java
.listener(new HamrahPay.Listener() {
    @Override
    public void onErrorOccurred(String status, String message) {
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
    
    }
})
```

### کتابخانه Volley
اگر در برنامه خودتان از این کتابخانه استفاده می کنید، کافیست آنرا بصورت زیر به این کتابخانه اضافه کنید، در صورت اضافه نکردن برنامه بصورت پیش فرض برای خود صف جدیدی اضافه خواهد کرد
```java
RequestQueue myQueue = Volley.newRequestQueue(MainActivity.this)
new HamrahPay(MainActivity.this)
	...
	.requestQueue(myQueue)
	...
	.startPayment();
```

---
## پشتیبانی
لینک سایت همراه پی:

[![](https://hamrahpay.com/assets/home/theme/img/logo-red.png)](https://hamrahpay.com)

هرگونه سوالی در مورد کتابخانه دارید در قسمت مشکلات بپرسید.

<a href="https://github.com/TurKurT656/HamrahPay/issues" target="_blank">
لینک صفحه مشکلات
</a>
