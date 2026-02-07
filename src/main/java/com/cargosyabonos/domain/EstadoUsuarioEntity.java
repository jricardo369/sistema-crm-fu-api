package com.cargosyabonos.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "estado_usuario")
public class EstadoUsuarioEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_est_us")
	private int idEstadoUsuario;

	@Column(name = "estado", length = 2)
	private String estado;

	@Column(name = "id_usuario")
	private int idUsuario;

	@Column(name = "no_licencia")
	private Integer noLicencia;

	public Integer getNoLicencia() {
		return noLicencia;
	}

	public void setNoLicencia(Integer noLicencia) {
		this.noLicencia = noLicencia;
	}

	public int getIdEstadoUsuario() {
		return idEstadoUsuario;
	}

	public void setIdEstadoUsuario(int idEstadoUsuario) {
		this.idEstadoUsuario = idEstadoUsuario;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public int getIdUsuario() {
		return idUsuario;
	}

	public void setIdUsuario(int idUsuario) {
		this.idUsuario = idUsuario;
	}

	

}
