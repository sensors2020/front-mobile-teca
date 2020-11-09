package com.device.aquafox.data;

public class ReportLineRequest {

    private String token;
    private String periodo;
    private String device;

    public ReportLineRequest() {
    }

    public ReportLineRequest(String token) {
        this.token = token;
    }

    public String getPeriodo() {
        return periodo;
    }

    public void setPeriodo(String periodo) {
        this.periodo = periodo;
    }

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
