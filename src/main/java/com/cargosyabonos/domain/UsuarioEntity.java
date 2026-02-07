package com.cargosyabonos.domain;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonFormat;

@Entity
@Table(name = "usuario")
public class UsuarioEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_usuario")
	private int idUsuario;

	@Column(name = "usuario", length = 20)
	private String usuario;

	@Column(name = "contrasenia")
	private String contrasenia;

	@Column(name = "nombre", length = 150)
	private String nombre;

	@Column(name = "direccion", length = 200)
	private String direccion;

	@Column(name = "telefono", length = 20)
	private String telefono;

	@Column(name = "ciudad", length = 30)
	private String ciudad;
	
	@Column(name = "pais", length = 30)
	private String pais;

	@Column(name = "correo_electronico", length = 8)
	private String correoElectronico;

	@Column(name = "fecha_creacion", columnDefinition = "TIMESTAMP")
	@Temporal(TemporalType.TIMESTAMP)
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date fechaCreacion;

	@Column(name = "id_rol", length = 8)
	private String rol;

	@Column(name = "intentos")
	private int intentos;

	@Column(name = "estatus", length = 1)
	private String estatus;
	
	@Column(name = "edad", length = 1)
	private int edad;
	
	@Column(name = "sexo", length = 10)
	private String sexo;
	
	@Column(name = "resumen", length = 200)
	private String resumen;

	private boolean ausencia;
	
	private boolean revisor;
	
	@Column(name = "licencia", length = 20)
	private String licencia;
	
	@Column(name = "licencia_valida", length = 10)
	private String licenciaValida;
	
	@Column(name = "disponibilidad", length = 20)
	private String disponibilidad;
	
	private String rate;
	
	private String color;
	
	private String image;
	
	@Column(name = "with_supervision")
	private boolean withSupervision;
	
	private String supervisor;
	
	@Column(name = "unpaid_voc_flag")
	private boolean unpaidVocFlag;
	
	@Transient
	private List<PermisoUsuario> permisos;

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

	public String getContrasenia() {
		return contrasenia;
	}

	public void setContrasenia(String contrasenia) {
		this.contrasenia = contrasenia;
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
	
	public String getSupervisor() {
		return supervisor;
	}

	public void setSupervisor(String supervisor) {
		this.supervisor = supervisor;
	}

	public boolean isWithSupervision() {
		return withSupervision;
	}

	public void setWithSupervision(boolean withSupervision) {
		this.withSupervision = withSupervision;
	}

	public boolean isUnpaidVocFlag() {
		return unpaidVocFlag;
	}

	public void setUnpaidVocFlag(boolean unpaidVocFlag) {
		this.unpaidVocFlag = unpaidVocFlag;
	}


}
