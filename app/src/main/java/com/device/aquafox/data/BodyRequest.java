package com.device.aquafox.data;

public class BodyRequest <T>{

    private T body;

    public BodyRequest() {
    }

    public T getBody() {
        return body;
    }

    public void setBody(T body) {
        this.body = body;
    }
}
