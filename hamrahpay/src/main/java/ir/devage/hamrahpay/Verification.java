package ir.devage.hamrahpay;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Devage on 7/4/2017.
 */

public class Verification {
    @SerializedName("error")
    private boolean error;

    @SerializedName("status")
    private String status;

    @SerializedName("message")
    private String message;


    public Verification()
    {
        this.error=true;
        this.status=null;
        this.message=null;
    }

    public Verification(boolean error, String status, String message) {
        this.error = error;
        this.status = status;
        this.message = message;
    }

    public boolean getError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Verification{" +
                "error=" + error +
                ", status='" + status + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
