package com.device.aquafox.data;

import java.util.List;

public class Dashboard {

    private List<Consumo> consumos;
    private Integer anio;
    private Integer rangoLitro;
    private Integer rangoM3;

    public Dashboard() {
    }

    public Integer getRangoLitro() {
        return rangoLitro;
    }

    public void setRangoLitro(Integer rangoLitro) {
        this.rangoLitro = rangoLitro;
    }

    public Integer getRangoM3() {
        return rangoM3;
    }

    public void setRangoM3(Integer rangoM3) {
        this.rangoM3 = rangoM3;
    }

    public List<Consumo> getConsumos() {
        return consumos;
    }

    public void setConsumos(List<Consumo> consumos) {
        this.consumos = consumos;
    }

    public Integer getAnio() {
        return anio;
    }

    public void setAnio(Integer anio) {
        this.anio = anio;
    }
}
