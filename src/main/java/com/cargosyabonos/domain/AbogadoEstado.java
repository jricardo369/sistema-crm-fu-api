package com.cargosyabonos.domain;

public class AbogadoEstado {
    private String estado;
    private int numeroSolicitudes;

    public AbogadoEstado() {
    }

    public AbogadoEstado(String estado, int numeroSolicitudes) {
        this.estado = estado;
        this.numeroSolicitudes = numeroSolicitudes;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public int getNumeroSolicitudes() {
        return numeroSolicitudes;
    }

    public void setNumeroSolicitudes(int numeroSolicitudes) {
        this.numeroSolicitudes = numeroSolicitudes;
    }
}
