# hamrahpay_b4a
Hamrahpay Library for Basic4Android
## کتابخانه همراه پی برای بیسیک فور اندروید

## ویدئو آموزشی

[آپارات همراه پی](http://www.aparat.com/hamrahpay)


کتابخانه های مورد نیاز در بیسیک فور اندروید :
* HttpUtils2
* JSON
* Network
* Phone
* RandomAccessFile
* Reflection
* WebViewExtras2     - [Download](http://b4a.martinpearman.co.uk/webviewextras/)

پرمیژن مورد نیاز کتابخانه همراه پی :
```xml
AddManifestText(<uses-permission android:name="android.permission.GET_ACCOUNTS" />)
```

##کد نویسی دکمه پرداخت

```basic
Sub PayBtn_Click
  Dim product_sku As String
  product_sku = "hp_55202faf1ddd8901148214" ' این کد را به شناسه کالای خود تغییر دهید
  
  
	If hamrahpay.IsConnected=False Then
		ToastMessageShow("دستگاه شما به اینترنت متصل نمیباشد . لطفا از صحت اتصال به اینترنت اطمینان حاصل فرمایید.",True)
	Else
		hamrahpay.pay_request(product_sku,Me)
	End If
End Sub

Sub JobDone (Job As HttpJob)
	hamrahpay.JobDone(Job)
End Sub
```



### پشتیبانی

[![](https://hamrahpay.com/assets/home/theme/img/logo-red.png)](https://hamrahpay.com)

 هرگونه سوالی در رابطه با این کتابخانه را از بخش تیکت ها در پنل خود در سایت ما مطرح نمایید.
 
