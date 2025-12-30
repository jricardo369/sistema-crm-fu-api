package com.cargosyabonos.domain;

public class CitaInterview {

    private int solicitud;
    private String fecha;
    private String hora;
    private String tipo;
    private int usuario;
    private String nombreUsuario;
    private String color;
    private boolean finSchedule;
    private String descEst;
    private int estSol;
    private String importante;

    public int getSolicitud() {
        return solicitud;
    }

    public void setSolicitud(int solicitud) {
        this.solicitud = solicitud;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public int getUsuario() {
        return usuario;
    }

    public void setUsuario(int usuario) {
        this.usuario = usuario;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public boolean isFinSchedule() {
        return finSchedule;
    }

    public void setFinSchedule(boolean finSchedule) {
        this.finSchedule = finSchedule;
    }

    public String getDescEst() {
        return descEst;
    }

    public void setDescEst(String descEst) {
        this.descEst = descEst;
    }

    public int getEstSol() {
        return estSol;
    }

    public void setEstSol(int estSol) {
        this.estSol = estSol;
    }

    public String getImportante() {
        return importante;
    }

    public void setImportante(String importante) {
        this.importante = importante;
    }


}
