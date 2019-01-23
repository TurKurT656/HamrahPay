package ir.devage.hamrahpay;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Devage on 7/11/2017.
 */

public class SupportInfo {
    @SerializedName("error")
    private boolean error;

    @SerializedName("status")
    private String status;

    @SerializedName("message")
    private String  message;
    @SerializedName("email")
    private String  email;
    @SerializedName("mobile")
    private String  mobile;
    @SerializedName("phone")
    private String  phone;
    @SerializedName("telegram")
    private String  telegram;

    public SupportInfo(boolean error, String status, String message, String email, String mobile, String phone, String telegram) {
        this.error = error;
        this.status = status;
        this.message = message;
        this.email = email;
        this.mobile = mobile;
        this.phone = phone;
        this.telegram = telegram;
    }

    public SupportInfo() {
        this.error = false;
        this.status = null;
        this.message=null;
        this.email =null;
        this.mobile =null;
        this.phone=null;
        this.telegram=null;
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getTelegram() {
        return telegram;
    }

    public void setTelegram(String telegram) {
        this.telegram = telegram;
    }


    @Override
    public String toString() {
        return "SupportInfo{" +
                "error=" + error +
                ", status='" + status + '\'' +
                ", message='" + message + '\'' +
                ", email='" + email + '\'' +
                ", mobile='" + mobile + '\'' +
                ", phone='" + phone + '\'' +
                ", telegram='" + telegram + '\'' +
                '}';
    }


}
