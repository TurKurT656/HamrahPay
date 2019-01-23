package ir.devage.hamrahpay;

/**
 * Created by Devage on 7/1/2017.
 */
import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;
import java.util.List;

public class PayRequest {

    @SerializedName("error")
    private boolean error;

    @SerializedName("status")
    private String status;

    @SerializedName("pay_code")
    private String payCode;

    @SerializedName("prd_status")
    private String productStatus;

    @SerializedName("prd_type")
    private String productType;

    @SerializedName("message")
    private String message;


    public PayRequest(boolean error,String status,String payCode,String productStatus,String productType)
    {
        this.error = error;
        this.status= status;
        this.payCode = payCode;
        this.productType = productType;
        this.productStatus=productStatus;
    }

    public PayRequest()
    {
        error=true;
        status=null;
        payCode=null;
        productStatus=null;
        productType=null;
        message=null;
    }

    public boolean getError() {
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

    public String getPayCode() {
        return payCode;
    }

    public void setPayCode(String payCode) {
        this.payCode = payCode;
    }

    public String getProductStatus() {
        return productStatus;
    }

    public void setProductStatus(String productStatus) {
        this.productStatus = productStatus;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "PayRequest{" +
                "error='" + error + '\'' +
                ", status='" + status + '\'' +
                ", payCode='" + payCode + '\'' +
                ", productStatus='" + productStatus + '\'' +
                ", productType='" + productType + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
