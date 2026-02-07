package com.cargosyabonos.adapter.out.mail;

import java.time.LocalDateTime;

public class EventoCalendario {
    private final String titulo;
    private final String descripcion;
    private final String ubicacion;
    private final LocalDateTime inicio;
    private final LocalDateTime fin;

    public EventoCalendario(String titulo,
            String descripcion,
            String ubicacion,
            LocalDateTime inicio,
            LocalDateTime fin) {
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.ubicacion = ubicacion;
        this.inicio = inicio;
        this.fin = fin;
    }

    public String titulo() {
        return titulo;
    }

    public String descripcion() {
        return descripcion;
    }

    public String ubicacion() {
        return ubicacion;
    }

    public LocalDateTime inicio() {
        return inicio;
    }

    public LocalDateTime fin() {
        return fin;
    }
}