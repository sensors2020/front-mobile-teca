package com.device.aquafox.data;

import com.google.gson.annotations.SerializedName;

public class ApiRequest<T> {

    @SerializedName("body")
    private T body;

    public ApiRequest(T body) {
        this.body = body;
    }

    public ApiRequest() {
    }

    public T getBody() {
        return body;
    }

    public void setBody(T body) {
        this.body = body;
    }
}
