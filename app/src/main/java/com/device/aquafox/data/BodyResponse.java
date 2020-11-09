package com.device.aquafox.data;

import com.google.gson.annotations.SerializedName;

public class BodyResponse {

    @SerializedName("body")
    private Object body;
    @SerializedName("description")
    private String description;
    @SerializedName("error")
    private Error error;

    //2020-04-27 05:54:50.201 21177-23655/com.device.aquafox D/OkHttp:
    // //{"statusCode":200,"headers":{"Access-Control-Allow-Origin":"*"},
    // "body":{"description":"","body":{"codigo":3,"usuario":"mpoma","nombre":"Martin","apellido":"Pomas","email":"mpoma@gmail.com",
    // "tipodoc":"DNI","numdoc":"43849503","telefono":"983456849","perfil":"A","token":"4Sn3qV1h3Or78APule8xR4vqM2NEV06ysjvWlAn5pB0=",
    // "fecha":1587984891492},   "error":null}        }

    public BodyResponse() {
    }

    public Object getBody() {
        return body;
    }

    public void setBody(Object body) {
        this.body = body;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Error getError() {
        return error;
    }

    public void setError(Error error) {
        this.error = error;
    }
}
