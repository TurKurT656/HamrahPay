package ir.devage.hamrahpay;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Devage on 7/4/2017.
 */

public class LastPurchase {

    @SerializedName("status")
    private String  status;

    @SerializedName("error")
    private String  error;

    @SerializedName("message")
    private String  message;

    @SerializedName("date_unix_timestamp")
    private String  unixTimestamp;

    @SerializedName("date")
    private String  date;

    @SerializedName("reserve_id")
    private String  payCode;

    @SerializedName("days_ago")
    private String  daysAgo;

    @SerializedName("current_date")
    private String  currentDate;



    public LastPurchase()
    {
        this.status=null;
        this.error= null;
        this.message=null;
        this.unixTimestamp=null;
        this.date = null;
        this.payCode=null;
    }

    public LastPurchase(String error, String message, String unixTimestamp, String date, String payCode) {
        this.error = error;
        this.message = message;
        this.unixTimestamp = unixTimestamp;
        this.date = date;
        this.payCode = payCode;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUnixTimestamp() {
        return unixTimestamp;
    }

    public void setUnixTimestamp(String unixTimestamp) {
        this.unixTimestamp = unixTimestamp;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPayCode() {
        return payCode;
    }

    public void setPayCode(String payCode) {
        this.payCode = payCode;
    }

    public String getDaysAgo() {
        return daysAgo;
    }

    public void setDaysAgo(String daysAgo) {
        this.daysAgo = daysAgo;
    }

    public String getCurrentDate() {
        return currentDate;
    }

    public void setCurrentDate(String currentDate) {
        this.currentDate = currentDate;
    }

    @Override
    public String toString() {
        return "LastPurchase{" +
                "status='" + status + '\'' +
                ", error='" + error + '\'' +
                ", message='" + message + '\'' +
                ", unixTimestamp='" + unixTimestamp + '\'' +
                ", date='" + date + '\'' +
                ", payCode='" + payCode + '\'' +
                ", daysAgo='" + daysAgo + '\'' +
                ", currentDate='" + currentDate + '\'' +
                '}';
    }
}
