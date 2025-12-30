package com.cargosyabonos.domain;

import java.util.Date;

/**
 * @author joser.vazquez
 *
 */
public class SolicitudVocEndingSessions {
	
	private int idSolicitud;
	private Date fechaInicio;
	private String cliente;
	private String telefono;
	private String email;
	private int idTerapeuta;
	private String nombreTerapeuta;
	private String emailTerapeuta;
	private int numSesiones;
	private int numSchedules;
	private int sesionesPendientes;
	
	public int getIdSolicitud() {
		return idSolicitud;
	}
	public void setIdSolicitud(int idSolicitud) {
		this.idSolicitud = idSolicitud;
	}
	public String getCliente() {
		return cliente;
	}
	public void setCliente(String cliente) {
		this.cliente = cliente;
	}
	public String getTelefono() {
		return telefono;
	}
	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public int getNumSesiones() {
		return numSesiones;
	}
	public void setNumSesiones(int numSesiones) {
		this.numSesiones = numSesiones;
	}
	public int getNumSchedules() {
		return numSchedules;
	}
	public void setNumSchedules(int numSchedules) {
		this.numSchedules = numSchedules;
	}
	public int getSesionesPendientes() {
		return sesionesPendientes;
	}
	public void setSesionesPendientes(int sesionesPendientes) {
		this.sesionesPendientes = sesionesPendientes;
	}

	public Date getFechaInicio() {
		return fechaInicio;
	}
	public void setFechaInicio(Date fechaInicio) {
		this.fechaInicio = fechaInicio;
	}
	public int getIdTerapeuta() {
		return idTerapeuta;
	}
	public void setIdTerapeuta(int idTerapeuta) {
		this.idTerapeuta = idTerapeuta;
	}
	public String getNombreTerapeuta() {
		return nombreTerapeuta;
	}
	public void setNombreTerapeuta(String nombreTerapeuta) {
		this.nombreTerapeuta = nombreTerapeuta;
	}
	public String getEmailTerapeuta() {
		return emailTerapeuta;
	}
	public void setEmailTerapeuta(String emailTerapeuta) {
		this.emailTerapeuta = emailTerapeuta;
	}
	
	
	
}
