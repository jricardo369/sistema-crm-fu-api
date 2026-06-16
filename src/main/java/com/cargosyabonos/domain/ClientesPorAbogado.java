package com.cargosyabonos.domain;

public class ClientesPorAbogado {
    private String firma;
    private String email;
    private int total;

    public ClientesPorAbogado() {}

    public ClientesPorAbogado(String firma, String email, int total) {
        this.firma = firma;
        this.email = email;
        this.total = total;
    }

    public String getFirma() {
        return firma;
    }

    public void setFirma(String firma) {
        this.firma = firma;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
