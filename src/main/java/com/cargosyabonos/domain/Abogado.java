package com.cargosyabonos.domain;

import java.util.List;

public class Abogado {
	
	private int idAbogado;
	
	private String firma;
	
	private String nombre;
	
	private String email;
	
	private String telefono;
	
	private String sinonimos;
	
	private String fechaCreacion;

	private boolean cupon;
	
	private String fechaCupon;

	private String estado;

	private String referencia;

	private List<EmailAbogado> emailsAbogado;

	public int getIdAbogado() {
		return idAbogado;
	}

	public void setIdAbogado(int idAbogado) {
		this.idAbogado = idAbogado;
	}

	public String getFirma() {
		return firma;
	}

	public void setFirma(String firma) {
		this.firma = firma;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public String getSinonimos() {
		return sinonimos;
	}

	public void setSinonimos(String sinonimos) {
		this.sinonimos = sinonimos;
	}

	public String getFechaCreacion() {
		return fechaCreacion;
	}

	public void setFechaCreacion(String fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}
	
	
	
	public List<EmailAbogado> getEmailsAbogado() {
		return emailsAbogado;
	}

	public void setEmailsAbogado(List<EmailAbogado> emailsAbogado) {
		this.emailsAbogado = emailsAbogado;
	}

	public boolean isCupon() {
		return cupon;
	}

	public void setCupon(boolean cupon) {
		this.cupon = cupon;
	}

	public String getFechaCupon() {
		return fechaCupon;
	}

	public void setFechaCupon(String fechaCupon) {
		this.fechaCupon = fechaCupon;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public String getReferencia() {
		return referencia;
	}

	public void setReferencia(String referencia) {
		this.referencia = referencia;
	}

	

}
