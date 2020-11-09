package com.device.aquafox.data;

public class ServicesConsumo {

    private String serie;
    private String alias;
    private  Integer tarifa;
    private Integer litros;
    private Integer m3;

    public ServicesConsumo() {
    }

    public void setSerie(String serie) {
        this.serie = serie;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public void setTarifa(Integer tarifa) {
        this.tarifa = tarifa;
    }

    public void setLitros(Integer litros) {
        this.litros = litros;
    }

    public void setM3(Integer m3) {
        this.m3 = m3;
    }
}
