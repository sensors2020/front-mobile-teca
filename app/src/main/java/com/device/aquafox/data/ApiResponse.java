package com.device.aquafox.data;

import com.google.gson.annotations.SerializedName;

import retrofit2.http.Headers;

public class ApiResponse {
    //2020-04-27 05:54:50.201 21177-23655/com.device.aquafox D/OkHttp:
    // //{"statusCode":200,"headers":{"Access-Control-Allow-Origin":"*"},
    // "body":{"description":"","body":{"codigo":3,"usuario":"mpoma","nombre":"Martin","apellido":"Pomas","email":"mpoma@gmail.com",
    // "tipodoc":"DNI","numdoc":"43849503","telefono":"983456849","perfil":"A","token":"4Sn3qV1h3Or78APule8xR4vqM2NEV06ysjvWlAn5pB0=",
    // "fecha":1587984891492},   "error":null}        }

    @SerializedName("statusCode")
    private int statusCode;
    @SerializedName("body")
    private BodyResponse body;
    //@SerializedName("headers")
    //private Headers headers;


    public ApiResponse() {
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public BodyResponse getBody() {
        return body;
    }

    public void setBody(BodyResponse body) {
        this.body = body;
    }

}
