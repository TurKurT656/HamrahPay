package ir.devage.hamrahpay;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.telephony.TelephonyManager;

/**
 * Created by Devage on 7/8/2017.
 */

public class Analytics {

    public static String getAndroidVersionNumber()
    {
        return Integer.toString(android.os.Build.VERSION.SDK_INT);
    }
    public static String    getAndroidVersionName()
    {
        return Build.VERSION.RELEASE;
    }

    public static String    getAppVersionCode(Context context)
    {
        PackageManager manager = context.getPackageManager();
        PackageInfo info = null;
        String version = null;
        try {
            info = manager.getPackageInfo(context.getPackageName(), 0);
            version = Integer.toString(info.versionCode);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return version;
    }
    public static String getOperatorName(Context context)
    {
        TelephonyManager manager = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
        String carrierName = manager.getNetworkOperatorName();
        return carrierName;
    }

    public static int getNetworkType(Context context)
    {
        TelephonyManager manager = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
        return manager.getNetworkType();
    }

    public static String getNetworkTypeName(Context context)
    {
        ConnectivityManager conMan = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = conMan.getActiveNetworkInfo();
        if (activeNetwork != null) { // connected to the internet
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
                // connected to wifi
                return activeNetwork.getTypeName();
            } else if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
                // connected to the mobile provider's data plan
               return activeNetwork.getSubtypeName();
            }
        } else {
            return "UNKNOWN";
        }
        return "UNKNOWN";

    }

    public static String getDeviceBrand()
    {
        return Build.BRAND;
    }
    public static String getDeviceManufacturer()
    {
        if (Build.MANUFACTURER!=null)
        return Build.MANUFACTURER;
        else
            return Build.BOARD;
    }



}
