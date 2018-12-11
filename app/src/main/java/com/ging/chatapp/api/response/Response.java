package com.ging.chatapp.api.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Response<Data> {

    @SerializedName("status")
    @Expose
    private Integer status;

    @SerializedName("message")
    @Expose
    private String message;

    @SerializedName("data")
    @Expose
    private Data data;

    public Integer getStatus() {
        return status != null ? status : 0;
    }

//    public void setStatus(Integer status) {
//        this.status = status;
//    }

    public String getMessage() {
        return message != null ? message : "";
    }

//    public void setMessage(String message) {
//        this.message = message;
//    }

    public Data getData() {
        return data;
    }

//    public void setData(Data data) {
//        this.data = data;
//    }
}
