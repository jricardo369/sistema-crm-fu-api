package com.cargosyabonos.domain;

public class EmailAbogado {

    private int idEmailAbo;
	private int idAbogado;
	private String email;
    private String nombre;
    private String tipo;
    
    public int getIdEmailAbo() {
        return idEmailAbo;
    }
    public void setIdEmailAbo(int idEmailAbo) {
        this.idEmailAbo = idEmailAbo;
    }
    public int getIdAbogado() {
        return idAbogado;
    }
    public void setIdAbogado(int idAbogado) {
        this.idAbogado = idAbogado;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public String getTipo() {
        return tipo;
    }
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    


}
