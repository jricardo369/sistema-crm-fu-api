package com.cargosyabonos.domain;

public class FilesFirmaAbogadoObj {

	private int idSolicitud;
	private String email;
	private String cliente;
	private String abogado;
	private String emailAbogado;
	private String estado;
	private String firma;
	private int idAbogado;
	
	public int getIdSolicitud() {
		return idSolicitud;
	}
	public void setIdSolicitud(int idSolicitud) {
		this.idSolicitud = idSolicitud;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getCliente() {
		return cliente;
	}
	public void setCliente(String cliente) {
		this.cliente = cliente;
	}
	public String getAbogado() {
		return abogado;
	}
	public void setAbogado(String abogado) {
		this.abogado = abogado;
	}
	public String getEmailAbogado() {
		return emailAbogado;
	}
	public void setEmailAbogado(String emailAbogado) {
		this.emailAbogado = emailAbogado;
	}
	public String getEstado() {
		return estado;
	}
	public void setEstado(String estado) {
		this.estado = estado;
	}
	public String getFirma() {
		return firma;
	}
	public void setFirma(String firma) {
		this.firma = firma;
	}
	public int getIdAbogado() {
		return idAbogado;
	}
	public void setIdAbogado(int idAbogado) {
		this.idAbogado = idAbogado;
	}
	public FilesFirmaAbogadoObj(int idSolicitud, String email, String cliente, String abogado, String emailAbogado,String estado,String firma,int idAbogado) {
		super();
		this.idSolicitud = idSolicitud;
		this.email = email;
		this.cliente = cliente;
		this.abogado = abogado;
		this.emailAbogado = emailAbogado;
		this.estado = estado;
		this.firma = firma;
		this.idAbogado = idAbogado;
	}
	
}
