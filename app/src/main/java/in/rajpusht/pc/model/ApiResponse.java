package in.rajpusht.pc.model;

import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ApiResponse<T> {
//Json response: { data: {} /[] /null, message:””, status:true/false }


    public static final int NO_DATA_SYNC = -12;
    @SerializedName("data")
    @Expose
    private T data = null;

    @SerializedName("message")
    @Expose
    private String message;

    @SerializedName("status")
    @Expose
    private boolean status;

    private boolean internalError = false;
    private int internalErrorCode;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public boolean isInternalError() {
        return internalError;
    }

    public void setInternalError(boolean internalError) {
        this.internalError = internalError;
    }

    public int getInternalErrorCode() {
        return internalErrorCode;
    }

    public void setInternalErrorCode(int internalErrorCode) {
        this.internalErrorCode = internalErrorCode;
    }

    @NonNull
    @Override
    public String toString() {
        return "ApiResponse{" +
                "data=" + data +
                ", message='" + message + '\'' +
                ", status=" + status +
                ", internalError=" + internalError +
                ", internalErrorCode=" + internalErrorCode +
                '}';
    }
}