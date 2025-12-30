package com.cargosyabonos.domain;

import java.util.Date;
import java.util.List;

public class UsuarioObj {

	private int idUsuario;

	private String usuario;

	private String nombre;

	private String direccion;

	private String telefono;

	private String ciudad;
	
	private String pais;

	private String correoElectronico;

	private Date fechaCreacion;

	private String rol;

	private int intentos;

	private String estatus;
	
	private int edad;
	
	private String sexo;
	
	private String resumen;
	
	private boolean ausencia;
	
	private boolean revisor;
	
	private String licencia;
	
	private String licenciaValida;
	
	private String disponibilidad;
	
	private String rate;
	
	private String color;
	
	private String image;
	
	private String iniciales;
	
	private List<PermisoUsuario> permisos;
	
	private boolean withSupervision;
	
	private String supervisor;

	public int getIdUsuario() {
		return idUsuario;
	}

	public void setIdUsuario(int idUsuario) {
		this.idUsuario = idUsuario;
	}

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public String getCiudad() {
		return ciudad;
	}

	public void setCiudad(String ciudad) {
		this.ciudad = ciudad;
	}

	public String getCorreoElectronico() {
		return correoElectronico;
	}

	public void setCorreoElectronico(String correoElectronico) {
		this.correoElectronico = correoElectronico;
	}

	public String getRol() {
		return rol;
	}

	public void setRol(String rol) {
		this.rol = rol;
	}

	public int getIntentos() {
		return intentos;
	}

	public void setIntentos(int intentos) {
		this.intentos = intentos;
	}

	public String getEstatus() {
		return estatus;
	}

	public void setEstatus(String estatus) {
		this.estatus = estatus;
	}

	public Date getFechaCreacion() {
		return fechaCreacion;
	}

	public void setFechaCreacion(Date fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}

	public List<PermisoUsuario> getPermisos() {
		return permisos;
	}

	public void setPermisos(List<PermisoUsuario> permisos) {
		this.permisos = permisos;
	}

	public int getEdad() {
		return edad;
	}

	public void setEdad(int edad) {
		this.edad = edad;
	}

	public String getSexo() {
		return sexo;
	}

	public void setSexo(String sexo) {
		this.sexo = sexo;
	}

	public String getPais() {
		return pais;
	}

	public void setPais(String pais) {
		this.pais = pais;
	}

	public String getResumen() {
		return resumen;
	}

	public void setResumen(String resumen) {
		this.resumen = resumen;
	}

	public boolean isAusencia() {
		return ausencia;
	}

	public void setAusencia(boolean ausencia) {
		this.ausencia = ausencia;
	}

	public boolean isRevisor() {
		return revisor;
	}

	public void setRevisor(boolean revisor) {
		this.revisor = revisor;
	}

	public String getLicencia() {
		return licencia;
	}

	public void setLicencia(String licencia) {
		this.licencia = licencia;
	}

	public String getLicenciaValida() {
		return licenciaValida;
	}

	public void setLicenciaValida(String licenciaValida) {
		this.licenciaValida = licenciaValida;
	}

	public String getDisponibilidad() {
		return disponibilidad;
	}

	public void setDisponibilidad(String disponibilidad) {
		this.disponibilidad = disponibilidad;
	}

	public String getRate() {
		return rate;
	}

	public void setRate(String rate) {
		this.rate = rate;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getIniciales() {
		return iniciales;
	}

	public void setIniciales(String iniciales) {
		this.iniciales = iniciales;
	}

	public boolean isWithSupervision() {
		return withSupervision;
	}

	public void setWithSupervision(boolean withSupervision) {
		this.withSupervision = withSupervision;
	}

	public String getSupervisor() {
		return supervisor;
	}

	public void setSupervisor(String supervisor) {
		this.supervisor = supervisor;
	}

}
