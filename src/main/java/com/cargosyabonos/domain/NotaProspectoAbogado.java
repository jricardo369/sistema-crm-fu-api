package com.cargosyabonos.domain;

public class NotaProspectoAbogado {

    private int idNota;

    private String descripcion;

    private String fechaCreacion;

    private String hora;

    private int idProspectoAbogado;

    private int idUsuario;

    private String nombreUsuario;

    public int getIdNota() {
        return idNota;
    }

    public void setIdNota(int idNota) {
        this.idNota = idNota;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(String fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public int getIdProspectoAbogado() {
        return idProspectoAbogado;
    }

    public void setIdProspectoAbogado(int idProspectoAbogado) {
        this.idProspectoAbogado = idProspectoAbogado;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    

}
