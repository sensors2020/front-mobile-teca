package com.device.aquafox.data;

public class ConsumoReporte {

    private String alias;
    private float litros;
    private float metro3;
    private float monto;

    public ConsumoReporte() {
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public float getLitros() {
        return litros;
    }

    public void setLitros(float litros) {
        this.litros = litros;
    }

    public float getMetro3() {
        return metro3;
    }

    public void setMetro3(float metro3) {
        this.metro3 = metro3;
    }

    public float getMonto() {
        return monto;
    }

    public void setMonto(float monto) {
        this.monto = monto;
    }
}
