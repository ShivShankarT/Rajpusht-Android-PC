package in.rajpusht.pc.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ApiResponse<T> {

    @SerializedName("error")
    @Expose
    private boolean error;
    @SerializedName("code")
    @Expose
    private int code;

    @SerializedName("data")
    @Expose
    private T data = null;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("_ts")
    @Expose
    private long ts;


    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

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

    public long getTs() {
        return ts;
    }

    public void setTs(long ts) {
        this.ts = ts;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}