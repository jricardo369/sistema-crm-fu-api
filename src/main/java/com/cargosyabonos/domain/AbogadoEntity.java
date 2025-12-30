package com.cargosyabonos.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "abogado")
public class AbogadoEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_abogado")
	private int idAbogado;
	
	private String firma;
	
	private String nombre;
	
	private String telefono;
	
	private String sinonimos;
	
	@Column(name = "fecha_creacion", insertable = false, updatable = false)
	private String fechaCreacion;
	
	private boolean cupon;
	
	@Column(name = "fecha_cupon", insertable = false, updatable = false)
	private String fechaCupon;

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
	
	
}
