package com.cargosyabonos.domain;

public class OficinaAnioYMes {
    private int anio;
    private int mes;
    private int total;

    public OficinaAnioYMes() {}

    public OficinaAnioYMes(int anio, int mes, int total) {
        this.anio = anio;
        this.mes = mes;
        this.total = total;
    }

    public int getAnio() {
        return anio;
    }

    public void setAnio(int anio) {
        this.anio = anio;
    }

    public int getMes() {
        return mes;
    }

    public void setMes(int mes) {
        this.mes = mes;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
