package com.device.aquafox.data;

public class Consumo {

    private String id;
    private String mes;
    private String values;
    private String periodo;
    private Integer litros;
    private Integer monto;
    private String metro3;

    public Consumo() {
    }

    public Consumo(String id, String mes, String periodo, Integer litros, Integer monto) {
        this.id = id;
        this.mes = mes;
        this.periodo = periodo;
        this.litros = litros;
        this.monto = monto;
    }

    public String getValues() {
        return values;
    }

    public void setValues(String values) {
        this.values = values;
    }

    public String getMetro3() {
        return metro3;
    }

    public void setMetro3(String metro3) {
        this.metro3 = metro3;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMes() {
        return mes;
    }

    public void setMes(String mes) {
        this.mes = mes;
    }

    public String getPeriodo() {
        return periodo;
    }

    public void setPeriodo(String periodo) {
        this.periodo = periodo;
    }

    public Integer getLitros() {
        return litros;
    }

    public void setLitros(Integer litros) {
        this.litros = litros;
    }

    public Integer getMonto() {
        return monto;
    }

    public void setMonto(Integer monto) {
        this.monto = monto;
    }
}
