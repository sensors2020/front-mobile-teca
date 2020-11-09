package com.device.aquafox.data;

public class Clave {

    private String claveActual;
    private String claveNueva;
    private String token;

    public Clave() {
    }

    public Clave(String claveActual, String claveNueva, String token) {
        this.claveActual = claveActual;
        this.claveNueva = claveNueva;
        this.token = token;
    }

    public String getClaveActual() {
        return claveActual;
    }

    public void setClaveActual(String claveActual) {
        this.claveActual = claveActual;
    }

    public String getClaveNueva() {
        return claveNueva;
    }

    public void setClaveNueva(String claveNueva) {
        this.claveNueva = claveNueva;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
